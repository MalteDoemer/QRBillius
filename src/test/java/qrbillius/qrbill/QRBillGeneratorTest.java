package qrbillius.qrbill;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class QRBillGeneratorTest {

    @Test
    void parsePaymentAmount() {
        assertAll(
                () -> assertEquals(BigDecimal.valueOf(10), QRBillGenerator.parsePaymentAmount("10")),
                () -> assertEquals(BigDecimal.valueOf(1, 2), QRBillGenerator.parsePaymentAmount("0.01")),
                () -> assertEquals(BigDecimal.valueOf(99999999999L, 2), QRBillGenerator.parsePaymentAmount("999999999.99")),

                () -> assertEquals(BigDecimal.valueOf(10), QRBillGenerator.parsePaymentAmount("Fr. 10")),
                () -> assertEquals(BigDecimal.valueOf(1, 2), QRBillGenerator.parsePaymentAmount("Fr. 0.01")),
                () -> assertEquals(BigDecimal.valueOf(99999999999L, 2), QRBillGenerator.parsePaymentAmount("Fr. 999999999.99")),

                () -> assertEquals(BigDecimal.valueOf(10), QRBillGenerator.parsePaymentAmount("CHF 10")),
                () -> assertEquals(BigDecimal.valueOf(1, 2), QRBillGenerator.parsePaymentAmount("CHF 0.01")),
                () -> assertEquals(BigDecimal.valueOf(99999999999L, 2), QRBillGenerator.parsePaymentAmount("CHF 999999999.99")),

                () -> assertNull(QRBillGenerator.parsePaymentAmount(null)),
                () -> assertNull(QRBillGenerator.parsePaymentAmount("")),
                () -> assertNull(QRBillGenerator.parsePaymentAmount("       \n")),


                () -> assertThrows(NumberFormatException.class, () -> QRBillGenerator.parsePaymentAmount("hi")),
                () -> assertThrows(NumberFormatException.class, () -> QRBillGenerator.parsePaymentAmount("Fr."))
        );
    }
}
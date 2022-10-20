package qrbillius.qrbill;

import net.codecrete.qrbill.generator.Address;
import net.codecrete.qrbill.generator.Language;

public record QRBillConfig(String account, Address address, Language language) {
}

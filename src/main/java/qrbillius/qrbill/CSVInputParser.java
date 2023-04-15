package qrbillius.qrbill;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import qrbillius.io.UnicodeBOMInputStream;

import java.io.*;
import java.util.Iterator;
import java.util.List;

public class CSVInputParser implements InputParser {
    private final CSVParser csvParser;

    public CSVInputParser(CSVParser csvParser) {
        this.csvParser = csvParser;
    }

    public static CSVInputParser create(File file, String csvSeparator) throws IOException {

        var format = CSVFormat
                .EXCEL
                .builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setDelimiter(csvSeparator)
                .build();

        var reader = new InputStreamReader(new UnicodeBOMInputStream(new FileInputStream(file)).skipBOM());
        return new CSVInputParser(format.parse(reader));
    }


    @Override
    public long getCurrentLineNumber() {
        return csvParser.getCurrentLineNumber();
    }

    @Override
    public Iterator<List<String>> iterator() {
        return csvParser.stream().map(CSVRecord::toList).iterator();
    }

    @Override
    public void close() throws IOException {
        csvParser.close();
    }
}

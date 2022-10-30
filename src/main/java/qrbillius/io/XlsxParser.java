package qrbillius.io;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;
import org.xlsx4j.exceptions.Xlsx4jException;
import org.xlsx4j.sml.CTRst;
import org.xlsx4j.sml.STCellType;
import org.xlsx4j.sml.Worksheet;

import java.io.*;
import java.util.*;

public class XlsxParser implements Iterable<XlsxRecord> {
    private final XlsxFormat format;

    private final Iterator<List<String>> records;

    private final Map<String, Integer> header;

    public XlsxParser(InputStream stream, XlsxFormat format) throws Xlsx4jException {
        try (stream) {
            var xlsxPackage = SpreadsheetMLPackage.load(stream);
            var workbookPart = xlsxPackage.getWorkbookPart();
            var worksheet = workbookPart.getWorksheet(format.getWorksheet()).getContents();
            var sharedStrings = workbookPart.getSharedStrings().getContents().getSi();

            this.records = new RecordIterable(worksheet, sharedStrings).iterator();
            this.format = format;
            this.header = createHeader();
        } catch (Docx4JException | IOException e) {
            throw new Xlsx4jException(e.getMessage(), e);
        }
    }

    /**
     * Creates the header map from either the format or the first record and skips the first record if wanted.
     *
     * @return The header map or null if no mapping exists
     */
    private Map<String, Integer> createHeader() {
        var formatHeader = this.format.getHeader();

        Map<String, Integer> headerMap = null;

        if (formatHeader != null) {

            List<String> header;

            if (formatHeader.length == 0) {
                // parse header from first record
                header = nextRecord();
            } else {
                // use the provided header from the format
                header = List.of(formatHeader);

                // skip the first record if wanted
                if (format.skipHeaderRecord()) {
                    nextRecord();
                }
            }

            if (header != null) {
                headerMap = new HashMap<>();
                for (int i = 0; i < header.size(); i++) {
                    headerMap.put(header.get(i), i);
                }
            }
        }

        return headerMap;
    }

    private List<String> nextRecord() {
        if (records.hasNext()) {
            return records.next();
        } else {
            return null;
        }
    }

    @Override
    public Iterator<XlsxRecord> iterator() {
        return new XlsxRecordIterator();
    }

    private class XlsxRecordIterator implements Iterator<XlsxRecord> {
        @Override
        public boolean hasNext() {
            return XlsxParser.this.records.hasNext();
        }

        @Override
        public XlsxRecord next() {
            var record = XlsxParser.this.records.next();
            return new XlsxRecord(XlsxParser.this.header, record);
        }
    }

    private record RecordIterable(Worksheet worksheet, List<CTRst> sharedStrings) implements Iterable<List<String>> {

        @Override
        public Iterator<List<String>> iterator() {
            var rows = worksheet.getSheetData().getRow().stream().map(row -> {
                List<String> cells = new ArrayList<>();
                for (var c : row.getC()) {
                    var type = c.getT();
                    if (type == STCellType.S) {
                        // TODO: handle index out of bounds maybe?
                        var index = Integer.parseInt(c.getV());
                        cells.add(sharedStrings.get(index).getT().getValue());
                    } else {
                        cells.add(c.getV());
                    }
                }

                return cells;
            });

            return rows.iterator();
        }
    }
}

package qrbillius.qrbill;

import ch.rabanti.nanoxlsx4j.Workbook;
import ch.rabanti.nanoxlsx4j.Worksheet;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class XLSXInputParser implements InputParser {

    private final Workbook workbook;

    private RowIterator rowIterator;

    private XLSXInputParser(Workbook workbook) {
        this.workbook = workbook;
    }

    public static InputParser create(File file) throws Exception {
        var workbook = Workbook.load(new FileInputStream(file));
        return new XLSXInputParser(workbook);
    }

    @Override
    public long getCurrentLineNumber() {
        if (rowIterator != null) {
            return rowIterator.startRow + rowIterator.currentRow;
        } else {
            return -1;
        }
    }

    @Override
    public void close() throws Exception {
    }

    @Override
    public Iterator<List<String>> iterator() {
        // TODO: figure out what the correct worksheet is or let the user select one
        var sheet = workbook.getWorksheet(0);

        var startRow = sheet.getFirstDataRowNumber();
        var endRow = sheet.getLastDataRowNumber();
        var totalRows = endRow - startRow;

        var iterator = new RowIterator(totalRows, startRow, sheet);
        this.rowIterator = iterator;

        return iterator;
    }

    private static class RowIterator implements Iterator<List<String>> {

        private final int totalRows;

        private final int startRow;
        private final Worksheet sheet;

        private int currentRow = 0;

        public RowIterator(int totalRows, int startRow, Worksheet sheet) {
            this.totalRows = totalRows;
            this.startRow = startRow + 1;
            this.sheet = sheet;

        }


        @Override
        public boolean hasNext() {
            return currentRow < totalRows;
        }

        @Override
        public List<String> next() {
            var row = sheet.getRow(startRow + currentRow);
            currentRow++;

            List<String> result = new ArrayList<>();
            row.forEach(cell -> result.add(cell.getValue().toString()));
            return result;
        }
    }


}

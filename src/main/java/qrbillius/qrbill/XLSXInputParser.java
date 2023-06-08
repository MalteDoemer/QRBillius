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

    private final String selectedWorksheet;

    private RowIterator rowIterator;

    private XLSXInputParser(Workbook workbook, String selectedWorksheet) {
        this.workbook = workbook;
        this.selectedWorksheet = selectedWorksheet;
    }

    public static InputParser create(File file, String selectedWorksheet) throws Exception {
        var workbook = Workbook.load(new FileInputStream(file));
        return new XLSXInputParser(workbook, selectedWorksheet);
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
        Worksheet sheet;

        if (selectedWorksheet != null) {
            sheet = workbook.getWorksheet(selectedWorksheet);
        } else {
            sheet = workbook.getCurrentWorksheet();
        }

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
            row.forEach(cell -> {
                var value = cell.getValue();
                var text = value == null ? "" : value.toString();
                result.add(text);
            });

            // TODO: if every cell is empty or whitespace only we could theoretically skip it.
            if (result.stream().allMatch(String::isBlank) && hasNext()) {
                System.out.printf("Note: row number %d is empty and could be skipped.", currentRow);
            }

            return result;
        }
    }
}

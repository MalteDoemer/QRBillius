package qrbillius.qrbill;

import ch.rabanti.nanoxlsx4j.Address;
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

        var startColumn = sheet.getFirstDataColumnNumber();
        var endColumn = sheet.getLastDataColumnNumber();

        var iterator = new RowIterator(startRow, endRow, startColumn, endColumn, sheet);
        this.rowIterator = iterator;

        return iterator;
    }

    private static class RowIterator implements Iterator<List<String>> {

        private final int startRow;
        private final int endRow;

        private final int startColumn;
        private final int endColumn;

        private final Worksheet sheet;

        private int currentRow;

        public RowIterator(int startRow, int endRow, int startColumn, int endColumn, Worksheet sheet) {
            this.startRow = startRow;
            this.endRow = endRow;
            this.startColumn = startColumn;
            this.endColumn = endColumn;
            // Note: the first row is automatically skipped
            this.currentRow = startRow + 1;
            this.sheet = sheet;
        }


        @Override
        public boolean hasNext() {
            return currentRow <= endRow;
        }

        @Override
        public List<String> next() {
            var allCells = sheet.getCells();
            List<String> result = new ArrayList<>();

            for (int column = startColumn; column <= endColumn; column++) {
                var address = new Address(column, currentRow).getAddress();
                if (allCells.containsKey(address)) {
                    // we have a cell that might contain data.
                    var cell = allCells.get(address);
                    var value = cell.getValue();
                    var text = value == null ? "" : value.toString();
                    result.add(text);
                } else {
                    // this is the same as if the cell is empty.
                    result.add("");
                }
            }

            // increment the current row for the next iteration
            currentRow++;

            // TODO: if every cell is empty or whitespace only we could theoretically skip it.
            if (result.stream().allMatch(String::isBlank) && hasNext()) {
                System.out.printf("Note: row number %d is empty and could be skipped.", currentRow);
            }

            return result;
        }
    }
}

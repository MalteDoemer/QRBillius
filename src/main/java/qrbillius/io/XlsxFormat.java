package qrbillius.io;

import org.xlsx4j.exceptions.Xlsx4jException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class XlsxFormat {
    private final int worksheet;

    private final boolean skipHeaderRecord;

    private final String[] header;

    public static final XlsxFormat DEFAULT = new XlsxFormat(0, false, null);

    private XlsxFormat(int worksheet, boolean skipHeaderRecord, String[] header) {
        this.worksheet = worksheet;
        this.skipHeaderRecord = skipHeaderRecord;
        this.header = header;
    }

    private XlsxFormat(Builder builder) {
        this.worksheet = builder.worksheet;
        this.skipHeaderRecord = builder.skipHeaderRecord;
        this.header = builder.header;
    }

    public Builder builder() {
        return new Builder(this);
    }

    public int getWorksheet() {
        return worksheet;
    }

    public boolean skipHeaderRecord() {
        return skipHeaderRecord;
    }

    public XlsxParser parse(File file) throws Xlsx4jException, IOException {
        return new XlsxParser(new FileInputStream(file), this);
    }

    public XlsxParser parse(InputStream stream) throws Xlsx4jException {
        return new XlsxParser(stream, this);
    }

    public String[] getHeader() {
        return header;
    }

    public static class Builder {
        private int worksheet;

        private boolean skipHeaderRecord;

        private String[] header;

        private Builder(XlsxFormat format) {
            this.worksheet = format.worksheet;
            this.skipHeaderRecord = format.skipHeaderRecord;
            this.header = format.header;
        }

        public Builder setWorksheet(int worksheet) {
            this.worksheet = worksheet;
            return this;
        }

        public Builder setHeader(String... header) {
            this.header = header;
            return this;
        }

        public Builder setSkipHeaderRecord(boolean skipHeaderRecord) {
            this.skipHeaderRecord = skipHeaderRecord;
            return this;
        }

        public XlsxFormat build() {
            return new XlsxFormat(this);
        }
    }
}

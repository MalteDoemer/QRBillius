package qrbillius.config;

public record ExportConfiguration(boolean openPDFWhenFinished, boolean enablePDFTemplate, String pdfTemplate) {
}

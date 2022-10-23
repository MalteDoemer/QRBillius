package qrbillius.io;

public enum FileExtensions {
    PDF,
    DOCX,
    CSV,
    XLSX,
    UNSUPPORTED;

    public static FileExtensions parse(String extension) {
        if (extension.compareToIgnoreCase("pdf") == 0)
            return PDF;
        else if (extension.compareToIgnoreCase("docx") == 0)
            return DOCX;
        else if (extension.compareToIgnoreCase("csv") == 0)
            return CSV;
        else if (extension.compareToIgnoreCase("xlsx") == 0)
            return XLSX;
        else
            return UNSUPPORTED;
    }
}

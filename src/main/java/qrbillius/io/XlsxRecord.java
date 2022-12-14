package qrbillius.io;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XlsxRecord {
    private final Map<String, Integer> header;
    private final List<String> cells;

    public XlsxRecord(Map<String, Integer> header, List<String> cells) {
        this.header = header;
        this.cells = cells;
    }

    public XlsxRecord(List<String> cells) {
        this.header = null;
        this.cells = cells;
    }

    public String get(int index) {
        return cells().get(index);
    }

    public String get(String name) {
        if (header == null) {
            throw new IllegalStateException("No header mapping was provided, the record cannot be accessed by name");
        } else {
            var index = header.get(name);

            if (index == null) {
                throw new IllegalArgumentException(String.format("Mapping for header '%s' not found, expected one of %s", name, header.keySet()));
            }

            if (index >= cells().size()) {
                throw new IllegalArgumentException(String.format("Index for header '%s' is %d but this record only has %d values", name, index, cells().size()));
            }

            return cells().get(index);
        }
    }

    public boolean isMapped(String name) {
        return header != null && header.containsKey(name);
    }

    public <M extends Map<String, String>> M putIn(final M map) {
        if (header == null) {
            return map;
        }
        header.forEach((key, value) -> {
            final int col = value;
            if (col < cells.size()) {
                map.put(key, cells.get(col));
            }
        });
        return map;
    }

    public List<String> cells() {
        return cells;
    }

    public List<String> toList() {
        return cells();
    }

    public Map<String, String> toMap() {
        return putIn(new HashMap<>(cells.size()));
    }
}

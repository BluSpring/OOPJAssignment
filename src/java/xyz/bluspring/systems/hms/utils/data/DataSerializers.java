package xyz.bluspring.systems.hms.utils.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DataSerializers {
    private static final Path DATA_PATH = Path.of("data");

    public static File getPath(String name) {
        if (!Files.exists(DATA_PATH)) {
            try {
                Files.createDirectories(DATA_PATH);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return DATA_PATH.resolve(name).toFile();
    }

    public static <T extends DataSerializable<T>> void serializeValues(File file, List<T> list) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            var lines = new ArrayList<String>();

            for (T value : list) {
                lines.add(value.getSerializer().serialize(value));
            }

            try (FileOutputStream stream = new FileOutputStream(file)) {
                try (OutputStreamWriter streamWriter = new OutputStreamWriter(stream, StandardCharsets.UTF_8)) {
                    for (String line : lines) {
                        streamWriter.write(line);
                        streamWriter.write('\n');
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> void serializeValues(DataSerializer<T> serializer, File file, List<T> list) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            var lines = new ArrayList<String>();

            for (T value : list) {
                lines.add(serializer.serialize(value));
            }

            try (FileOutputStream stream = new FileOutputStream(file)) {
                try (OutputStreamWriter streamWriter = new OutputStreamWriter(stream, StandardCharsets.UTF_8)) {
                    for (String line : lines) {
                        streamWriter.write(line);
                        streamWriter.write('\n');
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> void serializeValue(DataSerializer<T> serializer, File file, T value) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            try (FileOutputStream stream = new FileOutputStream(file)) {
                try (OutputStreamWriter streamWriter = new OutputStreamWriter(stream, StandardCharsets.UTF_8)) {
                    streamWriter.write(serializer.serialize(value));
                    streamWriter.write('\n');
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> void deserializeLines(DataSerializer<T> serializer, File file, List<T> list) {
        try {
            if (file.exists()) {
                var lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

                for (String line : lines) {
                    list.add(serializer.deserialize(line));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T deserializeFile(DataSerializer<T> serializer, File file) {
        try {
            if (file.exists()) {
                var lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

                return serializer.deserialize(lines.getFirst());
            }

            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final String SEGMENT_DELIMITER = ", ";
    private static final char SEGMENT_CONTAINER = '"';

    // Allows reading lines in a format of "segment 1""segment2" without worrying about any characters causing breakage to the system.
    // Additionally, almost any character can be within these segments, allowing for delimiters.
    public static List<String> readSegmentedLine(String line) {
        var segments = new ArrayList<String>();
        var stringBuilder = new StringBuilder();

        boolean isInSegment = false;
        boolean isInEscape = false;
        for (var i = 0; i < line.length(); i++) {
            var c = line.charAt(i);

            if (c == '\\' && !isInEscape) {
                isInEscape = true;
            } else if (isInEscape) {
                if (isInSegment) {
                    stringBuilder.append(c);
                    isInEscape = false;
                }
            } else if (c == SEGMENT_CONTAINER) {
                if (isInSegment) {
                    isInSegment = false;
                    segments.add(stringBuilder.toString());
                    stringBuilder = new StringBuilder();
                } else {
                    isInSegment = true;
                }
            } else if (isInSegment) {
                stringBuilder.append(c);
            }
        }

        return segments;
    }

    public static String writeSegmentedLine(List<String> segments) {
        var stringBuilder = new StringBuilder();

        for (var i = 0; i < segments.size(); i++) {
            var segment = segments.get(i);
            stringBuilder.append(SEGMENT_CONTAINER);

            stringBuilder.append(
                segment
                    .replace("\\", "\\\\") // Ensure that backslashes aren't used to escape anything themselves.
                    .replace("" + SEGMENT_CONTAINER, "\\" + SEGMENT_CONTAINER)
            );

            stringBuilder.append(SEGMENT_CONTAINER);

            // Add delimiter unless this is the last segment.
            if (i < segments.size() - 1) {
                stringBuilder.append(SEGMENT_DELIMITER);
            }
        }

        return stringBuilder.toString();
    }
}

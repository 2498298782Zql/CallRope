package zql.CallRope.spi.util;

import java.io.*;
import java.util.Set;
import java.util.stream.Collectors;

public class StreamUtil {

    /**
     * 读取输入流中的所有行，并将它们存储在一个集合中返回。
     *
     * @param inputStream 输入流
     * @return 包含所有行的集合
     * @throws IOException 如果在读取过程中发生 I/O 错误
     */
    public static Set<String> readAllLines(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines().collect(Collectors.toSet());
        }
    }

    /**
     * 将集合中的所有行写入输出流。
     *
     * @param lines        要写入的行集合
     * @param outputStream 输出流
     * @throws IOException 如果在写入过程中发生 I/O 错误
     */
    public static void write(Set<String> lines, OutputStream outputStream) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
}


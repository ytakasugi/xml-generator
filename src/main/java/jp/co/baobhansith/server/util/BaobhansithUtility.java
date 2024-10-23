package jp.co.baobhansith.server.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;

public class BaobhansithUtility {
    /**
     * 指定されたCSVファイルを読み込み、指定されたキーに一致する行を返却するメソッド
     * 
     * @param filePath
     * @param key
     * @return
     * @throws IOException
     */
    public static String[] getRowByKey(String filePath, String key) throws IOException {
        Path path = Paths.get(filePath);

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                // CSVの行をカンマで分割
                String[] columns = line.split(",", -1);

                // 最初のカラムがキーと一致するか確認
                if (columns[0].equals(key)) {
                    return columns;
                }
            }
        }
        return null;
    }

    /**
     * 指定されたCSVファイルを読み込み、指定されたキーに一致する値を返却するメソッド
     * 
     * @param filePath
     * @param key
     * @param index
     * @return
     * @throws IOException
     */
    public static String getValueByKey(String filePath, String key, int index) throws IOException {
        Path path = Paths.get(filePath);

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                // CSVの行をカンマで分割
                String[] columns = line.split(",", -1);

                // 最初のカラムがキーと一致するか確認
                if (columns[0].equals(key)) {
                    return columns[index];
                }
            }
        }
        return null;
    }

    /**
     * ディレクトリを取得するメソッド
     * 
     * @param record
     * @return
     */
    public static String getDirectroy(String[] record) {
        Optional<String> value = Arrays.stream(record)
                .filter(v -> StringUtils.isNotEmpty(v))
                .findFirst();

        return value.orElse(StringUtils.EMPTY);
    }

    /**
     * 
     * @param directory
     * @return
     */
    public static boolean isCheckDirectory(Path directory) {
        if (Files.exists(directory) && Files.isDirectory(directory)) {
            return true;
        }
        return false;
    }

    /**
     * ディレクトリ配列を取得するメソッド
     * 
     * @param record
     * @param indexArray
     * @return
     */
    public static String[] getDirectoryArray(String[] record, int[] indexArray) {
        return IntStream.of(indexArray)
                .mapToObj(index -> record[index])
                .toArray(String[]::new);
    }

    /**
     * ディレクトリ配列をPath配列に変換するメソッド
     * 
     * @param directoryArray
     * @return
     * @throws IOException
     */
    public static Path[] convertToPathArray(String[] directoryArray) {
        return Arrays.stream(directoryArray)
                .map(Paths::get)
                .toArray(Path[]::new);

    }

    /**
     * ディレクトリが存在するかどうかを判定するメソッド
     * 
     * @param directoryArray
     * @return
     */
    public static boolean isExistsDirectoryArray(Path[] directoryArray) {
        return Arrays.stream(directoryArray)
                .allMatch(path -> Files.exists(path));
    }

    /**
     * ディレクトリかどうかを判定するメソッド
     * 
     * @param directoryArray
     * @return
     */
    public static boolean isDirectoryArray(Path[] directoryArray) {
        return Arrays.stream(directoryArray)
                .allMatch(path -> Files.isDirectory(path));
    }

    /**
     * ディレクトリが存在し、ディレクトリであるかを判定するメソッド
     * @param directory
     * @return
     */
    public static boolean isExistsAndDirectory(Path[] directory) {
        // "指定したディレクトリが存在しないか、ディレクトリではありません。{}", directory 
        return Arrays.stream(directory)
                .allMatch(path -> Files.exists(path) && Files.isDirectory(path));
    }

    /**
     * 文字列配列から空文字やnullの要素を削除し、削除された要素のインデックスを返却するメソッド
     * 
     * @param array 文字列の配列
     * @param removedIndices 削除された要素のインデックスを格納するリスト
     * @return 空文字やnullの要素が削除された新しい配列
     */
    public static String[] removeEmptyStrings(String[] array, List<Integer> removedIndices) {
        List<String> filteredList = IntStream.range(0, array.length)
                .filter(i -> {
                    boolean isEmpty = array[i] == null || array[i].trim().isEmpty();
                    if (isEmpty) {
                        removedIndices.add(i);
                    }
                    return !isEmpty;
                })
                .mapToObj(i -> array[i])
                .collect(Collectors.toList());

        return filteredList.toArray(new String[0]);
    }
}

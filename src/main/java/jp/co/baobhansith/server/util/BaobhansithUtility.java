package jp.co.baobhansith.server.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;

public class BaobhansithUtility {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 指定されたCSVファイルを読み込み、指定されたキーに一致する値を返却するメソッド
     * 
     * @param filePath
     * @param key
     * @return
     * @throws IOException
     */
    public static HashMap<String, String> getMapByKey(String filePath, String key, int position) throws IOException {
        Path path = Paths.get(filePath);
        HashMap<String, String> map = new HashMap<>();

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                // CSVの行をカンマで分割
                String[] columns = line.split(",", -1);

                // 最初のカラムがキーと一致するか確認
                if (columns[0].equals(key)) {
                    map.put(columns[0], columns[position]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 指定されたCSVファイルを読み込み、指定されたキーに一致する行を返却するメソッド
     * 
     * @param filePath
     * @param keys
     * @param positions
     * @return
     * @throws Exception
     */
    public static List<String[]> getRowsByKey(String filePath, String keys, int... positions) throws Exception {
        return getRowsByKey(filePath, new String[] {keys}, positions);
    }

    /**
     * 指定されたCSVファイルを読み込み、指定されたキーに一致する行を返却するメソッド
     * 
     * @param filePath
     * @param key
     * @return
     * @throws IOException
     */
    public static List<String[]> getRowsByKey(String filePath, String[] keys, int... positions) throws IOException {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
            return br.lines()
                    .map(line -> line.split(",", -1))
                    .filter(columns -> IntStream.range(0, Math.min(keys.length, positions.length))
                            .allMatch(i -> columns[positions[i]].equals(keys[i])))
                    .collect(Collectors.toList());
        }
    }
    
    public static List<String[]> getRowsByKey(String filePath, String key, int position) throws IOException {
        Path path = Paths.get(filePath);
        List<String[]> rows = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                // CSVの行をカンマで分割
                String[] columns = line.split(",", -1);

                // 最初のカラムがキーと一致するか確認
                if (columns[position].equals(key)) {
                    rows.add(columns);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rows;
    }

    /**
     * レコードから空の要素を削除するメソッド
     * @param record
     * @return
     */
    public static String[] removeEmptyElement(String[] record) {
        return Arrays.stream(record)
                .filter(v -> StringUtils.isNotEmpty(v))
                .toArray(String[]::new);
    }

    /**
     * レコードから指定した位置のディレクトリを取得するメソッド
     * 
     * @param record
     * @return
     */
    public static String getFirstNonEmptyElement(String[] record, int skipNumber) {
        Optional<String> value = Arrays.stream(record)
                .skip(skipNumber)
                .filter(v -> StringUtils.isNotEmpty(v))
                .findFirst();

        return value.orElse(StringUtils.EMPTY);
    }

    /**
     *　ディレクトリが存在するかどうかを判定するメソッド
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
     * ディレクトリパス配列を取得するメソッド
     * 
     * @param record
     * @param indexArray
     * @return
     */
    public static Path[] getDirectoryPathArray(String[] record, int[] indexArray) {
        return IntStream.of(indexArray)
                .mapToObj(index -> Paths.get(record[index]))
                .toArray(Path[]::new);
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
     * 
     * @param directory
     * @return
     */
    public static boolean isExistsAndDirectory(Path[] directory) {
        // "指定したディレクトリが存在しないか、ディレクトリではありません。{}", directory
        return Arrays.stream(directory)
                .allMatch(path -> Files.exists(path) && Files.isDirectory(path));
    }
}

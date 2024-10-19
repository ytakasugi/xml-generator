package jp.co.baobhansith.server.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BaobhansithUtility {
    // 指定されたCSVファイルを読み込み、指定されたキーに一致する行を返却するメソッド
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

    // 指定されたCSVファイルを読み込み、指定されたキーに一致する行を返却するメソッド
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
}

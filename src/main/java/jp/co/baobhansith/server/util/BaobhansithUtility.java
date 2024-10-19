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
                String[] columns = line.split(",");

                // 最初のカラムがキーと一致するか確認
                if (columns[0].equals(key)) {
                    return columns;  // キーが一致した行をそのまま返却
                }
            }
        }
        return null;  // キーに一致する行が見つからなかった場合
    }
}

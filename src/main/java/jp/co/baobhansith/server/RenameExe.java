package jp.co.baobhansith.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RenameExe {
    private static final String DIRECTORY = "/home/ytakasugi/java-workspace/baobhansith/pre";
    public static void main(String[] args) throws Exception {
        List<Path> fileList = getFileList(DIRECTORY);

        if (fileList.isEmpty()) {
            System.out.println("No files found");
            return;
        }

        fileList.forEach(System.out::println);

        renameFiles(fileList);
        
    }

    private static List<Path> getFileList(String directory) throws IOException {
        // 指定されたディレクトリ直下のファイルの絶対パスを取得
        try (Stream<Path> paths = Files.list(Paths.get(directory))) {  // ディレクトリ直下のファイルのみを取得
            return paths.filter(Files::isRegularFile)                  // 通常のファイルのみをフィルタ
                        .map(Path::toAbsolutePath)                     // ファイルの絶対パスに変換
                        //.map(Path::toString)                           // PathをStringに変換
                        .collect(Collectors.toList());                 // List<String>に収集
        }
    }

    // ファイルをリネームするメソッド
    private static void renameFiles(List<Path> filePaths) {
        for (Path filePath : filePaths) {
            try {
                // 新しいファイル名を作成（元のファイル名に "renamed_" を付加）
                Path newFileName = filePath.resolveSibling("renamed_" + filePath.getFileName().toString());

                // ファイルを新しい名前にリネーム
                Files.move(filePath, newFileName);

                System.out.println("Renamed: " + filePath + " to " + newFileName);
            } catch (IOException e) {
                System.err.println("Failed to rename: " + filePath);
                e.printStackTrace();
            }
        }
    }
}

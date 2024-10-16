package jp.co.baobhansith.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RenameExe {
    private static final String DIRECTORY = "/pre";
    private static final String MOVE_TO_DIRECTORY = "/out";

    public static void main(String[] args) throws Exception {
        List<Path> fileList = getFileList(DIRECTORY);

        if (fileList.isEmpty()) {
            System.out.println("No files found");
            return;
        }

        fileList.forEach(System.out::println);

        // renameFiles(fileList);
        renameAndMoveFiles(fileList, MOVE_TO_DIRECTORY);

    }

    public static List<Path> getFileList(String directory) throws IOException {
        // 指定されたディレクトリ直下のファイルの絶対パスを取得
        try (Stream<Path> paths = Files.list(Paths.get(directory))) { // ディレクトリ直下のファイルのみを取得
            return paths.filter(Files::isRegularFile) // 通常のファイルのみをフィルタ
                    .map(Path::toAbsolutePath) // ファイルの絶対パスに変換
                    // .map(Path::toString) // PathをStringに変換
                    .collect(Collectors.toList()); // List<String>に収集
        }
    }

    // ファイルをリネームするメソッド
    public static void renameFiles(List<Path> filePaths) {
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

    // ファイルをリネームして指定されたディレクトリに移動するメソッド
    public static void renameAndMoveFiles(List<Path> filePaths, String destinationDirectory) {
        Path destinationDir = Paths.get(destinationDirectory); // 移動先ディレクトリを指定

        for (Path filePath : filePaths) {
            try {
                // 新しいファイル名を作成（元のファイル名に "renamed_" を付加）
                String newFileName = "renamed_" + filePath.getFileName().toString();

                // 移動先ディレクトリ内に新しいファイルパスを作成
                Path destinationPath = destinationDir.resolve(newFileName);

                // ファイルをリネームして移動
                Files.move(filePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

                System.out.println("Moved and Renamed: " + filePath + " to " + destinationPath);
            } catch (IOException e) {
                System.err.println("Failed to rename and move: " + filePath);
                e.printStackTrace();
            }
        }
    }

    // ファイルをリネームして指定されたディレクトリにコピーするメソッド
    public static void renameAndCopyFiles(List<Path> filePaths, String destinationDirectory) {
        Path destinationDir = Paths.get(destinationDirectory); // コピー先ディレクトリを指定

        for (Path filePath : filePaths) {
            try {
                // 新しいファイル名を作成（元のファイル名に "renamed_" を付加）
                String newFileName = "renamed_" + filePath.getFileName().toString();

                // コピー先ディレクトリ内に新しいファイルパスを作成
                Path destinationPath = destinationDir.resolve(newFileName);

                // ファイルをコピー (同じファイルがある場合は上書き)
                Files.copy(filePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

                System.out.println("Copied and Renamed: " + filePath + " to " + destinationPath);
            } catch (IOException e) {
                System.err.println("Failed to copy and rename: " + filePath);
                e.printStackTrace();
            }
        }
    }

    // ファイルをリネームするメソッド
    public static Path renameFile(Path filePath) throws IOException {
        // 新しいファイル名を作成（元のファイル名に "renamed_" を付加）
        Path renamedFilePath = filePath.resolveSibling("renamed_" + filePath.getFileName().toString());

        // ファイルをリネーム
        Files.move(filePath, renamedFilePath, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Renamed: " + filePath + " to " + renamedFilePath);

        return renamedFilePath;  // リネーム後のファイルパスを返却
    }

    // ファイルを移動するメソッド
    public static void moveFile(Path filePath, String destinationDirectory) throws IOException {
        Path destinationDir = Paths.get(destinationDirectory);  // 移動先ディレクトリを指定
        Path destinationPath = destinationDir.resolve(filePath.getFileName());  // 移動先のファイルパス

        // ファイルを移動
        Files.move(filePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Moved: " + filePath + " to " + destinationPath);
    }

    // ファイルをコピーするメソッド
    public static void copyFile(Path filePath, String destinationDirectory) throws IOException {
        Path destinationDir = Paths.get(destinationDirectory);  // コピー先ディレクトリを指定
        Path destinationPath = destinationDir.resolve(filePath.getFileName());  // コピー先のファイルパス

        // ファイルをコピー (同じファイルが存在する場合は上書き)
        Files.copy(filePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Copied: " + filePath + " to " + destinationPath);
    }
}

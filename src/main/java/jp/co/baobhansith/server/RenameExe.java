package jp.co.baobhansith.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jp.co.baobhansith.server.util.BaobhansithException;
import jp.co.baobhansith.server.util.BaobhansithUtility;

public class RenameExe {
    // ----------------------------------------------------------
    // クラス定数
    // ----------------------------------------------------------
    private static final String TARGET_ID = "X00_00_000_1";
    private static final String CONFIG_PATH = "/home/ytakasugi/java-workspace/baobhansith/config.csv";

    // ----------------------------------------------------------
    // ロガー
    // ----------------------------------------------------------
    private static final Logger logger = LogManager.getLogger(RenameExe.class);

    public static void main(String[] args) throws Exception {
        // ----------------------------------------------------------
        // ローカル変数の宣言
        // ----------------------------------------------------------
        String id = null;
        String[] directoryArray = null;
        String sourceDirectory = null;
        String destinationDirectory = null;

        try {
            // ----------------------------------------------------------
            // ローカル変数の初期化
            // ----------------------------------------------------------
            id = TARGET_ID;
            // ----------------------------------------------------------
            // ファイル取得元ディレクトリとファイル移動先ディレクトリの取得
            // ----------------------------------------------------------
            directoryArray = getDirectory(id);
            // 配列が空の場合
            if (ArrayUtils.isEmpty(directoryArray)) {
                // ログ出力
                logger.error("Failed to get directory");
                // プロセスを終了
                return;
            }
            // ファイル取得元ディレクトリを取得
            sourceDirectory = directoryArray[1];
            // ファイル移動先ディレクトリを取得
            destinationDirectory = directoryArray[2];
            // ファイル取得元ディレクトリまたはファイル移動先ディレクトリが空の場合
            if (isEmptyDirectories(sourceDirectory, destinationDirectory)) {
                // ログ出力
                logger.error("Invalid directory. Source Directroy : {}, Destination Directroy : {}",
                        sourceDirectory,
                        destinationDirectory);
                // プロセスを終了
                return;
            }
            // ----------------------------------------------------------
            // ファイルリストの取得
            // ----------------------------------------------------------
            List<Path> fileList = getFileList(sourceDirectory);

            // ファイルリストが空の場合
            if (CollectionUtils.isEmpty(fileList)) {
                // ログ出力
                logger.error("File list is empty");
                // プロセスを終了
                return;
            }

            fileList.forEach(System.out::println);

        } catch (IOException e) {
            logger.error("Failed to get directory", e.getMessage());
            return;
        }
    }

    private static String[] getDirectory(String id) throws IOException {
        return BaobhansithUtility.getRowByKey(CONFIG_PATH, id);
    }

    private static boolean isEmptyDirectories(String sourceDirectory, String destinationDirectory) {
        return StringUtils.isEmpty(sourceDirectory) || StringUtils.isEmpty(destinationDirectory);
    }

    /**
     * <dd> 指定されたディレクトリ内のファイルの絶対パスを取得する
     * 
     * @param directory
     * @return List<Path>
     * @throws BaobhansithException
     */
    public static List<Path> getFileList(String directory) throws BaobhansithException {
        List<Path> fileList = new ArrayList<>();

        // 指定されたディレクトリ直下のファイルの絶対パスを取得
        try (Stream<Path> paths = Files.list(Paths.get(directory))) {
            fileList = paths.filter(Files::isRegularFile)
                    .map(Path::toAbsolutePath)
                    .collect(Collectors.toList()); 
        } catch (IOException e) {
            throw new BaobhansithException("Failed to get file list", e);
        }
        return fileList;
    }

    /**
     * <dd> ファイルをリネームするメソッド
     * 
     * @param filePathList
     */
    public static void rename(List<Path> filePathList) {
        for (Path filePath : filePathList) {
            try {
                Path newFileName = filePath.resolveSibling("renamed_" + filePath.getFileName().toString());

                Files.move(filePath, newFileName);

                System.out.println("Renamed: " + filePath + " to " + newFileName);
            } catch (IOException e) {
                System.err.println("Failed to rename: " + filePath);
                e.printStackTrace();
            }
        }
    }

    /**
     * <dd> ファイルを移動する
     * 
     * @param filePath
     * @param destinationDirectory
     * @throws IOException
     */
    public static void moveFile(Path filePath, String destinationDirectory) throws IOException {
        Path destinationDir = Paths.get(destinationDirectory); // 移動先ディレクトリを指定
        Path destinationPath = destinationDir.resolve(filePath.getFileName()); // 移動先のファイルパス

        // ファイルを移動
        Files.move(filePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Moved: " + filePath + " to " + destinationPath);
    }

    /**
     * <dd> ファイルをコピーする
     * 
     * @param filePath
     * @param destinationDirectory
     * @throws IOException
     */
    public static void copyFile(Path filePath, String destinationDirectory) throws IOException {
        Path destinationDir = Paths.get(destinationDirectory); // コピー先ディレクトリを指定
        Path destinationPath = destinationDir.resolve(filePath.getFileName()); // コピー先のファイルパス

        // ファイルをコピー (同じファイルが存在する場合は上書き)
        Files.copy(filePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Copied: " + filePath + " to " + destinationPath);
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
}

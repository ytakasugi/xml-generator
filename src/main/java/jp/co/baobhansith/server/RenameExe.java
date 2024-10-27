package jp.co.baobhansith.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private static final String DATE_FORMAT = "yyyyMMdd'T'HHmmss";
    private static final int INIT_SEQUENCE_VALUE = 0;
    private static final int MAX_SEQUENCE_VALUE = 9999;
    private static final String SEQUENCE_FORMAT = "%04d";
    private static final String FILE_NAME_ZERO_PADDING = "0";
    private static final String HOST_NAME = "localhost";
    private static final String HYPYEN = "-";

    // ----------------------------------------------------------
    // ロガー
    // ----------------------------------------------------------
    private static final Logger Logger = LogManager.getLogger(RenameExe.class);

    public static void main(String[] args) throws Exception {
        // ----------------------------------------------------------
        // ローカル変数の宣言
        // ----------------------------------------------------------
        String id = null;
        // ファイル取得元ディレクトリとファイル移動先ディレクトリの配列
        Path[] directoryArray = null;
        // ファイル取得元ディレクトリ
        Path sourceDirectory = null;
        // ファイル移動先ディレクトリ
        Path destinationDirectory = null;
        // エラーディレクトリ
        Path errorDirectory = null;

        try {
            // ----------------------------------------------------------
            // ローカル変数の初期化
            // ----------------------------------------------------------
            id = TARGET_ID;
            // ----------------------------------------------------------
            // ファイル取得元ディレクトリとファイル移動先ディレクトリの取得
            // ----------------------------------------------------------
            directoryArray = getDirectory(id);
            // ファイル取得元ディレクトリを取得
            sourceDirectory = directoryArray[0];
            // ファイル移動先ディレクトリを取得
            destinationDirectory = directoryArray[1];
            // エラーディレクトリを取得
            errorDirectory = directoryArray[2];
            // ----------------------------------------------------------
            // ファイルリストの取得
            // ----------------------------------------------------------
            List<Path> fileList = getFileList(sourceDirectory);

            // ファイルリストが空の場合
            if (CollectionUtils.isEmpty(fileList)) {
                // ログ出力
                Logger.info("File list is empty");
                // プロセスを終了
                return;
            }

            // ----------------------------------------------------------
            // デバッグ用
            // ----------------------------------------------------------
            fileList.forEach(System.out::println);

            // ----------------------------------------------------------
            // ファイルをリネームして指定されたディレクトリに移動
            // ----------------------------------------------------------
            execute(fileList, destinationDirectory, errorDirectory, id);

        } catch (IOException e) {
            Logger.error("Failed to get directory", e.getMessage());
            return;
        }
    }

    /**
     * <dd>指定されたIDに対応するディレクトリ情報を取得
     * 
     * @param id
     * @return String[]
     * @throws IOException
     */
    private static Path[] getDirectory(String id) throws IOException {
        String[] record = null;
        Path[] pathArray = null;

        record = BaobhansithUtility.getRowByKey(CONFIG_PATH, id);
        record = BaobhansithUtility.getNonEmptyElement(record, 3);
        pathArray = BaobhansithUtility.getDirectoryPathArray(record);

        return pathArray;
    }

    /**
     * <dd>指定されたディレクトリ内のファイルの絶対パスを取得する
     * 
     * @param directory
     * @return List<Path>
     * @throws BaobhansithException
     */
    public static List<Path> getFileList(Path directory) throws BaobhansithException {
        List<Path> fileList = new ArrayList<>();

        // 指定されたディレクトリ直下のファイルの絶対パスを取得
        try (Stream<Path> paths = Files.list(directory)) {
            fileList = paths.filter(Files::isRegularFile)
                    .map(Path::toAbsolutePath)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new BaobhansithException("Failed to get file list", e);
        }
        return fileList;
    }

    /**
     * <dd>ファイルをリネームして指定されたディレクトリに移動する
     * 
     * @param filePathList
     * @param destinationDirectory
     */
    public static void execute(List<Path> filePathList, Path destinationDirectory, Path errorDirectory, String id) {
        String fileName = null;
        String convertTime = null;
        int seq = INIT_SEQUENCE_VALUE;
        String fileSequence = null;
        try {
            convertTime = setConvertTime();

            for (Path filePath : filePathList) {
                seq++;
                if (seq > MAX_SEQUENCE_VALUE) {
                    seq = INIT_SEQUENCE_VALUE;
                }
                fileSequence = String.format(SEQUENCE_FORMAT, seq);
                fileName = generateOutputFileName(id, convertTime, fileSequence);
                
                Path newFileName = destinationDirectory.resolve(Paths.get(fileName));
                Files.move(filePath, newFileName);
            }
        } catch (IOException e) {
            Logger.error("Failed to rename files", e);
            
        }
    }

    /**
     * <dd>ファイルをリネームするメソッド
     * 
     * @param filePathList
     * @return List<Path>
     * @throws BaobhansithException
     */
    public static List<Path> rename(List<Path> filePathList) throws BaobhansithException {
        List<Path> renamedFilePathList = new ArrayList<>();

        for (Path filePath : filePathList) {
            try {
                Path newFileName = filePath.resolveSibling("renamed_" + filePath.getFileName().toString());
                Files.move(filePath, newFileName);
                renamedFilePathList.add(newFileName);
            } catch (IOException e) {
                throw new BaobhansithException("Failed to rename: " + filePath, e);
            }
        }
        return renamedFilePathList;
    }

    /**
     * <dd>ファイルを移動する
     * 
     * @param filePath
     * @param destinationDirectory
     * @throws BaobhansithException
     */
    public static void move(List<Path> filePathList, String destinationDirectory) throws BaobhansithException {
        for (Path filePath : filePathList) {
            try {
                Path destinationDir = Paths.get(destinationDirectory);
                Path destinationPath = destinationDir.resolve(filePath.getFileName());
                Files.move(filePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new BaobhansithException("Failed to move: " + filePath, e);
            }
        }
    }

    private static String setConvertTime() {
        // yyyyMMddHHmmss形式に変換
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        return localDateTime.format(formatter);
    }

    private static String generateOutputFileName(String id, String convertTime, String seq) {
        StringBuffer outputFileName = new StringBuffer();

        outputFileName.append(id);
        outputFileName.append(HYPYEN);
        outputFileName.append(convertTime);
        outputFileName.append(HYPYEN);
        outputFileName.append(seq);
        outputFileName.append(FILE_NAME_ZERO_PADDING);
        outputFileName.append(HYPYEN);
        outputFileName.append(HOST_NAME);
        outputFileName.append(".xml");
        return outputFileName.toString();
    }
}

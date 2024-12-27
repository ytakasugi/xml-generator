package jp.co.baobhansith.server.logic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jp.co.baobhansith.server.util.BaobhansithException;

public class TimeCalculation {
    //---------------------------------------------------
    // クラス定数
    //---------------------------------------------------
    private static final String DATE_FORMAT_ISO8061 = "yyyyMMdd'T'HHmmss";
    private static final String DIRECTORY_PATH = "/home/ytakasugi/java-workspace/baobhansith/out";
    private static final String COMPILE_PATTERN = "(\\d{8}T\\d{6})";


    public static void main(String[] args) throws BaobhansithException {
        //---------------------------------------------------
        // 1.ローカル変数宣言
        //---------------------------------------------------
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        LocalDateTime currentTime = null;
        List<Path> filePathList = null;
        long minusMinute = Long.parseLong("0");
        List<Path> processFileList = null;

        //---------------------------------------------------
        // 2.ローカル変数初期化
        //---------------------------------------------------
        currentTime = timestamp.toLocalDateTime();
        filePathList = new ArrayList<>();
        processFileList = new ArrayList<>();

        //---------------------------------------------------
        // 3.処理開始
        //---------------------------------------------------
        filePathList = getFileList(Path.of(DIRECTORY_PATH));
        processFileList = filterFilesByTimeRange(filePathList, currentTime, minusMinute);

        if (processFileList.size() == 0) {
            System.out.println("no process files found within the specified time range.");
        } else {
            System.out.println("Files found within the specified time range:");
            for (Path path : processFileList) {
                System.out.println(path);
            }
        }
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

    // ファイルリストから指定範囲内のファイルを抽出
    public static List<Path> filterFilesByTimeRange(List<Path> filePaths, LocalDateTime currentTime, long minutes) {
        if (minutes == 0) {
            // 引数が"0"の場合は、ファイルパスリストをそのまま返却
            return filePaths;
        }

        // ---------------------------------------------------
        // ローカル変数宣言
        // ---------------------------------------------------
        LocalDateTime pastTime = getCurrentTimeMinusMinutes(currentTime, minutes);
        List<Path> filteredFiles = new ArrayList<>();
        LocalDateTime fileTime = null;

        for (Path filePath : filePaths) {
            fileTime = getCreateFileDate(filePath);

            if (isWithinRange(pastTime, currentTime, fileTime)) {
                filteredFiles.add(filePath);
            }
        }
        return filteredFiles;
    }

    public static LocalDateTime getCreateFileDate(Path filePath) {
        String fileName = filePath.getFileName().toString();
        Pattern pattern = Pattern.compile(COMPILE_PATTERN);
        Matcher matcher = pattern.matcher(fileName);

        if (matcher.find()) {
            String timeString = matcher.group(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_ISO8061);
            return LocalDateTime.parse(timeString, formatter);
        }
        return null;
    }
    

    public static LocalDateTime getCurrentTimeMinusMinutes(LocalDateTime currentTime, long minutes) {
        LocalDateTime adjustedTime = currentTime.minus(minutes, ChronoUnit.MINUTES);
        return adjustedTime;
    }

    public static boolean isWithinRange(LocalDateTime startTime, LocalDateTime endTime, LocalDateTime fileTime) {
        return (fileTime.isEqual(startTime) || fileTime.isAfter(startTime)) &&
                (fileTime.isEqual(endTime) || fileTime.isBefore(endTime));
    }
}

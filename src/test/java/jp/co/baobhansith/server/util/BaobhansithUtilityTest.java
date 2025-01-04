package jp.co.baobhansith.server.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

public class BaobhansithUtilityTest {
    @TempDir
    Path tempDir;

    @Test
    public void testGetFileList_Normal() {
        try {
            // 一時ディレクトリにテスト用のファイルを作成
            Path file1 = Files.createFile(tempDir.resolve("X00_00_000_1-20241229T233457-00010-localhost.xml"));
            Path file2 = Files.createFile(tempDir.resolve("X00_00_000_1-20241229T233457-00020-localhost.xml"));

            // メソッドを呼び出してファイルリストを取得
            List<Path> fileList = BaobhansithUtility.getFileList(tempDir);

            // ファイルリストが正しく取得されていることを確認
            assertNotNull(fileList);
            assertEquals(2, fileList.size());
            assertTrue(fileList.contains(file1.toAbsolutePath()));
            assertTrue(fileList.contains(file2.toAbsolutePath()));
        } catch (BaobhansithException | IOException e) {
            fail("例外が発生しました: " + e.getMessage());
        }
    }

    @Test
    public void testGetFileList_Empty() {
        try {
            // メソッドを呼び出して空のファイルリストを取得
            List<Path> fileList = BaobhansithUtility.getFileList(tempDir);

            // ファイルリストが空であることを確認
            assertNotNull(fileList);
            assertTrue(fileList.isEmpty());
        } catch (BaobhansithException e) {
            fail("例外が発生しました: " + e.getMessage());
        }
    }

    @Test
    public void testGetFileList_Exception() {
        try {
            // メソッドを呼び出して例外がスローされることを確認
            assertThrows(BaobhansithException.class,
                    () -> BaobhansithUtility.getFileList(tempDir.resolve("nonexistent")));
        } catch (Exception e) {
            fail("予期しない例外が発生しました: " + e.getMessage());
        }
    }

    @Test
    public void testGetValueByKey_Normal() {
        try {
            // テスト用のCSVファイルのパスを取得
            String filePath = "/home/ytakasugi/java-workspace/baobhansith/src/test/java/resources/config.csv";
            // 期待値
            String expected = "/home/ytakasugi/java-workspace/baobhansith/out";

            // メソッドを呼び出して値を取得
            String actual = BaobhansithUtility.getValueByKey(filePath, "X00_00_000_1", 8);

            assertEquals(expected, actual);
        } catch (IOException e) {
            fail("例外が発生しました: " + e.getMessage());
        }
    }

    @Test
    public void testGetValueByKey_KeyNotFound() {
        try {
            // テスト用のCSVファイルのパスを取得
            String filePath = "src/test/resources/config.csv";

            // メソッドを呼び出して値を取得
            String actual = BaobhansithUtility.getValueByKey(filePath, "X00_00_000_5", 1);

            // 値がnullであることを確認
            assertNull(actual);
        } catch (IOException e) {
            fail("例外が発生しました: " + e.getMessage());
        }
    }

    @Test
    public void testGetValueByKey_IndexOutOfBounds() {
        try {
            // テスト用のCSVファイルのパスを取得
            String filePath = "src/test/resources/config.csv";

            // メソッドを呼び出して値を取得
            String actual = BaobhansithUtility.getValueByKey(filePath, "X00_00_000_2", 10);

            // 値がnullであることを確認
            assertNull(actual);
        } catch (IOException e) {
            fail("例外が発生しました: " + e.getMessage());
        }
    }

    @Test
    public void testGetHostName_Normal() {
        try {
            String hostName = BaobhansithUtility.getHostName();
            assertNotNull(hostName);
            assertFalse(hostName.isEmpty());
        } catch (BaobhansithException e) {
            fail("UnknownHostException");
        }
    }

    @Test
    public void testGetHostName_Exception() {
        try (MockedStatic<InetAddress> mockedInetAddress = mockStatic(InetAddress.class)) {
            // InetAddress.getLocalHost()をモックして例外をスローさせる
            mockedInetAddress.when(InetAddress::getLocalHost).thenThrow(new UnknownHostException());

            // BaobhansithUtility.getHostName()を呼び出して例外がスローされることを確認
            Executable executable = () -> BaobhansithUtility.getHostName();
            BaobhansithException exception = assertThrows(BaobhansithException.class, executable);
            assertEquals("ホスト名取得に失敗しました。", exception.getMessage());
            assertEquals(UnknownHostException.class, exception.getCause().getClass());
        } catch (Exception e) {
            fail("予期しない例外が発生しました: " + e.getMessage());
        }
    }

    @ParameterizedTest
    @CsvSource({
            "X00_00_000_1, 20241229T233457, 0001, X00_00_000_1-20241229T233457-00010-localhost.xml",
            "X00_00_000_1, 20241229T233457, 99999, X00_00_000_1-20241229T233457-99999-localhost.xml",
            "X00_00_000_1, 20241229T233457, , X00_00_000_1-20241229T233457-localhost.xml",

    })
    public void testGenerateOutputFileName(String id, String convertTime, String sequence, String expected) {
        String result = BaobhansithUtility.generateOutputFileName(id, convertTime, sequence);
        assertEquals(expected, result);
    }

}

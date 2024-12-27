package jp.co.baobhansith.server.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.MockedStatic;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

public class BaobhansithUtilityTest {
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
            mockedInetAddress.when(InetAddress::getLocalHost).thenThrow(new UnknownHostException("テスト用の例外"));

            // BaobhansithUtility.getHostName()を呼び出して例外がスローされることを確認
            Executable executable = () -> BaobhansithUtility.getHostName();
            assertThrows(BaobhansithException.class, executable, "UnknownHostExceptionがスローされることを期待します");
        } catch (Exception e) {
            fail("予期しない例外が発生しました: " + e.getMessage());
        }
    }
}

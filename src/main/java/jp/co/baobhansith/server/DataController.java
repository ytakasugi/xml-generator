package jp.co.baobhansith.server;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jp.co.baobhansith.server.common.bean.CommonBean;

public class DataController {
    private static final Logger logger = LogManager.getLogger(DataController.class);

    private static final int INIT_FILE_SEQUENCE = 0;
    private static ConcurrentHashMap<String, AtomicInteger> seqMap = new ConcurrentHashMap<>();

    public DataController() {
    }

    public void processData(CommonBean bean) {
        new Thread(() -> {
            bean.setCreated(new Timestamp(System.currentTimeMillis()));
            AtomicInteger seq = seqMap.computeIfAbsent(bean.getId(), k -> new AtomicInteger(INIT_FILE_SEQUENCE));
            bean.setSeq(String.format("%04d", seq.incrementAndGet()));

            // JAXB2クラスに引き渡してXML化
            JAXB2 jaxb2 = new JAXB2(bean);
            try {
                if (!jaxb2.convertFormat()) {
                    logger.error("Failed to write to file");
                }
                ;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
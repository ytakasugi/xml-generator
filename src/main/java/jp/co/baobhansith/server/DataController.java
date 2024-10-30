package jp.co.baobhansith.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jp.co.baobhansith.server.common.bean.CommonBean;

public class DataController {
    private static final Logger logger = LogManager.getLogger(DataController.class);

    private static final int THREAD_POOL_SIZE = 10;
    private final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    public DataController() {
    }

    public void processData(CommonBean bean) {
        executorService.submit(() -> {
            // JAXB2クラスに引き渡してXML化
            JAXB2 jaxb2 = new JAXB2(bean);
            try {
                logger.info("Start writing to file. Thread Name: " + Thread.currentThread().getName());
                if (!jaxb2.convertFormat()) {
                    logger.error("Failed to write to file");
                }
                logger.info("End writing to file. Thread Name: " + Thread.currentThread().getName());
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        });
    }

    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    logger.error("ExecutorService did not terminate");
                }
            }
        } catch (InterruptedException ie) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
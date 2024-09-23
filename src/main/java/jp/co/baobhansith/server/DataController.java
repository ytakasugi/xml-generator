package jp.co.baobhansith.server;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import jp.co.baobhansith.server.common.bean.CommonBean;

public class DataController {
    private static final ConcurrentHashMap<String, String> createdMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, AtomicInteger> seqMap = new ConcurrentHashMap<>();
    private static String globalCreated;

    public DataController() {
        // AppXmlGeneratorクラスが起動されるごとに新しい値を生成
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        globalCreated = LocalDateTime.now().format(formatter);
    }

    public void processData(CommonBean bean) {
        new Thread(() -> {
            String created = createdMap.computeIfAbsent(bean.getId(), k -> globalCreated);
            AtomicInteger seq = seqMap.computeIfAbsent(bean.getId(), k -> new AtomicInteger(0));
            String seqStr = String.format("%04d", seq.incrementAndGet());
            bean.setSeq(seqStr);
            bean.setCreated(created);

            // JAXB2クラスに引き渡してXML化
            JAXB2 jaxb2 = new JAXB2();
            try {
                jaxb2.convert(bean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
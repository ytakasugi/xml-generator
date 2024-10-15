package jp.co.baobhansith.server;

import java.util.concurrent.ConcurrentHashMap;

public class Register {
    private static final int INIT_SEQUENCE_VALUE = 0;
    private static final int MAX_SEQUENCE_VALUE = 9999;
    private static String SEQUENCE_FORMAT = "%04d";
    private static ConcurrentHashMap<String, SequenceManager> sequenceMap = new ConcurrentHashMap<>();

    private static class SequenceManager {
        private String timestamp;
        private int sequence;

        private SequenceManager(String timestamp, int sequence) {
            this.timestamp = timestamp;
            this.sequence = sequence;
        }
    }

    private static int sequenceNumbering(String id, String timestamp) {
        sequenceMap.compute(id, (k, value) -> {
            // [パラメータ]IDに対応するSequenceManagerオブジェクトが存在しない場合、新規作成
            if (value == null) {
                value = new SequenceManager(timestamp, INIT_SEQUENCE_VALUE);
            } else {
                // [パラメータ]IDに対応するSequenceManagerオブジェクトが存在する場合
                // [処理]タイムスタンプが異なる場合、タイムスタンプを更新、シーケンスを初期化
                if (!value.timestamp.equals(timestamp)) {
                    value.timestamp = timestamp;
                    value.sequence = INIT_SEQUENCE_VALUE;
                } else {
                    // [処理]タイムスタンプが同じ場合
                    // [処理]シーケンスが最大値に達した場合、初期化
                    if (value.sequence >= MAX_SEQUENCE_VALUE) {
                        value.sequence = INIT_SEQUENCE_VALUE;
                    }
                }
            }
            // [処理]シーケンスをインクリメント
            value.sequence++;
            // [戻り値]更新後のSequenceManagerオブジェクトを返却し、Mapに格納
            // [補足]ラムダ式の戻り値をMapに格納する
            return value;
        });
        // [戻り値]シーケンスを返却
        return sequenceMap.get(id).sequence;
    }

    public static void main(String[] args) {
        String id1 = "id1";
        String id2 = "id2";
        String timestamp1 = "2021-01-01";
        String timestamp2 = "2021-01-02";

        int ret1 = sequenceNumbering(id1, timestamp1);
        System.out.println("sequence1 = " + ret1);
        int ret2 = sequenceNumbering(id1, timestamp1);
        System.out.println("sequence2 = " + ret2);
        int ret3 = sequenceNumbering(id1, timestamp2);
        System.out.println("sequence3 = " + ret3);
        int ret4 = sequenceNumbering(id2, timestamp2);
        System.out.println("sequence4 = " + ret4);

        System.out.println(String.format(SEQUENCE_FORMAT, sequenceNumbering(id1, timestamp2)));

        // System.out.println("----- " + "id = " + id1 + ", timestamp = " + timestamp1 + " -----");
        // sequenceNumbering(id1, timestamp1);
        // sequenceNumbering(id1, timestamp1);
        // SequenceManager ret1 = sequenceMap.get(id1);
        // System.out.println("sequence = " + ret1.sequence);

        // System.out.println("----- " + "id = " + id1 + ", timestamp = " + timestamp2 + " -----");
        // sequenceNumbering(id1, timestamp2);
        // SequenceManager ret2 = sequenceMap.get(id1);
        // System.out.println("sequence = " + ret2.sequence);

        // System.out.println("----- " + "id = " + id2 + ", timestamp = " + timestamp2 + " -----");
        // sequenceNumbering(id2, timestamp2);
        // SequenceManager ret3 = sequenceMap.get(id2);
        // System.out.println("sequence = " + ret3.sequence);

        // for (int i = 0; i < 10000; i++) {

        //     sequenceNumbering(id1, timestamp1);
        //     SequenceManager ret = sequenceMap.get(id1);

        //     if (i >= 9998) {
        //         System.out.println("----- loop -----");
        //         System.out.println("sequence = " + ret.sequence);
        //     }
        // }
    }
}

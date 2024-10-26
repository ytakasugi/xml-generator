package jp.co.baobhansith.server;

import java.util.List;

import jp.co.baobhansith.server.util.BaobhansithUtility;

public class UtilityExecutor {
    private static final String CONFIG_PATH = "/home/ytakasugi/java-workspace/baobhansith/config.csv";
    private static final String TARGET_ID = "X00_00_000_1";
    private static final int[] INDEX_ARRAY = {5, 6, 7};
    private static final String[] TERAMS1 = {"HTTP", "ASYNC"};
    private static final String[] TERAMS2 = {"HTTP", "HTTPS"};

    public static void main(String[] args) {
        try {
            // String[] record = BaobhansithUtility.getRowByKey(CONFIG_PATH, TARGET_ID);
            // String[] values = BaobhansithUtility.getDirectoryArray(record, INDEX_ARRAY);

            // for (String value : values) {
            //     System.out.println(value);
            // }
            
            // String value = BaobhansithUtility.getFirstNonEmptyElement(record, 1);
            // System.out.println(value);

            List<String[]> records = BaobhansithUtility.getRowsByKey(CONFIG_PATH, "X00_00_000_1", 0);

            for (String[] record : records) {
                System.out.println(record[0]);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}

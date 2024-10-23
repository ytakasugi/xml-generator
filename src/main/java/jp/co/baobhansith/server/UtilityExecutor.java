package jp.co.baobhansith.server;

import jp.co.baobhansith.server.util.BaobhansithUtility;

public class UtilityExecutor {
    private static final String CONFIG_PATH = "/home/ytakasugi/java-workspace/baobhansith/config.csv";
    private static final String TARGET_ID = "X00_00_000_1";
    private static final int[] INDEX_ARRAY = {5, 6, 7};

    public static void main(String[] args) {
        try {
            String[] record = BaobhansithUtility.getRowByKey(CONFIG_PATH, TARGET_ID);
            String[] values = BaobhansithUtility.getDirectoryArray(record, INDEX_ARRAY);

            for (String value : values) {
                System.out.println(value);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}

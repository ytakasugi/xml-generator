package jp.co.baobhansith.server;

import jp.co.baobhansith.server.util.BaobhansithUtility;

public class UtilityExecutor {
    private static final String CONFIG_PATH = "/home/ytakasugi/java-workspace/baobhansith/config.csv";
    private static final String TARGET_ID = "X00_00_000_4";

    public static void main(String[] args) {
        try {

            String[] record = BaobhansithUtility.getRowByKey(CONFIG_PATH, TARGET_ID);
            record = BaobhansithUtility.getNonEmptyElement(record, 3);
            System.out.println(record[0]);
            System.out.println(record[1]);
            System.out.println(record[2]);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}

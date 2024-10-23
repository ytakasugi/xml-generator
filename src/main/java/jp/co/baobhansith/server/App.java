package jp.co.baobhansith.server;

import jp.co.baobhansith.server.util.BaobhansithUtility;

/**
 * Hello world!
 *
 */
public class App {
    private static final String CONFIG_PATH = "/home/ytakasugi/java-workspace/baobhansith/config.csv";

    public static void main(String[] args) throws Exception {
        String value = BaobhansithUtility.getValueByKey(CONFIG_PATH, "X00_00_000_1", 1);
        System.out.println(value);

        String[] values = BaobhansithUtility.getRowByKey(CONFIG_PATH, "X00_00_000_1");
        String value1 = values[1];
        String value2 = values[2];
        String value3 = values[3];

        if(value1 == null) {
            System.out.println("value1 is null");
        } else if (value1.isEmpty()) {
            System.out.println("value1 is empty");
        } else {
            System.out.println(value1);
        }

        if(value2 == null) {
            System.out.println("value2 is null");
        } else if (value2.isEmpty()) {
            System.out.println("value2 is empty");
        } else {
            System.out.println(value2);
        }

        if(value3 == null) {
            System.out.println("value3 is null");
        } else if (value3.isEmpty()) {
            System.out.println("value3 is empty");
        } else {
            System.out.println(value3);
        } 
    }
}

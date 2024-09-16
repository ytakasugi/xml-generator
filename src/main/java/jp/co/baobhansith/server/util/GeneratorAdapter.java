package jp.co.baobhansith.server.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class GeneratorAdapter extends XmlAdapter<String, String> {
    @Override
    public String marshal(String value) {
        if (value == null) {
            // null の場合は空の文字列として出力
            return "";
        }
        return value;
    }

    @Override
    public String unmarshal(String value) {
        return value;
    }
}

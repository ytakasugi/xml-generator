package jp.co.baobhansith.server.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class GeneratorAdapter extends XmlAdapter<String, String> {
    @Override
    public String unmarshal(String v) throws Exception {
        if (v == null || v.trim().isEmpty()) {
            return null;
        }
        return v;
    }

    @Override
    public String marshal(String v) throws Exception {
        if (v == null || v.trim().isEmpty()) {
            return null;
        }
        return v;
    }
}

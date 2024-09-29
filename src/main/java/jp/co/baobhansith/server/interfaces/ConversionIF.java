package jp.co.baobhansith.server.interfaces;

import jp.co.baobhansith.server.common.bean.CommonBean;

public interface ConversionIF {
    public void setData(String[] message, String convertTimeWithTimeZone);

    public Object getXmlObject();

    public String getData(String[] message);

    public String getFormat();

}

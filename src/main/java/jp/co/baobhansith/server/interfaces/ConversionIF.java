package jp.co.baobhansith.server.interfaces;

public interface ConversionIF {
    public void setData(String[] message, String convertTimeWithTimeZone);

    public Object getXmlObject();

    public String getData(String[] message);

    public String getFormat();

}

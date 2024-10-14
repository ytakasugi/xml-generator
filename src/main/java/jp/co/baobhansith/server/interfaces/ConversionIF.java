package jp.co.baobhansith.server.interfaces;

public interface ConversionIF<T> {
    public void setData(ConversionIF<?> oxm, String[] message, String convertTimeWithTimeZone);

    public Object getXmlObject();

    public String getData(String[] message);

    public String getFormat();

}

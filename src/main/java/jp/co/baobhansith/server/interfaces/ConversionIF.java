package jp.co.baobhansith.server.interfaces;

import jp.co.baobhansith.server.common.bean.CommonBean;

public interface ConversionIF {
    public void setData(CommonBean bean);

    public Object getRoot();

    public String getData(CommonBean bean);
}

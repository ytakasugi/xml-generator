package jp.co.baobhansith.server.common.bean;

import java.util.Arrays;

public class CommonBean {
    private String[] dataList;

    public String[] getDataList() {
        return dataList;
    }

    public void setDataList(String[] dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "CommonBean [dataList=" + Arrays.toString(dataList) + "]";
    }
}

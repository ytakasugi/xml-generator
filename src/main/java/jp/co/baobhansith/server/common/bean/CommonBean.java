package jp.co.baobhansith.server.common.bean;

import java.util.Arrays;
import java.sql.Timestamp;

public class CommonBean {
    private String id;
    private String seq;
    private Timestamp created;
    private String[] dataList;

    public CommonBean() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

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

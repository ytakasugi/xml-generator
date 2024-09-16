package jp.co.baobhansith.server.bean;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

public class Payload {
    private List<Info> infoList;

    @XmlElement(name = "Info")
    public List<Info> getInfoList() {
        return infoList;
    }

    public void setInfoList(List<Info> infoList) {
        this.infoList = infoList;
    }
}

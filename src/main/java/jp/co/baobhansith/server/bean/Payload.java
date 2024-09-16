package jp.co.baobhansith.server.bean;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

public class Payload {
    @XmlElement(name = "Info")
    private List<Info> infoList;

    public List<Info> getInfoList() {
        return infoList;
    }

    public void setInfoList(List<Info> infoList) {
        this.infoList = infoList;
    }
}

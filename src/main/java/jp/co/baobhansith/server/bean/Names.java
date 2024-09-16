package jp.co.baobhansith.server.bean;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

public class Names {
    private List<Name> nameList;

    @XmlElement(name = "Name")
    public List<Name> getNameList() {
        return nameList;
    }

    public void setNameList(List<Name> nameList) {
        this.nameList = nameList;
    }
}
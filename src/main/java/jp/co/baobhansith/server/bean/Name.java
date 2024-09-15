package jp.co.baobhansith.server.bean;

import javax.xml.bind.annotation.XmlElement;

public class Name {

    private String id;

    @XmlElement(name = "Id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

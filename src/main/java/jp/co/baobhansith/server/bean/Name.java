package jp.co.baobhansith.server.bean;

import javax.xml.bind.annotation.XmlElement;

public class Name {
    @XmlElement(name = "Id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
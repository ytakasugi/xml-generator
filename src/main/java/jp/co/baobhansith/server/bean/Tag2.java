package jp.co.baobhansith.server.bean;

import javax.xml.bind.annotation.XmlElement;

public class Tag2 {

    private Names names;

    @XmlElement(name = "Names")
    public Names getNames() {
        return names;
    }

    public void setNames(Names names) {
        this.names = names;
    }
}

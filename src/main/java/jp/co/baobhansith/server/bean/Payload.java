package jp.co.baobhansith.server.bean;

import javax.xml.bind.annotation.XmlElement;

public class Payload {
    private ParentTag parentTag;
    private Tag2 tag2;

    @XmlElement(name = "ParentTag")
    public ParentTag getParentTag() {
        return parentTag;
    }

    public void setParentTag(ParentTag parentTag) {
        this.parentTag = parentTag;
    }

    @XmlElement(name = "Tag2")
    public Tag2 getTag2() {
        return tag2;
    }

    public void setTag2(Tag2 tag2) {
        this.tag2 = tag2;
    }
}

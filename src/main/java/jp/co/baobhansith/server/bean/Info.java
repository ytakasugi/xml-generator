package jp.co.baobhansith.server.bean;

import javax.xml.bind.annotation.XmlElement;

public class Info {
    @XmlElement(name = "ParentTag")
    private ParentTag parentTag;
    @XmlElement(name = "Tag2")
    private Tag2 tag2;

    public ParentTag getParentTag() {
        return parentTag;
    }

    public void setParentTag(ParentTag parentTag) {
        this.parentTag = parentTag;

        if (parentTag != null && parentTag.isAllChildTagsEmpty()) {
            this.parentTag = null;
        }
    }

    public Tag2 getTag2() {
        return tag2;
    }

    public void setTag2(Tag2 tag2) {
        this.tag2 = tag2;
    }
}

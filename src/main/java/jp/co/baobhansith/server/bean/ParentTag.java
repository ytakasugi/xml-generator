package jp.co.baobhansith.server.bean;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

public class ParentTag {
    private List<ChildTag> childTags;

    @XmlElement(name = "ChildTag")
    public List<ChildTag> getChildTags() {
        return childTags;
    }

    public void setChildTags(List<ChildTag> childTags) {
        this.childTags = childTags;
    }
}
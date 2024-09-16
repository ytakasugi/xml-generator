package jp.co.baobhansith.server.bean;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

public class ParentTag {
    @XmlElement(name = "ChildTag")
    private List<ChildTag> childTags;

    public List<ChildTag> getChildTags() {
        return childTags;
    }

    public void setChildTags(List<ChildTag> childTags) {
        this.childTags = childTags;
        
        this.childTags.removeIf(childTag -> childTag.isAllFieldsNullOrEmpty());
    }

    public boolean isAllChildTagsEmpty() {
        return childTags == null || childTags.isEmpty();
    }
}
package jp.co.baobhansith.server.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import jp.co.baobhansith.server.conversion.AbstractXmlFormat;

@XmlRootElement(name = "Root")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "header", "payload" })
public class XmlFormatRootBean extends AbstractXmlFormat {
    @XmlAttribute(name = "xmlns:xsd")
    private String xsdNamespace = "http://www.w3.org/2001/XMLSchema";
    @XmlAttribute(name = "xmlns:xsi")
    private String xsiNamespace = "http://www.w3.org/2001/XMLSchema-instance";
    @XmlAttribute(name = "xmlns:xml")
    private String defaultNamespace = "http://www.w3.org/XML/1998/namespace";

    @XmlElement(name = "Payload")
    private Payload payload;
    @XmlElement(name = "Header")
    private Header header;

    public XmlFormatRootBean() {
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Header {
        @XmlElement(name = "Project")
        private String project;
        @XmlElement(name = "Timestamp")
        private String timestamp;
        @XmlElement(name = "Version")
        private String version;
        @XmlElement(name = "MessageId")
        private String messageId;

        public Header(String project, String timestamp, String version, String messageId) {
            this.project = project;
            this.timestamp = timestamp;
            this.version = version;
            this.messageId = messageId;
        }

        public String getProject() {
            return project;
        }

        public void setProject(String project) {
            this.project = project;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Payload {
        @XmlElement(name = "Info")
        private List<Info> infoList;

        public Payload() {
        }

        public List<Info> getInfoList() {
            return infoList;
        }

        public void setInfoList(List<Info> infoList) {
            this.infoList = infoList;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Info {
        @XmlAttribute(name = "xmlns:xsd")
        private String xsdNamespace;
        @XmlAttribute(name = "xmlns:xsi")
        private String xsiNamespace ;
        @XmlAttribute(name = "xmlns")
        private String defaultNamespace;
        @XmlElement(name = "Tag1")
        private Tag1 tag1;
        @XmlElement(name = "Tag2")
        private Tag2 tag2;

        public Info() {
        }

        public String getXsdNamespace() {
            return xsdNamespace;
        }

        public void setXsdNamespace(String xsdNamespace) {
            this.xsdNamespace = xsdNamespace;
        }

        public String getXsiNamespace() {
            return xsiNamespace;
        }

        public void setXsiNamespace(String xsiNamespace) {
            this.xsiNamespace = xsiNamespace;
        }

        public String getDefaultNamespace() {
            return defaultNamespace;
        }

        public void setDefaultNamespace(String defaultNamespace) {
            this.defaultNamespace = defaultNamespace;
        }

        public Tag1 getTag1() {
            return tag1;
        }

        public void setTag1(Tag1 tag1) {
            this.tag1 = tag1;
        }

        public Tag2 getTag2() {
            return tag2;
        }

        public void setTag2(Tag2 tag2) {
            this.tag2 = tag2;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Tag1 {
        @XmlElement(name = "ChildTag")
        private List<ChildTag> childTags;

        public Tag1() {
        }

        public List<ChildTag> getChildTags() {
            return childTags;
        }

        public void setChildTags(List<ChildTag> childTags) {
            this.childTags = childTags;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ChildTag {
        @XmlElement(name = "Enabled")
        private String enabled;

        @XmlElement(name = "EnabledDate")
        private String enabledDate;

        @XmlElement(name = "Version")
        private String version;

        @XmlElement(name = "VersionName")
        private String versionName;

        public ChildTag() {
        }

        public String getEnabled() {
            return enabled;
        }

        public void setEnabled(String enabled) {
            this.enabled = enabled;
        }

        public String getEnabledDate() {
            return enabledDate;
        }

        public void setEnabledDate(String enabledDate) {
            this.enabledDate = enabledDate;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Tag2 {
        @XmlElement(name = "Names")
        private Names names;

        public Names getNames() {
            return names;
        }

        public void setNames(Names names) {
            this.names = names;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Names {
        @XmlElement(name = "Name")
        private List<Name> nameList;

        public Names() {
        }

        public List<Name> getNameList() {
            return nameList;
        }

        public void setNameList(List<Name> nameList) {
            this.nameList = nameList;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Name {
        @XmlElement(name = "Id")
        private String id;

        public Name() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    @Override
    public String toString() {
        return "XmlFormatRootBean{" + "payload=" + payload + ", header=" + header + '}';
    }
}

package jp.co.baobhansith.server.bean;

import javax.xml.bind.annotation.XmlElement;

public class ChildTag {
    private String enabled;
    private String enabledDate;
    private String version;
    private String versionName;

    @XmlElement(name = "Enabled")
    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    @XmlElement(name = "EnabledDate")
    public String getEnabledDate() {
        return enabledDate;
    }

    public void setEnabledDate(String enabledDate) {
        this.enabledDate = enabledDate;
    }

    @XmlElement(name = "Version")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @XmlElement(name = "VersionName")
    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}
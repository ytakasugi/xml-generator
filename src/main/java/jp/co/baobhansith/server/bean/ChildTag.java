package jp.co.baobhansith.server.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;

@XmlAccessorType(XmlAccessType.FIELD)
public class ChildTag {
    @XmlElement(name = "Enabled")
    private String enabled;

    @XmlElement(name = "EnabledDate")
    private String enabledDate;

    @XmlElement(name = "Version")
    private String version;
    
    @XmlElement(name = "VersionName")
    private String versionName;

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        if (enabled == null || enabled.trim().isEmpty()) {
            this.enabled = null;
        } else {
            this.enabled = enabled;
        }
    }

    public String getEnabledDate() {
        return enabledDate;
    }

    public void setEnabledDate(String enabledDate) {
        if (enabledDate == null || enabledDate.trim().isEmpty()) {
            this.enabledDate = null;
        } else {
            this.enabledDate = enabledDate;
        }
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        if (version == null || version.isEmpty()) {
            this.version = null;
        } else {
            this.version = version;
        }
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        if (versionName == null || versionName.isEmpty()) {
            this.versionName = null;
        } else {
            this.versionName = versionName;
        }
    }

    // すべてのフィールドがnullまたは空かを確認するメソッド
    public boolean isAllFieldsNullOrEmpty() {
        return (isNullOrEmpty(enabled) &&
                isNullOrEmpty(enabledDate) &&
                isNullOrEmpty(version) &&
                isNullOrEmpty(versionName));
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }
}
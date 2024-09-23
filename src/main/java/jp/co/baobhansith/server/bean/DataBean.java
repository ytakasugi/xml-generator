package jp.co.baobhansith.server.bean;

public class DataBean {
    private String enabled;
    private String enabledDate;
    private String version;
    private String versionName;
    private String id;

    public DataBean() {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

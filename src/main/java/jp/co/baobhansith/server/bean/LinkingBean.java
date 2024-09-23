package jp.co.baobhansith.server.bean;

public class LinkingBean {
    private String enabled;
    private String enabledDate;
    private String version;
    private String versionName;
    private String id;

    public LinkingBean() {
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

    public String getCsv() {
        StringBuffer sb = new StringBuffer();
        sb.append(enabled);
        sb.append(",");
        sb.append(enabledDate);
        sb.append(",");
        sb.append(version);
        sb.append(",");
        sb.append(versionName);
        sb.append(",");
        sb.append(id);
        return sb.toString();
    }
}

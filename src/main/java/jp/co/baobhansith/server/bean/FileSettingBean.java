package jp.co.baobhansith.server.bean;

public class FileSettingBean {
    private final String id;
    private final String tag;
    private final String prefix;
    private final String namespace;

    public FileSettingBean(String id, String tag, String prefix, String namespace) {
        this.id = id;
        this.tag = tag;
        this.prefix = prefix;
        this.namespace = namespace;
    }

    public String getId() {
        return id;
    }

    public String getTag() {
        return tag;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getNamespace() {
        return namespace;
    }
}

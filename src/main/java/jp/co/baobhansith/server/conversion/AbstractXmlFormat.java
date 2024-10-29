package jp.co.baobhansith.server.conversion;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import jp.co.baobhansith.server.bean.XmlFormatRootBean;
import jp.co.baobhansith.server.bean.XmlFormatRootBean.Header;
import jp.co.baobhansith.server.bean.XmlFormatRootBean.Payload;
import jp.co.baobhansith.server.bean.XmlFormatRootBean.Tag1;
import jp.co.baobhansith.server.bean.XmlFormatRootBean.Tag2;
import jp.co.baobhansith.server.bean.XmlFormatRootBean.ChildTag;
import jp.co.baobhansith.server.bean.XmlFormatRootBean.Names;
import jp.co.baobhansith.server.bean.XmlFormatRootBean.Name;
import jp.co.baobhansith.server.bean.XmlFormatRootBean.Info;
import jp.co.baobhansith.server.interfaces.ConversionIF;

public class AbstractXmlFormat implements ConversionIF<XmlFormatRootBean> {
    private static final String XSD_NAMESPACE = "http://www.w3.org/2001/XMLSchema";
    private static final String XSI_NAMESPACE = "http://www.w3.org/2001/XMLSchema-instance";
    private static final String DEFAULT_NAMESPACE = "http://www.w3.org/XML/1998/Info#";

    private XmlFormatRootBean root;
    private Header header;
    private String project;
    private String timestamp;
    private String version;
    private String messageId;

    public AbstractXmlFormat() {
    }

    @Override
    public void setData(ConversionIF<?> oxm, String[] message, String convertTimeWithTimeZone) {
        this.project = "SampleProject";
        this.timestamp = convertTimeWithTimeZone;
        this.version = "1.0.0";
        this.messageId = UUID.randomUUID().toString();

        this.root = new XmlFormatRootBean();
        this.header = new Header(this.project, this.timestamp, this.version, this.messageId);

        List<Info> infoList = setInfo(message);

        Payload payload = new Payload();
        payload.setInfoList(infoList);

        root.setHeader(header);
        root.setPayload(payload);
    }

    @Override
    public Object getXmlObject() {
        return this.root;
    }

    @Override
    public String getData(String[] message) {
        return StringUtils.EMPTY;
    }

    @Override
    public String getFormat() {
        return "xml";
    }

    private List<Info> setInfo(String[] message) {
        List<Info> infoList = new ArrayList<>();

        for (int i = 0; i < message.length; i++) {
            String[] values = message[i].split(",");

            ChildTag childTag1 = new ChildTag();
            ChildTag childTag2 = new ChildTag();
            Tag1 tag1 = new Tag1();
            List<ChildTag> childTags = new ArrayList<>();
            Name name = new Name();
            Names names = new Names();
            List<Name> nameList = new ArrayList<>();
            Tag2 tag2 = new Tag2();
            Info info = new Info();

            childTag1.setEnabled(values[0]);
            childTag1.setEnabledDate(values[1]);
            childTag2.setVersion(values[2]);
            childTag2.setVersionName(values[3]);
            childTags.add(childTag1);
            childTags.add(childTag2);
            tag1.setChildTags(childTags);
            name.setId(values[4]);
            nameList.add(name);
            names.setNameList(nameList);
            tag2.setNames(names);

            if (infoList.isEmpty()) {
                info.setXsdNamespace(XSD_NAMESPACE);
                info.setXsiNamespace(XSI_NAMESPACE);
                info.setDefaultNamespace(DEFAULT_NAMESPACE);
                info.setTag1(tag1);
                info.setTag2(tag2);

                infoList.add(info);
            } else {
                info.setTag1(tag1);
                info.setTag2(tag2);

                infoList.add(info);
            }
        }

        return infoList;
    }
}

package jp.co.baobhansith.server.conversion;

import java.util.ArrayList;
import java.util.List;

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
import jp.co.baobhansith.server.common.bean.CommonBean;
import jp.co.baobhansith.server.interfaces.ConversionIF;

public abstract class AbstractXmlFormat implements ConversionIF {
    private XmlFormatRootBean root;
    private Header header;
    private String project;
    private String timestamp;
    private String version;
    private String messageId;

    public AbstractXmlFormat() {
    }

    @Override
    public void setData(CommonBean bean) {
        this.project = "SampleProject";
        this.timestamp = "2024-09-01T00:00:00+09:00";
        this.version = "1.0.0";
        this.messageId = "MessageId";

        this.root = new XmlFormatRootBean();
        this.header = new Header(this.project, this.timestamp, this.version, this.messageId);

        List<Info> infoList = new ArrayList<>();

        for (String csv : bean.getDataList()) {
            String[] values = csv.split(",");

            // ChildTagにデータを設定
            ChildTag childTag1 = new ChildTag();
            childTag1.setEnabled(values[0]);
            childTag1.setEnabledDate(values[1]);

            ChildTag childTag2 = new ChildTag();
            childTag2.setVersion(values[2]);
            childTag2.setVersionName(values[3]);

            Tag1 tag1 = new Tag1();
            List<ChildTag> childTags = new ArrayList<>();
            childTags.add(childTag1);
            childTags.add(childTag2);
            tag1.setChildTags(childTags);

            Name name = new Name();
            name.setId(values[4]);

            Names names = new Names();
            List<Name> nameList = new ArrayList<>();
            nameList.add(name);
            names.setNameList(nameList);

            Tag2 tag2 = new Tag2();
            tag2.setNames(names);

            Info info = new Info();
            info.setTag1(tag1);
            info.setTag2(tag2);

            infoList.add(info);
        }

        Payload payload = new Payload();
        payload.setInfoList(infoList);

        root.setHeader(header);
        root.setPayload(payload);
    }

    @Override
    public String getData(CommonBean bean) {
        return StringUtils.EMPTY;
    }
}

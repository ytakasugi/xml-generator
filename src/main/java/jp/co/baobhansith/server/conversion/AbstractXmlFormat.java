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

        Header header = new Header(this.project, this.timestamp, this.version, this.messageId);

        List<Info> infoList = generator(bean.getDataList(), Info.class);

        Payload payload = new Payload();
        payload.setInfoList(infoList);

        XmlFormatRootBean root = new XmlFormatRootBean();
        root.setHeader(header);
        root.setPayload(payload);
    }

    @Override
    public String getData(CommonBean bean) {
        return StringUtils.EMPTY;
    }


    public static <T> List<T> generator(String[] csvArray, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        for (String csv : csvArray) {
            list.add(parse(csv, clazz));
        }
        return list;
    }

    private static <T> T parse(String csv, Class<T> clazz) {
        String[] values = csv.split(",");
        if (clazz == Info.class) {
            ChildTag childTag1 = new ChildTag();
            childTag1.setEnabled(values[0]);
            childTag1.setEnabledDate(values[1]);

            ChildTag childTag2 = new ChildTag();
            childTag2.setVersion(values[2]);
            childTag2.setVersionName(values[3]);

            Tag1 parentTag = new Tag1();
            List<ChildTag> childTags = new ArrayList<>();
            childTags.add(childTag1);
            childTags.add(childTag2);
            parentTag.setChildTags(childTags);

            Name name = new Name();
            name.setId(values[4]);

            Names names = new Names();
            List<Name> nameList = new ArrayList<>();
            nameList.add(name);
            names.setNameList(nameList);

            Tag2 tag2 = new Tag2();
            tag2.setNames(names);

            Info info = new Info();
            info.setParentTag(parentTag);
            info.setTag2(tag2);

            return clazz.cast(info);
        }
        throw new IllegalArgumentException("Unsupported class: " + clazz);
    }
}

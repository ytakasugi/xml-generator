package jp.co.baobhansith.server;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import jp.co.baobhansith.server.bean.ChildTag;
import jp.co.baobhansith.server.bean.Name;
import jp.co.baobhansith.server.bean.Names;
import jp.co.baobhansith.server.bean.ParentTag;
import jp.co.baobhansith.server.bean.Payload;
import jp.co.baobhansith.server.bean.Root;
import jp.co.baobhansith.server.bean.Tag2;

public class OxmXmlGenerator {
    public static void main(String[] args) {
        // データの作成
        ChildTag childTag1 = new ChildTag();
        childTag1.setEnabled("true");
        childTag1.setEnabledDate("2024-09-01");

        ChildTag childTag2 = new ChildTag();
        childTag2.setVersion("1.0.0");
        childTag2.setVersionName("Version-1.0.0");

        ParentTag parentTag = new ParentTag();
        List<ChildTag> childTags = new ArrayList<>();
        childTags.add(childTag1);
        childTags.add(childTag2);
        parentTag.setChildTags(childTags);

        Name name = new Name();
        name.setId("F000000001");

        Names names = new Names();
        List<Name> nameList = new ArrayList<>();
        nameList.add(name);
        names.setNameList(nameList);

        Tag2 tag2 = new Tag2();
        tag2.setNames(names);

        Payload payload = new Payload();
        payload.setParentTag(parentTag);
        payload.setTag2(tag2);

        Root root = new Root();
        root.setPayload(payload);

        // JAXBを使用してオブジェクトをXMLに変換
        try {
            Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
            marshaller.setClassesToBeBound(Root.class, Payload.class, ParentTag.class, ChildTag.class, Tag2.class, Names.class, Name.class);

            StringWriter writer = new StringWriter();
            marshaller.marshal(root, new StreamResult(writer));
            System.out.println(writer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
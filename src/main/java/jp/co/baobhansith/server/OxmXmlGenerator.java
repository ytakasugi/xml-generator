package jp.co.baobhansith.server;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.List;

import jp.co.baobhansith.server.bean.ChildTag;
import jp.co.baobhansith.server.bean.Name;
import jp.co.baobhansith.server.bean.Names;
import jp.co.baobhansith.server.bean.ParentTag;
import jp.co.baobhansith.server.bean.Payload;
import jp.co.baobhansith.server.bean.Root;
import jp.co.baobhansith.server.bean.Tag2;
import jp.co.baobhansith.server.bean.Info;
import jp.co.baobhansith.server.util.CustomMarshaller;
import jp.co.baobhansith.server.util.Parser;

public class OxmXmlGenerator {
    public static void main(String[] args) {
        // データの作成
        String csv1 = "true,2024-09-01,1.0.0,Version-1.0.0,F000000001";
        String csv2 = "true,2024-09-01,1.0.0,Version-1.0.0,F000000002";
        String[] arr = { csv1, csv2 };

        List<Info> infoList = Parser.generator(arr, Info.class);

        Payload payload = new Payload();
        payload.setInfoList(infoList);

        Root root = new Root();
        root.setPayload(payload);

        // JAXBを使用してオブジェクトをXMLに変換
        try {
            // Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
            // marshaller.setClassesToBeBound(Root.class, Payload.class, Info.class, ParentTag.class, ChildTag.class,
            //         Tag2.class, Names.class, Name.class);

            // StringWriter writer = new StringWriter();
            // writer.write("<?xml version=\"1.1\" encoding=\"ISO-8859-1\" standalone=\"no\"?>\n");
            // marshaller.marshal(root, new StreamResult(writer));
            // System.out.println(writer.toString());
            String xmlDeclaration = "<?xml version=\"1.1\" encoding=\"ISO-8859-1\" standalone=\"no\"?>";
            String xmlOutput = CustomMarshaller.marshal(root, xmlDeclaration);
            System.out.println(xmlOutput);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
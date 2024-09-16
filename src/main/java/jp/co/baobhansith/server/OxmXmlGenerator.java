package jp.co.baobhansith.server;

import java.util.List;

import jp.co.baobhansith.server.bean.Payload;
import jp.co.baobhansith.server.bean.Root;
import jp.co.baobhansith.server.bean.Info;
import jp.co.baobhansith.server.util.CustomMarshaller;
import jp.co.baobhansith.server.util.Parser;

public class OxmXmlGenerator {
    private static String XML_DECLARATION = "<?xml version=\"1.1\" encoding=\"UTF-8\"?>";
    public static void main(String[] args) {
        // データの作成
        String csv1 = "true,2024-09-01,1.0.0,Version-1.0.0,F000000001";
        String csv2 = "true,2024-09-01,1.0.0,Version-1.0.0,F000000002";
        String csv3 = ",,,,F000000003";
        String csv4 = "true,2024-09-01,,,F000000002";
        String[] arr = { csv1, csv2, csv3, csv4};

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
            // marshaller.marshal(root, new StreamResult(writer));
            // System.out.println(writer.toString());
            String xmlOutput = CustomMarshaller.marshal(root, XML_DECLARATION);
            System.out.println(xmlOutput);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
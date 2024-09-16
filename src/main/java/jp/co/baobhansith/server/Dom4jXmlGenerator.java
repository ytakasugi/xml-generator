package jp.co.baobhansith.server;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class Dom4jXmlGenerator {

    public static void main(String[] args) {
        // ルート要素を作成
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("Root");

        // Payload要素
        Element payload = createElementWithChildren(root, "Payload");

        // ParentTag要素
        Element parentTag = createElementWithChildren(payload, "ParentTag");

        // ChildTag1
        Element childTag1 = createElementWithChildren(parentTag, "ChildTag");
        addElementIfNotEmpty(childTag1, "Enabled", "true");
        addElementIfNotEmpty(childTag1, "EnabledDate", "2024-09-01");

        // ChildTag2
        Element childTag2 = createElementWithChildren(parentTag, "ChildTag");
        addElementIfNotEmpty(childTag2, "Version", "1.0.0");
        addElementIfNotEmpty(childTag2, "VersionName", "Version-1.0.0");

        // Tag2
        Element tag2 = createElementWithChildren(payload, "Tag2");

        // Namesとその中のName
        Element names = createElementWithChildren(tag2, "Names");
        Element name = createElementWithChildren(names, "Name");
        addElementIfNotEmpty(name, "Id", "F000000001");

        // XMLを文字列に変換して出力
        System.out.println(document.asXML());
    }

    // 要素を作成し、親に追加するが、子要素が追加されなければ親も作成しない
    private static Element createElementWithChildren(Element parent, String elementName) {
        Element element = parent.addElement(elementName);
        return element;
    }

    // 要素に値があれば追加する
    private static void addElementIfNotEmpty(Element parent, String elementName, String value) {
        if (value != null && !value.isEmpty()) {
            parent.addElement(elementName).setText(value);
        }
    }
}
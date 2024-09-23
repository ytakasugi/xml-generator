package jp.co.baobhansith.server;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import jp.co.baobhansith.server.common.bean.CommonBean;
import jp.co.baobhansith.server.interfaces.ConversionIF;

import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamResult;

import java.io.FileWriter;
import java.io.StringWriter;
// import java.nio.file.Files;
// import java.nio.file.Paths;
// import java.util.HashMap;
// import java.util.List;
import java.util.Map;

public class JAXB2 {

    public void convert(CommonBean bean) {
        Jaxb2Marshaller marshaller = null;

        try {
            // 対象のBeanクラスをここで動的に設定できるようにする
            Class<?> clazz = Class.forName("jp.co.baobhansith.server.bean.XmlFormatRootBean");
            // 対象のBeanクラスのインスタンスを生成する
            ConversionIF conversion = (ConversionIF) clazz.getConstructor().newInstance();
            marshaller = new Jaxb2Marshaller();
            marshaller.setClassesToBeBound(clazz);
            // フォーマットされた出力を有効にする
            marshaller.setMarshallerProperties(Map.of(
                    Marshaller.JAXB_FORMATTED_OUTPUT, true));
            conversion.setData(bean);

            // XMLの出力
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);

            // 対象のBeanをマーシャルする
            marshaller.marshal(conversion.getXmlObject(), result);

            // XML結果を出力
            String xmlOutput = writer.toString();
            xmlOutput = xmlOutput.replace("\n", "\r\n");

            StringBuffer outputFileName = new StringBuffer();
            outputFileName.append("output");
            outputFileName.append("_");
            outputFileName.append(bean.getId());
            outputFileName.append("_");
            outputFileName.append(bean.getCreated());
            outputFileName.append("_");
            outputFileName.append(bean.getSeq());
            outputFileName.append(".xml");

            // ファイルに出力
            try (FileWriter fileWriter = new FileWriter(outputFileName.toString())) {
                fileWriter.write(xmlOutput);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * CSVファイルを読み込んで設定をマップに格納するメソッド
     */
    // private static Map<String, String> readConfigFile(String filePath) throws Exception {
    //     List<String> lines = Files.readAllLines(Paths.get(filePath));
    //     Map<String, String> configMap = new HashMap<>();

    //     // 1行目はヘッダーとして扱い、2行目の内容を設定に読み取る
    //     if (lines.size() > 1) {
    //         String[] headers = lines.get(0).split(",");
    //         String[] values = lines.get(1).split(",");

    //         for (int i = 0; i < headers.length; i++) {
    //             configMap.put(headers[i], values[i]);
    //         }
    //     }
    //     return configMap;
    // }
}

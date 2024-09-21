package jp.co.baobhansith.server;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import jp.co.baobhansith.server.common.bean.CommonBean;
import jp.co.baobhansith.server.interfaces.ConversionIF;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

public class JAXB2 {
    public static void main(String[] args) throws Exception {
        // 対象のBeanクラスをここで動的に設定できるようにする（例: XmlFormatRootBean）
        Class<?> clazz = Class.forName("jp.co.baobhansith.server.bean.XmlFormatRootBean");

        // 対象のBeanクラスのインスタンスを生成する
        ConversionIF conversion = (ConversionIF) clazz.getConstructor().newInstance();

        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        // 複数のクラスを対象にできるように、ここにクラスを設定
        marshaller.setClassesToBeBound(clazz);

        // データの作成
        String csv1 = "true,2024-09-01,1.0.0,Version-1.0.0,F000000001";
        String csv2 = "true,2024-09-01,1.0.0,Version-1.0.0,F000000002";
        String csv3 = ",,,,F000000003";
        String csv4 = "true,2024-09-01, , ,F000000004";
        String[] arr = { csv1, csv2, csv3, csv4 };

        CommonBean bean = new CommonBean();
        bean.setDataList(arr);

        System.out.println(bean.toString());

        // 対象のBeanクラスにデータをセットする
        conversion.setData(bean);

        // XMLの出力
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);

        // 対象のBeanをマーシャルする
        marshaller.marshal(conversion, result);

        // XML結果を出力
        String xmlOutput = writer.toString();
        System.out.println(xmlOutput);
    }
}

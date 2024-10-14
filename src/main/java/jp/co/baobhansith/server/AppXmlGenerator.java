package jp.co.baobhansith.server;

import java.util.List;
import java.util.ArrayList;

import jp.co.baobhansith.server.bean.DataBean;
import jp.co.baobhansith.server.bean.LinkingBean;
import jp.co.baobhansith.server.common.bean.CommonBean;

public class AppXmlGenerator {
    public static void main(String[] args) {
        List<DataBean> dataList = generateData(100);
        List<LinkingBean> linkingBeanList = new ArrayList<>();
        List<String> csvRecords = new ArrayList<>();
        int batchSize = 10;

        for (DataBean data : dataList) {
            LinkingBean linkingBean = new LinkingBean();
            linkingBean.setEnabled(data.getEnabled());
            linkingBean.setEnabledDate(data.getEnabledDate());
            linkingBean.setVersion(data.getVersion());
            linkingBean.setVersionName(data.getVersionName());
            linkingBean.setId(data.getId());

            linkingBeanList.add(linkingBean);

            if (linkingBeanList.size() == batchSize) {
                for (LinkingBean lb : linkingBeanList) {
                    String csvRecord = String.join(",",
                        lb.getEnabled(),
                        lb.getEnabledDate(),
                        lb.getVersion(),
                        lb.getVersionName(),
                        lb.getId()
                    );
                    csvRecords.add(csvRecord);
                }

                // CommonBeanにCSVレコードを設定
                CommonBean commonBean = new CommonBean();
                commonBean.setDataList(csvRecords.toArray(new String[0]));
                commonBean.setId("0000_00_000_1");

                // JAXB2クラスに引き渡してXML化
                DataController controller = new DataController();
                controller.processData(commonBean);

                // リストをクリア
                linkingBeanList.clear();
                csvRecords.clear();
            }
        }

        // 残ったデータを処理
        if (!linkingBeanList.isEmpty()) {
            for (LinkingBean lb : linkingBeanList) {
                String csvRecord = String.join(",",
                    lb.getEnabled(),
                    lb.getEnabledDate(),
                    lb.getVersion(),
                    lb.getVersionName(),
                    lb.getId()
                );
                csvRecords.add(csvRecord);
            }

            // CommonBeanにCSVレコードを設定
            CommonBean commonBean = new CommonBean();
            commonBean.setDataList(csvRecords.toArray(new String[0]));
            commonBean.setId("0000_00_000_1");

            // JAXB2クラスに引き渡してXML化
            DataController controller = new DataController();
            controller.processData(commonBean);
        }
    }

    private static List<DataBean> generateData(int num) {
        List<DataBean> beanList = new ArrayList<>();

        for (int i = 0; i < num; i++) {
            DataBean data = new DataBean();
            data.setEnabled("true");
            data.setEnabledDate("2024-09-01");
            data.setVersion("1.0.0");
            data.setVersionName("Version-1.0.0");
            data.setId("F00000000" + i);
            beanList.add(data);
        }

        return beanList;
    }

    
}

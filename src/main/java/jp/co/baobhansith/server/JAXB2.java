package jp.co.baobhansith.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import jp.co.baobhansith.server.common.bean.CommonBean;
import jp.co.baobhansith.server.interfaces.ConversionIF;

import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamResult;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import jp.co.baobhansith.server.util.ConversionException;

public class JAXB2 {
    private static final Logger logger = LogManager.getLogger(JAXB2.class);

    // ######################################################################################
    // ## クラス定数
    // ######################################################################################
    private static final String DATE_FORMAT = "yyyyMMdd'T'HHmmss";
    private static final String DATE_FORMAT_WITH_TIMEZONE = "yyyy-MM-dd'T'HH:mm:ssXXX";
    private static final String FILE_NAME_ZERO_PADDING = "0";

    // ######################################################################################
    // ## メンバ変数
    // ######################################################################################
    private String[] message;
    private String id;
    private String seq;
    private Timestamp timestamp;
    private String convertTime;
    private String convertTimeWithTimeZone;
    private String convertMessage;
    private String classPath;
    private static ConcurrentHashMap<String, Long> timeStampMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, AtomicInteger> timestampCounterMap = new ConcurrentHashMap<>();

    public JAXB2(CommonBean bean) {
        this.message = bean.getDataList();
        this.id = bean.getId();
        this.seq = bean.getSeq();
        this.timestamp = bean.getCreated();
        // this.timestamp = System.currentTimeMillis();

        // synchronized (this) {
        //     if (!timeStampMap.containsKey(bean.getId())) {
        //         timeStampMap.put(bean.getId(), System.currentTimeMillis());
        //     } else {
        //         long lastTimestamp = timeStampMap.get(bean.getId());
        //         if (this.timestamp <= lastTimestamp) {
        //             this.timestamp = this.timestamp + 1000;
        //         }
        //         timeStampMap.put(id, this.timestamp);
        //     }
        // }
        this.classPath = "jp.co.baobhansith.server.bean.XmlFormatRootBean";
    }

    private void setConvertTime() {
        // yyyyMMddHHmmss形式に変換
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        this.convertTime = localDateTime.format(formatter);

        // ISO8601形式に変換
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        DateTimeFormatter isoFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_WITH_TIMEZONE);
        this.convertTimeWithTimeZone = zonedDateTime.format(isoFormatter);
    }

    public boolean convertFormat() throws ConversionException {
        try {
            setConvertTime();
            if (!convertMessage()) {
                return false;
            }
            output(this.convertMessage);
            return true;
        } catch (ConversionException e) {
            logger.error("convertMessage failed.", e);
            return false;
        } catch (Exception e) {
            logger.error("convertMessage failed.", e);
            return false;
        }
    }

    public boolean convertMessage() throws ConversionException {
        Jaxb2Marshaller marshaller = null;

        try {
            // 対象のBeanクラスをここで動的に設定できるようにする
            Class<?> clazz = Class.forName(this.classPath);
            // 対象のBeanクラスのインスタンスを生成する
            ConversionIF<?> conversion = (ConversionIF<?>) clazz.getConstructor().newInstance();

            if (conversion.getFormat().equals("xml")) {
                StringWriter writer = new StringWriter();
                StreamResult result = new StreamResult(writer);
                marshaller = new Jaxb2Marshaller();

                marshaller.setClassesToBeBound(clazz);

                // フォーマットされた出力を有効にする
                marshaller.setMarshallerProperties(Map.of(
                        Marshaller.JAXB_FORMATTED_OUTPUT, true));
                conversion.setData(conversion, this.message, this.convertTimeWithTimeZone);
                // 対象のBeanをマーシャルする
                marshaller.marshal(conversion.getXmlObject(), result);

                // XML結果を出力
                this.convertMessage = writer.toString();
                this.convertMessage = this.convertMessage.replace("\n", "\r\n");
                return true;
            } else {
                // フォーマットがxml以外の場合は処理を行わない
                return true;
            }

        } catch (NoSuchMethodException e) {
            // メソッドが存在しない場合の処理
            logger.error("指定したメソッドが見つかりません: " + e.getMessage());
            throw new ConversionException("指定したメソッドが見つかりません。", e);

        } catch (SecurityException e) {
            // セキュリティ違反が発生した場合の処理
            logger.error("セキュリティエラーが発生しました: " + e.getMessage());
            throw new ConversionException("セキュリティエラーが発生しました。", e);

        } catch (InstantiationException e) {
            // インスタンス化に失敗した場合の処理
            logger.error("インスタンス化に失敗しました。抽象クラスまたはインターフェースの可能性があります。: " + e.getMessage());
            throw new ConversionException("インスタンス化に失敗しました。", e);

        } catch (IllegalAccessException e) {
            // アクセス権がない場合の処理
            logger.error("アクセスできません: " + e.getMessage());
            throw new ConversionException("アクセス権限エラーが発生しました。", e);

        } catch (IllegalArgumentException e) {
            // 不正な引数が渡された場合の処理
            logger.error("不正な引数が渡されました: " + e.getMessage());
            throw new ConversionException("不正な引数が渡されました。", e);

        } catch (InvocationTargetException e) {
            // 呼び出したメソッドが例外をスローした場合の処理
            Throwable cause = e.getCause();
            logger.error("メソッド呼び出し中に例外が発生しました: " + cause.getMessage());
            throw new ConversionException("メソッド呼び出し中に例外が発生しました。", cause);

        } catch (ClassNotFoundException e) {
            // クラスが見つからない場合の処理
            logger.error("対象のクラスがみつかりません。" + this.classPath);
            return false;

        } catch (XmlMappingException e) {
            // XMLマッピングに失敗した場合の処理
            logger.error("XMLのマッピングに失敗しました。" + this.id);
            return false;

        } finally {
            marshaller = null;
        }
    }

    private synchronized void output(String convertMessage) {
        String outputFileName = null;

        try {
            outputFileName = generateOutputFileName();

            // if (this.id.equals("0000_00_000_1")) {
            //     Thread.sleep(1000);
            //     this.timestamp = new Timestamp(System.currentTimeMillis());
            //     setConvertTime();
            //     outputFileName = generateOutputFileName();
            // }

            // ファイルに出力
            try (FileWriter fileWriter = new FileWriter(outputFileName.toString())) {
                fileWriter.write(this.convertMessage);
            } catch (IOException e) {
                logger.error("Failed to write to file" + outputFileName, e);
            }
        } catch (Exception e) {
            logger.error("Failed to write to file" + outputFileName, e);
        } finally {
            outputFileName = null;
        }
    }

    private String generateOutputFileName() {
        StringBuffer outputFileName = new StringBuffer();
        outputFileName.append("output");
        outputFileName.append("-");
        outputFileName.append(this.id);
        outputFileName.append("-");
        outputFileName.append(this.convertTime);
        outputFileName.append("-");
        outputFileName.append(this.seq);
        outputFileName.append(FILE_NAME_ZERO_PADDING);
        outputFileName.append(".xml");
        return outputFileName.toString();
    }
}

package jp.co.baobhansith.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import jp.co.baobhansith.server.common.bean.CommonBean;
import jp.co.baobhansith.server.interfaces.ConversionIF;

import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamResult;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jp.co.baobhansith.server.util.BaobhansithException;
import jp.co.baobhansith.server.util.BaobhansithUtility;

public class JAXB2 {
    private static final Logger logger = LogManager.getLogger(JAXB2.class);

    // ######################################################################################
    // ## クラス定数
    // ######################################################################################
    private static final String DATE_FORMAT = "yyyyMMdd'T'HHmmss";
    private static final String DATE_FORMAT_WITH_TIMEZONE = "yyyy-MM-dd'T'HH:mm:ssXXX";
    private static final int INIT_SEQUENCE_VALUE = 0;
    private static final int MAX_SEQUENCE_VALUE = 9999;
    private static final String SEQUENCE_FORMAT = "%04d";
    private static final String FILE_NAME_ZERO_PADDING = "0";
    private static final String HOST_NAME = "localhost";
    private static final String HYPYEN = "-";
    private static final String CONFIG_PATH = "/home/ytakasugi/java-workspace/baobhansith/config.csv";

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
    private Path outputDirectory;
    private static ConcurrentHashMap<String, SequenceManager> sequenceMap = new ConcurrentHashMap<>();

    public JAXB2(CommonBean bean) {
        this.message = bean.getDataList();
        this.id = bean.getId();
        this.seq = bean.getSeq();
        this.timestamp = bean.getCreated();
        this.classPath = "jp.co.baobhansith.server.bean.XmlFormatRootBean";
    }

    private static class SequenceManager {
        private String timestamp;
        private int sequence;

        private SequenceManager(String timestamp, int sequence) {
            this.timestamp = timestamp;
            this.sequence = sequence;
        }
    }

    public boolean convertFormat() throws BaobhansithException {
        try {
            setConvertTime();

            sequenceNumbering(this.id, this.convertTime);

            getOutputDirectory();

            convertMessage();

            output(this.convertMessage);
            return true;
        } catch (BaobhansithException e) {
            logger.error("convertMessage failed.", e);
            return false;
        } catch (Exception e) {
            logger.error("convertMessage failed.", e);
            return false;
        }
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

    private synchronized void sequenceNumbering(String id, String timestamp) {
        sequenceMap.compute(this.id, (k, value) -> {
            // [分岐][パラメータ]IDに対応するSequenceManagerオブジェクトが存在しない場合
            // [パラメータ]IDに対応するSequenceManagerオブジェクトが存在しない場合、新規作成
            if (value == null) {
                // SequenceManagerオブジェクトを生成し、タイムスタンプと初期シーケンス値を設定
                value = new SequenceManager(this.convertTime, INIT_SEQUENCE_VALUE);
                // [分岐][パラメータ]IDに対応するSequenceManagerインスタンスが存在する場合
            } else {
                // [分岐]タイムスタンプが異なる場合
                if (!value.timestamp.equals(this.convertTime)) {
                    // [処理]Sequenceオブジェクトに[パラメータ]タイムスタンプを設定し、シーケンスを初期化
                    value.timestamp = this.convertTime;
                    value.sequence = INIT_SEQUENCE_VALUE;
                    // [分岐]タイムスタンプが同じ場合
                } else {
                    // [分岐]シーケンスが最大値に達した場合
                    if (value.sequence >= MAX_SEQUENCE_VALUE) {
                        // [処理]シーケンスを初期化
                        value.sequence = INIT_SEQUENCE_VALUE;
                    }
                }
            }
            // [処理]シーケンスをインクリメント
            value.sequence++;
            // [戻り値]更新後のSequenceManagerオブジェクトを返却し、Mapに格納
            // [補足]computeメソッドの戻り値をMapに格納する
            return value;
        });
        this.seq = String.format(SEQUENCE_FORMAT, sequenceMap.get(id).sequence);
    }

    private void getOutputDirectory() throws IOException {
        String[] record = BaobhansithUtility.getRowByKey(CONFIG_PATH, this.id);
        record = BaobhansithUtility.getNonEmptyElement(record, 3);

        this.outputDirectory = Paths.get(record[0]);
    }

    private void convertMessage() throws BaobhansithException {
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
            } else {
                // フォーマットがxml以外の場合
            }

        } catch (NoSuchMethodException e) {
            // メソッドが存在しない場合の処理
            logger.error("指定したメソッドが見つかりません: " + e.getMessage());
            throw new BaobhansithException("指定したメソッドが見つかりません。", e);

        } catch (SecurityException e) {
            // セキュリティ違反が発生した場合の処理
            logger.error("セキュリティエラーが発生しました: " + e.getMessage());
            throw new BaobhansithException("セキュリティエラーが発生しました。", e);

        } catch (InstantiationException e) {
            // インスタンス化に失敗した場合の処理
            logger.error("インスタンス化に失敗しました。抽象クラスまたはインターフェースの可能性があります。: " + e.getMessage());
            throw new BaobhansithException("インスタンス化に失敗しました。", e);

        } catch (IllegalAccessException e) {
            // アクセス権がない場合の処理
            logger.error("アクセスできません: " + e.getMessage());
            throw new BaobhansithException("アクセス権限エラーが発生しました。", e);

        } catch (IllegalArgumentException e) {
            // 不正な引数が渡された場合の処理
            logger.error("不正な引数が渡されました: " + e.getMessage());
            throw new BaobhansithException("不正な引数が渡されました。", e);

        } catch (InvocationTargetException e) {
            // 呼び出したメソッドが例外をスローした場合の処理
            Throwable cause = e.getCause();
            logger.error("メソッド呼び出し中に例外が発生しました: " + cause.getMessage());
            throw new BaobhansithException("メソッド呼び出し中に例外が発生しました。", cause);

        } catch (ClassNotFoundException e) {
            // クラスが見つからない場合の処理
            logger.error("対象のクラスがみつかりません。" + this.classPath);

        } catch (XmlMappingException e) {
            // XMLマッピングに失敗した場合の処理
            logger.error("XMLのマッピングに失敗しました。" + this.id);

        } finally {
            marshaller = null;
        }
    }

    private void output(String convertMessage) {
        String outputFileName = null;

        try {
            outputFileName = generateOutputFileName();
            Path outputFilePath = this.outputDirectory.resolve(outputFileName);

            // ファイルに出力
            try (BufferedWriter bufferWriter = Files.newBufferedWriter(outputFilePath)) {
                bufferWriter.write(this.convertMessage);
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

        outputFileName.append(this.id);
        outputFileName.append(HYPYEN);
        outputFileName.append(this.convertTime);
        outputFileName.append(HYPYEN);
        outputFileName.append(this.seq);
        outputFileName.append(FILE_NAME_ZERO_PADDING);
        outputFileName.append(HYPYEN);
        outputFileName.append(HOST_NAME);
        outputFileName.append(".xml");
        return outputFileName.toString();
    }
}

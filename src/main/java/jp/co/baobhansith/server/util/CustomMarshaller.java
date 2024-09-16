package jp.co.baobhansith.server.util;

import javax.xml.bind.Marshaller;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

public class CustomMarshaller {
    public static String marshal(Object jaxbElement, String xmlDeclaration) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(jaxbElement.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

        StringWriter writer = new StringWriter();
        if (xmlDeclaration != null && !xmlDeclaration.isEmpty()) {
            writer.write(xmlDeclaration + "\n");
        }
        marshaller.marshal(jaxbElement, new StreamResult(writer));
        return writer.toString();
    }
}
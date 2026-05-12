package pl.pwr.ite.dynak.lib.utils;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

public class XsltTransformer {
    public String transform(String xml, String xslt) throws TransformerException {
        Source xmlSource = new StreamSource(new StringReader(xml));
        Source xsltSource = new StreamSource(new StringReader(xslt));

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer(xsltSource);

        StringWriter writer = new StringWriter();
        transformer.transform(xmlSource, new StreamResult(writer));
        return writer.toString();
    }
}

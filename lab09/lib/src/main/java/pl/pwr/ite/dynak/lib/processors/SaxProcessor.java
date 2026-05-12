package pl.pwr.ite.dynak.lib.processors;

import jakarta.xml.bind.JAXBException;
import org.xml.sax.SAXException;
import pl.pwr.ite.dynak.lib.utils.RecallResponse;
import pl.pwr.ite.dynak.lib.utils.SaxHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

public class SaxProcessor implements IProcessor {
    private final SaxHandler saxHandler = new SaxHandler();
    @Override
    public RecallResponse process(File file) throws JAXBException, ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();

        parser.parse(file, saxHandler);

        return saxHandler.getRecalls();
    }
}



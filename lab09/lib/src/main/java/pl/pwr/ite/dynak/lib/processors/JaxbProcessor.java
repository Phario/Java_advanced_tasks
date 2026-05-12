package pl.pwr.ite.dynak.lib.processors;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import pl.pwr.ite.dynak.lib.utils.RecallResponse;

import java.io.File;

public class JaxbProcessor implements IProcessor {
    @Override
    public RecallResponse process(File file) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(RecallResponse.class);
        return (RecallResponse) context.createUnmarshaller()
                .unmarshal(file);
    }
}

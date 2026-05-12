package pl.pwr.ite.dynak.lib.processors;

import jakarta.xml.bind.JAXBException;
import pl.pwr.ite.dynak.lib.utils.RecallResponse;

import java.io.File;

public interface IProcessor {
    RecallResponse process(File file) throws Exception;
}

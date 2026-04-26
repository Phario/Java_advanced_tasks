package pl.pwr.ite.dynak.app;

import ex.api.AnalysisService;

import java.net.URL;
import java.util.ServiceLoader;

public class AnalysisServiceLoader {
    public AnalysisService loadAnalysisService(URL name) {
        ServiceLoader<AnalysisService> loader = ServiceLoader.load(AnalysisService.class);

        for (AnalysisService service : loader) {
            if (service.getName().equals(name)) {
                return service;
            }
        }

        return null;
    }
}

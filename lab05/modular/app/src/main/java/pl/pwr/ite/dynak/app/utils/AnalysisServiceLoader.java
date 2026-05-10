package pl.pwr.ite.dynak.app.utils;

import ex.api.AnalysisService;

import java.net.URL;
import java.util.ServiceLoader;

public class AnalysisServiceLoader {
    public AnalysisService loadAnalysisService(String name) {
        ServiceLoader<AnalysisService> loader = ServiceLoader.load(AnalysisService.class);

        for (AnalysisService service : loader) {
            if (service.getName().equals(name)) {
                return service;
            }
        }

        return null;
    }
}

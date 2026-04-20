module services {
    requires serviceloader.example;

    exports pl.pwr.ite.dynak.services;

    provides ex.api.AnalysisService with
            pl.pwr.ite.dynak.services.KMedianAlgorithm,
            pl.pwr.ite.dynak.services.KMeanAlgorithm;
}
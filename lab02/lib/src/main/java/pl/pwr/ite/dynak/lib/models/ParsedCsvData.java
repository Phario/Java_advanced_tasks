package pl.pwr.ite.dynak.lib.models;

import java.util.List;

public record ParsedCsvData(
        List<CsvData> csvDataList,
        String mostCommonCar,
        String longestName,
        String mostCommonGender,
        String shortestLastName,
        String mostCommonName,
        Integer taxDodgerAmount,
        String mostCommonCity
) {}

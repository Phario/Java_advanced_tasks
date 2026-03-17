package pl.pwr.ite.dynak.lib.parser;

import pl.pwr.ite.dynak.lib.models.CsvData;
import pl.pwr.ite.dynak.lib.models.ParsedCsvData;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CsvParser implements IFileParser<ParsedCsvData> {
    @Override
    public ParsedCsvData parse(File file) throws IOException {
        List<CsvData> csvDataList = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(file.toPath())) {
            String line;

            String header = br.readLine();
            while ((line = br.readLine()) != null) {
                csvDataList.add(parseCsvLine(line));
            }

        }

        return new ParsedCsvData(
                csvDataList.stream().limit(10).toList(),
                getMostCommonCar(csvDataList),
                getLongestName(csvDataList),
                getMostCommonGender(csvDataList),
                getShortestLastName(csvDataList),
                getMostCommonName(csvDataList),
                getTaxDodgerAmount(csvDataList),
                getMostCommonCity(csvDataList)
        );
    }

    private CsvData parseCsvLine(String line) {
        String[] parts = line.split(",");

        return new CsvData(parts[0], parts[1], parts[2], parts[3], Boolean.parseBoolean(parts[4]), parts[5]);
    }

    private String getLongestName(List<CsvData> csvDataList) {
        return csvDataList.stream()
                .map(CsvData::name)
                .max(Comparator.comparingInt(String::length))
                .orElse(null);
    }

    private String getShortestLastName(List<CsvData> csvDataList) {
        return csvDataList.stream()
                .map(CsvData::lastName)
                .min(Comparator.comparingInt(String::length))
                .orElse(null);
    }


    private Integer getTaxDodgerAmount(List<CsvData> csvDataList) {
        return (int) csvDataList.stream()
                .filter(CsvData::taxDodger)
                .count();
    }

    private String getMostCommonCity(List<CsvData> csvDataList) { return getMostCommon(csvDataList, CsvData::city); }

    private String getMostCommonName(List<CsvData> csvDataList) { return getMostCommon(csvDataList, CsvData::name); }

    private String getMostCommonGender(List<CsvData> csvDataList) { return getMostCommon(csvDataList, CsvData::gender); }

    private String getMostCommonCar(List<CsvData> csvDataList) { return getMostCommon(csvDataList, CsvData::car); }

    private String getMostCommon(List<CsvData> list, Function<CsvData, String> getter) {
        return list.stream()
                .collect(Collectors.groupingBy(getter, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("—");
    }
}

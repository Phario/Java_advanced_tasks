package pl.pwr.ite.dynak.lib.models;

public record CsvData(
        String name,
        String lastName,
        String gender,
        String car,
        boolean taxDodger,
        String city
) {}
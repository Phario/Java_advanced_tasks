package ex.api;

/**
 * Interfejs serwisu pozwalającego przeprowadzić analizę danych.
 * Zakładamy, że serwis działa asynchronicznie.
 * Na początek należy do serwisu załadować dane.
 * Potem można z serwisu pobrać wyniki analizy.
 * W przypadku niepowodzenia wykonania jakiejś metody wyrzucony ma być wyjątek.
 *
 * @author tkubik
 *
 */
public interface AnalysisService {
    // metoda ustawiająca opcje algorytmu (jeśli takowe są potrzebne)
    void setOptions(String[] options) throws AnalysisException;
    // metoda zwracająca nazwę algorytmu
    String getName();
    // metoda przekazująca dane do analizy, wyrzucająca wyjątek jeśli aktualnie trwa przetwarzanie danych
    void submit(ex.api.DataSet ds) throws AnalysisException;

    DataSet retrieve(boolean clear) throws AnalysisException;
}
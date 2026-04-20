package ex.api;
/**
 * Klasa reprezentująca zbiór danych w postaci tabelarycznej.
 * Przechowuje nagłówek (jednowymiarowa tablica z nazwami kolumn) 
 * oraz dane (dwuwymiarowa tablica, której wiersze reprezentują wektory danych).
 *
 * @author tkubik
 *
 */
public class DataSet {
	private String[] header = {}; 
	private String[][] data = {{}};

	private <T> T[][] deepCopy(T[][] matrix) {
	    return java.util.Arrays.stream(matrix).map(el -> el.clone()).toArray(i -> matrix.clone());
	}
	
	public String[] getHeader() {
		return header;
	}
	public void setHeader(String[] header) {
		this.header = header.clone();
	}
	public String[][] getData() {
		return data;
	}
	public void setData(String[][] data) {
		this.data = deepCopy(data);
	}
}

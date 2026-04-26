package main;

import ex.api.DataSet;

public class Main {
	private String s;

	public static void main(String[] args) {
		Main m = new Main();
		DataSet ds = new DataSet();
		String[][] data = {{"A","B"},{"C","D"}};
        ds.setData(data);
        data[0][0] = "Z";
        System.out.println(ds.getData()[0][0]);
	}

	public String getS() {
		return s;
	}

	public void setS(String s) {
		this.s = s;
	}
}

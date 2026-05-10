package pl.pwr.ite.dynak.app.utils;

import ex.api.DataSet;

public class DataGenerator {
    private int pointAmount;
    public DataSet generateDataSet() {
        String[] header = {"x", "y"};
        String[][] data = new String[pointAmount][2];

        DataSet dataSet = new DataSet();

        for (int i = 0; i < pointAmount; i++) {
            data[i][0] = String.valueOf(Math.random());
            data[i][1] = String.valueOf(Math.random());
        }

        dataSet.setHeader(header);
        dataSet.setData(data);

        return dataSet;
    }

    public void setPointAmount(int pointAmount) {
        this.pointAmount = pointAmount;
    }
}

package pl.pwr.ite.dynak.services;

import ex.api.AnalysisException;
import ex.api.AnalysisService;
import ex.api.DataSet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public abstract class AnalyzerBase implements AnalysisService {
    protected int maxIterations;
    protected int k;
    protected List<List<double[]>> clusters;
    protected DataSet dataSet;
    protected boolean isCalculating = false;
    double[][] centroids;

    protected abstract void recalculateCentroids() throws AnalysisException;
    protected abstract double calculateDistance(double[] a, double[] b);

    @Override
    public void setOptions(String[] options) throws AnalysisException {
        try {
            k = Integer.parseInt(options[0]);
            maxIterations = Integer.parseInt(options[1]);
        }
        catch (Exception e){
            throw new AnalysisException("Invalid options");
        }
    }

    @Override
    public void submit(DataSet ds) throws AnalysisException {
        if (isCalculating) {
            throw new AnalysisException("Already calculating");
        }

        dataSet = ds;
        isCalculating = true;

        try {
            pickCentroids();
            for (int i = 0; i < maxIterations; i++) {
                assignClusters();
                recalculateCentroids();
            }

            dataSet = createCentroidsDataSet();
        } catch (Exception e) {
            throw new AnalysisException("Error while reading data");
        }

        isCalculating = false;
    }

    @Override
    public DataSet retrieve(boolean clear) throws AnalysisException {
        try {
            if (isCalculating) {
                throw new AnalysisException("Cannot retrieve data while calculating");
            }

            return dataSet;
        } finally {
            if (clear) {
                dataSet = null;
            }
        }
    }


    private void pickCentroids() {
        Random random = new Random();
        centroids = new double[k][dataSet.getHeader().length];

        for (int i = 0; i < k; i++) {
            int randomPoint = random.nextInt(dataSet.getData().length);
            centroids[i] = Arrays.stream(dataSet.getData()[randomPoint]).mapToDouble(Double::parseDouble).toArray();
        }
    }

    private void assignClusters(){
        clusters = new ArrayList<>();

        for (int i = 0; i < k; i++) {
            clusters.add(new ArrayList<>());
        }

        for (String[] point : dataSet.getData()) {
            double[] doubles = Arrays.stream(point).mapToDouble(Double::parseDouble).toArray();

            clusters.get(getNearestCentroidIndex(doubles)).add(doubles);
        }

    }

    private int getNearestCentroidIndex(double[] point){
        double minDistance = Double.MAX_VALUE;
        int nearestIndex = 0;

        for (int i = 0; i < k; i++) {
            double distance = calculateDistance(point, centroids[i]);

            if (distance < minDistance) {
                minDistance = distance;
                nearestIndex = i;
            }
        }

        return nearestIndex;
    }

    private DataSet createCentroidsDataSet() {
        DataSet ds = new DataSet();
        ds.setHeader(dataSet.getHeader());

        String[][] centroidData = new String[centroids.length][centroids[0].length];

        for (int i = 0; i < centroids.length; i++) {
            for (int j = 0; j < centroids[i].length; j++) {
                centroidData[i][j] = String.valueOf(centroids[i][j]);
            }
        }

        ds.setData(centroidData);

        return ds;
    }
}

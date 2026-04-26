package pl.pwr.ite.dynak.services;

import com.google.auto.service.AutoService;
import ex.api.AnalysisException;

@AutoService(ex.api.AnalysisService.class)
public class KMeanAlgorithm extends AnalyzerBase {

    @Override
    public void recalculateCentroids() throws AnalysisException {
        if (clusters == null || centroids == null) {
            throw new AnalysisException("No clusters or centroids");
        }

        for (int i = 0; i < k; i++) {
            if (clusters.get(i).isEmpty()) {
                continue;
            }

            double[] newCentroid = new double[centroids[i].length];

            for (double[] point : clusters.get(i)) {
                for (int j = 0; j < point.length; j++) {
                    newCentroid[j] += point[j];
                }
            }

            for (int j = 0; j < newCentroid.length; j++) {
                newCentroid[j] /= clusters.get(i).size();
            }

            centroids[i] = newCentroid;
        }
    }

    @Override
    protected double calculateDistance(double[] a, double[] b) {
        double sum = 0;

        for (int i = 0; i < a.length; i++) {
            sum += Math.pow(a[i] - b[i], 2);
        }

        return Math.sqrt(sum);
    }

    @Override
    public String getName() {
        return "K-Mean";
    }
}

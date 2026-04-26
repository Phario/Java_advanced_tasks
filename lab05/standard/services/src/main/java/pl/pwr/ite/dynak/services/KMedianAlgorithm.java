package pl.pwr.ite.dynak.services;

import com.google.auto.service.AutoService;
import ex.api.AnalysisException;

import java.util.Arrays;

@AutoService(ex.api.AnalysisService.class)
public class KMedianAlgorithm extends AnalyzerBase {

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

            for (int j = 0; j < newCentroid.length; j++) {
                double[] values = new double[clusters.get(i).size()];

                for (int p = 0; p < clusters.get(i).size(); p++) {
                    values[p] = clusters.get(i).get(p)[j];
                }

                Arrays.sort(values);

                int middle = values.length / 2;

                if (values.length % 2 == 0) {
                    newCentroid[j] = (values[middle - 1] + values[middle]) / 2;
                } else {
                    newCentroid[j] = values[middle];
                }
            }

            centroids[i] = newCentroid;
        }
    }

    @Override
    protected double calculateDistance(double[] a, double[] b) {
        double sum = 0;

        for (int i = 0; i < a.length; i++) {
            sum += Math.abs(a[i] - b[i]);
        }

        return sum;
    }

    @Override
    public String getName() {
        return "K-Median";
    }
}

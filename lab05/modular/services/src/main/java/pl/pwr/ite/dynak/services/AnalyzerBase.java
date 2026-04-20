package pl.pwr.ite.dynak.services;

import ex.api.AnalysisException;
import ex.api.AnalysisService;
import ex.api.DataSet;

public abstract class AnalyzerBase implements AnalysisService {
    DataSet dataSet;

    public abstract void calculateClustering() throws AnalysisException;

    @Override
    public void setOptions(String[] options) throws AnalysisException {
        int clusterAmount = Integer.parseInt(options[0]);
    }

    @Override
    public void submit(DataSet ds) throws AnalysisException {

    }

    @Override
    public DataSet retrieve(boolean clear) throws AnalysisException {
        return null;
    }

    public void readDataSet(){
        dataSet =  new DataSet();


    }
}

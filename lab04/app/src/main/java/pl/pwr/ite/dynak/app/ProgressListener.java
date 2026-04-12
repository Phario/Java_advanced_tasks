package pl.pwr.ite.dynak.app;

import javafx.scene.control.ProgressBar;
import pl.pwr.ite.dynak.lib.Status;
import pl.pwr.ite.dynak.lib.StatusListener;

public class ProgressListener implements StatusListener {

    private ProgressBar pb;

    public ProgressListener(ProgressBar pb) {
        this.pb = pb;
    }
    @Override
    public void statusChanged(Status s) {
        pb.setProgress((s.getProgress()*1.0)/100);
    }
}

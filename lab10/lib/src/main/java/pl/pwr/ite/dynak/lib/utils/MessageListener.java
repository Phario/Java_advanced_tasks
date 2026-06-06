package pl.pwr.ite.dynak.lib.utils;

import java.io.File;

public interface MessageListener {
    void onMessage(String message);
    void onLog(String log);
    void onFileReceived(File file);
}

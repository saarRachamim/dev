package app.network;

import app.common.Constants;
import app.local.ProcessHandler;

import java.io.IOException;
import java.util.Calendar;

public class PerformanceReqThread extends Thread {
    String testName;

    public PerformanceReqThread(String testName) {
        this.testName = testName;
        setDaemon(true);
    }

    @Override
    public void run() {
        try {
            ProcessHandler ph = new ProcessHandler();
            RequestHandler requestHandler = new RequestHandler(Constants.FB_URL);
            String retKey = requestHandler.postHttp("", new String[]{"Content-Type", "Authorization"}, new String[]{"application/x-www-form-urlencoded", "key=" + Constants.SECRET}, "{\"time\":\"" + Calendar.getInstance().getTime() + "\", \"testName\":\"" + testName + "\", \"browser\":\"Chrome\"}");

            while (true) {
                try {
                    String cpu = ph.getCpuUsageByProcessName(Constants.PROCESS_NAME);
                    String mem = ph.getMemoryUsageByProcessName(Constants.PROCESS_NAME);
                    requestHandler.postHttp(retKey, new String[]{"Content-Type", "Authorization"}, new String[]{"application/x-www-form-urlencoded", "key=" + Constants.SECRET}, "{\"mem\":\"" + mem + "\",\"cpu\":\"" + cpu + "\", \"time\":\"" + Calendar.getInstance().getTime() + "\"}");
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

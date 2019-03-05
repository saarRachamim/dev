package app.executers;

import app.network.PerformanceReqThread;

public class ThreadExecutor {

    String testName;

    public ThreadExecutor(String testName){
        this.testName = testName;
    }

    public void execute(){
        PerformanceReqThread reqThread = new PerformanceReqThread(testName);
        try {
            reqThread.start();
        }
        catch (Exception e)
        {
            System.out.print(e.getMessage());
        }
    }
}

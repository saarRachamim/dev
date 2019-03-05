package app.tests;
import app.executers.TestExecuter;
import app.executers.ThreadExecutor;
import org.junit.Test;

public class SeleniumTest {

    @Test
    public void test(){
        String testName = Thread.currentThread().getStackTrace()[1].getMethodName();
        new ThreadExecutor(testName).execute();
        new TestExecuter().execute();
    }
}

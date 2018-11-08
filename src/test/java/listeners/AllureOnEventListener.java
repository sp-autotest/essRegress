package listeners;

import config.Values;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import java.io.IOException;
import static listeners.ScreenShoter.makeScreenshot;
import static pages.Page.*;

/**
 * Created by mycola on 20.02.2018.
 */
public class AllureOnEventListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {}

    @Override
    public void onTestSuccess(ITestResult result) {
        int number = stringIntoInt(result.getMethod().getMethodName().replaceAll("\\D+", ""));
        resultat(number);
        if (Values.getERR(number).size()>0) nonBlockingErrors(number);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        int number = stringIntoInt(result.getMethod().getMethodName().replaceAll("\\D+", ""));
        try {
            makeScreenshot();
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultat(number);
        if (Values.getERR(number).size()>0) nonBlockingErrors(number);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        int number = stringIntoInt(result.getMethod().getMethodName().replaceAll("\\D+", ""));
        try {
            makeScreenshot();
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultat(number);
        if (Values.getERR(number).size()>0) nonBlockingErrors(number);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        int number = stringIntoInt(result.getMethod().getMethodName().replaceAll("\\D+", ""));
        try {
            makeScreenshot();
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultat(number);
        if (Values.getERR(number).size()>0) nonBlockingErrors(number);
    }

    @Override
    public void onStart(ITestContext context) {}

    @Override
    public void onFinish(ITestContext context) {}
}

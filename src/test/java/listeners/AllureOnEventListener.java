package listeners;

import config.Values;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;

import static listeners.ScreenShoter.makeScreenshot;
import static pages.Page.logDoc;
import static pages.Page.resultat;

/**
 * Created by mycola on 20.02.2018.
 */
public class AllureOnEventListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {}

    @Override
    public void onTestSuccess(ITestResult result) {
        resultat();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        try {
            makeScreenshot("Скриншот");
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultat();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        try {
            makeScreenshot("Скриншот");
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultat();
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        try {
            makeScreenshot("Скриншот");
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultat();
    }

    @Override
    public void onStart(ITestContext context) {}

    @Override
    public void onFinish(ITestContext context) {
        logDoc(Values.error1);
        logDoc(Values.error2);
        logDoc(Values.error3);
    }
}

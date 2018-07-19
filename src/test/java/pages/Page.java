package pages;

import com.codeborne.selenide.SelenideElement;
import config.Values;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
//import ru.yandex.qatools.allure.annotations.Step;
import io.qameta.allure.Step;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static listeners.ScreenShoter.makeScreenshot;

/**
 * Created by mycola on 20.02.2018.
 */
public class Page {

    public static void Sleep(int time){
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Step("Скриншот")
    public static void screenShot(String name){
        try {
            makeScreenshot(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Step("Запись результатов")
    public static void resultat(){
        logPNR(Values.pnr);
        logCardNumber(Values.card[0][0]);
        logDocuments(Values.docs);
    }

    @Step("Неблокирующие ошибки")
    public static void nonBlockingErrors(){
        for (String e : Values.errors) logDoc(e);
    }

    @Step("PNR: {0}")
    public static void logPNR(String pnr){}

    @Step("Номер карты: {0}")
    public static void logCardNumber(String number){}

    @Step("Документы:")
    public static void logDocuments(String doc){
        try {
            for (String retval : doc.split(", ")) {
                logDoc(retval);
            }
        }catch (NullPointerException ex) {System.out.println("The step of parsing documents is not achieved"); }
    }

    @Step("{0}")
    public static void logDoc(String doc){}

    public static String getRandomString(int l) {
        StringBuilder tag = new StringBuilder();
        for (int i = 0; i < l; i++) {
            tag.append((char) ((int) (Math.random() * 25 + 97)));
        }
        return tag.toString();
    }

    public static String getRandomNumberString(int l) {
        StringBuilder tag = new StringBuilder();
        for (int i = 0; i < l; i++) {
            tag.append((char) ((int) (Math.random() * 8 + 49)));
        }
        return tag.toString();
    }

    public static int getRandomNumberLimit (int limit) {
        return (int)(Math.random()*limit);
    }

    public static int stringIntoInt (String s) {
        int a=0;
        String s1 = s.replace(" ", "");
        try{
            if (s1.indexOf(".") > 0) {
                a = Integer.parseInt(s1.substring(0, s1.indexOf(".")));
            } else {
                a = Integer.parseInt(s1);
            }
        }catch (NumberFormatException ne) {System.out.println("Error string to int parsing"); }
        return a;
    }

    public static void waitPlane() {
        Sleep(3);
        $(byXpath("//svg[contains(@class,'circle-preloader--plane')]")).shouldNotBe(visible);
    }

    public static int getLanguageNumber(String language) {
        for (int i=0; i<9; i++) {
            if (Values.lang[i][0].equals(language)) return i;
        }
        return 0;
    }

    public static void switchFromFirstPageToSecond(String parentHandle) {
        WebDriver wd = getWebDriver();
        try {
            for (String handle: wd.getWindowHandles()) {
                if (handle != parentHandle) {
                    wd.switchTo().window(handle);
                    //wd.switchTo().activeElement();
                }
            }
        } catch (Exception e) {
            System.err.println("Couldn't get to second page");
        }
    }

    public static void jsClick(SelenideElement el) {
        JavascriptExecutor executor = (JavascriptExecutor) getWebDriver();
        executor.executeScript("arguments[0].click();", el.toWebElement());
    }

    public static void scrollWithOffset(SelenideElement el, int x, int y) {
        String code = "window.scroll(" + (el.getLocation().x + x) + ","
                + (el.getLocation().y + y) + ");";
        ((JavascriptExecutor)getWebDriver()).executeScript(code, el.toWebElement(), x, y);
        Sleep(1);
    }

    public static String addMonthAndDays(Date startDate, int months, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.MONTH, months);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return new java.text.SimpleDateFormat("ddMMyyyy").format(cal.getTime());
    }

    public Date string2Date(String d, String f) {
        Date parsingDate=null;
        try {
            parsingDate = new SimpleDateFormat(f).parse(d);
        }catch (ParseException e) {
            System.out.println("Parsing date error");
        }
        return parsingDate;
    }

}

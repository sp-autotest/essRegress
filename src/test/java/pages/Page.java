package pages;

import ru.yandex.qatools.allure.annotations.Step;

import java.io.IOException;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static listeners.ScreenShoter.makeScreenshot;

/**
 * Created by mycola on 20.02.2018.
 */
public class Page {

    public static void Sleep(int time){
        try{
            Thread.sleep(time*1000);
        } catch(InterruptedException e) {
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
            tag.append((char) ((int) (Math.random() * 9 + 48)));
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

}

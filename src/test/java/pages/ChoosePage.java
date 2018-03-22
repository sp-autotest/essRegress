package pages;

import com.codeborne.selenide.ElementsCollection;
import config.Values;
import ru.yandex.qatools.allure.annotations.Step;
import soap.SoapRequest;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.WebDriverRunner.url;
import static config.Values.pnr;
import static org.testng.AssertJUnit.assertTrue;


/**
 * Created by mycola on 21.02.2018.
 */
public class ChoosePage extends Page {

    private String env = System.getProperty("area", "RC");//получить имя площадки из дженкинса, при неудаче площадка=RC


    @Step("Действие 4, выбор стенда")
    public void step4() {
        checkChoosePage();
        System.out.println("URL = " + url());
        if (!Values.cur.equals("RUB")) changeCurrency();
        clickEnvironment();
    }

    @Step("Действие {0}, выбор стенда")
    public void chooseTestStend (String n) {
        System.out.println("\t" + n + ". Choose Test Stend");
        $("h1").shouldBe(exactText("Вход в тестовую среду системы ЕПР"));
        System.out.println("URL = " + url());
        clickEnvironment();
        checkEprPageAppear();
    }

    @Step("Подождать страницу выбора стенда")
    private void checkChoosePage(){
        $("h1").shouldBe(exactText("Вход в тестовую среду системы ЕПР"));
        int start = url().indexOf("&PNR") + 5;
        pnr = url().substring(start, start + 6);
        System.out.println("PNR = " + pnr);
    }

    @Step("Проверить переход на платёжную страницу ЕПР")
    private void checkEprPageAppear(){
        String epr = "https://pay.test.aeroflot.ru/test-" + env.toLowerCase() + "/aeropayment/epr/payment2.html";
        assertTrue("URL платежной страницы ЕПР не соответствует эталону", url().contains(epr));
    }

    @Step("Выбрать стенд")
    private void clickEnvironment() {
        ElementsCollection buttons = $$(byXpath("//span[text()='TEST-" + env + "']"));
        buttons.get(1).click();
        waitPlane();
    }

    @Step("Действие 5, смена валюты")
    private void changeCurrency() {
        new SoapRequest().changeCurrency();
    }



}

package pages;

import com.codeborne.selenide.ElementsCollection;
import config.Values;
import io.qameta.allure.Step;
import soap.SoapRequest;
import struct.CollectData;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.url;
import static org.testng.AssertJUnit.assertTrue;


/**
 * Created by mycola on 21.02.2018.
 */
public class ChoosePage extends Page {

    private CollectData collectData;

    public ChoosePage(CollectData collectData) {
        this.collectData = collectData;
    }

    private String env = System.getProperty("area", "RC");//получить имя площадки из дженкинса, при неудаче площадка=RC


    @Step("Действие 4, выбор стенда")
    public void step4() {
        checkChoosePage();
        System.out.println("[" + collectData.getTest() + "] URL = " + url());
        if ((!collectData.getCur().equals("RUB"))&(Values.currencyChange.equals("soap"))) changeCurrency();
        clickEnvironment();
    }

    @Step("Действие 4, добавить доп. услуги")
    public void step4_8() {
        checkChoosePage();
        System.out.println("[" + collectData.getTest() + "] URL = " + url());
        if ((!collectData.getCur().equals("RUB"))&(Values.currencyChange.equals("soap"))) changeCurrency();
        addAdditionalServices();
        String backdoor;
        if (env.equals("RC")) backdoor = Values.backdoor_host;
        else backdoor = Values.backdoor_host.replace("ws.ess", "ws-nf.ess");
        open(backdoor + Values.getPNR(collectData.getTest()));
        String link = $(byXpath("//a")).shouldBe(visible).getText().
                replaceFirst("Language=RU", "Language="+Values.lang[collectData.getLn()][2].toUpperCase());
        System.out.println("Backdoor link = " + link);
        open(link);
    }

    @Step("Действие {0}, выбор стенда")
    public void chooseTestStend (int n) {
        System.out.println("\t" + n + ". Choose Test Stend");
        if (env.equals("RC")) {
            $("h1").shouldBe(exactText("Вход в тестовую среду системы ЕПР"));
            System.out.println("[" + collectData.getTest() + "] URL = " + url());
            clickEnvironment();
        }
        checkEprPageAppear();
    }

    @Step("Подождать страницу выбора стенда")
    private void checkChoosePage(){
        $("h1").shouldBe(text("Вход в тестовую среду системы ЕПР"));
        int start = url().indexOf("&PNR") + 5;
        String pnr = url().substring(start, start + 6);
        Values.setPNR(collectData.getTest(), pnr);
        System.out.println("[" + collectData.getTest() + "] PNR = " + pnr);
    }

    @Step("Проверить переход на платёжную страницу ЕПР")
    private void checkEprPageAppear(){
        String epr = "https://pay.test.aeroflot.ru/test-" + env.toLowerCase() + "/aeropayment/epr";
        assertTrue("URL платежной страницы ЕПР не соответствует эталону", url().contains(epr));
        if ($$("#acceptCookiesLaw").size()>0){
            Sleep(1);
            $("#acceptCookiesLaw").click();
        }
    }

    @Step("Выбрать стенд")
    private void clickEnvironment() {
        ElementsCollection buttons = $$(byXpath("//span[text()='TEST-" + env + "']"));
        buttons.get(1).click();
        waitPlane();
    }

    @Step("Действие 5, смена валюты")
    private void changeCurrency() {
        new SoapRequest(collectData).changeCurrency();
    }

    @Step("Добавить дополнительные авиационные услуги (SOAP)")
    public void addAdditionalServices() {
        System.out.println("Add aditional services");
        new SoapRequest(collectData).addAdditionalAviaServices();
    }

}

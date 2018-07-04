package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import config.Values;
import ru.yandex.qatools.allure.annotations.Step;
import struct.Passenger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.WebDriverRunner.url;
import static config.Values.lang;
import static config.Values.ln;

/**
 * Created by mycola on 21.02.2018.
 */
public class PassengerPage extends Page {

    @Step("Действие 3, информация о пассажирах")
    public void step3(List<Passenger> passengerList) {
        checkPageAppear();
        if ((!Values.cur.equals("RUB"))&(Values.currencyChange.equals("link"))) currencyChange(Values.cur);
        ElementsCollection cards = $$(byXpath("//div[@class='passenger-card']"));
        int count = cards.size();
        for (int i=0;i<count;i++) fillPassengerData(cards.get(i), passengerList.get(i));
        setEmail();
        setPhone();
        setTerms();
        clickContinueButton();
    }

    private void checkPageAppear(){
        $(byXpath("//div[@class='passenger-card__gender']")).shouldBe(exist);
    }


    @Step("Заполнить данные для {1}-го пассажира")
    private void fillPassengerData(SelenideElement card, Passenger p){
        setGender(card, p.getGender());
        setLastName(card, p.getLastname());
        setFirstName(card, p.getFirstname());
        setDOB(card, p.getDob());
        setNumber(card, p.getNumber());
        setNationality(card, p.getNationality());
        setСountry(card, p.getCoutry());
        clickUnlimitedLink(card);
    }

    @Step("Указать пол: {1}")
    private void setGender(SelenideElement card, int g){
        ElementsCollection gender = card.$$(byXpath("descendant::div[@class='passenger-card__gender']/a"));
        gender.get(g).click();
    }

    @Step("Указать фамилию: {1}")
    private void setLastName(SelenideElement card, String lastName){
        card.$$(byXpath("descendant::input[@type='text']")).get(0).setValue(lastName);
    }

    @Step("Указать имя: {1}")
    private void setFirstName(SelenideElement card, String firstName){
        card.$$(byXpath("descendant::input[@type='text']")).get(1).setValue(firstName);
    }

    @Step("Указать день рождения: {1}")
    private void setDOB(SelenideElement card, String dob){
        card.$$(byXpath("descendant::input[@type='text']")).get(3).setValue(dob);
    }

    @Step("Указать номер: {1}")
    private void setNumber(SelenideElement card, String number){
        card.$$(byXpath("descendant::input[@type='text']")).get(4).setValue(number);
    }

    @Step("Указать гражданство: {1}")
    private void setNationality(SelenideElement card, String natio){
        SelenideElement el = card.$$(byXpath("descendant::input[@role='listbox']")).get(0);
        while(!el.getValue().equals(natio)) el.setValue(natio);
    }

    @Step("Указать страну выдачи: {1}")
    private void setСountry(SelenideElement card, String country){
        SelenideElement el = card.$$(byXpath("descendant::input[@role='listbox']")).get(1);
        while(!el.getValue().equals(country)) el.setValue(country);
    }

    @Step("Кликнуть \"Бессрочно\"")
    private void clickUnlimitedLink(SelenideElement card){
        card.$(byXpath("descendant::a[contains(@class,'card__unlimited-link')]")).click();
    }

    @Step("Указать электронную почту")
    private void setEmail(){
        SelenideElement block = $(byXpath("//div[contains(@class,'--icon-contacts')]/following-sibling::div"));
        block.$(byXpath("descendant::input[@type='text']")).setValue(Values.email);
    }

    @Step("Указать телефон")
    private void setPhone(){
        String phone = getRandomNumberString(10);
        SelenideElement block = $(byXpath("//div[contains(@class,'--icon-contacts')]/following-sibling::div"));
        block.$$(byXpath("descendant::input[@type='text']")).get(1).setValue(phone);
        Values.phone = phone;
    }

    @Step("Согласиться с правилами")
    private void setTerms(){
        $(byXpath("//label[@for='passangersTermsAndConditions']")).click();
    }

    @Step("Нажать \"Продолжить\"")
    private void clickContinueButton() {
        $(byXpath("//a[@class='next__button']")).shouldBe(visible).click();
        waitPlane();
        waitPlane();
    }

    @Step("Действие 4, смена валюты на: {0}")
    private void currencyChange(String currency) {
        String cur = "";
        if (!currency.equals("RUB")) {
            switch (currency) {
                case "USD":
                    cur = "us";
                    break;
                case "EUR":
                    cur = "fr";
                    break;
                case "CNY":
                    cur = "cn";
                    break;
            }
            String link = url();
            link = link.replaceFirst("app/ru", "app/" + cur);
            System.out.println("New link = " + link);
            getWebDriver().get(link);
            checkPageAppear();
        }
    }
}

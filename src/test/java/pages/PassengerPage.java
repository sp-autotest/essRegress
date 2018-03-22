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
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.url;
import static config.Values.lang;
import static config.Values.ln;

/**
 * Created by mycola on 21.02.2018.
 */
public class PassengerPage extends Page {

    List<Passenger> passengerList = new ArrayList<Passenger>();

    @Step("Действие 3, информация о пассажирах")
    public List<Passenger> step3() {
        checkPageAppear();
        if ((!Values.cur.equals("RUB"))&(Values.currencyChange.equals("link"))) currencyChange(Values.cur);
        ElementsCollection cards = $$(byXpath("//div[@class='passenger-card']"));
        int count = getPassengersCount();
        for (int i=0;i<count;i++) fillPassengerData(cards.get(i),i+1);
        setEmail();
        setPhone();
        setTerms();
        clickContinueButton();
        return passengerList;
    }

    private void checkPageAppear(){
        $(byXpath("//div[@class='passenger-card__gender']")).shouldBe(exist);
    }

    private int getPassengersCount(){
        return $$(byXpath("//div[@class='passenger-card']")).size();
    }

    private static String addMonthsFromToday(int months)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, months);
        return new java.text.SimpleDateFormat("ddMMyyyy").format(cal.getTime());
    }

    @Step("Заполнить данные для {1}-го пассажира")
    private void fillPassengerData(SelenideElement card, int i){
        Passenger p = new Passenger();
        p.gender = setRandomGender(card);
        p.lastname = setRandomLastName(card);
        p.firstname = setRandomFirstName(card);
        p.dob = setDOB(card, i);
        p.number = setRandomNumber(card);
        setNationality(card);
        setСountry(card);
        clickUnlimitedLink(card);
        passengerList.add(p);
    }

    @Step("Указать пол")
    private int setRandomGender(SelenideElement card){
        ElementsCollection gender = card.$$(byXpath("descendant::div[@class='passenger-card__gender']/a"));
        int g = getRandomNumberLimit(gender.size());
        gender.get(g).click();
        return g;
    }

    @Step("Указать фамилию")
    private String setRandomLastName(SelenideElement card){
        String lastName = getRandomString(8);
        card.$$(byXpath("descendant::input[@type='text']")).get(0).setValue(lastName);
        return lastName;
    }

    @Step("Указать имя")
    private String setRandomFirstName(SelenideElement card){
        String firstName = getRandomString(4);
        card.$$(byXpath("descendant::input[@type='text']")).get(1).setValue(firstName);
        return firstName;
    }

    @Step("Указать день рождения")
    private String setDOB(SelenideElement card, int i){
        int delta=0;
        if (i == 1) delta = -438;
        if (i == 2) delta = -415;
        if (i == 3) delta = -66;
        if (i == 4) delta = -100;
        if (i == 5) delta = -8;
        String dob = addMonthsFromToday(delta);
        card.$$(byXpath("descendant::input[@type='text']")).get(3).setValue(dob);
        return dob;
    }

    @Step("Указать гражданство")
    private void setNationality(SelenideElement card){
        SelenideElement el = card.$$(byXpath("descendant::input[@role='listbox']")).get(0);
        while(!el.getValue().equals(lang[ln][4])) el.setValue(lang[ln][4]);
    }

    @Step("Указать страну выдачи")
    private void setСountry(SelenideElement card){
        SelenideElement el = card.$$(byXpath("descendant::input[@role='listbox']")).get(1);
        while(!el.getValue().equals(lang[ln][4])) el.setValue(lang[ln][4]);
    }

    @Step("Указать номер")
    private String setRandomNumber(SelenideElement card){
        String number = getRandomNumberString(6);
        card.$$(byXpath("descendant::input[@type='text']")).get(4).setValue(number);
        return number;
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
            open(link);
            checkPageAppear();
        }
    }
}

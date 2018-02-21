package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import config.Values;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.Calendar;
import java.util.Date;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Created by mycola on 21.02.2018.
 */
public class PassengerPage extends Page {

    @Step("Действие 3")
    public void step3() {
        checkPageAppear();
        ElementsCollection cards = $$(byXpath("//div[@class='passenger-card']"));
        int count = getPassengersCount();
        for (int i=0;i<count;i++) fillPassengerData(cards.get(i),i+1);
        setEmail();
        setPhone();
        setTerms();
        clickContinueButton();
    }

    private void checkPageAppear(){
        $(byXpath("//div[@class='passenger-card__gender']")).shouldBe(exist);
    }

    private int getPassengersCount(){
        return $$(byXpath("//div[@class='passenger-card']")).size();
    }

    private static String addMonthsFromToday(int monts)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, monts);
        return new java.text.SimpleDateFormat("ddMMyyyy").format(cal.getTime());
    }

    @Step("Заполнить данные для {1}-го пассажира")
    private void fillPassengerData(SelenideElement card, int i){
        setRandomGender(card);
        setRandomLastName(card);
        setRandomFirstName(card);
        setDOB(card, i);
        setRandomNumber(card);
        clickUnlimitedLink(card);
    }

    @Step("Указать пол")
    private void setRandomGender(SelenideElement card){
        ElementsCollection gender = card.$$(byXpath("descendant::div[@class='passenger-card__gender']/a"));
        gender.get(getRandomNumberLimit(gender.size())).click();
    }

    @Step("Указать фамилию")
    private void setRandomLastName(SelenideElement card){
        card.$$(byXpath("descendant::input[@type='text']")).get(0).setValue(getRandomString(8));
    }

    @Step("Указать имя")
    private void setRandomFirstName(SelenideElement card){
        card.$$(byXpath("descendant::input[@type='text']")).get(1).setValue(getRandomString(4));
    }

    @Step("Указать день рождения")
    private void setDOB(SelenideElement card, int i){
        int delta=0;
        if (i == 1) delta = -438;
        if (i == 2) delta = -415;
        if (i == 3) delta = -66;
        if (i == 4) delta = -100;
        if (i == 5) delta = -8;
        card.$$(byXpath("descendant::input[@type='text']")).get(3).setValue(addMonthsFromToday(delta));
    }

    @Step("Указать номер")
    private void setRandomNumber(SelenideElement card){
        card.$$(byXpath("descendant::input[@type='text']")).get(4).setValue(getRandomNumberString(6));
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
        SelenideElement block = $(byXpath("//div[contains(@class,'--icon-contacts')]/following-sibling::div"));
        block.$$(byXpath("descendant::input[@type='text']")).get(1).setValue(getRandomNumberString(10));
    }

    @Step("Согласиться с правилами")
    private void setTerms(){
        $(byXpath("//label[@for='passangersTermsAndConditions']")).click();
    }

    @Step("Нажать \"Продолжить\"")
    private void clickContinueButton() {
        $(byXpath("//a[@class='next__button']")).shouldBe(visible).click();
    }

}

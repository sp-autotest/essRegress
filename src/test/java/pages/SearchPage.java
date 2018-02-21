package pages;

import com.codeborne.selenide.ElementsCollection;
import ru.yandex.qatools.allure.annotations.Step;
import java.util.Calendar;
import java.util.Date;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Created by mycola on 20.02.2018.
 */
public class SearchPage extends Page {


    @Step("Действие 1, поиск рейсов")
    public void step1(String locale) {
        selectLocale(locale);
        setFrom("MOW");
        setTo("PRG");
        setThere(addOneMonthAndDays(0));
        setBack (addOneMonthAndDays(2));
        clickPassengers();
        addAdult();
        addChild();
        addChild();
        addInfant();
        clickPassengers();
        clickSearchButton();
    }

    @Step("Действие 2, выбор рейсов")
    public void step2() {
        selectRandomFlight("туда");
        clickBuyButton();
        selectRandomFlight("обратно");
        clickBuyButton();
        clickPassengersButton();
    }


    @Step("Выбрать язык: {0}")
    private void selectLocale(String locale) {
        $(byXpath("//div[@class='header__select-items']")).shouldBe(visible).click();
        $(byXpath("//div[text()='" + locale + "']")).shouldBe(visible).click();
    }

    @Step("Выбрать город вылета: {0}")
    private void setFrom(String city) {
        $$(byXpath("//input[@class='input__text-input']")).get(0).shouldBe(visible).setValue(city);
    }

    @Step("Выбрать город прибытия: {0}")
    private void setTo(String city) {
        $$(byXpath("//input[@class='input__text-input']")).get(1).shouldBe(visible).setValue(city);

    }

    @Step("Указать дату \"Туда\": {0}")
    private void setThere(String date) {
        $$(byXpath("//input[@class='input__text-input']")).get(2).shouldBe(visible).setValue(date);
    }

    @Step("Указать дату \"Обратно\": {0}")
    private void setBack(String date) {
        $$(byXpath("//input[@class='input__text-input']")).get(3).shouldBe(visible).setValue(date);
    }

    @Step("Клик по пассажирам")
    private void clickPassengers() {
        $$(byXpath("//input[@class='input__text-input']")).get(4).shouldBe(visible).click();
    }

    @Step("Добавить взрослого")
    private void addAdult() {
        $$(byXpath("//div[@class='input-counter']")).get(0).
                $(byXpath("div[@class='input-counter__plus']")).shouldBe(visible).click();
    }

    @Step("Добавить ребенка")
    private void addChild() {
        $$(byXpath("//div[@class='input-counter']")).get(1).
                $(byXpath("div[@class='input-counter__plus']")).shouldBe(visible).click();
    }

    @Step("Добавить младенца")
    private void addInfant() {
        $$(byXpath("//div[@class='input-counter']")).get(2).
                $(byXpath("div[@class='input-counter__plus']")).shouldBe(visible).click();
    }

    @Step("Нажать \"Закрыть\"")
    private void clickCloseButton() {
        $(byXpath("//a[contains(@class,'dropdown-close')]")).shouldBe(visible).click();
    }

    @Step("Нажать \"Найти\"")
    private void clickSearchButton() {
        $(byXpath("//a[contains(@class,'button--tell')]")).shouldBe(visible).click();
    }

    @Step("Выбрать рейс {0}")
    private void selectRandomFlight(String r) {
        int i;
        if (r.equals("туда")) {
            i = 0;
        } else {
            i = 1;
        }
        $(byXpath("//div[contains(@class,'frame__heading')]")).shouldBe(visible);
        Sleep(2);
        ElementsCollection headers = $$(byXpath("//div[@class='row flight-search__header']"));
        ElementsCollection flights = headers.get(i).$$(byXpath("following-sibling::*"));
        flights.get(getRandomNumberLimit(flights.size())).click();
    }

    @Step("Нажать \"Купить\"")
    private void clickBuyButton() {
        $(byXpath("//a[contains(@class,'button--small-padding')]")).shouldBe(visible).click();
    }

    @Step("Нажать \"Указать пассажиров\"")
    private void clickPassengersButton() {
        $(byXpath("//a[@class='next__button']")).shouldBe(visible).click();
    }


    private static String addOneMonthAndDays(int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return new java.text.SimpleDateFormat("ddMMyyyy").format(cal.getTime());
    }

}

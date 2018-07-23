package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import config.Values;
import org.openqa.selenium.JavascriptExecutor;
//import ru.yandex.qatools.allure.annotations.Step;
import io.qameta.allure.Step;
import struct.Flight;
import struct.InitialData;
import java.util.ArrayList;
import java.util.List;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static config.Values.lang;
import static config.Values.ln;
import static config.Values.ticket;

/**
 * Created by mycola on 20.02.2018.
 */
public class SearchPage extends Page {

    List<Flight> flightList = new ArrayList<Flight>();
    String duration = "";

    private InitialData initData;

    public SearchPage(InitialData initData) {
        this.initData = initData;
    }

    @Step("Действие 1, поиск рейсов")
    public void step1() {
        clickAcceptCookiesButton();
        countrySelect();
        if (null != initData.getCityFrom()) setFrom(initData.getCityFrom());
        if (null != initData.getCityTo()) setTo(initData.getCityTo());
        if (null != initData.getDateThere()) setThere(initData.getDateThere());
        if (null != initData.getDateBack()) setBack(initData.getDateBack());
        clickPassengers();
        for (int i=1; i<initData.getAdult(); i++) addAdult();
        for (int i=0; i<initData.getChild(); i++) addChild();
        for (int i=0; i<initData.getInfant(); i++) addInfant();
        clickPassengers();
        clickSearchButton();
    }

    @Step("Действие 2, выбор рейсов")
    public List<Flight> step2() {
        selectRandomFlight("туда");
        clickBuyButton();
        if (null != initData.getDateBack()) {
            selectRandomFlight("обратно");
            clickBuyButton();
        }
        saveFlightData();
        clickPassengersButton();
        return flightList;
    }


    @Step("Выбрать язык: {0}")
    private void selectLocale() {
        if (getWebDriver().manage().window().getSize().getWidth() < 1280) {
            $(byXpath("//div[@class='header__menu-icon']/..")).shouldBe(visible).click();
        }
        $(byXpath("//div[@class='header__select-items']")).shouldBe(visible).click();
        $(byXpath("//div[text()='" + lang[ln][1] + "']")).shouldBe(visible).click();
        $(byXpath("//input[@class='input__text-input']")).shouldBe(visible);
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
        date = date.substring(0,2)+"."+date.substring(2,4)+"."+date.substring(4);
        SelenideElement el = $$(byXpath("//input[@class='input__text-input']")).get(2);
        while(!el.getValue().equals(date)) {
            el.setValue(date);
            Sleep(1);
        }
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
        ticket++;
    }

    @Step("Добавить ребенка")
    private void addChild() {
        $$(byXpath("//div[@class='input-counter']")).get(1).
                $(byXpath("div[@class='input-counter__plus']")).shouldBe(visible).click();
        ticket++;
    }

    @Step("Добавить младенца")
    private void addInfant() {
        $$(byXpath("//div[@class='input-counter']")).get(2).
                $(byXpath("div[@class='input-counter__plus']")).shouldBe(visible).click();
        ticket++;
    }

    @Step("Нажать \"Закрыть\"")
    private void clickCloseButton() {
        $(byXpath("//a[contains(@class,'dropdown-close')]")).shouldBe(visible).click();
    }

    @Step("Нажать \"Найти\"")
    private void clickSearchButton() {
        $(byXpath("//a[contains(@class,'button--wide')]")).shouldBe(visible).click();
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
        int limit = flights.size();
        if (limit>10) limit = 3;
        flights.get(getRandomNumberLimit(limit)).click();
    }

    @Step("Нажать \"Купить\"")
    private void clickBuyButton() {
        saveDuration();
        ElementsCollection buttons = $$(byXpath("//a[contains(@class,'button--bordered')][@aria-disabled='false']"));
        if (buttons.size()>0) {
            buttons.get(0).shouldBe(visible).click();
        } else {
            $(byXpath("//a[contains(@class,'button--bordered')]")).shouldBe(visible).click();
        }
    }

    @Step("Нажать \"Указать пассажиров\"")
    private void clickPassengersButton() {
        $(byXpath("//a[@class='next__button']")).shouldBe(visible).click();
    }

    private void saveFlightData() {
        Values.price.fly = $(byXpath("//div[@class='cart__item-price js-popover']")).getText().replaceAll("\\D+","");
        System.out.println("Fly price = " + Values.price.fly);
        Flight f;
        String d;
        ElementsCollection groups = $$(byXpath("//div[@class='flight-search flight-search--active']"));
        System.out.println("Groups count = " + groups.size());
        for (int m=0; m<groups.size(); m++) {
            ElementsCollection flights = groups.get(m).$$(byXpath("descendant::div[@class='row flight-search__flights']"));
            for (int i = 0; i < flights.size(); i++) {
                ElementsCollection el = flights.get(i).$$(byXpath("child::*"));
                if (el.size() < 3) {
                    String transfer = flights.get(i).$(byXpath("descendant::div[@class='flight-search__transfer']/span")).getText();
                    String[] arr = transfer.split(" "); //заносим информацию о пересадке в массив
                    int minutes = stringIntoInt(arr[1])*60; //переводим часы ожидания в минуты
                    if (arr.length>3) minutes = minutes + stringIntoInt(arr[3]); //добавляем минуты ожидания, если указаны
                    flightList.get(flightList.size() - 1).transfer = minutes*60000; //сохраняем время ожидания в милисекундах
                    continue;
                }
                f = new Flight();
                f.from_orig = flights.get(i).$(byXpath("descendant::div[@class='time-destination__from']/div[@class='time-destination__airport']")).getText();
                f.from = f.from_orig;
                if (f.from.equals("SVO")|f.from.equals("VKO")) f.from = "MOW";
                f.to_orig = flights.get(i).$(byXpath("descendant::div[@class='time-destination__to']/div[@class='time-destination__airport']")).getText();
                f.to = f.to_orig;
                if (f.to.equals("SVO")|f.to.equals("VKO")) f.to = "MOW";
                //f.number = flights.get(i).$(byXpath("descendant::div[@class='flight-search__plane-number']")).getText();
                SelenideElement num = flights.get(i).$(byXpath("descendant::div[@class='flight-search__plane-number']"));
                JavascriptExecutor executor = (JavascriptExecutor) getWebDriver();
                f.number = (String) executor.executeScript("return arguments[0].textContent;", num.toWebElement());
                d = (m==0) ? initData.getDateThere() : initData.getDateBack();
                d = d + " " + flights.get(i).$(byXpath("descendant::div[@class='time-destination__from']/div[@class='time-destination__time']")).getText();
                f.start = string2Date(d, "ddMMyyyy HH:mm");
                d = (m==0) ? initData.getDateThere() : initData.getDateBack();
                d = d + " " + flights.get(i).$(byXpath("descendant::div[@class='time-destination__to']/div[@class='time-destination__time']")).getText();
                f.end = string2Date(d, "ddMMyyyy HH:mm");
                f.duration = duration.substring(0, duration.indexOf(" "));
                duration = duration.substring(duration.indexOf(" ")+1);
                flightList.add(f);
            }
        }
        for(Flight fl : flightList) {
            System.out.println(fl.from + " / " + fl.to + " / " + fl.number + " / " + fl.duration +
                    " [" + fl.start + "] [" + fl.end + "] " + fl.transfer);
        }
    }

    private void saveDuration() {
        String temp;
        $(byXpath("//a[contains(@class,'modal__close')]")).shouldBe(visible);
        ElementsCollection dur = $$(byXpath("//div[@class='flight__date']"));
        for (int i=1; i<dur.size(); i=i+2) {
            temp = dur.get(i).$(byXpath("descendant::span[2]")).getText();
            if (temp.indexOf("：")>0) {
                temp = temp.substring(temp.indexOf("：") + 2);
            } else {
                temp = temp.substring(temp.indexOf(":") + 2);
            }
            temp = temp.substring(0, temp.length() - 3);
            for (int c = 0; c < temp.length(); c++) {
                if (Character.isDigit(temp.charAt(c))) {
                    duration = duration + temp.charAt(c);
                }
            }
            duration = duration + " ";
        }
    }

    private void countrySelect() {
        ElementsCollection country = $$("#countryListToggle");
        if (country.size()>0) {
            country.get(0).click();
            $(byXpath("//input[@id='select-country-ru']/..")).shouldBe(visible).click();
            $(byXpath("//button[contains(@class,'submitSelectedCountry')]")).shouldBe(visible).click();
            Sleep(2);
        }
    }

    private void clickAcceptCookiesButton() {
        ElementsCollection cookies = $$("#acceptCookiesLaw");
        if (cookies.size()>0) cookies.get(0).click();
    }


}

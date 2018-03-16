package pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.allure.annotations.Step;
import struct.Flight;
import java.text.SimpleDateFormat;
import java.util.List;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static config.Values.ln;
import static config.Values.text;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Created by mycola on 15.03.2018.
 */
public class HotelPage extends Page {

    @Step("Форма дополнительных услуг «Проживание» открылась")
    private void checkHotelFormAppear() {
        $(byXpath("//h1[contains(text(),'" + text[17][ln] + "')]")).shouldBe(visible);
        System.out.println("Accommodation form appeared");
    }

    @Step("Действие 15, Нажать кнопку «Проживание»")
    public void clickResidenceButton() {
        $(byXpath("//div[text()='" + text[3][ln] + "']")).shouldBe(visible).click();
        waitPlane();
        checkHotelFormAppear();
    }

    @Step("Действие 16, Проверка фильтра поиска отелей")
    public void checkHotelFilter() {
        $("#dateStartHotel").shouldBe(visible);//поле даты заезда
        $("#dateEndHotel").shouldBe(visible);//поле даты выезда
        $("#shown_stars").shouldBe(visible);//поле выбора звездности
        $("#hotel_min_price-value").shouldBe(visible);//поле минимальной цены
        $("#hotel_max_price-value").shouldBe(visible);//поле максимальной цены
        $(byXpath("//div[@class='range-slider']")).shouldBe(visible);//слайдер цены
        $("#hotels-filter-room-1").shouldBe(visible);//блок выбора гостей
        $(byXpath("//label[@for='hotel-checkbox']")).shouldBe(visible);//чекбокс Я ознакомился
        $(byXpath("//a[@data-check-id='button-hotel']")).shouldBe(visible);//кнопка Найти отель
        $("#additional-hotels-filter-name").shouldBe(visible);//поле Название
        $("#search-hotel-food4").shouldBe(visible);//поле Питание
        $("#search-hotel-service").shouldBe(visible);//поле Услуги в отеле
        $(byXpath("//button[@data-order-by='Price']")).shouldBe(visible);//кнопка Сортировка по цене
        $(byXpath("//button[@data-order-by='Distance']")).shouldBe(visible);//кнопка Сортировка по удаленности от центра
        $(byXpath("//button[@data-order-by='Category']")).shouldBe(visible);//кнопка Сортировка по звездности
    }

    @Step("Действие 17, Проверка логики отображения информации в блоке «Проживание»")
    public void checkHotelLogic(List<Flight> flightList) {
        checkStartHotelDate(new SimpleDateFormat("yyyy-MM-dd").format(flightList.get(0).end));
        checkEndHotelDate(new SimpleDateFormat("yyyy-MM-dd").format(flightList.get(1).start));
        checkResidentsNumber();
        checkRoomCount();
        clickHotelSearchButton();
    }

    @Step("Проверить совпадение автоматической даты вселения с датой прилета")
    private void checkStartHotelDate(String start) {
        String minHotel = $(byXpath("//input[@name='hotel_min_date']")).getValue();
        assertTrue("Дата вселения отличается от даты прилета" +
                        "\nОжидалось: " + start +
                        "\nФактически: " + minHotel,
                        start.equals(minHotel));
    }

    @Step("Проверить совпадение автоматической даты выселения с датой отлета")
    private void checkEndHotelDate(String end) {
        String maxHotel = $(byXpath("//input[@name='hotel_max_date']")).getValue();
        assertTrue("Дата выселения отличается от даты отлета" +
                        "\nОжидалось: " + end +
                        "\nФактически: " + maxHotel,
                        end.equals(maxHotel));
    }

    @Step("Проверить количество проживающих")
    private void checkResidentsNumber() {
        int n = $$(byXpath("//div[@class='js-travellers-list']/div")).size();
        assertTrue("Не соответствует количество номеров\nОжидалось: 1\nФактически: " + n, n == 1);
    }

    @Step("Проверить количество номеров")
    private void checkRoomCount() {
        int n = $$(byXpath("//div[contains(@id,'hotels-filter-room-')]")).size();
        assertTrue("Не соответствует количество номеров\nОжидалось: 1\nФактически: " + n, n == 1);
    }

    @Step("Нажать кнопку «Найти отель»")
    private void clickHotelSearchButton() {
        WebElement checkbox = $("#hotel-checkbox").toWebElement();
        JavascriptExecutor executor = (JavascriptExecutor)getWebDriver();
        executor.executeScript("arguments[0].click();", checkbox);
        $(byXpath("//a[@data-check-id='button-hotel']")).click();
        waitPlane();
    }

}

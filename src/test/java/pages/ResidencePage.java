package pages;

import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static config.Values.ln;
import static config.Values.text;

/**
 * Created by mycola on 15.03.2018.
 */
public class ResidencePage extends Page {

    @Step("Нажать кнопку «Проживание»")
    public void clickResidenceButton() {
        $(byXpath("//div[text()='" + text[3][ln] + "']")).shouldBe(visible).click();
        waitPlane();
        checkResidenceFormAppear();
    }

    @Step("Форма дополнительных услуг «Проживание» открылась")
    private void checkResidenceFormAppear() {
        $(byXpath("//h1[contains(text(),'" + text[17][ln] + "')]")).shouldBe(visible);
        System.out.println("Accommodation form appeared");
    }

    @Step("Проверка фильтра поиска отелей")
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

}
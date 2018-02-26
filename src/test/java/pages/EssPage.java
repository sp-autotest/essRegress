package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import config.Values;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static config.Values.text;

/**
 * Created by mycola on 22.02.2018.
 */
public class EssPage extends Page {

    @Step("Действие 6, проверка формы ESS")
    public void step6(String locale) {
        int l = getLanguageNumber(locale);
        checkPageAppear();
        checkFlight();
        checkNumber();
        checkDateTime();
        checkPrice();
        checkFlightInsurance(l);
        checkMedicalInsurance(l);
        checkCart();
        checkNextButton();
        checkTransport(l);
        checkDwelling(l);
        checkTimer();
    }

    private void checkPageAppear(){
        $(byXpath("//div[@class='cart__item-title']")).shouldBe(visible).shouldBe(text(Values.pnr)).click();
    }

    @Step("Маршрут")
    private void checkFlight(){
        $(byXpath("//div[@class='cart__item-details-item']")).shouldBe(visible);
    }

    @Step("Номер рейса")
    private void checkNumber(){
        $(byXpath("//div[@class='cart__item-details-model']")).shouldBe(visible);
    }

    @Step("Дата и время рейса")
    private void checkDateTime(){
        $(byXpath("//div[@class='h-color--gray h-mt--4']")).shouldBe(visible);
    }

    @Step("Стоимость для всех")
    private void checkPrice(){
        $(byXpath("//div[@class='cart__item-price']")).shouldBe(visible);
    }

    @Step("Блок полетной страховки")
    private void checkFlightInsurance(int loc){
        $(byXpath("//div[contains(text(),'" + text[0][loc] + "')]")).shouldBe(visible);
    }

    @Step("Блок медицинской страховки")
    private void checkMedicalInsurance(int loc){
        $(byXpath("//div[contains(text(),'" + text[1][loc] + "')][contains(@class,'icon-medicial')]")).shouldBe(visible);
    }

    @Step("Блок корзины")
    private void checkCart(){
        $("#window-main-cart").shouldBe(visible);
    }

    @Step("Кнопка перехода")
    private void checkNextButton(){
        $(byXpath("//div[@class='next__button-inner']")).shouldBe(visible);
    }

    @Step("Транспорт")
    private void checkTransport(int loc){
        $("#left-column-transport").shouldBe(visible).shouldBe(exactText(text[2][loc]));
    }

    @Step("Проживание")
    private void checkDwelling(int loc){
        $(byXpath("//div[@class='cart__item']")).shouldBe(visible).shouldBe(exactText(text[3][loc]));
    }

    @Step("Таймер")
    private void checkTimer(){
        $(byXpath("//div[@class='cart__item-counter-time']")).shouldBe(visible);
    }

}

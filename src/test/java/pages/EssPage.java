package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import config.Values;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
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
        checkPageAppear();
        checkFlight();
        checkNumber();
        checkDateTime();
        checkPrice();
        checkFlightInsurance(0);
        checkMedicalInsurance(0);
        checkCart();
        checkNextButton();
        checkTransport();
        checkDwelling();
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
        $(byXpath("//div[contains(text(),'" + text[1][loc] + "')]")).shouldBe(visible);
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
    private void checkTransport(){
        $("#left-column-transport").shouldBe(visible);
    }

    @Step("Проживание")
    private void checkDwelling(){
        $(byXpath("//div[@class='cart__item']")).shouldBe(visible);
    }

    @Step("Таймер")
    private void checkTimer(){
        $(byXpath("//div[@class='cart__item-counter-time']")).shouldBe(visible);
    }

}

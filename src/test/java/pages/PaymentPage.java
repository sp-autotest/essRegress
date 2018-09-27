package pages;

import com.codeborne.selenide.SelenideElement;
import config.Values;
import io.qameta.allure.Step;
import struct.CollectData;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Created by mycola on 05.03.2018.
 */
public class PaymentPage extends Page {

    private CollectData collectData;

    public PaymentPage(CollectData collectData) {
        this.collectData = collectData;
    }

    @Step("Действие {0}, проверка формы оплаты")
    public void checkPaymentForm1(String n) {
        System.out.println("\t" + n + ". Checking Payment form");
        new EprPage(collectData).clickPayButton();
        selectCardPay();
        checkTotalPrice1(n+"03");
    }

    @Step("Действие 15, проверка формы оплаты")
    public void checkPaymentForm2(boolean equalCityAndCurrency) {
        System.out.println("\t15. Checking Payment form");
        new EprPage(collectData).clickPayButton();
        selectCardPay();
        checkTotalPrice2(equalCityAndCurrency);  //отключили временно
        checkTransportPrice(); // отключили временно
    }

    @Step("Действие {0}, ввод реквизитов карточки и оплата")
    public void setCardDetails(String n) {
        System.out.println("\t" + n + ". Filling bank card details and click Pay");
        selectCardPay();
        setPan();
        setOwner();
        setMonth();
        setYear();
        setCVC();
        clickPayButton();
        checkPaySuccessfull();
    }

    @Step("Проверка стоимости на оплату")
    private void checkTotalPrice1(String n) {
        String priceTotal = Values.reportData[collectData.getTest()].getPrice().total;
        String price = $(byXpath("//div[@class='cart__item-price ng-binding']")).getText().replaceAll("\\D+","");
        System.out.println(price);
        if (!priceTotal.equals(price)){
            String text = "Ошибка: [" + n + "] Стоимость на странице оплаты картой не корректна, " +
                        "ожидалось: " + priceTotal + ", фактически: " + price;
            Values.addERR(collectData.getTest(), text);
            logDoc(text);
            screenShot("Скриншот");
        }//неблокирующая проверка
        //assertTrue("Стоимость «К ОПЛАТЕ ВСЕГО» некорректна", price.equals(Values.price.total));
        String button = $(byXpath("//span[contains(@ng-bind-html,'payAmountText')]")).getText().replaceAll("\\D+","");
        System.out.println(button);
        if (!priceTotal.equals(button)){
            String text = "Ошибка: [" + n + "] Стоимость на кнопке страницы оплаты картой не корректна, " +
                        "ожидалось: " + priceTotal + ", фактически:" + button;
            Values.addERR(collectData.getTest(), text);
            logDoc(text);
            screenShot("Скриншот");
        }//неблокирующая проверка
        //assertTrue("Стоимость «Заплатить» на кнопке некорректна", button.equals(Values.price.total));
    }

    @Step("Проверка стоимости на оплату")
    private void checkTotalPrice2(boolean equalCityAndCurrency) {
        String priceTotal = Values.reportData[collectData.getTest()].getPrice().total;
        String price = $(byXpath("//div[@class='cart__item-price ng-binding']")).getText().replaceAll("\\D+","");
        System.out.println(priceTotal);
        System.out.println(price);
        System.out.println("Валюта услуги равна валюте бронирования? " + equalCityAndCurrency);
        if (!equalCityAndCurrency) {
            assertTrue("Стоимость «К ОПЛАТЕ ВСЕГО» некорректна" +
                       "\nОжидалось : " + priceTotal + "\nФактически: " + price,
                       price.equals(priceTotal));
        }
        String now = $(byXpath("//div[contains(@translate,'now2')]/following-sibling::div")).getText().replaceAll("\\D+","");
        System.out.println(now);
        assertTrue("Стоимость «К оплате сейчас» некорректна" +
                   "\nОжидалось : " + priceTotal + "\nФактически: " + now,
                   now.equals(priceTotal));

        String button = $(byXpath("//span[contains(@ng-bind-html,'payAmountText')]")).getText().replaceAll("\\D+","");
        System.out.println(button);
        assertTrue("Стоимость «Заплатить» на кнопке некорректна" +
                   "\nОжидалось : " + priceTotal + "\nФактически: " + button,
                   button.equals(priceTotal));
    }

    @Step("Проверка стоимости аренды автомобиля")
    private void checkTransportPrice() {
        String priceTransport = Values.reportData[collectData.getTest()].getPrice().transport;
        String inplace = $(byXpath("//div[contains(@translate,'onSite')]/following-sibling::div")).getText().replaceAll("\\D+","");
        System.out.println("«На месте» = " + inplace);
        System.out.println("«Transport» = " + priceTransport);
        assertTrue("Стоимость «На месте» некорректна" +
                   "\nОжидалось : " + priceTransport + "\nФактически: " + inplace,
                   inplace.equals(priceTransport));

        String comment = $(byXpath("//div[@class='order-price__table-data-price ng-binding']")).getText().replaceAll("\\D+","");
        System.out.println(comment);
        assertTrue("Стоимость аренды авто в комментарии некорректна" +
                   "\nОжидалось : " + priceTransport + "\nФактически: " + comment,
                   comment.equals(priceTransport));
    }

    @Step("Заполнить поле \"Номер карты\"")
    private void setPan() {
        $("#pan_main").setValue(Values.card[0][0]);
    }

    @Step("Заполнить поле \"Владелец карты\"")
    private void setOwner() {
        $("#cardholder_main").setValue(Values.card[0][4]);
    }

    @Step("Заполнить поле \"Месяц\"")
    private void setMonth() {
        $("#exp_month_main").selectOptionByValue(Values.card[0][1]);
    }

    @Step("Заполнить поле \"Год\"")
    private void setYear() {
        $("#exp_year_main").selectOptionByValue(Values.card[0][2]);
    }

    @Step("Заполнить поле \"CVC\"")
    private void setCVC() {
        $("#cvc_main").setValue(Values.card[0][3]);
    }

    @Step("Нажать кнопку \"Заплатить\"")
    private void clickPayButton() {
        $("#cardButton").click();
    }

    @Step("Проверить сообщение об успешной оплате")
    private void checkPaySuccessfull() {
        String text = $(byXpath("//div[contains(@translate,'paymentSuccessful')]")).shouldBe(visible).getText();
        System.out.println("SUCCESS = " + text);
        assertTrue("Сообщение об успешной оплате отсутствует", text.equals(Values.text[11][collectData.getLn()]));
    }

    @Step("Открыть способ оплаты банковской картой")
    private void selectCardPay() {
        SelenideElement card = $("#accordion_card").shouldBe(visible);
        String status = card.getAttribute("class");
        if (!status.contains("open")) card.click();
    }


}
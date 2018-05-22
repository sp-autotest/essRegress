package pages;

import config.Values;
import ru.yandex.qatools.allure.annotations.Step;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static config.Values.error2;
import static config.Values.error3;
import static config.Values.ln;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Created by mycola on 05.03.2018.
 */
public class PaymentPage extends Page {

    @Step("Действие {0}, проверка формы оплаты")
    public void checkPaymentForm1(String n) {
        System.out.println("\t" + n + ". Checking Payment form");
        new EprPage().clickPayButton();
        checkTotalPrice1();
    }

    @Step("Действие 15, проверка формы оплаты")
    public void checkPaymentForm2() {
        System.out.println("\t15. Checking Payment form");
        new EprPage().clickPayButton();
        checkTotalPrice2();  //отключили временно
        checkTransportPrice(); // отключили временно
    }

    @Step("Действие {0}, ввод реквизитов карточки и оплата")
    public void setCardDetails(String n) {
        System.out.println("\t" + n + ". Filling bank card details and click Pay");
        setPan();
        setOwner();
        setMonth();
        setYear();
        setCVC();
        clickPayButton();
        checkPaySuccessfull();
    }

    @Step("Проверка стоимости на оплату")
    private void checkTotalPrice1() {
        String price = $(byXpath("//div[@class='cart__item-price ng-binding']")).getText().replaceAll("\\D+","");
        System.out.println(price);
        if (!Values.price.total.equals(price)){
            error2 = "ОШИБКА!: Стоимость на странице оплаты картой не корректна, ожидалось " + Values.price.total;
            logDoc(error2);
            screenShot("Скриншот");
        }//неблокирующая проверка
        //assertTrue("Стоимость «К ОПЛАТЕ ВСЕГО» некорректна", price.equals(Values.price.total));
        String button = $(byXpath("//span[contains(@ng-bind-html,'payAmountText')]")).getText().replaceAll("\\D+","");
        System.out.println(button);
        if (!Values.price.total.equals(button)){
            error3 = "ОШИБКА!: Стоимость на кнопке страницы оплаты картой не корректна, ожидалось " + Values.price.total;
            logDoc(error3);
            screenShot("Скриншот");
        }//неблокирующая проверка
        //assertTrue("Стоимость «Заплатить» на кнопке некорректна", button.equals(Values.price.total));
    }

    @Step("Проверка стоимости на оплату(без аренды авто)")
    private void checkTotalPrice2() {
        String price = $(byXpath("//div[@class='cart__item-price ng-binding']")).getText().replaceAll("\\D+","");
        System.out.println(price);
        assertTrue("Стоимость «К ОПЛАТЕ ВСЕГО» некорректна", price.equals(Values.price.total));

        String now = $(byXpath("//div[contains(@translate,'now2')]/following-sibling::div")).getText().replaceAll("\\D+","");
        System.out.println(now);
        assertTrue("Стоимость «К оплате сейчас» некорректна", now.equals(Values.price.total));

        String button = $(byXpath("//span[contains(@ng-bind-html,'payAmountText')]")).getText().replaceAll("\\D+","");
        System.out.println(button);
        assertTrue("Стоимость «Заплатить» на кнопке некорректна", button.equals(Values.price.total));
    }

    @Step("Проверка стоимости аренды автомобиля")
    private void checkTransportPrice() {
        String inplace = $(byXpath("//div[contains(@translate,'onSite')]/following-sibling::div")).getText().replaceAll("\\D+","");
        System.out.println("«На месте» = " + inplace);
        System.out.println("«Transport» = " + Values.price.transport);
        assertTrue("Стоимость «На месте» некорректна", inplace.equals(Values.price.transport));

        String comment = $(byXpath("//div[@class='order-price__table-data-price ng-binding']")).getText().replaceAll("\\D+","");
        System.out.println(comment);
        assertTrue("Стоимость аренды авто в комментарии некорректна", comment.equals(Values.price.transport));
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
        assertTrue("Сообщение об успешной оплате отсутствует", text.equals(Values.text[11][ln]));
    }



}
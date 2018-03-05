package pages;

import config.Values;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Created by mycola on 05.03.2018.
 */
public class PaymentPage extends Page {

    @Step("Действие 15, проверка формы оплаты")
    public void step15() {
        new EprPage().clickPayButton();
        checkTotalPrice();
        checkTransportPrice();
    }

    @Step("Проверка стоимости на оплату(без аренды авто)")
    private void checkTotalPrice() {
        String price = $(byXpath("//div[@class='cart__item-price ng-binding']")).getText().replace(" ", "");
        price = price.substring(0, price.length()-1);
        System.out.println(price);
        assertTrue("Стоимость «К ОПЛАТЕ ВСЕГО» некорректна", price.equals(Values.price.total));

        String now = $(byXpath("//div[contains(@translate,'now2')]/following-sibling::div")).getText().replace(" ", "");
        now = now.substring(0, now.length()-1);
        System.out.println(now);
        assertTrue("Стоимость «К оплате сейчас» некорректна", now.equals(Values.price.total));

        String button = $(byXpath("//span[contains(@ng-bind-html,'payAmountText')]")).getText().replaceAll("\\D+","");
        System.out.println(button);
        assertTrue("Стоимость «Заплатить» на кнопке некорректна", button.equals(Values.price.total));
    }

    @Step("Проверка стоимости аренды автомобиля")
    private void checkTransportPrice() {
        String inplace = $(byXpath("//div[contains(@translate,'onSite')]/following-sibling::div")).getText().replace(" ", "");
        inplace = inplace.substring(0, inplace.length()-1).replace(".", ",");
        System.out.println("«На месте» = " + inplace);
        System.out.println("«Transport» = " + Values.price.transport);
        assertTrue("Стоимость «На месте» некорректна", inplace.equals(Values.price.transport));

        String comment = $(byXpath("//div[@class='order-price__table-data-price ng-binding']")).getText().replace(" ", "");
        comment = comment.substring(0, comment.length()-1).replace(".", ",");
        System.out.println(comment);
        assertTrue("Стоимость аренды авто в комментарии некорректна", comment.equals(Values.price.transport));
    }


}
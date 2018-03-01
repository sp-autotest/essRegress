package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static config.Values.ln;
import static config.Values.text;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Created by mycola on 28.02.2018.
 */
public class TransportPage extends Page {
    int allPrice = 0;
    String allEuroPrice = "";

    @Step("Действие 10, Нажать на кнопку «Транспорт»")
    public void step10() {
        new EssPage().clickTransportButton();
        checkTransportBlock();
        checkCarFilter();
        checkSelectedCars();
    }

    @Step("Действие 11, Арендовать автомобиль")
    public void step11() {
        selectCar();
        int beforePrice = getCarPrice();
        addInsurance();
        checkAllPrice(getInsurancePrice(), beforePrice, getCarPrice());
        allPrice = getAllCarPrice();
        allEuroPrice = getEuroAllCarPrice();
        clickRentButton();
        checkTranspotrPriceInCard();
        checkRentButtonName();
        checkOptionsButton();
        checkTimeButton();
        checkInsuranceButton();
        checkTotalPrices();
    }

    @Step("Проверка перехода в раздел «Транспорт»")
    private void checkTransportBlock(){
        $(byXpath("//div[@id='left-column-transport'][contains(@class,'--active')]")).
                shouldBe(visible).shouldBe(exactText(text[2][ln]));
        $("#frame-cars").shouldBe(visible);
    }

    @Step("Проверка наличия фильтра для поиска автомобиля")
    private void checkCarFilter(){
        $(byXpath("//select[@name='stationReceive']")).shouldBe(visible).shouldBe(enabled);
        $(byXpath("//select[@name='stationReturn']")).shouldBe(visible).shouldBe(enabled);
        $("#dateStartCars").shouldBe(visible).shouldBe(enabled);
        $(byXpath("//input[@name='timeReceive']")).shouldBe(visible).shouldBe(enabled);
        $("#dateEndCars").shouldBe(visible).shouldBe(enabled);
        $(byXpath("//input[@name='timeReturn']")).shouldBe(visible).shouldBe(enabled);
        $("#auto\\/manual").shouldBe(visible).shouldBe(enabled);
        $(byXpath("//button[@onclick='priceSort(this);']")).shouldBe(visible).shouldBe(enabled);
        $(byXpath("//button[@onclick='sizeSort(this);']")).shouldBe(visible).shouldBe(enabled);
    }

    @Step("Проверка поиска авто с АКПП")
    private void checkSelectedCars(){
        $("#auto\\/manual").selectOptionByValue("1"); //выбрать только авто с АКПП
        Sleep(1);
        ElementsCollection gears = $$(byXpath("//div[@class='icon icon--auto-gear']/following-sibling::div"));
        for (int i=0; i< gears.size(); i++) {
            if (gears.get(i).isDisplayed()) {
                System.out.println("Gear =" + gears.get(i).getText());
                assertTrue("Фильтр по трансмиссии не сработал.", gears.get(i).getText().equals(text[7][ln]));
            }
        }
    }

    @Step("Выбрать автомобиль")
    private void selectCar(){
        ElementsCollection cars = $$(byXpath("//a[@class='auto-card__img ']"));
        for (int i=0; i<cars.size(); i++) {
            if (cars.get(i).isDisplayed()){
                cars.get(i).click();
                break;
            }
        }
    }

    @Step("Добавить страховку SPCDW")
    private void addInsurance(){
        $("#insurance-options-SPCDW").$(byXpath("descendant::div[@class='_button-group']")).click();
    }

    @Step("Нажать кнопку «Арендовать»")
    private void clickRentButton(){
        $("#button_down_price").click();
    }

    @Step("Проверка кнопки «В заказе»")
    private void checkRentButtonName(){
        $(byXpath("//div[@class='auto-card__button-top']")).shouldBe(visible).shouldBe(exactText(text[6][ln]));
    }

    @Step("Проверка кнопки «Изменить выбранные опции»")
    private void checkOptionsButton(){
        assertTrue("Кнопка не обнаружена", $(byXpath("//div[@class='auto-selected__options-button']/a"))
                .getText().equals(text[8][ln]));
    }

    @Step("Проверка кнопки «Изменить время и место»")
    private void checkTimeButton(){
        assertTrue("Кнопка не обнаружена", $(byXpath("//div[@class='auto-selected__rent-button']/a"))
                .getText().trim().equals(text[9][ln]));
    }

    @Step("Проверка кнопки «Изменить страховку»")
    private void checkInsuranceButton(){
        assertTrue("Кнопка не обнаружена", $(byXpath("//div[@class='auto-selected__options-button'][2]/a"))
                .getText().equals(text[10][ln]));
    }

    private int getInsurancePrice(){
        String p = $("#insurance-options-SPCDW").$(byXpath("descendant::div[@class='tile__price']")).getText();
        p = p.substring(0, p.length()-1);
        System.out.println("Car insurence price ="+p);
        return stringIntoInt(p);
    }

    private int getCarPrice(){
        String p = $(byXpath("//div[@id='button_down_price']/itm")).getText().replace(" ", "");
        System.out.println("Car price="+p);
        return stringIntoInt(p);
    }

    private int getAllCarPrice(){
        String p = $(byXpath("//div[@id='button_down_allprice']/itm")).getText().replace(" ", "");
        System.out.println("All car price ="+p);
        return stringIntoInt(p);
    }

    private String getEuroAllCarPrice(){
        String p = $(byXpath("//div[@id='button_down_allprice']/span/itmsub")).getText();
        System.out.println("Euro All car price ="+p);
        return p;
    }

    @Step("Проверка пересчета стоимости:\n- без доп. услуг {1}\n- страховка {0}\n- всего {2}")
    private void checkAllPrice(int insurance, int before, int after){
        assertTrue("Стоимость пересчитана неверно", after == before + insurance);
    }

    @Step("Проверка стоимости аренды автомобиля в корзине")
    private void checkTranspotrPriceInCard() {
        String leftPrice = $("#left-column-transport").$(byXpath("descendant::div[@class='cart__item-priceondemand-item-price']")).getText().replace(" ", "");
        leftPrice = leftPrice.substring(0, leftPrice.length()-1);
        System.out.println("Transport price = " + leftPrice);
        assertTrue("Стоимость страхования в корзине не совпадает с указанной в блоке", allPrice == stringIntoInt(leftPrice));
    }

    @Step("Проверка общей суммы заказа (включает в себя стоимость транспортных услуг)")
    private void checkTotalPrices(){
        String itemPrice;
        String flyPrice = $(byXpath("//div[@class='cart__item-price']")).getText().replace(" ", "");
        int summ = stringIntoInt(flyPrice.substring(0, flyPrice.length()-1));
        ElementsCollection items = $("#left-column-insurance-block").$$(byXpath("descendant::div[@class='cart__item-priceondemand-item-price']"));
        for (int i=0; i<items.size(); i++) {
            itemPrice = items.get(i).getText().replace(" ", "");
            System.out.println("Item[" + (i+1) + "] price = " + itemPrice);
            summ = summ + stringIntoInt(itemPrice.substring(0, itemPrice.length()-1));
        }
        summ = summ + allPrice;
        String totalPrice = $("#cart-total-incarts").$(byXpath("descendant::div[@class='cart__item-price']")).getText().replace(" ", "");
        System.out.println("Total price = " + totalPrice);
        assertTrue("Общая сумма заказа некорректна", summ == stringIntoInt(totalPrice.substring(0, totalPrice.length()-1)));
    }

}

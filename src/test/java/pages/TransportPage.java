package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import config.Values;
import org.openqa.selenium.interactions.Actions;
import ru.yandex.qatools.allure.annotations.Step;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static config.Values.ln;
import static config.Values.price;
import static config.Values.text;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Created by mycola on 28.02.2018.
 */
public class TransportPage extends Page {

    @Step("Действие 10, Нажать на кнопку «Транспорт»")
    public void step10(int test) {
        new EssPage().clickTransportButton();
        checkTransportBlock();
        if (test == 1) {
            screenShot("Скриншот");
            checkAeroexpressForm();
            checkAeroexpressInCard();
        }
        if (test == 2) {
            checkCarFilter();
            screenShot("Скриншот");
            checkSelectedCars();
        }
    }

    @Step("Действие 11, Арендовать автомобиль")
    public void step11() {
        selectCar();
        screenShot("Скриншот");
        int beforePrice = getCarPrice();
        addInsurance();
        screenShot("Скриншот");
        checkAllPrice(getInsurancePrice(), beforePrice, getCarPrice());
        price.nationalTransport = getAllCarPrice();
        price.transport = getEuroAllCarPrice();
        clickRentButton();
        screenShot("Скриншот");
        checkTranspotrPriceInCard();
        checkRentButtonName();
        checkOptionsButton();
        checkTimeButton();
        checkInsuranceButton();
        checkTotalPrices();
        saveTransportData();
    }

    @Step("Действие 12, Нажать Оплатить в корзине")
    public void step12() {
        $(byXpath("//a[@class='cart__item-counter-link']")).click();
        waitPlane();
    }

    @Step("Проверка перехода в раздел «Транспорт»")
    private void checkTransportBlock(){
        $(byXpath("//div[@id='left-column-transport'][contains(@class,'--active')]")).
                shouldBe(visible).shouldBe(exactText(text[2][ln]));
    }

    @Step("Проверка наличия фильтра для поиска автомобиля")
    private void checkCarFilter(){
        $("#frame-cars").shouldBe(visible).scrollTo();
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
        Sleep(1);
        ElementsCollection cars = $$(byXpath("//a[@class='auto-card__button']"));
        for (int i=0; i<cars.size(); i++) {
            if (cars.get(i).isDisplayed()){
                cars.get(i).click();
                break;
            }
        }
    }

    @Step("Добавить страховку SPCDW")
    private void addInsurance(){
        SelenideElement ins = $("#insurance-options-SPCDW");
        ins.scrollTo();
        ins.$(byXpath("descendant::div[@class='_button-group']")).click();
    }

    @Step("Нажать кнопку «Арендовать»")
    private void clickRentButton(){
        $("#button_down_price").click();
        Sleep(2);
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
        String p = $("#insurance-options-SPCDW").$(byXpath("descendant::div[@class='tile__price']")).getText().replaceAll("\\D+","");
        System.out.println("Car insurence price ="+p);
        return stringIntoInt(p);
    }

    private int getCarPrice(){
        String p = $(byXpath("//div[@id='button_down_price']/itm")).getText().replaceAll("\\D+","");
        System.out.println("Car price="+p);
        return stringIntoInt(p);
    }

    private String getAllCarPrice(){
        String p = $(byXpath("//div[@id='button_down_allprice']/itm")).getText().replaceAll("\\D+","");
        System.out.println("All car price ="+p);
        return p;
    }

    private String getEuroAllCarPrice(){
        String p = $(byXpath("//div[@id='button_down_allprice']/span/itmsub")).getText().replaceAll("\\D+","");
        System.out.println("Euro All car price ="+p);
        return p;
    }

    @Step("Проверка пересчета стоимости: без доп. услуг {1}, страховка {0}, всего {2}")
    private void checkAllPrice(int insurance, int before, int after){
        assertTrue("Стоимость пересчитана неверно\n" +
                "Ожидалось: " + (before + insurance) +
                "\nФактически: " + after,
                after == before + insurance);
    }

    @Step("Проверка стоимости аренды автомобиля в корзине")
    private void checkTranspotrPriceInCard() {
        String leftPrice = $("#left-column-transport").$(byXpath("descendant::div[@class='cart__item-priceondemand-item-price']")).getText().replaceAll("\\D+","");
        System.out.println("Transport price = " + leftPrice);
        assertTrue("Стоимость страхования в корзине не совпадает с указанной в блоке" +
                "\nОжидалось: " + price.nationalTransport +
                "\nФакически: " + leftPrice,
                price.nationalTransport.equals(leftPrice));
    }

    @Step("Проверка общей суммы заказа (включает в себя стоимость транспортных услуг)")
    private void checkTotalPrices(){
        String itemPrice;
        String flyPrice = $(byXpath("//div[@class='cart__item-price']")).getText().replaceAll("\\D+","");
        int summ = stringIntoInt(flyPrice);
        Actions actions = new Actions(getWebDriver());
        actions.moveToElement($("#left-column-insurance-block").toWebElement(),1,1).build().perform();
        Sleep(1);
        ElementsCollection items = $("#left-column-insurance-block").$$(byXpath("descendant::div[@class='cart__item-priceondemand-item-price']"));
        for (int i=0; i<items.size(); i++) {
            itemPrice = items.get(i).getText().replaceAll("\\D+","");
            System.out.println("Item[" + (i+1) + "] price = " + itemPrice);
            summ = summ + stringIntoInt(itemPrice);
        }
        String transportPrice = $("#left-column-transport").$(byXpath("descendant::div[@class='cart__item-priceondemand-item-price']")).getText().replaceAll("\\D+","");
        summ = summ + stringIntoInt(transportPrice);
        String totalPrice = $("#cart-total-incarts").$(byXpath("descendant::div[@class='cart__item-price']")).getText().replaceAll("\\D+","");
        System.out.println("Total price = " + totalPrice);
        assertTrue("Общая сумма заказа некорректна" +
                "\nОжидалось: " + summ +
                "\nФактически: " + totalPrice,
                summ == stringIntoInt(totalPrice));
    }

    private void saveTransportData(){
        Values.auto.name = $(byXpath("//div[@class='auto-card__title']")).getText();
        System.out.println("Auto = " + Values.auto.name);
        ElementsCollection ec = $$(byXpath("//div[@class='auto-selected__rent-element-text']"));
        for(int i = 0; i<ec.size(); i++) System.out.println("Auto = " + ec.get(i).getText());
        Values.auto.receiveLocation = ec.get(0).getText();
        String receiveDate = ec.get(1).getText() + ec.get(2).getText();
        try {
            Values.auto.receiveDate = new SimpleDateFormat("dd.MM.yyyyHH:mm").parse(receiveDate);
        }catch (ParseException e) {
            System.out.println("Parsing date error");
        }
        Values.auto.returnLocation = ec.get(3).getText();
        String returnDate = ec.get(4).getText() + ec.get(5).getText();
        try {
            Values.auto.returnDate = new SimpleDateFormat("dd.MM.yyyyHH:mm").parse(returnDate);
        }catch (ParseException e) {
            System.out.println("Parsing date error");
        }
    }

    @Step("Проверка отображения формы «Билеты на аэроэкспресс")
    private void checkAeroexpressForm(){
        SelenideElement h = $(byXpath("//div[@class='axpholder']")).shouldBe(exist).shouldBe(visible);
        h.$(byXpath("descendant::h2")).shouldBe(exactText(text[16][ln]));
    }

    @Step("Проверка отсутствия услуги «Аэроэкспресс» в корзине")
    private void checkAeroexpressInCard(){
        String service = "";
        if (ln == 0) {
            service = "Аэроэкспресс";
        }else {
            service = "Aeroexpress";
        }
        $("#left-column-transport").$(byXpath("descendant::div[contains(text(),'"+ service +"')]")).shouldNotBe(exist);
    }

}

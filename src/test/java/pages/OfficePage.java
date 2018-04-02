package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import config.Values;
import ru.yandex.qatools.allure.annotations.Step;
import struct.Flight;
import struct.Passenger;

import java.text.SimpleDateFormat;
import java.util.List;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.testng.AssertJUnit.assertTrue;


public class OfficePage extends Page{

    @Step("Форма «Заказы» открылась")
    private void checkOrderFormAppear() {
        $(byXpath("//h2[text()=' Заказы']")).shouldBe(visible);
        System.out.println("Order form appeared");
    }

    @Step("Вкладка с детализацией заказа открылась")
    private void checkOrderDetailsTabAppear(String pnr) {
        $(byXpath("//h2[contains(text(),'[" + pnr + "]')]")).shouldBe(visible);
        System.out.println("Order details Tab appeared");
    }

    @Step("Действие 17, Переход в АРМ ESS")
    public void authorization (String browser){
        System.out.println("\t17. Redirect to office ESS");
        open(Values.office_host);
        if (!browser.equals("ie")) {
            setLogin();
            setPassword();
            clickEnterButton();
        }
        checkOrderFormAppear();
    }

    @Step("Действие 18, Поиск заказа с PNR = {0}")
    public void searchOrder (String pnr) {
        System.out.println("\t18. Search order with PNR = "+pnr);
        setQueryField(pnr);
        clickSearchButton();
        checkOrderIsFound(pnr);
    }

    @Step("Действие 19, Открыть детализацию заказа {0}")
    public void openOrderDetails (String pnr, List<Flight> flyList, List<Passenger> passList) {
        System.out.println("\t19. Open order details");
        //String parentHandle = getWebDriver().getWindowHandle();
        clickOrder(pnr);
        //switchFromFirstPageToSecond(parentHandle);
        checkOrderDetailsTabAppear(pnr);
        checkServices();
        checkFlight(flyList);
        checkPassengers(passList);
        checkTariff();
    }

    @Step("Ввести логин")
    private void setLogin(){
        $(byName("login")).shouldBe(visible).setValue(Values.office_login);
    }

    @Step("Ввести пароль")
    private void setPassword(){
        $(byName("password")).shouldBe(visible).setValue(Values.office_password);
    }

    @Step("Нажать кнопку \"Вход\"")
    private void clickEnterButton(){
        $(byXpath("//button[@type='submit']")).shouldBe(visible).click();
    }

    @Step("Нажать кнопку поиска")
    private void clickSearchButton(){
        $(byXpath("//div/button[@type='submit']")).shouldBe(visible).click();
    }

    @Step("Ввести PNR в строку поиска")
    private void setQueryField(String pnr){
        $("#inputQuery").shouldBe(visible).setValue(pnr);
    }

    @Step("Заказ {0} найден")
    private void checkOrderIsFound(String pnr){
        $(byXpath("//td/a[text()='" + pnr + "']")).shouldBe(exist).shouldBe(visible);
    }

    @Step("Открыть заказ {0}")
    private void clickOrder(String pnr){
        String link = $(byXpath("//td/a[text()='" + pnr + "']")).getAttribute("href");
        open(link);
        Sleep(3);
    }

    @Step("Проверить сумму оплаченного тарифа")
    private void checkTariff(){
        String tarif = Values.price.total + Values.cur;
        String itogo = $(byXpath("//td[text()='Итого:']/following-sibling::td[2]")).getText();
        itogo = itogo.replace(" ", "");
        if (Values.cur.equals("RUB")|Values.cur.equals("CNY")) itogo = itogo.replaceFirst(",00", "");
        else itogo = itogo.replaceFirst(",", "");
        assertTrue("Сумма оплаченного тарифа не совпадает с забронированной" +
                   "\nОжидалось: " + tarif +
                   "\nФактически: " + itogo,
                   tarif.equals(itogo));
    }

    @Step("Проверить услуги ESS")
    private void checkServices(){
        SelenideElement strah = $(byXpath("//h4/a[text()=' Страховка']")).shouldBe(visible);
        strah.$(byXpath("../../table/tbody/tr")).shouldBe(text("Итого: 0,00"));
        $(byXpath("//h4/a[contains(text(),'Аэроэкспресс:')]")).shouldNotBe(visible);
        $(byXpath("//h4/a[contains(text(),'Авто:')]")).shouldNotBe(visible);
        $(byXpath("//h4/a[contains(text(),'Отель:')]")).shouldNotBe(visible);
    }

    @Step("Проверить данные перелета")
    private void checkFlight(List<Flight> flyList){
        ElementsCollection rows = $$(byXpath("//h5[text()='Перелет']/following-sibling::table/tbody/tr"));
        for (int i=0; i<rows.size(); i++){
            String number = rows.get(i).$(byXpath("td[1]")).getText();
            assertTrue("Номер " + (i+1) +"-го рейса не совпадает с забронированным" +
                       "\nОжидалось: " + flyList.get(i).number +
                       "\nФактически: " + number,
                       number.equals(flyList.get(i).number));

            String from = rows.get(i).$(byXpath("td[2]")).getText();
            if (from.equals("Москва, Шереметьево")) from = "SVO";
            else if (from.equals("Москва, Внуково")) from = "VKO";
            else if (from.equals("Прага, Аэропорт Вацлава Гавела")) from = "PRG";
            else from = flyList.get(i).from_orig;
            assertTrue("Направление ОТКУДА " + (i+1) +"-го рейса не совпадает с забронированным" +
                       "\nОжидалось: " + flyList.get(i).from_orig +
                       "\nФактически: " + from,
                       flyList.get(i).from_orig.equals(from));

            String to = rows.get(i).$(byXpath("td[4]")).getText();
            if (to.equals("Москва, Шереметьево")) to = "SVO";
            else if (to.equals("Москва, Внуково")) to = "VKO";
            else if (to.equals("Прага, Аэропорт Вацлава Гавела")) to = "PRG";
            else to = flyList.get(i).to_orig;
            assertTrue("Направление КУДА " + (i+1) +"-го рейса не совпадает с забронированным" +
                       "\nОжидалось: " + flyList.get(i).to_orig +
                       "\nФактически: " + to,
                       flyList.get(i).to_orig.equals(to));

            String date = rows.get(i).$(byXpath("td[5]")).getText();
            String dates = new SimpleDateFormat("HH:mm/").format(flyList.get(i).start);
            dates = dates + new SimpleDateFormat("HH:mm dd.MM.yyyy").format(flyList.get(i).end);
            assertTrue("Дата/время " + (i+1) +"-го рейса не совпадает с забронированным" +
                       "\nОжидалось: " + dates +
                       "\nФактически: " + date,
                       dates.equals(date));
        }
    }

    @Step("Проверить данные пассажиров")
    private void checkPassengers(List<Passenger> passList){
        ElementsCollection rows = $$(byXpath("//h5[text()='Пассажиры']/following-sibling::table[1]/tbody/tr"));
        for (int i=0; i<rows.size(); i++){
            String fio = (passList.get(i).lastname + " " + passList.get(i).firstname).toUpperCase();
            String name = rows.get(i).$(byXpath("td[1]/strong")).getText();
            assertTrue("ФИ " + (i+1) +"-го пассажира не совпадает с забронированным" +
                       "\nОжидалось: " + fio +
                       "\nФактически: " + name,
                       fio.equals(name));
            String type = rows.get(i).$(byXpath("td[1]")).getText().substring(fio.length());
            assertTrue("Тип " + (i+1) +"-го пассажира не совпадает с забронированным" +
                       "\nОжидалось: ADT" +
                       "\nФактически: " + type,
                       type.contains("(ADT, RU)"));
            String dob = rows.get(i).$(byXpath("td[2]")).getText().replace("-","");
            assertTrue("День рождения " + (i+1) +"-го пассажира не совпадает с забронированным" +
                       "\nОжидалось: " + passList.get(i).dob +
                       "\nФактически: " + dob,
                       dob.equals(passList.get(i).dob));
        }
    }



}

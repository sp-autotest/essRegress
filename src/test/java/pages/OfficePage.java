package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import config.Values;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import soap.SoapRequest;
import struct.CollectData;
import struct.Flight;
import struct.Passenger;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

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

    private CollectData collectData;

    public OfficePage(CollectData collectData) {
        this.collectData = collectData;
    }

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

    @Step("Действие {0}, Переход в АРМ ESS")
    public void authorization (int n){
        System.out.println("\t" + n + ". Redirect to office ESS");
        open(Values.office_host);
        Sleep(1);
        ElementsCollection login = $$(byName("login"));
        if (login.size()>0) {
            setLogin();
            setPassword();
            clickEnterButton();
        }
        checkOrderFormAppear();
    }

    @Step("Переход в АРМ ESS")
    public void authorizationStep (){
        open(Values.office_host);
        Sleep(1);
        ElementsCollection login = $$(byName("login"));
        if (login.size()>0) {
            setLogin();
            setPassword();
            clickEnterButton();
        }
        checkOrderFormAppear();
    }

    @Step("Действие {0}, Поиск заказа по PNR")
    public void searchOrder (int n) {
        String pnr = Values.getPNR(collectData.getTest());
        System.out.println("\t" + n + ". Search order with PNR = "+pnr);
        setQueryField(pnr);
        clickSearchButton();
        checkOrderIsFound(pnr);
    }
    @Step("Действие 17, Проверка даты/времени в АРМ")
    public void checkDateOnARM (List<Flight> flyList) {
        System.out.println("\t17. Open ans check order details");
        String pnr = Values.getPNR(collectData.getTest());
        clickOrder(pnr);
        checkOrderDetailsTabAppear(pnr);
        clickTransfer();
        screenShot();
        checkDateTime(flyList);
        checkTransferDateTime(flyList);
        checkTransferDateTimeInLog(flyList);
    }

    @Step("Действие 18, Проверка даты/времени в Sabre")
    public void checkSabre(List<Flight> flyList) {
        System.out.println("\t18. Check log in Sabre");
        String response = new SoapRequest(collectData).setPNRtoSabreCommand();
        Allure.addAttachment("Ответ Sabre", "text/xml", response.replace("><", ">\r\n<"));
        String[] lines = response.substring(response.indexOf("[ 1 "), response.indexOf("TKT/TIME")-10).split("CDATA");
        int i = 0;
        String end = null;
        for (String s : lines) {
            if (s.contains("/E]")) {
                System.out.println(s);
                String begin = s.substring(12, 17) + s.substring(32, 36);
                System.out.println(begin);
                String bDate =  new SimpleDateFormat("ddMMMHHmm", new Locale("en")).format(flyList.get(i).start).toUpperCase();
                assertTrue("Дата/время вылета в Sabre не совпадает с забронированным" +
                        "\nОжидалось : " + bDate + "\nФактически: " + begin, bDate.equals(begin));

                if (s.substring(44, 46).equals("/E")) {
                    end = s.substring(12, 17) + s.substring(38, 42);
                } else {
                    end = s.substring(45, 50) + s.substring(38, 42);
                }
                System.out.println(end);
                String eDate =  new SimpleDateFormat("ddMMMHHmm", new Locale("en")).format(flyList.get(i).end).toUpperCase();
                assertTrue("Дата/время прилета в Sabre не совпадает с забронированным" +
                           "\nОжидалось : " + eDate + "\nФактически: " + end, eDate.equals(end));
                i++;
            }
        }
    }

    @Step("Поиск заказа с PNR = {0}")
    public void searchOrderStep (String pnr) {
        setQueryField(pnr);
        clickSearchButton();
        checkOrderIsFound(pnr);
    }

    @Step("Действие 18, Открыть детализацию заказа")
    public void openOrderDetails (List<Flight> flyList, List<Passenger> passList) {
        System.out.println("\t19. Open order details");
        String pnr = Values.getPNR(collectData.getTest());
        clickOrder(pnr);
        checkOrderDetailsTabAppear(pnr);
        checkServices();
        checkFlight(flyList);
        checkPassengers(passList);
        checkTariff();
        screenShot();
    }

    public String getTransferNumberFromArm() {
        authorizationStep();
        searchOrderStep(Values.getPNR(collectData.getTest()));
        openTransfer();
        return getVoucherNumber();
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

    @Step("Нажать ТРАНСФЕР")
    private void clickTransfer(){
        $(byXpath("//a[contains(@href,'/transfer/view')]")).click();
        Sleep(3);
    }

    @Step("Проверить сумму оплаченного тарифа")
    private void checkTariff(){
        String tarif = Values.reportData[collectData.getTest()].getPrice().total + collectData.getCur();
        String itogo = $(byXpath("//td[text()='Итого:']/following-sibling::td[2]")).getText();
        itogo = itogo.replace(" ", "");
        if (collectData.getCur().equals("RUB")|collectData.getCur().equals("CNY")) itogo = itogo.replaceFirst(",00", "");
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
            String fio = (passList.get(i).getLastname() + " " + passList.get(i).getFirstname()).toUpperCase();
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
                       "\nОжидалось: " + passList.get(i).getDob() +
                       "\nФактически: " + dob,
                       dob.equals(passList.get(i).getDob()));
        }
    }


    @Step("Открыть раздел \"Трансфер\"")
    private void openTransfer(){
        System.out.println("Open Transfer details");
        String pnr = Values.getPNR(collectData.getTest());
        clickOrder(pnr);
        clickTransfer();
    }

    @Step("Извлечь номер ваучера")
    private String getVoucherNumber(){
        return $(byXpath("//table/tbody/tr/td[3]")).getText();
    }

    @Step("Проверить дату/время перелета")
    private void checkDateTime(List<Flight> flyList){
        ElementsCollection rows = $$(byXpath("//h5[text()='Перелет']/following-sibling::table/tbody/tr"));
        for (int i=0; i<rows.size(); i++){
            String date = rows.get(i).$(byXpath("td[5]")).getText();
            String dates = new SimpleDateFormat("HH:mm/").format(flyList.get(i).start)
                       + new SimpleDateFormat("HH:mm").format(flyList.get(i).end) + " "
                       + new SimpleDateFormat("dd.MM.yyyy").format(flyList.get(i).start);
            assertTrue("Дата/время " + (i+1) +"-го рейса не совпадает с забронированным" +
                       "\nОжидалось : " + dates +
                       "\nФактически: " + date,
                       dates.equals(date));
        }
    }

    @Step("Проверить дату/время трансфера на странице")
    private void checkTransferDateTime(List<Flight> flyList){
        String date = $(byXpath("//td[@style='white-space:nowrap;']")).getText();
        String dates = "00:00 " + new SimpleDateFormat("dd MMM yyyy", new Locale("en")).format(flyList.get(0).start);
        assertTrue("Дата/время трансфера на странице не совпадает с забронированным" +
                   "\nОжидалось : " + dates +
                   "\nФактически: " + date,
                   dates.equals(date));
    }

    @Step("Проверить дату/время трансфера в логе")
    private void checkTransferDateTimeInLog(List<Flight> flyList){
        String parentHandle = getWebDriver().getWindowHandle();
        $("#logTable").$(byXpath("descendant::a[contains(text(),'Input.json')]")).click();
        switchFromFirstPageToSecond(parentHandle);
        String date = $(byXpath("//pre")).getText();
        Allure.addAttachment("Лог трансфера", "application/json", date);
        int start = date.indexOf("dateArrival") + 14;
        date = date.substring(start, start + 16);
        String dates = new SimpleDateFormat("yyyy-MM-dd").format(flyList.get(0).start) + " 00:00";
        assertTrue("Дата/время трансфера в логе не совпадает с забронированным" +
                        "\nОжидалось : " + dates +
                        "\nФактически: " + date,
                dates.equals(date));
    }

}

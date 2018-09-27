import com.codeborne.selenide.WebDriverRunner;
import config.Values;
import dict.NationalityName;
import io.qameta.allure.Step;
import listeners.AllureOnEventListener;
import listeners.MyTransformer;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.testng.ITestResult;
import org.testng.TestNG;
import org.testng.annotations.*;
import pages.*;
import io.qameta.allure.Description;

import struct.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static pages.Page.*;

@Listeners({AllureOnEventListener.class})  //"слушатель" для Allure-отчета

public class EssTest {

    private Object[][] startData;
    private Object[][] startData7; //исходные данные для 7-го раздела
    private Object[][] startData8; //исходные данные для 8-го раздела
    private String browserName = "chrome";//браузер, по умолчанию хром

    @BeforeClass/* Метод, выполняющийся перед началом тест-сьюта */
    public void begin() {
        TestNG tng = new TestNG();
        tng.setAnnotationTransformer(new MyTransformer());
        Values.office_login = System.getProperty("officelogin", "any");//получить логин АРМ ESS из дженкинса
        Values.office_password = System.getProperty("officepassword", "");//получить пароль АРМ ESS из дженкинса
        startData = getStartUpParameters();//сформировать dataProvider из парамеров дженкинса
        startData7 = getStartUpParameters7(startData);
        startData8 = getStartUpParameters8(startData);
    }

    @Step("Запуск браузера")
    private void runBrowser(String browser, String res) {
        browserName = browser;
        int browserWidth = stringIntoInt(res.substring(0, res.indexOf("x")));//взять ширину браузера из строки с разрешением
        int browserHeight = stringIntoInt(res.substring(res.indexOf("x")+1));//взять высоту браузера из строки с разрешением

        com.codeborne.selenide.Configuration.browser = browserName;   //браузер для тестов
        com.codeborne.selenide.Configuration.timeout = 60000;         //максимальный интервал ожидания вебэлементов в милисекундах
        com.codeborne.selenide.Configuration.savePageSource = false;  //не сохранять дополнительные настройки
        WebDriver myWebDriver = null;
        switch (browser) {
            case "chrome":
                ChromeOptions options = new ChromeOptions();  //создать обьект для установки опций браузера хром
                options.addArguments("--disable-infobars");   //убрать в браузере полосу infobars
                options.addArguments("--disable-dev-tools");  //отключить в браузере dev-tools
                myWebDriver = new ChromeDriver(options);  //создать вебдрайвер с  указанными выше опциями
                break;
            case "firefox":
                myWebDriver = new FirefoxDriver();
                break;
            case "ie":
                myWebDriver = new InternetExplorerDriver();
                break;
            case "opera":
                OperaOptions oOptions = new OperaOptions();
                oOptions.setBinary("C:\\Program Files\\Opera\\launcher.exe");
                myWebDriver = new OperaDriver(oOptions);
                break;
        }
        WebDriverRunner.setWebDriver(myWebDriver); //запуск браузера
        myWebDriver.manage().window().setSize(new Dimension(browserWidth, browserHeight));
    }

    @BeforeMethod()
    public void start() {
        Values.docs = "";
        Values.ticket = 1;
        Values.pnr = null;
        Values.price = new Price();
        Values.auto = new Auto();
        Values.hotel = new Hotel();
        Values.errors.clear();//очистить массив неблокирующих ошибок перед каждым тестом
    }

    @AfterMethod
    public void stop(ITestResult testResult) throws IOException {
        getWebDriver().quit();
        // костыль, для того чтобы закрыть оперу, т.к. в ее драйвере есть баг
        // https://github.com/operasoftware/operachromiumdriver/issues/44
        if (browserName.equals("opera")) Runtime.getRuntime().exec("taskkill /f /im opera.exe");
    }

    @AfterClass
    public void tearDown() {
        getWebDriver().quit();
    }

    @DataProvider(name="data")
    public Object[][] parseLocaleData() {
        return startData;
    }

    @DataProvider(name="data7")
    public Object[][] parseLocaleData7() {
        return startData7;
    }

    @DataProvider(name="data8")
    public Object[][] parseLocaleData8() {
        return startData8;
    }

    @Description("Карта VISA;\nНаправление перелета: туда-обратно;\n" +
            "Состав бронирования авиаперелета, билеты: 2 взрослых;" +
            "Дополнительные услуги: «Полетная страховка», «Медицинская страховка» (классическая), «Отель»")
    @Test(priority = 1, description = "Раздел 1", groups = {"part1"}, dataProvider= "data")
    public void section1(String browser, String resolution, String language, String currency) {
        runBrowser(browser, resolution);
        int test = 1;
        Values.ln = getLanguageNumber(language);
        Values.cur = currency;

        System.out.println("==========================================================" +
                "\n*** AUTOTEST *** : section 1, " + browser + ", " + resolution + ", " +
                Values.lang[Values.ln][2].toUpperCase() + ", " + Values.cur +
                "\n==========================================================");

        InitialData initData = new InitialData(
                "MOW",//город "откуда"
                "PRG",//город "куда"
                null,//город "пересадка" для сложных маршрутов
                addMonthAndDays(new Date(),1,0),//дата "туда": плюс 1 месяц от текущей
                addMonthAndDays(new Date(),1,2),//дата "назад": плюс 1 месяц и 2 дня от текущей
                2,//взрослых
                0,//детей
                0//младенцев
        );
        open(Values.host + Values.lang[Values.ln][2]);
        SearchPage searchPg = new SearchPage(initData);
        searchPg.step1();
        List<Flight> flightList = searchPg.step2();
        List<Passenger> passList = createPassengers(initData);
        new PassengerPage().step3(passList);
        new PlacePage().clickPay();
        ChoosePage choosePg = new ChoosePage();
        choosePg.step4();
        EssPage essPg = new EssPage();
        essPg.step6();
        boolean timer = essPg.checkTimer();
        essPg.step7(flightList);
        essPg.step8();
        essPg.step9("COMMON_SPORT");
        TransportPage transportPg = new TransportPage();
        transportPg.step10(test);//шаг 10
        transportPg.checkAeroexpressPassengersList();//шаг 11
        transportPg.checkAeroexpressLogic(flightList);//шаг 12
        HotelPage hotelPg = new HotelPage(initData);
        hotelPg.clickResidenceButton("15");//шаг 15
        hotelPg.checkHotelFilter();//шаг 16
        hotelPg.checkHotelLogic(flightList, passList);//шаг 17
        hotelPg.checkFiltration();//шаг 19
        hotelPg.checkSorting();//шаг 20

        //искать нештрафную комнату
        int room = -1;
        for (int i=0; i<=9; i++) {
            hotelPg.selectHotel(i);//шаг 21
            room = hotelPg.selectRoomType();//шаг 22
            if (room>=0) break;
        }
        //------------------------

        hotelPg.clickBookButton(room);//шаг 23
        hotelPg.checkPassengersData(passList);//шаг 18
        hotelPg.clickPayInCart();//шаг 24
        choosePg.chooseTestStend("25");//шаг 25
        new EprPage().checkDataOnPayPage("26", flightList, passList, test, timer);//шаг 26
        PaymentPage paymentPg = new PaymentPage();
        paymentPg.checkPaymentForm1("27");//шаг 27
        paymentPg.setCardDetails("28");//шаг 28
        new ResultPage(passList).checkServicesData("29", test);//шаг 29
    }

    @Description("Карта VISA;\nHаправление перелета: туда-обратно;\n" +
            "Состав бронирования авиаперелета, билеты: 2 взрослых, 2 детских, 1 младенец;" +
            "Дополнительные услуги: «Полетная страховка», «Медицинская страховка» (Спортивная), «Аренда автомобиля»")
    @Test(priority = 2, description = "Раздел 2", groups = {"part2"}, dataProvider= "data")
    public void section2(String browser, String resolution, String language, String currency) {
        runBrowser(browser, resolution);
        int test = 2;
        Values.ln = getLanguageNumber(language);
        Values.cur = currency;

        System.out.println("==========================================================" +
                "\n*** AUTOTEST *** : section 2, " + browser + ", " + resolution + ", " +
                Values.lang[Values.ln][2].toUpperCase() + ", " + Values.cur +
                "\n==========================================================");

        InitialData initData = new InitialData(
                "MOW",//город "откуда"
                "PRG",//город "куда"
                null,//город "пересадка" для сложных маршрутов
                addMonthAndDays(new Date(),1,0),//дата "туда": плюс 1 месяц от текущей
                addMonthAndDays(new Date(),1,2),//дата "назад": плюс 1 месяц и 2 дня от текущей
                2,//взрослых
                2,//детей
                1//младенцев
        );
        open(Values.host + Values.lang[Values.ln][2]);
        SearchPage searchPg = new SearchPage(initData);
        searchPg.step1();
        List<Flight> flightList = searchPg.step2();
        List<Passenger> passList = createPassengers(initData);
        new PassengerPage().step3(passList);
        new PlacePage().clickPay();
        ChoosePage choosePg = new ChoosePage();
        choosePg.step4();
        EssPage essPg = new EssPage();
        essPg.step6();
        boolean timer = essPg.checkTimer();
        essPg.step7(flightList);
        essPg.step8();
        essPg.step9("TEAM_SPORTS");
        TransportPage transportPg = new TransportPage();
        transportPg.step10(test);
        transportPg.step11();
        transportPg.step12();
        choosePg.chooseTestStend("13");//шаг 13
        new EprPage().checkDataOnPayPage("14", flightList, passList, test, timer);//шаг 14
        PaymentPage paymentPg = new PaymentPage();
        paymentPg.checkPaymentForm2(Values.city.checkCityAndCurrencyEqual(initData.getCityTo(),currency));//шаг 15
        paymentPg.setCardDetails("16");//шаг 16
        new ResultPage(passList).checkServicesData("17", test);//шаг 17
    }

    @Description("Карта VISA;\nHаправление перелета: туда-обратно;\n" +
            "Состав бронирования авиаперелета, билеты: 2 взрослых;" +
            "Дополнительные услуги: «Полетная страховка», «Аэроэкспресс», «Трансфер»")
    @Test(priority = 3, description = "Раздел 3", groups = {"part3"}, dataProvider= "data")
    public void section3(String browser, String resolution, String language, String currency) {
        runBrowser(browser, resolution);
        int test = 3;
        Values.ln = getLanguageNumber(language);
        Values.cur = currency;

        System.out.println("==========================================================" +
                "\n*** AUTOTEST *** : section 3, " + browser + ", " + resolution + ", " +
                Values.lang[Values.ln][2].toUpperCase() + ", " + Values.cur +
                "\n==========================================================");


        InitialData initData = new InitialData(
                "MOW",
                "PRG",
                null,
                addMonthAndDays(new Date(),1,0),
                addMonthAndDays(new Date(),1,2),
                2,
                0,
                0
        );
        open(Values.host + Values.lang[Values.ln][2]);
        SearchPage searchPg = new SearchPage(initData);
        searchPg.step1();
        List<Flight> flightList = searchPg.step2();
        List<Passenger> passList = createPassengers(initData);
        new PassengerPage().step3(passList);
        new PlacePage().clickPay();//шаг 5
        ChoosePage choosePg = new ChoosePage();
        choosePg.step4();//шаг 6
        EssPage essPg = new EssPage();
        essPg.step6();//шаг 7
        boolean timer = essPg.checkTimer();
        essPg.step7(flightList);//шаг 8
        essPg.step8();//шаг 9
        TransportPage transportPg = new TransportPage();
        transportPg.step10(test);
        transportPg.checkAeroexpressPassengerLogic();//шаг 11
        transportPg.checkAeroexpressLogic(flightList);//шаг 12
        transportPg.addAeroexpressTickets();//шаг 13
        String dir = transportPg.setTransferLocations();//шаг 14
        transportPg.clickSelectStandartButton();//шаг 15
        transportPg.setTransferAdditionalInfo(flightList.get(0).start, dir);//шаг 16
        transportPg.selectTransfer(flightList.get(0).start, dir);//шаг 17
        transportPg.clickRepeatedlyContinue();//шаг 18
        choosePg.chooseTestStend("19");//шаг 19
        EprPage eprPg = new EprPage();
        eprPg.checkDataOnPayPage("20", flightList, passList, test, timer);//шаг 20
        eprPg.clickPayButton();//шаг 21
        PaymentPage paymentPg = new PaymentPage();
        paymentPg.setCardDetails("22");//шаг 22
        new ResultPage(passList).checkServicesData3(flightList.get(0));//шаг 23
    }

    @Description("Карта VISA;\nНаправление перелета: туда-обратно;\n" +
                 "Состав бронирования авиаперелета, билеты: 2 взрослых")
    @Test(priority = 4, description = "Раздел 4", groups = {"part4"}, dataProvider= "data")
    public void section4(String browser, String resolution, String language, String currency) {
        runBrowser(browser, resolution);
        int test = 4;
        Values.ln = getLanguageNumber(language);
        Values.cur = currency;

        System.out.println("==========================================================" +
                "\n*** AUTOTEST *** : section 4, " + browser + ", " + resolution + ", " +
                Values.lang[Values.ln][2].toUpperCase() + ", " + Values.cur +
                "\n==========================================================");


        InitialData initData = new InitialData(
                "MOW",
                "PRG",
                null,
                addMonthAndDays(new Date(),1,0),
                addMonthAndDays(new Date(),1,2),
                2,
                0,
                0
        );
        open(Values.host + Values.lang[Values.ln][2]);
        SearchPage searchPg = new SearchPage(initData);
        searchPg.step1();
        List<Flight> flightList = searchPg.step2();
        List<Passenger> passList = createPassengers(initData);
        new PassengerPage().step3(passList);
        new PlacePage().clickPay();
        ChoosePage choosePg = new ChoosePage();
        choosePg.step4();
        EssPage essPg = new EssPage();
        essPg.step6();
        boolean timer = essPg.checkTimer();
        essPg.step7(flightList);
        essPg.step8();
        essPg.deleteFlyInsurance();//шаг 9
        essPg.clickPayInCart();//шаг 10
        choosePg.chooseTestStend("11");//шаг 11
        new EprPage().checkDataOnPayPage("12", flightList, passList, test, timer);//шаг 12
        PaymentPage paymentPg = new PaymentPage();
        paymentPg.checkPaymentForm1("13");//шаг 13
        paymentPg.setCardDetails("14");//шаг 14
        new ResultPage(passList).checkServicesData("15", test);//шаг 15
        OfficePage officePg = new OfficePage();
        officePg.authorization();//шаг 17
        officePg.searchOrder(Values.pnr);//шаг 18
        officePg.openOrderDetails(Values.pnr, flightList, passList);//шаг 19
    }

    @Description("Карта VISA;\nНаправление перелета: туда-обратно;\n" +
            "Состав бронирования авиаперелета, билеты: 1 взрослый, 1 ребенок;" +
            "Дополнительные услуги: «Мед.страховка», «Авто», «Аэроэкспресс», «Трансфер», «Отель»")
    @Test(priority = 5, description = "Раздел 5", groups = {"part5"}, dataProvider= "data")
    public void section5(String browser, String resolution, String language, String currency) {
        runBrowser(browser, resolution);
        int test = 5;
        Values.ln = getLanguageNumber(language);
        Values.cur = currency;

        System.out.println("==========================================================" +
                "\n*** AUTOTEST *** : section 5, " + browser + ", " + resolution + ", " +
                Values.lang[Values.ln][2].toUpperCase() + ", " + Values.cur +
                "\n==========================================================");

        InitialData initData = new InitialData(
                "MOW",//город "откуда"
                "LAX",//город "куда"
                null,//город "пересадка" для сложных маршрутов
                addMonthAndDays(new Date(),1,0),//дата "туда": плюс 1 месяц от текущей
                addMonthAndDays(new Date(),1,2),//дата "назад": плюс 1 месяц и 2 дня от текущей
                1,//взрослых
                1,//детей
                0//младенцев
        );
        open(Values.host + Values.lang[Values.ln][2]);
        SearchPage searchPg = new SearchPage(initData);
        searchPg.step1();
        List<Flight> flightList = searchPg.step2();
        List<Passenger> passList = createPassengers(initData);
        new PassengerPage().step3(passList);
        new PlacePage().clickPay();
        ChoosePage choosePg = new ChoosePage();
        choosePg.step4();
        EssPage essPg = new EssPage();
        essPg.step6();
        boolean timer = essPg.checkTimer();
        essPg.step7(flightList);
        essPg.step8_5();
        essPg.step9("RANDOM");
        TransportPage transportPg = new TransportPage();
        transportPg.step10(test);//шаг 10
        transportPg.step11_5();//шаг 11

        transportPg.checkAeroexpressPassengerLogic();//шаг 11
        transportPg.addAeroexpressTickets();//шаг 13
        String dir = transportPg.setTransferLocations();//шаг 14
        transportPg.clickSelectStandartButton();//шаг 15
        transportPg.setTransferAdditionalInfo(flightList.get(0).start, dir);//шаг 16
        transportPg.selectTransfer(flightList.get(0).start, dir);//шаг 17


        HotelPage hotelPg = new HotelPage(initData);
        hotelPg.clickResidenceButton("15");//шаг 15
        hotelPg.checkHotelFilter();//шаг 16
        hotelPg.checkHotelLogic(flightList, passList);//шаг 17
        //искать не штрафную комнату
        int room = -1;
        for (int i=0; i<=9; i++) {
            hotelPg.selectHotel(i);//шаг 21
            room = hotelPg.selectRoomType();//шаг 22
            if (room>=0) break;
        }
        //------------------------
        hotelPg.clickBookButton(room);//шаг 23
        hotelPg.clickPayInCart();//шаг 24
        choosePg.chooseTestStend("25");//шаг 25
        new EprPage().checkDataOnPayPage("26", flightList, passList, test, timer);//шаг 26
        PaymentPage paymentPg = new PaymentPage();
        paymentPg.checkPaymentForm1("27");//шаг 27
        paymentPg.setCardDetails("28");//шаг 28
        new ResultPage(passList).checkServicesData5(flightList.get(0));//шаг 29
    }

    @Description("Карта VISA;\nНаправление перелета: в одну сторону;\n" +
            "Состав бронирования авиаперелета, билеты: 1 взрослый, 4 ребенка, 1 младенец;" +
            "Дополнительные услуги: «Мед.страховка», «Авто», «Аэроэкспресс», «Трансфер», «Отель»")
    @Test(priority = 6, description = "Раздел 6", groups = {"part6"}, dataProvider= "data")
    public void section6(String browser, String resolution, String language, String currency) {
        runBrowser(browser, resolution);
        int test = 5;
        Values.ln = getLanguageNumber(language);
        Values.cur = currency;

        System.out.println("==========================================================" +
                "\n*** AUTOTEST *** : section 6, " + browser + ", " + resolution + ", " +
                Values.lang[Values.ln][2].toUpperCase() + ", " + Values.cur +
                "\n==========================================================");

        InitialData initData = new InitialData(
                "MOW",//город "откуда"
                "LAX",//город "куда"
                null,//город "пересадка" для сложных маршрутов
                addMonthAndDays(new Date(),1,0),//дата "туда": плюс 1 месяц от текущей
                null,//дата "назад"
                1,//взрослых
                4,//детей
                1//младенцев
        );
        open(Values.host + Values.lang[Values.ln][2]);
        SearchPage searchPg = new SearchPage(initData);
        searchPg.step1();
        List<Flight> flightList = searchPg.step2();
        List<Passenger> passList = createPassengers(initData);
        new PassengerPage().step3(passList);
        new PlacePage().clickPay();
        ChoosePage choosePg = new ChoosePage();
        choosePg.step4();
        EssPage essPg = new EssPage();
        essPg.step6();
        boolean timer = essPg.checkTimer();
        essPg.step7(flightList);
        essPg.step8_5();
        essPg.step9("RANDOM");
        TransportPage transportPg = new TransportPage();
        transportPg.step10(test);//шаг 10
        transportPg.step11_5();//шаг 11
        transportPg.addAeroexpressTickets();//шаг 13
        String dir = transportPg.setTransferLocations();//шаг 14
        transportPg.clickSelectStandartButton();//шаг 15
        transportPg.setTransferAdditionalInfo(flightList.get(0).start, dir);//шаг 16
        transportPg.selectTransfer(flightList.get(0).start, dir);//шаг 17
        HotelPage hotelPg = new HotelPage(initData);
        hotelPg.clickResidenceButton("15");//шаг 15
        hotelPg.checkHotelFilter();//шаг 16
        hotelPg.checkHotelLogic(flightList, passList);//шаг 17
        //искать нештрафную комнату
        int room = -1;
        for (int i=0; i<=9; i++) {
            hotelPg.selectHotel(i);//шаг 21
            room = hotelPg.selectRoomType();//шаг 22
            if (room>=0) break;
        }
        //------------------------
        hotelPg.clickBookButton(room);//шаг 23
        hotelPg.clickPayInCart();//шаг 24
        choosePg.chooseTestStend("25");//шаг 25
        new EprPage().checkDataOnPayPage("26", flightList, passList, test, timer);//шаг 26
        PaymentPage paymentPg = new PaymentPage();
        paymentPg.checkPaymentForm1("27");//шаг 27
        paymentPg.setCardDetails("28");//шаг 28
        new ResultPage(passList).checkServicesData5(flightList.get(0));//шаг 29
    }
/*
    @Description("Карта VISA;\nНаправление перелета: туда-обратно;\n" +
            "Состав бронирования авиаперелета, билеты: 3 взрослых, 2 ребенка, 1 младенец;" +
            "Дополнительные услуги: «Мед.страховка», «Авто», «Аэроэкспресс», «Трансфер», «Отель»")
    @Test(priority = 7, description = "Раздел 7", groups = {"part7"}, dataProvider= "data7", enabled = false)
    public void section7(String browser, String resolution, String language, String currency,
                String adt1, String adt2, String adt3, String chd1, String chd2, String inf) {
        runBrowser(browser, resolution);
        int test = 5;
        Values.ln = getLanguageNumber(language);
        Values.cur = currency;
        List<String> nationality = new ArrayList<>();
        nationality.add(adt1);
        nationality.add(adt2);
        nationality.add(adt3);
        nationality.add(chd1);
        nationality.add(chd2);
        nationality.add(inf);

        System.out.println("==========================================================" +
                "\n*** AUTOTEST *** : section 7, " + browser + ", " + resolution + ", " +
                Values.lang[Values.ln][2].toUpperCase() + ", " + Values.cur +
                "\nadt1="+adt1+", adt2="+adt2+", adt3="+adt3+", chd1="+chd1+", chd2=" +chd2+", inf="+inf+
                "\n==========================================================");

        InitialData initData = new InitialData(
                "MOW",//город "откуда"
                "PRG",//город "куда"
                null,//город "пересадка" для сложных маршрутов
                addMonthAndDays(new Date(),1,0),//дата "туда": плюс 1 месяц от текущей
                addMonthAndDays(new Date(),1,2),//дата "назад": плюс 1 месяц и 2 дня от текущей
                3,//взрослых
                2,//детей
                1//младенцев
        );
        open(Values.host + Values.lang[Values.ln][2]);
        SearchPage searchPg = new SearchPage(initData);
        searchPg.step1();
        List<Flight> flightList = searchPg.step2();
        List<Passenger> passList = changeNationality(createPassengers(initData), nationality);
        for (Passenger p : passList) {
            p.toString();
        }
        new PassengerPage().step3(passList);
        new PlacePage().clickPay();
        ChoosePage choosePg = new ChoosePage();
        choosePg.step4();
        EssPage essPg = new EssPage();
        essPg.step6();
        boolean timer = essPg.checkTimer();
        essPg.step7(flightList);
        essPg.step8_7(passList);
        essPg.step9("RANDOM");
        TransportPage transportPg = new TransportPage();
        transportPg.step10(test);//шаг 10
        transportPg.step11_5();//шаг 11
        transportPg.addAeroexpressTickets();//шаг 13
        String dir = transportPg.setTransferLocations();//шаг 14
        transportPg.clickSelectStandartButton();//шаг 15
        transportPg.setTransferAdditionalInfo(flightList.get(0).start, dir);//шаг 16
        transportPg.selectTransfer(flightList.get(0).start, dir);//шаг 17
        HotelPage hotelPg = new HotelPage(initData);
        hotelPg.clickResidenceButton("15");//шаг 15
        hotelPg.checkHotelFilter();//шаг 16
        hotelPg.checkHotelLogic(flightList, passList);//шаг 17
        //искать нештрафную комнату
        int room = -1;
        for (int i=0; i<=9; i++) {
            hotelPg.selectHotel(i);//шаг 21
            room = hotelPg.selectRoomType();//шаг 22
            if (room>=0) break;
        }
        //------------------------
        hotelPg.clickBookButton(room);//шаг 23
        hotelPg.clickPayInCart();//шаг 24
        choosePg.chooseTestStend("25");//шаг 25
        new EprPage().checkDataOnPayPage("26", flightList, passList, test, timer);//шаг 26
        PaymentPage paymentPg = new PaymentPage();
        paymentPg.checkPaymentForm1("27");//шаг 27
        paymentPg.setCardDetails("28");//шаг 28
        new ResultPage(passList).checkServicesData5(flightList.get(0));//шаг 29
    }
*/
    @Description("Карта VISA;\nНаправление перелета: туда-обратно;\n" +
    "Состав бронирования авиаперелета, билеты: 1 взрослый, 2 ребенка;" +
    "Дополнительные услуги: «Выбор мест», «Питание», «Мед.страховка», «Авто», «Аэроэкспресс», «Трансфер»")
    @Test(priority = 8, description = "Раздел 8", groups = {"part8"}, dataProvider= "data8")
    public void section8(String browser, String resolution, String language, String currency, String city) {
        runBrowser(browser, resolution);
        int test = 8;
        Values.ln = getLanguageNumber(language);
        Values.cur = currency;

        System.out.println("=============================================================" +
                "\n*** AUTOTEST *** : section 8, " + browser + ", " + resolution + ", " +
                Values.lang[Values.ln][2].toUpperCase() + ", " + Values.cur + ", " + city +
                "\n=============================================================");

        InitialData initData = new InitialData(
                "MOW",//город "откуда"
                city, //город "куда"
                null, //город "пересадка" для сложных маршрутов
                addMonthAndDays(new Date(),1,0),//дата "туда": плюс 1 месяц от текущей
                addMonthAndDays(new Date(),1,2),//дата "назад": плюс 1 месяц и 2 дня от текущей
                1,//взрослых
                2,//детей
                0//младенцев
        );
        open(Values.host + Values.lang[Values.ln][2]);
        SearchPage searchPg = new SearchPage(initData);
        searchPg.step1();
        List<Flight> flightList = searchPg.step2();
        List<Passenger> passList = createPassengers(initData);
        new PassengerPage().step3(passList);
        new PlacePage().clickPay();
        ChoosePage choosePg = new ChoosePage();
        choosePg.step4_8();
        EssPage essPg = new EssPage();
        essPg.step6();
        essPg.checkSelectPlaceStep();//проверка выбора мест
        essPg.checkSelectFoodStep();//проверка основного блюда и десерта
        boolean timer = essPg.checkTimer();
        if (city.equals("LAX")) essPg.step8_5();
        else essPg.step8();
        essPg.step9("RANDOM");
        TransportPage transportPg = new TransportPage();
        transportPg.step10(test);//шаг 10
        transportPg.step11_5();//шаг 11
        transportPg.checkAeroexpressPassengerLogic();//шаг 11
        transportPg.addAeroexpressTickets();//шаг 13
        String dir = transportPg.setTransferLocations();//шаг 14
        transportPg.clickSelectStandartButton();//шаг 15
        transportPg.setTransferAdditionalInfo(flightList.get(0).start, dir);//шаг 16
        transportPg.selectTransfer(flightList.get(0).start, dir);//шаг 17
        transportPg.clickRepeatedlyContinue();//шаг 18
        choosePg.chooseTestStend("19");//шаг 19
        new EprPage().checkDataOnPayPage("20", flightList, passList, test, timer);//шаг 20
    }


    private List<Passenger> createPassengers(InitialData initData) {
        List<Passenger> passengerList = new ArrayList<Passenger>();
        for (int i=0; i<initData.getAdult(); i++) {
            Passenger p = new Passenger("ADT");
            passengerList.add(p);
        }
        for (int i=0; i<initData.getChild(); i++) {
            Passenger p = new Passenger("CHD");
            passengerList.add(p);
        }
        for (int i=0; i<initData.getInfant(); i++) {
            Passenger p = new Passenger("INF");
            passengerList.add(p);
        }
        return passengerList;
    }

    private List<Passenger> changeNationality(List<Passenger> passengers, List<String> natio) {
        int i=0;
        for (Passenger p : passengers) {
            p.setNationality(NationalityName.getNationalityByLanguage(natio.get(i),Values.ln));
            i++;
        }
        return passengers;
    }

    private Object[][] getStartUpParameters(){
        String chrome = System.getProperty("chrome_browser", "true");
        String firefox = System.getProperty("firefox_browser", "false");
        String opera = System.getProperty("opera_browser", "false");
        String ie = System.getProperty("ie_browser", "false");
        String res1 = System.getProperty("resolution_1920x1080", "true");
        String res2 = System.getProperty("resolution_1366x768", "false");
        String res3 = System.getProperty("resolution_1680x1050", "false");
        String res4 = System.getProperty("resolution_1280x1024", "false");
        String lc = System.getProperty("language_currency", "Russian,RUB");

        List<String> browsers = new ArrayList<>();
        List<String> resolutions = new ArrayList<>();
        List<String> languages = new ArrayList<>();

        if (chrome.equals("true")) browsers.add("chrome");
        if (firefox.equals("true")) browsers.add("firefox");
        if (opera.equals("true")) browsers.add("opera");
        if (ie.equals("true")) browsers.add("ie");
        if (res1.equals("true")) resolutions.add("1920x1080");
        if (res2.equals("true")) resolutions.add("1366x768");
        if (res3.equals("true")) resolutions.add("1680x1050");
        if (res4.equals("true")) resolutions.add("1280x1024");
        if (lc.equals("All")) {
            languages.add("French,EUR");
            languages.add("Spanish,EUR");
            languages.add("Italian,EUR");
            languages.add("Japanese,USD");
            languages.add("Chinese,USD");
            languages.add("English,USD");
            languages.add("Korean,RUB");
            languages.add("Russian,RUB");
            languages.add("German,RUB");
            languages.add("Russian,CNY");
            languages.add("Chinese,CNY");
            languages.add("German,CNY");
        }else languages.add(lc);

        int rows = browsers.size()*resolutions.size()*languages.size();
        Object[][] o = new Object[rows][4];
        int i = 0;
        for (String b: browsers) {
            for (String r: resolutions) {
                for (String l: languages) {
                    o[i][0] = b;
                    o[i][1] = r;
                    o[i][2] = l.substring(0, l.indexOf(","));
                    o[i][3] = l.substring(l.indexOf(",")+1);
                    System.out.println("DATA[" + i + "] = " + o[i][0] + " " + o[i][1] + " " + o[i][2] + " " + o[i][3]);
                    i++;
                }
            }
        }
        return o;
    }

    private Object[][] getStartUpParameters7(Object[][] sData){
        int n;
        String natio[][] = {
                {"us", "us", "us", "us", "us", "us"},
                {"us", "ru", "az", "us", "ru", "ru"},
                {"us", "ru", "de", "de", "ru", "ru"},
                {"de", "zh", "az", "zh", "zh", "ru"},
                {"es", "br", "pl", "gb", "ru", "zh"},
                {"ru", "ru", "ru", "us", "us", "hu"},
        };
        Object[][] o = new Object[sData.length*natio.length][10];
        for (int i=0; i<sData.length; i++){
            for (int j=0; j<natio.length; j++) {
                n = i*6 + j;
                o[n][0] = sData[i][0];
                o[n][1] = sData[i][1];
                o[n][2] = sData[i][2];
                o[n][3] = sData[i][3];
                o[n][4] = natio[j][0];
                o[n][5] = natio[j][1];
                o[n][6] = natio[j][2];
                o[n][7] = natio[j][3];
                o[n][8] = natio[j][4];
                o[n][9] = natio[j][5];
            }
        }
        return o;
    }

    private Object[][] getStartUpParameters8(Object[][] sData){
        int n;
        String city[] = {"PRG", "LAX"};
        Object[][] o = new Object[sData.length*city.length][5];
        for (int i=0; i<sData.length; i++){
            for (int j=0; j<city.length; j++) {
                n = i*2 + j;
                o[n][0] = sData[i][0];
                o[n][1] = sData[i][1];
                o[n][2] = sData[i][2];
                o[n][3] = sData[i][3];
                o[n][4] = city[j];
            }
        }
        return o;
    }

}

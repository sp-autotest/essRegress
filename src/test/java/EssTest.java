import com.codeborne.selenide.WebDriverRunner;
import config.Values;
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
import org.testng.TestNG;
import org.testng.annotations.*;
import pages.*;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.annotations.Title;
import struct.Flight;
import struct.Passenger;
import java.util.List;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static pages.Page.getLanguageNumber;
import static pages.Page.stringIntoInt;


@Listeners({AllureOnEventListener.class})  //"слушатель" для Allure-отчета
@Title("Aeroflot Test Suite")
public class EssTest {


    private String browserName = "chrome";//браузер, по умолчанию хром
    private int browserWidth = 1920;//ширина окна браузера, по умолчанию 1920
    private int browserHight = 1080;//высота окна браузера, по умолчанию 1080

    @BeforeClass/* Метод, выполняющийся перед началом тест-сьюта */
    public void begin() {
        TestNG tng = new TestNG();
        tng.setAnnotationTransformer(new MyTransformer());
        browserName = System.getProperty("browser", "chrome");//получить имя браузера из дженкинса, при неудаче браузер=хром
        String res = System.getProperty("resolution", "1920x1080");//получить разрешение браузера из дженкинса, при неудаче разрешение=1920x1080
        browserWidth = stringIntoInt(res.substring(0, res.indexOf("x")));//взять ширину браузера из строки с разрешением
        browserHight = stringIntoInt(res.substring(res.indexOf("x")+1));//взять высоту браузера из строки с разрешением
        System.out.println("Browser = " + browserName);//вывести в лог значение имени браузера
        System.out.println("Resolution = " + res);//вывести в лог значение разрешения
        Values.office_login = System.getProperty("officelogin", "any");//получить логин АРМ ESS из дженкинса
        Values.office_password = System.getProperty("officepassword", "");//получить пароль АРМ ESS из дженкинса
    }

    @BeforeMethod
    public void start() {
        com.codeborne.selenide.Configuration.browser = browserName;   //браузер для тестов
        com.codeborne.selenide.Configuration.timeout = 60000;         //максимальный интервал ожидания вебэлементов в милисекундах
        com.codeborne.selenide.Configuration.savePageSource = false;  //не сохранять дополнительные настройки
        WebDriver myWebDriver = null;
        switch (browserName) {
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
        myWebDriver.manage().window().setSize(new Dimension(browserWidth, browserHight));
    }

    @AfterMethod
    public void stop() {
        getWebDriver().quit();
    }

    @AfterClass
    public void tearDown() {
        getWebDriver().quit();
    }

    @DataProvider
    public Object[][] parseLocaleData() {
        return new Object[][]{
            {"Французский", "EUR"},
            {"Испанский",   "EUR"},
            {"Итальянский", "EUR"},
            {"Японский",    "USD"},
            {"Китайский",   "USD"},
            {"Английский",  "USD"},
            {"Корейский",   "RUB"},
            {"Русский",     "RUB"},
            {"Немецкий",    "RUB"},
            {"Русский",     "CNY"},
            {"Китайский",   "CNY"},
            {"Немецкий",    "CNY"},
        };
    }

    @Stories("Раздел 1 регрессионных испытаний")
    @Title("Тестирование ESS, раздел 1")
    @Description("Карта VISA;\nНаправление перелета: туда-обратно;\n" +
            "Состав бронирования авиаперелета, билеты: 2 взрослых;\n" +
            "Дополнительные услуги: «Полетная страховка», «Медицинская страховка» (классическая), «Отель»")
    @Test(priority = 1, dataProvider = "parseLocaleData", description = "Раздел 1", groups = {"part1"})
    public void section1(String locale, String currency) {
        int test = 1;
        Values.ln = getLanguageNumber(locale);
        Values.cur = currency;
        Values.docs = "";
        Values.ticket = 1;
        System.out.println("=========================================================="+
        "\n\t\t*** AUTOTEST *** : section" + test + ", " + Values.lang[Values.ln][2].toUpperCase()+
        ", " + currency + "\n==========================================================");
        open(Values.host);
        SearchPage searchPg = new SearchPage();
        searchPg.step1(test);
        List<Flight> flightList = searchPg.step2();
        List<Passenger> passList = new PassengerPage().step3();
        new PlacePage().clickPay();
        ChoosePage choosePg = new ChoosePage();
        choosePg.step4();
        EssPage essPg = new EssPage();
        essPg.step6();
        essPg.step7(flightList);
        essPg.step8();
        essPg.step9("COMMON_SPORT");
        TransportPage transportPg = new TransportPage();
        transportPg.step10(test);//шаг 10
        transportPg.checkAeroexpressPassengersList();//шаг 11
        transportPg.checkAeroexpressLogic(flightList);//шаг 12
        HotelPage hotelPg = new HotelPage();
        hotelPg.clickResidenceButton();//шаг 15
        hotelPg.checkHotelFilter();//шаг 16
        hotelPg.checkHotelLogic(flightList);//шаг 17
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
        new EprPage().checkDataOnPayPage("26", flightList, passList, test);//шаг 26
        PaymentPage paymentPg = new PaymentPage();
        paymentPg.checkPaymentForm1("27");//шаг 27
        paymentPg.setCardDetails("28");//шаг 28
        new ResultPage().checkServicesData("29", test);//шаг 29
    }

    @Stories("Раздел 2 регрессионных испытаний")
    @Title("Тестирование ESS, раздел 2")
    @Description("Карта VISA;\nHаправление перелета: туда-обратно;\n" +
            "Состав бронирования авиаперелета, билеты: 2 взрослых, 2 детских, 1 младенец;\n" +
            "Дополнительные услуги: «Полетная страховка», «Медицинская страховка» (Спортивная), «Аренда автомобиля»")
    @Test(priority = 2, dataProvider = "parseLocaleData", description = "Раздел 2", groups = {"part2"})
    public void section2(String locale, String currency) {
        int test = 2;
        Values.ln = getLanguageNumber(locale);
        Values.cur = currency;
        Values.docs = "";
        Values.ticket = 1;
        System.out.println("=========================================================="+
        "\n\t\t*** AUTOTEST *** : section" + test + ", " + Values.lang[Values.ln][2].toUpperCase()+
        ", " + currency + "\n==========================================================");
        open(Values.host);
        SearchPage searchPg = new SearchPage();
        searchPg.step1(test);
        List<Flight> flightList = searchPg.step2();
        List<Passenger> passList = new PassengerPage().step3();
        new PlacePage().clickPay();
        ChoosePage choosePg = new ChoosePage();
        choosePg.step4();
        EssPage essPg = new EssPage();
        essPg.step6();
        essPg.step7(flightList);
        essPg.step8();
        essPg.step9("TEAM_SPORTS");
        TransportPage transportPg = new TransportPage();
        transportPg.step10(test);
        transportPg.step11();
        transportPg.step12();
        choosePg.chooseTestStend("13");//шаг 13
        new EprPage().checkDataOnPayPage("14", flightList, passList, test);//шаг 14
        PaymentPage paymentPg = new PaymentPage();
        paymentPg.checkPaymentForm2();//шаг 15
        paymentPg.setCardDetails("16");//шаг 16
        new ResultPage().checkServicesData("17", test);//шаг 17
    }

    @Stories("Раздел 3 регрессионных испытаний")
    @Title("Тестирование ESS, раздел 3")
    @Description("Карта VISA;\nHаправление перелета: туда-обратно;\n" +
            "Состав бронирования авиаперелета, билеты: 2 взрослых;\n" +
            "Дополнительные услуги: «Полетная страховка», «Аэроэкспресс», «Трансфер»")
    @Test(priority = 3, dataProvider = "parseLocaleData", description = "Раздел 3", groups = {"part3"}, enabled = false)
    public void section3(String locale, String currency) {
        int test = 3;
        Values.ln = getLanguageNumber(locale);
        Values.cur = currency;
        Values.docs = "";
        Values.ticket = 1;
        System.out.println("=========================================================="+
                "\n\t\t*** AUTOTEST *** : section" + test + ", " + Values.lang[Values.ln][2].toUpperCase()+
                ", " + currency + "\n==========================================================");
        open(Values.host);
        SearchPage searchPg = new SearchPage();
        searchPg.step1(test);
        List<Flight> flightList = searchPg.step2();
        List<Passenger> passList = new PassengerPage().step3();
        new PlacePage().clickPay();
        ChoosePage choosePg = new ChoosePage();
        choosePg.step4();
        EssPage essPg = new EssPage();
        essPg.step6();
        essPg.step7(flightList);
        essPg.step8();
        TransportPage transportPg = new TransportPage();
        transportPg.step10(test);
        transportPg.checkAeroexpressPassengerLogic();//шаг 11
        transportPg.checkAeroexpressLogic(flightList);//шаг 12
        transportPg.addAeroexpressTickets();//шаг 13
        String dir = transportPg.setTransferLocations();//шаг 14
        transportPg.clickSelectStandartButton();//шаг 15
        transportPg.setTransferAdditionalInfo(flightList.get(0).start, dir);//шаг 16
        transportPg.selectTransfer(flightList.get(0).start, dir);//шаг 17


    }

    @Stories("Раздел 4 регрессионных испытаний")
    @Title("Тестирование ESS, раздел 4")
    @Description("Карта VISA;\nНаправление перелета: туда-обратно;\n" +
                 "Состав бронирования авиаперелета, билеты: 2 взрослых")
    @Test(priority = 4, dataProvider = "parseLocaleData", description = "Раздел 4", groups = {"part4"})
    public void section4(String locale, String currency) {
        int test = 4;
        Values.ln = getLanguageNumber(locale);
        Values.cur = currency;
        Values.docs = "";
        Values.ticket = 1;
        System.out.println("=========================================================="+
                "\n\t\t*** AUTOTEST *** : section" + test + ", " + Values.lang[Values.ln][2].toUpperCase()+
                ", " + currency + "\n==========================================================");
        open(Values.host);
        SearchPage searchPg = new SearchPage();
        searchPg.step1(test);
        List<Flight> flightList = searchPg.step2();
        List<Passenger> passList = new PassengerPage().step3();
        new PlacePage().clickPay();
        ChoosePage choosePg = new ChoosePage();
        choosePg.step4();
        EssPage essPg = new EssPage();
        essPg.step6();
        essPg.step7(flightList);
        essPg.step8();
        essPg.deleteFlyInsurance();//шаг 9
        essPg.clickPayInCart();//шаг 10
        choosePg.chooseTestStend("11");//шаг 11
        new EprPage().checkDataOnPayPage("12", flightList, passList, test);//шаг 12
        PaymentPage paymentPg = new PaymentPage();
        paymentPg.checkPaymentForm1("13");//шаг 13
        paymentPg.setCardDetails("14");//шаг 14
        new ResultPage().checkServicesData("15", test);//шаг 15
        OfficePage officePg = new OfficePage();
        officePg.authorization(browserName);//шаг 17
        officePg.searchOrder(Values.pnr);//шаг 18
        officePg.openOrderDetails(Values.pnr, flightList, passList);//шаг 19
    }


}

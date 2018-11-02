package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import config.Values;
import io.qameta.allure.Step;
import struct.CollectData;
import struct.Flight;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.url;


/**
 * Created by mycola on 22.02.2018.
 */
public class PlacePage extends Page {

    private CollectData collectData;

    private String env = System.getProperty("area", "RC");//получить имя площадки из дженкинса, при неудаче площадка=RC

    public PlacePage(CollectData collectData) {
        this.collectData = collectData;
    }

    @Step("Кликнуть \"Оплатить\" на странице выбора места")
    public void clickPay() {
        for (int i=0; i<30; i++) {
            if ($$(byXpath("//div[@class='text text--inline']")).size()>0){
                Sleep(1);
                if ($$(byXpath("//div[@class='text text--inline']")).size()>0) {
                    $(byXpath("//div[@class='text text--inline']")).click();
                }
            }
            if ($$(byXpath("//span[@class='next__button-inner']")).size()>0) clickNextButton();
            if ($$(byXpath("//h1[text()='Вход в тестовую среду системы ЕПР']")).size()>0) break;
            Sleep(2);
        }
    }

    @Step("Извлечь дату/время перелета")
    public List<Flight> getFlightData(){
        for (int i=0; i<30; i++) {
            if ($$(byXpath("//div[@class='cart__item-pricelist']")).size()>0) break;
            Sleep(2);
        }
        $(byXpath("//div[@class='cart__item-pricelist']")).shouldBe(Condition.visible);
        List<Flight> flightList = new ArrayList<Flight>();
        ElementsCollection flights = $$(byXpath("//div[@class='h-color--gray h-mt--4']"));
        for (SelenideElement f : flights) {
            System.out.println(f.innerText());
            Flight flight = new Flight();
            String start = f.innerText().split("—")[0];
            try {
                flight.start = new SimpleDateFormat("dd MMMM yyyy г., HH:mm", new Locale("ru")).parse(start);
                System.out.println("[" + flight.start + "] ");
            } catch (ParseException e) {
                System.out.println("Дата вылета нераспаршена: " + start);
            }
            String stop = f.innerText().substring(0, f.innerText().indexOf("г.,")) + f.innerText().split("—")[1];  //тут возможна ошибка на 1 сутки если есть оранжевый +1
            System.out.println(stop);
            try {
                flight.end = new SimpleDateFormat("dd MMMM yyyy HH:mm", new Locale("ru")).parse(stop);
                System.out.println("[" + flight.end + "] ");
            } catch (ParseException e) {
                System.out.println("Дата вылета нераспаршена: " + stop);
            }
            flightList.add(flight);
        }
        return flightList;
    }

    @Step("Действие 4, Извлечь PNR")
    public void getPNR(){
        String pnr;
        for (int i=0; i<30; i++) {
            if ($$(byXpath("//div[@class='text text--inline']")).size()>0) {
                Sleep(1);
                if ($$(byXpath("//div[@class='text text--inline']")).size() > 0) break;
            }
        }
        if ($$(byXpath("//h1[text()='Вход в тестовую среду системы ЕПР']")).size()>0) {
            int start = url().indexOf("&PNR") + 5;
            pnr = url().substring(start, start + 6);
        } else {
            String text = $(byXpath("//div[contains(@class, 'cart__item-title--uppercase')]")).getText();
            pnr = text.substring(text.length() - 6);
        }
        Values.setPNR(collectData.getTest(), pnr);
        System.out.println("PNR = " + pnr);
    }

    @Step("Действие 5, Зайти в витрину с бэкдора")
    public void goBackDoor() {
        String backdoor;
        if (env.equals("RC")) backdoor = Values.backdoor_host;
        else backdoor = Values.backdoor_host.replace("ws.ess", "ws-nf.ess");
        open(backdoor + Values.getPNR(collectData.getTest()));
        String link = $(byXpath("//a")).shouldBe(visible).getText().
                replaceFirst("Language=RU", "Language="+Values.lang[collectData.getLn()][2].toUpperCase());
        System.out.println("Backdoor link = " + link);
        open(link);
    }

    @Step("Нажать кнопку Продолжить")
    private void clickNextButton(){
        $(byXpath("//span[@class='next__button-inner']")).click();
    }

}

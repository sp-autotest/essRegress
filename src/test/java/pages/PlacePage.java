package pages;

import config.Values;
import io.qameta.allure.Step;
import struct.CollectData;

import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.WebDriverRunner.url;
//import static config.Values.pnr;


/**
 * Created by mycola on 22.02.2018.
 */
public class PlacePage extends Page {

    private CollectData collectData;

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
                    //break;
                }
            }
            if ($$(byXpath("//h1[text()='Вход в тестовую среду системы ЕПР']")).size()>0) break;
            Sleep(2);
        }
    }

    @Step("Извлечь PNR")
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


}

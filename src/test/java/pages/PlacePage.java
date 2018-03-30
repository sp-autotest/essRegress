package pages;

import ru.yandex.qatools.allure.annotations.Step;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


/**
 * Created by mycola on 22.02.2018.
 */
public class PlacePage extends Page {

    @Step("Кликнуть \"Оплатить\" на странице выбора места")
    public static void clickPay() {
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

}

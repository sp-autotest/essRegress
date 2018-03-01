package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;

/**
 * Created by mycola on 22.02.2018.
 */
public class PlacePage extends Page {

    @Step("Кликнуть \"Оплатить\" на странице выбора места")
    public void clickPay(){
        $(byXpath("//div[@class='text text--inline']")).shouldBe(Condition.visible);
        Sleep(1);
        $(byXpath("//div[@class='text text--inline']")).click();
    }

}

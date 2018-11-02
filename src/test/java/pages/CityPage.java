package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import config.Values;
import io.qameta.allure.Step;
import struct.City;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

/**
 * Created by mycola on 11.10.2018.
 */
public class CityPage extends Page {

    @Step("Подготовка списка городов")
    public List<City> getCityList() {
        List<City> cities = new ArrayList<>();
        File citiesf = new File("cities.txt");
        if(citiesf.exists()) {
            System.out.println("File cities.txt exists");
            try {
                FileInputStream fstream = new FileInputStream(citiesf);
                BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
                String line;
                while ((line = br.readLine()) != null) {
                    City city = new City();
                    city.setCode(line.split(" ")[0]);
                    city.setHour(stringIntoInt(line.split(" ")[1]));
                    cities.add(city);
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            System.out.println("File cities.txt not found");
            open(Values.city_table_host);
            SelenideElement table = $(byXpath("//table[@class='tariff_list']")).shouldBe(visible);
            ElementsCollection rows = table.$$(byXpath("descendant::tr"));
            try {
                boolean created = citiesf.createNewFile();
                if(created) System.out.println("File cities.txt has been created");
                FileWriter writer = new FileWriter(citiesf, true);
                for (SelenideElement row : rows) {
                    ElementsCollection cells = row.$$(byXpath("descendant::td"));
                    if (cells.size() == 7) {
                        if (cells.get(6).getText().trim().length() == 3) {
                            if (cells.get(0).getText().equals("HMA")) continue; //Ханты-Мансийск отсутствует в СБ, пропускаем
                            if (cells.get(0).getText().equals("SLY")) continue; //Салехард отсутствует в СБ, пропускаем
                            if (cells.get(0).getText().equals("KZO")) continue; //Кызылорда отсутствует в СБ, пропускаем
                            City city = new City();
                            city.setCode(cells.get(0).getText());
                            city.setHour(stringIntoInt(cells.get(6).getText()));
                            cities.add(city);
                            writer.write(city.getCode() + " " + city.getHour() + "\n");
                        }
                    }
                }
                writer.flush();
                writer.close();
            } catch(IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        System.out.println(cities.toString());
        return cities;
    }

}

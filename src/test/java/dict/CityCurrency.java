package dict;

import java.util.HashMap;

/**
 * Created by mycola on 20.08.2018.
 */
public class CityCurrency {

    private static HashMap<String, String> map;

    public CityCurrency() {
        map = new HashMap<>();
        map.put("PRG", "EUR");
        map.put("LAX", "USD");
        map.put("BJS", "CNY");
        map.put("MOW", "RUB");
    }

    public boolean checkCityAndCurrencyEqual(String city, String cur) {
        String currency = map.get(city);
        if ((null != currency)&&(currency.equals(cur))) return true;
        return false;
    }

}

package dict;

import java.util.HashMap;

/**
 * Created by mycola on 02.08.2018.
 */
public class СityInfo {

    private static HashMap<String, CityData> map;

    public СityInfo() {
        map = new HashMap<>();
        map.put("PRG", new CityData("Праге", "Prague", "EUR"));
        map.put("LAX", new CityData("Лос-Анджелесе (Калифорния)","Los Angeles (CA)", "USD"));
        map.put("BJS",  new CityData("Пекине", "Pekin", "CNY"));
        map.put("MOW",  new CityData("Москве", "Moscow", "RUB"));
    }

    public String getCity(String city, int ln) {
        if (ln == 0) return map.get(city).getRusName();
        return map.get(city).getEngName();
    }

    public boolean checkCityAndCurrencyEqual(String city, String cur) {
        String currency = map.get(city).getCurrency();
        if ((null != currency)&&(currency.equals(cur))) return true;
        return false;
    }

    private static class CityData {
        private String rusName;
        private String engName;
        private String currency;

        public CityData(String rusName, String engName, String currency) {
            this.rusName = rusName;
            this.engName = engName;
            this.currency = currency;
        }

        public String getRusName() {
            return rusName;
        }
        public String getEngName() {
            return engName;
        }
        public String getCurrency() {
            return currency;
        }
    }

}

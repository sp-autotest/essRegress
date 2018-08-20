package dict;

import java.util.HashMap;

/**
 * Created by mycola on 02.08.2018.
 */
public class СityHotel {

    private static HashMap<String, String> map;

    public СityHotel() {
        map = new HashMap<>();
        map.put("PRG", "Праге,Prague,Prague,Prague,Prague,Prague,Prague,Prague,Prague");
        map.put("LAX", "Лос-Анджелесе (Калифорния),Los Angeles (CA),Los Angeles (CA),Los Angeles (CA),Los Angeles (CA),Los Angeles (CA),Los Angeles (CA),Los Angeles (CA),Los Angeles (CA)");
        map.put("BJS", "Пекине,Pekin");
        map.put("MOW", "Москве,Moscow");
    }

    public String getCity(String city) {
        return map.get(city);
    }

}

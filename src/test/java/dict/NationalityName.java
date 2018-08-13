package dict;

import java.util.HashMap;

/**
 * Created by mycola on 08.08.2018.
 */
public class NationalityName {

    private static HashMap<String, String> map;

    public NationalityName() {
        map = new HashMap<>();
        map.put("ru", "Россия,Russia,Russland,Rusia,Russia,Russie,俄罗斯,러시아,ロシア");
        map.put("us", "США,United States,USA,Estados Unidos,Stati Uniti,États-Unis,美国,미국,アメリカ合衆国");
        map.put("zh", "Китай,China,China,China,Cina,Chine,中国,중국,中国");
        map.put("de", "Германия,Germany,Deutschland,Alemania,Germania,Allemagne,德国,독일,ドイツ");
        map.put("az", "Азербайджан,Azerbaijan,Aserbaidschan,Azerbayán,Azerbaigian,Azerbaïdjan,阿塞拜疆,아제르바이잔,アゼルバイジャン");
        map.put("es", "Испания,Spain,Spanien,España,Spagna,Espagne,西班牙,스페인,スペイン");
        map.put("br", "Бразилия,Brazil,Brasilien,Brasil,Brasile,Brésil,巴西,브라질,ブラジル");
        map.put("pl", "Польша,Poland,Polen,Polonia,Polonia,Pologne,波兰,폴란드,ポーランド");
        map.put("gb", "Великобритания,United Kingdom,Großbritannien,Reino Unido,Regno Unito,Royaume-Uni,英国,영국,イギリス");
        map.put("hu", "Венгрия,Hungary,Ungarn,Hungría,Ungheria,Hongrie,匈牙利,헝가리,ハンガリー");
    }

    public static String getNationalityByLanguage(String natio, int ln) {
        return map.get(natio).split(",")[ln];
    }

}

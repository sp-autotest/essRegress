package config;

/**
 * Created by mycola on 20.02.2018.
 */
public class Values {

    public static String pnr = null;
    public static String price = null;
    public static int ln = 0;
    public static String email = "tafl@software-provider.ru";
    public static String host = "https://afl-test.test.aeroflot.ru/sb/app/ru-ru";

    public static String card[][] = {
        {"4154810047474554", "12", "2019", "314", "TEST TEST"}, //0 - Visa
    };

    public static String lang[][] = {
            {"Русский", "русский", "ru", "ddMMMMyyyy,HH:mm→"},
            {"Английский", "english", "en", "ddMMMyyyy,HH:mm→"},
            {"Немецкий", "deutsch", "de", "ddMMMyyyy,HH:mm→"},
            {"Испанский", "español", "es", "ddMMMyyyy,HH:mm→"},
            {"Итальянский", "italiano", "it", "ddMMMyyyy,HH:mm→"},
            {"Французский", "le français", "fr", "ddMMMyyyy,HH:mm→"},
            {"Китайский", "中文", "zh", "ddM月yyyy,HH:mm→"},
            {"Корейский", "한국어", "ko", "ddMMMyyyy,HH:mm→"},
            {"Японский", "日本語", "ja", "ddMMMMyyyy,HH:mm→"},
    };

    public static String text[][] = {
        {"Полетная страховка", "Flight insurance", "Flugversicherung", "Seguro de vuelo", "Assicurazione sul volo", "Assurance vol", "飞行保险", "항공 보험", "フライト保険"},
        {"Медицинская страховка", "Medical insurance", "Krankenversicherung", "Seguro médico", "Assicurazione medica", "Assurance médicale", "医疗保险", "의료 보험", "医療保険"},
        {"ТРАНСПОРТ", "TRANSPORT", "Transport", "Transporte", "Trasporto", "Transport", "交通", "운송", "輸送"},
        {"ПРОЖИВАНИЕ", "ACCOMMODATION", "UNTERKUNFT", "Alojamiento", "ALLOGGIO", "Hébergement", "住宿", "숙박", "宿泊設備"},
    };

}

package config;

/**
 * Created by mycola on 20.02.2018.
 */
public class Values {

    public static String pnr = null;
    public static String email = "tafl@software-provider.ru";
    public static String host = "https://afl-test.test.aeroflot.ru/sb/app/ru-ru";

    public static String card[][] = {
        {"4154810047474554", "12", "2019", "314", "TEST TEST"}, //0 - Visa
    };

    public static String text[][] = {
        {"Полетная страховка", "Flight insurance", "Flugversicherung", "Seguro de vuelo", "Assicurazione sul volo", "Assurance vol"},
        {"Медицинская страховка", "Medical insurance", "Krankenversicherung", "Seguro médico", "Assicurazione medica", "Assurance médicale"},
        {"ТРАНСПОРТ", "TRANSPORT", "Transport", "Transporte", "Trasporto", "Transport"},
        {"ПРОЖИВАНИЕ", "ACCOMMODATION", "UNTERKUNFT", "Alojamiento", "ALLOGGIO", "Hébergement"},

    };


}

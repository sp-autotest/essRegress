package struct;

import java.util.Calendar;
import java.util.Date;

import static config.Values.ln;
import static config.Values.lang;
import static pages.Page.getRandomString;
import static pages.Page.getRandomNumberLimit;
import static pages.Page.getRandomNumberString;


/**
 * Created by mycola on 01.03.2018.
 */
public class Passenger {
    private int gender; //пол
    private String firstname; //имя
    private String lastname; //фамилия
    private String dob; //день рождения
    private String number; //номер документа
    private String nationality; //национальность
    private String country; //страна выдачи

    public Passenger(String type) {
        this.gender = getRandomNumberLimit(2);
        this.firstname = getRandomString(4);
        this.lastname = getRandomString(8);
        this.number = getRandomNumberString(8);
        this.nationality = lang[ln][4];//пока что для всех - Россия
        this.country = lang[ln][4];//пока что для всех - Россия
        switch (type) {
            case "ADT":
                this.dob = addMonthsFromToday((-1)*getRandomFromInterval(415, 438));
                break;
            case "CHD":
                this.dob = addMonthsFromToday((-1)*getRandomFromInterval(85, 100)); //от 7 лет
                break;
            case "INF":
                this.dob = addMonthsFromToday((-1)*getRandomFromInterval(1, 10));
        }
    }

    public int getGender() {
        return gender;
    }
    public String getFirstname() {
        return firstname;
    }
    public String getLastname() {
        return lastname;
    }
    public String getDob() {
        return dob;
    }
    public String getNumber() {
        return number;
    }
    public String getNationality() {
        return nationality;
    }
    public String getCoutry() {
        return country;
    }

    private static String addMonthsFromToday(int months)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, months);
        return new java.text.SimpleDateFormat("ddMMyyyy").format(cal.getTime());
    }

    public static int getRandomFromInterval (int min, int max) {
        max -= min;
        return (int)(Math.random() * ++max) + min;
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "gender=" + gender +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", dob='" + dob + '\'' +
                ", number='" + number + '\'' +
                ", nationality='" + nationality + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}

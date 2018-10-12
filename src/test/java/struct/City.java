package struct;

/**
 * Created by mycola on 11.10.2018.
 */
public class City {
    private String code;
    private int hour;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    @Override
    public String toString() {
        return "City{" +
                "code='" + code + '\'' +
                ", hour=" + hour +
                '}';
    }

}

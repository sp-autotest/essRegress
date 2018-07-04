package struct;

/**
 * Created by mycola on 03.07.2018.
 */
public class InitialData {
    private String cityFrom;
    private String cityTo;
    private String cityMedium;
    private String dateThere;
    private String dateBack;
    private int adult;
    private int child;
    private int infant;

    public InitialData(String cityFrom, String cityTo, String cityMedium, String dateThere, String dateBack, int adult, int child, int infant) {
        this.cityFrom = cityFrom;
        this.cityTo = cityTo;
        this.cityMedium = cityMedium;
        this.dateThere = dateThere;
        this.dateBack = dateBack;
        this.adult = adult;
        this.child = child;
        this.infant = infant;
    }

    public void setCityFrom(String cityFrom) {
        this.cityFrom = cityFrom;
    }

    public void setCityTo(String cityTo) {
        this.cityTo = cityTo;
    }

    public void setCityMedium(String cityMedium) {
        this.cityMedium = cityMedium;
    }

    public void setDateThere(String dateThere) {
        this.dateThere = dateThere;
    }

    public void setDateBack(String dateBack) {
        this.dateBack = dateBack;
    }

    public void setAdult(int adult) {
        this.adult = adult;
    }

    public void setChild(int child) {
        this.child = child;
    }

    public void setInfant(int infant) {
        this.infant = infant;
    }

    public String getCityFrom() {
        return cityFrom;
    }

    public String getCityTo() {
        return cityTo;
    }

    public String getCityMedium() {
        return cityMedium;
    }

    public String getDateThere() {
        return dateThere;
    }

    public String getDateBack() {
        return dateBack;
    }

    public int getAdult() {
        return adult;
    }

    public int getChild() {
        return child;
    }

    public int getInfant() {
        return infant;
    }

    public int getTicketCount() {
        return getAdult() + getChild() + getInfant();
    }

    @Override
    public String toString() {
        return "InitialData{" +
                "cityFrom='" + cityFrom + '\'' +
                ", cityTo='" + cityTo + '\'' +
                ", cityMedium='" + cityMedium + '\'' +
                ", dateThere='" + dateThere + '\'' +
                ", dateBack='" + dateBack + '\'' +
                ", adult=" + adult +
                ", child=" + child +
                ", infant=" + infant +
                '}';
    }
}

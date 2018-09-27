package struct;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mycola on 14.09.2018.
 */
public class ReportData {

    private String pnr;
    private String docs;
    private List<String> errors;
    private Hotel hotel;
    private Auto auto;
    private Price price;

    public ReportData() {
        pnr = "";
        docs = "";
        errors = new ArrayList<>();
        hotel = new Hotel();
        auto = new Auto();
        price = new Price();
    }

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public String getDocs() {
        return docs;
    }

    public void setDocs(String docs) {
        this.docs = docs;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void addError(String error) {
        this.errors.add(error);
    }

    public void clearErrors() {
        this.errors.clear();
    }

    public Hotel getHotel() {
        return hotel;
    }

    public Auto getAuto() {
        return auto;
    }

    public Price getPrice() {
        return price;
    }

}

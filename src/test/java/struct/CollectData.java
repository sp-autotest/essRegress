package struct;

/**
 * Created by mycola on 13.09.2018.
 */
public class CollectData {

    //private String pnr;
    private String cur;
    private String phone;
    private int ln;
    private int ticket;
    private int test;

    public CollectData(){
        ticket = 1;
    }

   /* public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }
*/
    public String getCur() {
        return cur;
    }

    public void setCur(String cur) {
        this.cur = cur;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getTicket() {
        return ticket;
    }

    public int getLn() {
        return ln;
    }

    public void setLn(int ln) {
        this.ln = ln;
    }

    public void setTicket(int ticket) {
        this.ticket = ticket;
    }

    public void inkTicket() {
        ticket++;
    }

    public int getTest() {
        return test;
    }

    public void setTest(int test) {
        this.test = test;
    }
}

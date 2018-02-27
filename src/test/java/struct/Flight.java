package struct;

import java.util.Date;


/**
 * Created by mycola on 22.02.2018.
 */
public class Flight {
    public String from; //откуда
    public String to; //куда
    public String number; //номер рейса
    public String duration; //длительность перелета
    public Date start; //дата и время вылета
    public Date end; //дата и время прилета
    public long transfer;  //длительность пересадки в милисекундах
}

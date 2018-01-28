package raj.com.letsservicetask;

import java.io.Serializable;

/**
 * Created by prakash on 1/27/2018.
 */

public class LogObject implements Serializable {
    private String lat;
    private String lng;
    private String dateAndTime;



    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }
}

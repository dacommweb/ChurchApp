package com.suntechnology.churchapp.classes;

import java.text.SimpleDateFormat;

/**
 * Created by Flexy on 7/27/2017.
 */

public class Events {

    String eventId="";
    String eventTitle="";
    String eventDesc="";
    String eventDate="";

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public String getEventDate() {
        return eventDate;
    }
    public String getEventDateFormated() {
         SimpleDateFormat eventDateFormat  = new SimpleDateFormat("MMMM,dd yyyy");
       return eventDateFormat.format(Double.parseDouble(eventDate+"000"));
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }
}

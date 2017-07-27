package com.suntechnology.churchapp.classes;

/**
 * Created by Flexy on 7/27/2017.
 */

public class Devotional {
    String title="";
    String content="";
    String verse="";
    String date="";
    String hdate="";
    String prevDevotional="";
    String nextDevotional="";

    public String getPrevDevotional() {
        return prevDevotional;
    }

    public void setPrevDevotional(String prevDevotional) {
        this.prevDevotional = prevDevotional;
    }

    public String getNextDevotional() {
        return nextDevotional;
    }

    public void setNextDevotional(String nextDevotional) {
        this.nextDevotional = nextDevotional;
    }

    public String getHdate() {
        return hdate;
    }

    public void setHdate(String hdate) {
        this.hdate = hdate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVerse() {
        return verse;
    }

    public void setVerse(String verse) {
        this.verse = verse;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

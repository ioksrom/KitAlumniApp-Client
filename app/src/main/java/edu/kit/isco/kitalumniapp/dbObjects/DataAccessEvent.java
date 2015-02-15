package edu.kit.isco.kitalumniapp.dbObjects;

import java.util.Calendar;

/**
 * Created by Andre on 04.02.2015.
 */
public class DataAccessEvent implements DataAccessObject {

    private long id;
    private String title;
    private String short_info;
    private String text;
    private String url;
    private String date;

    public DataAccessEvent() {
    }

    public DataAccessEvent(long id, String title, String short_info, String text, String url, String date) {
        this.id = id;
        this.title = title;
        this.short_info = short_info;
        this.text = text;
        this.url = url;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getShort_info() {
        return short_info;
    }

    public String getText() {
        return text;
    }

    public String getUrl() {
        return url;
    }

    public String getDate() {
        return date;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setShort_info(String short_info) {
        this.short_info = short_info;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return new StringBuffer(" ID : ").append(this.getId())
                .append(" Title : ").append(this.getTitle())
                .append(" shortDescription: ").append(this.getShort_info())
                .append(" fullText : ").append(this.getText())
                .append(" url : ").append(this.getUrl())
                .toString();
    }

}
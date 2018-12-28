package cn.edu.hznu.cuckoobill;

import org.litepal.crud.DataSupport;

import java.util.Date;

public class User extends DataSupport {
    private int id;
    private String number;
    private int arriveDays;
    private int budget;
    private Date create_date;
    private int notingHour;
    private int notingMi;
    private String notingText;

    public int getNotingHour() {
        return notingHour;
    }

    public void setNotingHour(int notingHour) {
        this.notingHour = notingHour;
    }

    public int getNotingMi() {
        return notingMi;
    }

    public void setNotingMi(int notingMi) {
        this.notingMi = notingMi;
    }

    public String getNotingText() {
        return notingText;
    }

    public void setNotingText(String notingText) {
        this.notingText = notingText;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getArriveDays() {
        return arriveDays;
    }

    public void setArriveDays(int arriveDays) {
        this.arriveDays = arriveDays;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public int getId() {
        return id;
    }
}

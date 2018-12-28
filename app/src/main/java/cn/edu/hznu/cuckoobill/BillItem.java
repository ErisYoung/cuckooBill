package cn.edu.hznu.cuckoobill;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.Date;

public class BillItem extends DataSupport{
    private int id;

    private String user_id;

    private int head_id;
    private Date create_date;
    private double money;
    private boolean payment_type;
    private String money_type;

    @Column(nullable = true)
    private String note;

    public int getId() {
        return id;
    }

    public int getHead_id() {
        return head_id;
    }

    public void setHead_id(int head_id) {
        this.head_id = head_id;
    }

    public boolean isPayment_type() {
        return payment_type;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public double getMoney() {
        return money;
    }

    public String getMoney_type() {
        return money_type;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public void setMoney_type(String money_type) {
        this.money_type = money_type;
    }

    public void setPayment_type(boolean payment_type) {
        this.payment_type = payment_type;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}

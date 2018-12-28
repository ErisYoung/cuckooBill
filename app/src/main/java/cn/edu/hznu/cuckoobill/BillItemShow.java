package cn.edu.hznu.cuckoobill;

import java.util.Date;
import java.util.List;

public class BillItemShow {

    private int id;
    private int headId;
    private Date create_date;
//    private List<BillOneItem> billOneItems;
    private double money;
    private boolean payment_type;
    private String money_type;
    private String note;


    public BillItemShow( int id,int headId,Date create_date, double money, boolean payment_type, String money_type, String note) {
        this.id=id;
        this.create_date = create_date;
        this.headId = headId;
        this.money = money;
        this.payment_type = payment_type;
        this.money_type = money_type;
        this.note = note;
    }

//    public List<BillOneItem> getBillOneItems() {
//        return billOneItems;
//    }


    public int getId() {
        return id;
    }

    public int getHeadId() {
        return headId;
    }

    public Date getCreate_date() {
        return create_date;
    }
    public String getNote() {
        return note;
    }

    public String getMoney_type() {
        return money_type;
    }

    public double getMoney() {
        return money;
    }

    public boolean isPayment_type() {
        return payment_type;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public void setPayment_type(boolean payment_type) {
        this.payment_type = payment_type;
    }

    public void setMoney_type(String money_type) {
        this.money_type = money_type;
    }

    public void setMoney(double money) {
        this.money = money;
    }
//    public double getIncome_all() {
//        return income_all;
//    }
//
//    public double getSpending_all() {
//        return spending_all;
//    }

//
//
//    public void setIncome_all(double income_all) {
//        this.income_all = income_all;
//    }
//
//    public void setSpending_all(double spending_all) {
//        this.spending_all = spending_all;
//    }
}

package cn.edu.hznu.cuckoobill;

import java.util.List;

import cn.edu.hznu.cuckoobill.BillItem;

public class BillItemForChart {
    private List<BillItem> list;
    // 支出或或者 收入
    private boolean paymentType;
    private double moneyAll;
    //累计笔数
    private int itemCount;
    // 开始to 结束时间
    private int timeStartToEnd;
    // 笔数 和总金额
    private CountAndMoney moneyTypeOne;
    private CountAndMoney moneyTypeTwo;
    private CountAndMoney moneyTypethr;
    private CountAndMoney moneyTypeFor;

    private double max=0;

    public BillItemForChart(List<BillItem> list, Boolean paymentType,int timeStartToEnd) {
        this.list = list;
        this.paymentType = paymentType;
        this.timeStartToEnd = timeStartToEnd;

        this.moneyTypeOne =new CountAndMoney();
        this.moneyTypeTwo = new CountAndMoney();
        this.moneyTypethr = new CountAndMoney();
        this.moneyTypeFor = new CountAndMoney();

        setInit();
    }

    public void setInit(){
        if(!paymentType){
            this.moneyTypeOne.setMoneyType("吃喝");
            this.moneyTypeTwo.setMoneyType("购物");
            this.moneyTypethr.setMoneyType("娱乐");
            this.moneyTypeFor.setMoneyType("杂项");
            for (BillItem b:list) {
                moneyAll+=b.isPayment_type()?0:b.getMoney();
                itemCount+=b.isPayment_type()?0:1;
                if(b.getMoney()>max){
                    max=b.getMoney();
                }
                switch (b.getMoney_type()){
                    case "吃喝":
                        moneyTypeOne.increaseMoney();
                        moneyTypeOne.setMoney(moneyTypeOne.getMoney()+b.getMoney());
                        break;
                    case "购物":
                        moneyTypeTwo.increaseMoney();
                        moneyTypeTwo.setMoney(moneyTypeTwo.getMoney()+b.getMoney());
                        break;
                    case "娱乐":
                        moneyTypethr.increaseMoney();
                        moneyTypethr.setMoney(moneyTypethr.getMoney()+b.getMoney());
                        break;
                    case "杂项":
                        moneyTypeFor.increaseMoney();
                        moneyTypeFor.setMoney(moneyTypeFor.getMoney()+b.getMoney());
                        break;
                }
            }
        }else{
            this.moneyTypeOne.setMoneyType("工资");
            this.moneyTypeTwo.setMoneyType("理财");
            this.moneyTypethr.setMoneyType("红包");
            this.moneyTypeFor.setMoneyType("其他");
            for (BillItem b:list) {
                moneyAll+=b.isPayment_type()?b.getMoney():0;
                itemCount+=b.isPayment_type()?1:0;
                if(b.getMoney()>max){
                    max=b.getMoney();
                }
                switch (b.getMoney_type()){
                    case "工资":
                        moneyTypeOne.increaseMoney();
                        moneyTypeOne.setMoney(moneyTypeOne.getMoney()+b.getMoney());
                        break;
                    case "理财":
                        moneyTypeTwo.increaseMoney();
                        moneyTypeTwo.setMoney(moneyTypeTwo.getMoney()+b.getMoney());
                        break;
                    case "红包":
                        moneyTypethr.increaseMoney();
                        moneyTypethr.setMoney(moneyTypethr.getMoney()+b.getMoney());
                        break;
                    case "其他":
                        moneyTypeFor.increaseMoney();
                        moneyTypeFor.setMoney(moneyTypeFor.getMoney()+b.getMoney());
                        break;
                }
            }
        }
    }

    public double getMax() {
        return max;
    }

    public int getBillItemCount(){
        return list.size();
    }
    public List<BillItem> getList() {
        return list;
    }

    public Boolean getpaymentType() {
        return paymentType;
    }

    public double getMoneyAll() {
        return moneyAll;
    }

    public int getItemCount() {
        return itemCount;

    }

    public int getTimeStartToEnd() {
        return timeStartToEnd;
    }

    public CountAndMoney getMoneyTypeFor() {
        return moneyTypeFor;
    }

    public CountAndMoney getMoneyTypeOne() {
        return moneyTypeOne;
    }

    public CountAndMoney getMoneyTypethr() {
        return moneyTypethr;
    }

    public CountAndMoney getMoneyTypeTwo() {
        return moneyTypeTwo;
    }

    public void setList(List<BillItem> list) {
        this.list = list;
    }

    public void setpaymentType(Boolean paymentType){
        this.paymentType=paymentType;
    }

    public void setMoneyAll(double moneyAll) {
        this.moneyAll = moneyAll;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public void setTimeStartToEnd(int timeStartToEnd) {
        this.timeStartToEnd = timeStartToEnd;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public void setMoneyTypeFor(CountAndMoney moneyTypeFor) {
        this.moneyTypeFor = moneyTypeFor;
    }

    public void setMoneyTypeOne(CountAndMoney moneyTypeOne) {
        this.moneyTypeOne = moneyTypeOne;
    }

    public void setMoneyTypethr(CountAndMoney moneyTypethr) {
        this.moneyTypethr = moneyTypethr;
    }

    public void setMoneyTypeTwo(CountAndMoney moneyTypeTwo) {
        this.moneyTypeTwo = moneyTypeTwo;
    }

    public void setPaymentType(boolean paymentType) {
        this.paymentType = paymentType;
    }
}

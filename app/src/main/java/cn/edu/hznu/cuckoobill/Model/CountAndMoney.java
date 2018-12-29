package cn.edu.hznu.cuckoobill.Model;

public class CountAndMoney {
    private int count=0;
    private double money=0;
    private String moneyType;

    public void setMoneyType(String moneyType) {
        this.moneyType = moneyType;
    }

    public String getMoneyType() {
        return moneyType;
    }

    public void increaseMoney(){
        count++;
    }
    public void setMoney(double money) {
        this.money = money;
    }

    public void setCount(int count) {
        this.count = count;
    }
    public int getCount() {
        return count;
    }

    public double getMoney() {
        return money;
    }
}

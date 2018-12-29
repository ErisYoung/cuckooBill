package cn.edu.hznu.cuckoobill.Model;

import java.util.List;

// 对于某年某月的收入支出类
public class BillItemForCard {
    private int year;
    private int month;
    private double allSpendMoney;
    private double allIncomeMoney;
    private List<BillItem> eatingList;
    private List<BillItem> playingList;
    private List<BillItem> shoppingList;
    private List<BillItem> otherSpendList;
    private List<BillItem> salaryList;
    private List<BillItem> financingList;
    private List<BillItem> redMoneyList;
    private List<BillItem> otherIncomeList;

    private double eatMoney;
    private double playMoney;
    private double shopMoney;
    private double otherSpendMoney;
    private double salaryMoney;
    private double financeMoney;
    private double redMoney;
    private double otherIncomeMoney;

    public BillItemForCard(int year, int month, List<BillItem> eatingList, List<BillItem> playingList, List<BillItem> shoppingList, List<BillItem> otherSpendList, List<BillItem> salaryList, List<BillItem> financingList, List<BillItem> redMoneyList, List<BillItem> otherIncomeList) {
        this.year = year;
        this.month = month;
        this.eatingList = eatingList;
        this.playingList = playingList;
        this.shoppingList = shoppingList;
        this.otherSpendList = otherSpendList;
        this.salaryList = salaryList;
        this.financingList = financingList;
        this.redMoneyList = redMoneyList;
        this.otherIncomeList = otherIncomeList;

        for (BillItem b:eatingList) {
            eatMoney+=b.getMoney();
        }
        for (BillItem b:playingList) {
            playMoney+=b.getMoney();
        }
        for (BillItem b:shoppingList) {
            shopMoney+=b.getMoney();
        }
        for (BillItem b:otherSpendList) {
            otherSpendMoney+=b.getMoney();
        }
        for (BillItem b:salaryList) {
            salaryMoney+=b.getMoney();
        }
        for (BillItem b:financingList) {
            financeMoney+=b.getMoney();
        }
        for (BillItem b:redMoneyList) {
            redMoney+=b.getMoney();
        }
        for (BillItem b:otherIncomeList) {
            otherIncomeMoney+=b.getMoney();
        }

        allIncomeMoney=salaryMoney+financeMoney+redMoney+otherIncomeMoney;
        allSpendMoney=eatMoney+playMoney+shopMoney+otherSpendMoney;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public double getAllSpendMoney() {
        return allSpendMoney;
    }

    public double getAllIncomeMoney() {
        return allIncomeMoney;
    }

    public List<BillItem> getEatingList() {
        return eatingList;
    }

    public List<BillItem> getPlayingList() {
        return playingList;
    }

    public List<BillItem> getShoppingList() {
        return shoppingList;
    }

    public List<BillItem> getOtherSpendList() {
        return otherSpendList;
    }

    public List<BillItem> getSalaryList() {
        return salaryList;
    }

    public List<BillItem> getFinancingList() {
        return financingList;
    }

    public List<BillItem> getRedMoneyList() {
        return redMoneyList;
    }

    public List<BillItem> getOtherIncomeList() {
        return otherIncomeList;
    }

    public double getEatMoney() {
        return eatMoney;
    }

    public double getPlayMoney() {
        return playMoney;
    }

    public double getShopMoney() {
        return shopMoney;
    }

    public double getOtherSpendMoney() {
        return otherSpendMoney;
    }

    public double getSalaryMoney() {
        return salaryMoney;
    }

    public double getFinanceMoney() {
        return financeMoney;
    }

    public double getRedMoney() {
        return redMoney;
    }

    public double getOtherIncomeMoney() {
        return otherIncomeMoney;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setAllSpendMoney(double allSpendMoney) {
        this.allSpendMoney = allSpendMoney;
    }

    public void setAllIncomeMoney(double allIncomeMoney) {
        this.allIncomeMoney = allIncomeMoney;
    }

    public void setEatingList(List<BillItem> eatingList) {
        this.eatingList = eatingList;
    }

    public void setPlayingList(List<BillItem> playingList) {
        this.playingList = playingList;
    }

    public void setShoppingList(List<BillItem> shoppingList) {
        this.shoppingList = shoppingList;
    }

    public void setOtherSpendList(List<BillItem> otherSpendList) {
        this.otherSpendList = otherSpendList;
    }

    public void setSalaryList(List<BillItem> salaryList) {
        this.salaryList = salaryList;
    }

    public void setFinancingList(List<BillItem> financingList) {
        this.financingList = financingList;
    }

    public void setRedMoneyList(List<BillItem> redMoneyList) {
        this.redMoneyList = redMoneyList;
    }

    public void setOtherIncomeList(List<BillItem> otherIncomeList) {
        this.otherIncomeList = otherIncomeList;
    }
}

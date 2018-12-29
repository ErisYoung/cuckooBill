package cn.edu.hznu.cuckoobill.Helper;

import java.util.Comparator;

import cn.edu.hznu.cuckoobill.Model.CountAndMoney;

public class AllMoneyTypeComprator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        CountAndMoney t1=(CountAndMoney)o1;
        CountAndMoney t2=(CountAndMoney)o2;
        if(t1.getMoney()!=t2.getMoney()){
            return t1.getMoney()>t2.getMoney()?-1:1;
        }else {
            return t1.getMoney()>t2.getMoney()?-1:1;
        }

    }
}

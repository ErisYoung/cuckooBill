package cn.edu.hznu.cuckoobill;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private PageMain pageMain;
    private PageChart pageChart;
    private PageCard pageCard;
    private PageMine pageMine;

    private FrameLayout mainFl,chartFl,cardFl,mineFl;

    private ImageView mainIv,chartIv,cardIv,mineIv;

    private TextView mainTv,chartTv,cardTv,mineTv;

    private Button addItem;

    private FragmentTransaction fragmentTransaction;

    private SharedPreferences pref;

    public static String UserLogining="15990184787";
    public static Date LoginDate=new Date(Date.parse("10/19/2018"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        LitePal.getDatabase();
        pref=getSharedPreferences("User",MODE_PRIVATE);
        UserLogining=pref.getString("LoginUser","15990184787");

        FontHelper.injectFont(findViewById(R.id.rootView));


//        User user=new User();
//        user.setNumber(UserLogining);
//        user.setBudget(0);
//        user.setArriveDays(0);
//        user.setCreate_date(new Date());
//        user.setNotingHour(21);
//        user.setNotingMi(00);
//        user.setNotingText("松下问童子,文体两开花");
//        user.save();

//        init();
        initView();
        initClickListener();
    }

    public void init(){
        String month="07";
        String a="吃喝";
        String b="娱乐";
        String c="工资";
        String d="红包";

        BillItem billItem2=new BillItem();
        billItem2.setHead_id(Integer.parseInt("2018"+month+"01"));
        billItem2.setUser_id(UserLogining);
        billItem2.setPayment_type(false);
        billItem2.setMoney_type(a);
        billItem2.setCreate_date(new Date(Date.parse(month+"/1/2018")));
        billItem2.setMoney(200);
        billItem2.save();

        BillItem billItem3=new BillItem();
        billItem3.setHead_id(Integer.parseInt("2018"+month+"01"));
        billItem3.setUser_id(UserLogining);
        billItem3.setPayment_type(false);
        billItem3.setMoney_type(b);
        billItem3.setCreate_date(new Date(Date.parse(month+"/1/2018")));
        billItem3.setMoney(300);
        billItem3.save();

        BillItem billItem4=new BillItem();
        billItem4.setHead_id(Integer.parseInt("2018"+month+"02"));
        billItem4.setUser_id(UserLogining);
        billItem4.setPayment_type(true);
        billItem4.setMoney_type(c);
        billItem4.setCreate_date(new Date(Date.parse(month+"/2/2018")));
        billItem4.setMoney(400);
        billItem4.save();

        BillItem billItem5=new BillItem();
        billItem5.setHead_id(Integer.parseInt("2018"+month+"02"));
        billItem5.setUser_id(UserLogining);
        billItem5.setPayment_type(true);
        billItem5.setMoney_type(d);
        billItem5.setCreate_date(new Date(Date.parse(month+"/2/2018")));
        billItem5.setMoney(500);
        billItem5.save();

        BillItem billItem6=new BillItem();
        billItem6.setHead_id(Integer.parseInt("2018"+month+"03"));
        billItem6.setUser_id(UserLogining);
        billItem6.setPayment_type(true);
        billItem6.setMoney_type(c);
        billItem6.setCreate_date(new Date(Date.parse(month+"/3/2018")));
        billItem6.setMoney(600);
        billItem6.save();

        BillItem billItem7=new BillItem();
        billItem7.setHead_id(Integer.parseInt("2018"+month+"03"));
        billItem7.setUser_id(UserLogining);
        billItem7.setPayment_type(true);
        billItem7.setMoney_type(d);
        billItem7.setCreate_date(new Date(Date.parse(month+"/3/2018")));
        billItem7.setMoney(300);
        billItem7.save();

        BillItem billItem8=new BillItem();
        billItem8.setHead_id(Integer.parseInt("2018"+month+"04"));
        billItem8.setUser_id(UserLogining);
        billItem8.setPayment_type(false);
        billItem8.setMoney_type(b);
        billItem8.setCreate_date(new Date(Date.parse(month+"/4/2018")));
        billItem8.setMoney(300);
        billItem8.save();

        BillItem billItem9=new BillItem();
        billItem9.setHead_id(Integer.parseInt("2018"+month+"04"));
        billItem9.setUser_id(UserLogining);
        billItem9.setPayment_type(false);
        billItem9.setMoney_type(a);
        billItem9.setCreate_date(new Date(Date.parse(month+"/4/2018")));
        billItem9.setMoney(300);
        billItem9.save();

        BillItem billItem10=new BillItem();
        billItem10.setHead_id(Integer.parseInt("2018"+month+"04"));
        billItem10.setUser_id(UserLogining);
        billItem10.setPayment_type(true);
        billItem10.setMoney_type(c);
        billItem10.setCreate_date(new Date(Date.parse(month+"/4/2018")));
        billItem10.setMoney(300);
        billItem10.save();


    }
    public void initView(){
        mainFl=(FrameLayout)findViewById(R.id.frame_bill_main);
        chartFl=(FrameLayout)findViewById(R.id.frame_bill_chart);
        cardFl=(FrameLayout)findViewById(R.id.frame_bill_card);
        mineFl=(FrameLayout)findViewById(R.id.frame_bill_mime);

        mainIv=(ImageView)findViewById(R.id.image_bill_main);
        chartIv=(ImageView)findViewById(R.id.image_bill_chart);
        cardIv=(ImageView)findViewById(R.id.image_bill_card);
        mineIv=(ImageView)findViewById(R.id.image_bill_mine);

        mainTv=(TextView)findViewById(R.id.text_bill_main);
        chartTv=(TextView)findViewById(R.id.text_bill_chart);
        cardTv=(TextView)findViewById(R.id.text_bill_card);
        mineTv=(TextView)findViewById(R.id.text_bill_mine);

        addItem=(Button)findViewById(R.id.add_bill_item);


    }

    public  void initClickListener(){
        mainFl.setOnClickListener(this);
        chartFl.setOnClickListener(this);
        cardFl.setOnClickListener(this);
        mineFl.setOnClickListener(this);
        addItem.setOnClickListener(this);

        mainFl.performClick();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.frame_bill_main:
                pageMain=new PageMain();
                fragmentTransaction=this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_content,pageMain);
                fragmentTransaction.commit();

                mainFl.setSelected(true);
                chartFl.setSelected(false);
                cardFl.setSelected(false);
                mineFl.setSelected(false);
                break;
            case R.id.frame_bill_chart:
                pageChart=new PageChart();
                fragmentTransaction=this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_content,pageChart);
                fragmentTransaction.commit();

                mainFl.setSelected(false);
                chartFl.setSelected(true);
                cardFl.setSelected(false);
                mineFl.setSelected(false);
                break;
            case R.id.frame_bill_card:
                pageCard=new PageCard();
                fragmentTransaction=this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_content,pageCard);
                fragmentTransaction.commit();

                mainFl.setSelected(false);
                chartFl.setSelected(false);
                cardFl.setSelected(true);
                mineFl.setSelected(false);
                break;
            case R.id.frame_bill_mime:
                pageMine=new PageMine();
                fragmentTransaction=this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_content,pageMine);
                fragmentTransaction.commit();

                mainFl.setSelected(false);
                chartFl.setSelected(false);
                cardFl.setSelected(false);
                mineFl.setSelected(true);
                break;
            case R.id.add_bill_item:
                startActivity(new Intent(MainActivity.this,AddItemActivity.class));
                break;
        }

    }
    public static String getUserLogining(){
        return UserLogining;
    }
    public static Date getLoginDate(){
        return LoginDate;
    }
}

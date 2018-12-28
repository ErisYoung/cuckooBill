package cn.edu.hznu.cuckoobill;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class PageChart extends Fragment implements View.OnClickListener{
    private static final String TAG = "PageChart";

    public static final String FONTS_DIR = "fonts/";
    public static final String DEF_FONT = FONTS_DIR + "fa-solid-900.ttf";

    private Typeface tf;

    private View ChartPage;

    private RadioGroup chart_page_payment;

    private RadioGroup chart_page_date_bar;

    private TextView total_spend_inst,total_spend_number,total_list_inst;

    private TextView total_count,total_max;

    private RecyclerView listBar;

    private List<BillItem> billItems=new ArrayList<>();
    private List<BillItemForChart> billItemForChartList=new ArrayList<>();
    private List<BillItem> billItemsTemp=new ArrayList<>();
    private BillItemForChart billItemForChart;
    private String MonAndYear;

    private BillItemForChartAdapter billItemForChartAdapter;

//    private LayoutInflater inflater;

    private LinearLayout listLayout;
    private LinearLayout tempLayout;

    private TextView listMonetType,listItemCount,listMoneyPercent,listMoneyChangeBar,listPageChartListMoneyAll,listPageChartListMip;

    private LinearLayoutManager linearLayoutManager;

    //初始化 当前选中的item， 默认支出 月份类别
    private int currentPosition=0;
    private Boolean currentPayment=false;
    //  2 月 3 总
    private int currentDateType=2;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ChartPage=inflater.inflate(R.layout.page_bill_chart,container,false);
        init();
        initListener();

        linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        listBar.setLayoutManager(linearLayoutManager);
        billItemForChartAdapter=new BillItemForChartAdapter(billItemForChartList);
        listBar.setAdapter(billItemForChartAdapter);

        billItemForChartAdapter.setOnItemClickListener(new BillItemForChartAdapter.OnItemClickListen() {
            @Override
            public void onItemClick(View view, int position) {
                currentPosition=position;
                excuteForSetDateAndChange();

            }
        });

        return ChartPage;
    }
    //改变页面数据
    public void  changeListByPosition(int currentPosition){
        BillItemForChart billItemForChart=null;
        if(currentDateType==2){
            billItemForChart=billItemForChartList.get(currentPosition);
        }
        else if(currentDateType==3){
            billItemForChart=billItemForChartList.get(0);
        }
        String month=String.valueOf(billItemForChart.getTimeStartToEnd()).substring(4,6);
        String payment=billItemForChart.getpaymentType()?"收入":"支出";
        setInfor(billItemForChart,month,payment);
        // 插入 排行榜
        listLayout.removeAllViews();

        CountAndMoney[] arr=new CountAndMoney[4];
        arr[0]=billItemForChart.getMoneyTypeOne();
        arr[1]=billItemForChart.getMoneyTypeTwo();
        arr[2]=billItemForChart.getMoneyTypethr();
        arr[3]=billItemForChart.getMoneyTypeFor();
        Arrays.sort(arr,new AllMoneyTypeComprator());

        LayoutInflater inflater=LayoutInflater.from(getActivity());

        for (int i = 0; i <4; i++) {
            if(arr[i].getMoney()==0){
                continue;
            }
            tempLayout=(LinearLayout)inflater.inflate(R.layout.page_chart_list_tem,listLayout,false);

            //动态设置每列样式
            listPageChartListMip=(TextView)tempLayout.findViewById(R.id.page_chart_list_mip);
            listMonetType=(TextView)tempLayout.findViewById(R.id.moneyType);
            listItemCount=(TextView)tempLayout.findViewById(R.id.itemCount);
            listMoneyPercent=(TextView)tempLayout.findViewById(R.id.moneyPercent);
            listMoneyChangeBar=(TextView)tempLayout.findViewById(R.id.moneyChangeBar);
            listPageChartListMoneyAll=(TextView)tempLayout.findViewById(R.id.page_chart_list_moneyAll);

            listPageChartListMip.setTypeface(tf);
            listPageChartListMip.setText(fromMoneyTypeToMip(arr[i].getMoneyType()));
            listMonetType.setText(arr[i].getMoneyType());
            listItemCount.setText(arr[i].getCount()+"笔");
            listMoneyPercent.setText(String.format("%.2f%%",arr[i].getMoney()/billItemForChart.getMoneyAll()*100));
            listPageChartListMoneyAll.setText(arr[i].getMoney()+"");
            //设置进度条相对宽度
            LinearLayout.LayoutParams lp=(LinearLayout.LayoutParams)listMoneyChangeBar.getLayoutParams();
            lp.width=(int)(arr[i].getMoney()/billItemForChart.getMoneyAll()*300);
            listMoneyChangeBar.setLayoutParams(lp);
            listLayout.addView(tempLayout,-1);
        }
    }

    public String fromMoneyTypeToMip(String a){
        String mip=null;
        if(!currentPayment){
            switch (a){
                case "吃喝":
                    mip=getActivity().getString(R.string.eating_mip);
                    break;
                case "娱乐":
                    mip=getActivity().getString(R.string.play_mip);
                    break;
                case "购物":
                    mip=getActivity().getString(R.string.shop_mip);
                    break;
                case "杂项":
                    mip=getActivity().getString(R.string.otherSpend_mip);
                    break;
                default:
                    break;
            }
        }else {
            switch (a){
                case "工资":
                    mip=getActivity().getString(R.string.salary_mip);
                    break;
                case "红包":
                    mip=getActivity().getString(R.string.redMoney_mip);
                    break;
                case "理财":
                    mip=getActivity().getString(R.string.finance_mip);
                    break;
                case "其他":
                    mip=getActivity().getString(R.string.otherIncome_mip);
                    break;
                default:
                    break;
            }
        }
        return mip;
    }

    //设置上方总体收支
    public void setInfor(BillItemForChart billItemForChart,String month,String payment){
        SpannableString spannableString = new SpannableString(billItemForChart.getMax()+"元");
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, spannableString.length()-1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        total_max.setText(spannableString);
        spannableString=new SpannableString(billItemForChart.getMoneyAll()+"元");
        spannableString.setSpan(new RelativeSizeSpan(0.4f), spannableString.length()-1, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, spannableString.length()-1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        total_spend_number.setText(spannableString);
        spannableString=new SpannableString(billItemForChart.getItemCount()+"笔");
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, spannableString.length()-1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        total_count.setText(spannableString);
        if(currentDateType==2){
            total_spend_inst.setText(month+"月, "+payment+"总额");
        }
        else if(currentDateType==3){
            total_spend_inst.setText("全部, "+payment+"总额");
        }

    }
    public void  init(){
        chart_page_payment=(RadioGroup)ChartPage.findViewById(R.id.chart_page_payment);
        chart_page_date_bar=(RadioGroup)ChartPage.findViewById(R.id.chart_page_date_bar);
        total_spend_inst=(TextView)ChartPage.findViewById(R.id.total_spend_inst);
        total_spend_number=(TextView)ChartPage.findViewById(R.id.total_spend_number);
        total_list_inst=(TextView)ChartPage.findViewById(R.id.total_list_inst);
        total_count=(TextView)ChartPage.findViewById(R.id.total_count);
        total_max=(TextView)ChartPage.findViewById(R.id.total_max);
        listBar=(RecyclerView)ChartPage.findViewById(R.id.listBar);
        listLayout=(LinearLayout)ChartPage.findViewById(R.id.chart_page_list);

        chart_page_payment.check(R.id.spending);
        chart_page_date_bar.check(R.id.chart_date_month);

        tf=Typeface.createFromAsset(getActivity().getAssets(),DEF_FONT);

        FontHelper.injectFont(ChartPage.findViewById(R.id.rootView));

        defaultPageChartList();
        changeListByPosition(currentPosition);

    }
    // 月份类型数据初始化
    public void defaultPageChartList(){
        total_list_inst.setVisibility(View.VISIBLE);
        listBar.setVisibility(View.VISIBLE);
        billItems.clear();
        billItemsTemp.clear();
        billItemForChartList.clear();
        billItems=DataSupport.select().where("user_id = ?",MainActivity.getUserLogining()).order("create_date").find(BillItem.class);
        BillItem b=billItems.get(billItems.size()-1);
        Date newDate=b.getCreate_date();
        int months= (int) ((newDate.getTime()-MainActivity.getLoginDate().getTime())/(1000*3600*24)/30);
        MonAndYear=parseHeadIdToMonandYear(b.getHead_id());

        billItemsTemp.add(b);
        for (int i = billItems.size()-2; i >=0 ; i--) {
            b=billItems.get(i);
            if(parseHeadIdToMonandYear(b.getHead_id()).equals(MonAndYear)){
                billItemsTemp.add(b);
                if(i==0){
                    billItemForChart=new BillItemForChart(billItemsTemp,currentPayment,Integer.parseInt(MonAndYear));
                    billItemForChartList.add(billItemForChart);
                }
            }
            else {
                billItemForChart=new BillItemForChart(billItemsTemp,currentPayment,Integer.parseInt(MonAndYear));
                billItemForChartList.add(billItemForChart);
                billItemsTemp.clear();
                MonAndYear=parseHeadIdToMonandYear(b.getHead_id());
            }
        }
    }
    // 全部数据初始化
    public void allPageChartList(){
        total_list_inst.setVisibility(View.GONE);
        listBar.setVisibility(View.GONE);
        billItems.clear();
        billItemsTemp.clear();
        billItemForChartList.clear();
        billItems=DataSupport.select().where("user_id = ?",MainActivity.getUserLogining()).order("create_date").find(BillItem.class);
        BillItem b=null;
        for (int i = billItems.size()-1; i >=0 ; i--) {
            b=billItems.get(i);
            billItemsTemp.add(b);
            }
        BillItemForChart billItemForChart=new BillItemForChart(billItemsTemp,currentPayment,Integer.parseInt(MonAndYear));
        billItemForChartList.add(billItemForChart);
    }
    public String  parseHeadIdToMonandYear(int headId){
        return String.valueOf(headId).substring(0,6);
    }
    // 执行数据的初始化 和 页面数据的改变
    public void excuteForSetDateAndChange(){
        if(currentDateType==2){
            defaultPageChartList();
            changeListByPosition(currentPosition);

        }else if(currentDateType==3){
            allPageChartList();
            changeListByPosition(currentPosition);
        }
        billItemForChartAdapter.notifyDataSetChanged();
    }
    public void initListener(){

        chart_page_payment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.spending){
                  currentPayment=false;
                excuteForSetDateAndChange();

                }
                else if(checkedId==R.id.incoming){
                    currentPayment=true;
                    excuteForSetDateAndChange();
                }
            }
        });

        chart_page_date_bar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.chart_date_month){
                    currentDateType=2;
                    defaultPageChartList();
                    changeListByPosition(currentPosition);
                    billItemForChartAdapter.notifyDataSetChanged();
                }else if(checkedId==R.id.chart_date_all){
                    currentDateType=3;
                    allPageChartList();
                    changeListByPosition(currentPosition);
                    billItemForChartAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public  void  updatePage(){

    }
    @Override
    public void onClick(View v) {
    }
}

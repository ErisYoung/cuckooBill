package cn.edu.hznu.cuckoobill;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;


public class BillItemAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    public static final String FONTS_DIR = "fonts/";
    public static final String DEF_FONT = FONTS_DIR + "fa-solid-900.ttf";

    private static final String TAG = "BillItemAdapter";
    private String MonAndDay;
    private String Day;
    private String MoneyOneItemShow;

    private double spendingAll;
    private double incomeAll;

    private Context context;

    private List<BillItemShow> list;
    private List<Integer> listHead=new ArrayList<>();
    private List<Integer> listHeadIncome=new ArrayList<>();
    private List<Integer> listHeadSpend=new ArrayList<>();

    private LayoutInflater inflater;

    private Typeface tf;

    public BillItemAdapter(@NonNull Context context,List<BillItemShow> list) {
        inflater=LayoutInflater.from(context);
        this.list=list;
        this.context=context;
        initHeadIndex();

        //手动渲染 list 里item 的 图标
        tf=Typeface.createFromAsset(context.getAssets(),DEF_FONT);
    }

    public void initHeadIndex(){
        int lastHeader=list.get(0).getHeadId();
        listHead.add(lastHeader);
        for (int i = 0; i <list.size(); i++) {
            int nowHead=list.get(i).getHeadId();
            if(!(lastHeader==nowHead)){
                listHead.add(nowHead);
                lastHeader=nowHead;
            }
        }
        BillItemShow item=null;
        int income=0;
        int spend=0;
        for (int i = 0; i <listHead.size(); i++) {
            income=0;
            spend=0;
            for (int j = 0; j <list.size(); j++) {
                item=list.get(j);
                if(item.getHeadId()==listHead.get(i)){
                    if(item.isPayment_type()){
                        income+=item.getMoney();
                    }
                    else {
                        spend+=item.getMoney();
                    }
                }
            }
            listHeadIncome.add(income);
            listHeadSpend.add(spend);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getHeaderView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        HeaderViewHolder holder;
        if(convertView==null){
            holder=new HeaderViewHolder();
            view= inflater.inflate(R.layout.bill_item_title,parent,false);
            holder.billOdayBarDate=(TextView)view.findViewById(R.id.total_bill_oday_bar_date);
            holder.billOdayBarDay=(TextView)view.findViewById(R.id.total_bill_oday_bar_day);
            holder.billOdayBarIncomeAll=(TextView)view.findViewById(R.id.total_bill_oday_bar_income);
            holder.billOdayBarSpendAll=(TextView)view.findViewById(R.id.total_bill_oday_bar_spending);
            view.setTag(holder);
        }
        else {
            view=convertView;
            holder=(HeaderViewHolder) view.getTag();
        }
        parseDateToMonAndDay(list.get(position).getCreate_date());
        holder.billOdayBarDate.setText(MonAndDay);
        holder.billOdayBarDay.setText(Day);

//        Log.d(TAG, "getHeaderView: "+getHeaderId(position));
        int flag=0;
        for (int i = 0; i <listHead.size(); i++) {
            if(listHead.get(i)==getHeaderId(position)){
                flag=i;
            }
        }

        holder.billOdayBarSpendAll.setText("支出:"+listHeadSpend.get(flag));
        holder.billOdayBarIncomeAll.setText("收入:"+listHeadIncome.get(flag));

        return view;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.bill_item_content,parent,false);
            viewHolder.billOitemMoneyType=(TextView)convertView.findViewById(R.id.bill_oitem_money_type);
            viewHolder.billOitemMoneyCount=(TextView)convertView.findViewById(R.id.bill_oitem_money_count);
            viewHolder.getBillOitemMoneyNote=(TextView)convertView.findViewById(R.id.bill_oitem_money_note);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder=(ViewHolder)convertView.getTag();
        }
        BillItemShow billItemChoice=list.get(position);
        MoneyOneItemShow=billItemChoice.isPayment_type()?"+"+billItemChoice.getMoney():"-"+billItemChoice.getMoney();

        // 渲染字体font
        viewHolder.billOitemMoneyType.setTypeface(tf);
        viewHolder.billOitemMoneyCount.setTypeface(tf);
        viewHolder.getBillOitemMoneyNote.setTypeface(tf);

        String moneyType=list.get(position).getMoney_type();

        if(!billItemChoice.isPayment_type()){
            switch (moneyType){
                case "吃喝":
                    viewHolder.billOitemMoneyType.setText(context.getString(R.string.eating));
                    break;
                case "娱乐":
                    viewHolder.billOitemMoneyType.setText(context.getString(R.string.play));
                    break;
                case "购物":
                    viewHolder.billOitemMoneyType.setText(context.getString(R.string.shop));
                    break;
                case "杂项":
                    viewHolder.billOitemMoneyType.setText(context.getString(R.string.otherSpend));
                    break;
                default:
                    break;
            }
        }else {
            switch (moneyType){
                case "工资":
                    viewHolder.billOitemMoneyType.setText(context.getString(R.string.salary));
                    break;
                case "红包":
                    viewHolder.billOitemMoneyType.setText(context.getString(R.string.redMoney));
                    break;
                case "理财":
                    viewHolder.billOitemMoneyType.setText(context.getString(R.string.finance));
                    break;
                case "其他":
                    viewHolder.billOitemMoneyType.setText(context.getString(R.string.otherIncome));
                    break;
                default:
                    break;
            }
        }

        viewHolder.billOitemMoneyCount.setText(MoneyOneItemShow);
        viewHolder.getBillOitemMoneyNote.setText(billItemChoice.getNote());

        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        int headId=list.get(position).getHeadId();
        return headId;
    }

    class ViewHolder{
        TextView billOitemMoneyType;
        TextView billOitemMoneyCount;
        TextView getBillOitemMoneyNote;
    }

    class HeaderViewHolder{
        TextView billOdayBarDate;
        TextView billOdayBarDay;
        TextView billOdayBarSpendAll;
        TextView billOdayBarIncomeAll;
    }

    public void parseDateToMonAndDay(Date date){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        int month=calendar.get(Calendar.MONTH)+1;
        String monStr=month<10?"0"+month:month+"";
        int day=calendar.get(Calendar.DATE);
        String dayStr=day<10?"0"+day:day+"";
        MonAndDay=monStr+"月"+dayStr+"日";

        int week=calendar.get(Calendar.DAY_OF_WEEK);
        switch (week) {
            case 1:
                Day = "周日";
                break;
            case 2:
                Day = "周一";
                break;
            case 3:
                Day = "周二";
                break;
            case 4:
                Day = "周三";
                break;
            case 5:
                Day = "周四";
                break;
            case 6:
                Day = "周五";
                break;
            case 7:
                Day = "周六";
                break;
            default:
                break;
        }
        
    }
}

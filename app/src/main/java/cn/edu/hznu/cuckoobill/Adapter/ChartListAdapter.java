package cn.edu.hznu.cuckoobill.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.edu.hznu.cuckoobill.Model.CountAndMoney;
import cn.edu.hznu.cuckoobill.R;

public class ChartListAdapter extends RecyclerView.Adapter<ChartListAdapter.ViewHolder>{
    private List<CountAndMoney> list;
    private double moneyAll;
    private Context context;
    private boolean currentPayment;

    public static final String FONTS_DIR = "fonts/";
    public static final String DEF_FONT = FONTS_DIR + "fa-solid-900.ttf";

    private Typeface tf;

    static  class ViewHolder extends RecyclerView.ViewHolder{
        TextView listPageChartListMip,listMonetType,listItemCount,listMoneyPercent,listMoneyChangeBar,listPageChartListMoneyAll;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            listPageChartListMip=(TextView)itemView.findViewById(R.id.page_chart_list_mip);
            listMonetType=(TextView)itemView.findViewById(R.id.moneyType);
            listItemCount=(TextView)itemView.findViewById(R.id.itemCount);
            listMoneyPercent=(TextView)itemView.findViewById(R.id.moneyPercent);
            listMoneyChangeBar=(TextView)itemView.findViewById(R.id.moneyChangeBar);
            listPageChartListMoneyAll=(TextView)itemView.findViewById(R.id.page_chart_list_moneyAll);

        }
    }

    public ChartListAdapter(Context context, List<CountAndMoney> list, double moneyAll,boolean currentPayment) {
        this.list = list;
        this.moneyAll=moneyAll;
        this.context=context;
        this.currentPayment=currentPayment;
        tf=Typeface.createFromAsset(context.getAssets(),DEF_FONT);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.page_chart_list_tem,viewGroup,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        CountAndMoney countAndMoney=list.get(i);
        viewHolder.listItemCount.setText(countAndMoney.getCount()+"笔");
        viewHolder.listMonetType.setText(countAndMoney.getMoneyType());
        viewHolder.listMoneyPercent.setText(String.format("%.2f%%",countAndMoney.getMoney()/moneyAll*100));
        viewHolder.listPageChartListMoneyAll.setText(countAndMoney.getMoney()+"");
        viewHolder.listPageChartListMip.setTypeface(tf);
        viewHolder.listPageChartListMip.setText(fromMoneyTypeToMip(countAndMoney.getMoneyType()));

        LinearLayout.LayoutParams lp=(LinearLayout.LayoutParams)viewHolder.listMoneyChangeBar.getLayoutParams();
        lp.width=(int)(countAndMoney.getMoney()/moneyAll*300);
        viewHolder.listMoneyChangeBar.setLayoutParams(lp);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public String fromMoneyTypeToMip(String a){
        String mip=null;
        if(!currentPayment){
            switch (a){
                case "吃喝":
                    mip=context.getString(R.string.eating_mip);
                    break;
                case "娱乐":
                    mip=context.getString(R.string.play_mip);
                    break;
                case "购物":
                    mip=context.getString(R.string.shop_mip);
                    break;
                case "杂项":
                    mip=context.getString(R.string.otherSpend_mip);
                    break;
                default:
                    break;
            }
        }else {
            switch (a){
                case "工资":
                    mip=context.getString(R.string.salary_mip);
                    break;
                case "红包":
                    mip=context.getString(R.string.redMoney_mip);
                    break;
                case "理财":
                    mip=context.getString(R.string.finance_mip);
                    break;
                case "其他":
                    mip=context.getString(R.string.otherIncome_mip);
                    break;
                default:
                    break;
            }
        }
        return mip;
    }

}

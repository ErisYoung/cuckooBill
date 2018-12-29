package cn.edu.hznu.cuckoobill.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.edu.hznu.cuckoobill.Model.BillItem;
import cn.edu.hznu.cuckoobill.R;

public class BillItemForCardAdapter extends RecyclerView.Adapter<BillItemForCardAdapter.ViewHolder> {
    private List<BillItem> billItemList;
    private Context context;
    public static final String FONTS_DIR = "fonts/";
    public static final String DEF_FONT = FONTS_DIR + "fa-solid-900.ttf";

    private Typeface tf;

    static class ViewHolder extends  RecyclerView.ViewHolder{
        View parentView;
        TextView moneyType;
        TextView moneyCount;
        TextView moneyNote;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parentView=itemView;
            moneyType=(TextView)itemView.findViewById(R.id.bill_oitem_money_type);
            moneyCount=(TextView)itemView.findViewById(R.id.bill_oitem_money_count);
            moneyNote=(TextView)itemView.findViewById(R.id.bill_oitem_money_note);

        }
    }

    public BillItemForCardAdapter(Context context,List<BillItem> billItemList) {
        this.billItemList = billItemList;
        this.context=context;
        //手动渲染 list 里item 的 图标
        tf=Typeface.createFromAsset(context.getAssets(),DEF_FONT);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bill_item_content,viewGroup,false);
        ViewHolder holder=new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        BillItem b=billItemList.get(i);


        viewHolder.moneyCount.setText((b.isPayment_type()?"+":"-")+b.getMoney());
        viewHolder.moneyNote.setText(b.getNote());
//        viewHolder.moneyType.setText(b.getMoney_type());

        viewHolder.moneyType.setTypeface(tf);

        String moneyType=b.getMoney_type();
        if(!b.isPayment_type()){
            switch (moneyType){
                case "吃喝":
                    viewHolder.moneyType.setText(context.getString(R.string.eating));
                    break;
                case "娱乐":
                    viewHolder.moneyType.setText(context.getString(R.string.play));
                    break;
                case "购物":
                    viewHolder.moneyType.setText(context.getString(R.string.shop));
                    break;
                case "杂项":
                    viewHolder.moneyType.setText(context.getString(R.string.otherSpend));
                    break;
                default:
                    break;
            }
        }else {
            switch (moneyType){
                case "工资":
                    viewHolder.moneyType.setText(context.getString(R.string.salary));
                    break;
                case "红包":
                    viewHolder.moneyType.setText(context.getString(R.string.redMoney));
                    break;
                case "理财":
                    viewHolder.moneyType.setText(context.getString(R.string.finance));
                    break;
                case "其他":
                    viewHolder.moneyType.setText(context.getString(R.string.otherIncome));
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return billItemList.size();
    }
}

package cn.edu.hznu.cuckoobill.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hznu.cuckoobill.Model.BillItemForChart;
import cn.edu.hznu.cuckoobill.R;

public class BillItemForChartAdapter extends RecyclerView.Adapter<BillItemForChartAdapter.ViewHolder> {
    private static final String TAG = "BillItemForChartAdapter";
    private List<BillItemForChart> billItemForChartList;
    private List<Boolean> isChecked;
    private BillItemForChartAdapter.OnItemClickListen onItemClickListener;
    private int count=1;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View parentView;
        TextView chart_bar_black;
        TextView chart_bar_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parentView=itemView;
            chart_bar_black=(TextView)itemView.findViewById(R.id.chart_bar_black);
            chart_bar_date=(TextView)itemView.findViewById(R.id.chart_bar_date);

        }
    }

    public BillItemForChartAdapter(List<BillItemForChart> billItemForCharts){
        billItemForChartList=billItemForCharts;
        isChecked=new ArrayList<>();
        for (int i = 0; i <billItemForChartList.size(); i++) {
            if(i==0){
                isChecked.add(true);
            }
            isChecked.add(false);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.page_chart_bar_tem,viewGroup,false);
        final ViewHolder holder=new ViewHolder(view);



        holder.parentView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int position=holder.getAdapterPosition();
                onItemClickListener.onItemClick(v,position);

                isChecked.set(position,true);
                for(int i = 0; i <isChecked.size();i++){
                    isChecked.set(i,false);
                }
                isChecked.set(position,true);
                notifyDataSetChanged();

            }
        });
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull BillItemForChartAdapter.ViewHolder viewHolder, int i) {
        if(i==0&&count==1){
            viewHolder.chart_bar_black.setBackgroundResource(R.color.pink);
            count--;
        }

        BillItemForChart billItemForChart=billItemForChartList.get(i);
        String time=String.valueOf(billItemForChart.getTimeStartToEnd());
        viewHolder.chart_bar_date.setText(time.substring(0,4)+"年"+time.substring(4,6)+"月");
        RelativeLayout.LayoutParams lp=(RelativeLayout.LayoutParams)viewHolder.chart_bar_black.getLayoutParams();
        lp.height=(int)(billItemForChart.getMoneyAll()/50);
        viewHolder.chart_bar_black.setLayoutParams(lp);
        if(isChecked.get(i)){
            viewHolder.chart_bar_black.setBackgroundResource(R.color.pink);
        }
        else {
            viewHolder.chart_bar_black.setBackgroundResource(R.color.littlepink);
        }

    }
    public interface OnItemClickListen {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(BillItemForChartAdapter.OnItemClickListen listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return billItemForChartList.size();
    }
}

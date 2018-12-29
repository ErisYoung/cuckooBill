package cn.edu.hznu.cuckoobill.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hznu.cuckoobill.Adapter.BillItemAdapter;
import cn.edu.hznu.cuckoobill.Activities.ChangeItem;
import cn.edu.hznu.cuckoobill.Helper.FontHelper;
import cn.edu.hznu.cuckoobill.Activities.MainActivity;
import cn.edu.hznu.cuckoobill.Model.BillItem;
import cn.edu.hznu.cuckoobill.Model.BillItemShow;
import cn.edu.hznu.cuckoobill.R;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import static android.content.Context.MODE_PRIVATE;

public class PageMain extends Fragment {
    private static final String TAG = "PageMain";
    private List<BillItem> billItems = new ArrayList<>();
    private List<BillItemShow> BillList = new ArrayList<>();
    private List<BillItem> billItemForCalMonthBalance = new ArrayList<>();

    private View stickItem;
    private View MainFragment;

    private BillItemAdapter adapter;

    private TextView balance, balance_month, spend, spend_month, income, income_month;
    private TextView scrollDate;

    private boolean isScroll = false;

    private String lastCurrentDateEndMonth = "19900701";
    private String currentMonth;

    //Clicked Item Infor
    private String clickedItemNote;
    private String clickedItemDate;
    private String clickedItemId;
    private String moneyType;
    private String moneyCount;

    private SharedPreferences pref;

    private StickyListHeadersListView stickyList;

    private boolean flag = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainFragment = inflater.inflate(R.layout.page_bill_main, container, false);
        FontHelper.injectFont(MainFragment.findViewById(R.id.rootView));
        stickyList = (StickyListHeadersListView) MainFragment.findViewById(R.id.stickyListView);

        billItems = DataSupport.select().where("user_id = ?", MainActivity.getUserLogining()).order("create_date").find(BillItem.class);

        init();
        adapter = new BillItemAdapter(getActivity(), BillList);
        stickyList.setAdapter(adapter);
        initListener();

        return MainFragment;
    }

    public void initListener() {
        stickyList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                stickItem=view.getChildAt(firstVisibleItem);
//                scrollDate=(TextView) stickItem.findViewById(R.id.save_date);
                BillItemShow b = (BillItemShow) adapter.getItem(firstVisibleItem);
                if (b != null) {
                    String currentDateEndMonth = String.valueOf(b.getHeadId()).substring(0, 6);
                    if (!currentDateEndMonth.equals(lastCurrentDateEndMonth)) {
                        lastCurrentDateEndMonth = currentDateEndMonth;
                        calculateMonthBalance(currentDateEndMonth);
                    }
                }
            }
        });

        stickyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView temp = view.findViewById(R.id.bill_oitem_money_count);
                moneyCount = temp.getText().toString();
                temp = view.findViewById(R.id.bill_oitem_money_type);
                moneyType = temp.getText().toString();
                BillItemShow b = (BillItemShow) adapter.getItem(position);


                Intent intent = new Intent(getActivity(), ChangeItem.class);
                intent.putExtra("moneyCount", moneyCount);
                intent.putExtra("moneyType", moneyType);
                intent.putExtra("clickedItemId", String.valueOf(b.getId()));
                intent.putExtra("clickedItemDate", String.valueOf(b.getHeadId()));

//                Log.d(TAG, "onItemClick: "+b.getHeadId());
                intent.putExtra("clickedItemNote", b.getNote());
                startActivity(intent);


            }
        });
    }

    //查找当月的item ，计算支出收入和结余
    public void calculateMonthBalance(String currentDateEndMonth) {
        double spendMonth = 0, incomeMonth = 0;
        currentMonth = currentDateEndMonth.substring(4, 6);
//        currentMonth=scrollDate.getText().toString().substring(4,6);
        billItemForCalMonthBalance = DataSupport.where("head_id like ? and user_id = ?", currentDateEndMonth + "%", MainActivity.getUserLogining()).order("create_date").find(BillItem.class);
        for (BillItem b : billItemForCalMonthBalance) {
            if (b.isPayment_type()) {
                incomeMonth += b.getMoney();
            } else {
                spendMonth += b.getMoney();
            }
        }
        changeTitle(incomeMonth, spendMonth);
    }

    //替换显示 支出 收入 结余
    public void changeTitle(double incomeMonth, double spendMonth) {

        //加入预算功能判断
        pref = getActivity().getSharedPreferences("User", MODE_PRIVATE);
        Boolean isBudget = pref.getBoolean("isBudget", false);
        if (isBudget) {
            Float currentCount = pref.getFloat("budgetCount", 0);
//            Log.d(TAG, "changeTitle:---- "+currentMonth);

            balance_month.setText(currentMonth + "月预算结余");
            balance.setText(String.format("%.2f", incomeMonth - spendMonth + currentCount));
        } else {
            balance_month.setText(currentMonth + "月结余");
            balance.setText(String.format("%.2f", incomeMonth - spendMonth));
        }
        spend_month.setText(currentMonth + "月支出");
        income_month.setText(currentMonth + "月收入");
        spend.setText(String.format("-%.2f", spendMonth));
        income.setText(String.format("+%.2f", incomeMonth));
    }

    public void init() {
        //查询item
        queryAll();

        balance = (TextView) MainFragment.findViewById(R.id.balance);
        balance_month = (TextView) MainFragment.findViewById(R.id.balance_month);
        spend = (TextView) MainFragment.findViewById(R.id.spend);
        spend_month = (TextView) MainFragment.findViewById(R.id.spend_month);
        income = (TextView) MainFragment.findViewById(R.id.income);
        income_month = (TextView) MainFragment.findViewById(R.id.income_month);


    }

    public void queryAll() {
        BillList.clear();
        BillItemShow billItemShow;
//        billItems=DataSupport.findAll(BillItem.class);
        billItems = DataSupport.select().where("user_id = ?", MainActivity.getUserLogining()).order("create_date").find(BillItem.class);
        for (int i = billItems.size() - 1; i >= 0; i--) {
            BillItem b = billItems.get(i);
            billItemShow = new BillItemShow(b.getId(), b.getHead_id(), b.getCreate_date(), b.getMoney(), b.isPayment_type(), b.getMoney_type(), b.getNote());
            BillList.add(billItemShow);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        queryAll();
        adapter.notifyDataSetChanged();
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("User", MODE_PRIVATE).edit();
        editor.putBoolean("isDelete", false);
        editor.apply();


    }
}

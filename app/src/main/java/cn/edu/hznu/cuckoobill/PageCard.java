package cn.edu.hznu.cuckoobill;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

public class PageCard extends Fragment implements View.OnClickListener {
    private static final String TAG = "PageCard";
    private View CardPage;
    private TextView page_card_current_payment,page_card_listName;
    private TextView page_card_current_time;
    private PieChart mPieChart;
    private RecyclerView page_card_listView;
    private FancyButton page_card_selector, page_card_change_payment;

    private OptionsPickerView pvOptions;
    private ArrayList<ProvinceBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();

    private List<BillItem> billItemList = new ArrayList<>();

    private String currentSelectorTime;

    //构建一个当月的类实例，包含各类信息
    private BillItemForCard billItemForCard;
    private String currentYear, currentMonth;
    private Boolean currentePayment;
    private String currentMoneyType;

    private PieDataSet pieDataSet;
    private PieData pieData;

    private LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
    private BillItemForCardAdapter billItemForCardAdapter;
    private List<BillItem> recycleViewList=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CardPage = inflater.inflate(R.layout.page_bill_card, container, false);
        init();
        initListener();

        return CardPage;
    }

    public void init() {
        page_card_selector = (FancyButton) CardPage.findViewById(R.id.page_card_selector);
        page_card_change_payment = (FancyButton) CardPage.findViewById(R.id.page_card_change_payment);
        mPieChart = (PieChart) CardPage.findViewById(R.id.mPieChart);
        page_card_listView = (RecyclerView) CardPage.findViewById(R.id.page_card_listView);
        page_card_current_time = (TextView) CardPage.findViewById(R.id.page_card_current_time);

        page_card_current_payment = (TextView) CardPage.findViewById(R.id.page_card_current_payment);
        page_card_listName=(TextView)CardPage.findViewById(R.id.page_card_listName);

        //字体图标渲染
        FontHelper.injectFont(CardPage.findViewById(R.id.rootView));

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        initSetCurrentDate();

        initSelectorDate();

    }

    public void initSetCurrentDate() {
        billItemList.clear();
        billItemList = DataSupport.where("user_id = ?",MainActivity.getUserLogining()).order("create_date").find(BillItem.class);
        String headId = String.valueOf(billItemList.get(0).getHead_id());
        String year = headId.substring(0, 4);
        String month = headId.substring(4, 6);


        page_card_current_time.setText(year + "年" + month + "月");
        // 改变默认值
        currentePayment = billItemList.get(0).isPayment_type();
        currentMonth=month;
        currentYear=year;

        page_card_current_payment.setText(currentePayment ? "收入" : "支出");
        page_card_listName.setText((currentePayment ? "收入" : "支出")+"排行榜");


        queryPieDate(headId);


    }

    public void setShowInfor() {
        page_card_current_time.setText(currentYear + "年" + currentMonth + "月");
        page_card_current_payment.setText(currentePayment ? "收入" : "支出");
        page_card_listName.setText((currentePayment ? "收入" : "支出")+"排行榜");

    }

    public void queryPieDate(String headId) {
        billItemList.clear();
        billItemList = DataSupport.where("head_id like ? and user_id = ?", headId + "%",MainActivity.getUserLogining()).order("create_date").find(BillItem.class);
        List<BillItem> eatingList = new ArrayList<>();
        List<BillItem> playingList = new ArrayList<>();
        List<BillItem> shoppingList = new ArrayList<>();
        List<BillItem> otherSpendList = new ArrayList<>();
        List<BillItem> salaryList = new ArrayList<>();
        List<BillItem> financingList = new ArrayList<>();
        List<BillItem> redMoneyList = new ArrayList<>();
        List<BillItem> otherIncomeList = new ArrayList<>();
        for (int i = 0; i < billItemList.size(); i++) {
            BillItem b = billItemList.get(i);
            switch (b.getMoney_type()) {
                case "吃喝":
                    eatingList.add(b);
                    break;
                case "娱乐":
                    playingList.add(b);
                    break;
                case "购物":
                    shoppingList.add(b);
                    break;
                case "杂项":
                    otherSpendList.add(b);
                    break;
                case "工资":
                    salaryList.add(b);
                    break;
                case "理财":
                    financingList.add(b);
                    break;
                case "红包":
                    redMoneyList.add(b);
                    break;
                case "其他":
                    otherIncomeList.add(b);
                    break;
                default:
                    break;

            }
        }
        billItemForCard = null;
        billItemForCard = new BillItemForCard(Integer.parseInt(headId.substring(0,4)), Integer.parseInt(headId.substring(4,6)), eatingList, playingList, shoppingList, otherSpendList, salaryList, financingList, redMoneyList, otherIncomeList);

        setPieChart(billItemForCard);

    }

    public void setPieChart(BillItemForCard billItemForCard) {
        mPieChart.clear();

        //构建完成 对其进行操作 图表
        List<String> pieDateListTitle = new ArrayList<>();
        List<Entry> pieDateListNumber = new ArrayList<>();
        if (currentePayment) {

            int count = 0;
            if (billItemForCard.getSalaryMoney() > 0) {
                pieDateListTitle.add("工资");
                pieDateListNumber.add(new Entry((float) billItemForCard.getSalaryMoney(), count++));
            }
            if (billItemForCard.getFinanceMoney() > 0) {
                pieDateListTitle.add("理财");
                pieDateListNumber.add(new Entry((float) billItemForCard.getFinanceMoney(), count++));
            }
            if (billItemForCard.getRedMoney() > 0) {
                pieDateListTitle.add("红包");
                pieDateListNumber.add(new Entry((float) billItemForCard.getRedMoney(), count++));
            }
            if (billItemForCard.getOtherIncomeMoney() > 0) {
                pieDateListTitle.add("其他");
                pieDateListNumber.add(new Entry((float) billItemForCard.getOtherIncomeMoney(), count));
            }

        } else {
            int count = 0;
            if (billItemForCard.getEatMoney() > 0) {
                pieDateListTitle.add("吃喝");
                pieDateListNumber.add(new Entry((float) billItemForCard.getEatMoney(), count++));
            }
            if (billItemForCard.getPlayMoney() > 0) {
                pieDateListTitle.add("娱乐");
                pieDateListNumber.add(new Entry((float) billItemForCard.getPlayMoney(), count++));
            }
            if (billItemForCard.getShopMoney() > 0) {
                pieDateListTitle.add("购物");
                pieDateListNumber.add(new Entry((float) billItemForCard.getShopMoney(), count++));
            }
            if (billItemForCard.getOtherSpendMoney() > 0) {
                pieDateListTitle.add("杂项");
                pieDateListNumber.add(new Entry((float) billItemForCard.getOtherSpendMoney(), count));
            }
        }

        //设置图表
        pieDataSet = new PieDataSet(pieDateListNumber, "金额类型");
        pieDataSet.setSliceSpace(3f);
        //饼图Item被选中时变化的距离
        pieDataSet.setSelectionShift(10f);
        //颜色填充
        pieDataSet.setColors(new int[]{Color.parseColor("#fa86be"), Color.parseColor("#a275e3"), Color.parseColor("#9aebed"), Color.parseColor("#fffcab")});
        pieDataSet.setValueTextSize(15);
        pieData = new PieData(pieDateListTitle, pieDataSet);
        //% 显示
        pieData.setValueFormatter(new PercentFormatter());

        mPieChart.setData(pieData);
        mPieChart.animateX(1400);
        //使用百分比
        mPieChart.setUsePercentValues(true);
        //设置图列可见
        mPieChart.getLegend().setEnabled(true);
        //设置图列标识的大小
        mPieChart.getLegend().setFormSize(14);
        //设置图列标识文字的大小
        mPieChart.getLegend().setTextSize(14);
        //设置图列标识的形状
        mPieChart.getLegend().setForm(Legend.LegendForm.CIRCLE);
        mPieChart.setDescription("金额饼图");
        //设置是否可转动
        mPieChart.setRotationEnabled(true);
        mPieChart.setHoleColorTransparent(true);
        mPieChart.setDrawCenterText(true);         //Boolean类型    设置中心圆是否可以显示文字
        mPieChart.setCenterText(currentePayment?"收入":"支出");
        mPieChart.setCenterTextSize(15);         //设置中心圆的文字大小
        mPieChart.setCenterTextColor(Color.parseColor("#2b2b2b"));
        //设置图列的位置
        mPieChart.getLegend().setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        //设置图列标识的形状
        mPieChart.getLegend().setForm(Legend.LegendForm.CIRCLE);
        mPieChart.invalidate();

        mPieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
//                Toast.makeText(getContext()," Val "+e.getVal()+"  Xindex  "+e.getXIndex()+"   "+pieData.getXVals().get(e.getXIndex()),Toast.LENGTH_SHORT).show();
                currentMoneyType=pieData.getXVals().get(e.getXIndex());
                setListViewForList();
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    public  void  setListViewForList(){
        int payment=currentePayment?1:0;
        recycleViewList.clear();
        recycleViewList=DataSupport.select().where("head_id like ? and payment_type = ? and money_type = ? and user_id = ?",currentYear+currentMonth+"%",payment+"",currentMoneyType,MainActivity.getUserLogining()).order("money desc").find(BillItem.class);
//        Log.d(TAG, "setListViewForList: "+recycleViewList.size());
        page_card_listView.setLayoutManager(linearLayoutManager);
        billItemForCardAdapter=new BillItemForCardAdapter(getActivity(),recycleViewList);
        page_card_listView.setAdapter(billItemForCardAdapter);
        billItemForCardAdapter.notifyDataSetChanged();

    }
    public void initListener() {
        page_card_selector.setOnClickListener(this);
        page_card_change_payment.setOnClickListener(this);
    }

    //查询已存在时间 ，加入seletor选项
    public void initSelectorDate() {
        billItemList.clear();
        billItemList = DataSupport.select().where("user_id = ?",MainActivity.getUserLogining()).order("create_date").find(BillItem.class);
        ArrayList<String> datelist = new ArrayList<>();
        BillItem b = null;
        for (int i = billItemList.size() - 1; i >= 0; i--) {
            b = billItemList.get(i);
            String str = String.valueOf(b.getHead_id()).substring(0, 6);
            if (!datelist.contains(str)) {
                datelist.add(str);
            }
        }

        ArrayList<ArrayList<String>> date = new ArrayList<>();
        int count = 0;
        String year = datelist.get(0).substring(0, 4);
        String month = datelist.get(0).substring(4, 6);
        ArrayList<String> dateinner = new ArrayList<>();
        dateinner.add(year);
        dateinner.add(month);
        date.add(dateinner);
        for (int i = 1; i < datelist.size(); i++) {
            year = datelist.get(i).substring(0, 4);
            month = datelist.get(i).substring(4, 6);
            if (date.get(count).contains(year)) {
                date.get(count).add(month);
            } else {
                count++;
                dateinner.add(year);
                dateinner.add(month);
                date.add(dateinner);
            }
        }
        for (int i = 0; i < date.size(); i++) {
            options1Items.add(new ProvinceBean(i, date.get(i).get(0), "des", "c"));
            ArrayList<String> a = new ArrayList<>();
            for (int j = 1; j < date.get(i).size() - 1; j++) {
                a.add(date.get(i).get(j));
            }
            options2Items.add(a);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.page_card_selector:
                pvOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3, View v) {
                        //返回的分别是三个级别的选中位置
                        currentYear = options1Items.get(options1).getPickerViewText();
                        currentMonth = options2Items.get(options1).get(option2);
                        currentSelectorTime = currentSelectorTime + "年"
                                + currentMonth + "月";
                        //清除 Recycle

                        if(billItemForCardAdapter!=null){
                            recycleViewList.clear();
                            billItemForCardAdapter.notifyDataSetChanged();
                        }

                        setShowInfor();
                        queryPieDate(currentYear+currentMonth);
                    }
                }).setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
                        String str = "options1: " + options1 + "\noptions2: " + options2 + "\noptions3: " + options3;
                    }
                })
                        .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                        .setTitleColor(Color.LTGRAY)
                        .setTextColorCenter(Color.LTGRAY)
                        .isDialog(true)
                        .setTextColorCenter(Color.BLACK)
                        .setBackgroundId(0x00000000) //设置外部遮罩颜色
                        .setSubmitText("确定")//确定按钮文字
                        .setCancelText("取消")//取消按钮文字
                        .setSubCalSize(18)//确定和取消文字大小
                        .setTitleText("选择月份")
                        .setTitleSize(20)//标题文字大小
                        .setContentTextSize(18)//滚轮文字大小
                        .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                        .setSelectOptions(0, 1)  //设置默认选中项
                        .setOutSideCancelable(false)//点击外部dismiss default true
                        .isDialog(true)//是否显示为对话框样式
                        .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                        .build();

                pvOptions.setPicker(options1Items, options2Items);//添加数据源
                pvOptions.show();
                break;
            case R.id.page_card_change_payment:
                currentePayment=currentePayment?false:true;

                page_card_current_payment.setText(currentePayment ? "收入" : "支出");
                page_card_listName.setText((currentePayment ? "收入" : "支出")+"排行榜");
                queryPieDate(currentYear+currentMonth);
                //清除 Recycle

                if(billItemForCardAdapter!=null){
                    recycleViewList.clear();
                    billItemForCardAdapter.notifyDataSetChanged();
                }

                break;
            default:
                break;
        }
    }
}

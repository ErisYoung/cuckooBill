package cn.edu.hznu.cuckoobill;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView billTypeSelected;

    private EditText billCount;
    private EditText bill_note_content;

    private InputMethodManager inputMethodManager;

    private LinearLayout incomeLL;
    private LinearLayout spendLL;
    private LinearLayout keyboard;

    private RadioGroup moneyTypeSpend;
    private RadioGroup moneyTypeIncome;
    private RadioGroup paymentType;

    private RadioButton radioPaymentSelected;
    private RadioButton radioSelected;
    private RadioButton RBMoneyTypeSpend;
    private RadioButton RBMoneyTypeIncome;

    private TextView cancle;
    private TextView saveItem;
    private TextView bill_data;

    private String paymentTypeFinal;
    private String moneyTypeSelectedStr;
    private String MonAndDay;
    private String note_content = null;
    private int headId;

    //支出为false
    private boolean payment_type;

    private Intent getIntent;


    private Button saveCommand;
    private Button button_1, button_2, button_3, button_4, button_5, button_6, button_7, button_8, button_9, button_0, button_dot, button_del, button_confirm;

    private Date create_date;

    private int itemCurrentId=-1;
    private boolean itemCurrentPaymentType;
    private String itemCurrentCount;

    FragmentManager fragmentManagerPay = getSupportFragmentManager();
    FragmentTransaction fragmentTransactionPay = fragmentManagerPay.beginTransaction();

    private String eating=null;
    private String salary=null;


    private static final String TAG = "AddItemActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref=getSharedPreferences("User",MODE_PRIVATE);
        if(!pref.getBoolean("isLogin",false)){
            startActivity(new Intent(AddItemActivity.this,LoginActivity.class));
        }
        setContentView(R.layout.activity_add_item);

        init();
        initKeyboard();
        initListeners();
    }

    public void init() {
        eating=this.getResources().getString(R.string.eating);
        salary=this.getResources().getString(R.string.salary);

        create_date = new Date();

        billCount = (EditText) findViewById(R.id.bill_amount);

        billCount.setInputType(InputType.TYPE_NULL);


        bill_note_content = (EditText) findViewById(R.id.bill_note_content);


        bill_data = (TextView) findViewById(R.id.bill_date);
        billTypeSelected = (TextView) findViewById(R.id.bill_type_selected);

        moneyTypeSpend = (RadioGroup) findViewById(R.id.radiogroup_types_money_spend);
        moneyTypeIncome = (RadioGroup) findViewById(R.id.radiogroup_types_money_income);

        incomeLL = (LinearLayout) findViewById(R.id.all_type_income);
        spendLL = (LinearLayout) findViewById(R.id.all_type_spend);
        keyboard = (LinearLayout) findViewById(R.id.keyboard);

        paymentType = (RadioGroup) findViewById(R.id.radiogroup_types_payments);

        RBMoneyTypeSpend = (RadioButton) findViewById(R.id.spending_eating);
        RBMoneyTypeIncome = (RadioButton) findViewById(R.id.income_salary);

        cancle = (TextView) findViewById(R.id.cancle_item);
        saveItem = (TextView) findViewById(R.id.save_item);

        FontHelper.injectFont(findViewById(R.id.rootView));

        itemCurrentCount=billCount.getText().toString();

        //判断为add 还是Edit 来初始化选项
        getIntent=getIntent();
        if(getIntent.getStringExtra("clickedItemId")!=null){
            itemCurrentId=Integer.parseInt(getIntent.getStringExtra("clickedItemId"));
            MonAndDay=getIntent.getStringExtra("clickedItemDate").substring(4,6)+"月"+getIntent.getStringExtra("clickedItemDate").substring(6)+"日";
            moneyTypeSelectedStr=getIntent.getStringExtra("moneyType");
            itemCurrentCount=getIntent.getStringExtra("moneyCount");
            itemCurrentPaymentType= itemCurrentCount.startsWith("+");
            bill_note_content.setText(getIntent.getStringExtra("clickedItemNote"));

//            Toast.makeText(AddItemActivity.this,"payment"+itemCurrentPaymentType+"  moneyType"+moneyTypeSelectedStr,Toast.LENGTH_SHORT).show();
            if(itemCurrentPaymentType){
                paymentType.check(R.id.incoming);
                incomeLL.setVisibility(View.VISIBLE);
                spendLL.setVisibility(View.GONE);
                billCount.setText("+"+itemCurrentCount.substring(1));
                switch (moneyTypeSelectedStr){
                    case "工资":
                        moneyTypeIncome.check(R.id.income_salary);
                        break;
                    case "红包":
                        moneyTypeIncome.check(R.id.income_red_packet);
                        break;
                    case "理财":
                        moneyTypeIncome.check(R.id.income_financial);
                        break;
                    case "其他":
                        moneyTypeIncome.check(R.id.income_other);
                        break;
                }
            }
            else {
                paymentType.check(R.id.spending);
                incomeLL.setVisibility(View.GONE);
                spendLL.setVisibility(View.VISIBLE);
                billCount.setText("-"+itemCurrentCount.substring(1));
                switch (moneyTypeSelectedStr){
                    case "吃喝":
                        moneyTypeSpend.check(R.id.spending_eating);
                        break;
                    case "购物":
                        moneyTypeSpend.check(R.id.spending_shopping);
                        break;
                    case "娱乐":
                        moneyTypeSpend.check(R.id.spending_playing);
                        break;
                    case "杂项":
                        moneyTypeSpend.check(R.id.spending_clutter);
                        break;
                }
            }

        }
        // 新增某日的一条iTem
        else if(getIntent.getStringExtra("currentDate")!=null){
            paymentType.check(R.id.spending);
            moneyTypeIncome.check(R.id.income_salary);
            moneyTypeSpend.check(R.id.spending_eating);
            String currentTime=getIntent.getStringExtra("currentDate");
            MonAndDay=currentTime.substring(4,6)+"月"+currentTime.substring(6)+"日";
            headId=Integer.parseInt(currentTime);
            create_date=new Date(Date.parse(currentTime.substring(4,6)+"/"+currentTime.substring(6)+"/"+currentTime.substring(0,4)));
            moneyTypeSelectedStr = eating;
        }
        //新增当日的一条Item
        else {
            paymentType.check(R.id.spending);
            moneyTypeIncome.check(R.id.income_salary);
            moneyTypeSpend.check(R.id.spending_eating);
            parseDateToMonAndDay(create_date);
            moneyTypeSelectedStr = eating;
        }


        bill_data.setText(MonAndDay);
        billTypeSelected.setText(moneyTypeSelectedStr);
        //控制软键盘
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS,0);

    }

    public void initKeyboard() {
        button_0 = (Button) findViewById(R.id.button_0);
        button_1 = (Button) findViewById(R.id.button_1);
        button_2 = (Button) findViewById(R.id.button_2);
        button_3 = (Button) findViewById(R.id.button_3);
        button_4 = (Button) findViewById(R.id.button_4);
        button_5 = (Button) findViewById(R.id.button_5);
        button_6 = (Button) findViewById(R.id.button_6);
        button_7 = (Button) findViewById(R.id.button_7);
        button_8 = (Button) findViewById(R.id.button_8);
        button_9 = (Button) findViewById(R.id.button_9);
        button_dot = (Button) findViewById(R.id.button_dot);
        button_del = (Button) findViewById(R.id.button_del);
        button_confirm = (Button) findViewById(R.id.button_confirm);
        button_0.setOnClickListener(this);
        button_1.setOnClickListener(this);
        button_2.setOnClickListener(this);
        button_3.setOnClickListener(this);
        button_4.setOnClickListener(this);
        button_5.setOnClickListener(this);
        button_6.setOnClickListener(this);
        button_7.setOnClickListener(this);
        button_8.setOnClickListener(this);
        button_9.setOnClickListener(this);
        button_dot.setOnClickListener(this);
        button_del.setOnClickListener(this);
        button_confirm.setOnClickListener(this);
    }

    public void initListeners() {
        cancle.setOnClickListener(this);
        saveItem.setOnClickListener(this);

        billCount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText d=(EditText)v;
                if(hasFocus){
                    d.setText("");
                }
            }
        });

        bill_note_content.addTextChangedListener(new EditChangedListener());
        bill_note_content.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText d=(EditText)v;
                if(hasFocus){
                    d.setText("");
                }
            }
        });

        moneyTypeSpend.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioSelected = (RadioButton) findViewById(moneyTypeSpend.getCheckedRadioButtonId());
                moneyTypeSelectedStr = radioSelected.getText().toString().substring(2);
                billTypeSelected.setText(radioSelected.getText().toString());
//                Log.d(TAG, "onCheckedChanged: spend" + moneyTypeSelectedStr);
            }
        });
        moneyTypeIncome.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioSelected = (RadioButton) findViewById(moneyTypeIncome.getCheckedRadioButtonId());
                moneyTypeSelectedStr = radioSelected.getText().toString().substring(2);
                billTypeSelected.setText(radioSelected.getText().toString());
//                Log.d(TAG, "onCheckedChanged: income" + moneyTypeSelectedStr);
            }
        });


        paymentType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioPaymentSelected = (RadioButton) findViewById(paymentType.getCheckedRadioButtonId());
                paymentTypeFinal = radioPaymentSelected.getText().toString();
                payment_type = paymentTypeFinal.equals("收入");

                if (payment_type) {
                    incomeLL.setVisibility(View.VISIBLE);
                    spendLL.setVisibility(View.GONE);
                    moneyTypeIncome.check(R.id.income_salary);
                    moneyTypeSelectedStr = "工资";
                    itemCurrentCount=billCount.getText().toString();
                    if(itemCurrentCount.startsWith("+")||itemCurrentCount.startsWith("-")){
                        billCount.setText("+"+itemCurrentCount.substring(1));
                    }
                    else {
                        billCount.setText("+"+itemCurrentCount.substring(0));
                    }
                    billCount.setSelection(billCount.getText().toString().length()-1);

                    //设置默认moneyType
                    billTypeSelected.setText(salary);


                } else {
                    incomeLL.setVisibility(View.GONE);
                    spendLL.setVisibility(View.VISIBLE);
                    moneyTypeSpend.check(R.id.spending_eating);
                    moneyTypeSelectedStr = "吃喝";
                    itemCurrentCount=billCount.getText().toString();


                    if(itemCurrentCount.startsWith("-")||itemCurrentCount.startsWith("+")){
                        billCount.setText("-"+itemCurrentCount.substring(1));
                    }
                    else {
                        billCount.setText("-"+itemCurrentCount.substring(0));
                    }
                    billCount.setSelection(billCount.getText().toString().length()-1);

                    billTypeSelected.setText(eating);


                }

            }
        });


        billCount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    billCount.setText("");
                    setKeyBoardFrag();
                } else {
                    hideKeyBoardFrag();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancle_item:
                this.finish();
                break;
            case R.id.button_confirm:
            case R.id.save_item:
                commitItem();
                break;
            case R.id.button_0:
                performKeyDown(KeyEvent.KEYCODE_0);
                break;
            case R.id.button_1:
                performKeyDown(KeyEvent.KEYCODE_1);
                break;
            case R.id.button_2:
                performKeyDown(KeyEvent.KEYCODE_2);
                break;
            case R.id.button_3:
                performKeyDown(KeyEvent.KEYCODE_3);
                break;
            case R.id.button_4:
                performKeyDown(KeyEvent.KEYCODE_4);
                break;
            case R.id.button_5:
                performKeyDown(KeyEvent.KEYCODE_5);
                break;
            case R.id.button_6:
                performKeyDown(KeyEvent.KEYCODE_6);
                break;
            case R.id.button_7:
                performKeyDown(KeyEvent.KEYCODE_7);
                break;
            case R.id.button_8:
                performKeyDown(KeyEvent.KEYCODE_8);
                break;
            case R.id.button_9:
                performKeyDown(KeyEvent.KEYCODE_9);
                break;
            case R.id.button_dot:
                performKeyDown(KeyEvent.KEYCODE_NUMPAD_DOT);
                break;
            case R.id.button_del:
                performKeyDown(KeyEvent.KEYCODE_DEL);
                break;
            default:
                break;
        }
    }

    //模拟键盘输入
    public void performKeyDown(final int keyCode) {
        new Thread() {
            public void run() {
                try {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(keyCode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void commitItem() {
        //为Edit界面，update数据
        String billCountText=billCount.getText().toString();
        double money;
        if(TextUtils.isEmpty(billCountText)){
            Toast.makeText(AddItemActivity.this,"请输入金额",Toast.LENGTH_SHORT).show();
            return;
        }
        if(billCountText.startsWith("-")||billCountText.startsWith("+")){
            money = Double.parseDouble(billCount.getText().toString().substring(1));
        }
        else {
            money = Double.parseDouble(billCount.getText().toString());
        }

//        Log.d(TAG, "commitItem: "+moneyTypeSelectedStr);

        String user_id = MainActivity.getUserLogining();
        note_content = bill_note_content.getText().toString();
        BillItem billItem = new BillItem();

        if(itemCurrentId!=-1){
            String time=getIntent.getStringExtra("clickedItemDate");
            billItem.setHead_id(Integer.parseInt(getIntent.getStringExtra("clickedItemDate")));
            billItem.setMoney(money);
            billItem.setNote(note_content);
//            billItem.setCreate_date(new Date(Date.parse(time.substring(4,6)+"/"+time.substring(6)+"/"+time.substring(0,4))));
            billItem.setMoney_type(moneyTypeSelectedStr);
            billItem.setPayment_type(payment_type);
            billItem.updateAll("id=?",itemCurrentId+"");
            itemCurrentId=-1;
            startActivity(new Intent(AddItemActivity.this,MainActivity.class));
        }
        else {
            billItem.setHead_id(headId);
            billItem.setUser_id(user_id);
            billItem.setMoney(money);
            billItem.setCreate_date(create_date);
            billItem.setMoney_type(moneyTypeSelectedStr);
            billItem.setPayment_type(payment_type);
            billItem.setNote(note_content);
            billItem.save();
            startActivity(new Intent(AddItemActivity.this,MainActivity.class));
        }
        finish();

    }

    public void parseDateToMonAndDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;
        String monStr = month < 10 ? "0" + month : month + "";
        int day = calendar.get(Calendar.DATE);
        String dayStr = day < 10 ? "0" + day : day + "";
        MonAndDay = monStr + "月" + dayStr + "日";
        int year=calendar.get(Calendar.YEAR);
        headId=Integer.parseInt(year+monStr+dayStr);
    }

    public void setKeyBoardFrag() {
        inputMethodManager.hideSoftInputFromWindow(billCount.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        keyboard.setVisibility(View.VISIBLE);
    }

    public void hideKeyBoardFrag() {
        keyboard.setVisibility(View.INVISIBLE);

    }
    //监听Text长度类
    class EditChangedListener implements TextWatcher {
        private CharSequence temp;//监听前的文本
        private int editStart;//光标开始位置
        private int editEnd;//光标结束位置
        private final int charMaxNum = 50;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }
        @Override
        public void afterTextChanged(Editable s) {
            /** 得到光标开始和结束位置 ,超过最大数后记录刚超出的数字索引进行控制 */
            editStart = bill_note_content.getSelectionStart();
            editEnd = bill_note_content.getSelectionEnd();
            if (temp.length() > charMaxNum) {
                Toast.makeText(getApplicationContext(), " 不要超过50个字哦！", Toast.LENGTH_LONG).show();
                s.delete(editStart - 1, editEnd);
                int tempSelection = editStart;
                bill_note_content.setText((itemCurrentPaymentType?"+":"-")+s);
                bill_note_content.setSelection(tempSelection);
            }
        }
    }
}

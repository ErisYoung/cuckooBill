package cn.edu.hznu.cuckoobill;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.suke.widget.SwitchButton;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

public class BillNoting extends AppCompatActivity {

    private static final String TAG = "BillNoting";
    
    private com.suke.widget.SwitchButton SwithButton;
    private TextView cancle,noting_current_time,textSize,deter;
    private EditText noting_text;

    private LinearLayout rootView;
    private LinearLayout inner_content;
    private LinearLayout noting_time_selector;

    private ArrayList<ProvinceBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();

    private OptionsPickerView pvOptions;

    public boolean isNoting=false;

    public String currentHour="21";
    public String currentMinute="00";
    public String currentNoteContent;
    private SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_noting);

        //获取上次保留的数据
        pref=getSharedPreferences("noteDate",MODE_PRIVATE);
        isNoting=pref.getBoolean("isNoting",false);
        currentHour=pref.getString("currentHour","21");
        currentMinute=pref.getString("currentMinute","00");
        currentNoteContent=pref.getString("currentNoteContent","松下问童子,文体两开花");

//        Log.d(TAG, "onCreate: "+isNoting);

        SwithButton=(com.suke.widget.SwitchButton)findViewById(R.id.switch_button);
        cancle=(TextView)findViewById(R.id.cancle_button);
        rootView=(LinearLayout) findViewById(R.id.rootView);
        inner_content=(LinearLayout)findViewById(R.id.inner_content);
        noting_time_selector=(LinearLayout)findViewById(R.id.noting_time_selector);

        noting_current_time=(TextView)findViewById(R.id.noting_current_time);
        textSize=(TextView)findViewById(R.id.textSize);
        deter=(TextView)findViewById(R.id.deter);

        noting_text=(EditText)findViewById(R.id.noting_text);

        //finish 后回来  保存之前的状态
        if(isNoting){
            SwithButton.setChecked(true);
            inner_content.setVisibility(View.VISIBLE);
            noting_current_time.setText(currentHour+":"+currentMinute);
            noting_text.setText(currentNoteContent);
            textSize.setText("(" + (currentNoteContent.length()) +"/50"+")");
        }
        else {
            SwithButton.setChecked(false);
            inner_content.setVisibility(View.GONE);
        }

        FontHelper.injectFont(findViewById(R.id.rootView));

        initPickerDate();
        deter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=noting_text.getText().toString();

                //判断当前EditView中的text,如何为""，则设置默认值,存储
                if(TextUtils.isEmpty(text)){
                    currentNoteContent=noting_text.getHint().toString();
                }
                else{
                    currentNoteContent=text;
                }

                SharedPreferences.Editor editor=getSharedPreferences("noteDate",MODE_PRIVATE).edit();
                editor.putBoolean("isNoting",isNoting);

                User userList= DataSupport.select().where("number = ?",pref.getString("LoginUser","15990184787")).findFirst(User.class);
                userList.setNotingHour(Integer.parseInt(currentHour));
                userList.setNotingMi(Integer.parseInt(currentMinute));
                userList.setNotingText(currentNoteContent);
                userList.save();

                editor.putString("currentHour",currentHour);
                editor.putString("currentMinute",currentMinute);
                editor.putString("currentNoteContent",currentNoteContent);
                //通道标记 true 为由 确定到Pause， false 为 cancle和 返回
                editor.putBoolean("enter",true);
                editor.apply();
                if(isNoting){
                    //启动服务
                    startService(new Intent(BillNoting.this,NotingServer.class));
                    finish();
                }
                else {
                    //关闭服务
                    stopService(new Intent(BillNoting.this,NotingServer.class));
                    finish();
                }
            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 finish();
            }
        });


        noting_text.addTextChangedListener(new EditChangedListener());

        noting_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText d=(EditText)v;
                if(hasFocus){
                    d.setText("");
                }
            }
        });


        noting_time_selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvOptions = new OptionsPickerBuilder(BillNoting.this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3, View v) {
                        //返回的分别是三个级别的选中位置
                        currentHour= options1Items.get(options1).getPickerViewText();
                        currentMinute = options2Items.get(options1).get(option2);

                        noting_current_time.setText(currentHour+":"+currentMinute);


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
                        .setCyclic(true,true,true)
                        .setTitleText("选择时间")
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
            }
        });

        SwithButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(isChecked){
                    isNoting=true;

                    inner_content.setVisibility(View.VISIBLE);
                }
                else {
                    isNoting=false;


                    inner_content.setVisibility(View.GONE);
                }
            }
        });

    }

    public void  initPickerDate(){
        ArrayList<String> op1=new ArrayList<>();

        for (int j = 0; j < 60; j++) {
            op1.add(String.format("%02d",j));
        }
        for (int i = 0; i <24; i++) {
            options1Items.add(new ProvinceBean(i,String.format("%02d",i),"des","c"));
            options2Items.add(op1);
        }
    }

    public String getCurrentHour() {
        return currentHour;
    }

    public String getCurrentMinute() {
        return currentMinute;
    }

    public String getCurrentNoteContent() {
        return currentNoteContent;
    }

    public boolean isNoting() {
        return isNoting;
    }


    @Override
    protected void onPause() {
        super.onPause();

        currentNoteContent=noting_text.getText().toString();
        pref=getSharedPreferences("noteDate",MODE_PRIVATE);
        SharedPreferences.Editor editor=getSharedPreferences("noteDate",MODE_PRIVATE).edit();
        if(pref.getBoolean("enter",false)){
            editor.putBoolean("isNoting",isNoting);
            editor.putBoolean("enter",false);
        }
        editor.apply();
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
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            textSize.setText("(" + (s.length()) +"/"+charMaxNum+")");
        }
        @Override
        public void afterTextChanged(Editable s) {
            /** 得到光标开始和结束位置 ,超过最大数后记录刚超出的数字索引进行控制 */
            editStart = noting_text.getSelectionStart();
            editEnd = noting_text.getSelectionEnd();
            if (temp.length() > charMaxNum) {
                Toast.makeText(getApplicationContext(), "你输入的字数已经超过了限制！", Toast.LENGTH_LONG).show();
                s.delete(editStart - 1, editEnd);
                int tempSelection = editStart;
                noting_text.setText(s);
                noting_text.setSelection(tempSelection);
            }
        }
    }



}

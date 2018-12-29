package cn.edu.hznu.cuckoobill.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.suke.widget.SwitchButton;

import org.litepal.crud.DataSupport;

import cn.edu.hznu.cuckoobill.Helper.FontHelper;
import cn.edu.hznu.cuckoobill.Model.User;
import cn.edu.hznu.cuckoobill.R;

public class BillBudgetSetting extends AppCompatActivity {
    private static final String TAG = "BillBudgetSetting";
    private LinearLayout inner_content;

    private TextView cancle,deter;

    private EditText moneySelector;

    private com.suke.widget.SwitchButton SwithButton;

    public boolean isBudget=false;
    public float currentCount=0;
    private SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_budget_setting);
        //初始化

        //获取上次保留的数据
        pref=getSharedPreferences("User",MODE_PRIVATE);
        isBudget=pref.getBoolean("isBudget",false);
        currentCount=pref.getFloat("budgetCount",10000);

        SwithButton=(com.suke.widget.SwitchButton)findViewById(R.id.switch_button);
        cancle=(TextView)findViewById(R.id.cancle_button);
        inner_content=(LinearLayout)findViewById(R.id.inner_content);
        deter=(TextView)findViewById(R.id.deter);
        moneySelector=(EditText)findViewById(R.id.budget_acount);

        FontHelper.injectFont(findViewById(R.id.rootView));

        if(isBudget){
            SwithButton.setChecked(true);
            inner_content.setVisibility(View.VISIBLE);
            moneySelector.setText(String.valueOf((int)currentCount));
        }

        else {
            SwithButton.setChecked(false);
            inner_content.setVisibility(View.GONE);
        }

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        moneySelector.addTextChangedListener(new EditChangedListener());
        moneySelector.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText d=(EditText)v;
                if(hasFocus){
                    d.setText("");
                }
            }
        });

        deter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=moneySelector.getText().toString();

                //输入字符 清空
                for (int i = 0; i <text.length(); i++) {
                    if(Character.isLetter(text.charAt(i))){
                        Toast.makeText(BillBudgetSetting.this,"请输入数字",Toast.LENGTH_SHORT).show();
                        moneySelector.setText("");
                        return;
                    }
                }

                //判断当前EditView中的text,如何为""，则设置默认值
                if(TextUtils.isEmpty(text)){
                    currentCount=Float.parseFloat(moneySelector.getHint().toString());
                }
                else {
                    currentCount=Float.parseFloat(text);
                }

                SharedPreferences.Editor editor=getSharedPreferences("User",MODE_PRIVATE).edit();
                pref=getSharedPreferences("User",MODE_PRIVATE);
                //更新数据库
                User userList=DataSupport.select().where("number = ?",pref.getString("LoginUser","15990184787")).findFirst(User.class);
                userList.setBudget((int)currentCount);
                userList.save();

                editor.putBoolean("isBudget",isBudget);
                editor.putFloat("budgetCount",currentCount);
                //通道标记 true 为由 确定到Pause， false 为 cancle和 返回
                editor.putBoolean("enter",true);
                editor.apply();
                finish();
            }
        });

        SwithButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(isChecked){
                    isBudget=true;

                    inner_content.setVisibility(View.VISIBLE);
                }
                else {
                    isBudget=false;


                    inner_content.setVisibility(View.GONE);
                }
            }
        });

    }
    @Override
    protected void onPause() {
        super.onPause();

        pref=getSharedPreferences("User",MODE_PRIVATE);
        SharedPreferences.Editor editor=getSharedPreferences("User",MODE_PRIVATE).edit();
        if(pref.getBoolean("enter",false)){
            Log.d(TAG, "onPause: "+isBudget+" "+currentCount);
            editor.putBoolean("isBudget",isBudget);
            editor.putBoolean("enter",false);
            editor.apply();
        }
    }

    class EditChangedListener implements TextWatcher {
        private CharSequence temp;//监听前的文本
        private int editStart;//光标开始位置
        private int editEnd;//光标结束位置
        private final int charMaxNum = 5;
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
        @Override
        public void afterTextChanged(Editable s) {
            /** 得到光标开始和结束位置 ,超过最大数后记录刚超出的数字索引进行控制 */
            editStart = moneySelector.getSelectionStart();
            editEnd = moneySelector.getSelectionEnd();

            if (temp.length() > charMaxNum) {
                Toast.makeText(getApplicationContext(), "你输入的字数已经超过了限制！", Toast.LENGTH_LONG).show();
                s.delete(editStart - 1, editEnd);
                int tempSelection = editStart;
                moneySelector.setText(s);
                moneySelector.setSelection(tempSelection);
            }
        }
    }
}

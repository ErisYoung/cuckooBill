package cn.edu.hznu.cuckoobill.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.hznu.cuckoobill.Helper.FontHelper;
import cn.edu.hznu.cuckoobill.Model.User;
import cn.edu.hznu.cuckoobill.R;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";

    private Button validateNum_btn;
    private Button landing_btn;
    private EditText userName;
    private EditText validateNum;
    public EventHandler eh; //事件接收器
    private TimeCount mTimeCount;//计时器

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref=getSharedPreferences("User",MODE_PRIVATE);
        boolean isLogin=pref.getBoolean("isLogin",false);

        if(isLogin){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
        setContentView(R.layout.activity_login);

        FontHelper.injectFont(findViewById(R.id.rootView));

        initEvent();
        init();
    }

    private void initEvent(){
        userName = (EditText) findViewById(R.id.userName);
        validateNum = (EditText) findViewById(R.id.validateNum);
        validateNum_btn = (Button) findViewById(R.id.validateNum_btn);
        landing_btn = (Button) findViewById(R.id.landing_btn);
        validateNum_btn.setOnClickListener(this);
        landing_btn.setOnClickListener(this);
        mTimeCount = new TimeCount(60000, 1000);
    }

    /**
     * 初始化事件接收器
     */
    private void init(){
        eh = new EventHandler(){
            private static final String TAG = "LoginActivity";
        public void afterEvent(int event, int result, Object data) {
            // afterEvent会在子线程被调用，因此如果后续有UI相关操作，需要将数据发送到UI线程
            Message msg = new Message();
            msg.arg1 = event;
            msg.arg2 = result;
            msg.obj = data;
            new Handler(Looper.getMainLooper(), new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            // TODO 处理成功得到验证码的结果
                            // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                        } else {
                            // TODO 处理错误的结果
                            ((Throwable) data).printStackTrace();
                        }
                    } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                            saveLoginStatus(true,userName.getText().toString());
//                            LitePal.getDatabase();
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            finish();
                            // TODO 处理验证码验证通过的结果
                        } else {
                            // TODO 处理错误的结果
                            ((Throwable) data).printStackTrace();
                        }
                    }
                    // TODO 其他接口的返回结果也类似，根据event判断当前数据属于哪个接口
                    return false;
                }
            }).sendMessage(msg);
        }
    };
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }

    public void saveLoginStatus(boolean loginStatus,String userName){
        LitePal.getDatabase();
        User userList=DataSupport.select().where("number = ?",userName).findFirst(User.class);
        SharedPreferences.Editor editor=getSharedPreferences("User",MODE_PRIVATE).edit();
        if(userList==null) {
            User user = new User();
            user.setNumber(userName);
            user.setCreate_date(new Date());
            user.setArriveDays(0);
            user.setBudget(0);
            user.setNotingText("松下问童子,文体两开花");
            user.setNotingMi(0);
            user.setNotingHour(21);
            user.save();

            Log.d(TAG, "saveLoginStatus: 1");
            editor.putString("LoginUser",userName);
            editor.putBoolean("isLogin",true);
            editor.putFloat("budgetCount",user.getBudget());
            editor.putString("currentHour",String.format("%02d",user.getNotingHour()));
            editor.putString("currentMinute",String.format("%02d",user.getNotingMi()));
            editor.putString("currentNoteContent",user.getNotingText());

        }
        else {
            Log.d(TAG, "saveLoginStatus: 2");
            editor.putString("LoginUser",userName);
            editor.putBoolean("isLogin",true);
            editor.putFloat("budgetCount",userList.getBudget());
            editor.putString("currentHour",String.format("%02d",userList.getNotingHour()));
            editor.putString("currentMinute",String.format("%02d",userList.getNotingMi()));
            editor.putString("currentNoteContent",userList.getNotingText());
        }

        editor.apply();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.validateNum_btn:
//                SMSSDK.getSupportedCountries();//获取短信目前支持的国家列表
                if(!userName.getText().toString().trim().equals("")){
                    if (checkTel(userName.getText().toString().trim())) {
                        SMSSDK.getVerificationCode("+86",userName.getText().toString());//获取验证码
                        mTimeCount.start();
                    }else{
                        Toast.makeText(LoginActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.landing_btn:
                if (!userName.getText().toString().trim().equals("")) {
                    if (checkTel(userName.getText().toString().trim())) {
                        if (!validateNum.getText().toString().trim().equals("")) {
                            SMSSDK.submitVerificationCode("+86",userName.getText().toString().trim(),validateNum.getText().toString().trim());//提交验证
                        }else{
                            Toast.makeText(LoginActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(LoginActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //手机号正则匹配
    public boolean checkTel(String tel){
        Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
        Matcher matcher = p.matcher(tel);
        return matcher.matches();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);
    }

    //验证码计时器
    class TimeCount extends CountDownTimer{

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            validateNum_btn.setClickable(false);
            validateNum_btn.setText(l/1000 + "秒后重新获取");
        }

        @Override
        public void onFinish() {
            validateNum_btn.setClickable(true);
            validateNum_btn.setText("获取验证码");
        }
    }

}

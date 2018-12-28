package cn.edu.hznu.cuckoobill;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import org.litepal.crud.DataSupport;

import java.util.Date;

import jp.wasabeef.glide.transformations.BlurTransformation;
import mehdi.sakout.fancybuttons.FancyButton;

import static android.content.Context.MODE_PRIVATE;

public class PageMine extends Fragment implements View.OnClickListener{
    private static final String TAG = "PageMine";
    private View MineFragment;
    private ImageView h_head,h_black;

    private TextView user_days;

    private FancyButton bill_noting;
    private FancyButton bill_budget;
    private FancyButton sign_out;
    private SharedPreferences pref;
    private Date nowDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)  {
        MineFragment=inflater.inflate(R.layout.page_mine,container,false);
        pref=getActivity().getSharedPreferences("User",MODE_PRIVATE);
        if(!pref.getBoolean("isLogin",false)){
            startActivity(new Intent(getActivity(),LoginActivity.class));
        }
        init();

        return MineFragment;
    }
    public void init(){



        h_head=(ImageView)MineFragment.findViewById(R.id.h_head);
        h_black=(ImageView)MineFragment.findViewById(R.id.h_back);
        user_days=(TextView)MineFragment.findViewById(R.id.user_days);

        bill_budget=(FancyButton) MineFragment.findViewById(R.id.bill_budget);
        bill_noting=(FancyButton) MineFragment.findViewById(R.id.bill_noting);
        sign_out=(FancyButton) MineFragment.findViewById(R.id.sign_out);


        bill_budget.setOnClickListener(this);
        bill_noting.setOnClickListener(this);
        sign_out.setOnClickListener(this);

        calHomeDays();

        setUserPic();

    }

    public void calHomeDays(){
        User user= DataSupport.select().where("number = ?",MainActivity.getUserLogining()).findFirst(User.class);
        nowDate=new Date();
        int days=(int)(nowDate.getTime()-user.getCreate_date().getTime())/(1000*24*3600);
        user_days.setText("欢迎来到CuckooBill的第"+(days+1)+"天");
    }

    public void setUserPic(){
        Glide.with(getContext())
                .load(R.drawable.timg)
                .asBitmap()
                .centerCrop()
                .dontAnimate()
                .override(100, 100)
                .into(new BitmapImageViewTarget(h_head) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        h_head.setImageDrawable(circularBitmapDrawable);
                    }
                });


        Glide.with(getContext())
                .load(R.drawable.timg)
                .bitmapTransform(new BlurTransformation(getContext(),25),new CenterCrop(getContext()))
                .dontAnimate()
                .into(h_black);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bill_noting:
                startActivity(new Intent(getActivity(),BillNoting.class));
                break;
            case R.id.bill_budget:
                startActivity(new Intent(getActivity(),BillBudgetSetting.class));
                break;
            case R.id.sign_out:
                SharedPreferences.Editor editor=getActivity().getSharedPreferences("User",MODE_PRIVATE).edit();
                editor.putBoolean("isLogin",false);
                editor.apply();
                startActivity(new Intent(getActivity(),LoginActivity.class));
                getActivity().finish();
                break;
            default:
                break;
        }
    }
}

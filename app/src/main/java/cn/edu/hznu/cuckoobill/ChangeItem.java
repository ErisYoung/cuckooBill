package cn.edu.hznu.cuckoobill;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

public class ChangeItem extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "ChangeItem";

    private TextView ReturnTV;
    private TextView DeleteTV;
    private TextView MoneyCountTV;
    private TextView ItemNoteTV;
    private TextView ItemTimeTV;
    private TextView MoneyTypeTV;
    private Button EditBT;
    private FloatingActionButton addItem;

    private String clickedItemNote;
    private String clickedItemDate;
    private String moneyType;
    private String moneyCount;
    private String clickedItemId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_item);
        Intent intent=getIntent();
        clickedItemNote=intent.getStringExtra("clickedItemNote");
        clickedItemDate=intent.getStringExtra("clickedItemDate");
        clickedItemId=intent.getStringExtra("clickedItemId");
        moneyType=intent.getStringExtra("moneyType");
        moneyCount=intent.getStringExtra("moneyCount");
        init();
        initListener();
    }
    public void init(){
        ReturnTV=(TextView)findViewById(R.id.change_item_return);
        DeleteTV=(TextView)findViewById(R.id.change_item_delete);
        EditBT=(Button)findViewById(R.id.edit_item);
        MoneyCountTV=(TextView)findViewById(R.id.bill_oitem_money_count);
        ItemNoteTV=(TextView)findViewById(R.id.change_item_note);
        ItemTimeTV=(TextView)findViewById(R.id.change_item_time);
        MoneyTypeTV=(TextView)findViewById(R.id.bill_oitem_money_type);
        addItem=(FloatingActionButton)findViewById(R.id.addItem);

        FontHelper.injectFont(findViewById(R.id.rootView));

        MoneyCountTV.setText(moneyCount);
        ItemNoteTV.setText(clickedItemNote);
        MoneyTypeTV.setText(moneyType);
        ItemTimeTV.setText(clickedItemDate.substring(0,4)+"-"+clickedItemDate.substring(4,6)+"-"+clickedItemDate.substring(6));

    }
    public void initListener(){
        EditBT.setOnClickListener(this);
        ReturnTV.setOnClickListener(this);
        DeleteTV.setOnClickListener(this);
        addItem.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_item:
                sendToEditItemActivity();
                finish();
                break;
            case R.id.change_item_return:
                this.finish();
                break;
            case R.id.change_item_delete:
                DataSupport.delete(BillItem.class,Integer.parseInt(clickedItemId));
                this.finish();
                break;
            case R.id.addItem:
                Intent intent=new Intent(ChangeItem.this,AddItemActivity.class);
                intent.putExtra("currentDate",clickedItemDate);
                startActivity(intent);
                this.finish();
            default:
                break;
        }
    }
    public void sendToEditItemActivity(){
        Intent intent=new Intent(ChangeItem.this,AddItemActivity.class);
        intent.putExtra("moneyCount",moneyCount);
        intent.putExtra("moneyType",moneyType);
        intent.putExtra("clickedItemId",clickedItemId);
        intent.putExtra("clickedItemDate",clickedItemDate);
        intent.putExtra("clickedItemNote",clickedItemNote);
        startActivity(intent);
    }
}

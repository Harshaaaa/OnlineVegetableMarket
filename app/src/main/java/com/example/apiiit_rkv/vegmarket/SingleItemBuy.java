package com.example.apiiit_rkv.vegmarket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SingleItemBuy extends AppCompatActivity {
    ImageView pic;
    TextView title,cost,weight,costofitem;
    Button plus,minus;
    DatabaseReference mDatabaseSigleItem;
    TextView quan;
    int finalcost=0;
    Bundle bd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_item_buy);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Buy");
        pic=(ImageView)findViewById(R.id.item_image);
        title=(TextView)findViewById(R.id.item_title);
        cost=(TextView)findViewById(R.id.itemcost);
        weight=(TextView)findViewById(R.id.item_description);
        plus=(Button)findViewById(R.id.plus);
        minus=(Button)findViewById(R.id.minus);
        quan=(TextView)findViewById(R.id.data);
        costofitem=(TextView)findViewById(R.id.costofitem);
        bd=getIntent().getExtras();
        title.setText(bd.getString("title"));
        cost.setText(bd.getString("cost"));
        weight.setText(bd.getString("name"));
        Glide.with(this).load(bd.getString("url")).into(pic);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String c=quan.getText().toString();
                int p=Integer.parseInt(c);
                p++;
                quan.setText(""+p);
                setcost(p);
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String c=quan.getText().toString();
                int p=Integer.parseInt(c);
                if(p>0) {
                    p--;
                    quan.setText(""+p);
                }
                setcost(p);
            }
        });
    }
    public void setcost(int p)
    {
        String c=cost.getText().toString();
        int q=Integer.parseInt(c)*p;
        finalcost=q;
        costofitem.setText(""+q);
    }
    public void checkout(View view) {
        if(finalcost==0)
            Toast.makeText(this, "No Items", Toast.LENGTH_SHORT).show();
        else
        {
            final Intent i=new Intent(this,Address.class);
            i.putExtra("amount",finalcost);
            i.putExtra("type","single");
            final ProgressDialog pd;
            pd = new ProgressDialog(SingleItemBuy.this);
            pd.setMessage("Loading Please Wait....");
            pd.show();
            new CountDownTimer(1000,800) {
                @Override
                public void onTick(long millisUntilFinished) {
                }
                @Override
                public void onFinish() {
                    pd.dismiss();
                    startActivity(i);
                }
            }.start();
        }
    }
}

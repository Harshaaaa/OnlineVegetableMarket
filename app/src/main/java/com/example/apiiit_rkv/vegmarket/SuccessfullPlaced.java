package com.example.apiiit_rkv.vegmarket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SuccessfullPlaced extends AppCompatActivity {
    Bundle bd;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successfull_placed);
        bd=getIntent().getExtras();
        tv=(TextView)findViewById(R.id.setcost);
        tv.setText(bd.getInt("mycost")+" Rs");
    }

    public void navtomain(View view) {
        final ProgressDialog pd;
        pd = new ProgressDialog(SuccessfullPlaced.this);
        pd.setMessage("Loading Please Wait....");
        pd.show();
        new CountDownTimer(1000,800) {
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                pd.dismiss();
                startActivity(new Intent(SuccessfullPlaced.this,VegetableMarket.class));
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,VegetableMarket.class));
    }
}

package com.example.apiiit_rkv.vegmarket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Address extends AppCompatActivity {
    Bundle bd;
    EditText yourname,contact,state,flat,area,landmark,pincode,town;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        bd=getIntent().getExtras();
        yourname=(EditText)findViewById(R.id.yourname);
        contact=(EditText)findViewById(R.id.contact);
        state=(EditText)findViewById(R.id.state);
        flat=(EditText)findViewById(R.id.flat);
        area=(EditText)findViewById(R.id.area);
        landmark=(EditText)findViewById(R.id.landmark);
        pincode=(EditText)findViewById(R.id.pincode);
        town=(EditText)findViewById(R.id.town);
    }

    public void payment(View view) {
        if(TextUtils.isEmpty(yourname.getText().toString())||yourname.getText().toString().length()<=5)
        {
            yourname.setError("Please Enter Valid Name");
            return;
        }
        if(TextUtils.isEmpty(contact.getText().toString())||contact.getText().toString().length()!=10)
        {
            contact.setError("Please Enter Valid Mobile Number");
            return;
        }
        if(TextUtils.isEmpty(pincode.getText().toString())||pincode.getText().toString().length()!=6)
        {
            pincode.setError("Please Enter Valid Pincode..");
            return;
        }
        if(TextUtils.isEmpty(flat.getText().toString()))
        {
            flat.setError("Please Enter Valid Address..");
            return;
        }
        if(TextUtils.isEmpty(area.getText().toString()))
        {
            area.setError("Please Enter Valid Area..");
            return;
        }
        if(TextUtils.isEmpty(landmark.getText().toString()))
        {
            landmark.setError("Please Enter Valid Landmark..");
            return;
        }
        if(TextUtils.isEmpty(town.getText().toString()))
        {
            town.setError("Please Enter Valid Town..");
            return;
        }
        if(TextUtils.isEmpty(state.getText().toString()))
        {
            state.setError("Please Enter Valid State..");
            return;
        }
        final Intent i=new Intent(this,PaymentPage.class);
        i.putExtra("amount",bd.getInt("amount"));
        i.putExtra("type",bd.getString("type"));
        final ProgressDialog pd;
        pd = new ProgressDialog(Address.this);
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

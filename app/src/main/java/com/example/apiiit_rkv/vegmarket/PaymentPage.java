package com.example.apiiit_rkv.vegmarket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PaymentPage extends AppCompatActivity {
    Bundle bd;
    int cost;
    String type;
    TextView setcost;
    RadioGroup paytype;
    DatabaseReference mDatabaseCart;
    Button order;
    DatabaseReference fromPath,toPath;
    boolean paying=false;
    RadioButton credit,cod;
    LinearLayout codmenu,creditmenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd=getIntent().getExtras();
        setContentView(R.layout.activity_payment_page);
        cost=bd.getInt("amount");
        setcost=(TextView)findViewById(R.id.payamout);
        setcost.setText("Rs."+cost);
        type=bd.getString("type");
        order=(Button)findViewById(R.id.order);
        codmenu=(LinearLayout)findViewById(R.id.layoutcod);
        creditmenu=(LinearLayout)findViewById(R.id.layoutcredit);
        paytype=(RadioGroup)findViewById(R.id.payment);
        mDatabaseCart= FirebaseDatabase.getInstance().getReference();
        cod=(RadioButton)findViewById(R.id.cod);
        credit=(RadioButton)findViewById(R.id.credit);
        paytype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.cod) {
                    codmenu.setVisibility(View.VISIBLE);
                    creditmenu.setVisibility(View.GONE);
                    paying=true;
                }
                else if(checkedId==R.id.credit) {
                    creditmenu.setVisibility(View.VISIBLE);
                    codmenu.setVisibility(View.GONE);
                    paying=false;
                }
            }
        });
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!paying)
                {
                    Toast.makeText(PaymentPage.this, "Updated Soon...!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(type.equals("gruop")) {
                        mDatabaseCart.child("Cart").removeValue();
                    }
                    final Intent i=new Intent(PaymentPage.this,SuccessfullPlaced.class);
                    i.putExtra("mycost",cost);
                    final ProgressDialog pd;
                    pd = new ProgressDialog(PaymentPage.this);
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
        });
    }

}

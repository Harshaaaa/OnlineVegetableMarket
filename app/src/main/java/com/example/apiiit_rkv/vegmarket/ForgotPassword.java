package com.example.apiiit_rkv.vegmarket;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    EditText ed;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ed=(EditText)findViewById(R.id.forgetmailid);
    }

    public void forgetmailsend(View view) {
        String a=ed.getText().toString();
        if(TextUtils.isEmpty(a))
        {
            Toast.makeText(this, "Please Enter Email id", Toast.LENGTH_SHORT).show();
            return;
        }
        auth= FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(a)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPassword.this, "Verification link was send to your mail", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(ForgotPassword.this, "Please Enter Valid Email id", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

package com.example.apiiit_rkv.vegmarket;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Registration extends AppCompatActivity {
    EditText username, email;
    TextInputLayout password, confirm;
    private FirebaseAuth firebase;
    ProgressDialog progressDialog;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        auth = FirebaseAuth.getInstance();
        firebase = FirebaseAuth.getInstance();
        username = (EditText) findViewById(R.id.username);
        password = (TextInputLayout) findViewById(R.id.pass);
        progressDialog = new ProgressDialog(this);
        confirm = (TextInputLayout) findViewById(R.id.pass2);
        email = (EditText) findViewById(R.id.email);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void regtofire(View view) {
        final String usser = username.getText().toString().trim();
        String pass = password.getEditText().getText().toString().trim();
        String mail = email.getText().toString().trim();
        String pass2 = confirm.getEditText().getText().toString().trim();
        if (TextUtils.isEmpty(usser) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(mail) || TextUtils.isEmpty(pass2)) {
            Toast.makeText(this, "Please Fill ALL Fields", Toast.LENGTH_SHORT).show();
            return;
        } else if (!(pass.equals(pass2))) {
            Toast.makeText(this, "Passwords Doesn't Match..!", Toast.LENGTH_SHORT).show();
            return;
        } else if (pass.length() <= 8) {
            Toast.makeText(this, "Password may contain atleast 8 characters..!", Toast.LENGTH_SHORT).show();
        }
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "Please Enable Internet Connection", Toast.LENGTH_SHORT).show();
            return;
        } else {
            progressDialog.setMessage("Creating User.....");
            progressDialog.show();
            progressDialog.setCancelable(false);
            firebase.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();
                    if (!task.isSuccessful()) {
                        Toast.makeText(Registration.this, "Account Not Created", Toast.LENGTH_SHORT).show();
                    } else {
                        setprofilename();
                        sendVerificationEmail();

                    }
                }
            });
        }

    }
    private void setprofilename() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username.getText().toString()).build();
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                        }
                    }
                });
    }

    private void sendVerificationEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            new CountDownTimer(2500,1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {
                                    FirebaseAuth.getInstance().signOut();
                                    startActivity(new Intent(Registration.this, Login.class));
                                    finish();
                                    Toast.makeText(Registration.this, "Click on the verification link send to your mail", Toast.LENGTH_SHORT).show();
                                }
                            }.start();
                        }
                        else
                        {
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                        }
                    }
                });
    }
}
package com.example.apiiit_rkv.vegmarket;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    FirebaseAuth firebaseUser;
    FirebaseUser fireuser;
    EditText ed;
    TextInputLayout textInputLayout;
    CheckBox remember;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreference;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        getSupportActionBar().setTitle("Login To VegetableMarket");
        sharedPreference=getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        editor=sharedPreference.edit();
        remember=(CheckBox)findViewById(R.id.remembernames);
        ed=(EditText)findViewById(R.id.eemail);
        textInputLayout=(TextInputLayout)findViewById(R.id.epass);
        firebaseUser=FirebaseAuth.getInstance();
        fireuser=firebaseUser.getCurrentUser();
        progressDialog=new ProgressDialog(this);
        ed.setText(sharedPreference.getString("username",""));
        textInputLayout.getEditText().setText(sharedPreference.getString("password",""));
        if(firebaseUser.getCurrentUser()!=null)
        {
            checkIfEmailVerified();
        }
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setMessage("Are you sure want to Exit the App?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);
            }
        }).setNegativeButton("No", null).show();
    }

    public void LoginTOVegMarket(View view) {
        if(!isNetworkAvailable())
        {
            Toast.makeText(this, "Please Enable Internet Connection", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Loging You In.....");
        String name=ed.getText().toString().trim();
        String pass=textInputLayout.getEditText().getText().toString().trim();
        if(TextUtils.isEmpty(name)||TextUtils.isEmpty(pass))
        {
            Toast.makeText(this, "Please Enter mail and password", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            if(remember.isChecked())
            {
                editor.putString("username",name);
                editor.putString("password",pass);
                editor.apply();
            }
            progressDialog.show();
            progressDialog.setCancelable(false);
            firebaseUser.signInWithEmailAndPassword(name, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //Log.d("TAG", "signInWithEmail:onComplete:" + task.isSuccessful());
                            progressDialog.dismiss();
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(Login.this, "Sigin Falied", Toast.LENGTH_SHORT).show();

                            } else {
                                checkIfEmailVerified();
                            }
                            // ...
                        }
                    });
        }

    }
    private void checkIfEmailVerified()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified())
        {
            Toast.makeText(Login.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,VegetableMarket.class));
        }
        else
        {
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            Toast.makeText(this, "Please Verify Your Mail", Toast.LENGTH_SHORT).show();
            sendVerificationEmail();
            FirebaseAuth.getInstance().signOut();

            //restart this activity

        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private void sendVerificationEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Sended to your mailid", Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();
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

    public void createAcoount(View view) {
        startActivity(new Intent(this,Registration.class));
    }
    public void navtoforgot(View view) {
        startActivity(new Intent(this,ForgotPassword.class));
    }

}

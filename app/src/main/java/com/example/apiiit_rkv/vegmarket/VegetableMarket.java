package com.example.apiiit_rkv.vegmarket;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class VegetableMarket extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseAuth auth;
    EditText search;
    // Creating DatabaseReference.
    DatabaseReference databaseReference,atabaseReference;

    // Creating RecyclerView.
    RecyclerView recyclerView;

    // Creating RecyclerView.Adapter.
    RecyclerView.Adapter adapter ;
    TextView name,email;
    // Creating Progress dialog
    ProgressDialog progressDialog,pd;
    private LinearLayoutManager mLayoutManager;
    List<ImageUploadInfo> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vegetable_market);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        search=(EditText)findViewById(R.id.search);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        auth=FirebaseAuth.getInstance();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
/*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
        name = (TextView)header.findViewById(R.id.setusername);
        email = (TextView)header.findViewById(R.id.setemail);
        name.setText(auth.getCurrentUser().getDisplayName()+"");
        email.setText(auth.getCurrentUser().getEmail()+"");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // Setting RecyclerView size true.
        recyclerView.setHasFixedSize(true);

        // Setting RecyclerView layout as LinearLayout.
        recyclerView.setLayoutManager(new LinearLayoutManager(VegetableMarket.this));
        progressDialog = new ProgressDialog(VegetableMarket.this);

        // Setting up message in Progress dialog.
        progressDialog.setMessage("Vegetables Loading Please Wait....");

        // Showing progress dialog.
        progressDialog.show();

        // Setting up Firebase image upload folder path in databaseReference.
        databaseReference = FirebaseDatabase.getInstance().getReference().child("/VegetableMarket").child("/List");

        // Adding Add Value Event Listener to databaseReference.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    ImageUploadInfo imageUploadInfo = postSnapshot.getValue(ImageUploadInfo.class);

                    list.add(imageUploadInfo);
                }
                mLayoutManager = new LinearLayoutManager(VegetableMarket.this);
                mLayoutManager.setReverseLayout(true);
                mLayoutManager.setStackFromEnd(true);
                recyclerView.setLayoutManager(mLayoutManager);
                adapter = new RecyclerViewAdapter(getApplicationContext(), list);

                recyclerView.setAdapter(adapter);

                // Hiding the progress dialog.
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                // Hiding the progress dialog.
                progressDialog.dismiss();

            }
        });
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.vegetable_market, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id==R.id.cart)
        {
            startActivity(new Intent(this,Cart.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_offer) {
            startActivity(new Intent(VegetableMarket.this,NewOffers.class));

        } else if (id == R.id.nav_cart) {
            pd = new ProgressDialog(VegetableMarket.this);
            pd.setMessage("Cart Loading Please Wait....");
            pd.show();
            new CountDownTimer(1000,800) {
                @Override
                public void onTick(long millisUntilFinished) {
                }
                @Override
                public void onFinish() {
                    pd.dismiss();
                    startActivity(new Intent(VegetableMarket.this,Cart.class));
                }
            }.start();
        } else if (id == R.id.nav_faq) {
            pd = new ProgressDialog(VegetableMarket.this);
            pd.setMessage("FAQ Loading Please Wait....");
            pd.show();
            new CountDownTimer(1000,800) {
                @Override
                public void onTick(long millisUntilFinished) {
                }
                @Override
                public void onFinish() {
                    pd.dismiss();
                    startActivity(new Intent(VegetableMarket.this,FAQs.class));
                }
            }.start();
        } else if (id == R.id.nav_help) {
            pd = new ProgressDialog(VegetableMarket.this);
            pd.setMessage("Help Loading Please Wait....");
            pd.show();
            new CountDownTimer(1000,800) {
                @Override
                public void onTick(long millisUntilFinished) {
                }
                @Override
                public void onFinish() {
                    pd.dismiss();
                    startActivity(new Intent(VegetableMarket.this,ContactUS.class));
                }
            }.start();

        } else if (id == R.id.nav_logout) {
            auth.signOut();
            pd = new ProgressDialog(VegetableMarket.this);
            pd.setMessage("Loging out....");
            pd.show();
            new CountDownTimer(1000,800) {
                @Override
                public void onTick(long millisUntilFinished) {
                }
                @Override
                public void onFinish() {
                    pd.dismiss();
                    startActivity(new Intent(VegetableMarket.this,Login.class));
                }
            }.start();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void searchItem(View view) {
        String str=search.getText().toString();
        atabaseReference = FirebaseDatabase.getInstance().getReference().child("Keyy");
        atabaseReference.setValue(str);
        Bundle bundle = new Bundle();
        bundle.putString("edttext", "From Activity");
        final Intent i=new Intent(this,SearchItems.class);
        i.putExtra("str",str);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        search.setText("");
    }
}

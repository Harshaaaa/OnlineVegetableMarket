package com.example.apiiit_rkv.vegmarket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchItems extends AppCompatActivity {
    Bundle bd;
    // Creating DatabaseReference.
    FirebaseAuth auth;
    // Creating DatabaseReference.
    DatabaseReference databaseReference,atabaseReference;

    // Creating RecyclerView.
    RecyclerView recyclerView;

    // Creating RecyclerView.Adapter.
    RecyclerView.Adapter adapter ;

    // Creating Progress dialog
    ProgressDialog progressDialog;
    private LinearLayoutManager mLayoutManager;
    List<SearchItemsInfo> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_items);
        bd=getIntent().getExtras();
        auth=FirebaseAuth.getInstance();
        auth=FirebaseAuth.getInstance();
        recyclerView = (RecyclerView) findViewById(R.id.MyrecyclerView);

        // Setting RecyclerView size true.
        recyclerView.setHasFixedSize(true);

        // Setting RecyclerView layout as LinearLayout.
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchItems.this));
        progressDialog = new ProgressDialog(SearchItems.this);

        // Setting up message in Progress dialog.
        progressDialog.setMessage("Vegetables Loading Please Wait....");

        // Showing progress dialog.
        progressDialog.show();

        // Setting up Firebase image upload folder path in databaseReference.
        databaseReference = FirebaseDatabase.getInstance().getReference().child("/VegetableMarket").child("/List");

        // Adding Add Value Event Listener to databaseReference.

        Query query=databaseReference.orderByChild("itemtitle").equalTo( bd.getString("str")+"");
        query.addValueEventListener(new ValueEventListener() {
            @Override            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    SearchItemsInfo imageUploadInfo = postSnapshot.getValue(SearchItemsInfo.class);
                    list.add(imageUploadInfo);
                }
                mLayoutManager = new LinearLayoutManager(SearchItems.this);
                mLayoutManager.setReverseLayout(true);
                mLayoutManager.setStackFromEnd(true);
                recyclerView.setLayoutManager(mLayoutManager);
                adapter = new SearchItemsAdapter(getApplicationContext(), list);

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


    }
    public String getmykey()
    {
        return bd.getString("str")+"";
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,VegetableMarket.class));
    }
}

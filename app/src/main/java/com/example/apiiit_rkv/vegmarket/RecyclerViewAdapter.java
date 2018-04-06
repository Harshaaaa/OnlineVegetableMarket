package com.example.apiiit_rkv.vegmarket;


/**
 * Created by Busem Kumar on 2/16/2018.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by AndroidJSon.com on 6/18/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context,mycontext;
    List<ImageUploadInfo> MainImageUploadInfoList;
    View view_one;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference databaseReference;
    DatabaseReference mDatabaseCart;
    private boolean mProcessCart=false;
    ProgressDialog progressDialog,pd;
    public RecyclerViewAdapter(Context context, List<ImageUploadInfo> TempList) {

        this.MainImageUploadInfoList = TempList;

        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vegitems_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ImageUploadInfo UploadInfo = MainImageUploadInfoList.get(position);
        Glide.with(context).load(UploadInfo.getItemurl()).into(holder.imageView);
        holder.title.setText(UploadInfo.getItemtitle());
        holder.cost.setText("Rs."+UploadInfo.getItemcost());
        holder.itemquntity.setText(UploadInfo.getItemname());
        final String bee=UploadInfo.getKey();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabaseCart= FirebaseDatabase.getInstance().getReference("/Cart");
        mDatabaseCart.keepSynced(true);
        final DatabaseReference myRef = database.getReference().child("/Cart");
        final  DatabaseReference mynew=database.getReference().child("/SingItemBuy");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap: dataSnapshot.getChildren()) {
                    if (bee.equals(snap.getKey()))
                    {
                        mProcessCart=true;
                    }

                }
                if(mProcessCart)
                {
                    holder.cart.setBackgroundResource(R.drawable.full);

                }
                else
                {
                    holder.cart.setBackgroundResource(R.drawable.empty);
                }
                mProcessCart=false;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mProcessCart=false;
        holder.buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i=holder.getAdapterPosition();
                final ImageUploadInfo up=MainImageUploadInfoList.get(i);
                final Intent intent=new Intent(mycontext,SingleItemBuy.class);
                intent.putExtra("title",up.getItemtitle());
                intent.putExtra("url",up.getItemurl());
                intent.putExtra("name",up.getItemname());
                String costt=up.getItemcost()+"";
                intent.putExtra("cost",costt);
                pd = new ProgressDialog(mycontext);
                pd.setMessage("Loading Please Wait....");
                pd.show();
                new CountDownTimer(1000,800) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }
                    @Override
                    public void onFinish() {
                        pd.dismiss();
                        mycontext.startActivity(intent);
                    }
                }.start();

            }
        });
        holder.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i=holder.getAdapterPosition();
                final ImageUploadInfo up=MainImageUploadInfoList.get(i);
                final String key = up.getKey();
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snap: dataSnapshot.getChildren()) {
                            if (key.equals(snap.getKey()))
                            {
                                mProcessCart=true;
                            }

                        }
                        if(mProcessCart)
                        {
                            Toast.makeText(context, "Removed From Cart", Toast.LENGTH_SHORT).show();
                            mDatabaseCart.child(key).child("itemcost").removeValue();
                            mDatabaseCart.child(key).child("itemname").removeValue();
                            mDatabaseCart.child(key).child("itemurl").removeValue();
                            mDatabaseCart.child(key).child("itemtitle").removeValue();
                            mDatabaseCart.child(key).child("key").removeValue();
                            holder.cart.setBackgroundResource(R.drawable.empty);

                        }
                        else
                        {
                            Toast.makeText(context, "Added To Cart SucessFull", Toast.LENGTH_SHORT).show();
                            mDatabaseCart.child(key).child("itemcost").setValue(up.getItemcost());
                            mDatabaseCart.child(key).child("itemname").setValue(up.getItemname());
                            mDatabaseCart.child(key).child("itemurl").setValue(up.getItemurl());
                            mDatabaseCart.child(key).child("itemtitle").setValue(up.getItemtitle());
                            mDatabaseCart.child(key).child("key").setValue(up.getKey());
                            holder.cart.setBackgroundResource(R.drawable.full);
                        }
                        mProcessCart=false;
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                mProcessCart=false;
            }
        });
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
    }

    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView title,itemquntity,cost;
        Button buy,cart;
        public ViewHolder(View itemView) {
            super(itemView);
            view_one=itemView;
            mycontext=itemView.getContext();

            imageView = (ImageView) itemView.findViewById(R.id.item_image);
            title=(TextView)itemView.findViewById(R.id.item_title);
            itemquntity=(TextView)itemView.findViewById(R.id.item_description);
            cost=(TextView)itemView.findViewById(R.id.itemcost);
            buy=(Button)itemView.findViewById(R.id.itembuy);
            cart=(Button)itemView.findViewById(R.id.itemcart);
    }
}}
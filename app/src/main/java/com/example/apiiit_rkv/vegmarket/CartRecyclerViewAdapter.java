package com.example.apiiit_rkv.vegmarket;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by apiiit-rkv on 2/4/18.
 */
public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.ViewHolder> {

    Context context,mycontext;
    List<CartUploadInfo> MainImageUploadInfoList;
    View view_one;
    DatabaseReference mDatabaseCart;
    int finalcost=0;
    public CartRecyclerViewAdapter(Context context, List<CartUploadInfo> TempList) {

        this.MainImageUploadInfoList = TempList;

        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        CartUploadInfo UploadInfo = MainImageUploadInfoList.get(position);
        Glide.with(context).load(UploadInfo.getItemurl()).into(holder.imageView);
        holder.title.setText(UploadInfo.getItemtitle());
        holder.cost.setText("Rs."+UploadInfo.getItemcost());
        mDatabaseCart= FirebaseDatabase.getInstance().getReference("/Cart");
        holder.itemquntity.setText(UploadInfo.getItemname());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i=holder.getAdapterPosition();
                final CartUploadInfo up=MainImageUploadInfoList.get(i);
                final String key = up.getKey();
                mDatabaseCart.child(key).child("itemcost").removeValue();
                mDatabaseCart.child(key).child("itemname").removeValue();
                mDatabaseCart.child(key).child("itemurl").removeValue();
                mDatabaseCart.child(key).child("itemtitle").removeValue();
                mDatabaseCart.child(key).child("key").removeValue();
                mycontext.startActivity(new Intent(mycontext,Cart.class));
            }
        });
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i=holder.getAdapterPosition();
                final CartUploadInfo up=MainImageUploadInfoList.get(i);
                final int cost = up.getItemcost();
                String itemno=holder.data.getText().toString();
                int im=Integer.parseInt(itemno);
                im++;
                holder.data.setText(im+"");
                finalcost=finalcost-holder.prefinal;
                int pre=cost*im;
                finalcost=finalcost+pre;
                holder.prefinal=pre;
            }
        });
        if(holder.getAdapterPosition()==0)
        {
            holder.orderbutton.setVisibility(View.VISIBLE);
        }
        holder.orderbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mycontext,Address.class);
                i.putExtra("amount",finalcost);
                i.putExtra("type","gruop");
                if(finalcost>0)
                mycontext.startActivity(i);
                else
                    Toast.makeText(context, "No Items Placed Choose Items", Toast.LENGTH_SHORT).show();
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i=holder.getAdapterPosition();
                final CartUploadInfo up=MainImageUploadInfoList.get(i);
                final int cost = up.getItemcost();
                String itemno=holder.data.getText().toString();
                int im=Integer.parseInt(itemno);
                if(im>0)
                   im--;
                holder.data.setText(im+"");
                if(finalcost>0)
                finalcost=finalcost-cost;
                Toast.makeText(context, finalcost+"", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView title,itemquntity,cost;
        Button delete,minus,plus,orderbutton;
        TextView data;
        int prefinal=0;
        int preefinal=0;
        public ViewHolder(View itemView) {
            super(itemView);
            view_one=itemView;
            mycontext=itemView.getContext();
            delete=(Button)itemView.findViewById(R.id.delete);
            imageView = (ImageView) itemView.findViewById(R.id.item_image);
            title=(TextView)itemView.findViewById(R.id.item_title);
            minus=(Button)itemView.findViewById(R.id.minus);
            plus=(Button)itemView.findViewById(R.id.plus);
            orderbutton=(Button)itemView.findViewById(R.id.orderbutton);
            data=(TextView) itemView.findViewById(R.id.data);
            itemquntity=(TextView)itemView.findViewById(R.id.item_description);
            cost=(TextView)itemView.findViewById(R.id.itemcost);
        }
    }}
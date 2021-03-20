package com.example.thuctaptotnghiep.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.thuctaptotnghiep.Object.TinTuc;
import com.example.thuctaptotnghiep.R;
import com.example.thuctaptotnghiep.inteface.IClickTinTucListener;

import java.util.List;

public class TinTucAdapter extends RecyclerView.Adapter<TinTucAdapter.TinTucViewHolder>{
    List<TinTuc> tinTucList;
    Context mcontext;
    IClickTinTucListener iClickTinTucListener;

    public TinTucAdapter(List<TinTuc> tinTucList, Context mcontext,IClickTinTucListener iClickTinTucListener) {
        this.tinTucList = tinTucList;
        this.mcontext = mcontext;
        this.iClickTinTucListener=iClickTinTucListener;
    }

    @NonNull
    @Override
    public TinTucViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tintuc,parent,false);

        return new TinTucViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TinTucViewHolder holder, int position) {

        TinTuc tinTuc=tinTucList.get(position);
        holder.txt_title.setText(tinTuc.getTen());
        Glide.with(mcontext).load(tinTuc.getHinhanh()).into(holder.img);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickTinTucListener.onClickTinTucItem(tinTuc);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(tinTucList!=null){
            return tinTucList.size();
        }
        return 0;
    }

    public class TinTucViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txt_title;
        public TinTucViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.image_itemtintuc);
            txt_title=itemView.findViewById(R.id.txt_title_itemtintuc);
        }
    }
}

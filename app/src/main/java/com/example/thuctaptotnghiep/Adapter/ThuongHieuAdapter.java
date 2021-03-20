package com.example.thuctaptotnghiep.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.thuctaptotnghiep.Object.Product;
import com.example.thuctaptotnghiep.Object.ThuongHieu;
import com.example.thuctaptotnghiep.R;
import com.example.thuctaptotnghiep.inteface.IClickThuongHieuListener;

import java.util.List;

public class ThuongHieuAdapter extends RecyclerView.Adapter<ThuongHieuAdapter.ThuongHieuViewHolder> {
    private List<ThuongHieu> dsThuongHieu;
    private Context mcontext;
    IClickThuongHieuListener mIClickThuongHieuListener;

    public ThuongHieuAdapter(List<ThuongHieu> list, Context context,IClickThuongHieuListener iClickThuongHieuListener) {
        this.dsThuongHieu=list;
        this.mcontext=context;
        this.mIClickThuongHieuListener=iClickThuongHieuListener;
    }


    @NonNull
    @Override
    public ThuongHieuAdapter.ThuongHieuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thuonghieu,parent,false);
        return new ThuongHieuAdapter.ThuongHieuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThuongHieuAdapter.ThuongHieuViewHolder holder, int position) {

        ThuongHieu p=dsThuongHieu.get(position);
        holder.txtname.setText(p.name);
        Glide.with(mcontext).load(p.img).into(holder.img);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIClickThuongHieuListener.onClickThuongHieu(p);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(dsThuongHieu!=null){
            return dsThuongHieu.size();
        }
        return 0;
    }

    public class ThuongHieuViewHolder extends RecyclerView.ViewHolder{

        private ImageView img;
        private TextView txtname;
        private LinearLayout linearLayout;


        public ThuongHieuViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.img_thuonghieu);
            txtname=itemView.findViewById(R.id.txt_tenthuonghieu);
            linearLayout=itemView.findViewById(R.id.linerlayout_thuonghieu);
        }
    }
}

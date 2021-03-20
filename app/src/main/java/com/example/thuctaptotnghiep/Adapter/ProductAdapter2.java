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
import com.example.thuctaptotnghiep.R;
import com.example.thuctaptotnghiep.inteface.IClickItemListener;

import java.util.List;

public class ProductAdapter2 extends RecyclerView.Adapter<ProductAdapter2.ProductViewHolder>{
    private List<Product> dsproduct;
    private Context mcontext;
    IClickItemListener mIClickItemListener;

    public ProductAdapter2(List<Product> listproduct, Context context,IClickItemListener iClickItemListener) {
        this.dsproduct=listproduct;
        this.mcontext=context;
        this.mIClickItemListener=iClickItemListener;
    }


    @NonNull
    @Override
    public ProductAdapter2.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_2,parent,false);
        return new ProductAdapter2.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter2.ProductViewHolder holder, int position) {


        final Product p=dsproduct.get(position);
        holder.txttitle.setText(p.title);
        holder.txtgia.setText(" "+p.price);
        Glide.with(mcontext).load(p.image).into(holder.img);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIClickItemListener.onClickItemProduct(p);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(dsproduct!=null){
            return dsproduct.size();
        }
        return 0;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{

        private ImageView img;
        private TextView txttitle,txtgia;
        private LinearLayout linearLayout;


        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.imgproduct2);
            txttitle=itemView.findViewById(R.id.txttitle2);
            txtgia=itemView.findViewById(R.id.txtgia2);
            linearLayout=itemView.findViewById(R.id.linerlayout_product2);
        }
    }
}

package com.example.thuctaptotnghiep.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.thuctaptotnghiep.Object.GioHang;
import com.example.thuctaptotnghiep.R;
import com.example.thuctaptotnghiep.Server;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.GioHangViewHolder> {

    private List<GioHang> gioHangList;
    private Context mContext;

    private ViewBinderHelper viewBinderHelper=new ViewBinderHelper();

    public GioHangAdapter(List<GioHang> gioHangList, Context mContext) {
        this.gioHangList = gioHangList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public GioHangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang,parent,false);
        return new GioHangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GioHangViewHolder holder, int position) {

        GioHang gioHang=gioHangList.get(position);
        holder.txt_title.setText(gioHang.getTitle());

        //chuyển đối số sang đơn vị tiền tệ việt nam
        Locale locale=new Locale("vi","VN");
        NumberFormat numberFormat=NumberFormat.getCurrencyInstance(locale);
        String gia=numberFormat.format(gioHang.getPrice());
        holder.txt_gia.setText(gia);
        holder.txt_sl_mua.setText(gioHang.getSoluong_mua()+"");
        Glide.with(mContext).load(gioHang.getImage()).into(holder.img);

        viewBinderHelper.bind(holder.swipeRevealLayout, String.valueOf(gioHang.getId()));
        holder.linearLayout_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest2=new StringRequest(Request.Method.POST, Server.url_deleteitemgiohang, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(mContext, gioHang.getId()+"", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> para=new HashMap<>();
                        para.put("idg", String.valueOf(gioHang.getId()));


                        return para;
                    }
                };
                Volley.newRequestQueue(mContext).add(stringRequest2);

                gioHangList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        if(gioHangList!=null){
            return gioHangList.size();
        }
        return 0;
    }

    public class GioHangViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView txt_title,txt_gia,txt_sl_mua;
        private SwipeRevealLayout swipeRevealLayout;
        private LinearLayout linearLayout_delete;

        public GioHangViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.image_itemgiohang);
            txt_title=itemView.findViewById(R.id.txt_title_itemgiohang);
            txt_gia=itemView.findViewById(R.id.txt_gia_itemgiohang);
            txt_sl_mua=itemView.findViewById(R.id.txt_soluongmua_itemgiohang);

            swipeRevealLayout=itemView.findViewById(R.id.swipeRevealLayout);
            linearLayout_delete=itemView.findViewById(R.id.linerlayout_itemgiohang);
        }
    }
}

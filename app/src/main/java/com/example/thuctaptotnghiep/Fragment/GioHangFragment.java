package com.example.thuctaptotnghiep.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.thuctaptotnghiep.Adapter.GioHangAdapter;
import com.example.thuctaptotnghiep.MainActivity;
import com.example.thuctaptotnghiep.Object.GioHang;
import com.example.thuctaptotnghiep.Object.Product;
import com.example.thuctaptotnghiep.Object.User;
import com.example.thuctaptotnghiep.R;
import com.example.thuctaptotnghiep.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GioHangFragment extends Fragment {

    public static final String TAG=GioHangFragment.class.getName();

    User user;
   // String iduser;

   public static GioHang gioHang;
   public static List<GioHang> listGioHang;

    GioHangAdapter mAdapter;
    RecyclerView recyclerView_giohang;
    ImageView img_back,img_trong_giohang;
    TextView txt_tongtien,txt_trong_giohang;
    Button btn_thanhtoan;
    TextView txttest;

    double tongtien;

    MainActivity mainActivity;

    public static GioHangFragment getInstance(String id){
        GioHangFragment gioHangFragment=new GioHangFragment();
        Bundle bundle=new Bundle();
        //bundle.putSerializable("giohang_object",u);
        bundle.putString("iduser",id);
        gioHangFragment.setArguments(bundle);
        return gioHangFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_giohang,container,false);

        recyclerView_giohang=view.findViewById(R.id.recyclerview_giohang);
        img_back=view.findViewById(R.id.image_back_giohang);
        txt_tongtien=view.findViewById(R.id.txt_tongtien_giohang);
        txt_trong_giohang=view.findViewById(R.id.txt_trong_giohang);
        img_trong_giohang=view.findViewById(R.id.image_trong_giohang);
        btn_thanhtoan=view.findViewById(R.id.btn_thanhtoan_giohang);

        txttest=view.findViewById(R.id.testgiohang);

        mainActivity= (MainActivity) getActivity();
        mainActivity.bottomNavigationView.setVisibility(View.GONE);

        Bundle bundle=getArguments();
       String iduser=bundle.getString("iduser");

//       txttest.setText(iduser);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        recyclerView_giohang.setLayoutManager(linearLayoutManager);
        listGioHang=new ArrayList<>();
        getgiohang(iduser);
        mAdapter=new GioHangAdapter(listGioHang,getContext());
        recyclerView_giohang.setAdapter(mAdapter);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        btn_thanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                HoaDonFragment hoaDonFragment=new HoaDonFragment();
                Bundle bundle=new Bundle();
                bundle.putString("iduser_giohang",iduser);
                hoaDonFragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.frame_layout,hoaDonFragment);
                fragmentTransaction.addToBackStack(DetailTinTucFragment.TAG);
                fragmentTransaction.commit();
            }
        });





        return view;
    }

    private void getgiohang(String id){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Server.url_getgiohang, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int id=0;
                String idproduct="";
                String tendangnhap="";
                String title="";
                double price=0;
                String image="";
                int soluong_mua=0;

                double tien=0;

                try {
                    JSONArray array=new JSONArray(response);
                    for(int i=0;i<array.length();i++){
                        JSONObject object=array.getJSONObject(i);

                        id=object.getInt("idg");
                        idproduct=object.getString("idproduct");
                        tendangnhap=object.getString("tendangnhap");
                        title=object.getString("title");
                        price=object.getDouble("price");
                        image=object.getString("image");
                        soluong_mua=object.getInt("soluongmua");

                         tien=soluong_mua*price;


//                        //chuyển đối số sang đơn vị tiền tệ việt nam
//                        Locale locale=new Locale("vi","VN");
//                        NumberFormat numberFormat=NumberFormat.getCurrencyInstance(locale);
//                        String gia=numberFormat.format(price);

                        gioHang=new GioHang(id,idproduct,tendangnhap,title,price,image,soluong_mua);
                        listGioHang.add(gioHang);
                        mAdapter.notifyDataSetChanged();

                    }


                    if(listGioHang.size()<=0){
                        img_trong_giohang.setVisibility(View.VISIBLE);
                        txt_trong_giohang.setVisibility(View.VISIBLE);
                        txt_trong_giohang.setText("không có sản phẩm nào!!!");
                    }

                    for (int i=0;i<listGioHang.size();i++){
                        tongtien += listGioHang.get(i).getSoluong_mua()*listGioHang.get(i).getPrice();
                    }
                    //txttest.setText(listGioHang.size()+"");

                    //chuyển đối số sang đơn vị tiền tệ việt nam
                    Locale locale=new Locale("vi","VN");
                    NumberFormat numberFormat=NumberFormat.getCurrencyInstance(locale);
                    String gia=numberFormat.format(tongtien);
                    txt_tongtien.setText(gia);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("iduser",id);
                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

}

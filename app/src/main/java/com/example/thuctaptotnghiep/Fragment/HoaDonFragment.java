package com.example.thuctaptotnghiep.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.thuctaptotnghiep.MainActivity;
import com.example.thuctaptotnghiep.Object.GioHang;
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

public class HoaDonFragment extends Fragment {

    public static GioHang gioHang;
    public static List<GioHang> listGioHang;
    EditText ed_hoten,ed_sdt,ed_email,ed_diachinhan;
    Button btn_dathang;
    ImageView img_back;

    MainActivity mainActivity;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_hoadon,container,false);

        ed_hoten=view.findViewById(R.id.ed_hoten_fragmenthoadon);
        ed_sdt=view.findViewById(R.id.ed_sdt_fragmenthoadon);
        ed_email=view.findViewById(R.id.ed_email_fragmenthoadon);
        ed_diachinhan=view.findViewById(R.id.ed_diachinhan_fragmenthoadon);
        btn_dathang=view.findViewById(R.id.btn_dathang_fragmenthoadon);
        img_back=view.findViewById(R.id.image_back_hoadon);

        mainActivity= (MainActivity) getActivity();
        mainActivity.bottomNavigationView.setVisibility(View.GONE);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        listGioHang=new ArrayList<>();
        Bundle bundle=getArguments();


        btn_dathang.setOnClickListener(new View.OnClickListener() {




            @Override
            public void onClick(View v) {
                String iduser=bundle.getString("iduser_giohang");
                String hoten=ed_hoten.getText().toString();
                String sdt=ed_sdt.getText().toString();
                String email=ed_email.getText().toString();
                String diachinhan=ed_diachinhan.getText().toString();
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

                                gioHang=new GioHang(id,idproduct,tendangnhap,title,price,image,soluong_mua);
                                listGioHang.add(gioHang);

                            }

                            for (int i=0;i<listGioHang.size();i++){

                                String idproductgiohang=listGioHang.get(i).getIdproduct();
                                String slmua= String.valueOf(listGioHang.get(i).getSoluong_mua());
                                Toast.makeText(getContext(),idproductgiohang, Toast.LENGTH_SHORT).show();

                                StringRequest stringRequest1=new StringRequest(Request.Method.POST, Server.url_inserthoadon, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if(response.equals("insert successfully")){
                                            StringRequest stringRequest2=new StringRequest(Request.Method.POST, Server.url_deletegiohang, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {

                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {

                                                }
                                            }){
                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    Map<String,String> para=new HashMap<>();
                                                    para.put("iduser",iduser);
                                                    para.put("idproduct",idproductgiohang);

                                                    return para;
                                                }
                                            };
                                            Volley.newRequestQueue(getContext()).add(stringRequest2);

                                            //Toast.makeText(getContext(), "đã thêm vào hoa đơn", Toast.LENGTH_SHORT).show();
                                            FragmentTransaction transaction=getFragmentManager().beginTransaction();
                                            transaction.replace(R.id.frame_layout,GioHangFragment.getInstance(iduser));
                                            transaction.addToBackStack(null);
                                            transaction.commit();
                                        }

                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }){
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String,String> param=new HashMap<>();
                                        param.put("iduser", iduser);
                                        param.put("idproduct",idproductgiohang);
                                        param.put("slmua",slmua);
                                        param.put("hoten",hoten);
                                        param.put("sdt",sdt);
                                        param.put("email",email);
                                        param.put("diachinhan",diachinhan);

                                        return param;
                                    }
                                };
                                Volley.newRequestQueue(getContext()).add(stringRequest1);


                            }



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
                        params.put("iduser",iduser);
                        return params;
                    }
                };
                Volley.newRequestQueue(getContext()).add(stringRequest);
            }
        });



        return view;
    }


}

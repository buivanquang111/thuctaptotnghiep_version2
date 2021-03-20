package com.example.thuctaptotnghiep.Fragment;

import android.os.Bundle;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.thuctaptotnghiep.LoginActivity;
import com.example.thuctaptotnghiep.MainActivity;
import com.example.thuctaptotnghiep.Object.Product;
import com.example.thuctaptotnghiep.Object.User;
import com.example.thuctaptotnghiep.Object.YeuThich;
import com.example.thuctaptotnghiep.R;
import com.example.thuctaptotnghiep.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailFragment extends Fragment {

    User user;
    Product product;
    YeuThich yeuThich;
    List<YeuThich> yeuThichList;
    public static final  String TAG=DetailFragment.class.getName();
    ImageView img_back,img_detailproduct,img_tym_trang,img_tym_do;
    TextView txt_deatailprice,txt_detaildescription,txt_detailtitle;
    //Button btnmuahang;
    ImageView btnmuahang;
    String ten;
    String pass;

    MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_detail,container,false);

        mainActivity= (MainActivity) getActivity();
        mainActivity.bottomNavigationView.setVisibility(View.GONE);

        user=LoginActivity.user;

        img_back=view.findViewById(R.id.image_backDetail);
        img_detailproduct=view.findViewById(R.id.image_product_detail);
        txt_deatailprice=view.findViewById(R.id.txt_giaDetail);
        txt_detaildescription=view.findViewById(R.id.txt_detalproduct);
        txt_detailtitle=view.findViewById(R.id.txt_detailtitle);
        btnmuahang=view.findViewById(R.id.btn_muahang_detail);
        img_tym_trang=view.findViewById(R.id.tym_trang);
        img_tym_do=view.findViewById(R.id.tym_do);

        yeuThichList=new ArrayList<>();
        Bundle bundle = getArguments();

        if(LoginActivity.user==null){
            if (bundle != null) {
                product = (Product) bundle.get("object_product");
                if (product != null) {
                    txt_detailtitle.setText(product.getTitle());
                    txt_deatailprice.setText(product.getPrice());
                    txt_detaildescription.setText(product.getDescription());
                    Glide.with(getContext()).load(product.getImage()).into(img_detailproduct);
                }

            }
            img_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getFragmentManager() != null) {
                        getFragmentManager().popBackStack();
                    }

                }
            });

            img_tym_trang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mainActivity, "bạn cần đăng nhập để thả tym sản phẩm", Toast.LENGTH_SHORT).show();
                }
            });
            btnmuahang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mainActivity, "bạn cần đăng nhập thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                }
            });

        }
        else {


            //lấy tên của khách hàng
            ten=user.getTendangnhap();
            pass=user.getPassword();

            if (bundle != null) {
                product = (Product) bundle.get("object_product");
                if (product != null) {
                    txt_detailtitle.setText(product.getTitle());
                    txt_deatailprice.setText(product.getPrice());
                    txt_detaildescription.setText(product.getDescription());
                    Glide.with(getContext()).load(product.getImage()).into(img_detailproduct);
                }

            }
            getUser(ten, pass);

            img_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getFragmentManager() != null) {
                        getFragmentManager().popBackStack();
                    }

                }
            });


            img_tym_trang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (LoginActivity.user == null) {
                        Toast.makeText(mainActivity, "bạn cần đăng nhập để tym", Toast.LENGTH_SHORT).show();
                    } else {
                        img_tym_do.setVisibility(View.VISIBLE);
                        img_tym_trang.setVisibility(View.INVISIBLE);
                        insertYeuThich(String.valueOf(user.getId()), String.valueOf(product.getId()));
                    }


                }
            });
            img_tym_do.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (LoginActivity.user == null) {
                        Toast.makeText(mainActivity, "bạn cần đăng nhập", Toast.LENGTH_SHORT).show();
                    } else {
                        img_tym_trang.setVisibility(View.VISIBLE);
                        img_tym_do.setVisibility(View.INVISIBLE);
                        deleteYeuThich(String.valueOf(user.getId()), String.valueOf(product.getId()));
                    }
                }
            });


            //show dialog khi click button
            btnmuahang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClickShowBottomSheetDialogFragment();
                }
            });
        }

        return view;
    }

    private void ClickShowBottomSheetDialogFragment() {
        MyBottomSheetFragment myBottomSheetFragment=MyBottomSheetFragment.getInstance(product,user);
        myBottomSheetFragment.show(getFragmentManager(),myBottomSheetFragment.getTag());
    }
    private void getUser(String tendangnhap,String pass) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Server.url_getdetailUser, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int id=0;
                String name="";
                String email="";
                String password="";
                String tendangnhap="";
                String sdt="";

                try {
                    JSONArray array=new JSONArray(response);
                    for(int i=0;i<array.length();i++){
                        JSONObject object=array.getJSONObject(i);

                        id=object.getInt("id");
                        name=object.getString("name");
                        email=object.getString("email");
                        password=object.getString("password");
                        tendangnhap=object.getString("tendangnhap");
                        sdt=object.getString("sdt");

                        //user=new User(id,name,email,password,tendangnhap,sdt);
                    }
                    StringRequest stringRequest=new StringRequest(Request.Method.POST, Server.url_getyeuthich, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Toast.makeText(getContext(), iduser, Toast.LENGTH_SHORT).show();
                            int idtym=0;
                            int iduser=0;
                            int idproduct=0;

                            try {
                                JSONArray array=new JSONArray(response);
                                for(int i=0;i<array.length();i++){
                                    JSONObject object=array.getJSONObject(i);

                                    idtym=object.getInt("idtym");
                                    iduser=object.getInt("iduser");
                                    idproduct=object.getInt("idproduct");

                                    yeuThich=new YeuThich(idtym,iduser,idproduct);
                                    yeuThichList.add(yeuThich);

                                }
                                for (int i=0;i<yeuThichList.size();i++){
                                if( product.getId()==yeuThichList.get(i).getIdproduct()){
                                    img_tym_do.setVisibility(View.VISIBLE);
                                    img_tym_trang.setVisibility(View.INVISIBLE);
                                }
                                else{
                                    img_tym_trang.setVisibility(View.VISIBLE);
                                    img_tym_do.setVisibility(View.INVISIBLE);
                                }
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
                            Map<String,String> para=new HashMap<>();
                            para.put("iduser", String.valueOf(user.getId()));
                            para.put("idproduct",String.valueOf(product.getId()));
                            return para;
                        }
                    };
                    Volley.newRequestQueue(getContext()).add(stringRequest);

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
                params.put("tendangnhap",tendangnhap);
                params.put("pass",pass);
                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    private void insertYeuThich(String iduser,String idproduct){
        StringRequest request = new StringRequest(Request.Method.POST, Server.url_insertyeuthich, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("iduser", iduser);
                params.put("idproduct", idproduct);


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

    private void deleteYeuThich(String iduser,String idproduct){
        StringRequest request = new StringRequest(Request.Method.POST, Server.url_deleteyeuthich, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("iduser", iduser);
                params.put("idproduct", idproduct);


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

    private void getYeuThich(String iduser,String idproduct){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Server.url_getyeuthich, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), iduser, Toast.LENGTH_SHORT).show();
                int idtym=0;
                int iduser=0;
                int idproduct=0;

                try {
                    JSONArray array=new JSONArray(response);
                    for(int i=0;i<array.length();i++){
                        JSONObject object=array.getJSONObject(i);

                        idtym=object.getInt("idtym");
                        iduser=object.getInt("iduser");
                        idproduct=object.getInt("idproduct");

                        yeuThich=new YeuThich(idtym,iduser,idproduct);


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
                Map<String,String> para=new HashMap<>();
                para.put("iduser", iduser);
                para.put("idproduct",idproduct);
                return para;
            }
        };
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}

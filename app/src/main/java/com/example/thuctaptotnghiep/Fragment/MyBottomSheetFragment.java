package com.example.thuctaptotnghiep.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import com.example.thuctaptotnghiep.Object.GioHang;
import com.example.thuctaptotnghiep.Object.Product;
import com.example.thuctaptotnghiep.Object.User;
import com.example.thuctaptotnghiep.R;
import com.example.thuctaptotnghiep.RegisterActivity;
import com.example.thuctaptotnghiep.Server;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MyBottomSheetFragment extends BottomSheetDialogFragment {

    private static final String KEY_OBJECT_BOTTOMSHEET="object_product_bottomsheet";
    private static final String KEY_OBJECT_USER="Oject_user";
    MainActivity mainActivity;

    public User user;
    public Product product;
    //Product p;
    String ten;
    String pass;
    private TextView txt_title,txt_gia,txt_soluong,txttest;
    private ImageView img_close,img_product,img_giam,img_tang;
    private EditText ed_sl;
    private Button btn_dathang;

    String iduser="";
    String idproduct="";
    String str_sl="";
    String soluongmoi="";
    GioHang gioHang;
    public  static List<GioHang> listGioHang;
    BottomSheetDialog bottomSheetDialog;
    ProgressDialog progressDialog;
    int dem=0;
    int check=0;
    int ma;

    public static MyBottomSheetFragment getInstance(Product p,User user){
        MyBottomSheetFragment myBottomSheetFragment=new MyBottomSheetFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable(KEY_OBJECT_BOTTOMSHEET,p);
        bundle.putSerializable(KEY_OBJECT_USER,user);
        myBottomSheetFragment.setArguments(bundle);
        return  myBottomSheetFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle=getArguments();
        if(bundle!=null){
            product= (Product) bundle.get(KEY_OBJECT_BOTTOMSHEET);
            user= (User) bundle.get(KEY_OBJECT_USER);
        }



    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
         bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View view= LayoutInflater.from(getContext()).inflate(R.layout.layout_bottom_sheet_fragment,null);
        bottomSheetDialog.setContentView(view);

        mainActivity= (MainActivity) getActivity();
        //ánh xạ
        txt_title=view.findViewById(R.id.txt_title_bottomsheet_fragment);
        txt_gia=view.findViewById(R.id.txt_gia_bottomsheet_fragment);
        txt_soluong=view.findViewById(R.id.txt_soluong_bottomsheet_fragment);
        img_close=view.findViewById(R.id.image_close_bottomsheet_fragment);
        img_product=view.findViewById(R.id.image_bottomsheet_fragment);
        img_giam=view.findViewById(R.id.image_soluong_giam_bottomsheetfragment);
        img_tang=view.findViewById(R.id.image_soluong_tang_bottomsheetfragment);
        ed_sl=view.findViewById(R.id.ed_soluong_bottomsheetfragment);
        btn_dathang=view.findViewById(R.id.btn_dathang_bottomsheet_fragmnet);

txttest=view.findViewById(R.id.txttest);

        listGioHang=new ArrayList<>();


        //set data
        txt_title.setText(product.getTitle());
        txt_gia.setText(product.getPrice());
        txt_soluong.setText(""+product.getSoluong());
        Glide.with(getContext()).load(product.getImage()).into(img_product);

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });


        img_tang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dem=dem+1;
                ed_sl.setText(""+dem);

                int sl=Integer.parseInt(ed_sl.getText().toString());
                if(sl>1){
                    img_giam.setVisibility(View.VISIBLE);
                }
                else {
                    img_giam.setVisibility(View.INVISIBLE);
                }
                //txttest.setText(ed_sl.getText().toString());


            }
        });

            img_giam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dem=dem-1;
                    ed_sl.setText(""+dem);
                    int sl=Integer.parseInt(ed_sl.getText().toString());
                    if(sl<=1){
                        img_giam.setVisibility(View.INVISIBLE);
                    }
                    else {
                        img_giam.setVisibility(View.VISIBLE);
                    }
                    //txttest.setText(ed_sl.getText().toString());



                }
            });





            //bat su kien dat hang
        btn_dathang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginActivity.user == null) {
                    Intent intent=new Intent(mainActivity,LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    progressDialog = new ProgressDialog(getContext());
                    progressDialog.setTitle("Please wait...");
                    progressDialog.show();

                    iduser = String.valueOf(user.id);
                    idproduct = String.valueOf(product.getId());
                    str_sl = ed_sl.getText().toString();


                    StringRequest request3 = new StringRequest(Request.Method.POST, Server.url_insertGiohang, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                        if (response.trim().equals("registered successfully")) {
//                            progressDialog.dismiss();
//                            bottomSheetDialog.dismiss();
//
//                            //Toast.makeText(getContext(), "đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
//                            chuyenDuLieuSangFragmentGioHang(GioHangFragment.getInstance(iduser));
//
//
//                        }
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.url_getgiohangwithid, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    int id = 0;
                                    String idproducts = "";
                                    String tendangnhap = "";
                                    String title = "";
                                    double price = 0;
                                    String image = "";
                                    int soluong_mua = 0;

                                    double tien = 0;

                                    try {
                                        JSONArray array = new JSONArray(response);
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject object = array.getJSONObject(i);

                                            id = object.getInt("id");
                                            idproducts = object.getString("idproduct");
                                            tendangnhap = object.getString("tendangnhap");
                                            title = object.getString("title");
                                            price = object.getDouble("price");
                                            image = object.getString("image");
                                            soluong_mua = object.getInt("soluongmua");


                                            gioHang = new GioHang(id, idproducts, tendangnhap, title, price, image, soluong_mua);
                                            listGioHang.add(gioHang);
                                        }
                                        int sl = 0;
                                        for (int i = 0; i < listGioHang.size(); i++) {
                                            if (listGioHang.get(i).getIdproduct().equals(idproduct)) {

                                                sl += listGioHang.get(i).getSoluong_mua();
                                                check = check + 1;
                                            }
                                        }
                                        soluongmoi = String.valueOf(sl);
                                        if (check != 1) {
                                            //xoa product di
                                            StringRequest request = new StringRequest(Request.Method.POST, Server.url_deletegiohang, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
//insert lai du lieu vs sl moi
                                                    StringRequest request2 = new StringRequest(Request.Method.POST, Server.url_insertGiohang, new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            if (response.trim().equals("registered successfully")) {
                                                                progressDialog.dismiss();
                                                                bottomSheetDialog.dismiss();

                                                                //Toast.makeText(getContext(), "đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                                                                chuyenDuLieuSangFragmentGioHang(GioHangFragment.getInstance(iduser));


                                                            }


                                                        }
                                                    }, new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            progressDialog.dismiss();

                                                        }
                                                    }
                                                    ) {
                                                        @Override
                                                        protected Map<String, String> getParams() throws AuthFailureError {
                                                            Map<String, String> params = new HashMap<String, String>();

                                                            params.put("iduser", iduser);
                                                            params.put("idproduct", idproduct);
                                                            params.put("soluong", soluongmoi);

                                                            return params;
                                                        }
                                                    };
                                                    RequestQueue requestQueue2 = Volley.newRequestQueue(getContext());
                                                    requestQueue2.add(request2);
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    progressDialog.dismiss();

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
                                            RequestQueue requestQueue1 = Volley.newRequestQueue(getContext());
                                            requestQueue1.add(request);
                                        } else {
                                            progressDialog.dismiss();
                                            bottomSheetDialog.dismiss();

                                            //Toast.makeText(getContext(), "đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                                            chuyenDuLieuSangFragmentGioHang(GioHangFragment.getInstance(iduser));
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
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("iduser", iduser);
                                    params.put("idproduct", idproduct);
                                    return params;
                                }
                            };
                            Volley.newRequestQueue(getContext()).add(stringRequest);
                            //


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();

                        }
                    }
                    ) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();

                            params.put("iduser", iduser);
                            params.put("idproduct", idproduct);
                            params.put("soluong", str_sl);

                            return params;
                        }
                    };
                    RequestQueue requestQueue3 = Volley.newRequestQueue(getContext());
                    requestQueue3.add(request3);

                    //


//                StringRequest request = new StringRequest(Request.Method.POST, Server.url_insertGiohang, new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            if (response.trim().equals("registered successfully")) {
//                                progressDialog.dismiss();
//                                bottomSheetDialog.dismiss();
//
//                                //Toast.makeText(getContext(), "đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
//                                chuyenDuLieuSangFragmentGioHang(GioHangFragment.getInstance(iduser));
//
//
//                            }
//
//
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            progressDialog.dismiss();
//
//                        }
//                    }
//                    ) {
//                        @Override
//                        protected Map<String, String> getParams() throws AuthFailureError {
//                            Map<String, String> params = new HashMap<String, String>();
//
//                            params.put("iduser", iduser);
//                            params.put("idproduct", idproduct);
//                            params.put("soluong", str_sl);
//
//                            return params;
//                        }
//                    };
//                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//                    requestQueue.add(request);

                }
            }


        });







        return bottomSheetDialog;
    }


    private void chuyenDuLieuSangFragmentGioHang(Fragment fragment){
        FragmentTransaction transaction=getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout,fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }


//    private void getgiohang(String id){
//       // final int[] slmoi = {0};
//
//
//        StringRequest stringRequest=new StringRequest(Request.Method.POST, Server.url_getgiohang, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                int id=0;
//                String idproducts="";
//                String tendangnhap="";
//                String title="";
//                double price=0;
//                String image="";
//                int soluong_mua=0;
//
//                double tien=0;
//
//                try {
//                    JSONArray array=new JSONArray(response);
//                    for(int i=0;i<array.length();i++){
//                        JSONObject object=array.getJSONObject(i);
//
//                        id=object.getInt("id");
//                        idproducts=object.getString("idproduct");
//                        tendangnhap=object.getString("tendangnhap");
//                        title=object.getString("title");
//                        price=object.getDouble("price");
//                        image=object.getString("image");
//                        soluong_mua=object.getInt("soluongmua");
//
//
//                        gioHang=new GioHang(id,idproducts,tendangnhap,title,price,image,soluong_mua);
//                        listGioHang.add(gioHang);
//
//
//                    }
////                    int sl=0;
////                    for(int i=0;i<listGioHang.size();i++){
////                        if(listGioHang.get(i).getIdproduct().equals(idproduct)){
////
////                            sl+=listGioHang.get(i).getSoluong_mua();
////                        }
////                    }
////                    slmoi[0] =sl;
////                    //soluongmoi=String.valueOf(sl+Integer.parseInt(str_sl));
////                    Log.d("aaa",soluongmoi);
//
//                    //Toast.makeText(getContext(), slmoi[0]+"", Toast.LENGTH_SHORT).show();
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> params=new HashMap<>();
//                params.put("iduser",id);
//                return params;
//            }
//        };
//        Volley.newRequestQueue(getContext()).add(stringRequest);
//
//
//    }

//    private void updategiohang(String iduser,String idproduct,String soluongmoi){
//        StringRequest stringRequest=new StringRequest(Request.Method.POST, Server.url_updatesoluonggiohang, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                if (response.equals("update successfully")) {
//                    //progressDialog.dismiss();
//                    chuyenDuLieuSangFragmentGioHang(GioHangFragment.getInstance(iduser));
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> params=new HashMap<>();
//                params.put("iduser",iduser);
//                params.put("idproduct",idproduct);
//                params.put("soluongmoi",soluongmoi);
//                return params;
//            }
//        };
//        Volley.newRequestQueue(getContext()).add(stringRequest);
//    }

}

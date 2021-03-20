package com.example.thuctaptotnghiep.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.thuctaptotnghiep.Adapter.PhotoAdapter;
import com.example.thuctaptotnghiep.Adapter.ProductAdapter;
import com.example.thuctaptotnghiep.Adapter.ThuongHieuAdapter;
import com.example.thuctaptotnghiep.LoginActivity;
import com.example.thuctaptotnghiep.MainActivity;
import com.example.thuctaptotnghiep.Object.Photo;
import com.example.thuctaptotnghiep.Object.Product;
import com.example.thuctaptotnghiep.Object.ThuongHieu;
import com.example.thuctaptotnghiep.Object.User;
import com.example.thuctaptotnghiep.R;
import com.example.thuctaptotnghiep.Server;
import com.example.thuctaptotnghiep.inteface.IClickItemListener;
import com.example.thuctaptotnghiep.inteface.IClickThuongHieuListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class HomeFragment extends Fragment {



    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private PhotoAdapter photoAdapter;
    private List<Photo> photoList;
    private Timer mTimer;

    private List<ThuongHieu> listThuongHieu;
    private List<Product> listproduct_nam;
    private List<Product> listproduct_nu;
    private List<Product> listproduct_capdoi;
   private ThuongHieuAdapter tAdapter;
    private ProductAdapter mAdapter;

    private RecyclerView recyclerView_nam;
    private RecyclerView recyclerView_nu;
    private RecyclerView recyclerView_capdoi;
    private RecyclerView recyclerView_thuonghieu;

    private EditText ed_search;
    private LinearLayout linearLayout_search;
    private ImageView img_giohang;
    private TextView txt_sosp_giohang;


    String loai_nam="đồng hồ nam";
    String loai_nu="đồng hồ nữ";
    String loai_capdoi="đồng hồ cặp đôi";

    private MainActivity mMainActivity;
    User user;

    public static HomeFragment getInstance(String ten,String pass){
        HomeFragment homeFragment=new HomeFragment();
        Bundle bundle=new Bundle();
        bundle.putString("ten_home",ten);
        bundle.putString("pass_home",pass);
        homeFragment.setArguments(bundle);
        return  homeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home,container,false);

        mMainActivity= (MainActivity) getActivity();
        viewPager= view.findViewById(R.id.viewpager);
        circleIndicator= view.findViewById(R.id.circler_indi);
        recyclerView_nam= view.findViewById(R.id.product_recylcerview_nam);
        recyclerView_nu=view.findViewById(R.id.product_recylcerview_nu);
        recyclerView_capdoi=view.findViewById(R.id.product_recylcerview_capdoi);
        recyclerView_thuonghieu=view.findViewById(R.id.product_recylcerview_thuonghieu);
        ed_search=view.findViewById(R.id.ed_search);
        //linearLayout_search=view.findViewById(R.id.linerlayout_search);
        img_giohang=view.findViewById(R.id.image_giohang_home);
        txt_sosp_giohang=view.findViewById(R.id.txt_sosp_giohang);


        mMainActivity.bottomNavigationView.setVisibility(View.VISIBLE);




        photoList=getListPhoto();
        photoAdapter=new PhotoAdapter(getContext(),photoList);
        viewPager.setAdapter(photoAdapter);
        circleIndicator.setViewPager(viewPager);
        photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
        autoSlideImage();

        //manager=new GridLayoutManager(getContext(),2);
        LinearLayoutManager manager=new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        recyclerView_nam.setLayoutManager(manager);
        listproduct_nam=new ArrayList<>();
        getProductNam(loai_nam);
        mAdapter=new ProductAdapter(listproduct_nam, getContext(), new IClickItemListener() {
            @Override
            public void onClickItemProduct(Product product) {
                mMainActivity.goToDetailFragment(product);
            }
        });
        recyclerView_nam.setAdapter(mAdapter);


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        recyclerView_nu.setLayoutManager(linearLayoutManager);
        listproduct_nu=new ArrayList<>();
        getProductNu(loai_nu);
        mAdapter=new ProductAdapter(listproduct_nu, getContext(), new IClickItemListener() {
            @Override
            public void onClickItemProduct(Product product) {
                mMainActivity.goToDetailFragment(product);
            }
        });
        recyclerView_nu.setAdapter(mAdapter);

        LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        recyclerView_capdoi.setLayoutManager(linearLayoutManager1);
        listproduct_capdoi=new ArrayList<>();
        getProductCapDoi(loai_capdoi);
        mAdapter=new ProductAdapter(listproduct_capdoi, getContext(), new IClickItemListener() {
            @Override
            public void onClickItemProduct(Product product) {
                mMainActivity.goToDetailFragment(product);
            }
        });
        recyclerView_capdoi.setAdapter(mAdapter);


        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        recyclerView_thuonghieu.setLayoutManager(layoutManager);
        listThuongHieu=new ArrayList<>();
        getThuongHieu();
        tAdapter=new ThuongHieuAdapter(listThuongHieu, getContext(), new IClickThuongHieuListener() {
            @Override
            public void onClickThuongHieu(ThuongHieu thuongHieu) {
                mMainActivity.goToThuongHieuFragment(thuongHieu);
            }
        });
        recyclerView_thuonghieu.setAdapter(tAdapter);

        //chuyển màn hình sang fragment search
//        linearLayout_search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mMainActivity.goToSearchFragment();
//            }
//        });
        ed_search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mMainActivity.goToSearchFragment();
                return false;
            }
        });
        if(LoginActivity.user==null){
            img_giohang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mMainActivity,LoginActivity.class);
                    startActivity(intent);
                }
            });
        }
        else {
            //
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

                            user=new User(id,name,email,password,tendangnhap,sdt);
                        }
                        //set count vao text gio hang
                        StringRequest stringRequest=new StringRequest(Request.Method.POST, Server.url_getCountGioHang, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                int count=0;

                                try {
                                    JSONArray array=new JSONArray(response);
                                    for(int i=0;i<array.length();i++){
                                        JSONObject object=array.getJSONObject(i);

                                        count=object.getInt("count");
                                    }
                                    txt_sosp_giohang.setText(count+"");
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
                                params.put("iduser", String.valueOf(user.getId()));

                                return params;
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
                    params.put("tendangnhap",LoginActivity.user.getTendangnhap());
                    params.put("pass",LoginActivity.user.getPassword());
                    return params;
                }
            };
            Volley.newRequestQueue(getContext()).add(stringRequest);
            //

            img_giohang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        String iduser = String.valueOf(LoginActivity.user.getId());
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, GioHangFragment.getInstance(iduser));
                        transaction.addToBackStack(null);
                        transaction.commit();



                }
            });
        }

        return view;

    }

    private void getProductNam(String loai) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Server.url_getproduct, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int id=0;
                String title="";
                double price=0;
                String image="";
                String description="";
                int soluong=0;

                try {
                    JSONArray array=new JSONArray(response);
                    for(int i=0;i<array.length();i++){
                        JSONObject object=array.getJSONObject(i);

                        id=object.getInt("id");
                        title=object.getString("title");
                        price=object.getDouble("price");
                        image=object.getString("image");
                        description=object.getString("description");
                        soluong=object.getInt("soluong");



                        //chuyển đối số sang đơn vị tiền tệ việt nam
                        Locale locale=new Locale("vi","VN");
                        NumberFormat numberFormat=NumberFormat.getCurrencyInstance(locale);
                        String gia=numberFormat.format(price);

                        Product p=new Product(id,title,description,image,gia,soluong);
                        listproduct_nam.add(p);
                        mAdapter.notifyDataSetChanged();
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
                params.put("loai",loai);
                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    private void getProductNu(String loai) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Server.url_getproduct, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int id=0;
                String title="";
                double price=0;
                String image="";
                String description="";
                int soluong=0;

                try {
                    JSONArray array=new JSONArray(response);
                    for(int i=0;i<array.length();i++){
                        JSONObject object=array.getJSONObject(i);

                        id=object.getInt("id");
                        title=object.getString("title");
                        price=object.getDouble("price");
                        image=object.getString("image");
                        description=object.getString("description");
                        soluong=object.getInt("soluong");



                        //chuyển đối số sang đơn vị tiền tệ việt nam
                        Locale locale=new Locale("vi","VN");
                        NumberFormat numberFormat=NumberFormat.getCurrencyInstance(locale);
                        String gia=numberFormat.format(price);

                        Product p=new Product(id,title,description,image,gia,soluong);
                        listproduct_nu.add(p);
                        mAdapter.notifyDataSetChanged();
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
                params.put("loai",loai);
                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    private void getProductCapDoi(String loai) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Server.url_getproduct, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int id=0;
                String title="";
                double price=0;
                String image="";
                String description="";
                int soluong=0;

                try {
                    JSONArray array=new JSONArray(response);
                    for(int i=0;i<array.length();i++){
                        JSONObject object=array.getJSONObject(i);

                        id=object.getInt("id");
                        title=object.getString("title");
                        price=object.getDouble("price");
                        image=object.getString("image");
                        description=object.getString("description");
                        soluong=object.getInt("soluong");



                        //chuyển đối số sang đơn vị tiền tệ việt nam
                        Locale locale=new Locale("vi","VN");
                        NumberFormat numberFormat=NumberFormat.getCurrencyInstance(locale);
                        String gia=numberFormat.format(price);

                        Product p=new Product(id,title,description,image,gia,soluong);
                        listproduct_capdoi.add(p);
                        mAdapter.notifyDataSetChanged();
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
                params.put("loai",loai);
                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    private void getThuongHieu() {
        StringRequest stringRequest=new StringRequest(Request.Method.GET, Server.url_getthuonghieu, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array=new JSONArray(response);
                    for(int i=0;i<array.length();i++){
                        JSONObject object=array.getJSONObject(i);

                        int id=object.getInt("id");
                        String name=object.getString("name");
                        String image=object.getString("img");

                        ThuongHieu p=new ThuongHieu(id,name,image);
                        listThuongHieu.add(p);
                        tAdapter.notifyDataSetChanged();
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
        });
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    private void autoSlideImage() {
        if(photoList==null || photoList.isEmpty() || viewPager==null){
            return;
        }
        if(mTimer==null){
            mTimer=new Timer();
        }
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentIem=viewPager.getCurrentItem();
                        int totalItem=photoList.size()-1;
                        if(currentIem<totalItem){
                            currentIem++;
                            viewPager.setCurrentItem(currentIem);
                        }
                        else{
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        },500,3000);
    }

    private List<Photo> getListPhoto() {
        List<Photo> list=new ArrayList<>();
        list.add(new Photo(R.drawable.anh1));
        list.add(new Photo(R.drawable.anh2));
        list.add(new Photo(R.drawable.h1));
        list.add(new Photo(R.drawable.maxresdefault));
        list.add(new Photo(R.drawable.thietkecataloguedongho));
        return list;
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

                        user=new User(id,name,email,password,tendangnhap,sdt);
                    }
                    //set count vao text gio hang
                    StringRequest stringRequest=new StringRequest(Request.Method.POST, Server.url_getCountGioHang, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            int count=0;

                            try {
                                JSONArray array=new JSONArray(response);
                                for(int i=0;i<array.length();i++){
                                    JSONObject object=array.getJSONObject(i);

                                    count=object.getInt("count");
                                }
                                txt_sosp_giohang.setText(count+"");
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
                            params.put("iduser", String.valueOf(user.getId()));

                            return params;
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



}

package com.example.thuctaptotnghiep;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.thuctaptotnghiep.Fragment.DetailFragment;
import com.example.thuctaptotnghiep.Fragment.DetailMyPageFragment;
import com.example.thuctaptotnghiep.Fragment.DetailTinTucFragment;
import com.example.thuctaptotnghiep.Fragment.HomeFragment;
import com.example.thuctaptotnghiep.Fragment.MyPageFragment;
import com.example.thuctaptotnghiep.Fragment.SearchFragment;
import com.example.thuctaptotnghiep.Fragment.ThuongHieuFragment;
import com.example.thuctaptotnghiep.Fragment.TinTucFragment;
import com.example.thuctaptotnghiep.Fragment.YeuThichFragment;
import com.example.thuctaptotnghiep.Object.Product;
import com.example.thuctaptotnghiep.Object.ThuongHieu;
import com.example.thuctaptotnghiep.Object.TinTuc;
import com.example.thuctaptotnghiep.Object.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Fragment fragment;
   // BottomNavigationView bottomNavigationView;
    public ChipNavigationBar bottomNavigationView;
    String tendangnhap;
    String pass;
    Bitmap bitmap;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView=findViewById(R.id.bottom_nv);

        if(LoginActivity.user==null){
            Toast.makeText(this, "bạn chưa có tài khoản", Toast.LENGTH_SHORT).show();
            loadFragment(new HomeFragment());
            bottomNavigationView.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
                @Override
                public void onItemSelected(int i) {
                    switch (i){
                        case R.id.action_home:
                            loadFragment(new HomeFragment());
                            break;
                        case R.id.action_tintuc:
                            loadFragment(new TinTucFragment());
                            break;
                        case R.id.action_person:
                            loadFragment(new MyPageFragment());
                            break;
                    }
                }
            });
        }
        else {


//        Intent intent=getIntent();
//        tendangnhap=intent.getStringExtra("tendangnhap");
//        pass=intent.getStringExtra("pass");
//        Log.d("aaa",tendangnhap);
//        tendangnhap=LoginActivity.user.getTendangnhap();
//        pass=LoginActivity.user.getPassword();
//
//
        getUser(LoginActivity.user.getTendangnhap(),LoginActivity.user.getPassword());

            loadFragment(HomeFragment.getInstance(LoginActivity.user.getTendangnhap(),LoginActivity.user.getPassword()));


//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//                switch (item.getItemId()){
//                    case R.id.action_home:
//                        fragment=new HomeFragment();
//                        loadFragment(fragment);
//                        return true;
//                    case R.id.action_person:
//                        fragment=MyPageFragment.getInstance(tendangnhap,pass);
//                        loadFragment(fragment);
//                        return true;
//
//
//                }
//                return false;
//            }
//        });
            bottomNavigationView.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
                @Override
                public void onItemSelected(int i) {
                    switch (i) {
                        case R.id.action_home:
                            loadFragment(HomeFragment.getInstance(LoginActivity.user.getTendangnhap(),LoginActivity.user.getPassword()));
                            break;
                        case R.id.action_tintuc:
                            loadFragment(new TinTucFragment());
                            break;
                        case R.id.action_person:
                            loadFragment(MyPageFragment.getInstance(LoginActivity.user.getTendangnhap(),LoginActivity.user.getPassword()));
                            break;
                    }
                }
            });
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    //chuyển màn đến fragment khi click vào sản phẩm
    public void goToDetailFragment(Product p){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        DetailFragment detailFragment=new DetailFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable("object_product",p);
//        bundle.putString("ten",LoginActivity.user.getTendangnhap());
//        bundle.putString("pass",LoginActivity.user.getPassword());
        detailFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.frame_layout,detailFragment);
        fragmentTransaction.addToBackStack(DetailFragment.TAG);
        fragmentTransaction.commit();
    }

    public void goToThuongHieuFragment(ThuongHieu t){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        ThuongHieuFragment thuongHieuFragment=new ThuongHieuFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable("object_thuonghieu",t);
        thuongHieuFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.frame_layout,thuongHieuFragment);
        fragmentTransaction.addToBackStack(ThuongHieuFragment.TAG);
        fragmentTransaction.commit();
    }
    public void goToSearchFragment(){
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        SearchFragment searchFragment=new SearchFragment();
        transaction.replace(R.id.frame_layout,searchFragment);
        transaction.addToBackStack(searchFragment.TAG);
        transaction.commit();
    }
    public void goToDetailTinTucFragment(TinTuc tinTuc){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        DetailTinTucFragment detailTinTucFragment=new DetailTinTucFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable("object_tintuc",tinTuc);
        detailTinTucFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.frame_layout,detailTinTucFragment);
        fragmentTransaction.addToBackStack(DetailTinTucFragment.TAG);
        fragmentTransaction.commit();

    }
    public void goToYeuThichFragment(User user){
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        YeuThichFragment yeuThichFragment=new YeuThichFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable("object_user_tym",user);
        yeuThichFragment.setArguments(bundle);

        transaction.replace(R.id.frame_layout,yeuThichFragment);
        transaction.addToBackStack(YeuThichFragment.TAG);
        transaction.commit();
    }

    public void dangxuat(){
//        Intent intent=new Intent(MainActivity.this,LoginActivity.class);
//        startActivity(intent);
        LoginActivity.user=null;
        Intent intent=new Intent(MainActivity.this,MainActivity.class);
        startActivity(intent);
    }

    //chon anh dai dien
    public void chonFileAnh(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select picture"),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode == RESULT_OK && data.getData()!=null){
            Uri filepath=data.getData();
            try {
                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            UploadAnh(String.valueOf(user.getId()),getStringImage(bitmap));
        }
    }
    private void UploadAnh(final String id,final String photo){
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Server.url_uploadImage, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String success=jsonObject.getString("success");
                    if(success.equals("1")){
                        Toast.makeText(MainActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "try again!", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "try again!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("id",id);
                params.put("photo",photo);
                return params;
            }
        };
        Volley.newRequestQueue(getBaseContext()).add(stringRequest);
    }
    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imageByteArray=byteArrayOutputStream.toByteArray();
        String encodeImage= Base64.encodeToString(imageByteArray,Base64.DEFAULT);
        return encodeImage;
    }
    //

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
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
        Volley.newRequestQueue(getBaseContext()).add(stringRequest);
    }
}
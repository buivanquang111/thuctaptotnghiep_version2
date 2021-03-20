package com.example.thuctaptotnghiep;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.thuctaptotnghiep.Object.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    EditText ed_name,ed_email,ed_pass,ed_tendangnhap,ed_sdt;
    String str_name,str_email,strpassword,str_tendangnhap,str_sdt;
    ImageView img_back;
    Bitmap bitmap;
    CircleImageView circleImageView;
    User user;
    List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ed_name=findViewById(R.id.ed_name);
        ed_email=findViewById(R.id.ed_email);
        ed_pass=findViewById(R.id.ed_pass);
        ed_tendangnhap=findViewById(R.id.ed_tendangnhap);
        ed_sdt=findViewById(R.id.ed_sdt);
        img_back=findViewById(R.id.img_back);
        circleImageView=findViewById(R.id.profile_image_register);

        userList=new ArrayList<>();
        getUser();

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chonFileAnh();
            }
        });

    }

    public void Register(View view) {

        for (int i=0;i<userList.size();i++){
            if (userList.get(i).getTendangnhap().equals(ed_tendangnhap.getText().toString())){
                Toast.makeText(RegisterActivity.this, "Tên đăng nhập này đã tồn tại!!!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        insertuser(getStringImage(bitmap));
//        ProgressDialog progressDialog=new ProgressDialog(this);
//        progressDialog.setTitle("Please wait...");
//        progressDialog.show();
//
//        str_name=ed_name.getText().toString().trim();
//        str_email=ed_email.getText().toString().trim();
//        strpassword=ed_pass.getText().toString().trim();
//        str_tendangnhap=ed_tendangnhap.getText().toString().trim();
//        str_sdt=ed_sdt.getText().toString().trim();
//        Log.d("aaa",str_tendangnhap);
//        Log.d("aaa",str_sdt);
//
//        StringRequest request=new StringRequest(Request.Method.POST, Server.url_register, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                progressDialog.dismiss();
//                ed_name.setText("");
//                ed_email.setText("");
//                ed_pass.setText("");
//                ed_tendangnhap.setText("");
//                ed_sdt.setText("");
//                Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
//                finish();
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                progressDialog.dismiss();
////                Toast.makeText(RegisterActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
////                Log.d("bbb",error.getMessage().toString());
//            }
//        }
//        )
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> params=new HashMap<String,String>();
//                params.put("name",str_name);
//                params.put("email",str_email);
//                params.put("password",strpassword);
//                params.put("tendn",str_tendangnhap);
//                params.put("sdt",str_sdt);
//                params.put("photo",getStringImage(bitmap));
//                return params;
//            }
//        };
//        RequestQueue requestQueue= Volley.newRequestQueue(RegisterActivity.this);
//        requestQueue.add(request);
    }

    private void getUser(){
        StringRequest stringRequest=new StringRequest(Request.Method.GET, Server.url_getUser, new Response.Listener<String>() {
            int id=0;
            String name="";
            String email="";
            String pass="";
            String tendn="";
            String sdt="";


            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array=new JSONArray(response);
                    for(int i=0;i<array.length();i++){
                        JSONObject object=array.getJSONObject(i);
                        id=object.getInt("id");
                        name=object.getString("name");
                        email=object.getString("email");
                        pass=object.getString("password");
                        tendn=object.getString("tendangnhap");
                        sdt=object.getString("sdt");

                        user=new User(id,name,email,pass,tendn,sdt);
                        userList.add(user);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(RegisterActivity.this);
        requestQueue.add(stringRequest);
    }

    //chon anh dai dien
    public void chonFileAnh(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select picture"),2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2 && resultCode == RESULT_OK && data.getData()!=null){
            Uri filepath=data.getData();
            try {
                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                circleImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
//            insertuser(getStringImage(bitmap));
        }
    }
    private void insertuser(String photo){
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.show();

        str_name=ed_name.getText().toString().trim();
        str_email=ed_email.getText().toString().trim();
        strpassword=ed_pass.getText().toString().trim();
        str_tendangnhap=ed_tendangnhap.getText().toString().trim();
        str_sdt=ed_sdt.getText().toString().trim();
        Log.d("aaa",str_tendangnhap);
        Log.d("aaa",str_sdt);

        StringRequest request=new StringRequest(Request.Method.POST, Server.url_register, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                ed_name.setText("");
                ed_email.setText("");
                ed_pass.setText("");
                ed_tendangnhap.setText("");
                ed_sdt.setText("");
                Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
//                Toast.makeText(RegisterActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
//                Log.d("bbb",error.getMessage().toString());
            }
        }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("name",str_name);
                params.put("email",str_email);
                params.put("password",strpassword);
                params.put("tendn",str_tendangnhap);
                params.put("sdt",str_sdt);
                params.put("photo",photo);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(RegisterActivity.this);
        requestQueue.add(request);
    }

    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imageByteArray=byteArrayOutputStream.toByteArray();
        String encodeImage= Base64.encodeToString(imageByteArray,Base64.DEFAULT);
        return encodeImage;
    }
}
package com.example.thuctaptotnghiep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.thuctaptotnghiep.Fragment.HomeFragment;
import com.example.thuctaptotnghiep.Fragment.MyPageFragment;
import com.example.thuctaptotnghiep.Object.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    TextView txt_dk;
    EditText ed_tendangnhap,ed_pass;
    String str_tendangnhap,strpassword;
    public static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ed_tendangnhap=findViewById(R.id.ed_tendangnhaplogin);
        ed_pass=findViewById(R.id.ed_passlogin);
        txt_dk=findViewById(R.id.txtdangki);




        txt_dk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    public void login(View view) {
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.show();


        str_tendangnhap=ed_tendangnhap.getText().toString().trim();
        strpassword=ed_pass.getText().toString().trim();


        StringRequest request=new StringRequest(Request.Method.POST, Server.url_login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("login successfully")){
                    progressDialog.dismiss();

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

                                    Toast.makeText(LoginActivity.this, "chào mừng "+user.getTendangnhap()+" đã quay trở lại ứng dụng.", Toast.LENGTH_SHORT).show();
                                    // User user=new User(str_email);
                                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
//                                    intent.putExtra("tendangnhap",str_tendangnhap);
//                                    intent.putExtra("pass",strpassword);
                                    startActivity(intent);
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
                            params.put("tendangnhap",str_tendangnhap);
                            params.put("pass",strpassword);
                            return params;
                        }
                    };
                    Volley.newRequestQueue(getBaseContext()).add(stringRequest);





                }
                else{
                    progressDialog.dismiss();
                    ed_tendangnhap.setText("");
                    ed_pass.setText("");
                    Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
               // Toast.makeText(LoginActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
               // Log.d("bbb",error.getMessage().toString());
            }
        }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();

                params.put("tendn",str_tendangnhap);
                params.put("password",strpassword);


                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(request);
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
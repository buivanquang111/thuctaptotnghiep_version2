package com.example.thuctaptotnghiep.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.thuctaptotnghiep.Adapter.ProductAdapter2;
import com.example.thuctaptotnghiep.MainActivity;
import com.example.thuctaptotnghiep.Object.Product;
import com.example.thuctaptotnghiep.Object.User;
import com.example.thuctaptotnghiep.Object.YeuThich;
import com.example.thuctaptotnghiep.R;
import com.example.thuctaptotnghiep.Server;
import com.example.thuctaptotnghiep.inteface.IClickItemListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class YeuThichFragment extends Fragment {

    public static final String TAG=YeuThichFragment.class.getName();
    User user;

    Product product;
    List<Product> productList;

    ProductAdapter2 mAdapter;
    MainActivity mMainActivity;
    RecyclerView recyclerView_yeuthich;
    ImageView img_back;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_yeuthich,container,false);

        recyclerView_yeuthich=view.findViewById(R.id.recyclerview_yeuthich);
        img_back=view.findViewById(R.id.image_back_yeuthich);

        mMainActivity= (MainActivity) getActivity();
        mMainActivity.bottomNavigationView.setVisibility(View.GONE);

        Bundle bundle=getArguments();
        if (bundle!=null){
            user= (User) bundle.get("object_user_tym");
        }

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager()!=null){
                    getFragmentManager().popBackStack();
                }
            }
        });

        GridLayoutManager manager=new GridLayoutManager(getContext(),2);
        recyclerView_yeuthich.setLayoutManager(manager);
        productList=new ArrayList<>();
        getYeuThich(String.valueOf(user.getId()));

        mAdapter=new ProductAdapter2(productList, getContext(), new IClickItemListener() {
            @Override
            public void onClickItemProduct(Product product) {
                mMainActivity.goToDetailFragment(product);
            }
        });
        recyclerView_yeuthich.setAdapter(mAdapter);


        return view;
    }

    private void getYeuThich(String iduser){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Server.url_getyeuthichwithiduser, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getContext(), iduser, Toast.LENGTH_SHORT).show();
                int idtym=0;
                int iduser=0;
                int idproduct=0;
                String title="";
                String image="";
                double price=0;
                String mota="";
                int sl=0;

                try {
                    JSONArray array=new JSONArray(response);
                    for(int i=0;i<array.length();i++){
                        JSONObject object=array.getJSONObject(i);

                        idtym=object.getInt("idtym");
                        iduser=object.getInt("iduser");
                        idproduct=object.getInt("idproduct");
                        title=object.getString("title");
                        image=object.getString("image");
                        price=object.getDouble("price");
                        mota=object.getString("description");
                        sl=object.getInt("soluong");

                        //chuyển đối số sang đơn vị tiền tệ việt nam
                        Locale locale=new Locale("vi","VN");
                        NumberFormat numberFormat=NumberFormat.getCurrencyInstance(locale);
                        String gia=numberFormat.format(price);

                        product=new Product(idproduct,title,mota,image,gia,sl);
                        productList.add(product);

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
                Map<String,String> para=new HashMap<>();
                para.put("iduser", iduser);

                return para;
            }
        };
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }
}

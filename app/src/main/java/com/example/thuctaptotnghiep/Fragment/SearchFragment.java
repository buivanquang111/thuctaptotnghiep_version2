package com.example.thuctaptotnghiep.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.example.thuctaptotnghiep.Adapter.ProductAdapter;
import com.example.thuctaptotnghiep.Adapter.ProductAdapter2;
import com.example.thuctaptotnghiep.MainActivity;
import com.example.thuctaptotnghiep.Object.Product;
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

public class SearchFragment extends Fragment {

    private MainActivity mMainActivity;
    public static final String TAG=SearchFragment.class.getName();
    EditText ed_searchfragment;
    ImageView img_search;
    TextView txt_close_search;
    String ten;
    RecyclerView recyclerView_search;



    private List<Product> listproduct;

    private ProductAdapter2 mAdapter;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_search,container,false);

        ed_searchfragment=view.findViewById(R.id.ed_searchfragment);
        img_search=view.findViewById(R.id.image_searchfragment);
        txt_close_search=view.findViewById(R.id.txt_thoat_searchfragment);
        recyclerView_search=view.findViewById(R.id.recyclerview_searchfragment);

        mMainActivity= (MainActivity) getActivity();
        mMainActivity.bottomNavigationView.setVisibility(View.GONE);

        txt_close_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getFragmentManager()!=null){
                    getFragmentManager().popBackStack();
                }
            }
        });



        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ten=ed_searchfragment.getText().toString().trim();
                Log.d("aaa",ten);
//                if(ten.equals("")){
//                    Toast.makeText(mMainActivity, "bạn cần nhập tên sản phẩm để tìm kiếm", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                GridLayoutManager manager=new GridLayoutManager(getContext(),2);
                recyclerView_search.setLayoutManager(manager);
                listproduct=new ArrayList<>();
                getProductSearch(ten);

                mAdapter=new ProductAdapter2(listproduct, getContext(), new IClickItemListener() {
                    @Override
                    public void onClickItemProduct(Product product) {
                        mMainActivity.goToDetailFragment(product);
                    }
                });
                recyclerView_search.setAdapter(mAdapter);

            }
        });


        return view;
    }
    private void getProductSearch(String ten) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Server.url_getSearchProduct, new Response.Listener<String>() {
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
                        listproduct.add(p);
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
                params.put("title",ten);
                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }
}

package com.example.thuctaptotnghiep.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.example.thuctaptotnghiep.Object.ThuongHieu;
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

public class ThuongHieuFragment extends Fragment {

    public static final String TAG=ThuongHieuFragment.class.getName();
    TextView txt_title;
    RecyclerView recyclerView;
    ImageView img_back;


    private List<Product> listProduct;
    private ProductAdapter2 mAdapter;
    ThuongHieu thuongHieu;
    private MainActivity mMainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_thuonghieu,container,false);

        mMainActivity= (MainActivity) getActivity();
        mMainActivity.bottomNavigationView.setVisibility(View.GONE);

        txt_title=view.findViewById(R.id.txt_tenthuonghieu_fragmentthuonghieu);
        recyclerView=view.findViewById(R.id.thuonghieu_recyclerview);
        img_back=view.findViewById(R.id.image_back_thuonghieu);

        Bundle bundle=getArguments();
        if(bundle!=null){
            thuongHieu= (ThuongHieu) bundle.get("object_thuonghieu");
            if(thuongHieu!=null){
                txt_title.setText(thuongHieu.getName());
            }
        }
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getFragmentManager()!=null){
                    getFragmentManager().popBackStack();
                }
            }
        });


        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        listProduct=new ArrayList<>();
        getProductWithThuongHieu(thuongHieu.getName());
        mAdapter=new ProductAdapter2(listProduct, getContext(), new IClickItemListener() {
            @Override
            public void onClickItemProduct(Product product) {
                mMainActivity.goToDetailFragment(product);
            }
        });
        recyclerView.setAdapter(mAdapter);


        return view;
    }

    private void getProductWithThuongHieu(String name) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Server.url_getProductWithThuonghieu, new Response.Listener<String>() {
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
                        listProduct.add(p);
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
                params.put("name",name);
                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

}

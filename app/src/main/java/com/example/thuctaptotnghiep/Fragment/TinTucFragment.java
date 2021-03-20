package com.example.thuctaptotnghiep.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.thuctaptotnghiep.Adapter.TinTucAdapter;
import com.example.thuctaptotnghiep.MainActivity;
import com.example.thuctaptotnghiep.Object.ThuongHieu;
import com.example.thuctaptotnghiep.Object.TinTuc;
import com.example.thuctaptotnghiep.R;
import com.example.thuctaptotnghiep.Server;
import com.example.thuctaptotnghiep.inteface.IClickTinTucListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TinTucFragment extends Fragment {

    ViewFlipper viewFlipper;
    RecyclerView recyclerView;

    List<TinTuc> tinTucList;
    TinTuc tinTuc;
    TinTucAdapter mAdapter;

    MainActivity mMainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_tintuc,container,false);

        //viewFlipper=view.findViewById(R.id.viewplipper_fragmenttintuc);
        recyclerView=view.findViewById(R.id.recyclerview_fragmenttintuc);

        //ActionViewFlipper();
        mMainActivity= (MainActivity) getActivity();
        mMainActivity.bottomNavigationView.setVisibility(View.VISIBLE);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        tinTucList=new ArrayList<>();
        getTinTuc();
        mAdapter=new TinTucAdapter(tinTucList, getContext(), new IClickTinTucListener() {
            @Override
            public void onClickTinTucItem(TinTuc tinTuc) {
                mMainActivity.goToDetailTinTucFragment(tinTuc);
            }
        });
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    private void getTinTuc() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Server.url_gettintuc, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);

                        int id = object.getInt("id");
                        String name = object.getString("ten");
                        String image = object.getString("hinhanh");
                        String noidung = object.getString("noidung");

                        TinTuc p = new TinTuc(id, name, image, noidung);
                        tinTucList.add(p);
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
        });
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    private void ActionViewFlipper(){
        List<String> mang=new ArrayList<>();
        mang.add("https://shopwatch.vn/wp-content/uploads/2018/04/anh-bia-dong-ho-18.jpg");
        mang.add("https://bazaarvietnam.vn/wp-content/uploads/2017/03/Omega-Basel-sneak-peek-ARTICLE.jpg");
        mang.add("https://dongho123.files.wordpress.com/2014/10/15.jpg");
        mang.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT6D0LsQPaNobTatqJZdw7Qpk6vGxtM2mkkCmYRRSRTiCJd6UCMc1gqamCFH-ztfAt2wt4&usqp=CAU");
        mang.add("https://i.ndh.vn/2014/06/17/maxresdefault_wwck-1402970696.jpg");

        for (int i=0;i<mang.size();i++){
            ImageView imageView=new ImageView(getContext());
            Glide.with(getContext()).load(mang.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(5000);
        viewFlipper.setAutoStart(true);
        //gan animation
        Animation animation_slide_in= AnimationUtils.loadAnimation(getContext(),R.anim.slide_in_right);
        Animation animation_slide_out=AnimationUtils.loadAnimation(getContext(),R.anim.slide_out_right);

        viewFlipper.setInAnimation(animation_slide_in);
        viewFlipper.setOutAnimation(animation_slide_out);
    }

}

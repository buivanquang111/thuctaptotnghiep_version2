package com.example.thuctaptotnghiep.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.thuctaptotnghiep.MainActivity;
import com.example.thuctaptotnghiep.Object.TinTuc;
import com.example.thuctaptotnghiep.R;

public class DetailTinTucFragment extends Fragment {

    public static final String TAG=DetailTinTucFragment.class.getName();
    TextView txt_title,txt_noidung;
    ImageView img,img_back;
    TinTuc tinTuc;
    MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_detail_tintuc,container,false);

        txt_title=view.findViewById(R.id.txt_title_detailtintuc);
        txt_noidung=view.findViewById(R.id.txt_noidung_detailtintuc);
        img=view.findViewById(R.id.image_detailtintuc);
        img_back=view.findViewById(R.id.image_back_Detailtintuc);

        mainActivity= (MainActivity) getActivity();
        mainActivity.bottomNavigationView.setVisibility(View.GONE);

        Bundle bundle=getArguments();
        if (bundle!=null){
            tinTuc= (TinTuc) bundle.get("object_tintuc");
            if (tinTuc!=null){
                txt_title.setText(tinTuc.getTen());
                txt_noidung.setText(tinTuc.getNoidung());
                Glide.with(getContext()).load(tinTuc.getHinhanh()).into(img);
            }
        }
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager()!=null) {
                    getFragmentManager().popBackStack();
                }
            }
        });


        return view;
    }
}

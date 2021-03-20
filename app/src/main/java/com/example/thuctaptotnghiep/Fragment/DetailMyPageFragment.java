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

import com.example.thuctaptotnghiep.MainActivity;
import com.example.thuctaptotnghiep.Object.Product;
import com.example.thuctaptotnghiep.Object.User;
import com.example.thuctaptotnghiep.R;

public class DetailMyPageFragment extends Fragment {

    public static final String TAG=DetailMyPageFragment.class.getName();
    TextView txt_hoten,txt_tendangnhap,txt_email,txt_sdt,txt_pass;
    ImageView img_back;

    MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_detail_mypage,container,false);

        txt_hoten=view.findViewById(R.id.txt_hoten_detail_mypage);
        txt_tendangnhap=view.findViewById(R.id.txt_tendangnhap_detail_mypage);
        txt_email=view.findViewById(R.id.txt_email_detail_mypage);
        txt_sdt=view.findViewById(R.id.txt_sdt_detail_mypage);
        txt_pass=view.findViewById(R.id.txt_password_detail_mypage);
        img_back=view.findViewById(R.id.image_backDetail_mypage);

        mainActivity= (MainActivity) getActivity();
        mainActivity.bottomNavigationView.setVisibility(View.GONE);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getFragmentManager()!=null){
                    getFragmentManager().popBackStack();
                }
            }
        });

        Bundle bundle=getArguments();
        if(bundle!=null){
            User user= (User) bundle.get("user_detaimypage");
            if(user!=null){
                txt_hoten.setText(user.getName());
                txt_tendangnhap.setText(user.getTendangnhap());
                txt_email.setText(user.getEmail());
                txt_sdt.setText(user.getSdt());
                txt_pass.setText(user.getPassword());
            }
        }


        return view;
    }
}

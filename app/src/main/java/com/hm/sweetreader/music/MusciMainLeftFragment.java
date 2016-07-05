package com.hm.sweetreader.music;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.hm.sweetreader.R;
import com.hm.sweetreader.file_manager.FileMangerActivity;

/**
 * project：SweetReader
 * author： FLY
 * date：   2016/7/5
 * time：   15:07
 * purpose：主界面左侧右滑界面
 */
public class MusciMainLeftFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.music_menu_main,container);
        init(root);
        return root;
    }
    private void init(View root){
        root.findViewById(R.id.music_main_path).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), FileMangerActivity.class);
                getActivity().startActivity(intent);
            }
        });
        CheckBox check=(CheckBox)root.findViewById(R.id.music_main_check);
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("this","this is "+isChecked);
            }
        });
    }
}

package com.hm.sweetreader.file_manager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hm.sweetreader.R;

/**
 * 文件选择主界面
 */
public class FileMangerActivity extends AppCompatActivity {

    private FileExplorerFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manger);
        init();
    }

    private void init() {
        fragment = new FileExplorerFragment();
        android.app.FragmentManager fg = getFragmentManager();
        fg.beginTransaction().add(R.id.choose_file_group, fragment).commit();
    }
}

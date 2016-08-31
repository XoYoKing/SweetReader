package com.hm.sweetreader.file_manager_copy;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hm.sweetreader.DayNightHelper;
import com.hm.sweetreader.FileUtils;
import com.hm.sweetreader.R;
import com.hm.sweetreader.ThemeUtils;
import com.hm.sweetreader.music.MusicMainActivity;
import com.hm.sweetreader.show.ShowActivity;

import java.io.File;
import java.util.LinkedList;

public class FileManagerCopyActivity extends AppCompatActivity {

    private static String TAG = FileManagerCopyActivity.class.getSimpleName();

    private File SDpath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtils.initTheme(this);
        setContentView(R.layout.activity_file_manager_copy);
        initView();
    }

    private void initView() {
        //退回上级
        findViewById(R.id.file_manager_copy_black).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        bindData();
        LinkedList<File> files = new LinkedList<>();
        File[] files1 = SDpath.listFiles();

        for (File file : files1) {
            files.add(file);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.file_manager_copy_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new FileManagerListItemDecoration(this,FileManagerListItemDecoration.VERTICAL_LIST));
        FileManagerAdapter adapter = new FileManagerAdapter(this, files);
        recyclerView.setAdapter(adapter);
    }

    // 绑定数据
    public void bindData() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            // 判断sd卡是否正常{
            SDpath = Environment.getExternalStorageDirectory();

            //判断文件夹数据组是否取到了数据
            for (int i = 0; i < SDpath.listFiles().length; i++) {
                Log.i("float1", "111111" + SDpath.listFiles()[i] + "");
            }
        } else {
            Log.e(TAG, "SD卡出现异常");
        }
    }

}

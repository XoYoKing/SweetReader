package com.hm.sweetreader.file_manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import com.hm.sweetreader.MainActivity;
import com.hm.sweetreader.R;
import com.hm.sweetreader.db.DBManager;

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //按下键盘上返回按钮
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            case KeyEvent.KEYCODE_MENU:
                super.openOptionsMenu();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

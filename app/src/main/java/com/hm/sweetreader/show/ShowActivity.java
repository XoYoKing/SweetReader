package com.hm.sweetreader.show;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hm.sweetreader.Contents;
import com.hm.sweetreader.FileUtils;
import com.hm.sweetreader.MainActivity;
import com.hm.sweetreader.R;
import com.hm.sweetreader.db.DBManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 书籍阅读主界面
 */
public class ShowActivity extends AppCompatActivity {

    private String TAG = ShowActivity.class.getSimpleName();
    private String bookName;

    private ProgressBar progressBar;
    private ViewPager viewPager;
    private List<String> listPath = new ArrayList<>();
    private ShowViewPagerAdapter adapter;

    private int test = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        progressBar = (ProgressBar) findViewById(R.id.show_progressbar);
        viewPager = (ViewPager) findViewById(R.id.show_viewpager);
        Button button = (Button) findViewById(R.id.show_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test++;
                viewPager.setCurrentItem(test);
            }
        });
        init();
    }

    private void init() {
        String path = getIntent().getStringExtra("filePath");
        if (path != null) {
            //刚刚导入的新文件
            createBook(path);
            return;
        } else {
            //之前已经看过的文件
            reReadBook(getIntent());
        }

    }

    //先处理源文件
    private void createBook(String path) {

        String name_tag = String.valueOf((int) (Math.random() * 100000));
        bookName = FileUtils.getFileName(path, true);

        long size = FileUtils.getFileLength(path);
        size = size / Contents.byteInPage;

        DBManager.newInstance(this).insertBook(bookName, path, "", name_tag, 0, size);

        try {
//            List<String> fileList = FileUtils.readFileContentBySection(path, name_tag);
            List<String> fileList = FileUtils.readFileContentByString(path, name_tag);
            if (fileList.size() == 0) {
                return;
            }

            Toast.makeText(this, "总共" + size + "页", Toast.LENGTH_SHORT).show();
            for (String fileName : fileList) {
                Log.e(TAG, "name is " + fileName);
                listPath.add(fileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        adapter = new ShowViewPagerAdapter(this, listPath, size);
        viewPager.setAdapter(adapter);

        progressBar.setVisibility(View.GONE);
        viewPager.setVisibility(View.VISIBLE);

    }

    private void reReadBook(Intent intent) {

        bookName = intent.getStringExtra("name");
        int page = (int) (intent.getLongExtra("page", 0));
        String name_tag = intent.getStringExtra("name_tag");

        if (bookName == null) {
            return;
        }

        List<String> bookTags = FileUtils.isSaveFileName(Contents.cacheFilePath, name_tag);
        long size = DBManager.newInstance(this).selectPageTotal(bookName);
        adapter = new ShowViewPagerAdapter(this, bookTags, size);
        viewPager.setAdapter(adapter);

        progressBar.setVisibility(View.GONE);
        viewPager.setVisibility(View.VISIBLE);

        viewPager.setCurrentItem(page);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //按下键盘上返回按钮
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                DBManager.newInstance(this).updatePageByName(bookName, viewPager.getCurrentItem());
                Log.e("destroy", " i==" + viewPager.getCurrentItem() + " bookName ==" + bookName);
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

//    @Override
//    public void onOptionsMenuClosed(Menu menu) {
////在这里做你想做的事情
//
//        super.onOptionsMenuClosed(menu);
//    }
//    @Override
//
//    public void openOptionsMenu() {
//
//        // TODO Auto-generated method stub
//
//        super.openOptionsMenu();
//
//    }
//    @Override
//
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        // TODO Auto-generated method stub
//
//        super.onCreateOptionsMenu(menu);
//
//        int group1 = 1;
//
//        int gourp2 = 2;
//
//        menu.add(group1, 1, 1, "item 11");
//
//        menu.add(group1, 2, 2, "item 12");
//
//        return true;
//
//    }
//
//    @Override
//
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        // TODO Auto-generated method stub
//
//        switch (item.getItemId()) {
//
//            case 1: // do something here
//
//                Log.i("MenuTest:", "ItemSelected:1");
//
//                break;
//
//            case 2: // do something here
//
//                Log.i("MenuTest:", "ItemSelected:2");
//
//                break;
//
//            default:
//
//                return super.onOptionsItemSelected(item);
//
//        }
//
//        return true;
//
//    }
}

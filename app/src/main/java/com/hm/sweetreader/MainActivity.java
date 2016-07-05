package com.hm.sweetreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.hm.sweetreader.db.DBManager;
import com.hm.sweetreader.entity.DataEntity;
import com.hm.sweetreader.file_list.MainListAdapter;
import com.hm.sweetreader.show.ShowActivity;
import com.nineoldandroids.view.ViewHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private String TAG = MainActivity.class.getSimpleName();

    private Button addMore;
    private RecyclerView listBook;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createFile();
        init();

    }


    private void init() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawerlayout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                Gravity.RIGHT);
        initEvents();

        addMore = (Button) findViewById(R.id.main_btn);
        addMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenLeftMenu(v);
            }
        });

        listBook = (RecyclerView) findViewById(R.id.mian_recycle);
        listBook.setLayoutManager(new GridLayoutManager(this, 4));


        List<DataEntity> list = DBManager.newInstance(this).selectAll();
        if (list == null || list.size() == 0) {
            return;
        }
        MainListAdapter adapter = new MainListAdapter(this);
        adapter.setData(list);
        adapter.setOnItemClickLitener(new MainListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, String name, String name_tag, long page) {
                Intent intent = new Intent(MainActivity.this, ShowActivity.class);
                intent.putExtra("name_tag", name_tag);
                intent.putExtra("name", name);
                intent.putExtra("page", page);
                startActivity(intent);
                finish();
            }
        });

        listBook.setAdapter(adapter);
    }

    private void createFile() {
        File file = new File(Contents.rootFilePath);
        if (!file.exists()) {
            file.mkdirs();
        }

        try {
            InputStream inputStream = getAssets().open("123.txt");
            FileOutputStream fos = new FileOutputStream(Contents.rootFilePath + File.separator + "小说.txt");
            byte[] b = new byte[1024];
            while ((inputStream.read(b)) != -1) {
                fos.write(b);
            }
            inputStream.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void OpenLeftMenu(View view)
    {
        mDrawerLayout.openDrawer(Gravity.LEFT);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
                Gravity.RIGHT);
    }

    private void initEvents()
    {
        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener()
        {
            @Override
            public void onDrawerStateChanged(int newState)
            {
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset)
            {
                View mContent = mDrawerLayout.getChildAt(0);
                View mMenu = drawerView;
                float scale = 1 - slideOffset;
                float rightScale = 0.8f + scale * 0.2f;

                if (drawerView.getTag().equals("LEFT"))
                {

                    float leftScale = 1 - 0.3f * scale;

                    ViewHelper.setScaleX(mMenu, leftScale);
                    ViewHelper.setScaleY(mMenu, leftScale);
                    ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
                    ViewHelper.setTranslationX(mContent,
                            mMenu.getMeasuredWidth() * (1 - scale));
                    ViewHelper.setPivotX(mContent, 0);
                    ViewHelper.setPivotY(mContent,
                            mContent.getMeasuredHeight() / 2);
                    mContent.invalidate();
//                    ViewHelper.setScaleX(mContent, rightScale);
//                    ViewHelper.setScaleY(mContent, rightScale);
                } else
                {
                    ViewHelper.setTranslationX(mContent,
                            -mMenu.getMeasuredWidth() * slideOffset);
                    ViewHelper.setPivotX(mContent, mContent.getMeasuredWidth());
                    ViewHelper.setPivotY(mContent,
                            mContent.getMeasuredHeight() / 2);
                    mContent.invalidate();
                    ViewHelper.setScaleX(mContent, rightScale);
                    ViewHelper.setScaleY(mContent, rightScale);
                }

            }

            @Override
            public void onDrawerOpened(View drawerView)
            {
            }

            @Override
            public void onDrawerClosed(View drawerView)
            {
                mDrawerLayout.setDrawerLockMode(
                        DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
            }
        });
    }

}

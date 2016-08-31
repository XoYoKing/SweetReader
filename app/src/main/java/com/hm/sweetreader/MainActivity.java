package com.hm.sweetreader;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hm.sweetreader.db.DBManager;
import com.hm.sweetreader.entity.DataEntity;
import com.hm.sweetreader.file_list.MainListAdapter;
import com.hm.sweetreader.show.ShowActivity;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private String TAG = MainActivity.class.getSimpleName();

    private Button addMore;
    private TextView title;
    private RecyclerView listBook;
    private DrawerLayout mDrawerLayout;
    private List<RelativeLayout> listLayout=new ArrayList<>(2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtils.initTheme(this);
        setContentView(R.layout.activity_main);
        createFile();
//        createFile("father.LRC","father.LRC");
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

        findViewById(R.id.main_theme).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"ONclIK");
                showAnimation();
                setTheme();
                refreshUI();
            }
        });

        title=(TextView)findViewById(R.id.main_title);

        listBook = (RecyclerView) findViewById(R.id.mian_recycle);
        listBook.setLayoutManager(new GridLayoutManager(this, 4));

        listLayout.add((RelativeLayout)findViewById(R.id.main_relative));
        listLayout.add((RelativeLayout)findViewById(R.id.main_view));

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

//        try {
//            InputStream inputStream = getAssets().open(name);
//            FileOutputStream fos = new FileOutputStream(Contents.rootFilePath + File.separator + fileName);
//            byte[] b = new byte[1024];
//            while ((inputStream.read(b)) != -1) {
//                fos.write(b);
//            }
//            inputStream.close();
//            fos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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


    private void setTheme(){
        if (DayNightHelper.newInstance(this).isDay()){
            DayNightHelper.newInstance(this).saveTheme(false);
            setTheme(R.style.NightTheme);
            return;
        }
            DayNightHelper.newInstance(this).saveTheme(true);
            setTheme(R.style.DayTheme);
    }

    /**
     * 刷新UI界面
     */
    private void refreshUI() {
        TypedValue background = new TypedValue();//背景色
        TypedValue textColor = new TypedValue();//字体颜色
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(R.attr.clockBackground, background, true);
        theme.resolveAttribute(R.attr.clockTextColor, textColor, true);

//        mHeaderLayout.setBackgroundResource(background.resourceId);
        for (RelativeLayout layout : listLayout) {
            layout.setBackgroundResource(background.resourceId);
        }
//        for (CheckBox checkBox : mCheckBoxList) {
//            checkBox.setBackgroundResource(background.resourceId);
//        }
        addMore.setBackgroundResource(background.resourceId);
//        for (TextView textView : mTextViewList) {
//            textView.setBackgroundResource(background.resourceId);
//        }

        Resources resources = getResources();
//        for (TextView textView : mTextViewList) {
//            textView.setTextColor(resources.getColor(textColor.resourceId));
//        }
        title.setBackgroundResource(background.resourceId);
        title.setTextColor(resources.getColor(textColor.resourceId));

        int childCount = listBook.getChildCount();
        for (int childIndex = 0; childIndex < childCount; childIndex++) {
            ViewGroup childView = (ViewGroup) listBook.getChildAt(childIndex);
            childView.setBackgroundResource(background.resourceId);
//            View infoLayout = childView.findViewById(R.id.info_layout);
//            infoLayout.setBackgroundResource(background.resourceId);
            TextView nickName = (TextView) childView.findViewById(R.id.main_list_item_text);
            nickName.setBackgroundResource(background.resourceId);
            nickName.setTextColor(resources.getColor(textColor.resourceId));
//            TextView motto = (TextView) childView.findViewById(R.id.tv_motto);
//            motto.setBackgroundResource(background.resourceId);
//            motto.setTextColor(resources.getColor(textColor.resourceId));
        }

        //让 RecyclerView 缓存在 Pool 中的 Item 失效
        //那么，如果是ListView，要怎么做呢？这里的思路是通过反射拿到 AbsListView 类中的 RecycleBin 对象，然后同样再用反射去调用 clear 方法
        Class<RecyclerView> recyclerViewClass = RecyclerView.class;
        try {
            Field declaredField = recyclerViewClass.getDeclaredField("mRecycler");
            declaredField.setAccessible(true);
            Method declaredMethod = Class.forName(RecyclerView.Recycler.class.getName()).getDeclaredMethod("clear", (Class<?>[]) new Class[0]);
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(declaredField.get(listBook), new Object[0]);
            RecyclerView.RecycledViewPool recycledViewPool = listBook.getRecycledViewPool();
            recycledViewPool.clear();

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        refreshStatusBar();
    }

    /**
     * 刷新 StatusBar
     */
    private void refreshStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = getTheme();
            theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
            getWindow().setStatusBarColor(getResources().getColor(typedValue.resourceId));
        }
    }

    /**
     * 展示一个切换动画
     */
    private void showAnimation() {
        final View decorView = getWindow().getDecorView();
        Bitmap cacheBitmap = getCacheBitmapFromView(decorView);
        if (decorView instanceof ViewGroup && cacheBitmap != null) {
            final View view = new View(this);
            view.setBackgroundDrawable(new BitmapDrawable(getResources(), cacheBitmap));
            ViewGroup.LayoutParams layoutParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            ((ViewGroup) decorView).addView(view, layoutParam);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
            objectAnimator.setDuration(300);
            objectAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    ((ViewGroup) decorView).removeView(view);
                }
            });
            objectAnimator.start();
        }
    }

    /**
     * 获取一个 View 的缓存视图
     *
     * @param view
     * @return
     */
    private Bitmap getCacheBitmapFromView(View view) {
        final boolean drawingCacheEnabled = true;
        view.setDrawingCacheEnabled(drawingCacheEnabled);
        view.buildDrawingCache(drawingCacheEnabled);
        final Bitmap drawingCache = view.getDrawingCache();
        Bitmap bitmap;
        if (drawingCache != null) {
            bitmap = Bitmap.createBitmap(drawingCache);
            view.setDrawingCacheEnabled(false);
        } else {
            bitmap = null;
        }
        return bitmap;
    }

}

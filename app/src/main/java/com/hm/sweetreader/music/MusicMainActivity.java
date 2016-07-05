package com.hm.sweetreader.music;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hm.sweetreader.Contents;
import com.hm.sweetreader.R;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;

public class MusicMainActivity extends AppCompatActivity {

    private List<String> mData = new ArrayList<>();
    private ImageButton btnPlayOrPause, btnPre, btnNext;
    //进度条
    private SeekBar skbMusic;
    // 获取界面中显示歌曲标题、作者文本框
    private TextView title, author;
    //是否正在播放
    private boolean isPlaying = false;
    String[] titleStrs = new String[]{"涛声依旧", "油菜花", "You Are The One"};
    String[] authorStrs = new String[]{"毛宁", "成龙", "未知艺术家"};
    //是否是手动拖动seekbar
    private boolean isAuto = false;

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_main);
        init();
        //注册接收器
        MusicBoxReceiver mReceiver = new MusicBoxReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Contents.MUSICBOX_ACTION);
        filter.addAction(Contents.MUSICBOX_ACTION_PROGRESS);
        registerReceiver(mReceiver, filter);



    }

    @Override
    protected void onResume() {
        super.onResume();
        //来自文件选择的路径
        String path = getIntent().getStringExtra("filePath");

        //启动后台Service
        Intent intent = new Intent(this, MusicService.class);
        if (path != null) {
            intent.putExtra("filePath", path);
        }
        startService(intent);
    }

    private void init() {
        drawerLayout=(DrawerLayout)findViewById(R.id.music_main_drawerlayout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                Gravity.RIGHT);
        initEvents();

        btnPlayOrPause = (ImageButton) findViewById(R.id.music_main_play);
        btnPre = (ImageButton) findViewById(R.id.music_main_last);
        btnNext = (ImageButton) findViewById(R.id.music_main_next);
        skbMusic = (SeekBar) findViewById(R.id.music_main_seekbar);
        btnNext.setOnClickListener(listener);
        btnPlayOrPause.setOnClickListener(listener);
        btnPre.setOnClickListener(listener);
        skbMusic.setOnSeekBarChangeListener(sChangeListener);
        title = (TextView) findViewById(R.id.music_main_name);
        author = (TextView) findViewById(R.id.music_main_anuthor);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            switch (v.getId()) {
                case R.id.music_main_next://下一首
                    btnPlayOrPause.setBackgroundResource(R.mipmap.ic_launcher);
                    sendBroadcastToService(Contents.STATE_NEXT);
                    isPlaying = true;
                    break;
                case R.id.music_main_play://播放或暂停
                    if (!isPlaying) {
                        btnPlayOrPause.setBackgroundResource(R.mipmap.ic_launcher);
                        sendBroadcastToService(Contents.STATE_PLAY);
                        isPlaying = true;
                    } else {
                        btnPlayOrPause.setBackgroundResource(R.mipmap.ic_launcher);
                        sendBroadcastToService(Contents.STATE_PAUSE);
                        isPlaying = false;
                    }
                    break;
                case R.id.music_main_last://上一首
                    btnPlayOrPause.setBackgroundResource(R.mipmap.ic_launcher);
                    sendBroadcastToService(Contents.STATE_PREVIOUS);
                    isPlaying = true;
                    break;
                default:
                    break;
            }
        }
    };
    /**
     * SeekBar进度改变事件
     */
    SeekBar.OnSeekBarChangeListener sChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
            //当拖动停止后，控制mediaPlayer播放指定位置的音乐
//            MusicService.mediaPlayer.seekTo(seekBar.getProgress());
//            MusicService.isChanging = false;
            if (isAuto) {
                sendBroadcastToService(0, seekBar.getProgress());
                isAuto = false;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
//            MusicService.isChanging = true;
            sendBroadcastToService(1, 0);
            isAuto = true;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            // TODO Auto-generated method stub

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(Menu.NONE, Contents.MENU_ABOUT, Menu.NONE, "关于");
        menu.add(Menu.NONE, Contents.MENU_EXIT, Menu.NONE, "退出");
        return true;
    }

    /**
     * 向后台Service发送控制广播
     *
     * @param state int state 控制状态码
     */
    protected void sendBroadcastToService(int state) {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        intent.setAction(Contents.MUSICSERVICE_ACTION_STATE);
        intent.putExtra("control", state);
//        intent.putExtra("handler", rr);

        //向后台Service发送播放控制的广播
        sendBroadcast(intent);
    }

    protected void sendBroadcastToService(int state, int progress) {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        intent.setAction(Contents.MUSICSERVICE_ACTION_PROGRESS);
        intent.putExtra("state", state);
        if (state == 0) {
            intent.putExtra("progress", progress);
        }

        //向后台Service发送播放控制的广播
        sendBroadcast(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case Contents.MENU_ABOUT:
//                LayoutInflater inflater = LayoutInflater.from(this);
//                LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.about, null);
                AlertDialog dialog = new AlertDialog.Builder(this).create();
                dialog.setTitle("关于");
                dialog.setIcon(R.mipmap.ic_launcher);
//                dialog.setView(layout);
                dialog.show();
                break;
            case Contents.MENU_EXIT:
                sendBroadcastToService(Contents.STATE_STOP);
                this.finish();
                break;
            default:
                break;
        }
        return true;
    }

    //创建一个广播接收器用于接收后台Service发出的广播
    class MusicBoxReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals(Contents.MUSICBOX_ACTION)) {
                // 获取Intent中的current消息，current代表当前正在播放的歌曲
                int current = intent.getIntExtra("current", -1);
                title.setText(titleStrs[current]);//更新音乐标题
                author.setText(authorStrs[current]);//更新音乐作者
            } else if (intent.getAction().equals(Contents.MUSICBOX_ACTION_PROGRESS)) {
                if (intent.getIntExtra("state", -1) == 0) {
                    skbMusic.setMax(intent.getIntExtra("max", 0));
                } else {
                    skbMusic.setProgress(intent.getIntExtra("progress", 0));
                }
            }
        }

    }


    public void OpenLeftMenu(View view)
    {
        drawerLayout.openDrawer(Gravity.LEFT);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
                Gravity.RIGHT);
    }

    private void initEvents()
    {
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener()
        {
            @Override
            public void onDrawerStateChanged(int newState)
            {
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset)
            {
                View mContent = drawerLayout.getChildAt(0);
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
                drawerLayout.setDrawerLockMode(
                        DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
            }
        });
    }
}

package com.hm.sweetreader.music;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.hm.sweetreader.Contents;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MusicService extends Service {

    private String TAG = MusicService.class.getSimpleName();

    private Timer mTimer;
    private TimerTask mTimerTask;
    static boolean isChanging = false;//互斥变量，防止定时器与SeekBar拖动时进度冲突
    //播放媒体对象
    private MediaPlayer mediaPlayer;
    //当前播放状态
    private int state = Contents.STATE_NON;
    //当前播放的状态
    private int current = 0;
    //记录Timer运行状态
    boolean isTimerRunning = false;
    //创建一个Asset管理器的的对象
    AssetManager assetManager;
    private String[] musics = {"fourheart.mp3", "father.mp3", "mizukiaLIEz.mp3"};

    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        assetManager = getAssets();
        mediaPlayer = new MediaPlayer();
        MusicSercieReceiver receiver = new MusicSercieReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Contents.MUSICSERVICE_ACTION_STATE);
        filter.addAction(Contents.MUSICSERVICE_ACTION_PROGRESS);
        registerReceiver(receiver, filter);
    }

    /**
     * 装载和播放音乐
     *
     * @param index int index 播放第几首音乐的索引
     */
    protected void prepareAndPlay(int index) {
        if (isTimerRunning) {//如果Timer正在运行
            mTimer.cancel();//取消定时器
            isTimerRunning = false;
        }
        if (index > 2) {
            current = index = 0;
        }
        if (index < 0) {
            current = index = 2;
        }
        sendBroadcastToActivity(index);
        try {
            //获取assets目录下指定文件的AssetFileDescriptor对象
            //TODO
            AssetFileDescriptor assetFileDescriptor = assetManager.openFd(musics[current]);
//            String path=intent.getStringExtra("filePath");
//            if (path==null){
//               return;
//            }
            //重置MediaPlayer进入未初始化状态
            mediaPlayer.reset();
            mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
                    assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
//            mediaPlayer.setDataSource(path);
            //准备播放音乐
            mediaPlayer.prepare();
//            mediaPlayer.prepareAsync();
            //播放音乐
            mediaPlayer.start();
            //getDuration()方法要在prepare()方法之后，否则会出现Attempt to call getDuration without a valid mediaplayer异常
            // TODO
//            MusicMainActivity.skbMusic.setMax(mediaPlayer.getDuration());//设置SeekBar的长度
//            onHandleIntent(intent, 0, mediaPlayer.getDuration());
            sendBroadcastToActivity(0, mediaPlayer.getDuration(), 0);
            Log.e(TAG, "max IS " + mediaPlayer.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //----------定时器记录播放进度---------//
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                isTimerRunning = true;
                if (isChanging == true)//当用户正在拖动进度进度条时不处理进度条的的进度
                    return;

                sendBroadcastToActivity(1, 0, mediaPlayer.getCurrentPosition());
                Log.e(TAG, "PROGRESS IS " + mediaPlayer.getCurrentPosition());
            }
        };
        //每隔10毫秒检测一下播放进度
        mTimer.schedule(mTimerTask, 0, 10);
    }


    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
//        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                current++;
                prepareAndPlay(current);
            }
        });

        return super.onStartCommand(intent, flags, startId);
    }

    private void sendBroadcastToActivity(int index) {
        //发送广播停止前台Activity更新界面
        Intent intentOne = new Intent();
        intentOne.putExtra("current", index);
        intentOne.setAction(Contents.MUSICBOX_ACTION);
        sendBroadcast(intentOne);
    }

    private void sendBroadcastToActivity(int state, int max, int progress) {
        //发送广播停止前台Activity更新界面
        Intent intentOne = new Intent();
        intentOne.putExtra("state", state);
        intentOne.setAction(Contents.MUSICBOX_ACTION_PROGRESS);
        if (state == 0) {
            intentOne.putExtra("max", max);
        } else {
            intentOne.putExtra("progress", progress);
        }
        sendBroadcast(intentOne);
    }

    //创建广播接收器用于接收前台Activity发去的广播
    public class MusicSercieReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals(Contents.MUSICSERVICE_ACTION_STATE)) {
                int control = intent.getIntExtra("control", -1);
                switch (control) {
                    case Contents.STATE_PLAY://播放音乐
                        if (state == Contents.STATE_PAUSE) {//如果原来状态是暂停
                            mediaPlayer.start();
                        } else if (state != Contents.STATE_PLAY) {
                            prepareAndPlay(current);
                        }
                        state = Contents.STATE_PLAY;
                        break;
                    case Contents.STATE_PAUSE://暂停播放
                        if (state == Contents.STATE_PLAY) {
                            mediaPlayer.pause();
                            state = Contents.STATE_PAUSE;
                        }
                        break;
                    case Contents.STATE_STOP://停止播放
                        if (state == Contents.STATE_PLAY || state == Contents.STATE_PAUSE) {
                            mediaPlayer.stop();
                            state = Contents.STATE_STOP;
                        }
                        break;
                    case Contents.STATE_PREVIOUS://上一首
                        prepareAndPlay(--current);
                        if (state == Contents.STATE_PLAY) {
                            state = Contents.STATE_PLAY;
                        } else {//如果原来状态是暂停
                            mediaPlayer.pause();
                            state=Contents.STATE_PAUSE;
                        }
                        break;
                    case Contents.STATE_NEXT://下一首
                        prepareAndPlay(++current);
                        if (state == Contents.STATE_PLAY) {
                            state = Contents.STATE_PLAY;
                        } else {//如果原来状态是暂停
                            mediaPlayer.pause();
                            state=Contents.STATE_PAUSE;
                        }
                        break;
                    default:
                        break;
                }
            } else if (intent.getAction().equals(Contents.MUSICSERVICE_ACTION_PROGRESS)) {

                if (intent.getIntExtra("state", -1) == 0) {
                    mediaPlayer.seekTo(intent.getIntExtra("progress", 0));
                    Log.e(TAG, "SEEK TO IS " + intent.getIntExtra("progress", 0));
                    isChanging = false;
                } else {
                    Log.e(TAG, "SEEK TO IS 1111" + intent.getIntExtra("progress", 0));

                    isChanging = true;

                }
            }
        }

    }

//    public void onHandleIntent(Intent i, int value, int progress) {
//        ResultReceiver rr = (ResultReceiver) i.getExtras().get("handler");
//
//        Bundle bundle = new Bundle();
//        switch (value) {
//            case 0:
//                bundle.putInt("max", value);
//                break;
//            case 1:
//                bundle.putInt("progress", progress);
//                break;
//        }
//        rr.send(Activity.RESULT_OK, bundle);
//    }
}

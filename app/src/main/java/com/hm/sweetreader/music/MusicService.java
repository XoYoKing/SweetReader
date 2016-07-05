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

import com.hm.sweetreader.Contents;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MusicService extends Service {

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
    protected void prepareAndPlay(int index, final Intent intent) {
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
            String path=intent.getStringExtra("filePath");
            if (path==null){
               return;
            }

            mediaPlayer.reset();//初始化mediaPlayer对象
//            mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
//                    assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
            mediaPlayer.setDataSource(path);
            //准备播放音乐
            mediaPlayer.prepare();
            //播放音乐
            mediaPlayer.start();
            //getDuration()方法要在prepare()方法之后，否则会出现Attempt to call getDuration without a valid mediaplayer异常
            // TODO
//            MusicMainActivity.skbMusic.setMax(mediaPlayer.getDuration());//设置SeekBar的长度
//            onHandleIntent(intent, 0, mediaPlayer.getDuration());
            sendBroadcastToActivity(0,mediaPlayer.getDuration(),0);
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
                //TODO
//                MusicMainActivity.skbMusic.setProgress(mediaPlayer.getCurrentPosition());
//                onHandleIntent(intent, 1, mediaPlayer.getCurrentPosition());
                sendBroadcastToActivity(1,0,mediaPlayer.getCurrentPosition());
            }
        };
        //每隔10毫秒检测一下播放进度
        mTimer.schedule(mTimerTask, 0, 10);
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                current++;
                prepareAndPlay(current, intent);
            }
        });

        return super.onStartCommand(intent, flags, startId);
    }

   private void sendBroadcastToActivity(int index){
       //发送广播停止前台Activity更新界面
       Intent intentOne = new Intent();
       intentOne.putExtra("current", index);
       intentOne.setAction(Contents.MUSICBOX_ACTION);
       sendBroadcast(intentOne);
   }
    private void sendBroadcastToActivity(int state,int max,int progress){
        //发送广播停止前台Activity更新界面
        Intent intentOne = new Intent();
        intentOne.putExtra("state", state);
        intentOne.setAction(Contents.MUSICBOX_ACTION_PROGRESS);
        if (state==0){
            intentOne.putExtra("max",max);
        }else {
            intentOne.putExtra("progress",progress);
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
                            prepareAndPlay(current, intent);
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
                        prepareAndPlay(--current, intent);
                        state = Contents.STATE_PLAY;
                        break;
                    case Contents.STATE_NEXT://下一首
                        prepareAndPlay(++current, intent);
                        state = Contents.STATE_PLAY;
                        break;
                    default:
                        break;
                }
            } else if (intent.getAction().equals(Contents.MUSICSERVICE_ACTION_PROGRESS)) {

                if (intent.getIntExtra("state", -1) == 0) {
                    mediaPlayer.seekTo(intent.getIntExtra("progress", 0));
                    isChanging = false;
                } else {
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

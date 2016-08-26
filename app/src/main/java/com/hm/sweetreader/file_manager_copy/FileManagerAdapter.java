package com.hm.sweetreader.file_manager_copy;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hm.sweetreader.FileUtils;
import com.hm.sweetreader.R;
import com.hm.sweetreader.music.MusicMainActivity;
import com.hm.sweetreader.show.ShowActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

/**
 * Purpose     :
 * Description :
 * Author      : FLY
 * Date        : 2016.08.25 15:06
 */

public class FileManagerAdapter extends RecyclerView.Adapter<FileManagerAdapter.MyHolder> {

    private Context context;
    private LinkedList<File> files = new LinkedList<>();

    public FileManagerAdapter(Context context, LinkedList<File> files) {
        this.context = context;
        this.files = files;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(context).inflate(R.layout.file_listview, parent, false);
        MyHolder holder = new MyHolder(root);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        holder.getView(files.get(position));
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFileList(files.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    private void getFileList(File file, int position) {
        int num = position;
        if (file.isDirectory()) {
//          文件夹分为空与非空
            if (file.listFiles().length == 0 && file.listFiles() == null) {
                return;
            }
            for (File file1 : file.listFiles()) {
                num += 1;
                files.add(num, file1);
                notifyItemInserted(num);
            }
        } else {
            getPath(file);
        }


    }

    private void getPath(File file) {
        String path = file.getAbsolutePath();
        if (FileUtils.isFileFormate(path, "txt") || FileUtils.isFileFormate(path, "TXT")) {
            Intent intent = new Intent(context, ShowActivity.class);
            intent.putExtra("filePath", path);
            context.startActivity(intent);
        } else if (FileUtils.isFileFormate(path, "mp3")) {
            Intent intent = new Intent(context, MusicMainActivity.class);
            intent.putExtra("filePath", path);
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "亲，这种格式我不是我的菜~~~~~~~~~~~~~", Toast.LENGTH_SHORT).show();
        }
    }


    class MyHolder extends RecyclerView.ViewHolder {
        View root;
        ImageView icon;
        TextView name;
        TextView time;

        public MyHolder(View itemView) {
            super(itemView);
            root = itemView;
            icon = (ImageView) itemView.findViewById(R.id.filelist_imageview);
            name = (TextView) itemView.findViewById(R.id.filelist_textview);
            time = (TextView) itemView.findViewById(R.id.filelist_timeText);
        }

        public void getView(File file) {
//            目录的显示特点
            if (file.isDirectory() && file.canRead()) {
//          文件夹分为空与非空
                if (file.listFiles().length == 0 && file.listFiles() == null) {
                    icon.setImageResource(R.drawable.icon_file);
                    name.setText(file.getName());
                    time.setText(new Date(System.currentTimeMillis()).toLocaleString());
                } else {
                    icon.setImageResource(R.drawable.icon_folder);
                    name.setText(file.getName());
                    time.setText(new Date(System.currentTimeMillis()).toLocaleString());
                }

            } else {   //文件的处理

                String _FileName = file.getName().toLowerCase();

                if (_FileName.endsWith(".txt")) {  //文本显示t
                    icon.setImageResource(R.drawable.icon_txt);
                    name.setText(_FileName);
                    time.setText(new Date(System.currentTimeMillis()).toLocaleString());

                } else if (_FileName.endsWith(".png") || _FileName.endsWith(".jpg") || _FileName.endsWith(".jpeg") || _FileName.endsWith(".gif")) {
                    icon.setTag(file.getAbsolutePath());
                    icon.setImageResource(R.drawable.icon_picture);
                    name.setText(_FileName);
                    time.setText(new Date(System.currentTimeMillis()).toLocaleString());

                } else if (_FileName.endsWith(".mp4") || _FileName.endsWith(".avi") || _FileName.endsWith(".3gp") || _FileName.endsWith(".rmvb")) {
                    icon.setImageResource(R.drawable.icon_video);
                    name.setText(_FileName);
                    time.setText(new Date(System.currentTimeMillis()).toLocaleString());

                } else if (_FileName.endsWith("mp3")) {
                    icon.setImageResource(R.drawable.icon_mp3);
                    name.setText(file.getName());
                    time.setText(new Date(System.currentTimeMillis()).toLocaleString());
                } else if (_FileName.endsWith("doc")) {
                    icon.setImageResource(R.drawable.icon_doc);
                    name.setText(file.getName());
                    time.setText(new Date(System.currentTimeMillis()).toLocaleString());
                }


            }

        }
    }
}
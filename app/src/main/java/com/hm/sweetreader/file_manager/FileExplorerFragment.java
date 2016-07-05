package com.hm.sweetreader.file_manager;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hm.sweetreader.FileUtils;
import com.hm.sweetreader.R;
import com.hm.sweetreader.music.MusicMainActivity;
import com.hm.sweetreader.show.ShowActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * project：SweetReader
 * author： FLY
 * date：   2016/6/14
 * time：   15:17
 * purpose：具体的文件选择界面
 */
public class FileExplorerFragment extends Fragment implements AdapterView.OnItemClickListener {

    private String TAG = FileExplorerFragment.class.getSimpleName();
    public static TextView showpath;
    private ListView listview;
    private FragmentAdapter adapter;
    //      private File[] files;
    private File[] mfileData;
    private File SDpath;
    int n;
    private List<File> filelist = new ArrayList<File>();

    //下段注释代码表示往Map里面添加文件数据，如果filename为文件夹，则设置为图标icon[1]，否则设置icon[0]
    //private int[] icon = { R.drawable.wenjian, R.drawable.wenjianjia };
    //Map<string,object> map=new HaspMap<string,object>();
    //map.put("keyname",filename.isDirectory?icon[1]:icon[0]);
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("onCreateView.........", "onCreateView.........");
        View view = inflater.inflate(R.layout.file_explorer_fragment, container, false);
        showpath = (TextView) view.findViewById(R.id.showpath);
        listview = (ListView) view.findViewById(R.id.listView);
        //
//          if (mfileData == null) {
//              SDpath = Environment.getExternalStorageDirectory();
//              showpath.setText(String.valueOf(SDpath));
//               mfileData = SDpath.listFiles(new CustomFileFilter()); // 过滤.文件
//               mfileData = FileSort.sortFile(mfileData);//排序
//               openFile(SDpath);
//          }
        //
        bindData();
        return view;
    }

    // 绑定数据
    public void bindData() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            // 判断sd卡是否正常
            if (mfileData == null) {
                SDpath = Environment.getExternalStorageDirectory();
                showpath.setText(String.valueOf(SDpath));
                getFileData();
            }
            //判断文件夹数据组是否取到了数据
            for (int i = 0; i < mfileData.length; i++) {
                Log.i("float1", "111111" + mfileData[i] + "");
            }
        } else {
            Log.e(TAG, "SD卡出现异常");
        }
    }

    private File[] getFileData() {
        mfileData = SDpath.listFiles(); // 过滤.文件
//        mfileData = FileSort.sortFile(mfileData);//排序
        openFile(SDpath);
        return mfileData;
    }

    public void openFile(File path) {
        mfileData = path.listFiles(); // 过滤.文件

        if (!path.isDirectory()) {
            getPath(path);
            return;
        }

        adapter = new FragmentAdapter(mfileData,
                getActivity());
        listview.setAdapter(adapter);
        for (int i = 0; i < mfileData.length; i++) {
            Log.i(TAG, "111111111111" + mfileData[i] + "");
        }
        Log.e(TAG, "当前文件夹有" + mfileData.length + "个对象");
        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listview.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mfileData == null) {
            return;
        }
        showpath.setText(String.valueOf(mfileData[position]));
        openFile(mfileData[position]);
    }

    private void getPath(File file) {
        String path = file.getAbsolutePath();
        if (FileUtils.isFileFormate(path, "txt") || FileUtils.isFileFormate(path, "TXT")) {
            Intent intent = new Intent(getActivity(), ShowActivity.class);
            intent.putExtra("filePath", path);
            startActivity(intent);
            getActivity().finish();
        } else if (FileUtils.isFileFormate(path, "mp3")) {
            Intent intent = new Intent(getActivity(), MusicMainActivity.class);
            intent.putExtra("filePath", path);
            startActivity(intent);
            getActivity().finish();
        } else {
            Toast.makeText(getActivity(), "亲，这种格式我不是我的菜~~~~~~~~~~~~~", Toast.LENGTH_SHORT).show();
        }
    }
}

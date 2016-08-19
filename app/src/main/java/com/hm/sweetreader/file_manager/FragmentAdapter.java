package com.hm.sweetreader.file_manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hm.sweetreader.R;

import java.io.File;
import java.util.Date;

/**
 * project：MyReader
 * author： FLY
 * date：   2016/6/14
 * time：   11:01
 * purpose：
 */
public class FragmentAdapter extends BaseAdapter {

    private Context mContext ;
    private File[] mfileData ;
    private LayoutInflater mlayoutinflater;


    public FragmentAdapter(File[]files , Context pContext) {
        mContext = pContext ;
        mfileData = files ;
        mlayoutinflater = LayoutInflater.from(mContext);
    }

    public  void updata(File[] pfile){
        mfileData = pfile ;
    }

    public int getCount() {
        return mfileData ==null? 0: mfileData.length;
    }

    public Object getItem(int position) {

        return mfileData[position];
    }

    public long getItemId(int position) {
        return position;
    }

    public static  class ViewHolder{
        public ImageView mFileImage ;
        public TextView mFileName ;
        public TextView mFileTime;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder ;
        View _view = convertView ;
        if(_view ==null){

            mViewHolder = new ViewHolder() ;
            _view = mlayoutinflater.inflate(R.layout.file_listview, null) ;
            mViewHolder.mFileImage =(ImageView) _view.findViewById(R.id.filelist_imageview
            ) ;
            mViewHolder.mFileName = (TextView) _view.findViewById(R.id.filelist_textview) ;
            mViewHolder.mFileTime = (TextView) _view.findViewById(R.id.filelist_timeText);
            _view.setTag(mViewHolder) ;
        }else{
            mViewHolder = (ViewHolder) _view.getTag() ;
        }

//      目录的显示特点
        if(mfileData[position].isDirectory() && mfileData[position].canRead()){
//          文件夹分为空与非空
            if(mfileData[position].listFiles().length == 0 && mfileData[position].listFiles()== null  ){
                mViewHolder.mFileImage.setImageResource(R.drawable.icon_file) ;
                mViewHolder.mFileName.setText(mfileData[position].getName()) ;
                mViewHolder.mFileTime.setText(new Date(System.currentTimeMillis()).toLocaleString());
            }else{
                mViewHolder.mFileImage.setImageResource(R.drawable.icon_folder) ;
                mViewHolder.mFileName.setText(mfileData[position].getName()) ;
                mViewHolder.mFileTime.setText(new Date(System.currentTimeMillis()).toLocaleString());
            }

        }else{   //文件的处理

            String _FileName = mfileData[position].getName().toLowerCase() ;

            if(_FileName.endsWith(".txt")){  //文本显示t
                mViewHolder.mFileImage.setImageResource(R.drawable.icon_txt) ;
                mViewHolder.mFileName.setText(_FileName) ;
                mViewHolder.mFileTime.setText(new Date(System.currentTimeMillis()).toLocaleString());

            }else if(_FileName.endsWith(".png") || _FileName.endsWith(".jpg") ||_FileName.endsWith(".jpeg") || _FileName.endsWith(".gif")){
                mViewHolder.mFileImage.setTag(mfileData[position].getAbsolutePath()) ;
                mViewHolder.mFileImage.setImageResource(R.drawable.icon_picture);
                mViewHolder.mFileName.setText(_FileName) ;
                mViewHolder.mFileTime.setText(new Date(System.currentTimeMillis()).toLocaleString());

            }else if(_FileName.endsWith(".mp4")|| _FileName.endsWith(".avi")|| _FileName.endsWith(".3gp") || _FileName.endsWith(".rmvb")){
                mViewHolder.mFileImage.setImageResource(R.drawable.icon_video) ;
                mViewHolder.mFileName.setText(_FileName) ;
                mViewHolder.mFileTime.setText(new Date(System.currentTimeMillis()).toLocaleString());

            }else if(_FileName.endsWith("mp3")){
                mViewHolder.mFileImage.setImageResource(R.drawable.icon_mp3) ;
                mViewHolder.mFileName.setText(mfileData[position].getName()) ;
                mViewHolder.mFileTime.setText(new Date(System.currentTimeMillis()).toLocaleString());
            }else if(_FileName.endsWith("doc")){
                mViewHolder.mFileImage.setImageResource(R.drawable.icon_doc ) ;
                mViewHolder.mFileName.setText(mfileData[position].getName()) ;
                mViewHolder.mFileTime.setText(new Date(System.currentTimeMillis()).toLocaleString());
            }

        }

    /*  View view = convertView ;
        view =mlayoutinflater.inflate(com.example.jsp.R.layout.list_item, null) ;
        TextView tv = (TextView)view.findViewById(com.example.jsp.R.id.tv_name1) ;
        tv.setText(mfileData[position].getName()+"") ;*/

        return _view;

    }
//  public static void main(String[] args) {
//      System.out.println(new Date(System.currentTimeMillis()).toString());
//  }
}

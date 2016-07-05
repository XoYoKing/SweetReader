package com.hm.sweetreader.show;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.hm.sweetreader.Contents;
import com.hm.sweetreader.FileUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * project：SweetReader
 * author： FLY
 * date：   2016/6/15
 * time：   14:15
 * purpose：
 */
public class ShowViewPagerAdapter extends PagerAdapter {

    private int POSITION_NONE = 0;
    private String TAG = ShowViewPagerAdapter.class.getSimpleName();

    private Context mContext;
    private List<String> filePathList;
    private View view;
    private long pageNum;

    private List<ShowEntity> showList = new ArrayList<>();
    private int pathListSize = 0;
    private int pageTag = 0;

    public ShowViewPagerAdapter(Context context, List<String> filePathList, long pageNum) {
        super();
        mContext = context;
        this.filePathList = filePathList;
        pathListSize = filePathList.size();
        this.pageNum = pageNum;
        try {
            loadMore(getData(filePathList.get(0)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getCount() {
//        if (filePathList != null) {
//            return filePathList.size();
//        }
        return (int) pageNum;
    }

    @Override
    public int getItemPosition(Object object) {
//        if (object != null && filePathList != null) {
//            String resId = (String) ((ImageView) object).getTag();
//            if (resId != null) {
//                for (int i = 0; i < filePathList.size(); i++) {
//                    if (resId.equals(filePathList.get(i))) {
//                        return i;
//                    }
//                }
//            }
//        }
        if (object != null && showList != null) {
            long resId = (long) ((ShowView) object).getTag();
            if (resId != 0) {
                for (int i = 0; i < showList.size(); i++) {
                    if (resId == (showList.get(i).getTag())) {
                        return i;
                    }
                }
            }
        }

        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        if (filePathList != null && position < filePathList.size()) {
//            String resId = filePathList.get(position);
//            if (resId != null) {
//                ShowView itemView = new ShowView(mContext);
//                String s = "";
//                try {
//                    s = FileUtils.readFileContentByBufferReader(resId);
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//
//                itemView.setText(s);
//
//                //此处假设所有的照片都不同，用resId唯一标识一个itemView；也可用其它Object来标识，只要保证唯一即可
//                itemView.setTag(resId);
//
//                ((ViewPager) container).addView(itemView);
//                return itemView;
//            }
//        }
        if (showList != null && position < pageNum) {
            //当指示器大于当前内容的数值
            while (position >= showList.size()) {
                pageTag++;
                if (pageTag > pathListSize) {
                    return POSITION_NONE;
                }
                try {
                    loadMore(getData(filePathList.get(pageTag)));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            long resId = showList.get(position).getTag();
            if (resId != 0) {
                ShowView itemView = new ShowView(mContext);
                itemView.setTextSize(19);
                itemView.setText(showList.get(position).getShow());

                //此处假设所有的照片都不同，用resId唯一标识一个itemView；也可用其它Object来标识，只要保证唯一即可
                itemView.setTag(resId);

                ((ViewPager) container).addView(itemView);
                return itemView;
            }
        }


        return null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //注意：此处position是ViewPager中所有要显示的页面的position，与Adapter mDrawableResIdList并不是一一对应的。
        //因为mDrawableResIdList有可能被修改删除某一个item，在调用notifyDataSetChanged()的时候，ViewPager中的页面
        //数量并没有改变，只有当ViewPager遍历完自己所有的页面，并将不存在的页面删除后，二者才能对应起来
        if (object != null) {
            ViewGroup viewPager = ((ViewGroup) container);
            int count = viewPager.getChildCount();
            for (int i = 0; i < count; i++) {
                View childView = viewPager.getChildAt(i);
                if (childView == object) {
                    viewPager.removeView(childView);
                    break;
                }
            }
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public void startUpdate(ViewGroup container) {
    }

    @Override
    public void finishUpdate(ViewGroup container) {
    }

    public void updateData(List<String> itemsResId) {
        if (itemsResId == null) {
            return;
        }
        filePathList = itemsResId;
        this.notifyDataSetChanged();
    }

    public void loadMore(List<ShowEntity> data) {
        if (data == null) {
            return;
        }
        showList.addAll(data);
        this.notifyDataSetChanged();
    }

    private List<ShowEntity> getData(String resId) throws UnsupportedEncodingException {
        String s = FileUtils.readFileContentByBufferReader(resId);
        Log.e(TAG, "s length is " + s.length());
        int page = 0;
        if (s.length() % Contents.byteInPage == 0) {
            page = s.length() / 1024;
        } else {
            page = s.length() / 1024 + 1;
        }
        Log.e(TAG, "page is " + page);
        List<ShowEntity> listShow = new ArrayList<>();
        ShowEntity showEntity = null;
        for (int i = 0; i < page; i++) {
            if (i == page - 1) {
                Log.e(TAG, "the last  === " + s.substring(i * Contents.byteInPage, s.length()));
                showEntity = new ShowEntity(System.currentTimeMillis(), s.substring(i * Contents.byteInPage, s.length()));
                listShow.add(showEntity);
                break;
            }
            String mm = s.substring(i * Contents.byteInPage, (i + 1) * Contents.byteInPage - 1);
            Log.e(TAG, "start " + i + " is" + i * Contents.byteInPage);
            Log.e(TAG, "end " + i + " is" + (i + 1) * Contents.byteInPage);
            Log.e(TAG, "MM === " + mm);
            showEntity = new ShowEntity(System.currentTimeMillis(), mm);
            listShow.add(showEntity);

        }
        return listShow;

    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        view = (View) object;
    }

    public View getPrimaryItem() {
        return view;
    }

}

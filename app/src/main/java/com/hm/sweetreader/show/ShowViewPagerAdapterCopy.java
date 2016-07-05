package com.hm.sweetreader.show;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
public class ShowViewPagerAdapterCopy extends PagerAdapter {

    private int POSITION_NONE = 0;

    private Context mContext;
    private List<String> filePathList;
    private View view;

    private List<String> showList=new ArrayList<>();

    public ShowViewPagerAdapterCopy(Context context, List<String> filePathList) {
        super();
        mContext = context;
        this.filePathList = filePathList;
    }


    @Override
    public int getCount() {
        if (filePathList != null) {
            return filePathList.size();
        }
        return 0;
    }

    @Override
    public int getItemPosition(Object object) {
        if (object != null && filePathList != null) {
            String resId = (String) ((ImageView) object).getTag();
            if (resId != null) {
                for (int i = 0; i < filePathList.size(); i++) {
                    if (resId.equals(filePathList.get(i))) {
                        return i;
                    }
                }
            }
        }
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (filePathList != null && position < filePathList.size()) {
            String resId = filePathList.get(position);
            if (resId != null) {
                ShowView itemView = new ShowView(mContext);
                String s = "";
                try {
                    s = FileUtils.readFileContentByBufferReader(resId);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                itemView.setText(s);

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

    public void loadMore(List<String> data){
        if (data==null){
            return;
        }
        showList.addAll(data);
        this.notifyDataSetChanged();
    }

    private List<String> getData(String resId) throws UnsupportedEncodingException {
        String s = FileUtils.readFileContentByBufferReader(resId);
        int page = 0;
        if (s.length() % 2048 == 0) {
            page = s.length() / 2048;
        } else {
            page = s.length() / 2048 + 1;
        }
        List<String> listShow = new ArrayList<>();
        for (int i = 0; i < page; i++) {
            String mm = s.substring(i * 2048, (i + 1) * 2048 - 1);
            listShow.add(mm);
            if (i == page - 1) {
                listShow.add(s.substring(i * 2048, s.length()));
            }
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

package com.hm.sweetreader.file_list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hm.sweetreader.R;
import com.hm.sweetreader.entity.DataEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * project：MyReader
 * author： FLY
 * date：   2016/5/26
 * time：   15:58
 * purpose：
 */
public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.RecycleHolder> {

    private Context context;
    private List<DataEntity> nameList = new ArrayList<>();


    public interface OnItemClickLitener {
        void onItemClick(View view, String name, String name_tag, long page);

    }

    private OnItemClickLitener mOnItemClickLitener;

    public MainListAdapter(Context context) {
        this.context = context;
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    public void setData(List<DataEntity> list) {
        nameList = list;
    }

    @Override
    public RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecycleHolder holder = new RecycleHolder(LayoutInflater.from(
                context).inflate(R.layout.main_list_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, final int position) {
        holder.tv.setText(nameList.get(position).getName());

        if (mOnItemClickLitener == null) {
            return;
        }

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickLitener.onItemClick(holder.rootView,nameList.get(position).getName(),nameList.get(position).getTag(),nameList.get(position).getPage() );
            }
        });


    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }

    class RecycleHolder extends RecyclerView.ViewHolder {
        View rootView;
        TextView tv;

        public RecycleHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            tv = (TextView) itemView.findViewById(R.id.main_list_item_text);
        }
    }

}

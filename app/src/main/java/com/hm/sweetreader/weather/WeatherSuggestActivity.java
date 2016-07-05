package com.hm.sweetreader.weather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hm.sweetreader.R;

/**
 * 各种天气指数
 */
public class WeatherSuggestActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_suggest);
        recyclerView = (RecyclerView) findViewById(R.id.weather_suggest_recycle);

    }


    class SuggestAdapter extends RecyclerView.Adapter<SuggestAdapter.MyHolder>{

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        class MyHolder extends RecyclerView.ViewHolder{
            public MyHolder(View itemView) {
                super(itemView);

            }
        }
    }
}

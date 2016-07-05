package com.hm.sweetreader.weather;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hm.sweetreader.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WeatherFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "temperatureHigh";
    private static final String ARG_PARAM3 = "temperatureLow";
    private static final String ARG_PARAM4 = "days";

    // TODO: Rename and change types of parameters
    private ArrayList<String> mParam1;

    private ArrayList<Integer> temperatureHigh, temperatureLow;
    private ArrayList<String> days;

    private OnFragmentInteractionListener mListener;

    public WeatherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment WeatherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeatherFragment newInstance(ArrayList<String> list, ArrayList<Integer> temperatureHighList, ArrayList<Integer> temperatureLowList, ArrayList<String> days) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM1, list);
        args.putIntegerArrayList(ARG_PARAM2, temperatureHighList);
        args.putIntegerArrayList(ARG_PARAM3, temperatureLowList);
        args.putStringArrayList(ARG_PARAM4, days);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getStringArrayList(ARG_PARAM1);
            temperatureHigh = getArguments().getIntegerArrayList(ARG_PARAM2);
            temperatureLow = getArguments().getIntegerArrayList(ARG_PARAM3);
            days = getArguments().getStringArrayList(ARG_PARAM4);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_weather, container, false);
        init(root);
        return root;
    }

    private void init(View root) {
        //城市名
        TextView address = (TextView) root.findViewById(R.id.weather_address_name);
        //温度
        TextView temperature = (TextView) root.findViewById(R.id.weather_temperature);
        //天气
        TextView weather = (TextView) root.findViewById(R.id.weather_weather);
        //最高温度
        final TextView temperature_high = (TextView) root.findViewById(R.id.weather_temperature_high);
        //最低温度
        TextView temperature_low = (TextView) root.findViewById(R.id.weather_temperature_low);
        //空气质量
        RelativeLayout aqiView = (RelativeLayout) root.findViewById(R.id.weather_api_view);
        TextView aqi = (TextView) root.findViewById(R.id.weather_aqi);
        //湿度
        RelativeLayout shiduView = (RelativeLayout) root.findViewById(R.id.weather_shidu_view);
        TextView shidu = (TextView) root.findViewById(R.id.weather_shidu);
        //日期
        TextView date = (TextView) root.findViewById(R.id.weather_date_text);
        //风向
        TextView fengxiang = (TextView) root.findViewById(R.id.weather_fengxiang);
        //风力
        TextView fengli = (TextView) root.findViewById(R.id.weather_fengli);
        //生活指南
        RelativeLayout tips = (RelativeLayout) root.findViewById(R.id.weather_tip_view);
        //更新时间
        TextView reflush = (TextView) root.findViewById(R.id.weather_reflush);
        //天气折线图
        Button btn = (Button) root.findViewById(R.id.weather_add_more);


        address.setText(mParam1.get(0));
        if (mParam1.get(1).isEmpty()) {
            temperature.setVisibility(View.GONE);
            root.findViewById(R.id.weather_temperature_icon).setVisibility(View.GONE);
        } else {
            temperature.setText(mParam1.get(1));
        }
        weather.setText(mParam1.get(2));
        temperature_high.setText(mParam1.get(3));
        temperature_low.setText(mParam1.get(4));
        if (mParam1.get(5).isEmpty()) {
            aqiView.setVisibility(View.GONE);
        } else {
            aqi.setText(mParam1.get(5));
        }
        if (mParam1.get(6).isEmpty()) {
            shiduView.setVisibility(View.GONE);
        } else {
            shidu.setText(mParam1.get(6));
        }
        date.setText(mParam1.get(7));
        fengxiang.setText(mParam1.get(8));
        fengli.setText(mParam1.get(9));
        reflush.setText(mParam1.get(10));
        //具体生活指南
        tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //天气折线图
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TemperatureActivity.class);
                intent.putIntegerArrayListExtra("high", temperatureHigh);
                intent.putIntegerArrayListExtra("low", temperatureLow);
                intent.putStringArrayListExtra("day", days);
                getActivity().startActivity(intent);
            }
        });

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

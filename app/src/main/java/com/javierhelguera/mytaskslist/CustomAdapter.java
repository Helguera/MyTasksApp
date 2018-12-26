package com.javierhelguera.mytaskslist;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends BaseAdapter {

    Activity activity;
    List<TaskModel> tasks;
    LayoutInflater inflater;


    public CustomAdapter(Activity activity) {
        this.activity = activity;
    }

    public CustomAdapter(Activity activity, List<TaskModel> tasks) {
        this.activity   = activity;
        this.tasks      = tasks;
        inflater        = activity.getLayoutInflater();
    }


    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder = null;

        if (view == null){

            view = inflater.inflate(R.layout.list_view_item, viewGroup, false);

            holder = new ViewHolder();

            holder.tvUserName = (TextView)view.findViewById(R.id.tv_user_name);
            holder.ivCheckBox = (ImageView) view.findViewById(R.id.iv_check_box);
            holder.dateHour = (TextView) view.findViewById(R.id.dateHour);

            view.setTag(holder);
        }else
            holder = (ViewHolder)view.getTag();

        TaskModel model = tasks.get(i);

        holder.tvUserName.setText(model.getTaskName());
        holder.dateHour.setText(model.getDate() + "\n" + model.getHour());



        if (model.isSelected() == 1)
            holder.ivCheckBox.setBackgroundResource(R.drawable.ic_check_circle_black_24dp);

        else
            holder.ivCheckBox.setBackgroundResource(R.drawable.ic_radio_button_unchecked_black_24dp);

        return view;

    }

    public void updateRecords(List<TaskModel> tasks){
        this.tasks = tasks;

        notifyDataSetChanged();
    }

    class ViewHolder{

        TextView tvUserName;
        ImageView ivCheckBox;
        TextView dateHour;

    }
}
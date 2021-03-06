package com.example.saleem.testgithub.listAdapters;


import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saleem.testgithub.R;
import com.example.saleem.testgithub.gson.items.ContactsItems;
import com.example.saleem.testgithub.gson.items.MyNeedsItems;
import com.example.saleem.testgithub.helper.CircularImageView;
import com.example.saleem.testgithub.model.Tasks;
import com.example.saleem.testgithub.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyNeedsListAdapter extends ArrayAdapter<MyNeedsItems.MyNeddsList> {
    private Context context;
    private List<MyNeedsItems.MyNeddsList> items;


    public MyNeedsListAdapter(Context context, int textViewResourceId,
                              List<MyNeedsItems.MyNeddsList> items) {
        super(context, textViewResourceId, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public MyNeedsItems.MyNeddsList getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void restart(List<MyNeedsItems.MyNeddsList> items) {
        this.items = items;
        notifyDataSetChanged();
    }


    public class viewHolder {
        ImageView thumbNail;
        TextView name, taskSubject, status;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder holder = null;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.my_needs_list, null);

            holder = new viewHolder();
            holder.thumbNail = (ImageView) convertView
                    .findViewById(R.id.receiverThumbnail);
            holder.name = (TextView) convertView.findViewById(R.id.receiver_name);
            holder.taskSubject = (TextView) convertView.findViewById(R.id.myNeed_title);
            holder.status = (TextView) convertView.findViewById(R.id.task_status);

            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }


        Picasso.with(context).load(items.get(position).getImageUrl()).placeholder(R.drawable.default_profile).error(R.drawable.default_profile).fit().transform(new CircleTransform()).into(holder.thumbNail);

        // task owner name
        holder.name.setText(items.get(position).getUserName()+"");

        // task title
        holder.taskSubject.setText(items.get(position).getTitle()+"");

        // task status

        holder.status.setText(items.get(position).getStatus()+"");

        if (items.get(position).getStatus().equals("Not yet")) {
            holder.status.setTextColor(Color.rgb(244, 47, 47));
        } else {
            holder.status.setTextColor(Color.rgb(76, 175, 80));
        }

        return convertView;
    }

}
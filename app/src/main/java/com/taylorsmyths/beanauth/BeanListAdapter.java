package com.taylorsmyths.beanauth;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.punchthrough.bean.sdk.Bean;

import java.util.List;

/**
 * Created by Taylor on 6/20/2016.
 */
public class BeanListAdapter extends ArrayAdapter<Bean> {
    private List<Bean> beans;
    private int layoutResourceId;
    private Context context;

    private BeanListAdapter(Context context, int layoutResourceId, List<Bean> beans) {
        super(context, layoutResourceId, beans);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.beans = beans;
    }

    @Override
    public View getView(int position, View row, @NonNull ViewGroup parent ) {
        BeanHolder holder = null;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        holder = new BeanHolder();
        holder.bean = beans.get(position);
        holder.connectButton = row.findViewById(R.id.connectButton);
        holder.macAddress = row.findViewById(R.id.macAddress);
        holder.connectButton.setTag(holder.bean);

        holder.connectButton.setText(holder.bean.getDevice().getName());
        holder.macAddress.setText(holder.bean.getDevice().getAddress());

        row.setTag(holder);

        return row;
    }

    public static class BeanHolder {
        Bean bean;
        Button connectButton;
        TextView macAddress;
    }
}


package com.yushilei.zoomview.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yushilei.zoomview.R;
import com.yushilei.zoomview.ZoomActivity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/23.
 */
public class RecyAdapter extends RecyclerView.Adapter<RecyAdapter.VH> implements View.OnClickListener {
    private Context context;
    private List<String> data = new LinkedList<>();

    public RecyAdapter(Context context) {
        this.context = context;
    }

    public void addAll(List<String> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recy, parent, false);
        VH vh = new VH(view);
        view.setTag(vh);
        return vh;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.tv.setText(data.get(position));
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onClick(View v) {
        context.startActivity(new Intent(context, ZoomActivity.class));
    }

    public static final class VH extends RecyclerView.ViewHolder {
        TextView tv;

        public VH(View itemView) {
            super(itemView);
            tv = ((TextView) itemView.findViewById(R.id.item_tv));
        }
    }
}

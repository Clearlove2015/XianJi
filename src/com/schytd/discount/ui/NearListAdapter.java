package com.schytd.discount.ui;

import java.util.List;

import com.schytd.xianji.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

class NearListAdapter extends BaseAdapter {
	private List<String> l;
	private Context context;
	private int selectItem = -1;
	private int idx;

	public NearListAdapter(Context context, List<String> l, int idx) {

		this.context = context;
		this.l = l;
		this.idx = idx;
	}

	@Override
	public int getCount() {
		return l.size();
	}

	@Override
	public Object getItem(int position) {
		return l.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = LayoutInflater.from(context).inflate(R.layout.shaixuan_popupwindow_item, null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		holder.name.setText(l.get(position).toString());
		if (idx == 1) {
			holder.icon.setImageResource(R.drawable.near_list_icon);
		}

		if (position == selectItem) {
			convertView.setBackgroundColor(context.getResources().getColor(R.color.near_list_bg));
		} else {
			convertView.setBackgroundColor(context.getResources().getColor(R.color.white));
		}
		return convertView;
	}

	public void setSelectItem(int selectItem) {
		this.selectItem = selectItem;
	}

	class Holder {
		TextView name;
		ImageView icon;
	}
}
package com.schytd.discount.ui;

import com.schytd.xianji.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

class NearSubAdapter extends BaseAdapter {

	Context context;
	LayoutInflater layoutInflater;
	String[] citiy;
	public int foodpoition;

	public NearSubAdapter(Context context, String[] citiy) {
		this.context = context;
		this.citiy = citiy;
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return citiy.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return citiy[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.shaixuan_popupwindow_item, null);
			viewHolder = new ViewHolder();
			viewHolder.textView = (TextView) convertView.findViewById(R.id.name);
			viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.textView.setText(citiy[position]);
		viewHolder.icon.setVisibility(View.GONE);
		return convertView;
	}

	public static class ViewHolder {
		public TextView textView;
		ImageView icon;
	}

}
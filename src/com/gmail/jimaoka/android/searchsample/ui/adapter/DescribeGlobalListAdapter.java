package com.gmail.jimaoka.android.searchsample.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gmail.jimaoka.android.searchsample.R;
import com.gmail.jimaoka.android.sfviewbuilder.vo.DescribeGlobalVo;

/**
 * DescribeGlobal の ListAdapter です
 * @author junji imaoka
 *
 */
public class DescribeGlobalListAdapter extends ArrayAdapter<DescribeGlobalVo> {

	private LayoutInflater inflater;
	private List<DescribeGlobalVo> vos;
	private ViewHolder viewHolder;
	
	/**
	 * コンストラクタ
	 * @param context
	 * @param resource
	 * @param objects
	 */
	public DescribeGlobalListAdapter(Context context, int resource,	List<DescribeGlobalVo> objects) {
		super(context, resource, objects);
		this.vos = objects;
		
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = inflater.inflate(R.layout.describe_global_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tvName = (TextView)convertView.findViewById(R.id.tv_name);
			viewHolder.tvLabel = (TextView)convertView.findViewById(R.id.tv_label);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		DescribeGlobalVo vo = vos.get(position);
		viewHolder.tvName.setText(vo.getName());
		viewHolder.tvLabel.setText(vo.getLabel());
		
		return convertView;
	}

	private class ViewHolder{
		TextView tvName;
		TextView tvLabel;
	}
}

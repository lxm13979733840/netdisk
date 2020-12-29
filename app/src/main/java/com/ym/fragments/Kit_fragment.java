package com.ym.fragments;

import java.util.ArrayList;
import java.util.List;


import com.ym.model.AnayM;
import com.ym.netdisk.DownloadedActivity;
import com.ym.netdisk.MyInfoActivity;
import com.ym.netdisk.PayActivity;
import com.ym.netdisk.R;
import com.ym.netdisk.TransmissionActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
/**
  * @Copyright (C)
  * @Author: ym
  * @Date:
  * @Description:
  */
public class Kit_fragment extends Fragment{
	ListView kit_listview;
	List<AnayM> list = new ArrayList<AnayM>();
//	Button btn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_kit, container, false);
		initView(view);
		addlist();
		Kit_Adapter adapter = new Kit_Adapter() ;
		kit_listview.setAdapter(adapter);
		kit_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch (arg2) {
				case 0:
					startActivity(new Intent(getActivity(),MyInfoActivity.class));
					break;
				case 1:
					startActivity(new Intent(getActivity(), PayActivity.class));
					break;
				case 2:
					startActivity(new Intent(getActivity(), DownloadedActivity.class));
					break;
				case 3:
					Intent intent  = new Intent(getActivity(), TransmissionActivity.class);
					intent.putExtra("temp", 0);
					startActivity(intent);
					break;
				default:
					break;
				}
			}
		});
		return view;

	}

	private void addlist() {
		list.add(new AnayM(R.drawable.men, "用户信息"));
		list.add(new AnayM(R.drawable.vip, "超级用户"));
		list.add(new AnayM(R.drawable.downloaded, "已下载"));
		list.add(new AnayM(R.drawable.translate, "正在传输"));
	}

	private void initView(View view) {
		kit_listview = (ListView) view.findViewById(R.id.kit_listview);
		//btn=(Button) view.findViewById(R.id.exit);
		//btn.setOnClickListener(this);
	}

	class Kit_Adapter extends BaseAdapter {

		@Override
		public int getCount() {

			return list.size();
		}

		@Override
		public Object getItem(int arg0) {

			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {

			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			if (arg1 == null)
				arg1 = LayoutInflater.from(getActivity()).inflate(
						R.layout.layout_listview, null);
			ImageView iv = (ImageView) arg1.findViewById(R.id.list_icon);
			TextView tv = (TextView) arg1.findViewById(R.id.list_tv);
			iv.setBackgroundResource(list.get(arg0).getIcon());
			tv.setText(list.get(arg0).getText().toString());
			return arg1;
		}

	}

/*	@Override
	public void onClick(View arg0) {
		int id=arg0.getId();
		 switch(id){
		 case R.id.exit:
			 BaseActivity.appexit();
			 break;
		 }
	}*/
}

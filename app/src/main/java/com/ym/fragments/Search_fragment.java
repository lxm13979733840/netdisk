package com.ym.fragments;

import java.util.ArrayList;
import java.util.List;

import com.ym.model.AnayM;
import com.ym.netdisk.R;
import com.ym.netdisk.Search_classActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
/**
  * @Copyright (C)
  * @Author: ym
  * @Date:
  * @Description:
  */
public class Search_fragment extends Fragment {
	List <AnayM> anayms =new ArrayList<AnayM>();
	GridView gv ;
	String searchclass;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_search, container, false);
		initView(view);
		Addanayms();
		GvAdapter adapter=new GvAdapter();
		gv.setAdapter(adapter);
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch (arg2) {
				case 0:
					searchclass ="file";
					break;
				case 1:
					searchclass ="image";
					break;
				case 2:
					searchclass="music";
					break;
				case 3:
					searchclass ="video";
					break;
				case 4:
					searchclass="document";
					break;
				default:
					break;
				}
				Intent intent =new Intent(getActivity(), Search_classActivity.class);
				intent.putExtra("searchclass", searchclass);
				startActivity(intent );
			}
		});
		return view;
	}
	private void Addanayms() {
		anayms.add(new AnayM(R.drawable.folder, "文件"));
		anayms.add(new AnayM(R.drawable.image, "照片"));
		anayms.add(new AnayM(R.drawable.music, "音乐"));
		anayms.add(new AnayM(R.drawable.video, "视频"));
		anayms.add(new AnayM(R.drawable.document, "文档"));
		
	}
	private void initView(View view) {
		gv =(GridView) view.findViewById(R.id.search_gv);
		
	}
	class GvAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			
			return anayms.size();
		}

		@Override
		public Object getItem(int arg0) {

			return anayms.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {

			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			if(arg1==null) arg1 =LayoutInflater.from(getActivity()).inflate(R.layout.layout_gridview, null);
			ImageView icon =(ImageView) arg1.findViewById(R.id.gv_icon);
			TextView text =(TextView) arg1.findViewById(R.id.gv_text);
			icon.setBackgroundResource(anayms.get(arg0).getIcon());
			text.setText(anayms.get(arg0).getText());
			return arg1;
		}
		
	}
}

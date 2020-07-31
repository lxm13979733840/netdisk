package com.wuming.netdisk;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.wuming.fragments.Downloads_fragment;
import com.wuming.model.Search_M;

import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DownloadedActivity extends Activity implements OnClickListener {
	List<Search_M> list = new ArrayList<Search_M>();
	ListView listview;
	ImageView downloaded_title_icon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_downloaded);
		listview = (ListView) findViewById(R.id.last_downloads_listview);
		downloaded_title_icon = (ImageView) findViewById(R.id.downloaded_title_icon);
		downloaded_title_icon.setOnClickListener(this);
		File loadfile = new File(Downloads_fragment.loadfile);
		if (!loadfile.exists()) {
			loadfile.mkdirs();
		}
		File[] files = loadfile.listFiles();
		for (File file : files) {
			Search_M m = new Search_M(file.getName(),
					switchingUnit(file.length()), date2Str(new Date(
							file.length())));
			list.add(m);
		}
		LastDownloadsAdapter adapter = new LastDownloadsAdapter();
		listview.setAdapter(adapter);
	}

	public static String date2Str(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.CHINA);
		return format.format(date);
	}

	public String switchingUnit(long lengths) {
		String sizes = null;
		if (lengths < 1024) {
			sizes = String.valueOf(lengths) + "B";
		} else if (lengths / 1024 < 1024) {
			sizes = String.valueOf(lengths / 1024) + "KB";
		} else if (lengths / (1024 * 1024) < 1024) {
			sizes = String.valueOf(lengths / (1024 * 1024)) + "MB";
		} else if (lengths / (1024 * 1024 * 1024) < 1024) {
			sizes = String.valueOf(lengths / (1024 * 1024 * 1024)) + "GB";
		}
		return sizes;
	}

	public class LastDownloadsAdapter extends BaseAdapter {

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
				arg1 = LayoutInflater.from(DownloadedActivity.this).inflate(
						R.layout.serach_list, null);
			ImageView icon = (ImageView) arg1
					.findViewById(R.id.search_list_item_icon);
			TextView name = (TextView) arg1
					.findViewById(R.id.search_list_item_name);
			TextView sizes = (TextView) arg1
					.findViewById(R.id.search_list_item_size);
			TextView time = (TextView) arg1
					.findViewById(R.id.search_list_item_time);
			/**
			 * 根据文件类型的不同给予文件不同的图标
			 */
			if (list.get(arg0).getName().endsWith(".txt")
					|| list.get(arg0).getName().endsWith(".doc")
					|| list.get(arg0).getName().endsWith(".docx")) {
				icon.setBackgroundResource(R.drawable.docfile);
			} else if (list.get(arg0).getName().endsWith(".jpg")
					|| list.get(arg0).getName().endsWith(".png")
					|| list.get(arg0).getName().endsWith(".gif")) {
				icon.setBackgroundResource(R.drawable.imgfile);
			} else if (list.get(arg0).getName().endsWith(".mp4")
					|| list.get(arg0).getName().endsWith(".avi")
					|| list.get(arg0).getName().endsWith(".rmvb")) {
				icon.setBackgroundResource(R.drawable.videofile);
			} else if (list.get(arg0).getName().endsWith(".mp3")
					|| list.get(arg0).getName().endsWith(".ape")) {
				icon.setBackgroundResource(R.drawable.micfile);
			} else {
				icon.setBackgroundResource(R.drawable.unknownfile);
			}
			name.setText(list.get(arg0).getName());
			sizes.setText(list.get(arg0).getSizes());
			time.setText(list.get(arg0).getLasttime());
			return arg1;
		}

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.downloaded_title_icon:
			this.finish();
			break;
		default:
			break;
		}

	}

}

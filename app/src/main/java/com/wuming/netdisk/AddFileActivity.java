package com.wuming.netdisk;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.wuming.model.Search_M;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AddFileActivity extends BaseActivity implements OnClickListener {
	private ImageView add_file_title_icon;
	private TextView add_file_right;
	private ListView add_file_listview;
	Add_Adapter adapter;
	List<Search_M> list = new ArrayList<Search_M>();
	File url = Environment.getExternalStorageDirectory();
	File presentfile = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		presentfile = url;
		adapter = new Add_Adapter();
		addlist(url);
		registerForContextMenu(add_file_listview);
		add_file_listview.setAdapter(adapter);
	}

	@SuppressWarnings("deprecation")
	public void addlist(File path) {
		presentfile = path;
		list.clear();
		File[] files = path.listFiles();
		if(files.length==0){
			adapter.notifyDataSetInvalidated();	
			return;
		}
		for (File file : files) {
			int ifdir = 0;
			if (!(file.isDirectory()))
				ifdir = 1;
			Calendar cal = Calendar.getInstance();
			long time = file.lastModified();
			cal.setTimeInMillis(time);
			Search_M m = new Search_M(file.getAbsolutePath(), file.getName(),
					cal.getTime().toLocaleString(),
					switchingUnit(file.length()), ifdir);
			list.add(m);
			adapter.notifyDataSetInvalidated();
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.add(0, 1, 0, "上传");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item
				.getMenuInfo();
		int position = menuInfo.position;
		Search_M m =list.get(position);
		switch (item.getItemId()) {
		case 1:
			Intent intent2 =new Intent(AddFileActivity.this, TransmissionActivity.class);
			intent2.putExtra("downloaderfile", m);
			intent2.putExtra("temp", 2);
			startActivity(intent2);
			break;

		default:
			break;
		}
		return true;
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

	@Override
	public void initView() {
		add_file_title_icon = (ImageView) findViewById(R.id.add_file_title_icon);
		add_file_right = (TextView) findViewById(R.id.add_file_right);
		add_file_listview = (ListView) findViewById(R.id.add_file_listview);

	}

	@Override
	public void initEvent() {
		add_file_title_icon.setOnClickListener(this);
		add_file_right.setOnClickListener(this);
		add_file_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (list.get(arg2).getIfdir() == 0)
					addlist(new File(list.get(arg2).getAbname()));
			}
		});
	}

	@Override
	public void init(Bundle savedInstanceState) {
		setContentView(R.layout.activity_add_file);

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.add_file_title_icon:
			this.finish();
			break;
		case R.id.add_file_right:
			if (presentfile.equals(url)) {
				Toast.makeText(AddFileActivity.this, "已经是第一页了",
						Toast.LENGTH_SHORT).show();
				return;
			}
			addlist(presentfile.getParentFile());
			break;
		default:
			break;
		}
	}

	class Add_Adapter extends BaseAdapter {

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
				arg1 = LayoutInflater.from(AddFileActivity.this).inflate(
						R.layout.serach_list, null);
			ImageView icon = (ImageView) arg1
					.findViewById(R.id.search_list_item_icon);
			TextView name = (TextView) arg1
					.findViewById(R.id.search_list_item_name);
			TextView sizes = (TextView) arg1
					.findViewById(R.id.search_list_item_size);
			TextView time = (TextView) arg1
					.findViewById(R.id.search_list_item_time);
			if (list.get(arg0).getIfdir() == 0) {
				icon.setBackgroundResource(R.drawable.folderfile);
			} else if (list.get(arg0).getName().endsWith(".txt")
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

}

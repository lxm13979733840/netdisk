package com.wuming.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wuming.http.HttpUtils;
import com.wuming.model.MyFile;
import com.wuming.model.Search_M;
import com.wuming.netdisk.R;
import com.wuming.netdisk.RenameFileActivity;
import com.wuming.netdisk.Search_FileActivity;
import com.wuming.netdisk.TransmissionActivity;
import com.wuming.storage.NNSharedpredpreferences;

import cz.msebera.android.httpclient.Header;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class MainPage_fragment extends Fragment implements OnClickListener {
	public static List<Search_M> list = new ArrayList<Search_M>();
	private ListView listview;
	public  MainPageAdapter adapter;
	public static List<MyFile> files = new ArrayList<MyFile>();
	Button mainpage_btn;
	static boolean temp =true ;
	int position = 0;
	public static int flag = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mainpage, container,
				false);
		initView(view);
		registerForContextMenu(listview);
		NNSharedpredpreferences nn = new NNSharedpredpreferences(getActivity());
		if(temp){
		MyFile file = new MyFile(null, nn.getUser().getUser_fatherDir());
		files.add(file);
		temp =false ;}
		String url =null ;
		if (files.get(flag).getName() == null) {
			url = files.get(flag).getParent();
		} else {
			url = files.get(flag).getParent() + "\\"
					+ files.get(flag).getName();
		}
		addlist(url);
		adapter = new MainPageAdapter();
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (list.get(arg2).getIfdir() == 0) {
					String url = null;
					if (files.get(flag).getName() == null) {
						url = files.get(flag).getParent();
					} else {
						url = files.get(flag).getParent() + "\\"
								+ files.get(flag).getName();
					}
					MyFile file = new MyFile(list.get(arg2).getName(), url);
					files.add(file);
					addlist(list.get(arg2).getAbname());
					flag++;
				}
			}
		});
		return view;
	}


	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.add(0, 1, 0, "删除");
		menu.add(0, 2, 0, "移动");
		menu.add(0, 3, 0, "重命名");
		menu.add(0, 4, 0, "下载");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item
				.getMenuInfo();
		position = menuInfo.position;
		Search_M m = list.get(position);
		switch (item.getItemId()) {
		case 1:
			RequestParams params = new RequestParams();
			params.put("file", m.getAbname());
			HttpUtils.post(HttpUtils.SERVLET_DELETEFILE, params,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONArray response) {
							super.onSuccess(statusCode, headers, response);
							boolean result = false;
							try {
								JSONObject object = response.getJSONObject(0);
								result = object.getBoolean("result");
								if (result) {
									list.remove(position);
									Toast.makeText(getActivity(), "删除成功",
											Toast.LENGTH_SHORT).show();
									adapter.notifyDataSetInvalidated();
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {
							super.onFailure(statusCode, headers, throwable,
									errorResponse);
							Toast.makeText(getActivity(), "网络连接失败",
									Toast.LENGTH_SHORT).show();
						}
					});
			break;
		case 2:
			Intent intent = new Intent(getActivity(), Search_FileActivity.class);
			intent.putExtra("move_file", m);
			startActivity(intent);
			break;
		case 3:
			Intent intents = new Intent(getActivity(), RenameFileActivity.class);
			intents.putExtra("rename_file", m);
			startActivity(intents);
			break;
		case 4:
			Intent intent2 =new Intent(getActivity(), TransmissionActivity.class);
			intent2.putExtra("downloaderfile", m);
			intent2.putExtra("temp", 1);
			startActivity(intent2);
			break;
		default:
			break;
		}
		return true;
	}

	public void addlist(String name) {
		RequestParams params = new RequestParams();
		params.put("filename", name);
		HttpUtils.post(HttpUtils.SERVLET_SCANFILE, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONArray response) {
						list.clear();
						super.onSuccess(statusCode, headers, response);
						try {
							for (int i = 0; i < response.length(); i++) {
								JSONObject object = response.getJSONObject(i);
								Search_M search = new Search_M(object
										.getString("fileAbsolutePath"), object
										.getString("filePath"), object
										.getString("fileLastModified"),
										switchingUnit(object
												.getLong("fileLength")), object
												.getInt("ifDir"));
								list.add(search);
							}
							adapter.notifyDataSetInvalidated();
						} catch (Exception e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {

						super.onFailure(statusCode, headers, throwable,
								errorResponse);
						Toast.makeText(getActivity(), "网络似乎有问题哦",
								Toast.LENGTH_SHORT).show();
					}
				});
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

	private void initView(View view) {
		listview = (ListView) view.findViewById(R.id.mainpage_listview);
		mainpage_btn = (Button) view.findViewById(R.id.mainpage_btn);
		mainpage_btn.setOnClickListener(this);
	}

	public class MainPageAdapter extends BaseAdapter {

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

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.mainpage_btn:
			if (flag == 0) {
				Toast.makeText(getActivity(), "已经是第一页了", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			files.remove(flag);
			flag--;
			String url = null;
			if (files.get(flag).getName() == null) {
				url = files.get(flag).getParent();
			} else {
				url = files.get(flag).getParent() + "\\"
						+ files.get(flag).getName();
			}
			addlist(url);
			break;
		}
	}

}

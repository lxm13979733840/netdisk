package com.wuming.netdisk;

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
import com.wuming.model.User;
import com.wuming.storage.NNSharedpredpreferences;

import cz.msebera.android.httpclient.Header;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

public class Search_FileActivity extends Activity implements OnClickListener {
	ListView listview;
	Button btn;
	TextView search_file_right;
	List<Search_M> list = new ArrayList<Search_M>();
	List<MyFile> files = new ArrayList<MyFile>();
	Search_FileAdapter adapter;
	ImageView search_file_title_icon;
	int flag = 0;
	Search_M movefile;
	User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search__file);
		listview = (ListView) findViewById(R.id.search_file_listview);
		btn = (Button) findViewById(R.id.btn);
		search_file_right = (TextView) findViewById(R.id.search_file_right);
		search_file_title_icon = (ImageView) findViewById(R.id.search_file_title_icon);
		btn.setOnClickListener(this);
		search_file_right.setOnClickListener(this);
		search_file_title_icon.setOnClickListener(this);
		movefile = (Search_M) getIntent().getSerializableExtra("move_file");
		adapter = new Search_FileAdapter();
		NNSharedpredpreferences shared = new NNSharedpredpreferences(
				Search_FileActivity.this);
		user = shared.getUser();
		MyFile file = new MyFile(null, user.getUser_fatherDir());
		files.add(file);
		String url =null ;
		
		if(files.get(flag).getName() ==null){
			url =files.get(flag).getParent();
		}else{
			url =files.get(flag).getParent()+"\\"+files.get(flag).getName();
		}
		addlist(url);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (list.get(arg2).getIfdir() == 0) {
					try{
					
					String name = list.get(arg2).getName();
					String url =null ;
					
					if(files.get(flag).getName() ==null){
						url =files.get(flag).getParent();
					}else{
						url =files.get(flag).getParent()+"\\"+files.get(flag).getName();
					}
					MyFile file = new MyFile(name, url);
					files.add(file);
					addlist(list.get(arg2).getAbname());
					flag++;
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		});
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
								list.add(search);}
								adapter.notifyDataSetInvalidated();
							}catch(Exception e){
								e.printStackTrace();}
							
							
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
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
						Toast.makeText(Search_FileActivity.this, "网络似乎有问题哦",
								Toast.LENGTH_SHORT).show();
					}
				});
	}

	class Search_FileAdapter extends BaseAdapter {

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
				arg1 = LayoutInflater.from(Search_FileActivity.this).inflate(
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

	@Override
	public void onClick(View arg0) {
		try{
			String url =null ;
			if(files.get(flag).getName() ==null){
				url =files.get(flag).getParent();
			}else{
				url =files.get(flag).getParent()+"\\"+files.get(flag).getName();
			}
		switch (arg0.getId()) {
		case R.id.btn:
			RequestParams params = new RequestParams();
			params.put("movefile", movefile.getAbname());
			
			params.put("targetDir",url);
			HttpUtils.post(HttpUtils.SERVLET_MOVEFILE, params,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONArray response) {
							super.onSuccess(statusCode, headers, response);
							try {
								boolean result = false;
								JSONObject jsonObject = response
										.getJSONObject(0);
								result = jsonObject.getBoolean("result");
								if (result) {
									Toast.makeText(Search_FileActivity.this,
											"移动成功", Toast.LENGTH_SHORT).show();
									Search_FileActivity.this.finish();
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
							Toast.makeText(Search_FileActivity.this,
									"网络似乎有问题哦", Toast.LENGTH_SHORT).show();
						}
					});
			break;
		case R.id.search_file_title_icon:
			this.finish();
			break;
		case R.id.search_file_right:
			if (flag == 0) {
				Toast.makeText(Search_FileActivity.this, "已经是第一页",
						Toast.LENGTH_SHORT).show();
				return;
			}
			files.remove(flag);
			flag--;
			addlist(url);
			break;
		}
	}catch(Exception e){
		e.printStackTrace();
	}
	}
}

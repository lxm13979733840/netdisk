package com.ym.fragments;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import com.ym.http.HttpUtils;
import com.ym.model.Search_M;
import com.ym.netdisk.R;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
/**
  * @Copyright (C)
  * @Author: ym
  * @Date:
  * @Description:
  */
@SuppressLint("HandlerLeak")
public class Downloads_fragment extends Fragment {
	public static List<Search_M> lists = new ArrayList<Search_M>();
	public static Map<String, Search_M> list = new HashMap<String, Search_M>();
	private Map<String, Handler> handlers = new HashMap<String, Handler>();
	public static final String loadfile = Environment
			.getExternalStorageDirectory().toString() + "/AIYUNPAN";
	private ListView download_listview;
	Download_Adapter Adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.download_layout, container, false);
		download_listview = (ListView) view
				.findViewById(R.id.download_listview);
		download_listview = (ListView) view
				.findViewById(R.id.download_listview);
		Bundle bundle = getArguments();
		if (bundle != null) {
			int temp = bundle.getInt("temp");
			if (temp == 1) {
				lists.add((Search_M) bundle.getSerializable("downloaderfile"));
				list.put(((Search_M) bundle.getSerializable("downloaderfile"))
						.getAbname(), (Search_M) bundle
						.getSerializable("downloaderfile"));
				Downloads(
						((Search_M) bundle.getSerializable("downloaderfile"))
								.getAbname(),
						((Search_M) bundle.getSerializable("downloaderfile"))
								.getName());
			}
		}
		Adapter = new Download_Adapter();
		download_listview.setAdapter(Adapter);
		return view;
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
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.e("asdasdas", "asdasdas");
		super.onDestroy();
	}

	public int chaint(String sizes) {
		int lengths = 0;
		if (sizes.endsWith("GB")) {
			lengths = (Integer.parseInt(sizes.substring(0, sizes.length() - 2)))
					* (1024 * 1024 * 1024);
		} else if (sizes.endsWith("MB")) {
			lengths = (Integer.parseInt(sizes.substring(0, sizes.length() - 2)))
					* (1024 * 1024);
		} else if (sizes.endsWith("KB")) {
			lengths = (Integer.parseInt(sizes.substring(0, sizes.length() - 2))) * (1024);
		} else if (sizes.endsWith("B")) {
			lengths = (Integer.parseInt(sizes.substring(0, sizes.length() - 2)));
		}
		return lengths;
	}

	public void Downloads(final String fileurl, final String loadfilename) {
		new Thread() {
			@Override
			public void run() {
				super.run();
				HttpURLConnection connection = null;
				InputStream is = null;
				DataOutputStream out = null;
				OutputStream os = null;
				try {
					URL url = new URL(HttpUtils.BASE_URL
							+ HttpUtils.SERVLET_DOWNLOADFILE);
					connection = (HttpURLConnection) url.openConnection();
					connection.setConnectTimeout(600000);
					connection.setRequestMethod("POST");
					out = new DataOutputStream(connection.getOutputStream());
					String content = "filename="
							+ URLEncoder.encode(fileurl, "UTF-8");
					out.writeBytes(content);
					File dir = new File(loadfile);
					if (!dir.exists()) {
						dir.mkdirs();
					}
					File file = new File(loadfile + "/" + loadfilename);
					if (file.exists()) {
						file.delete();
					}
					os = new FileOutputStream(file);
					is = connection.getInputStream();
					byte[] bytes = new byte[1024];
					int length = -1;

					while ((length = is.read(bytes)) != -1) {
						os.write(bytes, 0, length);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(getActivity(), "连接失败", Toast.LENGTH_LONG)
							.show();
				} finally {
					try {
						if (is != null) {
							is.close();
						}
						if (os != null) {
							os.close();
						}
						if (out != null) {
							out.flush();
							out.close();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		}.start();
	}

	class Download_Adapter extends BaseAdapter {

		@Override
		public int getCount() {
			return lists.size();
		}

		@Override
		public Object getItem(int arg0) {
			return lists.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@SuppressLint("SetTextI18n")
		@Override
		public View getView(final int arg0, View arg1, ViewGroup arg2) {
			if (arg1 == null)
				arg1 = LayoutInflater.from(getActivity()).inflate(
						R.layout.load_listview, null);
			final Search_M m = lists.get(arg0);
			ImageView load_listview_icon = (ImageView) arg1
					.findViewById(R.id.load_listview_icon);
			TextView load_listview_name = (TextView) arg1
					.findViewById(R.id.load_listview_name);
			TextView load_listview_size = (TextView) arg1
					.findViewById(R.id.load_listview_size);
			final TextView load_listview_load_size = (TextView) arg1
					.findViewById(R.id.load_listview_load_size);
			final ProgressBar bar = (ProgressBar) arg1
					.findViewById(R.id.load_listview_size_bar);
			load_listview_name.setText(lists.get(arg0).getName().toString());
			load_listview_size.setText("/"
					+ lists.get(arg0).getSizes().toString());
			handlers.put(m.getAbname(), new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					Bundle bundle = msg.getData();
					int lenght = bundle.getInt("lenght");
					load_listview_load_size.setText(switchingUnit(lenght));
					if (switchingUnit(lenght).equals(m.getSizes())) {
						Toast.makeText(getActivity(), m.getName() + "下载完成",
								Toast.LENGTH_SHORT).show();
					}
				}
			});
			if (lists.get(arg0).getIfdir() == 0) {
				load_listview_icon.setBackgroundResource(R.drawable.folderfile);
			} else if (lists.get(arg0).getName().endsWith(".txt")
					|| lists.get(arg0).getName().endsWith(".doc")
					|| lists.get(arg0).getName().endsWith(".docx")) {
				load_listview_icon.setBackgroundResource(R.drawable.docfile);
			} else if (lists.get(arg0).getName().endsWith(".jpg")
					|| lists.get(arg0).getName().endsWith(".png")
					|| lists.get(arg0).getName().endsWith(".gif")) {
				load_listview_icon.setBackgroundResource(R.drawable.imgfile);
			} else if (lists.get(arg0).getName().endsWith(".mp4")
					|| lists.get(arg0).getName().endsWith(".avi")
					|| lists.get(arg0).getName().endsWith(".rmvb")) {
				load_listview_icon.setBackgroundResource(R.drawable.videofile);
			} else if (lists.get(arg0).getName().endsWith(".mp3")
					|| lists.get(arg0).getName().endsWith(".ape")) {
				load_listview_icon.setBackgroundResource(R.drawable.micfile);
			} else {
				load_listview_icon
						.setBackgroundResource(R.drawable.unknownfile);
			}
			bar.setMax(chaint(((Search_M) list.get(m.getAbname())).getSizes()));
			try {
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						File file = new File(loadfile
								+ "/"
								+ ((Search_M) list.get(m.getAbname()))
										.getName());
						int lenght = (int) file.length();
						bar.setProgress(lenght);
						Bundle bundle = new Bundle();
						bundle.putInt("lenght", lenght);
						Message msg = new Message();
						msg.setData(bundle);
						handlers.get(m.getAbname()).sendMessage(msg);
						if (bar.getProgress() == bar.getMax()) {
							try {
								list.remove(m.getAbname());
								lists.remove(arg0);
								this.cancel();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}, 0, 500);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return arg1;
		}

	}

}

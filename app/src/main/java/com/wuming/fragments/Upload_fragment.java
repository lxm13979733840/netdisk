package com.wuming.fragments;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wuming.http.HttpUtils;
import com.wuming.model.MyFile;
import com.wuming.model.Search_M;
import com.wuming.netdisk.R;
import com.wuming.storage.NNSharedpredpreferences;
import cz.msebera.android.httpclient.Header;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak") public class Upload_fragment extends Fragment {
	public static List<String> lists = new ArrayList<String>();
	public static Map<String, Search_M> list = new HashMap<String, Search_M>();
	public static Map<String, Handler> handlers = new HashMap<String, Handler>();
	public static Map<String, MyFile> upbytes = new HashMap<String, MyFile>();
	public static Map<String ,ProgressBar> progress=new HashMap<String,ProgressBar>();
	private ListView upload_listview;
	UploadAdapter adapter;
	String userID = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.upload_layout, container, false);
		upload_listview = (ListView) view.findViewById(R.id.upload_listview);
		Bundle bundle = getArguments();
		if (bundle != null) {
			int temp = bundle.getInt("temp");
			if (temp == 2) {
				Search_M m = (Search_M) bundle
						.getSerializable("downloaderfile");
				lists.add(m.getAbname());
				list.put(m.getAbname(), m);
				String url = null;
				if (MainPage_fragment.files.get(MainPage_fragment.flag)
						.getName() == null) {
					url = MainPage_fragment.files.get(MainPage_fragment.flag)
							.getParent();
				} else {
					url = MainPage_fragment.files.get(MainPage_fragment.flag)
							.getParent()
							+ "\\"
							+ MainPage_fragment.files.get(
									MainPage_fragment.flag).getName();
				}
				UploadsFile(m.getAbname(), m.getName(), url,m.getAbname());
			}
		}
		NNSharedpredpreferences nnSharedpredpreferences = new NNSharedpredpreferences(
				getActivity());
		userID = nnSharedpredpreferences.getUser().getUser_ID();
		adapter = new UploadAdapter();
		upload_listview.setAdapter(adapter);
		
		return view;
	}

	private void UploadsFile(final String fileurl, final String filename,
			final String uploadfile,final String key) {
		RequestParams params =new RequestParams();
		try {
			
			params.put("upfilepath", uploadfile);
			params.put("filename", filename);
			params.put("upfile", new File(fileurl),"application/octet-stream");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		HttpUtils.post(HttpUtils.SERVLET_UPLOADFILE, params ,new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				Toast.makeText(getActivity(), "连接服务器失败", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				
			}
			@Override
			public void onProgress(long bytesWritten, long totalSize) {
				super.onProgress(bytesWritten, totalSize);
				ProgressBar bar=progress.get(key);
				if(bar.getMax()!=totalSize)
					bar.setMax((int)totalSize);
				bar.setProgress((int)bytesWritten);
				Bundle bundle =new Bundle();
				bundle.putInt("lenght", (int)bytesWritten);
				bundle.putInt("totalSize", (int)totalSize);
				Message msg =new Message();
				msg.setData(bundle);
				handlers.get(key).sendMessage(msg );
				if(bytesWritten==totalSize){
					list.remove(key);
					lists.remove(key);
					adapter.notifyDataSetInvalidated();
				}
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

	public int chaint(String sizes) {
		int lengths = 0;
		if (sizes.endsWith("GB")){
			lengths = (Integer.valueOf(sizes.substring(0, sizes.length() - 2)))
					* (1024 * 1024 * 1024);
		} else if (sizes.endsWith("MB")) {
			lengths = (Integer.valueOf(sizes.substring(0, sizes.length() - 2)))
					* (1024 * 1024);
		} else if (sizes.endsWith("KB")) {
			lengths = (Integer.valueOf(sizes.substring(0, sizes.length() - 2))) * (1024);
		} else if (sizes.endsWith("B")) {
			lengths = (Integer.valueOf(sizes.substring(0, sizes.length() - 2)));
		}
		return lengths;
	}

	class UploadAdapter extends BaseAdapter {

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

		@Override
		public View getView( int arg0, View arg1, ViewGroup arg2) {
			if (arg1 == null)
				arg1 = LayoutInflater.from(getActivity()).inflate(
						R.layout.load_listview, null);
			 final Search_M m = list.get(lists.get(arg0));
			ImageView load_listview_icon = (ImageView) arg1
					.findViewById(R.id.load_listview_icon);
			TextView load_listview_name = (TextView) arg1
					.findViewById(R.id.load_listview_name);
			TextView load_listview_size = (TextView) arg1
					.findViewById(R.id.load_listview_size);
			final TextView load_listview_load_size = (TextView) arg1
					.findViewById(R.id.load_listview_load_size);
			 ProgressBar bar = (ProgressBar) arg1
					.findViewById(R.id.load_listview_size_bar);
			if (m.getIfdir() == 0) {
				load_listview_icon.setBackgroundResource(R.drawable.folderfile);
			} else if (m.getName().endsWith(".txt")
					|| m.getName().endsWith(".doc")
					|| m.getName().endsWith(".docx")) {
				load_listview_icon.setBackgroundResource(R.drawable.docfile);
			} else if (m.getName().endsWith(".jpg")
					|| m.getName().endsWith(".png")
					|| m.getName().endsWith(".gif")) {
				load_listview_icon.setBackgroundResource(R.drawable.imgfile);
			} else if (m.getName().endsWith(".mp4")
					|| m.getName().endsWith(".avi")
					|| m.getName().endsWith(".rmvb")) {
				load_listview_icon.setBackgroundResource(R.drawable.videofile);
			} else if (m.getName().endsWith(".mp3")
					|| m.getName().endsWith(".ape")) {
				load_listview_icon.setBackgroundResource(R.drawable.micfile);
			} else {
				load_listview_icon
						.setBackgroundResource(R.drawable.unknownfile);
			}
			load_listview_name.setText(m.getName().toString());
			load_listview_size.setText("/"
					+ m.getSizes().toString());
			progress.put(m.getAbname(), bar);
			handlers.put(m.getAbname(), new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					Bundle bundle = msg.getData();
					int lenght = bundle.getInt("lenght");
					int totalSize =bundle.getInt("totalSize");
					load_listview_load_size.setText(switchingUnit(lenght));
					if(lenght ==totalSize){
						Toast.makeText(getActivity(), m.getName()+"下载完成", Toast.LENGTH_SHORT).show();
					};
				}
			});
			return arg1;
		}

	}

}

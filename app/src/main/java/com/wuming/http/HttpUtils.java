package com.wuming.http;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wuming.netdisk.LoginActivity;
import com.wuming.netdisk.MainActivity;
import com.wuming.netdisk.R;
import com.wuming.service.Http_service;
import com.wuming.storage.NNSharedpredpreferences;

import cz.msebera.android.httpclient.Header;

/**
 * http连接的辅助类
 * @author 吴铭
 */
public class HttpUtils {
	public static final String BASE_URL = "http://192.168.56.1:8080/NetDisk/servlet/"; // 连接服务器的基类地址
	//public static final String BASE_URL = "http://211.149.175.95:8080/NetDisk/servlet/"; // 连接服务器的基类地址
	public static final String SERVLET_LOGIN = "Login";
	public static final String SERVLET_REGIST = "Register";
	public static final String SERVLET_SEARCHFILE = "SearchFile";
	public static final String SERVLET_DELETEFILE = "DeleteFiles";
	public static final String SERVLET_MOVEFILE = "MoveFile";
	public static final String SERVLET_RENAMEFILE = "Rename";
	public static final String SERVLET_DOWNLOADFILE = "Download";
	public static final String SERVLET_UPLOADFILE = "Upload";
	public static final String SERVLET_SCANFILE = "ScanFile";
	public static final String SERVLET_GETINFO = "GetInfo";
	public static final String SERVLET_CREATENEWDIR = "CreateNewDir";
	public static final String SERVLET_UPDATEINFO = "UpdateInfo";
	public static AsyncHttpClient client = new AsyncHttpClient();

	/**
	 * 设置最长的连接时间
	 */
	public void setTimer() {
		client.setTimeout(50000);
	}

	/**
	 * client通过get方法于服务器连接
	 * 
	 * @param url
	 *            连接的服务器地址
	 * @param params
	 *            传入的参数
	 * @param responsehandler
	 *            接受参数
	 */
	public static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responsehandler) {
		client.get(getAbsoluteUrl(url), params, responsehandler);
	}

	/**
	 * client通过post方法于服务器连接
	 * 
	 * @param url
	 * @param params
	 * @param responsehandler
	 */
	public static void post(String url, RequestParams params,
			AsyncHttpResponseHandler responsehandler) {
		client.setURLEncodingEnabled(false);
		client.post(getAbsoluteUrl(url), params, responsehandler);
	}

	/**
	 * client下载大型文件的方法
	 * 
	 * @param url
	 * @param params
	 * @param responsehandler
	 */
	public static void download(String url, RequestParams params,
			FileAsyncHttpResponseHandler responsehandler) {
		client.get(getAbsoluteUrl(url), params, responsehandler);
		
	}

	/**
	 * 获取连接服务器绝对数据的方法
	 * 
	 * @param relativeUrl
	 *            访问的接口在服务器中的地址
	 * @return
	 */
	private static String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}

	public static void userLogin(final Activity activity,
			final String username, final String password) {
		RequestParams params = new RequestParams();
		params.put("username", username);
		params.put("password", Encrypt.MD5(password));
		post(SERVLET_LOGIN, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
				super.onSuccess(statusCode, headers, response);
				try {
					JSONObject jo = response.getJSONObject(0);
					boolean result = jo.getBoolean("result");
					if (result) {
						String userID = jo.getString("userID");
						String fatherDir = jo.getString("fatherDir");
						String componeneName = activity.getComponentName()
								.toString();
						if (componeneName
								.contains("com.wuming.netdisk.RegisterActivity")
								|| componeneName
										.contains("com.wuming.netdisk.LoginActivity")) {
							NNSharedpredpreferences nns = new NNSharedpredpreferences(
									activity);
							nns.setUser(userID, username, password, fatherDir);
							activity.startActivity(new Intent(activity,
									MainActivity.class));
							activity.startService(new Intent(activity,
									Http_service.class));
							activity.finish();
						}

					} else {
						Toast.makeText(activity, "对不起 ，你输入的用户名与密码不匹配",
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				Button btn=(Button) new LoginActivity().findViewById(R.id.login_btn);
				btn.setClickable(true);
				Toast.makeText(activity, "网络连接异常", Toast.LENGTH_SHORT).show();
			}
		});
	}
}

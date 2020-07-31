package com.wuming.storage;

import com.wuming.model.User;

import android.content.Context;
import android.content.SharedPreferences;

public class NNSharedpredpreferences {
	Context context;
	public static final String SP_USER_STORAGE = "sp_user";// 本地存储用户的文件名
	public static final String SP_USER_ID = "sp_user_id";// 用户的Id
	public static final String SP_USER_STORAGE_NAME = "sp_user_name";// 用户的用户名
	public static final String SP_USER_STIRAGE_PASSWORD = "sp_user_password";// 用户的密码
    public static final String SP_USER_FATHERDIR="sp_user_fatherDir";
	
	public NNSharedpredpreferences() {
	}

	public NNSharedpredpreferences(Context context) {
		this.context = context;
	}

	/**
	 * 调用SharedPerences对象的方法
	 * 
	 * @param sp_name
	 *            打开的本地存储文件名
	 * 
	 * @return SharedPerences对象
	 */
	@SuppressWarnings("static-access")
	public SharedPreferences getSharedPreferences(String sp_name) {
		SharedPreferences sp = context.getSharedPreferences(sp_name,
				context.MODE_PRIVATE);
		return sp;
	}

	/**
	 * 获取存储在本地数据库内的用户的数据
	 * 
	 * @return 获取到的本地的用户的数据
	 */
	public User getUser() {
		SharedPreferences sp = getSharedPreferences(SP_USER_STORAGE);
		User user = new User(sp.getString(SP_USER_ID,""),sp.getString(SP_USER_STORAGE_NAME, ""),
				sp.getString(SP_USER_STIRAGE_PASSWORD, ""),sp.getString(SP_USER_FATHERDIR, ""));
		return user;
	}

	/**
	 * 向本地数据库中存储当前登陆成功 的用户的信息
	 * 
	 * @param user_ID
	 *            当前登录用户的ID
	 * @param user_name
	 *            当前登录用户的用户名
	 * @param user_password
	 *            当前登录用户的用户名密码
	 */
	public void setUser(String user_ID, String user_name, String user_password,String fatherDir) {
		SharedPreferences sp = getSharedPreferences(SP_USER_STORAGE);
		sp.edit().putString(SP_USER_STORAGE_NAME, user_name)
				.putString(SP_USER_STIRAGE_PASSWORD, user_password)
				.putString(SP_USER_ID, user_ID).putString(SP_USER_FATHERDIR,fatherDir).commit();
	}

}

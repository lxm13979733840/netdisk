package com.wuming.netdisk;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
/**
 * app中所有Activity的基类
 * @author wuming
 *
 */
public abstract class BaseActivity extends Activity {
	public static List <Activity> activitys =new ArrayList <Activity>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		init(savedInstanceState);
		activitys.add(this);
		initView();
		initEvent();
	}
	/**
	 * app退出的方法
	 */
	public static void appexit(){
		for (Activity activity : activitys) {
			if(activity!=null&&!activity.isFinishing()){
				activity.finish();
			}
		}
	}
	
	/**
	 * 初始化界面控件的方法
	 */
    public abstract void initView();
    /**
     * 初始化界面动作的方法
     */
    public abstract void initEvent();
    /**
     * 加载界面布局的方法
     * @param savedInstanceState
     */
    public abstract void init(Bundle savedInstanceState);
}

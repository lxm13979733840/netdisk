package com.ym.myview;
/**
  * @Copyright (C)
  * @Author: ym
  * @Date:
  * @Description:
  */
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ym.netdisk.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

@SuppressLint("SimpleDateFormat") public class MyListView extends ListView implements OnScrollListener{
	private View head;// 顶部的布局；
	private int headHeight;// 顶部布局的上边距；
	private int scrollState;// ListView当前滚动状态
	private int firstVisibleItem;// 当前第一个可见的Item的位置；
	private boolean isRemark;// 标记当前是在ListView的顶端按下的；
	private int startY;// 按下的Y值；
	private int state;// 状态保存；
	private IReflashListener iReflashListener;//刷新数据的接口
	private final int NONE = 0;// 正常状态
	private final int PULL = 1;// 提示下拉刷新状态
	private final int RELESE = 2;// 提示松开刷新状态
	private final int REFLASHIHG = 3;// 正在刷新的状态
	public MyListView(Context context) {
		super(context);
		InitView(context);
	}

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		InitView(context);

	}

	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		InitView(context);

	}
	/**
	 * 初始化界面，添加顶部布局文件到ListView
	 * 
	 * @param context
	 */
	private void InitView(Context context) {
		head = LayoutInflater.from(context).inflate(R.layout.listview_head, null);
		measuteView(head);
		headHeight = head.getMeasuredHeight();
		Log.e("高度", "高度=" + headHeight);
		TopPadding(-headHeight);
		this.addHeaderView(head);
		this.setOnScrollListener(this);
	}
	/**
	 * 设置head布局的上边距
	 * @param topPadding
	 */
	private void TopPadding(int topPadding) {
		head.setPadding(head.getPaddingLeft(), topPadding,
				head.getPaddingRight(), head.getPaddingBottom());
		head.invalidate();
	}
	/**
	 * 通知父布局自布局占有的高度
	 * 
	 * @param view
	 */
	private void measuteView(View view) {
		ViewGroup.LayoutParams p = view.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int width = ViewGroup.getChildMeasureSpec(0, 0, p.width);
		int height;
		int tempHeight = p.height;
		if (tempHeight > 0) {
			height = MeasureSpec.makeMeasureSpec(tempHeight,
					MeasureSpec.EXACTLY);
		} else {
			height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		view.measure(width, height);
	}
	
	public void setInterface(IReflashListener iReflashListener){
		this.iReflashListener=iReflashListener;
	}
	public interface IReflashListener{
		public void onReflash();
	}
	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		firstVisibleItem = arg1;
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		scrollState = arg1;
		
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (firstVisibleItem == 0) {
				isRemark = true;
				startY = (int) ev.getY();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			onMove(ev);
			break;
		case MotionEvent.ACTION_UP:
			if (state == RELESE) {
				state = REFLASHIHG;
				// 加载最新数据
				reflashViewVyState();
				iReflashListener.onReflash();
			} else if (state == PULL) {
				state = NONE;
				isRemark = false;
				reflashViewVyState();
			}
			break;

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}
	/**
	 * 判断移动过程中的操作
	 * 
	 * @param ev
	 */
	private void onMove(MotionEvent ev) {
		if (!isRemark) {
			return;
		}
		int tempY = (int) ev.getY();
		int space = tempY - startY;
		int topPadding = space - headHeight;
		switch (state) {
		case NONE:
			TopPadding(topPadding);
			if (space > 0) {
				state = PULL;
				reflashViewVyState();
			}
			break;
		case PULL:
			TopPadding(topPadding);
			if (space > headHeight + 30
					&& scrollState == SCROLL_STATE_TOUCH_SCROLL) {
				state = RELESE;
				reflashViewVyState();
			}
			break;
		case RELESE:
			TopPadding(topPadding);
			if (space < headHeight + 30) {
				state = PULL;
				reflashViewVyState();
			} else if (space <= 0) {
				state = NONE;
				isRemark = false;
				reflashViewVyState();
			}
			break;
		case REFLASHIHG:

			break;
		default:
			break;
		}
	}
	/**
	 * 头部变化的方法
	 */

	private void reflashViewVyState() {
		TextView tip = (TextView) head.findViewById(R.id.head_title_text);
		ImageView arrow = (ImageView) head.findViewById(R.id.head_img);
		ProgressBar progress = (ProgressBar) head.findViewById(R.id.progress);
		RotateAnimation anim = new RotateAnimation(0, 180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		anim.setDuration(500);
		anim.setFillAfter(true);
		RotateAnimation anim1 = new RotateAnimation(180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		anim1.setDuration(500);
		anim1.setFillAfter(true);
		switch (state) {
		case NONE:
			arrow.clearAnimation();
			TopPadding(-(headHeight+15));
			break;
		case PULL:
			arrow.setVisibility(View.VISIBLE);
			progress.setVisibility(View.GONE);
			tip.setText("下拉可以刷新!");
			arrow.clearAnimation();
			arrow.setAnimation(anim1);
			break;
		case RELESE:
			arrow.setVisibility(View.VISIBLE);
			progress.setVisibility(View.GONE);
			tip.setText("松开可以刷新!");
			arrow.clearAnimation();
			arrow.setAnimation(anim);
			break;
		case REFLASHIHG:
			TopPadding(50);
			arrow.setVisibility(View.GONE);
			progress.setVisibility(View.VISIBLE);
			tip.setText("正在刷新...");
			arrow.clearAnimation();
			break;

		default:
			break;
		}
	}
	/**
	 * 获取完数据
	 */
	public void reflashComplete(){
		state=NONE;
		isRemark=false;
		reflashViewVyState();
		TextView lastupdatetime =(TextView) head.findViewById(R.id.head_data_text);
		SimpleDateFormat format= new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
		Date date =new Date(System.currentTimeMillis());
		String time=format.format(date);
		lastupdatetime.setText(time);			
	}
	
}

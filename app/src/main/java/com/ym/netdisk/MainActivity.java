package com.ym.netdisk;

import com.ym.fragments.Create_fragment;
import com.ym.fragments.Kit_fragment;
import com.ym.fragments.MainPage_fragment;
import com.ym.fragments.Search_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
  * @Copyright (C)
  * @Author: ym
  * @Date:
  * @Description:
  */
public class MainActivity extends FragmentActivity implements OnClickListener {
	private LinearLayout bottom_mainPage, bottom_create, bottom_search,
			bottom_kit;
	private TextView bottom_mainPage_text, bottom_create_text,
			bottom_search_text, bottom_kit_text;
	private ImageView bottom_mainPage_icon, bottom_create_icon,
			bottom_search_icon, bottom_kit_icon, add_file;
	private FragmentManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initView();
		initEvent();
		manager = getSupportFragmentManager();
		manager.beginTransaction()
				.add(R.id.main_context, new MainPage_fragment()).commit();
		initbottomstate();
	}

	public void initbottomstate() {
		bottom_mainPage_text.setTextColor(getResources()
				.getColor(R.color.black));
		bottom_create_text.setTextColor(getResources().getColor(R.color.gray));
		bottom_search_text.setTextColor(getResources().getColor(R.color.gray));
		bottom_kit_text.setTextColor(getResources().getColor(R.color.gray));

		bottom_mainPage_icon.setBackgroundResource(R.drawable.mainpage);
		bottom_create_icon.setBackgroundResource(R.drawable.createdir_off);
		bottom_search_icon.setBackgroundResource(R.drawable.search_off);
		bottom_kit_icon.setBackgroundResource(R.drawable.kit_off);
	}

	public void initbottomdefault() {
		bottom_mainPage_text
				.setTextColor(getResources().getColor(R.color.gray));
		bottom_create_text.setTextColor(getResources().getColor(R.color.gray));
		bottom_search_text.setTextColor(getResources().getColor(R.color.gray));
		bottom_kit_text.setTextColor(getResources().getColor(R.color.gray));

		bottom_mainPage_icon.setBackgroundResource(R.drawable.mainpage_off);
		bottom_create_icon.setBackgroundResource(R.drawable.createdir_off);
		bottom_search_icon.setBackgroundResource(R.drawable.search_off);
		bottom_kit_icon.setBackgroundResource(R.drawable.kit_off);
	}

	private void initEvent() {
		bottom_mainPage.setOnClickListener(this);
		bottom_create.setOnClickListener(this);
		bottom_search.setOnClickListener(this);
		bottom_kit.setOnClickListener(this);
		add_file.setOnClickListener(this);
	}

	private void initView() {
		bottom_mainPage = (LinearLayout) findViewById(R.id.bottom_mainPage);
		bottom_mainPage_text = (TextView) findViewById(R.id.bottom_mainPage_text);
		bottom_mainPage_icon = (ImageView) findViewById(R.id.bottom_mainPage_icon);

		bottom_create = (LinearLayout) findViewById(R.id.bottom_create);
		bottom_create_text = (TextView) findViewById(R.id.bottom_create_text);
		bottom_create_icon = (ImageView) findViewById(R.id.bottom_create_icon);

		bottom_search = (LinearLayout) findViewById(R.id.bottom_search);
		bottom_search_text = (TextView) findViewById(R.id.bottom_search_text);
		bottom_search_icon = (ImageView) findViewById(R.id.bottom_search_icon);

		bottom_kit = (LinearLayout) findViewById(R.id.bottom_kit);
		bottom_kit_text = (TextView) findViewById(R.id.bottom_kit_text);
		bottom_kit_icon = (ImageView) findViewById(R.id.bottom_kit_icon);

		add_file = (ImageView) findViewById(R.id.add_file);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.bottom_mainPage:
			manager.beginTransaction()
					.replace(R.id.main_context, new MainPage_fragment())
					.commit();
			initbottomstate();
			break;
		case R.id.bottom_create:
			manager.beginTransaction()
					.replace(R.id.main_context, new Create_fragment()).commit();
			initbottomdefault();
			bottom_create_text.setTextColor(getResources().getColor(
					R.color.black));
			bottom_create_icon.setBackgroundResource(R.drawable.createdir);
			break;
		case R.id.bottom_search:
			manager.beginTransaction()
					.replace(R.id.main_context, new Search_fragment()).commit();
			initbottomdefault();
			bottom_search_text.setTextColor(getResources().getColor(
					R.color.black));
			bottom_search_icon.setBackgroundResource(R.drawable.search);
			break;
		case R.id.bottom_kit:
			manager.beginTransaction()
					.replace(R.id.main_context, new Kit_fragment()).commit();
			initbottomdefault();
			bottom_kit_text
					.setTextColor(getResources().getColor(R.color.black));
			bottom_kit_icon.setBackgroundResource(R.drawable.kit);
			break;
		case R.id.add_file:
			startActivity(new Intent(MainActivity.this, AddFileActivity.class));
			break;
		default:
			break;
		}
	}

}

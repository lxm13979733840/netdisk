package com.wuming.netdisk;

import com.wuming.fragments.Downloads_fragment;
import com.wuming.fragments.Upload_fragment;
import com.wuming.model.Search_M;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TransmissionActivity extends FragmentActivity implements OnClickListener{
	FragmentManager manager ;
	private LinearLayout up_linearlayout ,down_linearlayout ;
	private TextView transmssion_download_fragment ,transmssion_upload_fragment ;
	private View transmssion_download_fragment_view ,transmssion_upload_fragment_view ;
	int temp =0;
	private ImageView transmssion_title_icon ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_transmission);
		initView();
		initEvent();
		temp =getIntent().getIntExtra("temp", 0);
		manager =getSupportFragmentManager();
		switch(temp){
		case 0:
			Upload_fragment upload_fragment =new Upload_fragment();
			Bundle bundle0 =new Bundle() ;
			bundle0.putInt("temp", 0);
			upload_fragment.setArguments(bundle0);
			manager.beginTransaction().add(R.id.transmission_content,upload_fragment).commit();
			inittab();
			break;
			
		case 1:
			Search_M m =(Search_M) getIntent().getSerializableExtra("downloaderfile");
			tabinit();
			transmssion_download_fragment.setTextColor(getResources().getColor(R.color.black));
			transmssion_download_fragment_view.setBackgroundColor(getResources().getColor(R.color.blue));
			Downloads_fragment downloads_fragment =new Downloads_fragment();
			Bundle bundle1 =new Bundle() ;
			bundle1.putSerializable("downloaderfile", m);
			bundle1.putInt("temp", 1);
			downloads_fragment.setArguments(bundle1);
			try{
			manager.beginTransaction().add(R.id.transmission_content, downloads_fragment).commit();
			}catch(Exception e){
				e.printStackTrace();
			}
			break;
		case 2:
			Search_M m2 = (Search_M) getIntent().getSerializableExtra(
					"downloaderfile");
			Upload_fragment upload_fragment2 = new Upload_fragment();
			Bundle bundle2 = new Bundle();
			bundle2.putInt("temp", temp);
			bundle2.putSerializable("downloaderfile", m2);
			upload_fragment2.setArguments(bundle2);
			manager.beginTransaction()
					.add(R.id.transmission_content, upload_fragment2).commit();
			inittab();
			break;
		}
		
	}
	/**
	 * 进来时  初始的tab标签的状态
	 */
	
	private void inittab() {
		transmssion_download_fragment.setTextColor(getResources().getColor(R.color.noselect_text));
		transmssion_upload_fragment.setTextColor(getResources().getColor(R.color.black));
		
		transmssion_download_fragment_view.setBackgroundColor(getResources().getColor(R.color.noselect_text));
		transmssion_upload_fragment_view.setBackgroundColor(getResources().getColor(R.color.blue));
	}
	/**
	 * 将tab标签中的所有状态都设置为未选中的状态
	 */
	private void tabinit(){
		transmssion_download_fragment.setTextColor(getResources().getColor(R.color.noselect_text));
		transmssion_upload_fragment.setTextColor(getResources().getColor(R.color.noselect_text));
		
		transmssion_download_fragment_view.setBackgroundColor(getResources().getColor(R.color.noselect_text));
		transmssion_upload_fragment_view.setBackgroundColor(getResources().getColor(R.color.noselect_text));
	}

	private void initView() {
		transmssion_download_fragment =(TextView) findViewById(R.id.transmssion_download_fragment);
		transmssion_upload_fragment =(TextView) findViewById(R.id.transmssion_upload_fragment);
		
		transmssion_download_fragment_view =findViewById(R.id.transmssion_download_fragment_view);
		transmssion_upload_fragment_view =findViewById(R.id.transmssion_upload_fragment_view);
		
		up_linearlayout =(LinearLayout) findViewById(R.id.up_linearlayout);
		down_linearlayout =(LinearLayout) findViewById(R.id.down_linearlayout);
		transmssion_title_icon =(ImageView) findViewById(R.id.transmssion_title_icon);
	}
	private void initEvent() {
		up_linearlayout.setOnClickListener(this);
		down_linearlayout.setOnClickListener(this);
		transmssion_title_icon.setOnClickListener(this);
	}


	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.up_linearlayout:
			tabinit();
			manager.beginTransaction().replace(R.id.transmission_content, new Upload_fragment()).commit();
			inittab();
			break;
		case R.id.down_linearlayout:
			tabinit();
			transmssion_download_fragment.setTextColor(getResources().getColor(R.color.black));
			transmssion_download_fragment_view.setBackgroundColor(getResources().getColor(R.color.blue));
			manager.beginTransaction().replace(R.id.transmission_content, new Downloads_fragment()).commit();
			break;
		case R.id.transmssion_title_icon:
			this.finish();
			break;
		default:
			Toast.makeText(TransmissionActivity.this, " "+arg0.getId(), Toast.LENGTH_LONG).show();
			break;
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.e("asdasd", "bbbbbbbbbbasdasd");
		super.onDestroy();
	}

}

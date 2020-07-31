package com.wuming.netdisk;

import com.wuming.sqlitehelp.DataBaseHelper;
import com.wuming.storage.NNSharedpredpreferences;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PayActivity extends BaseActivity {
	private TextView pay_yes_vip;
	private LinearLayout pay_no_vip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NNSharedpredpreferences nnSharedpredpreferences = new NNSharedpredpreferences(
				PayActivity.this);
		DataBaseHelper.DBhelper(PayActivity.this, nnSharedpredpreferences
				.getUser().getUser_name());
		 Cursor c = DataBaseHelper.sdb.rawQuery(
				"select ifVip from userinfo where user_id ='"
						+ nnSharedpredpreferences.getUser().getUser_ID() + "'",
				null);
		 int ifvip =0 ;
		 while(c.moveToNext()){
			 ifvip =c.getInt(c.getColumnIndex("ifVip"));
		 }
		 if(ifvip==0){
			 pay_no_vip.setVisibility(View.VISIBLE);
		 }else{
			 pay_yes_vip.setVisibility(View.VISIBLE);
		 }
	}

	@Override
	public void initView() {
		pay_yes_vip = (TextView) findViewById(R.id.pay_yes_vip);
		pay_no_vip = (LinearLayout) findViewById(R.id.pay_no_vip);
	}

	@Override
	public void initEvent() {

	}

	@Override
	public void init(Bundle savedInstanceState) {
		setContentView(R.layout.activity_pay);
	}

}

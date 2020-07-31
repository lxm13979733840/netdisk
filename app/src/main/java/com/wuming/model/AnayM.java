package com.wuming.model;

/**
 * 用于ListView 和GridView 的对象
 * @author 吴铭
 *
 */
public class AnayM {
	private int icon ;
	private String text ;
	public AnayM(int icon , String text) {
		this.icon =icon ;
		this.text=text ;
	}
	public AnayM() {
		
	}
	public AnayM(String text){
		this.text =text ;
	}
	public int getIcon() {
		return icon;
	}
	public void setIcon(int icon) {
		this.icon = icon;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

}

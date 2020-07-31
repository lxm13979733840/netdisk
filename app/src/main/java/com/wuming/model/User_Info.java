package com.wuming.model;

public class User_Info {
	private String user_id ;
	private String nick ;
	private String sex ;
	private String sizes ;
	private String used ;
	private int ifVip;
	public int getIfVip() {
		return ifVip;
	}
	public void setIfVip(int ifVip) {
		this.ifVip = ifVip;
	}
	public User_Info() {
	}
	public User_Info(String user_id ,String nick ,String sex ,String sizes ,String used ,int ifVip) {
		this.nick=nick;
		this.used=used;
		this.sex=sex;
		this.sizes=sizes;
		this.user_id=user_id;
		this.ifVip=ifVip;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getSizes() {
		return sizes;
	}
	public void setSizes(String sizes) {
		this.sizes = sizes;
	}
	public String getUsed() {
		return used;
	}
	public void setUsed(String used) {
		this.used = used;
	}

}

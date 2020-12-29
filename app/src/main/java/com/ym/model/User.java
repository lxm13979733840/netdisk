package com.ym.model;
/**
  * @Copyright (C)
  * @Author: ym
  * @Date:
  * @Description:
  */
public class User {
	private String user_ID;
	private String user_name;
	private String user_password;
    private String user_fatherDir;
	
	public User() {

	}

	public User(String user_name, String user_password) {
		this.user_name = user_name;
		this.user_password = user_password;
	}

	public User(String user_ID, String user_name, String user_password) {
		this.user_ID = user_ID;
		this.user_name = user_name;
		this.user_password = user_password;
	}
	
	public User(String user_ID, String user_name, String user_password,String user_fatherDir) {
		this.user_ID = user_ID;
		this.user_name = user_name;
		this.user_password = user_password;
		this.user_fatherDir=user_fatherDir;
	}

	public String getUser_fatherDir() {
		return user_fatherDir;
	}

	public void setUser_fatherDir(String user_fatherDir) {
		this.user_fatherDir = user_fatherDir;
	}

	public String getUser_ID() {
		return user_ID;
	}

	public void setUser_ID(String user_ID) {
		this.user_ID = user_ID;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_password() {
		return user_password;
	}

	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}

}

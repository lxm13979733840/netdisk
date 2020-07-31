package com.wuming.model;

import java.io.Serializable;


@SuppressWarnings("serial")
public class Search_M implements Serializable{
	private String name;//搜索传回来的文件名
	private String lasttime;//该文件上次修改的时间
	private String sizes;//文件的大小
	private String abname;//搜索传回来的文件的绝对路径
	private int ifdir ;//判断搜索传回来的是否为文件夹0是，1不是;

	public String getAbname() {
		return abname;
	}

	public void setAbname(String abname) {
		this.abname = abname;
	}

	public int getIfdir() {
		return ifdir;
	}

	public void setIfdir(int ifdir) {
		this.ifdir = ifdir;
	}

	public Search_M() {
	}
	public Search_M(String name ,String sizes ,String lasttime) {
		this.name =name ;
		this.sizes=sizes;
		this.lasttime=lasttime;
	}

	public Search_M(String abname,String name, String lasttime, String sizes,int ifdir) {
		this.name = name;
		this.lasttime = lasttime;
		this.sizes = sizes;
		this.abname=abname;
		this.ifdir=ifdir;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLasttime() {
		return lasttime;
	}

	public void setLasttime(String lasttime) {
		this.lasttime = lasttime;
	}

	public String getSizes() {
		return sizes;
	}

	public void setSizes(String sizes) {
		this.sizes = sizes;
	}

}

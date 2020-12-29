package com.ym.model;
/**
  * @Copyright (C)
  * @Author: ym
  * @Date:
  * @Description:
  */
public class MyFile {
	private String name ;
	private String parent ;
	private long lastupbytes;
	private long upfiles ;
	public MyFile(long lastupbytes ,long upfiles) {
		this.lastupbytes=lastupbytes;
		this.upfiles =upfiles;
	}
	public long getLastupbytes() {
		return lastupbytes;
	}
	public void setLastupbytes(long lastupbytes) {
		this.lastupbytes = lastupbytes;
	}
	public long getUpfiles() {
		return upfiles;
	}
	public void setUpfiles(long upfiles) {
		this.upfiles = upfiles;
	}
	public MyFile() {
	}
	public MyFile(String name ,String parent) {
		this.name=name ;
		this.parent=parent;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	

}

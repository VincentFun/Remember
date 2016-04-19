package com.example.meihui.remember.model;

/**
 * Created by meihui on 2016/4/5.
 * 单词查询-例句Model
 */
public class Sent {

	private String orig;   //原词
	private String trans;    //翻译

	public String getOrig() {
		return orig;
	}
	public void setOrig(String orig) {
		this.orig = orig;
	}

	public String getTrans() {
		return trans;
	}
	public void setTrans(String trans) {
		this.trans = trans;
	}

	@Override
	public String toString() {
		return orig;
	}
}

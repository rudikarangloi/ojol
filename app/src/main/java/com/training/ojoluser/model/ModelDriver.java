package com.training.ojoluser.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelDriver{

	@SerializedName("result")
	private String result;

	@SerializedName("msg")
	private String msg;

	@SerializedName("data")
	private List<DataDriver> data;

	public void setResult(String result){
		this.result = result;
	}

	public String getResult(){
		return result;
	}

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}

	public void setData(List<DataDriver> data){
		this.data = data;
	}

	public List<DataDriver> getData(){
		return data;
	}
}
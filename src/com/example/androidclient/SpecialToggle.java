package com.example.androidclient;

import android.content.Context;
import android.widget.ToggleButton;

public class SpecialToggle extends ToggleButton{
	
	private String nodeAddress;
	

	public SpecialToggle(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public void setAddress(String nodeAddress)
	{
		this.nodeAddress = nodeAddress;
	}
	
	public String getAddress(){
		return nodeAddress;
	}

}

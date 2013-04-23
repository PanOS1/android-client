package com.example.androidclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HomeFront extends Activity {
	private HomeFront instance;

	public void onCreate(Bundle savedInstanceState) {
		
	     instance = this;
		
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main);
		
		Button button = new Button(this);
		button = (Button)findViewById(R.id.button1);
		
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(instance, MainActivity.class);
				startActivity(intent);
			}
		});
	}

}

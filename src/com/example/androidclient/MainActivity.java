package com.example.androidclient;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static MainActivity instance;
	private LinearLayout ll;
	private ScrollView sv;
	private boolean clicked = false;
	private static int oldNumber;
	private static ArrayList<SpecialToggle> switches;
	private RelativeLayout rl;
	private TableLayout table;
	private TableRow row;
	private static TableLayout.LayoutParams tableParams;
	private static TableRow.LayoutParams rowParams;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// inisialisasi instansi
		instance = this;

		// inisialisasi tabel layout
		tableParams = new TableLayout.LayoutParams(
				TableLayout.LayoutParams.WRAP_CONTENT,
				TableLayout.LayoutParams.WRAP_CONTENT);

		rowParams = new TableRow.LayoutParams(
				TableRow.LayoutParams.WRAP_CONTENT,
				TableRow.LayoutParams.WRAP_CONTENT);

		// inisialisasi scroll view
		sv = new ScrollView(instance);

		// inisisalisasi linear layout
		ll = new LinearLayout(instance);

		// inisialisasi relative layout
		rl = new RelativeLayout(instance);
		//rl.setBackgroundColor(Color.BLUE);

		// switches merupakan kumpulan ToggleButton
		switches = new ArrayList<SpecialToggle>();

		// menambahkan scroll view ke dalam relative layout
		rl.addView(sv);

		// set orientasi dari list layout ke dalam vertical layout
		ll.setOrientation(LinearLayout.VERTICAL);

		// menambahkan linear layout ke dalam scroll view
		sv.addView(ll);

		// text untuk judul
		TextView tv = new TextView(instance);
		tv.setGravity(Gravity.CENTER | Gravity.TOP);
	
		tv.setText("Halo Dunia");
		tv.setTextSize(20);

		// tambahkan text ke dalam list view
		ll.addView(tv);

		// membuat tombol untuk menggernasikan switchh
		Button b = new Button(instance);
		b.setText("I do dynamically add toggle on your listview");

		// menambahkan tombol ke dalam list view
		ll.addView(b);

		// jika tombol di klik
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (clicked)
					switches.clear();
				for (int i = 0; i < oldNumber; i++) {
					ll.removeViewAt((oldNumber + 1) - i);
				}

				Random random = new Random();
				int count = random.nextInt(5) + 1;
				for (int i = 0; i < count; i++) {
					table = new TableLayout(instance);
					table.setLayoutParams(tableParams);

					row = new TableRow(instance);
					row.setLayoutParams(rowParams);

					TextView text = new TextView(instance);
					
					text.setLayoutParams(rowParams);
					
					

					final SpecialToggle toggle = new SpecialToggle(instance);
					toggle.setLayoutParams(rowParams);

					toggle.setId(i + 1);
					text.setText("Node "+toggle.getId()+",dengan alamat Node:"+toggle.getAddress());
					toggle.setChecked(random.nextBoolean());
					toggle.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (toggle.isChecked()) {
								Log.v("Coba: ", toggle.getId() + " Checked");
								Toast toast = Toast.makeText(instance, "Lamp "
										+ toggle.getId() + " is ON",
										Toast.LENGTH_SHORT);
								toast.show();
							} else {
								Log.v("Coba: ", toggle.getId() + " Unchecked");
								Toast toast = Toast.makeText(instance, "Lamp "
										+ toggle.getId() + " is OFF",
										Toast.LENGTH_SHORT);
								toast.show();
							}
						}
					});
					switches.add(toggle);
					
					//menambahkan tombol dan text ke dalam tabel view
					row.addView(switches.get(i));
					row.addView(text);
					table.addView(row);
					table.setGravity(Gravity.CENTER);
					ll.addView(table);
				}

				clicked = true;

				oldNumber = count;
			}

		});
		instance.setContentView(rl);
	}

}

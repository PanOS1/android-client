package com.example.androidclient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
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
	private Communicator comm;

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
		// rl.setBackgroundColor(Color.BLUE);

		// switches merupakan kumpulan ToggleButton
		switches = new ArrayList<SpecialToggle>();

		// menambahkan scroll view ke dalam relative layout
		rl.addView(sv);

		// set orientasi dari list layout ke dalam vertical layout sehingga akan tampil ke bawah tidak ke samping
		ll.setOrientation(LinearLayout.VERTICAL);

		// menambahkan linear layout ke dalam scroll view
		sv.addView(ll);

		// text untuk judul
		TextView tv = new TextView(instance);
		tv.setGravity(Gravity.CENTER | Gravity.TOP);

		//terima address dari parameter
		String address = this.getIntent().getStringExtra("IP_ADDR");
		comm = new Communicator(address);
		
		//koneksikan
		comm.connect();

		if (comm.isConnected()) {
			tv.setText("Connected to: " + address);
			tv.setTextSize(20);

			// tambahkan text ke dalam list view
			ll.addView(tv);

			// membuat tombol untuk menggernasikan switchh
			Button b = new Button(instance);
			b.setGravity(Gravity.CENTER);
			b.setText("Node Discovery");

			// menambahkan tombol ke dalam list view
			ll.addView(b);

			// jika tombol di klik
			b.setOnClickListener(new OnClickListener() {

				@SuppressWarnings("unchecked")
				@Override
				public void onClick(View v) {

					comm.sendString(Message.ND+"\n");
					ArrayList<String> nodes = new ArrayList<String>();
					ObjectInputStream input = comm.getStreamObject();
					try {
						nodes = (ArrayList<String>)input.readObject();
					} catch (OptionalDataException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (clicked)
						switches.clear();
					
					for (int i = 0; i < oldNumber; i++) {
						ll.removeViewAt((oldNumber + 1) - i);

					}

					int count = nodes.size();
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
						final String address = nodes.get(i).split(":")[0]+":"+nodes.get(i).split(":")[1];
						toggle.setAddress(address);
						text.setText("Node " + toggle.getId()
								+ ",dengan alamat Node:" + toggle.getAddress());
						boolean lampState = false;
						
						if(nodes.get(i).split(":")[2].equals("1"))
							lampState = true;
						
						toggle.setChecked(lampState);
						toggle.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								if (toggle.isChecked()) {
									comm.sendString(Message.OFF+","+address+"\n");
									Log.v("Coba: ", toggle.getId() + " Checked");
									Toast toast = Toast.makeText(instance, "Lamp "
											+ toggle.getId() + " is ON",
											Toast.LENGTH_SHORT);
									toast.show();
								} else {
									comm.sendString(Message.ON+","+address+"\n");
									Log.v("Coba: ", toggle.getId() + " Unchecked");
									Toast toast = Toast.makeText(instance, "Lamp "
											+ toggle.getId() + " is OFF",
											Toast.LENGTH_SHORT);
									toast.show();
								}
							}
						});
						switches.add(toggle);

						// menambahkan tombol dan text ke dalam tabel view
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
		}
		
		else
		{
			tv.setText("Koneksi gagal");
			ll.addView(tv);
		}

		
		instance.setContentView(rl);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			comm.sendString(Message.QUIT);
			comm.close();
			instance.finish();
			oldNumber = 0;
			return true;
		}
		return false;
	}

}

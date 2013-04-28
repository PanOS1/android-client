package com.example.androidclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

public class Communicator {

	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	private String address;

	private final int PORT = 2013;

	public Communicator() {
	}

	public Communicator(String address) {
		this.address = address;
	}

	public void connect() {
		try {
			socket = new Socket(address, PORT);
			//socket.connect(new InetSocketAddress(address, PORT), 2000);
			out = new PrintWriter(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
		} catch (UnknownHostException e) {
			Log.v("exception", "Host not found");
			e.printStackTrace();
		} catch (IOException e) {
			Log.v("exception", "Bad IO");
		}
	}

	public ObjectInputStream getStreamObject() {
		ObjectInputStream object = null;
		try {
			object = new ObjectInputStream(socket.getInputStream());
		} catch (StreamCorruptedException e) {
			Log.v("exception", "Stream corrupted");
		} catch (IOException e) {
			Log.v("exception", "Object input Stream error");
		}
		return object;
	}

	public void sendString(String message) {
		out.write(message);
		out.flush();
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void close() {
		if (this.socket != null) {
			try {
				socket.close();
			
				
				out.close();
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public boolean isConnected(){
		if(this.socket != null){
			return true;
		}
		else return false;
	}

}

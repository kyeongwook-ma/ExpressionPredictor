package message;

import com.google.gson.Gson;

public class Message {
	protected byte[] data;
	
	public String makeJson() {
		return new Gson().toJson(this);
	}
}

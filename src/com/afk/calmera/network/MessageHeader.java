package com.afk.calmera.network;



import java.io.Serializable;

public class MessageHeader implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1881184878962429215L;
	public static final int FILE_UPLOAD_REQ = 1;
	public static final int FILE_UPLOAD_RESP = 2;
	public static final int LABEL_DOWNLOAD_REQ = 3;
	public static final int LABEL_DOWNLOAD_RESP = 4;
	public static final int RECOGNIZE_REQ = 5;
	public static final int RECOGNIZE_RESP = 6;
	public static final int ERROR = -1;

	public MessageHeader() {}
		
	public MessageHeader(int msgType, String fileName) {
		super();
		this.msgType = msgType;
		this.fileName = fileName;
	}
	
	private int msgType;
	private int bodySize;
	private String fileName;

	public String getFileName() {
		return fileName;
	}
	
	public int getBodySize() {
		return bodySize;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	


}

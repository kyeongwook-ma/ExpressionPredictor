package com.afk.calmera.network;


import java.io.Serializable;

public  class Message implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6159051571891430028L;
	private MessageHeader header;
	private MessageBody body;

	public Message(MessageHeader header, MessageBody body) {
		this.body = body;
		this.header = header;
	}

	public Message() {}

	public Message(int msgType, String fileName, byte[] data) {
		
		this.header = new MessageHeader(msgType, fileName);
		this.body = new MessageBody();
		body.setData(data);		
	}
	
	public int getMsgType() {
		return header.getMsgType();
	}

	public MessageBody getBody() {
		return body;
	}

	public MessageHeader getHeader() {
		return header;
	}

	public void setHeader(MessageHeader header) {
		this.header = header;
	}
	

}

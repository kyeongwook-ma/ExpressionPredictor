package com.afk.calmera.network;



import java.io.Serializable;

public class MessageBody implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3856263375929806061L;
	private byte[] data;

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
	
	

}

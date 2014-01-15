package com.nickan.epiphany3D.model.messagingsystem;

public class Telegram {
	public int senderId;
	public int receiverId;
	float dispatchTime;
	public Message msg;
	public Object extraInfo;
	
	public enum Message { ATTACKED_BY_SENDER, ATTACK_RESPONSE, LOCATION_REQUEST, LOCATION_RESPONSE, 
		PATH_FIND_NODE, TARGETED_BY_SENDER, TARGETED_RESPONSE };

	public Telegram(int senderId, int receiverId, float dispatchTime, Message msg, Object extraInfo) {
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.dispatchTime = dispatchTime;
		this.msg = msg;
		this.extraInfo = extraInfo;
	}

}

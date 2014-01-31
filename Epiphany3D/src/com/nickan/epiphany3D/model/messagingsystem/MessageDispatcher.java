package com.nickan.epiphany3D.model.messagingsystem;

import java.util.ArrayList;
import java.util.List;

import com.nickan.epiphany3D.model.Entity;
import com.nickan.epiphany3D.model.messagingsystem.Telegram.Message;

/**
 * Handles the messages to be dispatched
 * @author Nickan
 *
 */
public class MessageDispatcher {
	private static final MessageDispatcher instance = new MessageDispatcher();
	private List<Telegram> telegrams = new ArrayList<Telegram>();

	private MessageDispatcher() {
		telegrams.clear();
	}

	public void update(float delta) {
		for (Telegram telegram: telegrams) {
			telegram.dispatchTime -= delta;

			if (telegram.dispatchTime <= 0) {
				dischargeDelayedMessage(telegram);
				break;
			}
		}

	}

	private void dischargeDelayedMessage(Telegram telegram) {
		sendMessage(telegram);

		// remove it from the list
		telegrams.remove(telegram);
	}

	private void sendMessage(Telegram telegram) {
		EntityManager manager = EntityManager.getInstance();
		Entity receiver = manager.getEntity(telegram.receiverId);
		receiver.handleMessage(telegram);
		
		//...
//		System.out.println("Send message");
	}

	public void dispatchMessage(int senderId, int receiverId, float dispatchTime, Message msg, Object extraInfo) {
		if (dispatchTime <= 0) {
			sendMessage(new Telegram(senderId, receiverId, dispatchTime, msg, extraInfo));
		} else {
			telegrams.add(new Telegram(senderId, receiverId, dispatchTime, msg, extraInfo));
		}
	}

	public static final MessageDispatcher getInstance() {
		return instance;
	}
}

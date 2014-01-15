package com.nickan.epiphany3D.model.state;

import com.nickan.epiphany3D.model.messagingsystem.Telegram;

public interface BaseState<Owner> {

	void start(Owner entity);

	void update(Owner entity);

	void exit(Owner entity);

	boolean onMessage(Telegram telegram);

}

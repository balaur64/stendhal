package games.stendhal.server.entity.mapstuff.sign;

import games.stendhal.server.core.events.UseListener;
import games.stendhal.server.entity.RPEntity;
import marauroa.common.game.RPEvent;
import marauroa.common.game.RPObject;


public class TradeCenterSign extends Sign implements UseListener {
	
	public TradeCenterSign() {
		super();
		setRPClass("sign");
		put("type", "tradecentersign");
		setResistance(100);
		setText("Offers of players");
		setDescription("This sign shows all offers of Players.");
	}
	
	public TradeCenterSign(RPObject object) {
		super(object);
		setResistance(100);
	}

	public boolean onUsed(RPEntity user) {
		//TODO: send display offer panel event/message to user
		return true;
	}

}

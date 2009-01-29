/* $Id$ */
/***************************************************************************
 *                      (C) Copyright 2003 - Marauroa                      *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.server.core.rule.defaultruleset;

import games.stendhal.server.core.engine.ItemLogger;
import games.stendhal.server.core.rule.ActionManager;
import games.stendhal.server.entity.RPEntity;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.item.Stackable;
import games.stendhal.server.entity.item.StackableItem;

import java.util.List;

import marauroa.common.game.RPObject;
import marauroa.common.game.RPSlot;

/**
 * 
 * @author Matthias Totz
 */
public class DefaultActionManager implements ActionManager {

	/** the singleton instance, lazy initialisation. */
	private static DefaultActionManager manager;

	/** no public constructor. */
	private DefaultActionManager() {
		// hide constructor, this is a Singleton
	}

	/**
	 * @return the instance of this manager. Note: This method is synchonized.
	 */
	public static synchronized DefaultActionManager getInstance() {
		if (manager == null) {
			manager = new DefaultActionManager();
		}
		return manager;
	}

	/**
	 * gets the name of the slot in which the entity can equip the item.
	 * @param entity 
	 * @param item 
	 * 
	 * @return the slot name for the item or null if there is no matching slot
	 *         in the entity
	 */
	public String getSlotNameToEquip(final RPEntity entity, final Item item) {
		// get all possible slots for this item
		final List<String> slotNames = item.getPossibleSlots();

		if (item instanceof Stackable) {
			// first try to put the item on an existing stack
			final Stackable stackEntity = (Stackable) item;
			for (final String slotName : slotNames) {
				if (entity.hasSlot(slotName)) {
					final RPSlot rpslot = entity.getSlot(slotName);
					for (final RPObject object : rpslot) {
						if (object instanceof Stackable) {
							// found another stackable
							final Stackable other = (Stackable) object;
							if (other.isStackable(stackEntity)) {
								return slotName;
							}
						}
					}
				}
			}
		}

		// We can't stack it on another item. Check if we can simply
		// add it to an empty cell.
		for (final String slot : slotNames) {
			if (entity.hasSlot(slot)) {
				final RPSlot rpslot = entity.getSlot(slot);
				if (!rpslot.isFull()) {
					return slot;
				}
			}
		}
		return null;
	}

	/** Equips the item in the specified slot. 
	 * @param entity 
	 * @param slotName 
	 * @param item 
	 * @return true if successfull*/
	public boolean onEquip(final RPEntity entity, final String slotName, final Item item) {
		if (!entity.hasSlot(slotName)) {
			return false;
		}

		final RPSlot rpslot = entity.getSlot(slotName);

		if (item instanceof StackableItem) {
			final StackableItem stackEntity = (StackableItem) item;
			// find a stackable item of the same type
			for (final RPObject object : rpslot) {
				if (object instanceof StackableItem) {
					// found another stackable
					final StackableItem other = (StackableItem) object;
					if (other.isStackable(stackEntity)) {
						// other is the same type...merge them
						new ItemLogger().merge(entity, stackEntity, other);
						other.add(stackEntity);
						entity.updateItemAtkDef();
						return true;
					}
				}
			}
		}

		// We can't stack it on another item. Check if we can simply
		// add it to an empty cell.
		if (rpslot.isFull()) {
			return false;
		} else {
			rpslot.add(item);
			entity.updateItemAtkDef();
			return true;
		}
	}

}

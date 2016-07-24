/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc>
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as
 published by the Free Software Foundation version 3 as published by
 the Free Software Foundation. You may not use, modify or distribute
 this program under any other version of the GNU Affero General Public
 License.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.server.handlers.login;

import net.AbstractMaplePacketHandler;
import net.server.Server;
import server.MapleItemInformationProvider;
import tools.FilePrinter;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;
import client.MapleCharacter;
import client.MapleClient;
import client.MapleJob;
import client.MapleSkinColor;
import client.autoban.AutobanFactory;
import client.inventory.Item;
import client.inventory.MapleInventory;
import client.inventory.MapleInventoryType;

public final class CreateCharHandler extends AbstractMaplePacketHandler {

	private final int[] allowedEquips = {1040006, 1040010, 1040002, 1060002, 1060006,
			1072005, 1072001, 1072037, 1072038, 1322005, 1312004, 1042167, 1062115, 1072383,
			1442079, 1302000, 1041002, 1041006, 1041010, 1041011, 1061002, 1061008, 1042180, 1060138, 1072418, 1061137,
			1302132, 1061160};       	


	@Override
	public final void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
		String name = slea.readMapleAsciiString();
		boolean isDualBlade = false;
		if (!MapleCharacter.canCreateChar(name)) {
			return;
		}
		MapleCharacter newchar = MapleCharacter.getDefault(c);
		newchar.setWorld(c.getWorld());

		int job = slea.readInt();
		int DBShort = slea.readShort();
		if (job == 1 && DBShort == 1)
			isDualBlade = true;
		int face = slea.readInt();

		int hair = slea.readInt();
		int hairColor = slea.readInt();
		int skincolor = slea.readInt();

		newchar.setSkinColor(MapleSkinColor.getById(skincolor));
		int top = slea.readInt();
		int bottom = slea.readInt();
		int shoes = slea.readInt();
		int weapon = slea.readInt();
		newchar.setGender(slea.readByte());
		newchar.setName(name);
		newchar.setHair(hair + hairColor);
		newchar.setFace(face);

		if(!((containsInt(allowedEquips, top) && containsInt(allowedEquips, bottom) && containsInt(allowedEquips, shoes) && containsInt(allowedEquips, weapon))))
		{
			//well now.
			System.out.println("Disconnected and banned client due to failed equips check.");
			c.banMacs(); //*shrugs* maybe they'll have logged in before
			c.getSession().close(true);
			return;
		}

		if (job == 0) { // Knights of Cygnus
			newchar.setJob(MapleJob.NOBLESSE);
			newchar.setMapId(130030000);
			newchar.getInventory(MapleInventoryType.ETC).addItem(new Item(4161047, (short) 0, (short) 1));
		} else if (job == 1) { // Adventurer   
			newchar.setJob(MapleJob.BEGINNER);
			newchar.setMapId(/*specialJobType == 2 ? 3000600 : */10000);
			newchar.getInventory(MapleInventoryType.ETC).addItem(new Item(4161001, (short) 0, (short) 1));
		} else if (job == 2) { // Aran
			newchar.setJob(MapleJob.LEGEND);
			newchar.setMapId(914000000);
			newchar.getInventory(MapleInventoryType.ETC).addItem(new Item(4161048, (short) 0, (short) 1));
		} else {
			System.out.println("[Char Creation] A new job ID has been found: " + job);
			c.announce(MaplePacketCreator.deleteCharResponse(0, 9));
			return;
		}

		MapleInventory equipped = newchar.getInventory(MapleInventoryType.EQUIPPED);

		Item eq_top = MapleItemInformationProvider.getInstance().getEquipById(top);
		eq_top.setPosition((byte) -5);
		equipped.addFromDB(eq_top);
		Item eq_bottom = MapleItemInformationProvider.getInstance().getEquipById(bottom);
		eq_bottom.setPosition((byte) -6);
		equipped.addFromDB(eq_bottom);
		Item eq_shoes = MapleItemInformationProvider.getInstance().getEquipById(shoes);
		eq_shoes.setPosition((byte) -7);
		equipped.addFromDB(eq_shoes);
		Item eq_weapon = MapleItemInformationProvider.getInstance().getEquipById(weapon);
		eq_weapon.setPosition((byte) -11);
		equipped.addFromDB(eq_weapon.copy());

		if (!newchar.insertNewChar()) {
			c.announce(MaplePacketCreator.deleteCharResponse(0, 9));
			return;
		}

		c.announce(MaplePacketCreator.addNewCharEntry(newchar));
		Server.getInstance().broadcastGMMessage(MaplePacketCreator.sendYellowTip("[NEW CHAR]: " + c.getAccountName() + " has created a new character with IGN " + name));
	}

	private boolean containsInt(int[] array, int toCompare)
	{
		for (int i : array)
			if (i == toCompare)
				return true;
		return false;
	}
}
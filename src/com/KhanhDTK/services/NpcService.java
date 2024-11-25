package com.KhanhDTK.services;

import com.KhanhDTK.consts.ConstNpc;
import com.KhanhDTK.models.npc.Npc;
import com.KhanhDTK.models.npc.NpcFactory;
import com.KhanhDTK.models.player.Player;
import com.KhanhDTK.server.Manager;
import com.girlkun.network.io.Message;
import com.KhanhDTK.utils.Logger;

public class NpcService {

    private static NpcService i;

    public static NpcService gI() {
        if (i == null) {
            i = new NpcService();
        }
        return i;
    }

    public void createMenuRongThieng(Player player, int indexMenu, String npcSay, String... menuSelect) {
        createMenu(player, indexMenu, ConstNpc.RONG_THIENG, 123, npcSay, menuSelect);
    }

    public void createMenuConMeo(Player player, int indexMenu, int avatar, String npcSay, String... menuSelect) {
        createMenu(player, indexMenu, ConstNpc.CON_MEO, avatar, npcSay, menuSelect);
    }

    public void createMenuConMeo(Player player, int indexMenu, int avatar, String npcSay, String[] menuSelect, Object object) {
        NpcFactory.PLAYERID_OBJECT.put(player.id, object);
        createMenuConMeo(player, indexMenu, avatar, npcSay, menuSelect);
    }

    private void createMenu(Player player, int indexMenu, byte npcTempId, int avatar, String npcSay, String... menuSelect) {
        if (player != null && player.iDMark != null) {
            Message msg;
            try {
                player.iDMark.setIndexMenu(indexMenu);
                msg = new Message(32);
                msg.writer().writeShort(npcTempId);
                msg.writer().writeUTF(npcSay);
                msg.writer().writeByte(menuSelect.length);
                for (String menu : menuSelect) {
                    msg.writer().writeUTF(menu);
                }
                if (avatar != -1) {
                    msg.writer().writeShort(avatar);
                }
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
                Logger.logException(NpcService.class, e);
            }
        }
    }

    public void createTutorial(Player player, int avatar, String npcSay) {
        Message msg;
        try {
            msg = new Message(38);
            msg.writer().writeShort(ConstNpc.CON_MEO);
            msg.writer().writeUTF(npcSay);
            if (avatar != -1) {
                msg.writer().writeShort(avatar);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
              
        }
    }

    public int getAvatar(int npcId) {
        for (Npc npc : Manager.NPCS) {
            if (npc.tempId == npcId) {
                return npc.avartar;
            }
        }
        return 1139;
    }
}
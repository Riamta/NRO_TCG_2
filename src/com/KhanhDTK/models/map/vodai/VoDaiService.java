package com.KhanhDTK.models.map.vodai;

import com.KhanhDTK.models.map.Zone;
import com.KhanhDTK.models.player.Player;
import com.girlkun.network.io.Message;
import com.KhanhDTK.services.MapService;
import com.KhanhDTK.services.Service;
import com.KhanhDTK.services.func.ChangeMapService;
import com.KhanhDTK.utils.Util;

/**
 * @author Bùi Kim Trường
 */
public class VoDaiService {

    private static VoDaiService i;

    public static VoDaiService gI() {
        if (i == null) {
            i = new VoDaiService();
        }
        return i;
    }

    public void startChallenge(Player player) {
        Zone zone = getMapChalllenge(112);
        if (zone != null) {
            ChangeMapService.gI().changeMap(player, zone, player.location.x, 336);
            Util.setTimeout(() -> {
                VoDai mc = new VoDai();
                mc.setPlayer(player);
                mc.setNpc(zone.getReferee1());
                mc.toTheNextRound();
                VoDaiManager.gI().add(mc);
                Service.getInstance().sendThongBao(player, "Số thứ tự của ngươi là 1\n chuẩn bị thi đấu nhé");
            }, 500);
        } else {

        }
    }

    public void moveFast(Player pl, int x, int y) {
        Message msg;
        try {
            msg = new Message(58);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeShort(x);
            msg.writer().writeShort(y);
            msg.writer().writeInt((int) pl.id);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendTypePK(Player player, Player boss) {
        Message msg;
        try {
            msg = Service.getInstance().messageSubCommand((byte) 35);
            msg.writer().writeInt((int) boss.id);
            msg.writer().writeByte(3);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public Zone getMapChalllenge(int mapId) {
        Zone map = MapService.gI().getMapWithRandZone(mapId);
        if (map.getNumOfBosses() < 1) {
            return map;
        }
        return null;
    }
}

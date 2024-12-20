package com.KhanhDTK.models.map.MapSatan;

import java.util.List;

import com.KhanhDTK.models.player.Player;
import com.KhanhDTK.services.MapService;
import com.KhanhDTK.services.Service;
import com.KhanhDTK.services.func.ChangeMapService;
import com.KhanhDTK.utils.TimeUtil;

public class MapTrung {

    public static final byte HOUR_OPEN_MAP_SATAN = 21;
    public static final byte MIN_OPEN_MAP_SATAN = 0;
    public static final byte SECOND_OPEN_MAP_SATAN = 0;

    public static final byte HOUR_CLOSE_MAP_SATAN = 23;
    public static final byte MIN_CLOSE_MAP_SATAN = 0;
    public static final byte SECOND_CLOSE_MAP_SATAN = 0;

    public static final int AVAILABLE = 9;

    private static MapTrung i;

    public static long TIME_OPEN_SATAN;
    public static long TIME_CLOSE_SATAN;

    private int day = -1;

    public static MapTrung gI() {
        if (i == null) {
            i = new MapTrung();
        }
        i.setTimeJoinMapSatan();
        return i;
    }

    public void setTimeJoinMapSatan() {
        if (i.day == -1 || i.day != TimeUtil.getCurrDay()) {
            i.day = TimeUtil.getCurrDay();
            try {
                TIME_OPEN_SATAN = TimeUtil.getTime(TimeUtil.getTimeNow("dd/MM/yyyy") + " " + HOUR_OPEN_MAP_SATAN + ":" + MIN_OPEN_MAP_SATAN + ":" + SECOND_OPEN_MAP_SATAN, "dd/MM/yyyy HH:mm:ss");
                TIME_CLOSE_SATAN = TimeUtil.getTime(TimeUtil.getTimeNow("dd/MM/yyyy") + " " + HOUR_CLOSE_MAP_SATAN + ":" + MIN_CLOSE_MAP_SATAN + ":" + SECOND_CLOSE_MAP_SATAN, "dd/MM/yyyy HH:mm:ss");
            } catch (Exception ignored) {
            }
        }
    }

    private void kickOutOfMapSatan(Player player) {
        if (MapService.gI().isMapSatan(player.zone.map.mapId)) {
            Service.gI().sendThongBao(player, "Trận đại chiến đã kết thúc, tàu vận chuyển sẽ đưa bạn về nhà");
            ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
        }
    }

    private void ketthucsatan(Player player) {
        player.zone.finishMapSatan = true;
        List<Player> playersMap = player.zone.getPlayers();
        for (int i = playersMap.size() - 1; i >= 0; i--) {
            Player pl = playersMap.get(i);
            kickOutOfMapSatan(pl);
        }
    }

    public void joinMapSatan(Player player) {
        boolean changed = false;
        if (player.clan != null) {
            List<Player> players = player.zone.getPlayers();
            for (Player pl : players) {
                if (pl.clan != null && !player.equals(pl) && player.clan.equals(pl.clan) && !player.isBoss) {
                    Service.gI().changeFlag(player, 0);
                    changed = true;
                    break;
                }
            }
        }
        if (!changed && !player.isBoss) {
            Service.gI().changeFlag(player, 0);
        }
    }

    public void update(Player player) {
        try {
            long now = System.currentTimeMillis();
            if (!(now > TIME_OPEN_SATAN && now < TIME_CLOSE_SATAN) && MapService.gI().isMapSatan(player.zone.map.mapId)) {

                ketthucsatan(player);

            }
        } catch (Exception ex) {
        }
    }
}

package com.KhanhDTK.models.map.gas;

import com.KhanhDTK.models.boss.BossID;
//import com.KhanhDTK.models.boss.bdkb.TrungUyXanhLo;
import com.KhanhDTK.models.boss.list_boss.gas.DrLyChee;
import com.KhanhDTK.models.boss.list_boss.gas.HaChiJack;
import com.KhanhDTK.models.item.Item;
//import static com.KhanhDTK.models.map.bando.BanDoKhoBau.TIME_BAN_DO_KHO_BAU;
import static com.KhanhDTK.models.map.gas.Gas.TIME_KHI_GAS;
import com.KhanhDTK.models.mob.Mob;
import com.KhanhDTK.models.player.Player;
import com.KhanhDTK.services.InventoryServiceNew;
import com.KhanhDTK.services.MapService;
import com.KhanhDTK.services.Service;
import com.KhanhDTK.services.func.ChangeMapService;
import com.KhanhDTK.utils.Logger;
import com.KhanhDTK.utils.Util;
import java.util.List;

/**
 *
 * @author Trum
 *
 */
public class GasService {

    public static GasService i;

    public GasService() {

    }

    public static GasService gI() {
        if (i == null) {
            i = new GasService();
        }
        return i;
    }
    private int MobinMap(Player pl)
    {
       int mob = 0;
       for(Mob m : pl.zone.mobs)
       {
           if(m.status != 0 && m.status != 1)
           {
               mob += 1;
           }
       }
       return mob;
    }
    public void update(Player player)
    {
        
        if (player.isPl() == true && player.clan.khiGas != null && player.clan.timeOpenKhiGas != 0)
        {
            if(player.zone.map.mapId == 148 && MobinMap(player) == 0 &&!player.clan.khiGas.isInitBoss )
            {
                player.clan.khiGas.InitBoss(player);
                player.clan.khiGas.isInitBoss = true;
            }
            if(Util.canDoWithTime(player.clan.timeOpenKhiGas, TIME_KHI_GAS)){
                ketthucGas(player);
                player.clan.khiGas = null;
            }
        }
    }
    
     private void kickOutOfGas(Player player) {
        if (MapService.gI().isMapKhiGas(player.zone.map.mapId)) {
            Service.gI().sendThongBao(player, "Trận đại chiến đã kết thúc, tàu vận chuyển sẽ đưa bạn về nhà");
            ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
        }
    }

    private void ketthucGas(Player player) {
        List<Player> playersMap = player.zone.getPlayers();
        for (int i = playersMap.size() - 1; i >= 0; i--) {
            Player pl = playersMap.get(i);
            kickOutOfGas(pl);
        }
    }


    public void openGas(Player player, int level) {
        if (level >= 1 && level <= 100) {
            if (player.clan != null && player.clan.khiGas == null) 
            {
                if (player.clan.SoLanDiKhiGas < 3)
                {
                    Gas gas = null;
                    for (Gas gasz : Gas.KHI_GAS) {
                        if (!gasz.isOpened)
                        {
                            gas = gasz;
                            break;
                        }
                    }
                    if (gas != null) 
                    {
                        player.clan.SoLanDiKhiGas += 1;
                        gas.openGas(player, player.clan, level);
                    } else {
                        Service.getInstance().sendThongBao(player, "Khí gas hủy diệt hiện tại đang quá đông, vui lòng quay lại sau");
                    }
                } else {
                    Service.getInstance().sendThongBao(player, "Tuần này Bang hội của bạn đã đi 3 lần rồi , hãy đi vào tuần sau");
                }
            } else {
                Service.getInstance().sendThongBao(player, "Cấp độ cao nhất là 100");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Cấp độ cao nhất là 100");
        }
    }
}

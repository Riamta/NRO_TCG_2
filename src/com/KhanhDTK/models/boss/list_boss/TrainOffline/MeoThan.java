package com.KhanhDTK.models.boss.list_boss.TrainOffline;

import com.KhanhDTK.models.boss.*;
import com.KhanhDTK.models.map.Zone;
import com.KhanhDTK.models.player.Player;
import com.KhanhDTK.services.Service;
import com.KhanhDTK.services.TaskService;
import com.KhanhDTK.services.func.ChangeMapService;
import com.KhanhDTK.utils.Util;

/**
 * @Stole By MITCHIKEN ZALO 0358689793
 */
public class MeoThan extends TrainBoss {

    public MeoThan(byte bossID, BossData bossData, Zone zone, int x, int y) throws Exception {
        super(BossID.MEO_THAN, BossesData.THAN_MEO,zone,x,y);
    }
    @Override
    public void reward(Player plKill) {
        //vật phẩm rơi khi diệt boss nhân bản
        if (plKill.isfight1){
            plKill.typetrain ++;
            TaskService.gI().checkDoneTaskKillBoss(plKill, this);
        }
        plKill.rsfight();
        this.chat("Hôm nay ta không được khỏe");
        this.playerkill = plKill;   
        
    }
}
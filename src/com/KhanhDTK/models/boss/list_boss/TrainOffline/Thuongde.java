package com.KhanhDTK.models.boss.list_boss.TrainOffline;

import com.KhanhDTK.models.boss.*;
import com.KhanhDTK.models.map.Zone;
import com.KhanhDTK.services.func.ChangeMapService;

/**
 * @Stole By MITCHIKEN ZALO 0358689793
 */
public class Thuongde extends TrainBoss {

    public Thuongde(byte bossID, BossData bossData, Zone zone, int x, int y) throws Exception {
        super(BossID.THUONG_DE, BossesData.THUONG_DE,zone,x,y);
    }
    @Override
    public void leaveMap() {
     //       ChangeMapService.gI().spaceShipArrive(this, (byte) 2, ChangeMapService.DEFAULT_SPACE_SHIP);
        ChangeMapService.gI().exitMap(this);     
        BossManager.gI().removeBoss(this);
        this.dispose();
        ChangeMapService.gI().changeMap(this.playerkill, 45, 0, 373, 408);
    }
    
}

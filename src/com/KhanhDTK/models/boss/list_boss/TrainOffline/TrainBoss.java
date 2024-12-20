package com.KhanhDTK.models.boss.list_boss.TrainOffline;

import com.KhanhDTK.data.DataGame;
import com.KhanhDTK.models.boss.*;
import com.KhanhDTK.models.map.ItemMap;
import com.KhanhDTK.models.map.Zone;
import com.KhanhDTK.models.player.Player;
import com.KhanhDTK.services.Service;
import com.KhanhDTK.services.func.ChangeMapService;
import com.KhanhDTK.utils.Util;

/**
 * @Stole By MITCHIKEN ZALO 0358689793
 */
public abstract class TrainBoss extends Boss {

    public TrainBoss(byte bossID, BossData bossData, Zone zone, int x, int y) throws Exception {
        super(bossID, bossData);
        this.zone = zone;
        this.location.x = x;
        this.location.y = y;
    }

    @Override
    public void reward(Player plKill) {
        //vật phẩm rơi khi diệt boss nhân bản
        if (plKill.isfight1) {
            plKill.typetrain++;
        }
        plKill.rsfight();
        this.chat("Hôm nay ta không được khỏe");
        this.playerkill = plKill;

    }

    @Override
    public void active() {
        super.active();

    }

    @Override
    public void attack() {
        super.attack();
        if (this.playerTarger == null || this.playerTarger.isDie()) {
            this.leaveMap();
        }
    }

    @Override
    public void joinMap() {
        if (zoneFinal != null) {
            joinMapByZone(zoneFinal);
            return;
        }
        if (this.zone == null) {
            if (this.parentBoss != null) {
                this.zone = parentBoss.zone;
            } else if (this.lastZone == null) {
                this.zone = getMapJoin();
            } else {
                this.zone = this.lastZone;
            }
        }
        if (this.zone != null) {
            if (this.currentLevel == 0) {
                if (this.parentBoss == null) {
                    ChangeMapService.gI().changeMap(this, this.zone, this.location.x, this.location.y);
                } else {
                    ChangeMapService.gI().changeMapBySpaceShip(this, this.zone,
                            this.parentBoss.location.x + Util.nextInt(-100, 100));
                }
                this.wakeupAnotherBossWhenAppear();
            } else {
                ChangeMapService.gI().changeMap(this, this.zone, this.location.x, this.location.y);
            }
            Service.getInstance().sendFlagBag(this);
        }
    }

    @Override
    public void leaveMap() {
        //       ChangeMapService.gI().spaceShipArrive(this, (byte) 2, ChangeMapService.DEFAULT_SPACE_SHIP);
        ChangeMapService.gI().exitMap(this);
        BossManager.gI().removeBoss(this);
        this.dispose();
        this.playerkill.zone.mapInfo(this.playerkill);
        DataGame.updateMap(this.playerkill.getSession());
    }
}

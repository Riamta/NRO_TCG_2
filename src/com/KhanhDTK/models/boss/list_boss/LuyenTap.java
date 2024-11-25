package com.KhanhDTK.models.boss.list_boss;

import com.KhanhDTK.consts.ConstPlayer;
import com.KhanhDTK.models.boss.Boss;
import com.KhanhDTK.models.boss.BossData;
import com.KhanhDTK.models.boss.BossID;
import com.KhanhDTK.models.boss.BossesData;
import com.KhanhDTK.models.map.Zone;
import com.KhanhDTK.models.player.Player;
import com.KhanhDTK.services.EffectSkillService;
import com.KhanhDTK.services.PlayerService;
import com.KhanhDTK.services.func.ChangeMapService;
import com.KhanhDTK.utils.Util;

public class LuyenTap extends Boss {

    public LuyenTap(int bossID, BossData bossData, Zone zone) throws Exception {
        super(bossID, bossData);
        this.zone = zone;
    }

    @Override
    public void joinMap() {
        ChangeMapService.gI().changeMapYardrat(this, this.zone, 330, 576);
        if (this.typePk == ConstPlayer.NON_PK) {
            this.changeToTypePK();
        }
        this.moveTo(330, 576);
    }

    @Override
    public void attack() {
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            this.nPoint.subHP(damage);
            if (this.nPoint.hp <= 0) {
                this.nPoint.hp = this.nPoint.hpMax;
            }
            return damage;
        } else {
            return 0;
        }
    }
}

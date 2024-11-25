package com.KhanhDTK.models.boss.list_boss.Kaido;

import com.KhanhDTK.models.boss.list_boss.Cooler.*;
import com.KhanhDTK.models.boss.Boss;
import com.KhanhDTK.models.boss.BossStatus;
import com.KhanhDTK.models.boss.BossesData;
import com.KhanhDTK.models.map.ItemMap;
import com.KhanhDTK.models.player.Player;
import com.KhanhDTK.server.Manager;
import com.KhanhDTK.services.EffectSkillService;
import com.KhanhDTK.services.Service;
import com.KhanhDTK.utils.Util;

import java.util.Random;

public class KAIDO1 extends Boss {

    public KAIDO1() throws Exception {
        super(Util.randomBossId(), BossesData.KAIDO_1,BossesData.KAIDO_2);
    }
    @Override
    public void reward(Player plKill) {
        int[] itemDos = new int[]{16,17,457};
         int randomnro = new Random().nextInt(itemDos.length);
          byte randomDo = (byte) new Random().nextInt(Manager.itemIds_TL.length - 1);
        if (Util.isTrue(5, 100)) {
          Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, Manager.itemIds_TL[randomDo], 1, this.location.x, this.location.y, plKill.id));      
        } else {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, itemDos[randomnro], 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id)); 
        }
    }
   @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st = System.currentTimeMillis();
    }

    private long st;

    @Override
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
        if (Util.canDoWithTime(st, 90000)) {
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }
    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1;
            }
            this.nPoint.subHP(damage);
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
    }

}

package com.KhanhDTK.models.boss.list_boss.BLACK;

import com.KhanhDTK.models.boss.*;
import com.KhanhDTK.models.map.ItemMap;
import com.KhanhDTK.models.player.Player;
import com.KhanhDTK.server.Manager;
import com.KhanhDTK.services.EffectSkillService;
import com.KhanhDTK.services.Service;
import com.KhanhDTK.utils.Util;

import java.util.Random;

public class SuperBlack2 extends Boss {

    public SuperBlack2() throws Exception {
        super(Util.randomBossId(), BossesData.SUPER_BLACK_GOKU_2);
    }

    @Override
    public void reward(Player plKill) {
        byte randomDo = (byte) new Random().nextInt(Manager.itemIds_TL.length - 1);
        byte randomNR = (byte) new Random().nextInt(Manager.itemIds_NR_SB.length);
        if (Util.isTrue(10, 100)) {
            Service.gI().dropItemMap(this.zone,
                    Util.ratiItem(zone, Manager.itemIds_TL[randomDo], 1, this.location.x, this.location.y, plKill.id));

        } else {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, Manager.itemIds_NR_SB[randomNR], 1, this.location.x,
                    zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
        }
        // đá ngũ sắc
        if (Util.isTrue(10, 100)) {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, 674, 1, this.location.x, this.location.y, plKill.id));
        }
    }

    @Override
    public void active() {
        super.active(); // To change body of generated methods, choose Tools | Templates.
        if (Util.canDoWithTime(st, 900000000)) {
            // this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }

    @Override
    public void joinMap() {
        super.joinMap(); // To change body of generated methods, choose Tools | Templates.
        st = System.currentTimeMillis();
    }

    private long st;

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
                damage = damage;
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
    // @Override
    // public void moveTo(int x, int y) {
    // if(this.currentLevel == 1){
    // return;
    // }
    // super.moveTo(x, y);
    // }
    //
    // @Override
    // public void reward(Player plKill) {
    // if(this.currentLevel == 1){
    // return;
    // }
    // super.reward(plKill);
    // }
    //
    // @Override
    // protected void notifyJoinMap() {
    // if(this.currentLevel == 1){
    // return;
    // }
    // super.notifyJoinMap();
    // }
}

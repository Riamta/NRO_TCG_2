package com.KhanhDTK.models.boss.list_boss.bocanhcung;

import com.KhanhDTK.models.boss.list_boss.BLACK.*;
import com.KhanhDTK.models.boss.list_boss.bocanhcung.*;
import com.KhanhDTK.models.boss.*;
import com.KhanhDTK.models.map.ItemMap;
import com.KhanhDTK.models.player.Player;
import com.KhanhDTK.server.Manager;
import com.KhanhDTK.services.EffectSkillService;
import com.KhanhDTK.services.PetService;
import com.KhanhDTK.services.Service;
import com.KhanhDTK.utils.Util;

import java.util.Random;


public class conbo1 extends Boss {

    public conbo1() throws Exception {
        super(Util.randomBossId(), BossesData.CON_BO_1);
    }

    @Override
//    public void reward(Player plKill) {
//      //  byte randomDo = (byte) new Random().nextInt(Manager.itemIds_CB.length - 1);
//      //  byte randomNR = (byte) new Random().nextInt(Manager.itemIds_CB.length);
//        if (Util.isTrue(BossManager.ratioReward, 100)) {
//            if (Util.isTrue(1, 20)) {
//                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 561, 1, this.location.x, this.location.y, plKill.id));
//            } else {
//                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, Manager.itemIds_CB[randomDo], 1, this.location.x, this.location.y, plKill.id));
//            }
//        } else {
//            Service.gI().dropItemMap(this.zone, new ItemMap(zone, Manager.itemIds_CB[randomNR], 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
//        }
//    }
//    @Override
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

   @Override
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
       
    }

    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
       
    }

//     @Override
//    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
//        if (!this.isDie()) {
//            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
//                this.chat("Xí hụt");
//                return 0;
//            }
//            damage = this.nPoint.subDameInjureWithDeff(damage/2);
//            if (!piercing && effectSkill.isShielding) {
//                if (damage > nPoint.hpMax) {
//                    EffectSkillService.gI().breakShield(this);
//                }
//                damage = damage;
//            }
//            this.nPoint.subHP(damage);
//            if (isDie()) {
//                this.setDie(plAtt);
//                die(plAtt);
//            }
//            return damage;
//        } else {
//            return 0;
//        }
//    }
//    @Override
//    public void moveTo(int x, int y) {
//        if(this.currentLevel == 1){
//            return;
//        }
//        super.moveTo(x, y);
//    }
//
//   
//
//    @Override
//    protected void notifyJoinMap() {
//        if(this.currentLevel == 1){
//            return;
//        }
//        super.notifyJoinMap();
//    }
}























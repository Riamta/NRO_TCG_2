package com.KhanhDTK.models.boss.list_boss.BLACK;

import com.KhanhDTK.models.boss.*;
import com.KhanhDTK.models.map.ItemMap;
import com.KhanhDTK.models.player.Player;
import com.KhanhDTK.server.Manager;
import com.KhanhDTK.services.EffectSkillService;
import com.KhanhDTK.services.PetService;
import com.KhanhDTK.services.Service;
import com.KhanhDTK.utils.Util;

import java.util.Random;


public class SuperBroly24 extends Boss {

    public SuperBroly24() throws Exception {
        super(Util.randomBossId(), BossesData.SUPER_BROLY24);
    }

    @Override
  public void reward(Player plKill) {
    byte randomDo = (byte) new Random().nextInt(Manager.itemIds_TL.length - 1);
     if (plKill.pet == null)  
     {    
      PetService.gI().createNormalPet(plKill);
   Service.getInstance().sendThongBao(plKill, "Bạn vừa nhận được đệ tử");
      } else if(Util.isTrue(2, 100)) {
         Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, Manager.itemIds_TL[randomDo], 1, this.location.x, this.location.y, plKill.id));
        
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
        if (Util.canDoWithTime(st, 900000)) {
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























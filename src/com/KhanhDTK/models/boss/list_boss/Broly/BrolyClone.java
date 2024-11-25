//package com.KhanhDTK.models.boss.list_boss.Broly;
//
//import com.KhanhDTK.models.boss.Boss;
//import com.KhanhDTK.models.boss.BossID;
//import com.KhanhDTK.models.boss.BossStatus;
//import com.KhanhDTK.models.boss.BossesData;
//import com.KhanhDTK.models.map.ItemMap;
//import com.KhanhDTK.models.player.Player;
//import com.KhanhDTK.services.EffectSkillService;
//import com.KhanhDTK.services.Service;
//import com.KhanhDTK.utils.Util;
//import java.util.Random;
//
//
////public class BrolyClone extends Boss {
//
// //   public BrolyClone() throws Exception {
//  //      super(BossID.BROLY, BossesData.BROLY_CLONE);
//   // }
//    
//    @Override
//    public void active() {
//        super.active();
//        if(Util.canDoWithTime(st,300000)){
//            this.changeStatus(BossStatus.LEAVE_MAP);
//        }
//    }
//    
//    @Override
//    public void joinMap() {
//        super.joinMap();
//        st= System.currentTimeMillis();
//    }
//    private long st;
//    
//        @Override
//    public void moveTo(int x, int y) {
//        if(this.currentLevel == 1){
//            return;
//        }
//        super.moveTo(x, y);
//    }
//    
//    @Override
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
//                damage = damage/2;
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
//}

package com.KhanhDTK.models.boss.list_boss.Doraemon;

import com.KhanhDTK.models.boss.Boss;
import com.KhanhDTK.models.boss.BossID;
import com.KhanhDTK.models.boss.BossStatus;
import com.KhanhDTK.models.boss.BossesData;
import com.KhanhDTK.models.map.ItemMap;
import com.KhanhDTK.models.player.Player;
import com.KhanhDTK.services.EffectSkillService;
import com.KhanhDTK.services.Service;
import com.KhanhDTK.services.TaskService;
import com.KhanhDTK.utils.Util;
import java.util.Random;

public class Xeko extends Boss {

    public Xeko() throws Exception {
        super(BossID.XEKO, BossesData.XEKO);
    }
   @Override
    public void reward(Player plKill) {
        int[] itemDos = new int[]{1142, 1142, 559, 556, 558,560, 562, 564, 566, 563,560, 562, 564, 566, 563, 1389, 560, 562, 564, 566, 563, 1142, 1142};
        int[] NRs = new int[]{16,18};
        int randomDo = new Random().nextInt(itemDos.length);
        int randomNR = new Random().nextInt(NRs.length);
        if (Util.isTrue(7, 100)) {
            if (Util.isTrue(3, 50)) {
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1391, 1, this.location.x, this.location.y, plKill.id));
                return;
            }
            Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, itemDos[randomDo], 1, this.location.x, this.location.y, plKill.id));
        } else  if (Util.isTrue(50, 100)){
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, NRs[randomNR], 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
        }
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }
 
    @Override
    public void wakeupAnotherBossWhenDisappear() {
        if (this.parentBoss == null) {
            return;
        }
        for (Boss boss : this.parentBoss.bossAppearTogether[this.parentBoss.currentLevel]) {
            if (boss.id == BossID.CHAIEN && boss.isDie()) {
                this.parentBoss.changeToTypePK();
                return;
            }
        }
    }  @Override
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
        if(Util.canDoWithTime(st,900000)){
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }

    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st= System.currentTimeMillis();
    }
    private long st;
      @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 500)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage/7);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
               damage = damage/4;
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

/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - GirlBeo
 */

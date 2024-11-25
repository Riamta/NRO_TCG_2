
package com.KhanhDTK.yadat;
import com.KhanhDTK.yadat.*;
//import com.KhanhDTK.models.boss.list_boss.cell.*;
import com.KhanhDTK.consts.ConstPlayer;
import com.KhanhDTK.models.boss.*;
import static com.KhanhDTK.models.boss.BossStatus.ACTIVE;
import static com.KhanhDTK.models.boss.BossStatus.JOIN_MAP;
import static com.KhanhDTK.models.boss.BossStatus.RESPAWN;
import com.KhanhDTK.models.boss.list_boss.cell.SieuBoHung;
import com.KhanhDTK.models.map.ItemMap;
import com.KhanhDTK.models.map.Zone;
import com.KhanhDTK.models.map.challenge.MartialCongressService;
import com.KhanhDTK.models.player.Player;
import com.KhanhDTK.models.skill.Skill;
import com.KhanhDTK.services.EffectSkillService;
import com.KhanhDTK.services.PlayerService;
import com.KhanhDTK.services.Service;
import com.KhanhDTK.utils.Util;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.KhanhDTK.models.boss.BossManager;
import com.KhanhDTK.models.boss.BossesData;
import com.KhanhDTK.models.boss.Boss;
import com.KhanhDTK.models.boss.BossID;
import com.KhanhDTK.models.map.ItemMap;
import com.KhanhDTK.server.Manager;
import com.KhanhDTK.consts.ConstPlayer;
import com.KhanhDTK.models.boss.Boss;
import com.KhanhDTK.models.boss.BossID;
import com.KhanhDTK.models.boss.BossManager;
import com.KhanhDTK.models.boss.BossesData;
import com.KhanhDTK.models.map.ItemMap;
import com.KhanhDTK.models.player.Player;
import com.KhanhDTK.models.skill.Skill;
import com.KhanhDTK.server.Manager;
import com.KhanhDTK.services.EffectSkillService;
import com.KhanhDTK.services.Service;
import com.KhanhDTK.services.TaskService;
import com.KhanhDTK.services.PlayerService;
import com.KhanhDTK.services.func.ChangeMapService;
import java.util.Random;
public class yadatprovip1 extends Boss {
    private static final int[][] FULL_LIENHOAN = new int[][]{{Skill.LIEN_HOAN, 1}, {Skill.LIEN_HOAN, 2}, {Skill.LIEN_HOAN, 3}, {Skill.LIEN_HOAN, 4}, {Skill.LIEN_HOAN, 5}, {Skill.LIEN_HOAN, 6}, {Skill.LIEN_HOAN, 7}};
     //   private static final int[][] FULL_TAI_TAO_NANG_LUONG = new int[][]{{Skill.TAI_TAO_NANG_LUONG, 1}, {Skill.TAI_TAO_NANG_LUONG, 2}, {Skill.TAI_TAO_NANG_LUONG, 3}, {Skill.TAI_TAO_NANG_LUONG, 4}, {Skill.TAI_TAO_NANG_LUONG, 5}, {Skill.TAI_TAO_NANG_LUONG, 6}, {Skill.TAI_TAO_NANG_LUONG, 7}};
  private long lastTimeHapThu;
    private int timeHapThu;
    private int initSuper = 0;
    protected Player playerAtt;
    private int timeLive = 10;
    private boolean calledNinja;
    public yadatprovip1() throws Exception {

        super(BossID.BOSS_YADAT1, BossesData.BOSS_YADAT1);
    }

    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(60, 100)) {
            ItemMap it = new ItemMap(this.zone, 590, 10, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.getInstance().dropItemMap(this.zone, it);
        }
    }
    
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
        if (Util.canDoWithTime(st, 1800000)) {
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }
    
    @Override
    public void leaveMap() {
        super.leaveMap();
        if (Util.canDoWithTime(st, 1800000)) {
            BossManager.gI().removeBoss(this);
        }
    }
     
    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st = System.currentTimeMillis();
    }
    private long st;
    
   
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
                damage = damage/2;
            }
            this.nPoint.subHP(damage);
            if (this.nPoint.hp <= 150000000 && !this.calledNinja) {
                try {
                    new tapsuyadat(this.zone, 2, Util.nextInt(1000, 10000), BossID.Yadat1);
                    new tapsuyadat(this.zone, 2, Util.nextInt(1000, 10000), BossID.Yadat2);
                    new tapsuyadat(this.zone, 2, Util.nextInt(1000, 10000), BossID.Yadat3);
                    new tapsuyadat(this.zone, 2, Util.nextInt(1000, 10000), BossID.Yadat4);
                    new tapsuyadat(this.zone, 2, Util.nextInt(1000, 10000), BossID.Yadat5);
                    new tapsuyadat(this.zone, 2, Util.nextInt(1000, 10000), BossID.Yadat6);
                    new tapsuyadat(this.zone, 2, Util.nextInt(1000, 10000), BossID.Yadat7);
                    new tapsuyadat(this.zone, 2, Util.nextInt(1000, 10000), BossID.Yadat8);                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                this.calledNinja = true;
            }
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

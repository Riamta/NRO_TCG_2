
package com.KhanhDTK.models.boss.list_boss.cell;
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
public class xensandetu extends Boss {
    private static final int[][] FULL_LIENHOAN = new int[][]{{Skill.LIEN_HOAN, 1}, {Skill.LIEN_HOAN, 2}, {Skill.LIEN_HOAN, 3}, {Skill.LIEN_HOAN, 4}, {Skill.LIEN_HOAN, 5}, {Skill.LIEN_HOAN, 6}, {Skill.LIEN_HOAN, 7}};
        private static final int[][] FULL_TAI_TAO_NANG_LUONG = new int[][]{{Skill.TAI_TAO_NANG_LUONG, 1}, {Skill.TAI_TAO_NANG_LUONG, 2}, {Skill.TAI_TAO_NANG_LUONG, 3}, {Skill.TAI_TAO_NANG_LUONG, 4}, {Skill.TAI_TAO_NANG_LUONG, 5}, {Skill.TAI_TAO_NANG_LUONG, 6}, {Skill.TAI_TAO_NANG_LUONG, 7}};
  private long lastTimeHapThu;
    private int timeHapThu;
    private int initSuper = 0;
    protected Player playerAtt;
    private int timeLive = 10;
    private boolean calledNinja;
    public xensandetu() throws Exception {

        super(BossID.XEN_SAN_DE_TU, BossesData.XEN_SAN_DE_TU);
    }

    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(100, 100)) {
            ItemMap it = new ItemMap(this.zone, 722, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
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
                    new xenclone(this.zone, 2, Util.nextInt(1000, 10000), BossID.XEN_SAN_DE_TU1);
                    new xenclone(this.zone, 2, Util.nextInt(1000, 10000), BossID.XEN_SAN_DE_TU2);
                    new xenclone(this.zone, 2, Util.nextInt(1000, 10000), BossID.XEN_SAN_DE_TU3);
                    new xenclone(this.zone, 2, Util.nextInt(1000, 10000), BossID.XEN_SAN_DE_TU4);
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

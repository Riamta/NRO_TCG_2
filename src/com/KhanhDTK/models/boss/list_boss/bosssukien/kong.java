package com.KhanhDTK.models.boss.list_boss.bosssukien;

import com.KhanhDTK.models.boss.list_boss.Cooler.*;
import com.KhanhDTK.models.boss.Boss;
import com.KhanhDTK.models.boss.BossID;
import com.KhanhDTK.models.boss.BossStatus;
import com.KhanhDTK.models.boss.BossesData;
import com.KhanhDTK.models.map.ItemMap;
import com.KhanhDTK.models.player.Player;
import com.KhanhDTK.server.Manager;
import com.KhanhDTK.services.EffectSkillService;
import com.KhanhDTK.services.Service;
import com.KhanhDTK.utils.Util;

import java.util.Random;

public class kong extends Boss {

    public kong() throws Exception {
        super(BossID.KONG, BossesData.KONG);
    }

    @Override
    public void reward(Player plKill) {
        int[] itemDos = new int[]{1442, 1443};
        int randomnro = new Random().nextInt(itemDos.length);
        if (Util.isTrue(25, 100)) {
            Service.gI().dropItemMap(this.zone, Util.sukienhungvuong(zone, itemDos[randomnro], 1, this.location.x, this.location.y, plKill.id));
            plKill.point_hungvuong += 1;
            Service.gI().sendThongBao(plKill, "Bạn Vừa Nhận Được Một Điểm Hùng Vương");
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

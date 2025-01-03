/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.KhanhDTK.models.boss.list_boss;

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

/**
 * @@Stole By Arriety
 */
public class Mabu extends Boss {

    public Mabu() throws Exception {
        super(BossID.MABU, BossesData.MABU);
    }

    @Override
    public void reward(Player plKill) {
        byte randomDo = (byte) new Random().nextInt(Manager.itemmabu.length - 1);
        if (plKill.vip >= 1) {
            if (Util.isTrue(40, 100)) {
                ItemMap it = new ItemMap(this.zone, Manager.itemmabu[randomDo], 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                        this.location.y - 24), plKill.id);
                Service.gI().dropItemMap(this.zone, it);
            }
        } else {
            if (Util.isTrue(40, 100)) {
                ItemMap it = new ItemMap(this.zone, 568, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                        this.location.y - 24), plKill.id);
                Service.gI().dropItemMap(this.zone, it);
            }
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

}

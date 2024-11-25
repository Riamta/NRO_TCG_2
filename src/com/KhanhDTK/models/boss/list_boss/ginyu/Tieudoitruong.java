package com.KhanhDTK.models.boss.list_boss.ginyu;

import com.KhanhDTK.consts.ConstPlayer;
import com.KhanhDTK.models.boss.Boss;
import com.KhanhDTK.models.boss.BossID;
import com.KhanhDTK.models.boss.BossManager;
import com.KhanhDTK.models.boss.BossStatus;
import com.KhanhDTK.models.boss.BossesData;
import com.KhanhDTK.models.map.ItemMap;
import com.KhanhDTK.models.player.Player;
import com.KhanhDTK.services.Service;
import com.KhanhDTK.services.TaskService;
import com.KhanhDTK.utils.Util;
import java.util.Random;


public class Tieudoitruong extends Boss {

    public Tieudoitruong() throws Exception {
        super(BossID.TIEU_DOI_TRUONG,BossesData.TIEU_DOI_TRUONG);
    }
   @Override
    public void reward(Player plKill) {
        if (Util.isTrue(15, 100)) {
            ItemMap it = new ItemMap(this.zone, 17, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
        Service.gI().dropItemMap(this.zone, it);
        }
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }
    @Override
    protected void notifyJoinMap() {
        if (this.currentLevel == 1000000000) {
            return;
        }
        super.notifyJoinMap();
    }
    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st = System.currentTimeMillis();
    }

    private long st;

    @Override
    public void active() {
        if (BossManager.gI().getBossByName("Số 1").zone != null) {
            this.changeToTypeNonPK();
        } else {
            if (this.typePk == ConstPlayer.NON_PK) {
                this.changeToTypePK();
            }
            this.attack();
        }
    }
   
}

/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - GirlBeo
 */

package com.KhanhDTK.models.boss.list_boss.Doraemon;

import com.KhanhDTK.models.boss.Boss;
import com.KhanhDTK.models.boss.BossID;
import com.KhanhDTK.models.boss.BossStatus;
import com.KhanhDTK.models.boss.BossesData;
import com.KhanhDTK.models.map.ItemMap;
import com.KhanhDTK.models.player.Player;
import com.KhanhDTK.models.skill.Skill;
import com.KhanhDTK.services.EffectSkillService;
import com.KhanhDTK.services.Service;
import com.KhanhDTK.services.TaskService;
import com.KhanhDTK.utils.Util;
import java.util.Random;


public class Doraemon extends Boss {

    public Doraemon() throws Exception {
        super(BossID.DORAEMON, BossesData.DORAEMON);
    }
    @Override
    public void reward(Player plKill) {
        int[] itemDos = new int[]{16, 16, 559, 556, 558, 560, 562, 564, 566,16, 16, 559, 556, 558, 560, 562, 564, 566, 1389, 563, 16, 16};
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

  
    
    }
/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - GirlBeo
 */
    
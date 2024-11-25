package com.KhanhDTK.models.boss.list_boss;

import com.KhanhDTK.models.boss.Boss;
import com.KhanhDTK.models.boss.BossID;
import com.KhanhDTK.models.boss.BossStatus;
import com.KhanhDTK.models.boss.BossesData;
import com.KhanhDTK.models.map.ItemMap;
import com.KhanhDTK.models.player.Player;
import com.KhanhDTK.services.EffectSkillService;
import com.KhanhDTK.services.InventoryServiceNew;
import com.KhanhDTK.services.Service;
import com.KhanhDTK.utils.Util;
import java.util.Random;

/**
 *
 * @author ADMIN
 */
public class AnTrom extends Boss {

    private long antrom;
    private long time;

    public AnTrom() throws Exception {
        super(BossID.ANTROM, BossesData.ANTROM);
    }

    private void antrom() {
        // Kiểm tra thời gian cho phép ăn trộm
        if (!Util.canDoWithTime(this.time, this.antrom)) {
            return;
        }
        // Lấy một người chơi ngẫu nhiên trong khu vực đang hoạt động của Boss
        Player pl = this.zone.getRandomPlayerInMap();
        if (pl == null || pl.isDie() || !pl.getSession().actived) {
            return;
        }
        // Kiểm tra số vàng và hoạt động của người chơi
        if (pl.inventory.gold <= 10000000) {
            this.chat("Không đủ vàng để ăn trộm!");
            return;
        }
        // ăn trộm vàng của người chơi
        int stolenGold = Util.nextInt(10000000, 900000000);
        pl.inventory.gold -= stolenGold;
        this.inventory.gold += stolenGold;
    // Thông báo ăn trộm trên kênh chat
    this.chat("Haha, tôi đã ăn trộm được " + stolenGold + " vàng rồi!!");
    // Cập nhật thời gian ăn trộm lần cuối và thời gian cho phép ăn trộm tiếp theo
    this.time = System.currentTimeMillis();
    this.antrom = 4000;
    // Gửi thông tin vàng mới của người chơi và cập nhật trên máy chủ
    Service.gI().sendMoney(pl);
    InventoryServiceNew.gI().sendItemBags(pl);
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
    public void reward(Player plKill) {
        if (Util.isTrue(97,100)) {
            int goldReward = (int) (this.inventory.gold * Util.nextInt(30, 50) / 100);
            Service.getInstance().dropItemMap(
                    this.zone,
                    Util.manhTS(zone, 76, goldReward, this.location.x, this.location.y, plKill.id)
            );
            Service.gI().sendThongBaoAllPlayer(plKill.name + " Vừa Tiêu Diệt Ăn Trộm Và Nhận Được " + goldReward + " Vàng");
            plKill.inventory.event += 1;
        }
    }
}
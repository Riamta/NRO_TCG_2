package com.KhanhDTK.models.boss.dhvt;

import com.KhanhDTK.models.boss.BossData;
import com.KhanhDTK.models.boss.BossID;
import com.KhanhDTK.models.boss.BossesData;
import com.KhanhDTK.models.player.Player;

/**
 * @author BTH sieu cap vippr0 
 */
public class ODo extends BossDHVT {

    public ODo(Player player) throws Exception {
        super(BossID.O_DO, BossesData.O_DO);
        this.playerAtt = player;
    }
}

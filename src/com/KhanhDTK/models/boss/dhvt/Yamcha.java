package com.KhanhDTK.models.boss.dhvt;

import com.KhanhDTK.models.boss.BossID;
import com.KhanhDTK.models.boss.BossesData;
import com.KhanhDTK.models.boss.dhvt.BossDHVT;
import com.KhanhDTK.models.player.Player;


/**
 *
 * @author BTH fix
 */
public class Yamcha extends BossDHVT {

    public Yamcha(Player player) throws Exception {
        super(BossID.YAMCHA, BossesData.YAMCHA);
        this.playerAtt = player;
    }
}

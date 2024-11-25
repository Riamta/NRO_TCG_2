package com.KhanhDTK.models.boss.vodai;

import com.KhanhDTK.models.boss.BossData;
import com.KhanhDTK.models.boss.BossID;
import com.KhanhDTK.models.boss.BossesData;
import com.KhanhDTK.models.player.Player;
/**
 * @author BTH sieu cap vippr0 
 */
public class Thodaubac extends BossVD {

    public Thodaubac(Player player) throws Exception {
        super(BossID.THODAUBAC, BossesData.THODAUBAC);
        this.playerAtt = player;
    }
}
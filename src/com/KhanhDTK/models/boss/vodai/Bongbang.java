package com.KhanhDTK.models.boss.vodai;

import com.KhanhDTK.models.boss.BossData;
import com.KhanhDTK.models.boss.BossID;
import com.KhanhDTK.models.boss.BossesData;
import com.KhanhDTK.models.player.Player;

/**
 * @author BTH sieu cap vippr0 
 */
public class Bongbang extends BossVD {

    public Bongbang(Player player) throws Exception {
        super(BossID.BONGBANG, BossesData.BONGBANG);
        this.playerAtt = player;
    }
}
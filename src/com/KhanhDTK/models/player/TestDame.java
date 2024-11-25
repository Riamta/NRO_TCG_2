package com.KhanhDTK.models.player;

import com.KhanhDTK.models.shop.ShopServiceNew;
import com.KhanhDTK.services.MapService;
import com.KhanhDTK.consts.ConstMap;
import com.KhanhDTK.consts.ConstPlayer;
import com.KhanhDTK.models.map.Map;
import com.KhanhDTK.models.map.Zone;
import com.KhanhDTK.server.Manager;
import com.KhanhDTK.services.MapService;
import com.KhanhDTK.services.PlayerService;
import com.KhanhDTK.services.Service;
import com.KhanhDTK.services.SkillService;
import com.KhanhDTK.utils.SkillUtil;
import com.KhanhDTK.utils.Util;
// đây
import java.util.ArrayList;
import java.util.List;

/**
 * @author BTH sieu cap vippr0
 */
public class TestDame extends Player {

    private long lastTimeChat;
//    protected Player playerTarger;

    private long lastTimeTargetPlayer;
    private long timeTargetPlayer = 5000;
    private long lastZoneSwitchTime;
    private long zoneSwitchInterval;
    private List<Zone> availableZones;

    public void initTestDame() {
        init();
    }

    @Override
    public short getHead() {
        return 1351;
    }

    @Override
    public short getBody() {
        return 1352;
    }

    @Override
    public short getLeg() {
        return 1353;
    }

    public void joinMap(Zone z, Player player) {
        MapService.gI().goToMap(player, z);
        z.load_Me_To_Another(player);
    }
    public void changeToTypePK() {
        PlayerService.gI().changeAndSendTypePK(this, ConstPlayer.PK_ALL);
    }
    public void active() {
        if (this.typePk == ConstPlayer.NON_PK) {
            this.changeToTypePK();
        }
    }

    protected long lastTimeAttack;
    

    @Override
    public void update() {
        active();
        if(this.isDie()){
            Service.getInstance().sendMoney(this);
            PlayerService.gI().hoiSinh(this);
            Service.getInstance().hsChar(this, this.nPoint.hpMax, this.nPoint.mpMax);
            PlayerService.gI().sendInfoHpMp(this);
        }
    }

    private void init() {
        int id = -1000000;
        for (Map m : Manager.MAPS) {
            if (m.mapId == 200) {
                for (Zone z : m.zones) {
                    TestDame pl = new TestDame();
                    pl.name = "TEST DAME";
                    pl.gender = 0;
                    pl.id = id++;
                    pl.nPoint.hpMax = 200000000;
                    pl.nPoint.hpg = 200000000;
                    pl.nPoint.hp = 200000000;
                    pl.nPoint.setFullHpMp();
                    pl.location.x = 360;
                    pl.location.y = 336;
                    joinMap(z, pl);
                    z.setReferee(pl);
                }
            }
//            else if (m.mapId == 7) {                      
//                    for (Zone z : m.zones) {
//                    Referee1 pl = new Referee1();
//                    pl.name = "TEST DAME";
//                    pl.gender = 0;
//                    pl.id = id++;
//                    pl.nPoint.hpMax = 50000000000L;
//                    pl.nPoint.hpg = 50000000000L;
//                    pl.nPoint.hp = 50000000000L;
//                    pl.nPoint.setFullHpMp();
//                    pl.location.x = 204;
//                    pl.location.y = 432;
//                    joinMap(z, pl);
//                    z.setReferee(pl);
//                 }
//              } 
        }
    }
}

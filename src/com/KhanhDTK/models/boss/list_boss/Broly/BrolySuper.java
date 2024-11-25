package com.KhanhDTK.models.boss.list_boss.Broly;

import com.KhanhDTK.consts.ConstPlayer;
import com.KhanhDTK.models.boss.*;
import static com.KhanhDTK.models.boss.BossStatus.ACTIVE;
import static com.KhanhDTK.models.boss.BossStatus.JOIN_MAP;
import static com.KhanhDTK.models.boss.BossStatus.RESPAWN;
import com.KhanhDTK.models.boss.list_boss.cell.SieuBoHung;
import com.KhanhDTK.models.map.ItemMap;
import com.KhanhDTK.models.map.Zone;
import com.KhanhDTK.models.map.challenge.MartialCongressService;
import com.KhanhDTK.models.player.Player;
import com.KhanhDTK.models.skill.Skill;
import com.KhanhDTK.services.EffectSkillService;
import com.KhanhDTK.services.PlayerService;
import com.KhanhDTK.services.Service;
import com.KhanhDTK.services.SkillService;
import com.KhanhDTK.services.func.ChangeMapService;
import com.KhanhDTK.utils.SkillUtil;
import com.KhanhDTK.utils.Util;
import java.util.logging.Level;
import java.util.logging.Logger;




//public class BrolySuper extends Boss {
//
//    public BrolySuper() throws Exception {
//        super(BossID.BROLY_SUPER, BossesData.BROLY_SUPER);
//    
   // }
public class BrolySuper extends Boss {
  
    private long lastUpdate = System.currentTimeMillis();
    private long timeJoinMap;
    protected Player playerAtt;
    private int timeLive = 200000000;

    
    
   
    public BrolySuper(Zone zone, int dame, int hp, int id) throws Exception {
        super(id, new BossData("Super Broly", //name
                ConstPlayer.TRAI_DAT, //gender
                new short[]{294, 295, 296, 28, -1, -1}, 50000 + dame, //dame
        new int[]{50000000 + hp}, //hp
                new int[]{49}, //map join
                new int[][]{
                    {Skill.KAMEJOKO, 7, 30000},
                    {Skill.MASENKO, 7, 10000},
                    {Skill.ANTOMIC, 7, 15000},
                    {Skill.TAI_TAO_NANG_LUONG, 1, 20000},}, new String[]{
                        "|-1|Dragon SUPER",
                        "|-2|Tới đây đi!"
                    }, //text chat 1
                new String[]{"|-1|Các ngươi tới số rồi mới gặp phải ta",
                    "|-1|Hãy Nhớ Mặt Ta đấy!",
                    "|-2|Không ngờ..Hắn mạnh cỡ này sao..!!"
                }, //text chat 2
                new String[]{"|-1|Tạm Biệt Con Gà Mai Ta sẽ Đấu Tai tôi Với Mi!!!"}, //text chat 3
                60));
        this.zone = zone;
    }
    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(100, 100)) {
            ItemMap it = new ItemMap(this.zone, 19, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.getInstance().dropItemMap(this.zone, it);
        }
    }
    @Override
    public void active() {
     if (this.typePk == ConstPlayer.NON_PK) {
            this.changeToTypePK();
        } 
       try {
            switch (this.bossStatus) {
                case RESPAWN:
                    this.respawn();
                    this.changeStatus(BossStatus.JOIN_MAP);
                case JOIN_MAP:
                    joinMap();
                    if (this.zone != null) {
                        changeStatus(BossStatus.ACTIVE);
                        timeJoinMap = System.currentTimeMillis();
                        this.typePk = 3;
                        MartialCongressService.gI().sendTypePK(playerAtt, this);
                        PlayerService.gI().changeAndSendTypePK(playerAtt, ConstPlayer.PK_PVP);
                        this.changeStatus(BossStatus.ACTIVE);
                    }
                    break;
                case ACTIVE:
                    if (this.playerSkill.prepareTuSat || this.playerSkill.prepareLaze || this.playerSkill.prepareQCKK) {
                        break;
                    } else {
                        this.attack();
                    }
                    break;
            }
            if (Util.canDoWithTime(lastUpdate, 1000)) {
                lastUpdate = System.currentTimeMillis();
                if (timeLive > 0) {
                    timeLive--;
                } else {
                    super.leaveMap();
                }
            }
        } catch (Exception e) {
            
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
                damage = damage/2;
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



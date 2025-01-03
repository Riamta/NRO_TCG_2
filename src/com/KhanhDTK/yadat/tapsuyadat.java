/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.KhanhDTK.yadat;
import com.KhanhDTK.yadat.*;
//import com.KhanhDTK.models.boss.list_boss.cell.*;
import com.KhanhDTK.consts.ConstPlayer;
import com.KhanhDTK.models.boss.*;
import static com.KhanhDTK.models.boss.BossStatus.ACTIVE;
import static com.KhanhDTK.models.boss.BossStatus.JOIN_MAP;
import static com.KhanhDTK.models.boss.BossStatus.RESPAWN;
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


public class tapsuyadat extends Boss {
  
    private long lastUpdate = System.currentTimeMillis();
    private long timeJoinMap;
    protected Player playerAtt;
    private int timeLive = 200000000;
    public tapsuyadat(Zone zone , int dame, int hp,int id) throws Exception {
        super(id, new BossData(
                "Tập sự", //name 264	265	266
                ConstPlayer.TRAI_DAT, //gender
                new short[]{526, 523, 524, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
                ((10000) ), //dame
                new int[]{((125000) )}, //hp
                new int[]{131,132,133}, //map join
                new int[][]{
                {Skill.DEMON, 3, 1}, {Skill.DEMON, 6, 2}, {Skill.DRAGON, 7, 3}, {Skill.DRAGON, 1, 4}, {Skill.GALICK, 5, 5},
                {Skill.KAMEJOKO, 7, 6}, {Skill.KAMEJOKO, 6, 7}, {Skill.KAMEJOKO, 5, 8}, {Skill.KAMEJOKO, 4, 9}, {Skill.KAMEJOKO, 3, 10}, {Skill.KAMEJOKO, 2, 11},{Skill.KAMEJOKO, 1, 12},
              {Skill.ANTOMIC, 1, 13},  {Skill.ANTOMIC, 2, 14},  {Skill.ANTOMIC, 3, 15},{Skill.ANTOMIC, 4, 16},  {Skill.ANTOMIC, 5, 17},{Skill.ANTOMIC, 6, 19},  {Skill.ANTOMIC, 7, 20},
                {Skill.MASENKO, 1, 21}, {Skill.MASENKO, 5, 22}, {Skill.MASENKO, 6, 23},},
                new String[]{}, //text chat 1
                new String[]{"|-1|Mau biến khỏi đây, ngài thủ lĩnh của chúng ta đang trên đường tới !"}, //text chat 2
                new String[]{}, //text chat 3
                60
        ));
        this.zone = zone;
    }
//    @Override
    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(45, 100)) {
            ItemMap it = new ItemMap(this.zone, 590, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
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
            e.printStackTrace();
        }
    }
    
 
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 1000;
            }
            damage = this.nPoint.subDameInjureWithDeff(1000);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1000;
            }
            this.nPoint.subHP(damage);
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            return 1000;
        } else {
            return 0;
        }
   
    }
}



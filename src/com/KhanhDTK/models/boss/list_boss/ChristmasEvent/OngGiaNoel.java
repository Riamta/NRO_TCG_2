package com.KhanhDTK.models.boss.list_boss.ChristmasEvent;

/*
 * @Author: DienCoLamCoi
 * @Description: Điện Cơ Lâm Còi - Chuyên cung cấp thiết bị điện cơ uy tín chất lượng cao.
 * @Group Zalo: Giao lưu chia sẻ kinh nghiệm code - https://zalo.me/g/lsqfzx907
 */

import com.KhanhDTK.models.boss.BossID;
import com.KhanhDTK.models.boss.BossStatus;
import com.KhanhDTK.models.boss.BossesData;
import com.KhanhDTK.models.boss.BossManager;
import static com.KhanhDTK.models.boss.BossType.CHRISTMAS_EVENT;

import java.util.Random;

import com.KhanhDTK.models.boss.Boss;
import com.KhanhDTK.models.map.ItemMap;
import com.KhanhDTK.models.player.Player;
import com.KhanhDTK.services.Service;
import com.KhanhDTK.services.TaskService;
import com.KhanhDTK.services.func.ChangeMapService;
import com.KhanhDTK.utils.Logger;
import com.KhanhDTK.utils.Util;

public class OngGiaNoel extends Boss {

    private long lastTimeDrop;
    private long st;
    private int timeLeave;
    private int countDrop = 0;
    public OngGiaNoel() throws Exception {
        super(BossID.ONG_GIA_NOEL, BossesData.ONG_GIA_NOEL);
    }
    @Override
    public void joinMap() {
        if (zoneFinal != null) {
            joinMapByZone(zoneFinal);
            this.notifyJoinMap();
            this.changeStatus(BossStatus.CHAT_S);
            this.wakeupAnotherBossWhenAppear();
            return;
        }
        if (this.zone == null) {
            if (this.parentBoss != null) {
                this.zone = parentBoss.zone;
            } else if (this.lastZone == null) {
                this.zone = getMapJoin();
            } else {
                this.zone = this.lastZone;
            }
        }
        if (this.zone != null) {
            try {
                int zoneid = 0;
                // Check trong khu lớn hơn 10 người chuyển sang khu n + 1
                while (zoneid < this.zone.map.zones.size() && this.zone.map.zones.get(zoneid).getNumOfPlayers() > 10) {
                    zoneid++;
                }
                // Check trong khu có boss sẽ chuyển sang khu n + 1
                while (zoneid < this.zone.map.zones.size()
                        && BossManager.gI().checkBosses(this.zone.map.zones.get(zoneid), BossID.ONG_GIA_NOEL)) {
                    zoneid++;
                }
                if (zoneid < this.zone.map.zones.size()) {
                    this.zone = this.zone.map.zones.get(zoneid);
                } else {
                    this.leaveMapNew();
                    return;
                }
                ChangeMapService.gI().changeMap(this, this.zone, Util.nextInt(100, 500),
                        this.zone.map.yPhysicInTop(this.location.x,
                                this.location.y - 24));
                this.changeStatus(BossStatus.CHAT_S);
                st = System.currentTimeMillis();
                timeLeave = Util.nextInt(100000, 300000);
            } catch (Exception e) {
                Logger.error(this.data[0].getName() + ": Lỗi đang tiến hành REST\n");
                this.changeStatus(BossStatus.REST);
            }
        } else {
            Logger.error(this.data[0].getName() + ": Lỗi map đang tiến hành RESPAWN\n");
            this.changeStatus(BossStatus.RESPAWN);
        }
    }

    @Override
    public void chatM() {
        if (this.data[this.currentLevel].getTextM().length == 0) {
            return;
        }
        if (!Util.canDoWithTime(this.lastTimeChatM, this.timeChatM)) {
            return;
        }
        String textChat = this.data[this.currentLevel].getTextM()[Util.nextInt(0,
                this.data[this.currentLevel].getTextM().length - 1)];
        int prefix = Integer.parseInt(textChat.substring(1, textChat.lastIndexOf("|")));
        textChat = textChat.substring(textChat.lastIndexOf("|") + 1);
        this.chat(prefix, textChat);
        this.lastTimeChatM = System.currentTimeMillis();
        this.timeChatM = Util.nextInt(3000, 20000);
    }

    private void giftBox() {
        if (Util.canDoWithTime(lastTimeDrop, 30000)) {
            this.chat("Hô hô hô");
            ItemMap item = new ItemMap(zone, 648, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), -1);
            ItemMap item2 = new ItemMap(zone, 648, 1, this.location.x + Util.nextInt(50),
                    this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24),
                    -1);
            ItemMap item3 = new ItemMap(zone, 648, 1, this.location.x - Util.nextInt(50),
                    this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24),
                    -1);
            if (Util.isTrue(1, 3)) {
                Service.gI().dropItemMap(this.zone, item);
            }
            if (Util.isTrue(1, 5)) {
                Service.gI().dropItemMap(this.zone, item2);
            }
            if (Util.isTrue(1, 7)) {
                Service.gI().dropItemMap(this.zone, item3);
            }
            lastTimeDrop = System.currentTimeMillis();
            countDrop++;
            if (countDrop >= 5) {
                this.leaveMap();
            }
        }
    }

    @Override
    public void active() {
        this.attack();
    }

    @Override
    public void autoLeaveMap() {
        if (Util.canDoWithTime(st, timeLeave)) {
            this.leaveMapNew();
        }
    }

    @Override
    public void leaveMap() {
        ChangeMapService.gI().exitMap(this);
        this.lastZone = null;
        this.lastTimeRest = System.currentTimeMillis();
        this.changeStatus(BossStatus.REST);
    }
             
    @Override
    public void attack() {
        if (Util.canDoWithTime(this.lastTimeAttack, 100)) {
            this.lastTimeAttack = System.currentTimeMillis();
            try {
                Player pl = getPlayerAttack();
                if (pl == null || pl.location == null) {
                    return;
                }
                this.playerSkill.skillSelect = this.playerSkill.skills
                        .get(Util.nextInt(0, this.playerSkill.skills.size() - 1));
                if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
                    if (Util.isTrue(5, 20) && Util.getDistance(this, pl) > 50) {
                        if (Util.isTrue(5, 20)) {
                            this.moveTo(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 200)),
                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 70));
                        } else {
                            this.moveTo(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(10, 40)), pl.location.y);
                        }
                    } else if (Util.getDistance(this, pl) <= 50) {
                        this.giftBox();
                    }
                    checkPlayerDie(pl);
                } else {
                    if (Util.isTrue(1, 2)) {
                        this.moveToPlayer(pl);
                    }
                }
            } catch (Exception ex) {
            }
        }
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        return 0;
    }

    @Override
    public void reward(Player plKill) {
    }
//     @Override
//     public void reward(Player plKill) {
//         int[] itemDos = new int[]{16, 16, 559, 556, 558, 560, 562, 564, 566,16, 16, 559, 556, 558, 560, 562, 564, 566, 1389, 563, 16, 16};
//         int[] NRs = new int[]{16,18};
//         int randomDo = new Random().nextInt(itemDos.length);
//         int randomNR = new Random().nextInt(NRs.length);
//         if (Util.isTrue(7, 100)) {
//             if (Util.isTrue(3, 50)) {
//                 Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1391, 1, this.location.x, this.location.y, plKill.id));
//                 return;
//             }
//             Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, itemDos[randomDo], 1, this.location.x, this.location.y, plKill.id));
//         } else  if (Util.isTrue(50, 100)){
//             Service.gI().dropItemMap(this.zone, new ItemMap(zone, NRs[randomNR], 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
//         }
//         TaskService.gI().checkDoneTaskKillBoss(plKill, this);
//     }

  
//    @Override
//     public void active() {
//         super.active(); //To change body of generated methods, choose Tools | Templates.
//         if(Util.canDoWithTime(st,900000)){
//             this.changeStatus(BossStatus.LEAVE_MAP);
//         }
//     }
//     @Override
//     public void joinMap() {
//         super.joinMap(); //To change body of generated methods, choose Tools | Templates.
//         st= System.currentTimeMillis();
//     }
//     private long st;

  
    
    }
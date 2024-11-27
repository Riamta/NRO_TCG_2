package com.KhanhDTK.models.boss.list_boss.Broly;

import com.KhanhDTK.consts.ConstPlayer;
import com.KhanhDTK.models.boss.Boss;
import com.KhanhDTK.models.boss.BossData;
import com.KhanhDTK.models.boss.BossID;
import com.KhanhDTK.models.boss.BossManager;
import com.KhanhDTK.models.boss.BossStatus;
import com.KhanhDTK.models.boss.BossesData;
import com.KhanhDTK.models.map.ItemMap;
import com.KhanhDTK.models.map.Zone;
import com.KhanhDTK.models.player.Player;
import com.KhanhDTK.models.skill.Skill;
import com.KhanhDTK.services.EffectSkillService;
import com.KhanhDTK.services.PlayerService;
import com.KhanhDTK.services.Service;
import com.KhanhDTK.utils.SkillUtil;
import com.KhanhDTK.utils.Util;
import java.util.Random;

public class Broly extends Boss {

    private int hi;
    private long lastTimeDamaged;
    private long lastTimeHP;
    private int timeHP;

    public Broly(Zone zone, int hp, int dame) throws Exception {
        super(Util.randomBossId(), new BossData(
                "Broly", // name
                ConstPlayer.XAYDA, // gender
                new short[] { 291, 292, 293, -1, -1, -1 }, // outfit {head, body, leg, bag, aura, eff}
                dame, // dame
                new int[] { hp }, // hp
                new int[] { 5 }, // map join
                new int[][] {
                        { Skill.DRAGON, 7, 1000 },
                        { Skill.TAI_TAO_NANG_LUONG, 7, 20000 },
                        { Skill.ANTOMIC, 7, 500 } }, // skill
                new String[] {
                        "|-1|Tuy không biết các ngươi là ai, nhưng ta rất ấn tượng đấy!",
                        "|-2|Tới đây đi!"
                }, // text chat 1
                new String[] { "|-1|Các ngươi tới số rồi mới gặp phải ta",
                        "|-1|Gaaaaaa",
                        "|-2|Không..thể..nào!!",
                        "|-2|Không ngờ..Hắn mạnh cỡ này sao..!!"
                }, // text chat 2
                new String[] { "|-1|Gaaaaaaaa!!!" }, // text chat 3
                600 // second rest
        ));
        this.zone = zone;
    }

    public Broly(Zone zone, int hp, int dame, int... id) throws Exception {
        super(Util.randomBossId(), new BossData(
                "Broly", // name
                ConstPlayer.XAYDA, // gender
                new short[] { 291, 292, 293, -1, -1, -1 }, // outfit {head, body, leg, bag, aura, eff}
                dame, // dame
                new int[] { hp }, // hp
                new int[] { 5 }, // map join
                new int[][] {
                        { Skill.DRAGON, 7, 1000 },
                        { Skill.TAI_TAO_NANG_LUONG, 7, 20000 },
                        { Skill.ANTOMIC, 7, 500 } }, // skill
                new String[] {
                        "|-1|Tuy không biết các ngươi là ai, nhưng ta rất ấn tượng đấy!",
                        "|-2|Tới đây đi!"
                }, // text chat 1
                new String[] { "|-1|Các ngươi tới số rồi mới gặp phải ta",
                        "|-1|Gaaaaaa",
                        "|-2|Không..thể..nào!!",
                        "|-2|Không ngờ..Hắn mạnh cỡ này sao..!!"
                }, // text chat 2
                new String[] { "|-1|Gaaaaaaaa!!!" }, // text chat 3
                600 // second rest
        ));
        this.zone = zone;

    }

    @Override
    public void reward(Player plKill) {
        int[] itemDos = new int[] { 568, 1333, 1334 };
        int[] NRs = new int[] { 16, 17, 1333, 1334 };
        int randomDo = new Random().nextInt(itemDos.length);
        int randomNR = new Random().nextInt(NRs.length);
        if (Util.isTrue(15, 100)) {
            if (Util.isTrue(1, 50)) {
                Service.gI().dropItemMap(this.zone,
                        Util.ratiItem(zone, 568, 1, this.location.x, this.location.y, plKill.id));
                return;
            }
            Service.gI().dropItemMap(this.zone,
                    Util.ratiItem(zone, itemDos[randomDo], 1, this.location.x, this.location.y, plKill.id));
        } else {
            Service.gI().dropItemMap(this.zone,
                    new ItemMap(zone, NRs[randomNR], 1, this.location.x, this.location.y, plKill.id));
        }
    }

    @Override
    public void active() {
        super.active();
        if (this.nPoint.hp > 1600000) {
            this.nPoint.hp = 1600000;
        }
        try {
            this.hoiPhuc();
        } catch (Exception ex) {
            // Logger.getLogger(SuperBroly.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void hoiPhuc() throws Exception {
        if (!Util.canDoWithTime(lastTimeHP, timeHP) || !Util.isTrue(1, 100)) {
            return;
        }
        Player pl = this.zone.getRandomPlayerInMap();
        if (pl == null || pl.isDie()) {
            return;
        }
        this.nPoint.dameg += this.nPoint.dame * 2 / 100;
        this.nPoint.hpg += this.nPoint.hpg * 80 / 100;
        this.nPoint.critg++;
        this.nPoint.calPoint();
        PlayerService.gI().hoiPhuc(this, this.nPoint.hp, this.nPoint.mp);
        Service.gI().sendThongBao(pl, "Tên broly hắn lại tăng sức mạnh rồi!");
        this.chat(2, "Mọi người cẩn thận sức mạnh hắn ta tăng đột biến..");
        this.chat("Graaaaaa...");
        lastTimeHP = System.currentTimeMillis();
        timeHP = Util.nextInt(5000, 10000);
    }

    public void heal(long amount) {
        nPoint.hp = (int) Math.min(nPoint.hp + amount, nPoint.hpMax);
    }

    @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
        this.dispose();
    }

    @Override
    public void joinMap() {
        super.joinMap();
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
            this.chat("Xí hụt");
            return 0;
        }
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTimeDamaged >= 10000) {
            long healAmount = nPoint.hpMax * 8 / 10; // hồi phục 20% HP gốc khi tấn công trong 1 phút
            this.heal(healAmount);
            byte skillId = (byte) Skill.TAI_TAO_NANG_LUONG;
            if (skillId != 0) {
                playerSkill.skills.add(SkillUtil.createSkill(skillId, 7));
                this.chat("Cảm giác thật tốt khi được hồi phục lại năng lượng");
            }
            lastTimeDamaged = currentTime;
        }
        damage = Math.min(damage, nPoint.hpMax * 5 / 100);
        this.nPoint.subHP((int) damage);
        if (isDie()) {
            Zone zonetemp = this.zone;
            long hptemp = this.nPoint.hpMax;
            long dametemp = this.nPoint.dame;
            this.setDie(plAtt);
            die(plAtt);
            try {
                new Broly(zonetemp, (int) hptemp * 2, (int) dametemp * 2);
            } catch (Exception e) {
            }
        }
        if (this.nPoint.hp >= 1600000) {
            Zone zonetemp = this.zone;
            long hptemp = this.nPoint.hpMax;
            long dametemp = this.nPoint.dame;
            this.setDie(plAtt);
            die(plAtt);
            try {
                new BrolySuper(zonetemp, (int) hptemp * 10, (int) dametemp * 5, Util.randomBossId());
            } catch (Exception e) {
            }
        }
        return damage;
    }
}

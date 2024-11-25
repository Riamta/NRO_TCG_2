///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.KhanhDTK.models.boss.list_boss.sontinhthuytinh;
//
//import com.KhanhDTK.consts.ConstPlayer;
//import com.KhanhDTK.models.boss.Boss;
//import com.KhanhDTK.models.boss.BossData;
//import com.KhanhDTK.models.boss.BossID;
//import com.KhanhDTK.models.boss.BossManager;
//import com.KhanhDTK.models.boss.BossStatus;
//import com.KhanhDTK.models.boss.BossesData;
//import com.KhanhDTK.models.item.Item;
//import com.KhanhDTK.models.map.ItemMap;
//import com.KhanhDTK.models.map.Zone;
//import com.KhanhDTK.models.player.Player;
//import com.KhanhDTK.models.skill.Skill;
//import com.KhanhDTK.services.EffectSkillService;
//import com.KhanhDTK.services.InventoryServiceNew;
//import com.KhanhDTK.services.PlayerService;
//import com.KhanhDTK.services.Service;
//import com.KhanhDTK.services.SkillService;
//import com.KhanhDTK.services.TaskService;
//import com.KhanhDTK.services.func.ChangeMapService;
//import com.KhanhDTK.utils.Logger;
//import com.KhanhDTK.utils.SkillUtil;
//import com.KhanhDTK.utils.Util;
//import java.util.Random;
//
///**
// *
// * @author Khánh Đẹp Zoai
// */
//public class Sontinh extends Boss {
//
//    public Sontinh() throws Exception {
//        super(BossID.SON_TINH, BossesData.SON_TINH);
//        this.cFlag = 9;
//    }
//
//    @Override
//    public void reward(Player plKill) {
//        int[] itemDos = new int[]{421, 422};
//        int randomnro = new Random().nextInt(itemDos.length);
//        if (Util.isTrue(50, 100)) {
//            Service.gI().dropItemMap(this.zone, Util.sukienhungvuong(zone, itemDos[randomnro], 1, this.location.x, this.location.y, plKill.id));
//        }
//        plKill.point_hungvuong += 10;
//        Service.gI().sendThongBao(plKill, "Bạn Vừa Nhận Được Một Điểm Hùng Vương");
//        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
//    }
//
//    @Override
//    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
//        if (plAtt.cFlag != 10) {// tỉ lệ hụt của thiên sứ
//            this.chat("|2|Không có kiếm mà đòi đánh ta hả");
//            damage = 0;
//
//        }
//        if (!this.isDie()) {
//            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
//                this.chat("Xí hụt");
//                return 0;
//            }
//            damage = this.nPoint.subDameInjureWithDeff(damage);
//            if (!piercing && effectSkill.isShielding) {
//                if (damage > nPoint.hpMax) {
//                    EffectSkillService.gI().breakShield(this);
//                }
//                damage = damage / 2;
//            }
//            this.nPoint.subHP(damage);
//            if (isDie()) {
//                this.setDie(plAtt);
//                die(plAtt);
//            }
//            return damage;
//        } else {
//            return 0;
//        }
//    }
//
//    @Override
//    public void active() {
//        super.active();
//        this.attack();
//        this.changeToTypeNonPK();
//        if (Util.canDoWithTime(st, 1800000)) {
//            this.changeStatus(BossStatus.LEAVE_MAP);
//        }
//        if (BossManager.gI().getBossById(BossID.THUY_TINH) == null) {
//            this.leaveMap();
//        }
//        if (System.currentTimeMillis() - lastTimeBlame > 10000) {
//            this.chat("|2|Hãy về phe của ta nếu không ngươi sẽ phải chịu hậu quả");
//            lastTimeBlame = System.currentTimeMillis();
//        }
//
//    }
//
//    @Override
//    public void attack() {
//        if (Util.canDoWithTime(this.lastTimeAttack, 100)) {
//            this.lastTimeAttack = System.currentTimeMillis();
//            try {
//                Player pl = getPlayerAttack();
//                if (pl == null || pl.isDie()) {
//                    return;
//                }
//                this.playerSkill.skillSelect = this.playerSkill.skills
//                        .get(Util.nextInt(0, this.playerSkill.skills.size() - 1));
//                if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
//                    if (Util.isTrue(5, 20)) {
//                        if (SkillUtil.isUseSkillChuong(this)) {
//                            this.moveTo(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 200)),
//                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 70));
//                        } else {
//                            this.moveTo(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(10, 40)),
//                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50));
//                        }
//                    }
//                    SkillService.gI().useSkill(this, pl, null, null);
//                    checkPlayerDie(pl);
//                } else {
//                    if (Util.isTrue(1, 2)) {
//                        this.moveToPlayer(pl);
//                    }
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
//
//    @Override
//    public void joinMap() {
//        super.joinMap(); // To change body of generated methods, choose Tools | Templates.
//        st = System.currentTimeMillis();
//    }
//
//    private long st;
//    private long lastTimeBlame;
//}

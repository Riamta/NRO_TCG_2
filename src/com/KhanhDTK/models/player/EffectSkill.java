package com.KhanhDTK.models.player;

import com.KhanhDTK.models.mob.Mob;
import com.KhanhDTK.services.EffectSkillService;
import com.KhanhDTK.services.ItemTimeService;
import com.KhanhDTK.services.Service;
import com.KhanhDTK.utils.Util;

public class EffectSkill {

    private Player player;

    public boolean isAnThan;
    public long lastTimeAnThan;
    public int timeAnThan;

    //thái dương hạ san
    public boolean isStun;
    public long lastTimeStartStun;
    public int timeStun;

    //khiên năng lượng
    public boolean isShielding;
    public long lastTimeShieldUp;
    public int timeShield;

    //biến khỉ
    public boolean isMonkey;
    public byte levelMonkey;
    public long lastTimeUpMonkey;
    public int timeMonkey;

    //tái tạo năng lượng
    public boolean isCharging;
    public int countCharging;

    //huýt sáo
    public int tiLeHPHuytSao;
    public long lastTimeHuytSao;

    //thôi miên
    public boolean isThoiMien;
    public long lastTimeThoiMien;
    public int timeThoiMien;

    //trói
    public boolean useTroi;
    public boolean anTroi;
    public long lastTimeTroi;
    public long lastTimeAnTroi;
    public int timeTroi;
    public int timeAnTroi;
    public Player plTroi;
    public Player plAnTroi;
    public Mob mobAnTroi;

    //dịch chuyển tức thời
    public boolean isBlindDCTT;
    public long lastTimeBlindDCTT;
    public int timeBlindDCTT;

    //socola
    public boolean isSocola;
    public long lastTimeSocola;
    public int timeSocola;
    public int countPem1hp;

    // biến thành cái bình
    public boolean isMaPhongBa;
    public long lastTimeMaPhongBa;
    public int timeMaPhongBa;

    public boolean isBienHinh;
    public long lastTimeBienHinh;
    public int timeBienHinh;

    //bien super ssj
    public boolean isTranformation;
    public byte levelTranformation = 0;
    public long lastTimeTranformation;
    public int timeTranformation;

    public boolean isEvolution;
    public byte levelEvolution;
    public long lastTimeEvolution;
    public int timeEvolution;

    public EffectSkill(Player player) {
        this.player = player;
    }

    public boolean isHaveEffectSkillChangeOutfit() {
        return isMaPhongBa || isSocola || isMonkey;
    }

    public void removeSkillEffectWhenDie() {
        if (isMonkey) {
            EffectSkillService.gI().monkeyDown(player);
        }
        if (isTranformation) {
            EffectSkillService.gI().TranformationDown(player);
        }
        if (isEvolution) {
            EffectSkillService.gI().EvolutionDown(player);
        }
        if (isBienHinh) {
            EffectSkillService.gI().removeBienHinh(player);
        }
        if (isShielding) {
            EffectSkillService.gI().removeShield(player);
            ItemTimeService.gI().removeItemTime(player, 3784);
        }
        if (useTroi) {
            EffectSkillService.gI().removeUseTroi(this.player);
        }
        if (isStun) {
            EffectSkillService.gI().removeStun(this.player);
        }
        if (isThoiMien) {
            EffectSkillService.gI().removeThoiMien(this.player);
        }
        if (isBlindDCTT) {
            EffectSkillService.gI().removeBlindDCTT(this.player);
        }
    }

    public void update() {
        if (isMonkey && (Util.canDoWithTime(lastTimeUpMonkey, timeMonkey))) {
            EffectSkillService.gI().monkeyDown(player);
        }
        if (isTranformation && (Util.canDoWithTime(lastTimeTranformation, 300000))) {
            EffectSkillService.gI().TranformationDown(player);
        }
        if (isEvolution && (Util.canDoWithTime(lastTimeEvolution, timeEvolution))) {
            EffectSkillService.gI().EvolutionDown(player);
        }
        if (isShielding && (Util.canDoWithTime(lastTimeShieldUp, timeShield))) {
            EffectSkillService.gI().removeShield(player);
        }
        if (useTroi && Util.canDoWithTime(lastTimeTroi, timeTroi)
                || plAnTroi != null && plAnTroi.isDie()
                || useTroi && isHaveEffectSkill()) {
            EffectSkillService.gI().removeUseTroi(this.player);
        }
//        if (anTroi && (Util.canDoWithTime(lastTimeAnTroi, timeAnTroi) || player.isDie())) {
//            EffectSkillService.gI().removeAnTroi(this.player);
//        }
        if (isStun && Util.canDoWithTime(lastTimeStartStun, timeStun)) {
            EffectSkillService.gI().removeStun(this.player);
        }
        if (isThoiMien && (Util.canDoWithTime(lastTimeThoiMien, timeThoiMien))) {
            EffectSkillService.gI().removeThoiMien(this.player);
        }
        if (isBlindDCTT && (Util.canDoWithTime(lastTimeBlindDCTT, timeBlindDCTT))) {
            EffectSkillService.gI().removeBlindDCTT(this.player);
        }
        if (isSocola && (Util.canDoWithTime(lastTimeSocola, timeSocola))) {
            EffectSkillService.gI().removeSocola(this.player);
        }
        if (tiLeHPHuytSao != 0 && Util.canDoWithTime(lastTimeHuytSao, 30000)) {
            EffectSkillService.gI().removeHuytSao(this.player);
        }
    }

    public boolean isHaveEffectSkill() {
        return isStun || isBlindDCTT || anTroi || isThoiMien;
    }

    public void dispose() {
        this.player = null;
    }
}

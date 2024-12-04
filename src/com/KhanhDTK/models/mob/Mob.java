package com.KhanhDTK.models.mob;

import com.KhanhDTK.De2.Thu_TrieuHoi;
import com.KhanhDTK.consts.ConstMap;
import com.KhanhDTK.consts.ConstMob;
import com.KhanhDTK.consts.ConstTask;
import com.KhanhDTK.models.item.Item;
import com.KhanhDTK.models.map.ItemMap;

import java.util.List;

import com.KhanhDTK.models.map.Zone;
import com.KhanhDTK.models.map.BDKB.BanDoKhoBau;
import com.KhanhDTK.consts.ConstPlayer;
import com.KhanhDTK.models.player.Location;
import com.KhanhDTK.models.player.Pet;
import com.KhanhDTK.models.player.Player;
import com.KhanhDTK.models.reward.ItemMobReward;
import com.KhanhDTK.models.reward.MobReward;
import com.KhanhDTK.models.skill.PlayerSkill;
import com.KhanhDTK.models.skill.Skill;
import com.girlkun.network.io.Message;
import com.KhanhDTK.server.Maintenance;
import com.KhanhDTK.server.Manager;
import com.KhanhDTK.server.ServerManager;
import com.KhanhDTK.services.*;
import com.KhanhDTK.utils.Logger;
import com.KhanhDTK.utils.TimeUtil;
import com.KhanhDTK.utils.Util;
import java.io.IOException;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class Mob {

    public int id;
    public Zone zone;
    public int tempId;
    public String name;
    public byte level;

    public MobPoint point;
    public MobEffectSkill effectSkill;
    public Location location;

    public byte pDame;
    public int pTiemNang;
    private long maxTiemNang;

    public long lastTimeDie;
    public int lvMob = 0;
    public int status = 5;

    public boolean isMobMe;
    private long timeAttack = 2000;

    public Mob(Mob mob) {
        this.point = new MobPoint(this);
        this.effectSkill = new MobEffectSkill(this);
        this.location = new Location();
        this.id = mob.id;
        this.tempId = mob.tempId;
        this.level = mob.level;
        this.point.setHpFull(mob.point.getHpFull());
        this.point.sethp(this.point.getHpFull());
        this.location.x = mob.location.x;
        this.location.y = mob.location.y;
        this.pDame = mob.pDame;
        this.pTiemNang = mob.pTiemNang;
        this.setTiemNang();
    }

    public Mob() {
        this.point = new MobPoint(this);
        this.effectSkill = new MobEffectSkill(this);
        this.location = new Location();
    }

    public void setTiemNang() {
        this.maxTiemNang = (long) this.point.getHpFull() * (this.pTiemNang + Util.nextInt(-2, 2)) / 100;
    }

    public static void initMobBanDoKhoBau(Mob mob, byte level) {
        mob.point.dame = level * 3250 * mob.level * 4;
        mob.point.maxHp = level * 12472 * mob.level * 2 + level * 7263 * mob.tempId;
    }

    public static void initMopbKhiGas(Mob mob, int level) {
        mob.point.maxHp = 20000000 * level;
        mob.point.dame = 10000 * level;
    }

    public static void hoiSinhMob(Mob mob) {
        mob.point.hp = mob.point.maxHp;
        mob.setTiemNang();
        Message msg;
        try {
            msg = new Message(-13);
            msg.writer().writeByte(mob.id);
            msg.writer().writeByte(mob.tempId);
            msg.writer().writeByte(0); // level mob
            msg.writer().writeInt(mob.point.hp);
            Service.getInstance().sendMessAllPlayerInMap(mob.zone, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    private long lastTimeAttackPlayer;

    public boolean isDie() {
        return this.point.gethp() <= 0;
    }

    public boolean isSieuQuai() {
        return this.lvMob > 0;
    }

    public synchronized void injured(Player plAtt, int damage, boolean dieWhenHpFull) {
        if (!this.isDie()) {
            if (damage >= this.point.hp) {
                damage = this.point.hp;
            }
            // if (this.zone.map.mapId == 112) {
            // plAtt.NguHanhSonPoint++;
            // }
            if (!dieWhenHpFull) {
                if (this.point.hp == this.point.maxHp && damage >= this.point.hp) {
                    damage = this.point.hp - 1;
                }
                if (this.tempId == 0 && damage > 10) {
                    damage = 10;
                }
            }
            // if (plAtt != null) {
            // switch (plAtt.playerSkill.skillSelect.template.id) {
            // case Skill.KAMEJOKO:
            // case Skill.MASENKO:
            // case Skill.ANTOMIC:
            // if (plAtt.nPoint.multicationChuong > 0 &&
            // Util.canDoWithTime(plAtt.nPoint.lastTimeMultiChuong,
            // PlayerSkill.TIME_MUTIL_CHUONG)) {
            // damage *= plAtt.nPoint.multicationChuong;
            // plAtt.nPoint.lastTimeMultiChuong = System.currentTimeMillis();
            // }
            //
            // }
            // }
            this.point.hp -= damage;

            if (this.isDie()) {
                if (plAtt != null) {
                    this.lvMob = 0;
                    this.status = 0;
                    this.sendMobDieAffterAttacked(plAtt, damage);
                    TaskService.gI().checkDoneTaskKillMob(plAtt, this);
                    TaskService.gI().checkDoneSideTaskKillMob(plAtt, this);
                }
                this.lastTimeDie = System.currentTimeMillis();

                if (this.id == 13) {
                    this.zone.isbulon13Alive = false;
                }
                if (this.id == 14) {
                    this.zone.isbulon14Alive = false;
                }
                if (this.isSieuQuai()) {
                    // plAtt.achievement.plusCount(12);
                }
            } else {
                this.sendMobStillAliveAffterAttacked(damage, plAtt != null ? plAtt.nPoint.isCrit : false);
            }
            if (plAtt != null && plAtt.nPoint != null
                    && plAtt.nPoint.power + getTiemNangForPlayer(plAtt, damage) < plAtt.nPoint.getPowerLimit()) {
                Service.gI().addSMTN(plAtt, (byte) 2, getTiemNangForPlayer(plAtt, damage), true);
            }
        }
    }

    public int getTiemNangForPlayer(Player pl, int dame) {
        int levelPlayer = Service.gI().getCurrLevel(pl);
        int n = levelPlayer - this.level;
        int pDameHit = dame * 100 / point.getHpFull();
        int tiemNang = (int) (pDameHit * maxTiemNang / 100);
        if (tiemNang <= 0) {
            tiemNang = 1;
        }
        if (n >= 0) {
            for (int i = 0; i < n; i++) {
                int sub = tiemNang * 10 / 100;
                if (sub <= 0) {
                    sub = 1;
                }
                tiemNang -= sub;
            }
        } else {
            for (int i = 0; i < -n; i++) {
                int add = tiemNang * 10 / 100;
                if (add <= 0) {
                    add = 1;
                }
                tiemNang += add;
            }
        }
        if (tiemNang <= 0) {
            tiemNang = 1;
        }
        tiemNang = (int) pl.nPoint.calSucManhTiemNang(tiemNang);
        if (pl.zone.map.mapId == 122 || pl.zone.map.mapId == 123 || pl.zone.map.mapId == 124 || pl.zone.map.mapId == 135
                || pl.zone.map.mapId == 136 || pl.zone.map.mapId == 137 || pl.zone.map.mapId == 138) {
            tiemNang *= 200;
        }
        return tiemNang;
    }

    public boolean FindChar(int CharID) {
        List<Player> players = this.zone.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            Player pl = players.get(i);
            if (pl != null && pl.id == CharID) {
                return true;
            }
        }
        return false;
    }

    public void update() {
        if (this.isDie() && !Maintenance.isRuning) {
            switch (zone.map.type) {
                case ConstMap.MAP_DOANH_TRAI:
                    if (this.tempId == 22 && this.zone.map.mapId == 59 && FindChar(-2_147_479_965)) {
                        if (Util.canDoWithTime(lastTimeDie, 10000)) {
                            if (this.id == 13) {
                                this.zone.isbulon13Alive = true;
                            }
                            if (this.id == 14) {
                                this.zone.isbulon14Alive = true;
                            }
                            this.hoiSinh();
                            this.sendMobHoiSinh();
                        }

                    }
                    break;
                case ConstMap.MAP_BAN_DO_KHO_BAU:
                    if (this.tempId == 72 || this.tempId == 71) {// ro bot bao ve
                        if (System.currentTimeMillis() - this.lastTimeDie > 3000) {
                            try {
                                Message t = new Message(102);
                                t.writer().writeByte((this.tempId == 71 ? 7 : 6));
                                Service.getInstance().sendMessAllPlayerInMap(this.zone, t);
                                t.cleanup();
                            } catch (IOException e) {

                            }
                        }
                    }
                    break;
                case ConstMap.MAP_KHI_GAS:
                    break;
                default:
                    if (Util.canDoWithTime(lastTimeDie, 2000)) {
                        this.randomSieuQuai();
                        this.hoiSinh();
                        this.sendMobHoiSinh();
                    }
            }
        }
        effectSkill.update();
        attackPlayer();
    }

    private void attackPlayer() {
        if (!isDie() && !effectSkill.isHaveEffectSkill() && !(tempId == 0)) {

            if ((this.tempId == 72 || this.tempId == 71) && Util.canDoWithTime(lastTimeAttackPlayer, 300)) {
                List<Player> pl = getListPlayerCanAttack();
                if (!pl.isEmpty()) {
                    this.sendMobBossBdkbAttack(pl, (int) this.point.getDameAttack());
                } else {
                    if (this.tempId == 71) {
                        Player plA = getPlayerCanAttack();
                        if (plA != null) {
                            try {
                                Message t = new Message(102);
                                t.writer().writeByte(5);
                                t.writer().writeByte(plA.location.x);
                                this.location.x = plA.location.x;
                                Service.getInstance().sendMessAllPlayerInMap(this.zone, t);
                                t.cleanup();
                            } catch (IOException e) {

                            }
                        }

                    }
                }
                this.lastTimeAttackPlayer = System.currentTimeMillis();
            } else if (Util.canDoWithTime(lastTimeAttackPlayer, 2000)) {
                Player pl = getPlayerCanAttack();
                if (pl != null) {
                    this.mobAttackPlayer(pl);
                }
                this.lastTimeAttackPlayer = System.currentTimeMillis();
            }

        }
    }

    private void sendMobBossBdkbAttack(List<Player> players, int dame) {
        if (this.tempId == 72) {
            try {
                Message t = new Message(102);
                int action = Util.nextInt(0, 2);
                t.writer().writeByte(action);
                if (action != 1) {
                    this.location.x = players.get(Util.nextInt(0, players.size() - 1)).location.x;
                }
                t.writer().writeByte(players.size());
                for (Byte i = 0; i < players.size(); i++) {
                    t.writer().writeInt((int) players.get(i).id);
                    t.writer().writeInt((int) players.get(i).injured(null, (int) dame, false, true));
                }
                Service.getInstance().sendMessAllPlayerInMap(this.zone, t);
                t.cleanup();
            } catch (IOException e) {

            }
        } else if (this.tempId == 71) {
            try {
                Message t = new Message(102);
                t.writer().writeByte(Util.getOne(3, 4));
                t.writer().writeByte(players.size());
                for (Byte i = 0; i < players.size(); i++) {
                    t.writer().writeInt((int) players.get(i).id);
                    t.writer().writeInt((int) players.get(i).injured(null, (int) dame, false, true));
                }
                Service.getInstance().sendMessAllPlayerInMap(this.zone, t);
                t.cleanup();
            } catch (IOException e) {

            }
        }
    }

    private List<Player> getListPlayerCanAttack() {
        List<Player> plAttack = new ArrayList<>();
        int distance = (this.tempId == 71 ? 250 : 600);
        try {
            List<Player> players = this.zone.getNotBosses();
            for (Player pl : players) {
                if (!pl.isDie() && !pl.isBoss && !pl.effectSkin.isVoHinh) {
                    int dis = Util.getDistance(pl, this);
                    if (dis <= distance) {
                        plAttack.add(pl);
                    }
                }
            }
        } catch (Exception e) {

        }

        return plAttack;
    }

    private Player getPlayerCanAttack() {
        int distance = 100;
        Player plAttack = null;
        try {
            List<Player> players = this.zone.getNotBosses();
            for (Player pl : players) {
                if (pl != null && pl.effectSkill != null) {
                    if (!pl.isDie() && !pl.isBoss && !pl.effectSkin.isVoHinh && !pl.isNewPet) {
                        int dis = Util.getDistance(pl, this);
                        if (dis <= distance) {
                            plAttack = pl;
                            distance = dis;
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
        return plAttack;
    }

    public boolean isBigBoss() {
        return (this.tempId == ConstMob.HIRUDEGARN || this.tempId == ConstMob.VUA_BACH_TUOC
                || this.tempId == ConstMob.ROBOT_BAO_VE || this.tempId == ConstMob.GAU_TUONG_CUOP);
    }

    // **************************************************************************
    private void mobAttackPlayer(Player player) {
        int dameMob = (int) this.point.getDameAttack();
        if (player.charms.tdDaTrau > System.currentTimeMillis()) {
            dameMob /= 2;
        }
        if (this.isSieuQuai()) {
            dameMob = player.nPoint.hpMax / 10;
        }
        int dame = player.injured(null, dameMob, false, true);
        this.sendMobAttackMe(player, dame);
        this.sendMobAttackPlayer(player);
    }

    public void attack() {
        Player player = getPlayerCanAttack();
        if (!isDie() && !effectSkill.isHaveEffectSkill() && tempId != ConstMob.MOC_NHAN
                && tempId != ConstMob.BU_NHIN_MA_QUAI && tempId != ConstMob.CO_MAY_HUY_DIET && !this.isBigBoss()
                && (this.lvMob < 1 || MapService.gI().isMapPhoBan(this.zone.map.mapId))
                && Util.canDoWithTime(lastTimeAttackPlayer, timeAttack)) {
            if (player != null) {
                this.mobAttackPlayer(player);
            }
            this.lastTimeAttackPlayer = System.currentTimeMillis();
        }
    }

    private void sendMobAttackMe(Player player, int dame) {
        if (!player.isPet && !player.isBot && !player.isNewPet) {
            Message msg;
            try {
                msg = new Message(-11);
                msg.writer().writeByte(this.id);
                msg.writer().writeInt(dame); // dame
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {

            }
        }
    }

    private void sendMobAttackPlayer(Player player) {
        Message msg;
        try {
            msg = new Message(-10);
            msg.writer().writeByte(this.id);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeInt(player.nPoint.hp);
            Service.gI().sendMessAnotherNotMeInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void randomSieuQuai() {
        if (this.tempId != 0 && MapService.gI().isMapKhongCoSieuQuai(this.zone.map.mapId) && Util.nextInt(0, 150) < 1) {
            this.lvMob = 1;
        }
    }

    public void hoiSinh() {
        this.status = 5;
        this.point.hp = this.point.maxHp;
        this.setTiemNang();
    }

    public void sendMobHoiSinh() {
        Message msg;
        try {
            msg = new Message(-13);
            msg.writer().writeByte(this.id);
            msg.writer().writeByte(this.tempId);
            msg.writer().writeByte(lvMob);
            msg.writer().writeInt(this.point.hp);
            Service.gI().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    // **************************************************************************
    private void sendMobDieAffterAttacked(Player plKill, int dameHit) {
        Message msg;
        try {
            msg = new Message(-12);
            msg.writer().writeByte(this.id);
            msg.writer().writeInt(dameHit);
            msg.writer().writeBoolean(plKill.nPoint.isCrit); // crit
            List<ItemMap> items = mobReward(plKill, this.dropItemTask(plKill), msg);
            Service.gI().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
            hutItem(plKill, items);
        } catch (Exception e) {

        }
    }

    public void sendMobDieAfterMobMeAttacked(Player plKill, int dameHit) {
        this.status = 0;
        Message msg;
        try {
            if (this.id == 13) {
                this.zone.isbulon13Alive = false;
            }
            if (this.id == 14) {
                this.zone.isbulon14Alive = false;
            }
            msg = new Message(-12);
            msg.writer().writeByte(this.id);
            msg.writer().writeInt(dameHit);
            msg.writer().writeBoolean(false); // crit

            List<ItemMap> items = mobReward(plKill, this.dropItemTask(plKill), msg);
            Service.getInstance().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
            hutItem(plKill, items);
        } catch (IOException e) {
            Logger.logException(Mob.class, e);
        }
        // if (plKill.isPl()) {
        // if (TaskService.gI().IsTaskDoWithMemClan(plKill.playerTask.taskMain.id)) {
        // TaskService.gI().checkDoneTaskKillMob(plKill, this, true);
        // } else {
        // TaskService.gI().checkDoneTaskKillMob(plKill, this, false);
        // }
        //
        // }
        this.lastTimeDie = System.currentTimeMillis();
    }

    private void hutItem(Player player, List<ItemMap> items) {
        if (!player.isPet && !player.isNewPet && !player.isBot) {
            if (player.charms.tdThuHut > System.currentTimeMillis()) {
                for (ItemMap item : items) {
                    // if (item.itemTemplate.id != 590) {
                    // ItemMapService.gI().pickItem(player, item.itemMapId, true);
                    // }
                    ItemMapService.gI().pickItem(player, item.itemMapId, true);
                }
            }
        } else if (player.isTrieuhoipet) {
            if (((Thu_TrieuHoi) player).masterr.charms.tdThuHut > System.currentTimeMillis()) {
                for (ItemMap item : items) {
                    ItemMapService.gI().pickItem(((Thu_TrieuHoi) player).masterr, item.itemMapId, true);
                }
            }
        } else {
            if (((Pet) player).master.charms.tdThuHut > System.currentTimeMillis()) {
                for (ItemMap item : items) {
                    // if (item.itemTemplate.id != 590) {
                    // ItemMapService.gI().pickItem(((Pet) player).master, item.itemMapId, true);
                    // }
                    ItemMapService.gI().pickItem(((Pet) player).master, item.itemMapId, true);
                }
            }
        }
    }

    private List<ItemMap> mobReward(Player player, ItemMap itemTask, Message msg) {
        List<ItemMap> itemReward = new ArrayList<>();
        try {
            if (player == null) {
                return new ArrayList<>();
            }
            if (zone.map.mapId == 155 && Util.isTrue(7, 100)) {
                Item mts = ItemService.gI().createNewItem((short) (1066 + Util.nextInt(5)), 1);
                InventoryServiceNew.gI().addItemBag(player, mts);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được x1" + mts.template.name);
            }
            if (zone.map.mapId == 187) {
                if (Util.isTrue(50, 100)) {
                    Item mts = ItemService.gI().createNewItem((short) Util.nextInt(1545, 1559));
                    InventoryServiceNew.gI().addItemBag(player, mts);
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendThongBao(player, "Bạn vừa nhận được x1" + mts.template.name);
                }
            }
            if (Util.isTrue(60, 100)) {
                if ((player.setClothes.godClothes || player.setClothes.IsSetHuyDiet()) && MapService.gI().isMapCold(player.zone.map)) {
                    ArrietyDrop.DropItemReWard(player,
                            ArrietyDrop.list_thuc_an[Util.nextInt(0, (ArrietyDrop.list_thuc_an.length - 1))], 1,
                            this.location.x, this.location.y);
                }
            }
            itemReward = this.getItemMobReward(player, this.location.x + Util.nextInt(-10, 10),
                    this.zone.map.yPhysicInTop(this.location.x, this.location.y));
            if (itemTask != null) {
                itemReward.add(itemTask);
            }
            if (Util.isTrue(50, 90000)) {
                if (MapService.gI().isMapCold(player.zone.map)) {
                    byte randomDo = (byte) new Random().nextInt(Manager.itemIds_TL.length - 1);
                    ItemMap itemTL = Util.ratiItem(zone, Manager.itemIds_TL[randomDo], 1, this.location.x,
                            this.location.y, player.id);
                    Service.gI().dropItemMap(this.zone, itemTL);
                    if (player.charms.tdThuHut > System.currentTimeMillis()) {
                        ItemMapService.gI().pickItem(player, itemTL.itemMapId, true);
                    }
                }
            }
            msg.writer().writeByte(itemReward.size());
            for (ItemMap itemMap : itemReward) {

                msg.writer().writeShort(itemMap.itemMapId);
                msg.writer().writeShort(itemMap.itemTemplate.id);
                msg.writer().writeShort(itemMap.x);
                msg.writer().writeShort(itemMap.y);
                msg.writer().writeInt((int) itemMap.playerId);
            }
        } catch (Exception e) {

        }
        return itemReward;
    }

    public boolean MapStart(int mapid) {
        return mapid == 0;
    }

    public List<ItemMap> getItemMobReward(Player player, int x, int yEnd) {
        List<ItemMap> list = new ArrayList<>();
        MobReward mobReward = Manager.MOB_REWARDS.get(this.tempId);
        if (mobReward == null) {
            return list;
        }
        final Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(11);
        if (MapStart(player.zone.map.mapId)) {
            return new ArrayList<>();
        }
        List<ItemMobReward> items = mobReward.getItemReward();
        List<ItemMobReward> golds = mobReward.getGoldReward();
        if (this.zone.map.mapId >= 0) {
            if (Util.isTrue(50, 100)) { // vàng từ quái tất cả map
                int gold = Util.nextInt(100000, 1000000);
                list.add(new ItemMap(zone, 76, gold, x, player.location.y, player.id));
            } else if (Util.isTrue(1, 300)) { // ngoc xanh
                list.add(new ItemMap(zone, 457, 1, x, player.location.y, player.id));
            } else if (Util.isTrue(5, 100)) { // thoi vang
                list.add(new ItemMap(zone, 77, Util.nextInt(1, 20), x, player.location.y, player.id));
            } else if (Util.isTrue(5, 100)) { // thoi vang
                list.add(new ItemMap(zone, 861, Util.nextInt(1, 10), x, player.location.y, player.id));
            }
        }

        if (!items.isEmpty()) {
            ItemMobReward item = items.get(Util.nextInt(0, items.size() - 1));
            ItemMap itemMap = item.getItemMap(zone, player, x, yEnd);
            if (itemMap != null) {
                list.add(itemMap);
            }
        }
        if (!golds.isEmpty()) {
            ItemMobReward gold = golds.get(Util.nextInt(0, golds.size() - 1));
            ItemMap itemMap = gold.getItemMap(zone, player, x, yEnd);
            if (itemMap != null) {
                list.add(itemMap);
            }
        }
        // quan than linh
        if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if (Util.isTrue(5, 90000)) {
                Item Quanthanlinh = ItemService.gI().createNewItem((short) (556));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(22, Util.nextInt(55, 65)));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(21, Util.nextInt(15, 17)));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + Quanthanlinh.template.name);
            }
        }
        if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if (Util.isTrue(5, 90000)) {
                Item Quanthanlinhxd = ItemService.gI().createNewItem((short) (560));
                Quanthanlinhxd.itemOptions.add(new Item.ItemOption(22, Util.nextInt(45, 55)));
                Quanthanlinhxd.itemOptions.add(new Item.ItemOption(21, Util.nextInt(15, 17)));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinhxd);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + Quanthanlinhxd.template.name);
            }
        }
        if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if (Util.isTrue(5, 90000)) {
                Item Quanthanlinhnm = ItemService.gI().createNewItem((short) (558));
                Quanthanlinhnm.itemOptions.add(new Item.ItemOption(22, Util.nextInt(45, 60)));
                Quanthanlinhnm.itemOptions.add(new Item.ItemOption(21, Util.nextInt(15, 17)));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinhnm);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + Quanthanlinhnm.template.name);
            }
        }
        if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if (Util.isTrue(5, 90000)) {
                Item Aothanlinh = ItemService.gI().createNewItem((short) (555));
                Aothanlinh.itemOptions.add(new Item.ItemOption(47, Util.nextInt(500, 600)));
                Aothanlinh.itemOptions.add(new Item.ItemOption(21, Util.nextInt(15, 17)));
                InventoryServiceNew.gI().addItemBag(player, Aothanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + Aothanlinh.template.name);
            }
        }
        if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if (Util.isTrue(5, 90000)) {
                Item Aothanlinhnm = ItemService.gI().createNewItem((short) (557));
                Aothanlinhnm.itemOptions.add(new Item.ItemOption(47, Util.nextInt(400, 550)));
                Aothanlinhnm.itemOptions.add(new Item.ItemOption(21, Util.nextInt(15, 17)));
                InventoryServiceNew.gI().addItemBag(player, Aothanlinhnm);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + Aothanlinhnm.template.name);
            }
        }
        if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if (Util.isTrue(5, 90000)) {
                Item Aothanlinhxd = ItemService.gI().createNewItem((short) (559));
                Aothanlinhxd.itemOptions.add(new Item.ItemOption(47, Util.nextInt(600, 700)));
                Aothanlinhxd.itemOptions.add(new Item.ItemOption(21, Util.nextInt(15, 17)));
                InventoryServiceNew.gI().addItemBag(player, Aothanlinhxd);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + Aothanlinhxd.template.name);
            }
        }

        if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if (Util.isTrue(5, 90000)) {
                Item Gangthanlinh = ItemService.gI().createNewItem((short) (562));
                Gangthanlinh.itemOptions.add(new Item.ItemOption(0, Util.nextInt(6000, 7000)));
                Gangthanlinh.itemOptions.add(new Item.ItemOption(21, Util.nextInt(15, 17)));
                InventoryServiceNew.gI().addItemBag(player, Gangthanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + Gangthanlinh.template.name);
            }
        }
        if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if (Util.isTrue(5, 90000)) {
                Item Gangthanlinhxd = ItemService.gI().createNewItem((short) (566));
                Gangthanlinhxd.itemOptions.add(new Item.ItemOption(0, Util.nextInt(6500, 7500)));
                Gangthanlinhxd.itemOptions.add(new Item.ItemOption(21, Util.nextInt(15, 17)));
                InventoryServiceNew.gI().addItemBag(player, Gangthanlinhxd);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + Gangthanlinhxd.template.name);
            }
        }
        if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if (Util.isTrue(5, 90000)) {
                Item Gangthanlinhnm = ItemService.gI().createNewItem((short) (564));
                Gangthanlinhnm.itemOptions.add(new Item.ItemOption(0, Util.nextInt(5500, 6500)));
                Gangthanlinhnm.itemOptions.add(new Item.ItemOption(21, Util.nextInt(15, 17)));
                InventoryServiceNew.gI().addItemBag(player, Gangthanlinhnm);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + Gangthanlinhnm.template.name);
            }
        }
        if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if (Util.isTrue(5, 90000)) {
                Item Giaythanlinh = ItemService.gI().createNewItem((short) (563));
                Giaythanlinh.itemOptions.add(new Item.ItemOption(23, Util.nextInt(50, 60)));
                Giaythanlinh.itemOptions.add(new Item.ItemOption(21, Util.nextInt(15, 17)));
                InventoryServiceNew.gI().addItemBag(player, Giaythanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + Giaythanlinh.template.name);
            }
        }
        if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if (Util.isTrue(5, 90000)) {
                Item Giaythanlinhxd = ItemService.gI().createNewItem((short) (567));
                Giaythanlinhxd.itemOptions.add(new Item.ItemOption(23, Util.nextInt(55, 65)));
                Giaythanlinhxd.itemOptions.add(new Item.ItemOption(21, Util.nextInt(15, 17)));
                InventoryServiceNew.gI().addItemBag(player, Giaythanlinhxd);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + Giaythanlinhxd.template.name);
            }
        }
        if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if (Util.isTrue(5, 90000)) {
                Item Giaythanlinhnm = ItemService.gI().createNewItem((short) (565));
                Giaythanlinhnm.itemOptions.add(new Item.ItemOption(23, Util.nextInt(65, 75)));
                Giaythanlinhnm.itemOptions.add(new Item.ItemOption(21, Util.nextInt(15, 17)));
                InventoryServiceNew.gI().addItemBag(player, Giaythanlinhnm);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + Giaythanlinhnm.template.name);
            }
        }
        if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if (Util.isTrue(5, 90000)) {
                Item Nhanthanlinh = ItemService.gI().createNewItem((short) (561));
                Nhanthanlinh.itemOptions.add(new Item.ItemOption(14, Util.nextInt(13, 16)));
                Nhanthanlinh.itemOptions.add(new Item.ItemOption(21, Util.nextInt(15, 17)));
                InventoryServiceNew.gI().addItemBag(player, Nhanthanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + Nhanthanlinh.template.name);
            }
        }
        if (this.zone.map.mapId >= 190 && this.zone.map.mapId <= 190) {
            if (Util.isTrue(5, 1000)) {
                Item xuhaitac = ItemService.gI().createNewItem((short) 1335);
                xuhaitac.itemOptions.add(new Item.ItemOption(73, 0));
                InventoryServiceNew.gI().addItemBag(player, xuhaitac);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + xuhaitac.template.name);
            }
        }
        if (this.zone.map.mapId >= 5 && this.zone.map.mapId <= 5
                || this.zone.map.mapId >= 29 && this.zone.map.mapId <= 29
                || this.zone.map.mapId >= 30 && this.zone.map.mapId <= 30) {
            if (Util.isTrue(10, 100)) {
                Item vpskhene = ItemService.gI().createNewItem((short) (Util.nextInt(695, 698)));
                vpskhene.itemOptions.add(new Item.ItemOption(174, 2024));
                InventoryServiceNew.gI().addItemBag(player, vpskhene);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + vpskhene.template.name);
            }
        }
        if (this.zone.map.mapId >= 156 && this.zone.map.mapId <= 159) {
            if (Util.isTrue(5, 500)) {
                Item capsulebang = ItemService.gI().createNewItem((short) (1382));
                InventoryServiceNew.gI().addItemBag(player, capsulebang);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + capsulebang.template.name);
            }
        }
        if (this.zone.map.mapId >= 156 && this.zone.map.mapId <= 159) {
            if (Util.isTrue(5, 500)) {
                Item capsulebang = ItemService.gI().createNewItem((short) (1382));
                InventoryServiceNew.gI().addItemBag(player, capsulebang);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + capsulebang.template.name);
            }
        }
        if (this.zone.map.mapId >= 156 && this.zone.map.mapId <= 159) {
            if (Util.isTrue(5, 100)) {
                Item manhbt = ItemService.gI().createNewItem((short) (Util.nextInt(933, 934)));
                InventoryServiceNew.gI().addItemBag(player, manhbt);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + manhbt.template.name);
            }
        }
        if (this.zone.map.mapId >= 122 && this.zone.map.mapId <= 124) {
            int random = Util.nextInt(1, 3);
            Util.isTrue(20, 100);
            player.NguHanhSonPoint += random;
            Service.gI().sendThongBao(player, "Bạn Nhận Được " + random + "  Điểm Ngũ Hành Sơn");
        }
        if (player.itemTime.isUseMayDo && Util.isTrue(5, 100) && this.tempId > 57 && this.tempId < 66) {
            list.add(new ItemMap(zone, 380, 1, x, player.location.y, player.id));
        } // vat phẩm rơi khi user maaáy dò adu hoa r o day ti code choa
        if (player.itemTime.isUseMayDo2 && Util.isTrue(1, 100) && this.tempId > 1 && this.tempId < 81) {
            list.add(new ItemMap(zone, 2036, 1, x, player.location.y, player.id));// cai nay sua sau nha
        }
        if (player.cFlag >= 1 && Util.isTrue(100, 100) && this.tempId == 0 && hour != 1 && hour != 3 && hour != 5
                && hour != 7 && hour != 9 && hour != 11 && hour != 13 && hour != 15 && hour != 17 && hour != 19
                && hour != 21 && hour != 23) { // up bí kíp
            list.add(new ItemMap(zone, 590, 2, x, player.location.y, player.id));// cai nay sua sau nha
            if (Util.isTrue(50, 100) && this.tempId == 0) { // up bí kíp
                list.add(new ItemMap(zone, 590, 2, x, player.location.y, player.id));
                if (Util.isTrue(50, 100) && this.tempId == 0) { // up bí kíp
                    list.add(new ItemMap(zone, 590, 2, x, player.location.y, player.id));
                    if (Util.isTrue(50, 100) && this.tempId == 0) { // up bí kíp
                        list.add(new ItemMap(zone, 590, 2, x, player.location.y, player.id));
                    }
                }
            }
        }
        if (this.zone.map.mapId >= 197 && this.zone.map.mapId <= 197) {
            if (Util.isTrue(10, 100)) {
                Item thudan = ItemService.gI().createNewItem((short) (1478));
                InventoryServiceNew.gI().addItemBag(player, thudan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + thudan.template.name);
            }
        }
        if (player.isPl()) {
            int mapId = player.zone.map.mapId;
            if (!player.isBoss && (mapId == 1 || mapId == 2 || mapId == 3 || mapId == 15 || mapId == 16 || mapId == 17
                    || mapId == 8 || mapId == 9 || mapId == 11)) {
                if (Util.isTrue(5, 100)) {
                    int[][] itemKH = { { 0, 6, 21, 27, 12 }, { 1, 7, 22, 28, 12 }, { 2, 8, 23, 29, 12 } }; // td,
                    int skhId = ItemService.gI().randomSKHId(player.gender);
                    ItemMap it = ItemService.gI().itemMapSKH(zone, itemKH[player.gender][Util.nextInt(0, 4)], 1,
                            this.location.x, this.location.y, player.id, skhId);
                    list.add(it);
                }
            }
        }
        if (this.tempId > 0 && this.zone.map.mapId >= 156 && this.zone.map.mapId <= 159
                && player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
            // if (Util.isTrue(10, 100)) { //up bí kíp
            // list.add(new ItemMap(zone, 2076, 1, x, player.location.y, player.id));}
        }
        if (this.tempId > 0 && this.zone.map.mapId >= 156 && this.zone.map.mapId <= 159) {
            if (Util.isTrue(5, 100)) { // up bí kíp
                list.add(new ItemMap(zone, 933, 1, x, player.location.y, player.id));
            }
        }
        if (this.tempId > 0 && this.zone.map.mapId == 155 && player.setClothes.IsSetHuyDiet()) {
            if (Util.isTrue(0.5f, 250)) { // up bí kíp
                list.add(new ItemMap(zone, Util.nextInt(1066, 1070), 1, x, player.location.y, player.id));
            }
        }
        if (this.zone.map.mapId >= 174 && this.zone.map.mapId <= 176) {
            if (Util.isTrue(100, 100)) {
                Item votbatbo = ItemService.gI().createNewItem((short) (1611));
                InventoryServiceNew.gI().addItemBag(player, votbatbo);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + votbatbo.template.name);
            }
        }
        if (this.zone.map.mapId >= 182 && this.zone.map.mapId <= 182) {
            if (Util.isTrue(10, 100)) {
                Item dahoangkim = ItemService.gI().createNewItem((short) (1279));
                InventoryServiceNew.gI().addItemBag(player, dahoangkim);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + dahoangkim.template.name);
            }
        }
        if (this.zone.map.mapId >= 250 && this.zone.map.mapId <= 250) {
            if (Util.isTrue(35, 100)) {
                Item khucmia = ItemService.gI().createNewItem((short) (1646));
                InventoryServiceNew.gI().addItemBag(player, khucmia);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + khucmia.template.name);
            }
        }
        if (this.zone.map.mapId >= 250 && this.zone.map.mapId <= 250) {
            if (Util.isTrue(10, 100)) {
                Item cucda = ItemService.gI().createNewItem((short) (1645));
                InventoryServiceNew.gI().addItemBag(player, cucda);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + cucda.template.name);
            }
        }
        if (this.zone.map.mapId >= 198 && this.zone.map.mapId <= 198) {
            if (Util.isTrue(10, 100)) {
                Item trangsachcu = ItemService.gI().createNewItem((short) (1393));
                InventoryServiceNew.gI().addItemBag(player, trangsachcu);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + trangsachcu.template.name);
            }
        }
        if (this.zone.map.mapId >= 198 && this.zone.map.mapId <= 198) {
            if (Util.isTrue(1, 9000000)) {
                Item kimbamgiay = ItemService.gI().createNewItem((short) (1390));
                InventoryServiceNew.gI().addItemBag(player, kimbamgiay);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + kimbamgiay.template.name);
            }
        }
        for (Item item : player.inventory.itemsBody) {
            if (this.zone.map.mapId >= 0) {
                if (item.isNotNullItem()) {
                    if (item.template.id == 2081) {
                        if (Util.isTrue(15, 100)) { // up skien bkt lam ne
                            list.add(new ItemMap(zone, Util.nextInt(2083, 2083), 1, x, player.location.y, player.id));
                        }
                    } else if (item.template.id != 2081) {
                        if (Util.isTrue(0, 1)) {
                            list.add(new ItemMap(zone, 76, 1, x, player.location.y, player.id));
                        }
                    }
                }
            }
            if (this.zone.map.mapId >= 0) {
                if (item.isNotNullItem()) {
                    if (item.template.id == 1411) {
                        if (Util.isTrue(10, 100)) { // up bí kíp
                            list.add(new ItemMap(zone, Util.nextInt(1004, 1004), 1, x, player.location.y, player.id));
                        }
                    } else if (item.template.id != 1411) {
                        if (Util.isTrue(0, 1)) {
                            list.add(new ItemMap(zone, 76, 1, x, player.location.y, player.id));
                        }
                    }
                }
            }
        }

        if (this.zone.map.mapId == 250 && Util.isTrue(1, 900000000)) {
            ItemMap itemx = new ItemMap(zone, 2000 + player.gender, 1, x, player.location.y, player.id);
            itemx.options.add(new Item.ItemOption(30, 1));
            list.add(itemx);
        }
        if (player.zone != null && player.zone.map != null && MapService.gI().isMapBanDoKhoBau(player.zone.map.mapId)) {
            if (player.clan != null && player.clan.BanDoKhoBau != null) {
                int level = player.clan.BanDoKhoBau.level;
                int slhn = Util.nextInt(1, 3) * (level / 10);
                slhn = slhn < 5 ? 5 : slhn;
                if (Util.nextInt(0, 100) < 100) {

                    list.add(new ItemMap(zone, 861, slhn, x, player.location.y, player.id));
                }
            }
        }
        // bi kip moc nhan
        if (tempId == ConstMob.MOC_NHAN) {
            if (TimeUtil.getCurrHour() % 2 == 0) {
                if (Util.isTrue(33, 100)) {
                    list.add(new ItemMap(zone, 590, 1, x, player.location.y, player.id));
                }
            }
        }
        return list;
    }

    private ItemMap dropItemTask(Player player) {
        ItemMap itemMap = null;
        switch (this.tempId) {
            case ConstMob.KHUNG_LONG:
            case ConstMob.LON_LOI:
            case ConstMob.QUY_DAT:
                if (TaskService.gI().getIdTask(player) == ConstTask.TASK_2_0) {
                    itemMap = new ItemMap(this.zone, 73, 1, this.location.x, this.location.y, player.id);
                }
                break;
        }
        if (itemMap != null) {
            return itemMap;
        }
        return null;
    }

    private void sendMobStillAliveAffterAttacked(int dameHit, boolean crit) {
        Message msg;
        try {
            msg = new Message(-9);
            msg.writer().writeByte(this.id);
            msg.writer().writeInt(this.point.gethp());
            msg.writer().writeInt(dameHit);
            msg.writer().writeBoolean(crit); // chí mạng
            msg.writer().writeInt(-1);
            Service.gI().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }
}

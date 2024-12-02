package com.KhanhDTK.models.player;

import com.KhanhDTK.De2.Thu_TrieuHoi;
import com.KhanhDTK.card.Card;
import com.KhanhDTK.models.map.doanhtrai.DoanhTraiService;
import com.KhanhDTK.models.map.MapMaBu.MapMaBu;
import com.KhanhDTK.models.skill.PlayerSkill;

import java.util.List;

import com.KhanhDTK.models.clan.Clan;
import com.KhanhDTK.models.intrinsic.IntrinsicPlayer;
import com.KhanhDTK.models.item.Item;
import com.KhanhDTK.models.item.ItemTime;
import com.KhanhDTK.models.npc.specialnpc.MagicTree;
import com.KhanhDTK.consts.ConstPlayer;
import com.KhanhDTK.consts.ConstTask;
import com.KhanhDTK.models.npc.specialnpc.MabuEgg;
import com.KhanhDTK.models.mob.MobMe;
import com.KhanhDTK.data.DataGame;
//import com.KhanhDTK.models.Event.CauCa;
//import com.KhanhDTK.models.ThanhTich.ThanhTich;
import com.KhanhDTK.models.clan.ClanMember;
import static com.KhanhDTK.models.item.ItemTime.TEXT_NHAN_BUA_MIEN_PHI;
import com.KhanhDTK.models.map.BDKB.BanDoKhoBauService;
import com.KhanhDTK.models.map.TrapMap;
import com.KhanhDTK.models.map.Zone;
//import com.KhanhDTK.models.yadat.Yadat;
import com.KhanhDTK.models.map.blackball.BlackBallWar;
import com.KhanhDTK.models.map.gas.GasService;
import com.KhanhDTK.models.matches.IPVP;
import com.KhanhDTK.models.matches.TYPE_LOSE_PVP;
import com.KhanhDTK.models.matches.TYPE_PVP;
//import com.KhanhDTK.models.matches.pvp.DaiHoiVoThuat;
import com.KhanhDTK.models.npc.specialnpc.BillEgg;
import com.KhanhDTK.models.skill.Skill;
import com.KhanhDTK.server.Manager;
import com.KhanhDTK.services.Service;
import com.KhanhDTK.server.io.MySession;
import com.KhanhDTK.models.task.TaskPlayer;
import com.girlkun.network.io.Message;
import com.KhanhDTK.server.Client;
import com.KhanhDTK.services.EffectSkillService;
import com.KhanhDTK.services.FriendAndEnemyService;
import com.KhanhDTK.services.ItemTimeService;
import com.KhanhDTK.services.MapService;
import com.KhanhDTK.services.NpcService;
import com.KhanhDTK.services.PetService;
import com.KhanhDTK.services.PlayerService;
import com.KhanhDTK.services.TaskService;
import com.KhanhDTK.services.func.ChangeMapService;
import com.KhanhDTK.services.func.ChonAiDay;
import com.KhanhDTK.services.func.CombineNew;
import com.KhanhDTK.services.func.SummonDragon;
import com.KhanhDTK.services.func.TopService;
import com.KhanhDTK.utils.Logger;
import com.KhanhDTK.utils.SkillUtil;
import com.KhanhDTK.utils.Util;
import com.KhanhDTK.models.mob.Mob;
import com.KhanhDTK.services.SkillService;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
//import javafx.scene.effect.Effect;

public class Player {

    public boolean isBot = false;
    public Clone clone;
    public boolean isClone;

    public long lastTimeTranformation;
    public int isbienhinh;
    public boolean lockPK;
    public Timer timerDHVT;
    public Player _friendGiaoDich;
    public int kemtraicay = 0;
    public int nuocmia = 0;

    public Date firstTimeLogin;
    public int luotNhanBuaMienPhi = 1;
    public int capboss = 0;
    public long lastTimeRevived1;
    public String TrieuHoiNamePlayer;
    public int TrieuHoiCapBac = -1;
    public String TenThuTrieuHoi;
    public int TrieuHoiThucAn;
    public int TrieuHoiDame;
    public int TrieuHoiHP;
    public long TrieuHoilastTimeThucan;
    public int TrieuHoiLevel;
    public long TrieuHoiExpThanThu;
    public Player TrieuHoiPlayerAttack;
    public double TrieuHoidamethanmeo;
    public Thu_TrieuHoi TrieuHoipet;
    public boolean isTrieuhoipet;
    public boolean justRevived1;
    public long Autothucan;
    public boolean trangthai = false;
    public long timeUseSkill;
    public int capChuyenSinh = 0;
    public int[] BktDauLaDaiLuc = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    public int Bkt_Exp_Tu_Ma = 0;
    public int Bkt_Ma_Hoa = 0;
    public long BktLasttimeMaHoa;
    public byte BktPhiPham = 10;
    public long BktLasttimeMaLenh;
    public int Bkt_Tu_Ma = 0;
    public int Captutien = 0;
    public long Exptutien = 0;
    public int Bkt_Ma_cot;
    public int[] Bkttutien = new int[] { 0, 0, 0 };
    public double dametong = 0;
    public String Hppl = "\n";
    public boolean resetdame = false;
    public long lastTimeDame;
    public long timevip;
    public long tutien;
    public byte vip;
    public byte CheckDayOnl;

    public int DuaHau;

    public boolean isdem = false;
    public long timeoff = 0;
    public boolean isTitleUse;
    public long lastTimeTitle1;
    public byte typetrain;
    public int expoff;
    public boolean istrain;
    public boolean istry;
    public boolean istry1;
    public boolean isfight;
    public boolean isfight1;
    public boolean isfake;
    public boolean seebossnotnpc;
    public boolean SetStart;
    public long LastStart;
    public int goldTai;
    public int goldXiu;
    public MySession session;
    public boolean beforeDispose;
    public Gift gift;
    // public List<ThanhTich> Archivement = new ArrayList<>();
    public boolean isPet;
    public boolean isNewPet;
    public int TimeOnline = 0;
    public boolean DoneDTDN = false;
    public boolean DoneDKB = false;
    public boolean JoinNRSD = false;
    public boolean DoneNRSD = false;
    public int TickCauCa = 0;
    public int NapNgay = 0;
    public int point_gapthu = 0;
    public int pointSb;
    public int topnv;
    public int topsm;
    public int topnap;
    public int sm;
    public long LastTimeOnline = System.currentTimeMillis() + 30000;
    public boolean tickxanh = false;
    public boolean isBoss;
    public boolean isYadat;
    public int NguHanhSonPoint = 0;
    public IPVP pvp;
    public int pointPvp;
    public int SuperAura;
    public int PointBoss;
    public int point_hungvuong;
    public byte maxTime = 30;
    public byte type = 0;
    public int ResetSkill = 0;
    public int mapIdBeforeLogout;
    // public boolean tickxanh = false;
    public List<Zone> mapBlackBall;
    public List<Zone> mapMaBu;
    public long limitgold = 0;
    public long LastDoanhTrai = 0;
    public Zone zone;
    public Zone mapBeforeCapsule;
    public Zone mapBeforeCapsule_2;
    public List<Zone> mapCapsule;
    public Pet pet;
    public NewPet newpet;
    public MobMe mobMe;
    public Location location;
    public SetClothes setClothes;
    public EffectSkill effectSkill;
    public MabuEgg mabuEgg;
    public BillEgg billEgg;
    public TaskPlayer playerTask;
    public ItemTime itemTime;
    public Fusion fusion;
    public MagicTree magicTree;
    public IntrinsicPlayer playerIntrinsic;
    public Inventory inventory;
    public PlayerSkill playerSkill;
    public CombineNew combineNew;
    public IDMark iDMark;
    public Charms charms;
    public EffectSkin effectSkin;
    public NPoint nPoint;
    public RewardBlackBall rewardBlackBall;
    public EffectFlagBag effectFlagBag;
    public FightMabu fightMabu;
    public SkillSpecial skillSpecial;
    public Clan clan;
    public ClanMember clanMember;
    public List<Friend> friends;
    public List<Enemy> enemies;

    public long id;
    public String name;
    public byte gender;
    public boolean isNewMember = true;
    public short head;
    public byte typePk;
    public byte cFlag;
    public boolean haveTennisSpaceShip;
    public boolean justRevived;
    public long lastTimeRevived;
    public boolean banv = false;
    public boolean muav = false;
    public long timeudbv = 0;
    public long timeudmv = 0;
    public long lasttimebanv;
    public long lasttimemuav;

    public int violate;
    public byte totalPlayerViolate;
    public long timeChangeZone;
    public long lastTimeUseOption;

    public short idNRNM = -1;
    public short idGo = -1;
    public long lastTimePickNRNM;
    public int goldNormar;
    public int goldVIP;
    public long lastTimeWin;
    public boolean isWin;
    public List<Card> Cards = new ArrayList<>();
    public short idAura = -1;
    public int vnd;
    public long diemdanh;
    public long diemdanhsk;
    public long leothap = 0;
    public int gioithieu;
    public int VND;
    public int tongnap;
    public int TONGNAP;
    public int levelWoodChest;
    public boolean receivedWoodChest;
    public int goldChallenge;
    public boolean bdkb_isJoinBdkb;
    // public CauCa cauca;
    public int data_task;
    public List<Integer> idEffChar = new ArrayList<>();

    public Player() {
        lastTimeUseOption = System.currentTimeMillis();
        location = new Location();
        nPoint = new NPoint(this);
        inventory = new Inventory();
        playerSkill = new PlayerSkill(this);
        setClothes = new SetClothes(this);
        effectSkill = new EffectSkill(this);
        fusion = new Fusion(this);
        playerIntrinsic = new IntrinsicPlayer();
        rewardBlackBall = new RewardBlackBall(this);
        effectFlagBag = new EffectFlagBag();
        fightMabu = new FightMabu(this);
        // ----------------------------------------------------------------------
        iDMark = new IDMark();
        combineNew = new CombineNew();
        playerTask = new TaskPlayer();
        friends = new ArrayList<>();
        enemies = new ArrayList<>();
        itemTime = new ItemTime(this);
        charms = new Charms();
        effectSkin = new EffectSkin(this);
        skillSpecial = new SkillSpecial(this);
        gift = new Gift(this);
        // cauca = new CauCa(this);
    }

    public void CreatePet(String NamePet) {
        this.TenThuTrieuHoi = NamePet;
        this.TrieuHoilastTimeThucan = System.currentTimeMillis();
        this.TrieuHoiThucAn = 100;
        this.TrieuHoiLevel = 0;
        this.TrieuHoiExpThanThu = 0;
        this.TrieuHoiCapBac = 0; // Util.nextInt(0, Util.nextInt(3, 10));
        this.TrieuHoiDame = Util.GioiHannext(10000, 10000 + ((this.TrieuHoiCapBac + 1) * 10000));
        this.TrieuHoiHP = Util.GioiHannext(100000, 100000 + ((this.TrieuHoiCapBac + 1) * 50000));
    }

    // --------------------------------------------------------------------------
    public boolean isDie() {
        if (this.nPoint != null) {
            return this.nPoint.hp <= 0;
        }
        return true;
    }

    // --------------------------------------------------------------------------
    public void setSession(MySession session) {
        this.session = session;
    }

    public void sendMessage(Message msg) {
        if (this.session != null) {
            session.sendMessage(msg);
        }
    }

    public MySession getSession() {
        return this.session;
    }

    public boolean isPl() {
        return !isPet && !isBoss && !isNewPet && !isTrieuhoipet && !isBot && !isClone;
    }

    public void update() {
        if (this.isBot) {
            active();
        }
        final Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(11);

        if (SetStart) {
            SetStart = false;
            if (inventory.itemsBody.get(11).isNotNullItem()) {
            }
        }
        if (!this.beforeDispose && this != null && !this.isBot) {
            try {
                if (!iDMark.isBan()) {
                    if (this != null) {
                        if (nPoint != null) {
                            nPoint.update();
                        }
                        if (fusion != null) {
                            fusion.update();
                        }
                        if (this.isPl() && this.clan != null && this.clan.khiGas != null) {
                            GasService.gI().update(this);
                        }
                        if (this.isPl() && !this.isDie() && this.Captutien >= 50 && this.Bkttutien[2] >= 1
                                && this.Bkttutien[0] >= BktDieukiencanhgioi(
                                        Util.BKT(this.Bkttutien[1]))) {
                            if (Util.isTrue(Bkttilecanhgioi(Util.BKT(this.Bkttutien[1])), 100)) {
                                this.Bkttutien[0] -= BktDieukiencanhgioi(
                                        Util.BKT(this.Bkttutien[1]));
                                this.Bkttutien[1]++;
                                Service.gI().sendThongBao(this, "Bạn đã thăng cảnh giới thành công lên: "
                                        + this.BktTuviTutien(Util.BKT(this.Bkttutien[1])));
                            } else {
                                this.Bkttutien[0] -= BktDieukiencanhgioi(
                                        Util.BKT(this.Bkttutien[1]));
                                if (Util.isTrue(20f, 100)) {
                                    this.Bkt_Ma_cot += Util.nextInt(1, Util.nextInt(1, Util.nextInt(1, 5)));
                                    Service.gI().sendThongBaoOK(this, "Trong lúc tu tiên thất bại bạn nhận đc ma cốt");
                                }
                                Service.gI().sendThongBao(this,
                                        "Bạn đã thăng cảnh giới thất bại và bị mất Exp tu tiên, cảnh giới bạn vẫn ở: "
                                                + this.BktTuviTutien(Util.BKT(this.Bkttutien[1])));
                                this.setDie(this);
                            }
                        }
                        if (this.istrain && !MapService.gI().isMapTrainOff(this, this.zone.map.mapId)
                                && this.timeoff >= 30) {
                            ChangeMapService.gI().changeMapBySpaceShip(this, MapService.gI().getMapTrainOff(this), -1,
                                    250);
                            congExpOff();
                            this.timeoff = 0;
                        }
                        if (effectSkin != null) {
                            effectSkill.update();
                        }
                        if (mobMe != null) {
                            mobMe.update();
                        }
                        if (effectSkin != null) {
                            effectSkin.update();
                        }
                        if (gift != null) {
                            gift.dispose();
                            gift = null;
                        }
                        if (pet != null) {
                            pet.update();
                        }
                        if (TrieuHoipet != null) {
                            TrieuHoipet.update();
                        }
                        if (newpet != null) {
                            newpet.update();
                        }
                        if (clone != null) {
                            clone.update();
                        }
                        if (magicTree != null) {
                            magicTree.update();
                        }
                        if (itemTime != null) {
                            itemTime.update();
                            if (this.itemTime.isdkhi = false) {
                                // Service.gI().setNotMonkey(this);
                                Service.gI().Send_Caitrang(this);
                                Service.gI().point(this);
                                PlayerService.gI().sendInfoHpMp(this);
                                Service.gI().Send_Info_NV(this);
                                Service.gI().sendInfoPlayerEatPea(this);
                            }
                        }
                        if (this.timevip != 0 && Util.canDoWithTime(this.timevip, 1000)) {
                            timevip = 0;
                            vip = 0;
                        }
                        if (this.isPl() && this.setClothes.set8 == 5) {
                            Service.gI().addEffectChar(this, 213, 1, -1, -1, 0);
                        }
                        if (this.isPl()) {
                            Service.gI().addEffectChar(this, 57, 1, -1, -1, 0);
                        }
                        if (this.isPl() && this.getSession().tongnap >= 500000) {
                            Service.gI().sendTitle(this, 999);
                        }
                        if (this.lastTimeTitle1 != 0 && Util.canDoWithTime(this.lastTimeTitle1, 6000)) {
                            lastTimeTitle1 = 0;
                            isTitleUse = false;
                        }
                        // if (!isdem && (hour >= 0 || hour < 24)) {
                        // SummonDragon.gI().activeNight(this);
                        // isdem = true;
                        //// Service.getInstance().sendThongBao(this, "|7| Bóng tối bao phủ, hắc ám
                        // chuỗi dậy!");
                        // } else if (isdem && (hour >= 0 && hour < 24)) {
                        // SummonDragon.gI().activeNight(this);
                        //// SummonDragon.gI().activeDay(this);
                        // isdem = false;
                        //
                        // }
                        BlackBallWar.gI().update(this);
                        MapMaBu.gI().update(this);
                        send_text_time_nhan_bua_mien_phi();
                        updateEff(this);
                        if (this.iDMark.isGoToGas() && Util.canDoWithTime(this.iDMark.getLastTimeGotoGas(), 3000)) {
                            ChangeMapService.gI().changeMapBySpaceShip(this, 149, -1, 163);
                            this.iDMark.setGoToGas(false);
                        }

                        if (this != null && this.iDMark != null && !isBoss && this.iDMark.isGotoFuture()
                                && Util.canDoWithTime(this.iDMark.getLastTimeGoToFuture(), 6000)) {
                            ChangeMapService.gI().changeMapBySpaceShip(this, 102, -1, Util.nextInt(60, 200));
                            this.iDMark.setGotoFuture(false);
                        }
                        if (this.iDMark != null && this.iDMark.isGoToBDKB()
                                && Util.canDoWithTime(this.iDMark.getLastTimeGoToBDKB(), 6000)) {
                            ChangeMapService.gI().changeMapBySpaceShip(this, 135, -1, 35);
                            this.iDMark.setGoToBDKB(true);
                        }
                        if (this.isPl() && this.TrieuHoipet == null && this.TrieuHoiCapBac >= 0
                                && this.TrieuHoiCapBac <= 10) {
                            PetService.Thu_TrieuHoi(this);
                        } else if (this.isPl() && this.TrieuHoipet != null && this.TrieuHoiCapBac < 0
                                && this.TrieuHoiCapBac > 10) {
                            ChangeMapService.gI().exitMap(this.TrieuHoipet);
                            TrieuHoipet.dispose();
                            TrieuHoipet = null;
                        }
                        if (this.zone != null) {
                            TrapMap trap = this.zone.isInTrap(this);
                            if (trap != null) {
                                trap.doPlayer(this);
                            }
                        }
                        if (this.isPl() && this.inventory.itemsBody.get(7) != null) {
                            Item it = this.inventory.itemsBody.get(7);

                        } else if (this.isPl() && newpet != null && !this.inventory.itemsBody.get(7).isNotNullItem()) {
                            newpet.dispose();
                            newpet = null;
                        }
                        if (this.isPl() && isWin && this.zone.map.mapId == 51
                                && Util.canDoWithTime(lastTimeWin, 2000)) {
                            ChangeMapService.gI().changeMapBySpaceShip(this, 52, 0, -1);
                            isWin = false;
                        }
                    } else {
                        if (Util.canDoWithTime(iDMark.getLastTimeBan(), 5000)) {
                            Client.gI().kickSession(session);
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    public long lastTimeSendTextTime;

    public void send_text_time_nhan_bua_mien_phi() {
        if (Util.canDoWithTime(lastTimeSendTextTime, 6000000)) {
            if (this.luotNhanBuaMienPhi == 1) {
                ItemTimeService.gI().sendTextTime(this, TEXT_NHAN_BUA_MIEN_PHI,
                        "Nhận ngẫu nhiên bùa 1h mỗi ngày tại Bà Hạt Mít ở vách núi", 30);
            }
            lastTimeSendTextTime = System.currentTimeMillis();
        }
    }

    public void updateEff(Player player) {
        try {
            if (player.nPoint != null && player.inventory.itemsBody.size() >= 17) {
                for (int i = 12; i <= 16; i++) {
                    Item item = player.inventory.itemsBody.get(i);
                    if (item.isNotNullItem()) {
                        Service.gI().addEffectChar(player, (short) item.template.part, 1, -1, -1, 1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void upSkill(byte id, short cost) {

        int tempId = this.playerSkill.skills.get(id).template.id;
        int level = this.playerSkill.skills.get(id).point + 1;
        if (level > 7) {
            Service.gI().sendThongBao((Player) this, "Kĩ năng đã đạt cấp tối đa!");
        } else if (((Player) this).inventory.gem < cost) {
            Service.gI().sendThongBao((Player) this, "Bạn không đủ ngọc để nâng cấp!");
        } else {
            Skill skill = null;
            try {
                skill = SkillUtil.nClassTD.getSkillTemplate(tempId).skillss.get(level - 1);
            } catch (Exception e) {
                try {
                    skill = SkillUtil.nClassNM.getSkillTemplate(tempId).skillss.get(level - 1);
                } catch (Exception ex) {
                    skill = SkillUtil.nClassXD.getSkillTemplate(tempId).skillss.get(level - 1);
                }
            }
            skill = new Skill(skill);
            if (id == 1) {
                skill.coolDown = 1000;
            }
            this.playerSkill.skills.set(id, skill);
            ((Player) this).inventory.gem -= cost;
            Service.gI().sendMoney((Player) this);
            Service.gI().player((Player) this);

        }

    }

    // --------------------------------------------------------------------------
    /*
     * {380, 381, 382}: ht lưỡng long nhất thể xayda trái đất
     * {383, 384, 385}: ht porata xayda trái đất
     * {391, 392, 393}: ht namếc
     * {870, 871, 872}: ht c2 trái đất
     * {873, 874, 875}: ht c2 namếc
     * {867, 878, 869}: ht c2 xayda
     */
    private static final short[][] idOutfitFusion = {
            { 380, 381, 382 }, // luong long
            { 383, 384, 385 }, // porata
            { 391, 392, 393 }, // hop the chung namec
            { 870, 871, 872 }, // trai dat c2
            { 873, 874, 875 }, // namec c2
            { 867, 868, 869 }, // xayda c2
            { 870, 871, 872 }, // td 3
            { 1650, 1651, 1652 }, // nm 3
            { 867, 868, 869 }, // xd 3
            { 2048, 2049, 2050 }, // td 4
            { 2051, 2052, 2053 }, // nm 4
            { 2054, 2055, 2056 },// xd 4
    };
    // private static final short[][] idOutfitFusion = {
    //         { 380, 381, 382 }, // luong long
    //         { 383, 384, 385 }, // porata
    //         { 391, 392, 393 }, // hop the chung namec
    //         { 870, 871, 872 }, // trai dat c2
    //         { 873, 874, 875 }, // namec c2
    //         { 867, 868, 869 }, // xayda c2
    //         { 1755, 1756, 1757 }, // td 3
    //         { 1650, 1651, 1652 }, // nm 3
    //         { 1734, 1735, 1736 }, // xd 3
    //         { 2048, 2049, 2050 }, // td 4
    //         { 2051, 2052, 2053 }, // nm 4
    //         { 2054, 2055, 2056 },// xd 4
    // };

    public byte getEffFront() {
        if (this.inventory.itemsBody.isEmpty() || this.inventory.itemsBody.size() < 10) {
            return -1;
        }
        int levelAo = 0;
        Item.ItemOption optionLevelAo = null;
        int levelQuan = 0;
        Item.ItemOption optionLevelQuan = null;
        int levelGang = 0;
        Item.ItemOption optionLevelGang = null;
        int levelGiay = 0;
        Item.ItemOption optionLevelGiay = null;
        int levelNhan = 0;
        Item.ItemOption optionLevelNhan = null;
        Item itemAo = this.inventory.itemsBody.get(0);
        Item itemQuan = this.inventory.itemsBody.get(1);
        Item itemGang = this.inventory.itemsBody.get(2);
        Item itemGiay = this.inventory.itemsBody.get(3);
        Item itemNhan = this.inventory.itemsBody.get(4);
        for (Item.ItemOption io : itemAo.itemOptions) {
            if (io.optionTemplate.id == 72) {
                levelAo = io.param;
                optionLevelAo = io;
                break;
            }
        }
        for (Item.ItemOption io : itemQuan.itemOptions) {
            if (io.optionTemplate.id == 72) {
                levelQuan = io.param;
                optionLevelQuan = io;
                break;
            }
        }
        for (Item.ItemOption io : itemGang.itemOptions) {
            if (io.optionTemplate.id == 72) {
                levelGang = io.param;
                optionLevelGang = io;
                break;
            }
        }
        for (Item.ItemOption io : itemGiay.itemOptions) {
            if (io.optionTemplate.id == 72) {
                levelGiay = io.param;
                optionLevelGiay = io;
                break;
            }
        }
        for (Item.ItemOption io : itemNhan.itemOptions) {
            if (io.optionTemplate.id == 72) {
                levelNhan = io.param;
                optionLevelNhan = io;
                break;
            }
        }
        if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null && optionLevelGiay != null
                && optionLevelNhan != null
                && levelAo >= 8 && levelQuan >= 8 && levelGang >= 8 && levelGiay >= 8 && levelNhan >= 8) {
            return 8;
        } // else if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang
          // != null && optionLevelGiay != null && optionLevelNhan != null
          // && levelAo >= 7 && levelQuan >= 7 && levelGang >= 7 && levelGiay >= 7 &&
          // levelNhan >= 7) {
          // return 7;
          // } else if (optionLevelAo != null && optionLevelQuan != null &&
          // optionLevelGang != null && optionLevelGiay != null && optionLevelNhan != null
          // && levelAo >= 6 && levelQuan >= 6 && levelGang >= 6 && levelGiay >= 6 &&
          // levelNhan >= 6) {
          // return 6;
          // } else if (optionLevelAo != null && optionLevelQuan != null &&
          // optionLevelGang != null && optionLevelGiay != null && optionLevelNhan != null
          // && levelAo >= 5 && levelQuan >= 5 && levelGang >= 5 && levelGiay >= 5 &&
          // levelNhan >= 5) {
          // return 5;
          // } else if (optionLevelAo != null && optionLevelQuan != null &&
          // optionLevelGang != null && optionLevelGiay != null && optionLevelNhan != null
          // && levelAo >= 4 && levelQuan >= 4 && levelGang >= 4 && levelGiay >= 4 &&
          // levelNhan >= 4) {
          // return 4;
          // }
        else {
            return -1;
        }
    }

    public short getHead() {
        if (effectSkill != null && effectSkill.isMonkey) {
            return (short) ConstPlayer.HEADMONKEY[effectSkill.levelMonkey - 1];
        }
        if (effectSkill != null) {
            if (effectSkill.isTranformation) {
                switch (this.gender) {
                    case 0:
                        ItemTimeService.gI().sendItemTime(this, 20958, this.effectSkill.timeTranformation / 1000);
                        return 1806;
                    case 1:
                        ItemTimeService.gI().sendItemTime(this, 20964, this.effectSkill.timeTranformation / 1000);
                        return 1823;
                    case 2:
                        ItemTimeService.gI().sendItemTime(this, 20952, this.effectSkill.timeTranformation / 1000);
                        return 1843;
                    default:
                }
            }
        }
        if (effectSkill != null) {
            if (effectSkill.isEvolution) {
                switch (this.gender) {
                    case 0:
                        switch (this.isbienhinh) {
                            case 1:
                                ItemTimeService.gI().removeItemTime(this, 20958);
                                ItemTimeService.gI().sendItemTime(this, 20959,
                                        this.effectSkill.timeTranformation / 1000);
                                return 1806;
                            case 2:
                                ItemTimeService.gI().removeItemTime(this, 20959);
                                ItemTimeService.gI().sendItemTime(this, 20960,
                                        this.effectSkill.timeTranformation / 1000);
                                return 1809;
                            case 3:
                                ItemTimeService.gI().removeItemTime(this, 20960);
                                ItemTimeService.gI().sendItemTime(this, 20961,
                                        this.effectSkill.timeTranformation / 1000);
                                return 1812;
                            case 4:
                                ItemTimeService.gI().removeItemTime(this, 20961);
                                ItemTimeService.gI().sendItemTime(this, 20962,
                                        this.effectSkill.timeTranformation / 1000);
                                return 1815;
                            case 5:
                                ItemTimeService.gI().removeItemTime(this, 20962);
                                ItemTimeService.gI().sendItemTime(this, 20963,
                                        this.effectSkill.timeTranformation / 1000);
                                return 1818;
                            default:
                        }
                    case 1:
                        switch (this.isbienhinh) {
                            case 1:
                                ItemTimeService.gI().removeItemTime(this, 20964);
                                ItemTimeService.gI().sendItemTime(this, 20965,
                                        this.effectSkill.timeTranformation / 1000);
                                return 1823;
                            case 2:
                                ItemTimeService.gI().removeItemTime(this, 20965);
                                ItemTimeService.gI().sendItemTime(this, 20966,
                                        this.effectSkill.timeTranformation / 1000);
                                return 1826;
                            case 3:
                                ItemTimeService.gI().removeItemTime(this, 20966);
                                ItemTimeService.gI().sendItemTime(this, 20967,
                                        this.effectSkill.timeTranformation / 1000);
                                return 1829;
                            case 4:
                                ItemTimeService.gI().removeItemTime(this, 20967);
                                ItemTimeService.gI().sendItemTime(this, 20968,
                                        this.effectSkill.timeTranformation / 1000);
                                return 1832;
                            case 5:
                                ItemTimeService.gI().removeItemTime(this, 20968);
                                ItemTimeService.gI().sendItemTime(this, 20969,
                                        this.effectSkill.timeTranformation / 1000);
                                return 1835;

                            default:
                        }

                    case 2:
                        switch (this.isbienhinh) {
                            case 1:
                                ItemTimeService.gI().removeItemTime(this, 20952);
                                ItemTimeService.gI().sendItemTime(this, 20953,
                                        this.effectSkill.timeTranformation / 1000);
                                return 1843;
                            case 2:
                                ItemTimeService.gI().removeItemTime(this, 20953);
                                ItemTimeService.gI().sendItemTime(this, 20954,
                                        this.effectSkill.timeTranformation / 1000);
                                return 1846;
                            case 3:
                                ItemTimeService.gI().removeItemTime(this, 20954);
                                ItemTimeService.gI().sendItemTime(this, 20955,
                                        this.effectSkill.timeTranformation / 1000);
                                return 1849;
                            case 4:
                                ItemTimeService.gI().removeItemTime(this, 20955);
                                ItemTimeService.gI().sendItemTime(this, 20956,
                                        this.effectSkill.timeTranformation / 1000);
                                return 1852;
                            case 5:
                                ItemTimeService.gI().removeItemTime(this, 20956);
                                ItemTimeService.gI().sendItemTime(this, 20957,
                                        this.effectSkill.timeTranformation / 1000);
                                return 1855;
                            default:
                        }

                    default:
                }
            }
        }
        if (effectSkill != null && itemTime.isdkhi) {
            return (short) 1285;
        } else if (this.effectSkill.isBienHinh) {
            return 180;
        } else if (effectSkill != null && effectSkill.isSocola) {
            return 412;
        } else if (effectSkill != null && effectSkill.isMaPhongBa) {
            return 1988;
        } else if (fusion != null && fusion.typeFusion != ConstPlayer.NON_FUSION) {
            if (fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 0][0];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
                if (this.pet.typePet == 1) {
                    return idOutfitFusion[3 + this.gender][0];
                }
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 1][0];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
                if (this.pet.typePet == 1) {
                    return idOutfitFusion[3 + this.gender][0];
                }
                return idOutfitFusion[3 + this.gender][0];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
                if (this.pet.typePet == 1) {
                    return idOutfitFusion[6 + this.gender][0];
                }
                return idOutfitFusion[6 + this.gender][0];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
                if (this.pet.typePet == 1) {
                    return idOutfitFusion[9 + this.gender][0];
                }
                return idOutfitFusion[9 + this.gender][0];
            }
        } else if (inventory != null && inventory.itemsBody.get(5).isNotNullItem()) {
            int head = inventory.itemsBody.get(5).template.head;
            if (head != -1) {
                return (short) head;
            }
        }
        return this.head;
    }

    public short getBody() {
        if (effectSkill != null) {
            if (effectSkill.isTranformation || effectSkill.isEvolution) {
                switch (this.gender) {
                    case 0:
                        return 1821;
                    case 1:
                        return 1841;
                    case 2:
                        return 1858;
                    default:
                }
            }
        }
        if (effectSkill != null && effectSkill.isMonkey) {
            return 193;
        }
        if (effectSkill != null && itemTime.isdkhi) {
            return (short) 1286;
        } else if (effectSkill != null && effectSkill.isSocola) {
            return 413;
        } else if (this.effectSkill.isBienHinh) {
            return 181;
        } else if (effectSkill != null && effectSkill.isMaPhongBa) {
            return 1989;
            // } else if (effectSkill.isBang) {
            // return 1252;
            // } else if (effectSkill.isDa) {
            // return 455;
            // } else if (effectSkill.isCarot) {
            // return 407;
        } else if (fusion != null && fusion.typeFusion != ConstPlayer.NON_FUSION) {
            if (fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 0][1];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
                if (this.pet.typePet == 1) {
                    return idOutfitFusion[3 + this.gender][1];
                }
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 1][1];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
                if (this.pet.typePet == 1) {
                    return idOutfitFusion[3 + this.gender][1];
                }
                return idOutfitFusion[3 + this.gender][1];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
                if (this.pet.typePet == 1) {
                    return idOutfitFusion[6 + this.gender][1];
                }
                return idOutfitFusion[6 + this.gender][1];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
                if (this.pet.typePet == 1) {
                    return idOutfitFusion[9 + this.gender][1];
                }
                return idOutfitFusion[9 + this.gender][1];
            }
        } else if (inventory != null && inventory.itemsBody.get(5).isNotNullItem()) {
            int body = inventory.itemsBody.get(5).template.body;
            if (body != -1) {
                return (short) body;
            }
        }
        if (inventory != null && inventory.itemsBody.get(0).isNotNullItem()) {
            return inventory.itemsBody.get(0).template.part;
        }
        return (short) (gender == ConstPlayer.NAMEC ? 59 : 57);
    }

    public short getLeg() {
        if (effectSkill != null) {
            if (effectSkill.isTranformation || effectSkill.isEvolution) {
                switch (this.gender) {
                    case 0:
                        return 1822;
                    case 1:
                        return 1842;
                    case 2:
                        return 1859;
                    default:
                }
            }
        }
        if (effectSkill != null && effectSkill.isMonkey) {
            return 194;
        }
        if (effectSkill != null && itemTime.isdkhi) {
            return (short) 1287;
        } else if (this.effectSkill.isBienHinh) {
            return 182;
        } else if (effectSkill != null && effectSkill.isMaPhongBa) {
            return 1990;
        } else if (effectSkill != null && effectSkill.isSocola) {
            return 414;
            // } else if (effectSkill.isBang) {
            // return 1253;
            // } else if (effectSkill.isDa) {
            // return 456;
            // } else if (effectSkill.isCarot) {
            // return 408;
        } else if (fusion != null && fusion.typeFusion != ConstPlayer.NON_FUSION) {
            if (fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 0][2];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
                if (this.pet.typePet == 1) {
                    return idOutfitFusion[3 + this.gender][2];
                }
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 1][2];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
                if (this.pet.typePet == 1) {
                    return idOutfitFusion[3 + this.gender][2];
                }
                return idOutfitFusion[3 + this.gender][2];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
                if (this.pet.typePet == 1) {
                    return idOutfitFusion[6 + this.gender][2];
                }
                return idOutfitFusion[6 + this.gender][2];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
                if (this.pet.typePet == 1) {
                    return idOutfitFusion[9 + this.gender][2];
                }
                return idOutfitFusion[9 + this.gender][2];
            }
        } else if (inventory != null && inventory.itemsBody.get(5).isNotNullItem()) {
            int leg = inventory.itemsBody.get(5).template.leg;
            if (leg != -1) {
                return (short) leg;
            }
        }
        if (inventory != null && inventory.itemsBody.get(1).isNotNullItem()) {
            return inventory.itemsBody.get(1).template.part;
        }
        return (short) (gender == 1 ? 60 : 58);
    }

    public byte getAura() {
        if (this.inventory.itemsBody.isEmpty()
                || this.inventory.itemsBody.size() < 10) {
            return -1;
        }
        Item item = this.inventory.itemsBody.get(5); // Gốc là 8
        if (!item.isNotNullItem()) {
            return 0;
        }
        if (this.effectSkill != null && this.effectSkill.isTranformation) {
            return 12;
        }
        if (item.template.id == 1479) {
            return 39;
        }
        if (item.template.id == 1485) {
            return 40;
        }
        if (item.template.id == 1477) {
            return 34;
        }
        if (item.template.id == 1459) {
            return 38;
        }
        if (item.template.id == 1065) {
            return 6;
        }
        if (item.template.id == 1064) {
            return 68;
        }
        if (item.template.id == 1340) {
            return 20;
        }
        if (item.template.id == 1336) {
            return 36;
        }
        if (item.template.id == 2075) {
            return 55;
        }
        if (item.template.id == 2074) {
            return 22;
        } else {
            return -1;
        }

    }

    public short getFlagBag() {
        if (this.iDMark.isHoldBlackBall()) {
            return 31;
        } else if (this.idNRNM >= 353 && this.idNRNM <= 359) {
            return 30;
        }
        if (this.inventory.itemsBody.size() >= 11) {
            if (this.inventory.itemsBody.get(8).isNotNullItem()) {
                return this.inventory.itemsBody.get(8).template.part;
            }
        }
        if (TaskService.gI().getIdTask(this) == ConstTask.TASK_3_2) {
            return 28;
        }
        if (this.clan != null) {
            return (short) this.clan.imgId;
        }
        return -1;
    }

    public short getMount() {
        if (this.inventory.itemsBody.isEmpty() || this.inventory.itemsBody.size() < 10) {
            return -1;
        }
        Item item = this.inventory.itemsBody.get(9);
        if (!item.isNotNullItem()) {
            return -1;
        }
        if (item.template.type == 24) {
            if (item.template.gender == 3 || item.template.gender == this.gender) {
                return item.template.id;
            } else {
                return -1;
            }
        } else {
            if (item.template.id < 500) {
                return item.template.id;
            } else {
                return (short) DataGame.MAP_MOUNT_NUM.get(String.valueOf(item.template.id));
            }
        }

    }

    public String NameThanthu(int CapBac) {
        switch (CapBac) {
            case 10:
                return "Đệ Tử 2";
            case 9:
                return "Đệ Tử 2";
            case 8:
                return "Đệ Tử 2";
            case 7:
                return "Đệ Tử 2";
            case 6:
                return "Đệ Tử 2";
            case 5:
                return "Đệ Tử 2";
            case 4:
                return "Đệ Tử 2";
            case 3:
                return "Đệ Tử 2";
            case 2:
                return "Đệ Tử 2";
            case 1:
                return "Đệ Tử 2";
            case 0:
                return "Đệ Tử 2";
            default:
                return "Đệ Tử 2";
        }
    }

    public String DaDotpha(int CapBac) {
        switch (CapBac) {
            case 9:
                return "Đế Vương Thạch";
            case 8:
                return "Hỏa Hồn Thạch";
            case 7:
                return "Thiên Mệnh Thạch";
            case 6:
                return "Huyết Tinh Thạch";
            case 5:
                return "Linh Vân Thạch";
            case 4:
                return "Mịch Lâm Thạch";
            default:
                return "Thiên Nguyệt thạch";
        }
    }

    public String TrieuHoiKiNang(int CapBac) {
        switch (CapBac) {
            case 10:
                return "Tìm " + ((TrieuHoiLevel + 1) * 1) + " Hồng ngọc cho Chủ nhân\n"
                        + "Tăng " + (TrieuHoiLevel + 1) + "% HP, KI, Giáp, SD, SD chí mạng cho Chủ nhân\n";
            case 9:
                return "Tăng " + ((TrieuHoiLevel + 1) / 2) + "% HP, KI, Giáp, SD, SD chí mạng cho Chủ nhân";
            case 8:
                return "Tăng " + ((TrieuHoiLevel + 1) / 5) + "% HP, KI, Giáp, SD cho Chủ nhân";
            case 7:
                return "Tăng " + ((TrieuHoiLevel + 1) / 6) + "% HP, KI, Giáp, SD, SD chí mạng cho Chủ nhân";
            case 6:
                return "Tăng " + ((TrieuHoiLevel + 1) / 7) + "% HP, KI, Giáp, SD, SD chí mạng cho Chủ nhân";
            case 5:
                return "Tăng " + ((TrieuHoiLevel + 1) / 7) + "% HP, KI, Giáp, SD, SD chí mạng cho Chủ nhân";
            case 4:
                return "Tăng " + ((TrieuHoiLevel + 1) * 30) + " HP, KI, SD, Giáp cho Chủ nhân";
            case 3:
                return "Tăng " + ((TrieuHoiLevel + 1) * 20) + " HP, KI\n" + ((TrieuHoiLevel + 1) * 10)
                        + " SD cho Chủ nhân";
            case 2:
                return "Tăng " + ((TrieuHoiLevel + 1) * 10) + " Sức đánh cho Chủ nhân";
            case 1:
                return "Tăng " + ((TrieuHoiLevel + 1) * 20) + " KI cho Chủ nhân";
            case 0:
                return "Tăng " + ((TrieuHoiLevel + 1) * 20) + " HP cho Chủ nhân";
            default:
                return "Phế vật mà làm được gì !!!";
        }
    }

    // --------------------------------------------------------------------------
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (plAtt != null) {
                switch (plAtt.playerSkill.skillSelect.template.id) {
                    case Skill.KAMEJOKO:
                    case Skill.MASENKO:
                    case Skill.ANTOMIC:
                        if (this.nPoint.voHieuChuong > 0) {
                            com.KhanhDTK.services.PlayerService.gI().hoiPhuc(this, 0,
                                    damage * this.nPoint.voHieuChuong / 100);
                            return 0;
                        }
                }
            }
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 100)) {
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1;
            }
            if (isMobAttack && this.charms.tdBatTu > System.currentTimeMillis() && damage >= this.nPoint.hp) {
                damage = this.nPoint.hp - 1;
            }

            this.nPoint.subHP(damage);
            if (isDie()) {
                if (this != null && this.zone != null && this.zone.map != null) {
                    if (this.zone.map.mapId == 112 && plAtt != null) {
                        plAtt.pointPvp++;
                    }
                }
                setDie(plAtt);
            }
            return damage;
        } else {
            if (this.isClone) {
                ChangeMapService.gI().exitMap(this);

            }
            return 0;
        }
    }

    public void setDie(Player plAtt) {
        // xóa phù
        if (this.effectSkin.xHPKI > 1) {
            this.effectSkin.xHPKI = 1;
            Service.gI().point(this);
        }
        // xóa tụ skill đặc biệt
        this.playerSkill.prepareQCKK = false;
        this.playerSkill.prepareLaze = false;
        this.playerSkill.prepareTuSat = false;
        // xóa hiệu ứng skill
        this.effectSkill.removeSkillEffectWhenDie();
        //
        nPoint.setHp(0);
        nPoint.setMp(0);
        // xóa trứng
        if (this.mobMe != null) {
            this.mobMe.mobMeDie();
        }
        Service.gI().charDie(this);
        // add kẻ thù
        if (!this.isPet && !this.isNewPet && !this.isBoss && plAtt != null && !plAtt.isPet && !plAtt.isNewPet
                && !plAtt.isBot && !plAtt.isBoss) {
            if (!plAtt.itemTime.isUseAnDanh) {
                FriendAndEnemyService.gI().addEnemy(this, plAtt);
            }
        }
        // kết thúc pk
        if (this.pvp != null) {
            this.pvp.lose(this, TYPE_LOSE_PVP.DEAD);
        }
        // PVPServcice.gI().finishPVP(this, PVP.TYPE_DIE);
        BlackBallWar.gI().dropBlackBall(this);
    }

    // --------------------------------------------------------------------------
    public void setClanMember() {
        if (this.clanMember != null) {
            this.clanMember.powerPoint = this.nPoint.power;
            this.clanMember.head = this.getHead();
            this.clanMember.body = this.getBody();
            this.clanMember.leg = this.getLeg();
        }
    }

    public boolean isAdmin() {
        return this.session.isAdmin;
    }

    public void congExpOff() {
        long exp = this.nPoint.getexp() * this.timeoff;
        Service.gI().addSMTN(this, (byte) 2, exp, false);
        NpcService.gI().createTutorial(this, 536,
                "Bạn tăng được " + exp + " sức mạnh trong thời gian " + this.timeoff + " phút tập luyện Offline");

    }

    public void setJustRevivaled() {
        if (this.isTrieuhoipet) {
            this.justRevived1 = true;
            this.lastTimeRevived1 = System.currentTimeMillis();
        }
        this.justRevived = true;
        this.lastTimeRevived = System.currentTimeMillis();
    }

    public void preparedToDispose() {

    }

    public String BktTuviTutien(int lvtt) {
        switch (lvtt) {
            case 0:
                return "Ngưng khí tầng 1";
            case 1:
                return "Ngưng khí tầng 2";
            case 2:
                return "Ngưng khí tầng 3";
            case 3:
                return "Ngưng khí tầng 4";
            case 4:
                return "Ngưng khí tầng 5";
            case 5:
                return "Ngưng khí tầng 6";
            case 6:
                return "Ngưng khí tầng 7";
            case 7:
                return "Ngưng khí tầng 8";
            case 8:
                return "Ngưng khí tầng 9";
            case 9:
                return "Ngưng khí đỉnh phong";
            case 10:
                return "Trúc cơ tầng 1";
            case 11:
                return "Trúc cơ tầng 2";
            case 12:
                return "Trúc cơ tầng 3";
            case 13:
                return "Trúc cơ tầng 4";
            case 14:
                return "Trúc cơ tầng 5";
            case 15:
                return "Trúc cơ tầng 6";
            case 16:
                return "Trúc cơ tầng 7";
            case 17:
                return "Trúc cơ tầng 8";
            case 18:
                return "Trúc cơ tầng 9";
            case 19:
                return "Trúc cơ đỉnh phong";
            case 20:
                return "Kết đan tầng 1";
            case 21:
                return "Kết đan tầng 2";
            case 22:
                return "Kết đan tầng 3";
            case 23:
                return "Kết đan tầng 4";
            case 24:
                return "Kết đan tầng 5";
            case 25:
                return "Kết đan tầng 6";
            case 26:
                return "Kết đan tầng 7";
            case 27:
                return "Kết đan tầng 8";
            case 28:
                return "Kết đan tầng 9";
            case 29:
                return "Kết đan đỉnh phong";
            case 30:
                return "Nguyên Anh tầng 1";
            case 31:
                return "Nguyên Anh tầng 2";
            case 32:
                return "Nguyên Anh tầng 3";
            case 33:
                return "Nguyên Anh tầng 4";
            case 34:
                return "Nguyên Anh tầng 5";
            case 35:
                return "Nguyên Anh tầng 6";
            case 36:
                return "Nguyên Anh tầng 7";
            case 37:
                return "Nguyên Anh tầng 8";
            case 38:
                return "Nguyên Anh tầng 9";
            case 39:
                return "Nguyên Anh đỉnh phong";
            case 40:
                return "Thiên Nhân tầng 1";
            case 41:
                return "Thiên Nhân tầng 2";
            case 42:
                return "Thiên Nhân tầng 3";
            case 43:
                return "Thiên Nhân tầng 4";
            case 44:
                return "Thiên Nhân tầng 5";
            case 45:
                return "Thiên Nhân tầng 6";
            case 46:
                return "Thiên Nhân tầng 7";
            case 47:
                return "Thiên Nhân tầng 8";
            case 48:
                return "Thiên Nhân tầng 9";
            case 49:
                return "Thiên Nhân đỉnh phong";
            case 50:
                return "bán thần tầng 1";
            case 51:
                return "bán thần tầng 2";
            case 52:
                return "bán thần tầng 3";
            case 53:
                return "bán thần tầng 4";
            case 54:
                return "bán thần tầng 5";
            case 55:
                return "bán thần tầng 6";
            case 56:
                return "bán thần tầng 7";
            case 57:
                return "bán thần tầng 8";
            case 58:
                return "bán thần tầng 9";
            case 59:
                return "bán thần đỉnh phong";
            case 60:
                return "Thiên tôn tầng 1";
            case 61:
                return "Thiên tôn tầng 2";
            case 62:
                return "Thiên tôn tầng 3";
            case 63:
                return "Thiên tôn tầng 4";
            case 64:
                return "Thiên tôn tầng 5";
            case 65:
                return "Thiên tôn tầng 6";
            case 66:
                return "Thiên tôn tầng 7";
            case 67:
                return "Thiên tôn tầng 8";
            case 68:
                return "Thiên tôn tầng 9";
            case 69:
                return "Thiên tôn đỉnh phong";
            case 70:
                return "Thái tổ tầng 1";
            case 71:
                return "Thái tổ tầng 2";
            case 72:
                return "Thái tổ tầng 3";
            case 73:
                return "Thái tổ tầng 4";
            case 74:
                return "Thái tổ tầng 5";
            case 75:
                return "Thái tổ tầng 6";
            case 76:
                return "Thái tổ tầng 7";
            case 77:
                return "Thái tổ tầng 8";
            case 78:
                return "Thái tổ tầng 9";
            case 79:
                return "Thái tổ đỉnh phong";
            case 80:
                return "Chúa tể tầng 1";
            case 81:
                return "Chúa tể tầng 2";
            case 82:
                return "Chúa tể tầng 3";
            case 83:
                return "Chúa tể tầng 4";
            case 84:
                return "Chúa tể tầng 5";
            case 85:
                return "Chúa tể tầng 6";
            case 86:
                return "Chúa tể tầng 7";
            case 87:
                return "Chúa tể tầng 8";
            case 88:
                return "Chúa tể tầng 9";
            case 89:
                return "Chúa tể Đỉnh cao";
            case 90:
                return "Vĩnh hằng sơ kì";
            case 91:
                return "Vĩnh hằng trung kì";
            case 93:
                return "Vĩnh hằng Hậu kì";
            case 94:
                return "Vĩnh hằng đỉnh phong";
            case 95:
                return "Vĩnh hằng đỉnh cao";
            case 96:
                return "Vĩnh hằng hoàn mỹ";
            default:
                return "Phế vật";
        }
    }

    public float Bkttilecanhgioi(int lvtt) {
        switch (lvtt) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                return 70f;
            case 9:
                return 69f;
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
                return 68f;
            case 19:
                return 67f;
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
                return 65f;
            case 29:
                return 60f;
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
                return 69f;
            case 39:
                return 65f;
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
                return 64f;
            case 49:
                return 62f;
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
                return 60f;
            case 59:
                return 58f;
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 68:
                return 55f;
            case 69:
                return 53f;
            case 70:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
            case 78:
                return 50f;
            case 79:
                return 49f;
            case 80:
            case 81:
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
                return 48f;
            case 89:
                return 47f;
            case 90:
                return 46f;
            case 91:
                return 43f;
            case 93:
                return 40f;
            case 94:
                return 39f;
            case 95:
                return 37f;
            case 96:
                return 35f;
            default:
                return 0.5f;
        }
    }

    public int BktDametutien(int lvtt) {
        switch (lvtt) {
            case 0:
                return 5;
            case 1:
                return 35;
            case 2:
                return 39;
            case 3:
                return 43;
            case 4:
                return 47;
            case 5:
                return 50;
            case 6:
                return 54;
            case 7:
                return 58;
            case 8:
                return 63;
            case 9:
                return 66;
            case 10:
                return 70;
            case 11:
                return 75;
            case 12:
                return 79;
            case 13:
                return 83;
            case 14:
                return 87;
            case 15:
                return 90;
            case 16:
                return 100;
            case 17:
                return 110;
            case 18:
                return 125;
            case 19:
                return 136;
            case 20:
                return 155;
            case 21:
                return 170;
            case 22:
                return 189;
            case 23:
                return 200;
            case 24:
                return 230;
            case 25:
                return 245;
            case 26:
                return 260;
            case 27:
                return 276;
            case 28:
                return 289;
            case 29:
                return 300;
            case 30:
                return 316;
            case 31:
                return 323;
            case 32:
                return 333;
            case 33:
                return 346;
            case 34:
                return 376;
            case 35:
                return 389;
            case 36:
                return 405;
            case 37:
                return 420;
            case 38:
                return 438;
            case 39:
                return 451;
            case 40:
                return 478;
            case 41:
                return 498;
            case 42:
                return 519;
            case 43:
                return 530;
            case 44:
                return 549;
            case 45:
                return 568;
            case 46:
                return 589;
            case 47:
                return 607;
            case 48:
                return 624;
            case 49:
                return 644;
            case 50:
                return 667;
            case 51:
                return 683;
            case 52:
                return 700;
            case 53:
                return 737;
            case 54:
                return 754;
            case 55:
                return 765;
            case 56:
                return 789;
            case 57:
                return 813;
            case 58:
                return 834;
            case 59:
                return 850;
            case 60:
                return 867;
            case 61:
                return 884;
            case 62:
                return 900;
            case 63:
                return 934;
            case 64:
                return 956;
            case 65:
                return 978;
            case 66:
                return 999;
            case 67:
                return 1000;
            case 68:
                return 1023;
            case 69:
                return 1050;
            case 70:
                return 1078;
            case 71:
                return 1100;
            case 72:
                return 1111;
            case 73:
                return 1133;
            case 74:
                return 1150;
            case 75:
                return 1178;
            case 76:
                return 1199;
            case 77:
                return 1227;
            case 78:
                return 1250;
            case 79:
                return 1276;
            case 80:
                return 1289;
            case 81:
                return 1313;
            case 82:
                return 1333;
            case 83:
                return 1356;
            case 84:
                return 1389;
            case 85:
                return 1424;
            case 86:
                return 1458;
            case 87:
                return 1500;
            case 88:
                return 1600;
            case 89:
                return 1900;
            case 90:
                return 2055;
            case 91:
                return 3500;
            case 93:
                return 4444;
            case 94:
                return 6666;
            case 95:
                return 7777;
            case 96:
                return 9999;
            default:
                return 0;
        }
    }

    public int BktHpKiGiaptutien(int lvtt) {
        switch (lvtt) {
            case 0:
                return 10;
            case 1:
                return 30;
            case 2:
                return 50;
            case 3:
                return 70;
            case 4:
                return 90;
            case 5:
                return 110;
            case 6:
                return 130;
            case 7:
                return 150;
            case 8:
                return 170;
            case 9:
                return 200;
            case 10:
                return 230;
            case 11:
                return 250;
            case 12:
                return 280;
            case 13:
                return 310;
            case 14:
                return 330;
            case 15:
                return 360;
            case 16:
                return 390;
            case 17:
                return 440;
            case 18:
                return 490;
            case 19:
                return 500;
            case 20:
                return 520;
            case 21:
                return 550;
            case 22:
                return 570;
            case 23:
                return 600;
            case 24:
                return 630;
            case 25:
                return 670;
            case 26:
                return 700;
            case 27:
                return 750;
            case 28:
                return 800;
            case 29:
                return 850;
            case 30:
                return 900;
            case 31:
                return 923;
            case 32:
                return 954;
            case 33:
                return 978;
            case 34:
                return 1003;
            case 35:
                return 1025;
            case 36:
                return 1056;
            case 37:
                return 1070;
            case 38:
                return 1111;
            case 39:
                return 1200;
            case 40:
                return 1222;
            case 41:
                return 1255;
            case 42:
                return 1277;
            case 43:
                return 1299;
            case 44:
                return 1323;
            case 45:
                return 1356;
            case 46:
                return 1378;
            case 47:
                return 1399;
            case 48:
                return 1456;
            case 49:
                return 1500;
            case 50:
                return 1555;
            case 51:
                return 1589;
            case 52:
                return 1678;
            case 53:
                return 1700;
            case 54:
                return 1734;
            case 55:
                return 1768;
            case 56:
                return 1789;
            case 57:
                return 1840;
            case 58:
                return 1888;
            case 59:
                return 1999;
            case 60:
                return 2023;
            case 61:
                return 2045;
            case 62:
                return 2067;
            case 63:
                return 2100;
            case 64:
                return 2112;
            case 65:
                return 2132;
            case 66:
                return 2153;
            case 67:
                return 2178;
            case 68:
                return 2199;
            case 69:
                return 2222;
            case 70:
                return 2266;
            case 71:
                return 2299;
            case 72:
                return 2333;
            case 73:
                return 2367;
            case 74:
                return 2398;
            case 75:
                return 2432;
            case 76:
                return 2454;
            case 77:
                return 2468;
            case 78:
                return 2487;
            case 79:
                return 2500;
            case 80:
                return 2534;
            case 81:
                return 2563;
            case 82:
                return 2578;
            case 83:
                return 2611;
            case 84:
                return 2654;
            case 85:
                return 2689;
            case 86:
                return 2727;
            case 87:
                return 2800;
            case 88:
                return 3100;
            case 89:
                return 4444;
            case 90:
                return 6666;
            case 91:
                return 7890;
            case 93:
                return 8765;
            case 94:
                return 9999;
            case 95:
                return 12000;
            case 96:
                return 15555;
            default:
                return 0;
        }
    }

    public int BktDieukiencanhgioi(int lvtt) {
        switch (lvtt) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                return 5000000;
            case 9:
                return 7000000;
            case 10:
                return 7300000;
            case 11:
            case 12:
            case 13:
            case 14:
                return 7700000;
            case 15:
            case 16:
            case 17:
            case 18:
                return 9000000;
            case 19:
                return 12000000;
            case 20:
                return 13000000;
            case 21:
            case 22:
            case 23:
            case 24:
                return 15000000;
            case 25:
            case 26:
            case 27:
            case 28:
                return 17000000;
            case 29:
                return 20000000;
            case 30:
                return 21000000;
            case 31:
            case 32:
            case 33:
            case 34:
                return 23000000;
            case 35:
            case 36:
            case 37:
            case 38:
                return 25000000;
            case 39:
                return 27000000;
            case 40:
                return 30000000;
            case 41:
            case 42:
            case 43:
            case 44:
                return 31000000;
            case 45:
            case 46:
            case 47:
            case 48:
                return 32000000;
            case 49:
                return 35000000;
            case 50:
                return 37000000;
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
                return 40000000;
            case 59:
                return 42000000;
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 68:
                return 45000000;
            case 69:
                return 47000000;
            case 70:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
            case 78:
                return 50000000;
            case 79:
                return 53000000;
            case 80:
            case 81:
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
                return 55000000;
            case 89:
                return 58000000;
            case 90:
                return 60000000;
            case 91:
                return 70000000;
            case 93:
                return 80000000;
            case 94:
                return 90000000;
            case 95:
                return 120000000;
            case 96:
                return 150000000;
            default:
                return Integer.MAX_VALUE;
        }
    }

    public String BktNameTuMa() {
        switch (this.Bkt_Tu_Ma) {
            case 0:
                return "Tiểu ma nhân";
            case 1:
                return "Ma nhân";
            case 2:
                return "Bán Ma";
            case 3:
                return "Hóa ma";
            case 4:
                return "Thiên ma";
            case 5:
                return "Địa ma";
            case 6:
                return "Huyền ma";
            case 7:
                return "Bán ma hoàng";
            case 8:
                return "Ma hoàng sơ kì";
            case 9:
                return "Ma hoàng trung kì";
            case 10:
                return "Ma hoàng hậu kì";
            case 11:
                return "Ma hoàng đỉnh phong";
            case 12:
                return "Ma Thần sơ kì";
            case 13:
                return "Ma Thần trung kì";
            case 14:
                return "Ma Thần hậu kì";
            case 15:
                return "Ma Thần \u0111\u1EC9nh phong";
            case 16:
                return "Thiên Ma Thần";
            default:
                return "Súc vật";
        }
    }

    public double BktDameTuMa() {
        switch (this.Bkt_Tu_Ma) {
            case 0:
                return 50;
            case 1:
                return 250;
            case 2:
                return 680;
            case 3:
                return 2001;
            case 4:
                return 2700;
            case 5:
                return 3500;
            case 6:
                return 4999;
            case 7:
                return 5892;
            case 8:
                return 7000;
            case 9:
                return 7800;
            case 10:
                return 8900;
            case 11:
                return 9999;
            case 12:
                return 11111;
            case 13:
                return 12555;
            case 14:
                return 13888;
            case 15:
                return 15555;
            case 16:
                return 33333;
            default:
                return -33333;
        }
    }

    public double BktGiapTuMa() {
        switch (this.Bkt_Tu_Ma) {
            case 0:
                return 25;
            case 1:
                return 50;
            case 2:
                return 80;
            case 3:
                return 201;
            case 4:
                return 270;
            case 5:
                return 350;
            case 6:
                return 499;
            case 7:
                return 582;
            case 8:
                return 700;
            case 9:
                return 780;
            case 10:
                return 890;
            case 11:
                return 999;
            case 12:
                return 1111;
            case 13:
                return 1255;
            case 14:
                return 1388;
            case 15:
                return 1555;
            case 16:
                return 3333;
            default:
                return -3333;
        }
    }

    public int BktTimeTuMa() {
        switch (this.Bkt_Tu_Ma) {
            case 0:
                return 5;
            case 1:
                return 12;
            case 2:
                return 17;
            case 3:
                return 25;
            case 4:
                return 33;
            case 5:
                return 38;
            case 6:
                return 43;
            case 7:
                return 50;
            case 8:
                return 55;
            case 9:
                return 59;
            case 10:
                return 65;
            case 11:
                return 69;
            case 12:
                return 76;
            case 13:
                return 83;
            case 14:
                return 89;
            case 15:
                return 95;
            case 16:
                return 100;
            default:
                return -100;
        }
    }

    public String BktNameHoncot(int Honcot) {
        switch (Honcot - 1) {
            case 0:
                return "-B\u00E1t Chu M\u00E2u ";
            case 1:
                return "-Tinh Th\u1EA7n Ng\u01B0ng T\u1EE5 Chi Tr\u00ED Tu\u1EC7 \u0110\u1EA7u C\u1ED1t";
            case 2:
                return "-Nhu C\u1ED1t Th\u1ECF H\u1EEFu T\u00ED C\u1ED1t";
            case 3:
                return "-Th\u00E1i Th\u1EA3n C\u1EF1 Vi\u00EAn";
            case 4:
                return "-Lam Ng\u00E2n Ho\u00E0ng";
            case 5:
                return "-T\u00E0 Ma H\u1ED5 K\u00ECnh";
            default:
                return "-H\u1ED3n x\u00E1c l\u00ECa \u0111\u1EDDi";
        }
    }

    public void dispose() {
        if (pet != null) {
            pet.dispose();
            pet = null;
        }
        if (clone != null && !this.isClone) {
            clone.dispose();
            clone = null;
        }
        if (newpet != null) {
            newpet.dispose();
            newpet = null;
        }
        if (mapBlackBall != null) {
            mapBlackBall.clear();
            mapBlackBall = null;
        }
        zone = null;
        mapBeforeCapsule = null;
        mapBeforeCapsule_2 = null;
        if (mapMaBu != null) {
            mapMaBu.clear();
            mapMaBu = null;
        }
        if (billEgg != null) {
            billEgg.dispose();
            billEgg = null;
        }
        zone = null;
        mapBeforeCapsule = null;
        mapBeforeCapsule_2 = null;
        if (mapCapsule != null) {
            mapCapsule.clear();
            mapCapsule = null;
        }
        if (mobMe != null) {
            mobMe.dispose();
            mobMe = null;
        }
        location = null;
        if (setClothes != null) {
            setClothes.dispose();
            setClothes = null;
        }
        if (effectSkill != null) {
            effectSkill.dispose();
            effectSkill = null;
        }
        if (mabuEgg != null) {
            mabuEgg.dispose();
            mabuEgg = null;
        }
        if (skillSpecial != null) {
            skillSpecial.dispose();
            skillSpecial = null;
        }
        if (playerTask != null) {
            playerTask.dispose();
            playerTask = null;
        }
        if (itemTime != null) {
            itemTime.dispose();
            itemTime = null;
        }
        if (fusion != null) {
            fusion.dispose();
            fusion = null;
        }
        if (magicTree != null) {
            magicTree.dispose();
            magicTree = null;
        }
        if (playerIntrinsic != null) {
            playerIntrinsic.dispose();
            playerIntrinsic = null;
        }
        if (inventory != null) {
            inventory.dispose();
            inventory = null;
        }
        if (playerSkill != null) {
            playerSkill.dispose();
            playerSkill = null;
        }
        if (combineNew != null) {
            combineNew.dispose();
            combineNew = null;
        }
        if (iDMark != null) {
            iDMark.dispose();
            iDMark = null;
        }
        if (charms != null) {
            charms.dispose();
            charms = null;
        }
        if (effectSkin != null) {
            effectSkin.dispose();
            effectSkin = null;
        }
        if (nPoint != null) {
            nPoint.dispose();
            nPoint = null;
        }
        if (rewardBlackBall != null) {
            rewardBlackBall.dispose();
            rewardBlackBall = null;
        }
        if (effectFlagBag != null) {
            effectFlagBag.dispose();
            effectFlagBag = null;
        }
        if (pvp != null) {
            pvp.dispose();
            pvp = null;
        }
        effectFlagBag = null;
        clan = null;
        clanMember = null;
        friends = null;
        enemies = null;
        session = null;
        name = null;
    }

    public void setfight(byte typeFight, byte typeTatget) {

        try {
            if (typeFight == (byte) 0 && typeTatget == (byte) 0) {
                this.istry = true;
            }
            if (typeFight == (byte) 0 && typeTatget == (byte) 1) {
                this.istry1 = true;
            }
            if (typeFight == (byte) 1 && typeTatget == (byte) 0) {
                this.isfight = true;
            }
            if (typeFight == (byte) 1 && typeTatget == (byte) 1) {
                this.isfight1 = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean IsActiveMaster() {

        if (this.istry || this.isfight) {
            this.istry = true;
        }

        return false;
    }

    public void rsfight() {
        if (this.istry) {
            this.istry = false;
        }
        if (this.istry1) {
            this.istry1 = false;
        }
        if (this.isfight) {
            this.isfight = false;
        }
        if (this.isfight1) {
            this.isfight1 = false;
        }
    }

    public boolean IsTry0() {
        if (this.istry && this.isfight) {
            return true;
        }
        return false;
    }

    public boolean IsTry1() {
        if (this.istry && this.isfight1) {
            return true;
        }
        return false;
    }

    public boolean IsFigh0() {
        if (this.istry && this.isfight1) {
            return true;
        }
        return false;
    }

    public String percentGold(int type) {
        try {
            if (type == 0) {
                double percent = ((double) this.goldNormar / ChonAiDay.gI().goldNormar) * 100;
                return String.valueOf(Math.ceil(percent));
            } else if (type == 1) {
                double percent = ((double) this.goldVIP / ChonAiDay.gI().goldVip) * 100;
                return String.valueOf(Math.ceil(percent));
            }
        } catch (ArithmeticException e) {

            return "0";
        }
        return "0";
    }

    public Mob mobTarget;

    public long lastTimeTargetMob;

    public long timeTargetMob;

    public long lastTimeAttack;

    public void moveTo(int x, int y) {
        byte dir = (byte) (this.location.x - x < 0 ? 1 : -1);
        byte move = (byte) Util.nextInt(40, 60);
        if (isBot) {
            move = (byte) (move * (byte) 2);
        }
        PlayerService.gI().playerMove(this, this.location.x + (dir == 1 ? move : -move),
                y + (Util.isTrue(3, 10) ? -50 : 0));
    }

    public Mob getMobAttack() {
        if (this.mobTarget != null && (this.mobTarget.isDie() || !this.zone.equals(this.mobTarget.zone))) {
            this.mobTarget = null;
        }
        if (this.mobTarget == null && Util.canDoWithTime(lastTimeTargetMob, timeTargetMob)) {
            this.mobTarget = this.zone.getRandomMobInMap();
            this.lastTimeTargetMob = System.currentTimeMillis();
            this.timeTargetMob = 500;
        }
        return this.mobTarget;
    }

    public int getRangeCanAttackWithSkillSelect() {
        int skillId = this.playerSkill.skillSelect.template.id;
        if (skillId == Skill.KAMEJOKO || skillId == Skill.MASENKO || skillId == Skill.ANTOMIC) {
            return Skill.RANGE_ATTACK_CHIEU_CHUONG;
        } else if (skillId == Skill.DRAGON || skillId == Skill.DEMON || skillId == Skill.GALICK) {
            return Skill.RANGE_ATTACK_CHIEU_DAM;
        }
        return 752002;
    }

    public void active() {
        if (this.isBot) {
            if (this.isDie()) {
                this.nPoint.hp = this.nPoint.hpMax;
            }
            if (this.nPoint.mp <= 0) {
                this.nPoint.mp = this.nPoint.mpMax;
            }
            this.attack();
        }
    }

    public void attack() {
        if (this.isBot) {
            // this.mobTarget = this.getMobAttack();
            if (Util.canDoWithTime(lastTimeAttack, 100) && this.mobTarget != null) {

                this.lastTimeAttack = System.currentTimeMillis();
                try {
                    Mob m = this.getMobAttack();
                    if (m == null || m.isDie()) {
                        return;
                    }

                    this.playerSkill.skillSelect = this.playerSkill.skills
                            .get(Util.nextInt(0, this.playerSkill.skills.size() - 1));
                    // System.out.println(m.name);
                    if (Util.nextInt(100) < 70) {
                        this.playerSkill.skillSelect = this.playerSkill.skills.get(0);
                    }
                    if (Util.getDistance(this, m) <= this.getRangeCanAttackWithSkillSelect()) {
                        if (Util.isTrue(5, 20)) {
                            if (SkillUtil.isUseSkillChuong(this)) {
                                this.moveTo(m.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 200)),
                                        Util.nextInt(10) % 2 == 0 ? m.location.y : m.location.y);
                            } else {
                                this.moveTo(m.location.x + (Util.getOne(-1, 1) * Util.nextInt(10, 40)),
                                        Util.nextInt(10) % 2 == 0 ? m.location.y : m.location.y);
                            }
                        }
                        SkillService.gI().useSkill(this, null, m, null);
                    } else {
                        this.moveTo(m.location.x, m.location.y);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                this.mobTarget = getMobAttack();
            }
        }
    }

    public List<String> textRuongGo = new ArrayList<>();

}

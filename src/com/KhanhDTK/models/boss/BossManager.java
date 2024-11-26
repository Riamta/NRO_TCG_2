package com.KhanhDTK.models.boss;
import com.KhanhDTK.utils.Logger;
import com.KhanhDTK.jdbc.daos.GodGK;
import com.KhanhDTK.models.boss.list_boss.LuyenTap;
import com.KhanhDTK.models.boss.list_boss.AnTrom;
import com.KhanhDTK.models.boss.list_boss.BLACK.*;
import com.KhanhDTK.models.boss.list_boss.Cooler.Cooler;
//import com.KhanhDTK.models.boss.list_boss.HuyDiet.Champa;
//import com.KhanhDTK.models.boss.list_boss.HuyDiet.ThanHuyDiet;
//import com.KhanhDTK.models.boss.list_boss.HuyDiet.ThienSuWhis;
//import com.KhanhDTK.models.boss.list_boss.HuyDiet.Vados;
import com.KhanhDTK.models.boss.list_boss.NgucTu.CoolerGold;
import com.KhanhDTK.models.boss.list_boss.Doraemon.Doraemon;
import com.KhanhDTK.models.boss.list_boss.FideBack.Kingcold;
import com.KhanhDTK.models.boss.list_boss.Mabu;
//import com.KhanhDTK.models.boss.list_boss.SuperXen;
//import com.KhanhDTK.models.boss.list_boss.NgucTu.Cumber;
import com.KhanhDTK.models.boss.list_boss.cell.Xencon;
import com.KhanhDTK.models.boss.list_boss.ginyu.Tieudoitruong;
import com.KhanhDTK.models.boss.list_boss.android.*;
import com.KhanhDTK.models.boss.list_boss.cell.SieuBoHung;
import com.KhanhDTK.models.boss.list_boss.cell.XenBoHung;
import com.KhanhDTK.models.boss.list_boss.doanh_trai.*;
import com.KhanhDTK.models.boss.list_boss.Broly.Broly;
import com.KhanhDTK.models.boss.list_boss.ChristmasEvent.OngGiaNoel;
import com.KhanhDTK.models.boss.list_boss.Doraemon.Nobita;
import com.KhanhDTK.models.boss.list_boss.Doraemon.Xeko;
import com.KhanhDTK.models.boss.list_boss.Doraemon.Xuka;
import com.KhanhDTK.models.boss.list_boss.FideBack.FideRobot;
//import com.KhanhDTK.models.boss.list_boss.NgucTu.SongokuTaAc;
import com.KhanhDTK.models.boss.list_boss.fide.Fide;
import com.KhanhDTK.models.boss.list_boss.Doraemon.Chaien;
import com.KhanhDTK.models.boss.list_boss.NRD.Rong1Sao;
import com.KhanhDTK.models.boss.list_boss.NRD.Rong2Sao;
import com.KhanhDTK.models.boss.list_boss.NRD.Rong3Sao;
import com.KhanhDTK.models.boss.list_boss.NRD.Rong4Sao;
import com.KhanhDTK.models.boss.list_boss.NRD.Rong5Sao;
import com.KhanhDTK.models.boss.list_boss.NRD.Rong6Sao;
import com.KhanhDTK.models.boss.list_boss.NRD.Rong7Sao;
import com.KhanhDTK.models.boss.list_boss.Kaido.KAIDO1;
import com.KhanhDTK.models.boss.list_boss.Mabu12h.MabuBoss;
import com.KhanhDTK.models.boss.list_boss.Mabu12h.BuiBui;
import com.KhanhDTK.models.boss.list_boss.Mabu12h.BuiBui2;
import com.KhanhDTK.models.boss.list_boss.Mabu12h.Drabura;
import com.KhanhDTK.models.boss.list_boss.Mabu12h.Drabura2;
import com.KhanhDTK.models.boss.list_boss.Mabu12h.Yacon;
import com.KhanhDTK.models.boss.list_boss.Rungboss.bill;
import com.KhanhDTK.models.boss.list_boss.Rungboss.bill_1;
import com.KhanhDTK.models.boss.list_boss.Rungboss.bill_2;
import com.KhanhDTK.models.boss.list_boss.Rungboss.whis;
import com.KhanhDTK.models.boss.list_boss.Rungboss.whis_2;
//import com.KhanhDTK.models.boss.list_boss.gohanNN;
//import com.KhanhDTK.models.boss.list_boss.kami.cumberBlack;
//import com.KhanhDTK.models.boss.list_boss.kami.cumberYellow;
//import com.KhanhDTK.models.boss.list_boss.kami.kamiLoc;
//import com.KhanhDTK.models.boss.list_boss.kami.kamiRin;
//import com.KhanhDTK.models.boss.list_boss.kami.kamiSooMe;
import com.KhanhDTK.models.boss.list_boss.nappa.*;
//import com.KhanhDTK.models.boss.list_boss.phoban.TrungUyXanhLoBdkb;
import com.KhanhDTK.yadat.*;
import com.KhanhDTK.models.map.Zone;
import com.KhanhDTK.models.player.Player;
import com.girlkun.network.io.Message;
import com.KhanhDTK.server.ServerManager;
import com.KhanhDTK.services.ItemMapService;
import com.KhanhDTK.services.MapService;
import com.KhanhDTK.models.boss.list_boss.cell.xensandetu;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import com.KhanhDTK.models.boss.list_boss.AnTrom;
import com.KhanhDTK.models.boss.list_boss.Kaido.GokuSsj4;
import com.KhanhDTK.models.boss.list_boss.May;
import com.KhanhDTK.models.boss.list_boss.TrainOffline.Bubbles;
import com.KhanhDTK.models.boss.list_boss.TrainOffline.Popo;
import com.KhanhDTK.models.boss.list_boss.TrainOffline.Yajiro;
import com.KhanhDTK.models.boss.list_boss.bosssukien.gachincua;
import com.KhanhDTK.models.boss.list_boss.bosssukien.gozila;
import com.KhanhDTK.models.boss.list_boss.bosssukien.kong;
import com.KhanhDTK.models.boss.list_boss.bosssukien.ngualomao;
import com.KhanhDTK.models.boss.list_boss.bosssukien.voichinnga;
import com.KhanhDTK.models.boss.list_boss.ginyu.So1;
import com.KhanhDTK.models.boss.list_boss.ginyu.So2;
import com.KhanhDTK.models.boss.list_boss.ginyu.So3;
import com.KhanhDTK.models.boss.list_boss.ginyu.So4;
import com.KhanhDTK.models.boss.list_boss.sanca.Sanca;
//import com.KhanhDTK.models.boss.list_boss.sontinhthuytinh.Sontinh;
//import com.KhanhDTK.models.boss.list_boss.sontinhthuytinh.Thuytinh;
import com.KhanhDTK.server.Manager;
import com.KhanhDTK.services.Service;
import com.KhanhDTK.services.func.ChangeMapService;
import com.KhanhDTK.utils.Util;
import java.io.IOException;
import java.text.Normalizer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BossManager implements Runnable {

    private static BossManager I;
    public static final byte ratioReward = 50;

    public static BossManager gI() {
        if (BossManager.I == null) {
            BossManager.I = new BossManager();
        }
        return BossManager.I;
    }

    protected BossManager() {
        this.bosses = new ArrayList<>();
    }

     public List<Boss> getBosses() {
        return this.bosses;
    }
    private boolean loadedBoss;
    private final List<Boss> bosses;

    public void addBoss(Boss boss) {
        this.bosses.add(boss);
    }

    public void removeBoss(Boss boss) {
        this.bosses.remove(boss);
    }

    public void loadBoss() {
        if (this.loadedBoss) {
            return;
        }
        try {
            this.createBoss(BossID.BROLY);
            this.createBoss(BossID.KING_KONG);
            this.createBoss(BossID.COOLER);
            this.createBoss(BossID.COOLER_GOLD);
            this.createBoss(BossID.XEN_BO_HUNG);
            this.createBoss(BossID.SIEU_BO_HUNG);
            this.createBoss(BossID.XEN_CON_1);
            this.createBoss(BossID.BLACK);
            this.createBoss(BossID.ZAMASZIN);
            this.createBoss(BossID.BLACK2);
            this.createBoss(BossID.BLACK);
            this.createBoss(BossID.BLACK3);
            this.createBoss(BossID.ZAMASMAX);
            this.createBoss(BossID.KUKU);
            this.createBoss(BossID.MAP_DAU_DINH);
            this.createBoss(BossID.RAMBO);
            this.createBoss(BossID.SO_4);
            this.createBoss(BossID.BILL);
            this.createBoss(BossID.BILL1);
            this.createBoss(BossID.BILL2);
            this.createBoss(BossID.FIDE);
            this.createBoss(BossID.MABU);
            this.createBoss(BossID.DR_KORE);
            this.createBoss(BossID.ANDROID_14);
            this.createBoss(BossID.XEN_SAN_DE_TU);
            this.createBoss(BossID.BOSS_YADAT);
            this.createBoss(BossID.BOSS_YADAT1);
            this.createBoss(BossID.BOSS_YADAT3);
            this.createBoss(BossID.KAIDO);
            this.createBoss(BossID.DORAEMON);
            this.createBoss(BossID.ANTROM);
            this.createBoss(BossID.GOZILA);
            this.createBoss(BossID.KONG);
            this.createBoss(BossID.VOICHINNGA);
            this.createBoss(BossID.GACHINCUA);
            this.createBoss(BossID.NGUALOMAO);
//            this.createBoss(BossID.SON_TINH);
            this.createBoss(BossID.GOKU_SSJ4);
            this.createBoss(BossID.MAY);
            this.createBoss(BossID.SAN_CA);
            this.createBoss(BossID.ONG_GIA_NOEL);

        } catch (Exception e) {
            e.printStackTrace();

        }
        this.loadedBoss = true;
        new Thread(BossManager.I, "Update boss").start();
    }

    public Boss createBossBdkb(int bossID, int dame, int hp, Zone zone) {
        try {
            switch (bossID) {
//                case BossID.TRUNG_UY_XANH_LO_BDKB:
//                    return new TrungUyXanhLoBdkb(dame, hp, zone);
                default:
                    return null;
            }
        } catch (Exception e) {

            return null;
        }
    }

    public Boss createBoss(int bossID) {
        try {
            if (bossID <= BossID.BROLY + 5 && bossID >= BossID.BROLY) {
                return new Broly(MapService.gI().getZone(Util.randomMapBossBroly()), 500, 5000);
            }
            switch (bossID) {
                case BossID.ANDROID_13:
                    return new Android13();
                case BossID.ANDROID_14:
                    return new Android14();
                case BossID.ANDROID_15:
                    return new Android15();
                case BossID.ANDROID_19:
                    return new Android19();
                case BossID.ANTROM:
                    return new AnTrom();
                case BossID.KUKU:
                    return new Kuku();
                case BossID.MAP_DAU_DINH:
                    return new MapDauDinh();
                case BossID.RAMBO:
                    return new Rambo();
                case BossID.DRABURA:
                    return new Drabura();
                case BossID.DRABURA_2:
                    return new Drabura2();
                case BossID.BUI_BUI:
                    return new BuiBui();
                case BossID.BUI_BUI_2:
                    return new BuiBui2();
                case BossID.YA_CON:
                    return new Yacon();
                case BossID.MABU_12H:
                    return new MabuBoss();
                case BossID.Rong_1Sao:
                    return new Rong1Sao();
                case BossID.Rong_2Sao:
                    return new Rong2Sao();
                case BossID.Rong_3Sao:
                    return new Rong3Sao();
                case BossID.Rong_4Sao:
                    return new Rong4Sao();
                case BossID.Rong_5Sao:
                    return new Rong5Sao();
                case BossID.Rong_6Sao:
                    return new Rong6Sao();
                case BossID.Rong_7Sao:
                    return new Rong7Sao();
                case BossID.FIDE:
                    return new Fide();
                case BossID.DR_KORE:
                    return new DrKore();
                case BossID.PIC:
                    return new Pic();
                case BossID.POC:
                    return new Poc();
                case BossID.KING_KONG:
                    return new KingKong();
                case BossID.XEN_BO_HUNG:
                    return new XenBoHung();
                case BossID.SIEU_BO_HUNG:
                    return new SieuBoHung();
                case BossID.VUA_COLD:
                    return new Kingcold();
                case BossID.FIDE_ROBOT:
                    return new FideRobot();
                case BossID.COOLER:
                    return new Cooler();
                case BossID.ZAMASZIN:
                    return new ZamasKaio();
                case BossID.BLACK2:
                    return new SuperBlack2();
                case BossID.BLACK1:
                    return new BlackGokuTl();
                case BossID.BILL:
                    return new bill();
                case BossID.WHIS:
                    return new whis();
                case BossID.BILL1:
                    return new bill_1();
                case BossID.WHIS1:
                    return new whis();
                case BossID.BILL2:
                    return new bill_2();
                case BossID.WHIS2:
                    return new whis_2();
                case BossID.BLACK:
                    return new Black();
                case BossID.BLACK3:
                    return new BlackGokuBase();
                case BossID.XEN_CON_1:
                    return new Xencon();
                case BossID.MABU:
                    return new Mabu();
                case BossID.TIEU_DOI_TRUONG:
                    return new Tieudoitruong();
                case BossID.SO_4:
                    return new So4();
                case BossID.SO_3:
                    return new So3();
                case BossID.SO_2:
                    return new So2();
                case BossID.SO_1:
                    return new So1();
                case BossID.COOLER_GOLD:
                    return new CoolerGold();
                case BossID.XEN_SAN_DE_TU:
                    return new xensandetu();
                case BossID.BOSS_YADAT:
                    return new yadatprovip();
                case BossID.BOSS_YADAT1:
                    return new yadatprovip1();
                case BossID.BOSS_YADAT3:
                    return new yadatprovip3();
                case BossID.KAIDO:
                    return new KAIDO1();
                case BossID.YARI:
                    return new Yajiro();
                case BossID.MR_POPO:
                    return new Popo();
                case BossID.BUBBLES:
                    return new Bubbles();
                case BossID.KONG:
                    return new kong();
                case BossID.GOZILA:
                    return new gozila();
                case BossID.VOICHINNGA:
                    return new voichinnga();
                case BossID.GACHINCUA:
                    return new gachincua();
                case BossID.NGUALOMAO:
                    return new ngualomao();
                case BossID.XUKA:
                    return new Xuka();
                case BossID.NOBITA:
                    return new Nobita();
                case BossID.XEKO:
                    return new Xeko();
                case BossID.CHAIEN:
                    return new Chaien();
                case BossID.DORAEMON:
                    return new Doraemon();
//                case BossID.SON_TINH:
//                    return new Sontinh();
//                case BossID.THUY_TINH:
//                    return new Thuytinh();
                case BossID.GOKU_SSJ4:
                    return new GokuSsj4();
                case BossID.MAY:
                    return new May();
                case BossID.SAN_CA:
                    return new Sanca();
                case BossID.ONG_GIA_NOEL:
                    new OngGiaNoel();
                default:
                    return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public boolean checkBosses(Zone zone, int BossID) {
        return this.bosses.stream().filter(boss -> boss.id == BossID && boss.zone != null && boss.zone.equals(zone) && !boss.isDie()).findFirst().orElse(null) != null;
    }
    public boolean existBossOnPlayer(Player player) {
        return player.zone.getBosses().size() > 0;
    }

    public void teleBoss(Player pl, Message _msg) {
        if (_msg != null) {
            try {
                int id = _msg.reader().readInt();
                Boss b = getBossById(id);
                if (b == null) {
                    Player player = GodGK.loadById(id);
                    if (player != null && player.zone != null) {
                        ChangeMapService.gI().changeMapYardrat(pl, player.zone, player.location.x, player.location.y);
                        return;
                    } else {
                        Service.gI().sendThongBao(pl, "Nó trốn rồi");
                        return;
                    }
                }
                if (!b.isDie() && b.zone != null) {
                    ChangeMapService.gI().changeMapYardrat(pl, b.zone, b.location.x, b.location.y);
                } else {
                    Service.gI().sendThongBao(pl, "Boss Hẹo Rồi");
                }
            } catch (IOException e) {
                System.out.println("Loi tele boss");
                e.printStackTrace();
            }
        }
    }

    public void summonBoss(Player pl, Message _msg) {
        if (!pl.getSession().isAdmin) {
            Service.gI().sendThongBao(pl, "Chỉ dành cho Admin");
            return;
        }
        if (_msg != null) {
            try {
                int id = _msg.reader().readInt();
                Boss b = getBossById(id);
                if (b != null && b.zone != null) {
                    ChangeMapService.gI().changeMapYardrat(b, pl.zone, pl.location.x, pl.location.y);
                    return;
                }
                if (b == null) {
                    Player player = GodGK.loadById(id);
                    if (player != null && player.zone != null) {
                        ChangeMapService.gI().changeMapYardrat(player, pl.zone, pl.location.x, pl.location.y);
                    } else {
                        Service.gI().sendThongBao(pl, "Nó trốn rồi");
                    }
                }
            } catch (IOException e) {
                System.out.println("Loi summon boss");
            }
        }
    }

    public void showListBosss(Player player) {
        if (!player.isAdmin()) {
            return;
        }
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(1);
            msg.writer().writeUTF("Boss");
            msg.writer()
                    .writeByte(
                            (int) bosses.stream()
                                    .filter(boss -> !MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0])
                                    && !MapService.gI().isMapDoanhTrai(boss.data[0].getMapJoin()[0])
                                    && !MapService.gI().isMapBlackBallWar(boss.data[0].getMapJoin()[0]))
                                    .count());
            for (int i = 0; i < bosses.size(); i++) {
                Boss boss = this.bosses.get(i);
                if (MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapBlackBallWar(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapBanDoKhoBau(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapDoanhTrai(boss.data[0].getMapJoin()[0])) {
                    continue;
                }
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeShort(boss.data[0].getOutfit()[0]);
                if (player.getSession().version > 214 || player.getSession().version < 231) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(boss.data[0].getOutfit()[1]);
                msg.writer().writeShort(boss.data[0].getOutfit()[2]);
                msg.writer().writeUTF(boss.data[0].getName());
                if (boss.zone != null) {
                    msg.writer().writeUTF("Sống");
                    msg.writer().writeUTF("Thông Tin Boss\n" + "|7|Map : " + boss.zone.map.mapName + "(" + boss.zone.map.mapId + ") \nZone: " + boss.zone.zoneId + "\nHP: " + Util.powerToString(boss.nPoint.hp) + "\nDame: " + Util.powerToString(boss.nPoint.dame));
                } else {
                    msg.writer().writeUTF("Chết");
                    msg.writer().writeUTF("Boss Respawn\n|7|Time to Reset : " + (boss.secondsRest <= 0 ? "BossAppear" : boss.secondsRest + " giây"));
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
//            Logger.logException(Manager.class, e, "Lỗi show list boss");
        }
    }
  public void showListBoss(Player player) {
        if (!player.isAdmin() && !player.isAdmin()) {
            return;
        }
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(1);
            msg.writer().writeUTF("Menu Boss");

            List<Boss> aliveBosses = bosses.stream()
                    .filter(boss -> boss.zone != null)
                    .filter(boss -> !MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapBlackBallWar(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapBanDoKhoBau(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapDoanhTrai(boss.data[0].getMapJoin()[0]))
                    .collect(Collectors.toList());
            

            msg.writer().writeByte(aliveBosses.size());
            for (int i = 0; i < aliveBosses.size(); i++) {
                Boss boss = aliveBosses.get(i);
                msg.writer().writeInt(i);
                msg.writer().writeInt(i);
                msg.writer().writeShort(boss.data[0].getOutfit()[0]);

                if (player.getSession().version > 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(boss.data[0].getOutfit()[1]);
                msg.writer().writeShort(boss.data[0].getOutfit()[2]);
                msg.writer().writeUTF(boss.data[0].getName());

                msg.writer().writeUTF(boss.zone.map.mapName + "(" + boss.zone.map.mapId + ") khu " + boss.zone.zoneId + "");
                if (boss.zone != null) {
                    msg.writer().writeUTF(boss.zone.map.mapName + "(" + boss.zone.map.mapId + ") khu " + boss.zone.zoneId + "");
                } else {
                    msg.writer().writeUTF("N/A");
                }
            }
//                msg.writer().writeShort(boss.data[0].getOutfit()[1]);
//                msg.writer().writeShort(boss.data[0].getOutfit()[2]);
//                msg.writer().writeUTF(boss.data[0].getName());
//                if (boss.zone != null) {
//                    msg.writer().writeUTF("Sống");
//                    msg.writer().writeUTF("Thông Tin Boss\n" + "|7|Map : " + boss.zone.map.mapName + "(" + boss.zone.map.mapId + ") \nZone: " + boss.zone.zoneId + "\nHP: " + Util.powerToString(boss.nPoint.hp) + "\nDame: " + Util.powerToString(boss.nPoint.dame));
//                } else {
//                    msg.writer().writeUTF("Chết");
//                    msg.writer().writeUTF("Boss Respawn\n|7|Time to Reset : " + (boss.secondsRest <= 0 ? "BossAppear" : boss.secondsRest + " giây"));
//                }
//            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void dobossmember(Player player) {
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Boss");
            msg.writer().writeByte((int) bosses.stream().filter(boss -> !MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0])
                    && !MapService.gI().isMapDoanhTrai(boss.data[0].getMapJoin()[0])
                    //                    && !MapService.gI().isMapTienMon(boss.data[0].getMapJoin()[0])
                    //                    && !(boss instanceof MiNuong)
                    && !(boss instanceof AnTrom)
                    && !MapService.gI().isMapBanDoKhoBau(boss.data[0].getMapJoin()[0])
                    && !MapService.gI().isMapKhiGas(boss.data[0].getMapJoin()[0])
                    //                    && !MapService.gI().isMapNhanBan(boss.data[0].getMapJoin()[0])
                    && !MapService.gI().isMapBlackBallWar(boss.data[0].getMapJoin()[0])).count());
            for (int i = 0; i < bosses.size(); i++) {
                Boss boss = this.bosses.get(i);
                if (MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0])
                        //                        || boss instanceof MiNuong 
                        || boss instanceof AnTrom
                        || MapService.gI().isMapBlackBallWar(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapDoanhTrai(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapBanDoKhoBau(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapKhiGas(boss.data[0].getMapJoin()[0])) //                        || MapService.gI().isMapNhanBan(boss.data[0].getMapJoin()[0]) 
                //                        || MapService.gI().isMapTienMon(boss.data[0].getMapJoin()[0]))
                {
                    continue;
                }
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeShort(boss.data[0].getOutfit()[0]);
                if (player.getSession().version > 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(boss.data[0].getOutfit()[1]);
                msg.writer().writeShort(boss.data[0].getOutfit()[2]);
                msg.writer().writeUTF(boss.data[0].getName());
                if (boss.zone != null) {
                    msg.writer().writeUTF("Sống");
                    msg.writer().writeUTF("Dịch chuyển");
                } else {
                    msg.writer().writeUTF("Chết");
                    msg.writer().writeUTF("Chết rồi");
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    public synchronized void callBoss(Player player, int mapId) {
        try {
            if (BossManager.gI().existBossOnPlayer(player)
                    || player.zone.items.stream().anyMatch(itemMap -> ItemMapService.gI().isBlackBall(itemMap.itemTemplate.id))
                    || player.zone.getPlayers().stream().anyMatch(p -> p.iDMark.isHoldBlackBall())) {
                return;
            }
            Boss k = null;
            switch (mapId) {
                case 85:
                    k = BossManager.gI().createBoss(BossID.Rong_1Sao);
                    break;
                case 86:
                    k = BossManager.gI().createBoss(BossID.Rong_2Sao);
                    break;
                case 87:
                    k = BossManager.gI().createBoss(BossID.Rong_3Sao);
                    break;
                case 88:
                    k = BossManager.gI().createBoss(BossID.Rong_4Sao);
                    break;
                case 89:
                    k = BossManager.gI().createBoss(BossID.Rong_5Sao);
                    break;
                case 90:
                    k = BossManager.gI().createBoss(BossID.Rong_6Sao);
                    break;
                case 91:
                    k = BossManager.gI().createBoss(BossID.Rong_7Sao);
                    break;
            }
            if (k != null) {
                k.currentLevel = 0;
                k.joinMapByZone(player);
            }
        } catch (Exception e) {

        }
    }

    public Boss getBossById(int bossId) {
        return BossManager.gI().bosses.stream().filter(boss -> boss.id == bossId && !boss.isDie()).findFirst().orElse(null);
    }

    public static String covertString(String value) {
        String temp = Normalizer.normalize(value, Normalizer.Form.NFD);
        try {
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(temp).replaceAll("");
        } catch (Exception ex) {
            return temp;
        }
    }

    public Boss getBossByName(String name) {
        try {
            for (Boss boss : this.bosses) {
                if (boss.currentLevel > 0) {
                    if (covertString(boss.data[0].getName()).equalsIgnoreCase(covertString(name))) {
                        return boss;
                    }
                }
                if (boss.name == null) {
                    continue;
                }
                if (covertString(boss.name).equalsIgnoreCase(covertString(name))) {
                    return boss;
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public void run() {
        while (ServerManager.isRunning) {
            try {
                long st = System.currentTimeMillis();
                for (Boss boss : this.bosses) {
                    boss.update();
                }
                Thread.sleep(150 - (System.currentTimeMillis() - st));
            } catch (Exception e) {

            }

        }
    }
}

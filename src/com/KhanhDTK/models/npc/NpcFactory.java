package com.KhanhDTK.models.npc;

//import com.KhanhDTK.MaQuaTang.MaQuaTangManager;
import DataControlGame.DataGame;
import com.KhanhDTK.De2.Thu_TrieuHoi;
import com.KhanhDTK.MaQuaTang.MaQuaTangManager;
import com.KhanhDTK.kygui.ItemKyGui;
import com.KhanhDTK.kygui.ShopKyGuiService;
import com.KhanhDTK.consts.ConstMap;
import com.KhanhDTK.consts.ConstNpc;
import com.KhanhDTK.consts.ConstPlayer;
import com.KhanhDTK.consts.ConstTask;
import com.KhanhDTK.models.boss.Boss;
import com.KhanhDTK.consts.ConstTask;
import com.KhanhDTK.server.ServerNotify;
import com.KhanhDTK.models.boss.BossData;
import com.KhanhDTK.models.boss.BossID;
import com.KhanhDTK.models.boss.BossManager;
import com.KhanhDTK.models.boss.list_boss.NhanBan;
import com.KhanhDTK.models.clan.Clan;
import com.KhanhDTK.models.clan.ClanMember;
import com.KhanhDTK.models.item.Item;
import com.KhanhDTK.models.map.Map;
import com.KhanhDTK.models.map.MapMaBu.MapMaBu;
import com.KhanhDTK.models.map.MapSatan.MapTrung;
import com.KhanhDTK.models.map.Zone;
import com.KhanhDTK.models.map.doanhtrai.DoanhTrai;
import com.KhanhDTK.models.map.doanhtrai.DoanhTraiService;
import com.KhanhDTK.models.map.BDKB.BanDoKhoBau;
import com.KhanhDTK.models.map.BDKB.BanDoKhoBauService;
import com.KhanhDTK.models.map.blackball.BlackBallWar;
import com.KhanhDTK.models.map.challenge.MartialCongressService;
//import com.KhanhDTK.models.map.khigas.KhiGas;
//import com.KhanhDTK.models.map.khigas.KhiGasService;
import com.girlkun.database.GirlkunDB;
import com.KhanhDTK.jdbc.daos.PlayerDAO;
import com.KhanhDTK.models.boss.BossesData;
import com.KhanhDTK.models.boss.list_boss.Broly.LeoThap;
import com.KhanhDTK.models.boss.list_boss.TrainOffline.MeoThan;
import com.KhanhDTK.models.boss.list_boss.TrainOffline.ThanVuTru;
import com.KhanhDTK.models.boss.list_boss.TrainOffline.Thuongde;
import com.KhanhDTK.models.boss.list_boss.TrainOffline.ToSuKaio;
import com.KhanhDTK.models.item.Item.ItemOption;
//import com.KhanhDTK.models.ThanhTich.OnlineHangNgay;
//import com.KhanhDTK.models.ThanhTich.QuaNapHangNgay;
//import com.KhanhDTK.models.ThanhTich.ThanhTichPlayer;
import com.KhanhDTK.models.map.BDKB.BanDoKhoBau;
import com.KhanhDTK.models.map.BDKB.BanDoKhoBauService;
import com.KhanhDTK.models.map.daihoi.DaiHoiManager;
import com.KhanhDTK.models.map.gas.Gas;
import com.KhanhDTK.models.map.gas.GasService;
import com.KhanhDTK.models.map.gas.TopGasService;
import com.KhanhDTK.models.map.nguhanhson.nguhs;
import com.KhanhDTK.models.map.vodai.VoDaiService;
//import com.KhanhDTK.models.matches.pvp.DaiHoiVoThuat;
//import com.KhanhDTK.models.matches.pvp.DaiHoiVoThuatService;
import com.KhanhDTK.models.matches.PVPService;
import com.KhanhDTK.models.matches.TOP;
//import com.KhanhDTK.models.matches.pvp.DaiHoiVoThuat;
//import com.KhanhDTK.models.matches.pvp.DaiHoiVoThuatService;
import com.KhanhDTK.models.mob.Mob;
import com.KhanhDTK.models.player.Inventory;
import com.KhanhDTK.models.player.NPoint;
import com.KhanhDTK.models.player.Player;
import com.KhanhDTK.models.shop.ShopServiceNew;
import com.KhanhDTK.models.skill.Skill;
import com.KhanhDTK.models.task.TaskPlayer;
import com.girlkun.network.io.Message;
import com.girlkun.result.GirlkunResultSet;
import com.KhanhDTK.server.Client;
import com.KhanhDTK.server.Maintenance;
import com.KhanhDTK.server.Manager;
import com.KhanhDTK.server.ServerManager;
import com.KhanhDTK.services.*;
import com.KhanhDTK.services.func.*;
import com.KhanhDTK.utils.Logger;
import com.KhanhDTK.utils.TimeUtil;
import com.KhanhDTK.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import static com.KhanhDTK.services.func.SummonDragon.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class NpcFactory {

    private static final int COST_HD = 50000000;

    private static boolean nhanVang = false;
    private static boolean nhanDeTu = false;

    // playerid - object
    public static final java.util.Map<Long, Object> PLAYERID_OBJECT = new HashMap<Long, Object>();

    private NpcFactory() {

    }

    public static int getRandomValue(int min, int max, Random random) {
        return random.nextInt(max - min + 1) + min;
    }

    public static Npc Tapion(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (mapId == 19) {
                        MapTrung.gI().setTimeJoinMapSatan();
                        long now = System.currentTimeMillis();
                        if (now > MapTrung.TIME_OPEN_SATAN && now < MapTrung.TIME_CLOSE_SATAN) {
                            this.createOtherMenu(player, ConstNpc.MENU_OPEN_SANTA, "Đại chiến Hirudegan đã mở, "
                                    + "ngươi có muốn tham gia không?",
                                    "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                        } else {
                            this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_SANTA,
                                    "Ta có thể giúp gì cho ngươi?", "Hướng dẫn",
                                    "Từ chối");
                        }
                    }
                    if (mapId == 126) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Muốn Chạy Về Nhà Khóc,Haha à?", "Chuẩn",
                                "Từ chối");

                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 19) {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.MENU_REWARD_SANTA:
                                break;
                            case ConstNpc.MENU_OPEN_SANTA:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_SATAN);
                                } else if (select == 1) {
                                    ChangeMapService.gI().changeMap(player, 126, Util.nextInt(0, 8), 163, 336);
                                }
                                break;
                            case ConstNpc.MENU_NOT_OPEN_BDW:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_SATAN);
                                }
                                if (select == 1) {
                                    if (player.nPoint.power >= 2000000000) {
                                        Service.gI().sendThongBaoAllPlayer("Kẻ Ngu Ngơ Khờ Dại Mang Tên " + player.name
                                                + " Đòi Không Làm Mà Đòi Có Ăn");
                                    } else {

                                        Service.gI().sendThongBaoOK(player,
                                                "Yêu cầu 2 tỉ sức mạnh");

                                    }
                                }
                                break;
                        }
                    } else if (this.mapId == 126) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.BASE_MENU) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        }
                    }
                }

            };

        }

        ;

    }

    public static Npc bkt(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 146) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi muốn tiếp tục leo tháp chứ!\n Tháp hiện tại của ngươi là :" + player.capboss,
                                "Thách Đấu", "Xem Top Lep Tháp", "Nhận Quà", "Về Đảo Kame", "Từ chối");
                    }
                }
            }

            @Override

            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        if (this.mapId == 146) {
                            switch (select) {
                                case 0:
                                    if (player.inventory.gem < 5) {
                                        this.npcChat(player, "Cần 5 ngọc xanh");
                                        return;
                                    }
                                    if (player.nPoint.hpMax + player.nPoint.dame < 20000) {
                                        this.npcChat(player, "Bạn còn quá yếu vui lòng quay lại sau");
                                        return;
                                    }
                                    Boss oldBossClone = BossManager.gI().getBossById(Util.createIdBossLV(player.id));
                                    if (oldBossClone != null) {
                                        oldBossClone.setDie(oldBossClone);
                                        this.npcChat(player, "Ấn thách đấu lại xem!");
                                    } else {
                                        long hp = 0;
                                        int dk = (player.capboss + 1) * 2;
                                        long hptong = (player.nPoint.hpMax + hp) * dk
                                                * (player.capboss >= 5 ? 2 * dk : 1);
                                        BossData bossDataClone = new BossData(
                                                "Leo Tháp [Tầng: " + player.capboss + "]",
                                                ConstPlayer.NAMEC,
                                                new short[] { 1554, 1555, 1556, player.getFlagBag(), player.idAura,
                                                        player.getEffFront() },
                                                10_000 * dk,
                                                new int[] { 10_000_000 * dk },
                                                new int[] { 174 },
                                                new int[][] {
                                                        { Skill.LIEN_HOAN, 7, 500 },
                                                        { Skill.MASENKO, 7, 3000 },
                                                        { Skill.DICH_CHUYEN_TUC_THOI, 7, 60000 },
                                                        { Skill.BIEN_KHI, 1, 60000 }
                                                },
                                                new String[] { "|-2|Ta sẽ tiêu diệt ngươi" }, // text
                                                // chat 1
                                                new String[] { "|-1|Ta Sẽ đập nát đầu ngươi!" }, // text chat 2
                                                new String[] { "|-1|Hẹn người lần sau" }, // text chat 3
                                                1);
                                        try {
                                            new LeoThap(Util.createIdBossLV(player.id), bossDataClone, player.zone,
                                                    player.name, player.capboss, player);
                                        } catch (Exception e) {
                                            Logger.logException(NpcFactory.class, e);
                                        }
                                        player.inventory.gem -= 5;
                                        Service.gI().sendMoney(player);
                                    }
                                    break;
                                case 1:
                                    Service.gI().showListTop(player, Manager.TopLeoThap);
                                    break;
                                case 2:
                                    Service.gI().sendThongBao(player, "Đang Update!!!");
                                    break;

                                case 3:
                                    ChangeMapService.gI().changeMap(player, 5, -1, 1043, 168);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc duahau(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 21 || this.mapId == 22 || this.mapId == 23) {
                    if (DuaHau.gI().NauXong == true) {
                        this.createOtherMenu(player, 0, "Dưa Hấu đã chín, Bạn có "
                                + Util.tinhgio(DuaHau.gI().ThoiGianChoDuaHau)
                                + " để lấy\n|2|Nếu offline số dưa hấu có thể được lấy vào đợt sau!",
                                "Lấy Dưa Hấu");
                    } else if (DuaHau.gI().ChoXong == true) {
                        this.createOtherMenu(player, 1, "|2|Trồng Dưa Hấu Toàn Server Đợt " + DuaHau.gI().Count
                                + "\n|-1|Đang trong thời gian trồng, bạn có thể cho thêm hạt giống vào trồng ké"
                                + "\nMỗi lần trồng chỉ trồng được 1 cái"
                                + "\nThời gian dưa hấu chín còn: " + Util.tinhgio(DuaHau.gI().ThoiGianTrong)
                                + "\nHiện tại có: " + (DuaHau.gI().ListPlDuaHau.size()) + " dưa hấu đang trồng"
                                + "\nTrong đó bạn có: " + (DuaHau.gI().plDuaHau) + " dưa hấu mới\n("
                                + (player.DuaHau) + " dưa hấu trước đó chưa lấy)", "Trồng Dưa Hấu", "Hướng dẫn");
                    } else if (DuaHau.gI().ChoXong == false) {
                        this.createOtherMenu(player, 4, "|2|Trồng Dưa Hấu Toàn Server Đợt " + DuaHau.gI().Count
                                + "\n|-1|Thời gian chờ trồng còn: " + Util.tinhgio(DuaHau.gI().ThoiGianChoDH)
                                + "\nPhân Bón: " + Util.format(DuaHau.gI().Phanbon) + " % "
                                + (DuaHau.gI().BinhNuoc >= 50 && DuaHau.gI().BinhNuoc < 100 ? "(Trung bình)"
                                        : DuaHau.gI().BinhNuoc >= 100 ? "(Đã đầy)" : "(Thấp)")
                                + "\nSố nuóc đã thêm: " + DuaHau.gI().BinhNuoc
                                + "\nĐủ nước và phân bón sẽ bắt đầu trồng"
                                + "\nThêm đủ nước để đất không bị khô và nhận đủ số bánh trồng"
                                + "\nThêm nuóc để tăng tốc thời gian trồng dưa hấu",
                                "Thêm phân bón", "Thêm nước", "Hướng dẫn");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 21 || this.mapId == 22 || this.mapId == 23) {
                        switch (player.iDMark.getIndexMenu()) {
                            case 1:
                                switch (select) {
                                    case 0:
                                        this.createOtherMenu(player, 2,
                                                "Trồng Dưa Hấu: 1 Hạt Giống, 1 Bình Nước, 1 Phân Bón", "Trồng", "Đóng");
                                        break;
                                }
                                break;
                            case 2:
                                Input.gI().createFormTrongDua(player);
                                break;
                        }
                        if (player.iDMark.getIndexMenu() == 0) {
                            if (player.DuaHau == 0) {
                                Service.gI().sendThongBao(player, "Có trồng gì đéo đâu mà đòi nhận");
                                return;
                            }
                            if (player.DuaHau != 0) {
                                Item DuaHau = ItemService.gI().createNewItem((short) 569, player.DuaHau);
                                InventoryServiceNew.gI().addItemBag(player, DuaHau);
                                InventoryServiceNew.gI().sendItemBags(player);
                                player.point_hungvuong += 1;
                                Service.gI().sendThongBao(player,
                                        "Bạn đã nhận được " + DuaHau.template.name + " 1 điểm Hùng Vương");
                                player.DuaHau = 0;
                            }
                        } else if (player.iDMark.getIndexMenu() == 4) {
                            switch (select) {
                                case 0:
                                    Item phanbon = InventoryServiceNew.gI().findItemBag(player, 1454);
                                    if (phanbon == null) {
                                        Service.gI().sendThongBao(player, "Có phân bón đâu cu");
                                        return;
                                    }
                                    if (DuaHau.gI().Phanbon < 100) {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, phanbon, 1);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        DuaHau.gI().Phanbon++;
                                    } else {
                                        Service.gI().sendThongBao(player, "Đủ phân bón rồi cu");
                                    }
                                    break;
                                case 1:
                                    Item binhnuoc = InventoryServiceNew.gI().findItemBag(player, 1455);
                                    if (binhnuoc == null) {
                                        Service.gI().sendThongBao(player, "Có nước đâu cu");
                                        return;
                                    }
                                    if (DuaHau.gI().BinhNuoc < 100) {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, binhnuoc, 1);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        DuaHau.gI().BinhNuoc++;
                                        DuaHau.gI().ThoiGianTrong -= (1000);
                                    }
                                    break;
                                case 2:
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.TRONGDUAHAU);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    private static Npc hungvuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "|7|Con hãy mang đến cho ta đủ voi chín ngà, gà chín cựa, ngựa chín hồng mao\n"
                                        + "Để có thể nhận lại phần quả sứng đáng",
                                "Dâng lễ vật", "Top Sự Kiện", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        if (this.mapId == 5) {
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, 3,
                                            "|7|Con hãy mang đến cho ta đủ voi chín ngà, gà chín cựa, ngựa chín hồng mao\n"
                                                    + "Để có thể nhận lại phần quả sứng đáng",
                                            "Hộp Quà Thường", "Hộp Quà Cao Cấp", "Từ chối");
                                    break;
                                case 1:
                                    Service.gI().showListTop(player, Manager.TopVuaHung);
                                    break;
                            }

                        }
                    } else if (player.iDMark.getIndexMenu() == 3) {
                        switch (select) {
                            case 0:
                                Item voichinnga = null;
                                Item gachincua = null;
                                Item nguachinhongmao = null;

                                try {
                                    voichinnga = InventoryServiceNew.gI().findItemBag(player, 1401);
                                    gachincua = InventoryServiceNew.gI().findItemBag(player, 1402);
                                    nguachinhongmao = InventoryServiceNew.gI().findItemBag(player, 1403);
                                } catch (Exception e) {
                                }
                                if (voichinnga == null || voichinnga.quantity < 2 && gachincua == null
                                        || gachincua.quantity < 2 && nguachinhongmao == null
                                        || nguachinhongmao.quantity < 2) {
                                    this.npcChat(player, "Con Làm Gì Có Lễ Vật Nào");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "Hành Trang Của Con Không Đủ Chỗ Trống");
                                } else {
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, voichinnga, 2);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, gachincua, 2);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, nguachinhongmao, 2);
                                    player.inventory.gold -= 500000;
                                    Item ct = ItemService.gI().createNewItem((short) 1457);
                                    ct.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                                    ct.itemOptions.add(new ItemOption(103, Util.nextInt(20, 35)));
                                    ct.itemOptions.add(new ItemOption(50, Util.nextInt(20, 35)));
                                    ct.itemOptions.add(new ItemOption(77, Util.nextInt(20, 35)));
                                    InventoryServiceNew.gI().addItemBag(player, ct);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn nhận được " + ct.template.name);
                                }
                                break;
                            case 1:
                                Item voichinngaa = null;
                                Item gachincuaa = null;
                                Item nguachinhongmaoo = null;

                                try {
                                    voichinngaa = InventoryServiceNew.gI().findItemBag(player, 1401);
                                    gachincuaa = InventoryServiceNew.gI().findItemBag(player, 1402);
                                    nguachinhongmaoo = InventoryServiceNew.gI().findItemBag(player, 1403);
                                } catch (Exception e) {
                                }
                                if (voichinngaa == null || voichinngaa.quantity < 2 && gachincuaa == null
                                        || gachincuaa.quantity < 2 && nguachinhongmaoo == null
                                        || nguachinhongmaoo.quantity < 2) {
                                    this.npcChat(player, "Con Làm Gì Có Lễ Vật Nào");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "Hành Trang Của Con Không Đủ Chỗ Trống");
                                } else {
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, voichinngaa, 2);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, gachincuaa, 2);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, nguachinhongmaoo, 2);
                                    player.inventory.ruby -= 5000;
                                    Item ct = ItemService.gI().createNewItem((short) 1458);
                                    ct.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                                    ct.itemOptions.add(new ItemOption(103, Util.nextInt(20, 35)));
                                    ct.itemOptions.add(new ItemOption(50, Util.nextInt(20, 35)));
                                    ct.itemOptions.add(new ItemOption(77, Util.nextInt(20, 35)));
                                    InventoryServiceNew.gI().addItemBag(player, ct);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn nhận được " + ct.template.name);
                                }
                                break;
                        }
                    }
                }
            }

        };
    }

    private static Npc baibien(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "|7|Mùa Hè Tới Rồi, Vào Mà Húp Sự Kiện Đi\n",
                                "Nhận Quà Sự Kiện Hằng Ngày", "Đổi Cá Diêu Hồng", "Đổi Xô Cá", "Shop Sự Kiện",
                                "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        if (this.mapId == 5) {
                            switch (select) {
                                case 0:
                                    if (player.diemdanhsk < 1) {
                                        int canoc = 0;
                                        Item canocne = ItemService.gI().createNewItem((short) 1002, canoc);
                                        canocne.itemOptions.add(new ItemOption(174, 2024));
                                        canocne.quantity += 2;
                                        InventoryServiceNew.gI().addItemBag(player, canocne);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendMoney(player);
                                        player.diemdanhsk++;
                                        Service.getInstance().sendThongBao(player,
                                                "|7|Nhận quà hằng ngày thành công!\nNhận được "
                                                        + canocne.template.name);
                                    } else {
                                        this.npcChat(player, "Hôm nay đã nhận rồi mà !!!");
                                    }
                                    break;
                                case 1:
                                    this.createOtherMenu(player, 3, "|7|Mùa Hè Tới Rồi, Vào Mà Húp Sự Kiện Đi\n"
                                            + "Công thức đổi cá Diêu Hồng\n"
                                            + "Cá Nóc x99 + 10 triệu vàng => 1 cá Diêu Hồng.\n"
                                            + "Cá Bảy Màu x10 + 10 triệu vàng => 1 cá Diêu Hồng.\n",
                                            "Đổi Cá Diêu Hồng Bằng Cá Nóc", "Đổi Cá Diêu Hồng Bằng Cá Bảy Màu",
                                            "Từ chối");
                                    break;
                                case 2:
                                    this.createOtherMenu(player, 4, "|7|Mùa Hè Tới Rồi, Vào Mà Húp Sự Kiện Đi\n"
                                            + "Công thức đổi quà:\n"
                                            + "Cá Diêu Hồng x1 + 5 triệu vàng => 1 xô cá Vàng.\n"
                                            + "Cá Diêu Hồng x1 + 10 Hồng ngọc => 1 xô cá Xanh.\n",
                                            "Đổi Xô Cá Vàng", "Đổi Xô Cá Xanh", "Từ chối");
                                    break;
                                case 3:
                                    ShopServiceNew.gI().opendShop(player, "BAIBIEN", false);
                                    break;
                            }

                        }
                    } else if (player.iDMark.getIndexMenu() == 3) {
                        switch (select) {
                            case 0:
                                Item canoc = null;

                                try {
                                    canoc = InventoryServiceNew.gI().findItemBag(player, 1002);
                                } catch (Exception e) {
                                }
                                if (canoc == null || canoc.quantity < 99 && player.inventory.gold >= 10000000) {
                                    this.npcChat(player, "Bạn Không Có Vật Phẩm Nào");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "Hành Trang Của Bạn Không Đủ Chỗ Trống");
                                } else if (player.inventory.gold < 10000000) {
                                    Service.gI().sendThongBaoOK(player,
                                            "Bạn còn thiều " + (10000000 - player.inventory.gold) + " Vàng");
                                    break;
                                } else {
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, canoc, 99);
                                    player.inventory.gold -= 500000000;
                                    Item cadieuhong = ItemService.gI().createNewItem((short) 1004);
                                    cadieuhong.itemOptions.add(new ItemOption(174, 2024));
                                    InventoryServiceNew.gI().addItemBag(player, cadieuhong);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn nhận được " + cadieuhong.template.name);
                                }
                                break;
                            case 1:
                                Item cabaymau = null;

                                try {
                                    cabaymau = InventoryServiceNew.gI().findItemBag(player, 1003);
                                } catch (Exception e) {
                                }
                                if (cabaymau == null || cabaymau.quantity < 10 && player.inventory.gold >= 10000000) {
                                    this.npcChat(player, "Bạn Không Có Vật Phẩm Nào");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "Hành Trang Của Bạn Không Đủ Chỗ Trống");
                                } else if (player.inventory.gold < 10000000) {
                                    Service.gI().sendThongBaoOK(player,
                                            "Bạn còn thiều " + (10000000 - player.inventory.gold) + " Vàng");
                                    break;
                                } else {
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, cabaymau, 10);
                                    player.inventory.gold -= 500000000;
                                    Item cadieuhong = ItemService.gI().createNewItem((short) 1004);
                                    cadieuhong.itemOptions.add(new ItemOption(174, 2024));
                                    InventoryServiceNew.gI().addItemBag(player, cadieuhong);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn nhận được " + cadieuhong.template.name);
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 4) {
                        switch (select) {
                            case 0:
                                Item cadieuhongne = null;

                                try {
                                    cadieuhongne = InventoryServiceNew.gI().findItemBag(player, 1004);
                                } catch (Exception e) {
                                }
                                if (cadieuhongne == null
                                        || cadieuhongne.quantity < 1 && player.inventory.gold >= 5000000) {
                                    this.npcChat(player, "Bạn Không Có Vật Phẩm Nào");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "Hành Trang Của Bạn Không Đủ Chỗ Trống");
                                } else if (player.inventory.gold < 5000000) {
                                    Service.gI().sendThongBaoOK(player,
                                            "Bạn còn thiều " + (5000000 - player.inventory.gold) + " Vàng");
                                    break;
                                } else {
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, cadieuhongne, 1);
                                    player.inventory.gold -= 500000000;
                                    Item xocavangne = ItemService.gI().createNewItem((short) 1006);
                                    xocavangne.itemOptions.add(new ItemOption(174, 2024));
                                    InventoryServiceNew.gI().addItemBag(player, xocavangne);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn nhận được " + xocavangne.template.name);
                                }
                                break;
                            case 1:
                                Item cadieuhongnee = null;

                                try {
                                    cadieuhongnee = InventoryServiceNew.gI().findItemBag(player, 1004);
                                } catch (Exception e) {
                                }
                                if (cadieuhongnee == null
                                        || cadieuhongnee.quantity < 1 && player.inventory.ruby >= 10) {
                                    this.npcChat(player, "Bạn Không Có Vật Phẩm Nào");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "Hành Trang Của Bạn Không Đủ Chỗ Trống");
                                } else if (player.inventory.gold < 5000000) {
                                    Service.gI().sendThongBaoOK(player,
                                            "Bạn còn thiều " + (10 - player.inventory.ruby) + " Hồng Ngọc");
                                    break;
                                } else {
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, cadieuhongnee, 1);
                                    player.inventory.ruby -= 10;
                                    Item xocaxanhne = ItemService.gI().createNewItem((short) 1005);
                                    xocaxanhne.itemOptions.add(new ItemOption(174, 2024));
                                    InventoryServiceNew.gI().addItemBag(player, xocaxanhne);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn nhận được " + xocaxanhne.template.name);
                                }
                                break;
                        }
                    }
                }
            }

        };
    }

    private static Npc xemia(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 0 || this.mapId == 7 || this.mapId == 14) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "|7|Cư dân tìm khúc mía và nước đá\n"
                                + "Sau đó đến các xe nước mía ở đầu làng để xay nước mía\n"
                                + "|5|[CÔNG THỨC]\n"
                                + "|2|Nước Mía Khổng Lồ: 100 Cục đá, 50 khúc mía, 500 triệu vàng\n"
                                + "Nước Mía Ép Thơm: 200 Cục đá, 70 khúc mía, 500 triệu vàng\n"
                                + "Nước Mía Sầu Riêng: 300 Cục đá, 100 khúc mía, 500 triệu vàng\n"
                                + "|7|Nước Mía Khổng Lồ tăng 10%HP\n"
                                + "Nước Mía Ép Tơm tăng 10% HP, KI\n"
                                + "Nước Mía Sầu Riêng tăng 10%HP, KI, SĐ\n"
                                + "|-1|Úp khúc mía tại Rừng Auruta, NPC Thiên Sứ Whis, quái tỉ lệ khúc mía 30%\n"
                                + "Cục đá đổi hoặc mua tại shop sự kiện, Poc Bãi Biển ở đảo kame\n"
                                + "Uống mỗi loại cốc nước mía sẽ được tăng chỉ số 15p\n"
                                + "chúc các bạn chơi game vui vẻ\n",
                                "Nước Mía\nKhổng Lồ\n(1 phút)", "Nước Mía\nÉp Thơm\n(3 phút)",
                                "Nước Mía\nSầu Riêng\n(5 phút)", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    Item cucDa;
                    Item khucMia;
                    if (player.iDMark.isBaseMenu()) {
                        if (this.mapId == 0 || this.mapId == 7 || this.mapId == 14) {
                            switch (select) {
                                case 0:
                                    cucDa = InventoryServiceNew.gI().findItemBag(player, 1645);
                                    khucMia = InventoryServiceNew.gI().findItemBag(player, 1646);
                                    if (cucDa != null && cucDa.quantity < 50) {
                                        this.npcChat(player, "Bạn còn thiếu x" + (50 - cucDa.quantity) + " cục đá.");
                                    } else if (cucDa == null) {
                                        this.npcChat(player, "Bạn không có cục đá nào.");
                                    } else if (khucMia != null && khucMia.quantity < 100) {
                                        this.npcChat(player,
                                                "Bạn còn thiếu x" + (100 - khucMia.quantity) + " khúc mía.");
                                    } else if (khucMia == null) {
                                        this.npcChat(player, "Bạn không có khúc mía nào.");
                                    } else {
                                        new Thread(() -> {
                                            int timeWait = 10;
                                            while (timeWait > 0) {
                                                try {
                                                    timeWait--;
                                                    this.npcChat(player, "Đang xay nước mía\n|7|Thời gian còn lại: "
                                                            + timeWait + ".");
                                                    Thread.sleep(1000);
                                                } catch (InterruptedException ex) {
                                                }
                                            }
                                            Item nuocMia = ItemService.gI().createNewItem((short) 1642);
                                            cucDa.quantity -= 50;
                                            khucMia.quantity -= 100;
                                            player.inventory.gold -= 500_000_000;
                                            Service.gI().sendMoney(player);
                                            InventoryServiceNew.gI().addItemBag(player, nuocMia);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                    "Đã xay nước mía xong\n|7|Bạn đã nhận được "
                                                            + nuocMia.template.name,
                                                    "Nhận Ngay");
                                        }).start();
                                    }
                                    break;
                                case 1:
                                    cucDa = InventoryServiceNew.gI().findItemBag(player, 1645);
                                    khucMia = InventoryServiceNew.gI().findItemBag(player, 1646);
                                    if (cucDa != null && cucDa.quantity < 70) {
                                        this.npcChat(player, "Bạn còn thiếu x" + (70 - cucDa.quantity) + " cục đá.");
                                    } else if (cucDa == null) {
                                        this.npcChat(player, "Bạn không có cục đá nào.");
                                    } else if (khucMia != null && khucMia.quantity < 200) {
                                        this.npcChat(player,
                                                "Bạn còn thiếu x" + (200 - khucMia.quantity) + " khúc mía.");
                                    } else if (khucMia == null) {
                                        this.npcChat(player, "Bạn không có khúc mía nào.");
                                    } else {
                                        new Thread(() -> {
                                            int timeWait = 30;
                                            while (timeWait > 0) {
                                                try {
                                                    timeWait--;
                                                    this.npcChat(player, "Đang xay nước mía\n|7|Thời gian còn lại: "
                                                            + timeWait + ".");
                                                    Thread.sleep(1000);
                                                } catch (InterruptedException ex) {
                                                }
                                            }
                                            Item nuocMia = ItemService.gI().createNewItem((short) 1643);
                                            cucDa.quantity -= 70;
                                            khucMia.quantity -= 200;
                                            player.inventory.gold -= 500_000_000;
                                            Service.gI().sendMoney(player);
                                            InventoryServiceNew.gI().addItemBag(player, nuocMia);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                    "Đã xay nước mía xong\n|7|Bạn đã nhận được "
                                                            + nuocMia.template.name,
                                                    "Nhận Ngay");
                                        }).start();
                                    }
                                    break;
                                case 2:
                                    cucDa = InventoryServiceNew.gI().findItemBag(player, 1645);
                                    khucMia = InventoryServiceNew.gI().findItemBag(player, 1646);
                                    if (cucDa != null && cucDa.quantity < 100) {
                                        this.npcChat(player, "Bạn còn thiếu x" + (100 - cucDa.quantity) + " cục đá.");
                                    } else if (cucDa == null) {
                                        this.npcChat(player, "Bạn không có cục đá nào.");
                                    } else if (khucMia != null && khucMia.quantity < 300) {
                                        this.npcChat(player,
                                                "Bạn còn thiếu x" + (300 - khucMia.quantity) + " khúc mía.");
                                    } else if (khucMia == null) {
                                        this.npcChat(player, "Bạn không có khúc mía nào.");
                                    } else {
                                        new Thread(() -> {
                                            int timeWait = 60;
                                            while (timeWait > 0) {
                                                try {
                                                    timeWait--;
                                                    this.npcChat(player, "Đang xay nước mía\n|7|Thời gian còn lại: "
                                                            + timeWait + ".");
                                                    Thread.sleep(1000);
                                                } catch (InterruptedException ex) {
                                                }
                                            }
                                            Item nuocMia = ItemService.gI().createNewItem((short) 1644);
                                            cucDa.quantity -= 100;
                                            khucMia.quantity -= 300;
                                            player.inventory.gold -= 500_000_000;
                                            Service.gI().sendMoney(player);
                                            InventoryServiceNew.gI().addItemBag(player, nuocMia);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                    "Đã xay nước mía xong\n|7|Bạn đã nhận được "
                                                            + nuocMia.template.name,
                                                    "Nhận Ngay");
                                        }).start();
                                    }
                                    break;
                            }

                        }
                    }
                }
            }

        };
    }

    private static Npc trungLinhThu(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 104) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Đổi Trứng Linh thú cần:\b|7|X99 Hồn Linh Thú + 1 Tỷ vàng", "Đổi Trứng\nLinh thú",
                                "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 104) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: {
                                    Item honLinhThu = null;
                                    try {
                                        honLinhThu = InventoryServiceNew.gI().findItemBag(player, 2029);
                                    } catch (Exception e) {
                                        System.err.print("\nError at 209\n");
                                        e.printStackTrace();
                                    }
                                    if (honLinhThu == null || honLinhThu.quantity < 99) {
                                        this.npcChat(player, "Bạn không đủ 99 Hồn Linh thú");
                                    } else if (player.inventory.gold < 1_000_000_000) {
                                        this.npcChat(player, "Bạn không đủ 1 Tỷ vàng");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        player.inventory.gold -= 1_000_000_000;
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, honLinhThu, 99);
                                        Service.gI().sendMoney(player);
                                        Item trungLinhThu = ItemService.gI().createNewItem((short) 2028);
                                        InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Bạn nhận được 1 Trứng Linh thú");
                                    }
                                    break;
                                }

                                case 1:

                                    break;
                                case 2:

                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.Nang_Chien_Linh:
                                case CombineServiceNew.MO_CHI_SO_Chien_Linh:
                                    if (select == 0) {

                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    private static Npc kyGui(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, 0,
                            "Cửa hàng chúng tôi chuyên mua bán hàng hiệu, hàng độc, cảm ơn bạn đã ghé thăm.",
                            "Hướng\ndẫn\nthêm", "Mua bán\nKý gửi", "Từ chối");
                }
            }

            @Override
            public void confirmMenu(Player pl, int select) {
                if (pl.nPoint.power < 50000000000L) {
                    Service.gI().sendThongBao(pl, "Yêu cầu sức mạnh lớn hơn 50 tỷ");
                    return;
                }
                // if (mapId == 0) {
                // if (player.nPoint.power < 400000000000L || player.nPoint.power >=
                // 2000000000000L) {
                // this.npcChat(player, "yeu cau 40 ti sm!");
                // return;
                if (canOpenNpc(pl)) {
                    switch (select) {
                        case 0:
                            Service.gI().sendPopUpMultiLine(pl, tempId, avartar,
                                    "Cửa hàng chuyên nhận ký gửi mua bán vật phẩm\bChỉ với 5 hồng ngọc\bGiá trị ký gửi 10k-200Tr vàng hoặc 2-2k ngọc\bMột người bán, vạn người mua, mại dô, mại dô");
                            break;
                        case 1:
                            ShopKyGuiService.gI().openShopKyGui(pl);
                            break;

                    }
                }
            }
        };
    }

    /////////////////////////////////////////// NPC Quy Lão
    /////////////////////////////////////////// Kame///////////////////////////////////////////
    private static Npc quyLaoKame(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            public void chatWithNpc(Player player) {
                String[] chat = {
                        "La lá là",
                        "La lá là",
                        "Lá là la"
                };
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    int index = 0;

                    @Override
                    public void run() {
                        npcChat(player, chat[index]);
                        index = (index + 1) % chat.length;
                    }
                }, 10000, 10000);
            }

            @Override
            public void openBaseMenu(Player player) {
                chatWithNpc(player);
                Item ruacon = InventoryServiceNew.gI().findItemBag(player, 874);
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (player.zone.map.mapId == 5) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Chào con, ta rất vui khi gặp con\n Con muốn làm gì nào ?\n",

                                    "Chức Năng Bang Hội", "Kho Báu\ndưới biển", "Từ chối", " Cho kẹo\nhay\nbị ghẹo?");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.getIndexMenu() == ConstNpc.BASE_MENU) {
                        if (player.zone.map.mapId == 5) {
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, ConstNpc.CHUC_NANG_BANG_HOI,
                                            "Ta có hỗ trợ những chức năng Bang hội, nhà ngươi cần gì?",
                                            "Giải tán\nBang", "Nâng cấp\nBang", "Quyên Góp\nĐiểm Capsule",
                                            "Lãnh địa\nBang", "Từ chối");
                                    break;
                                case 1:
                                    if (player.clan != null) {
                                        if (player.clan.BanDoKhoBau != null) {
                                            this.createOtherMenu(player, ConstNpc.MENU_OPENED_DBKB,
                                                    "Bang hội của con đang đi tìm kho báu dưới biển cấp độ "
                                                            + player.clan.BanDoKhoBau.level
                                                            + "\nCon có muốn đi theo không?",
                                                    "Đồng ý", "Từ chối");
                                        } else {
                                            this.createOtherMenu(player, ConstNpc.MENU_OPEN_DBKB,
                                                    "Đây là bản đồ kho báu x4 tnsm\nCác con cứ yên tâm lên đường\n"
                                                            + "Ở đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
                                                    "Chọn\ncấp độ", "Từ chối");
                                        }
                                    } else {
                                        this.npcChat(player, "Con phải có bang hội ta mới có thể cho con đi");
                                    }
                                    break;
                                case 3:
                                    if (player.diemdanhsk < 1) {
                                        short[] rdpet1 = new short[] { 1763, 1764, 1765, 1766, 1767, 1768 };
                                        Item _item = ItemService.gI()
                                                .createNewItem((short) rdpet1[Util.nextInt(rdpet1.length - 1)], 2);
                                        _item.itemOptions.add(new Item.ItemOption(30, Util.nextInt(1, 24)));
                                        _item.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 24)));
                                        InventoryServiceNew.gI().addItemBag(player, _item);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.getInstance().sendThongBao(player,
                                                "Chúc mừng bạn nhận được " + _item.template.name);
                                        ServerNotify.gI().notify(
                                                "Chúc mừng " + player.name + " nhận được " + _item.template.name + " ");
                                        player.diemdanhsk++;
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Đã nhận thưởng rồi");
                                    }

                                    break;
                                // case 3:
                                // if (player.inventory.gold >= 100_000_000) {
                                // Skill skill;
                                // for (int i = 0; i < player.playerSkill.skills.size(); i++) {
                                // skill = player.playerSkill.skills.get(i);
                                // skill.lastTimeUseThisSkill = System.currentTimeMillis() - (long)
                                // skill.coolDown;
                                // }
                                // Service.getInstance().sendTimeSkill(player);
                                // player.inventory.gold -= 100_000_000;
                                // Service.getInstance().sendMoney(player);
                                // } else {
                                // Service.getInstance().sendThongBao(player, "Bạn không đủ vàng");
                                // return;
                                // }
                                // break;
                                case 4:
                                    if (player.getSession().player.nPoint.power >= 80000000000L
                                            && player.playerTask.taskMain.id >= 21) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 174, -1, 552);
                                    } else if (player.clan == null) {
                                        this.npcChat(player, "Bạn chưa đủ 80 tỷ sức mạnh và xong nhiệm vụ 21 để vào");
                                        break;
                                    }
                                    break;

                            }
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.CHUC_NANG_BANG_HOI) {
                        switch (select) {
                            case 0:
                                Clan clan = player.clan;
                                if (clan != null) {
                                    ClanMember cm = clan.getClanMember((int) player.id);
                                    if (cm != null) {
                                        if (!clan.isLeader(player)) {
                                            Service.gI().sendThongBao(player, "Yêu cầu phải là bang chủ!");
                                            break;
                                        }
                                        if (clan.members.size() > 1) {
                                            Service.gI().sendThongBao(player,
                                                    "Yêu cầu bang hội chỉ còn một thành viên!");
                                            break;
                                        }
                                        NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DISSOLUTION_CLAN, -1,
                                                "Bạn có chắc chắn muốn giải tán bang hội?\n( Yêu cầu sẽ không thể hoàn tác )",
                                                "Đồng ý", "Từ chối!");
                                        break;
                                    }
                                    break;
                                }
                                Service.gI().sendThongBao(player, "Yêu câu tham gia bang hội");
                                break;
                            case 1:
                                if (player.clan != null) {
                                    if (!player.clan.isLeader(player)) {
                                        Service.gI().sendThongBao(player, "Yêu cầu phải là bang chủ!");
                                        break;
                                    }
                                    if (player.clan.level >= 0 && player.clan.level <= 10) {
                                        this.createOtherMenu(player, ConstNpc.CHUC_NANG_BANG_HOI2,
                                                "Bạn có muốn Nâng cấp lên " + (player.clan.maxMember + 1)
                                                        + " thành viên không?\n"
                                                        + "Cần 2000 Capsule Bang\n"
                                                        + "(Thu thập Capsule Bang bằng cách tiêu diệt quái tại Map Lãnh Địa Bang\n"
                                                        + "cùng các thành viên khác)",
                                                "Nâng cấp\n(20K Ruby)", "Từ chối");
                                    } else {
                                        Service.gI().sendThongBao(player, "Bang của bạn đã đạt cấp tối đa!");
                                        break;
                                    }
                                    break;
                                } else if (player.clan == null) {
                                    Service.gI().sendThongBao(player, "Yêu câu tham gia bang hội");
                                    break;
                                }
                                break;
                            case 2:
                                if (player.clan == null) {
                                    Service.gI().sendThongBao(player, "Yêu câu tham gia bang hội");
                                    break;
                                }
                                Input.gI().DonateCsbang(player);
                                break;
                            case 3:
                                if (player.getSession().player.nPoint.power >= 80000000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 153, -1, 432);
                                } else if (player.clan == null) {
                                    Service.gI().sendThongBao(player, "Yêu câu tham gia bang hội");
                                    break;
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 80 tỷ sức mạnh để vào");
                                }
                                break;
                            case 4:
                                if (player.getSession().player.nPoint.power >= 80000000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 153, -1, 432);
                                } else if (player.clan == null) {
                                    Service.gI().sendThongBao(player, "Yêu câu tham gia bang hội");
                                    break;
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 80 tỷ sức mạnh để vào");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.CHUC_NANG_BANG_HOI2) {
                        Clan clan = player.clan;
                        switch (select) {
                            case 0:
                                if (player.clan.capsuleClan >= 2000 && clan.isLeader(player)
                                        && player.inventory.ruby >= 20000) {
                                    player.clan.level += 1;
                                    player.clan.maxMember += 1;
                                    player.clan.capsuleClan -= 2000;
                                    player.inventory.subRuby(20000);
                                    player.clan.update();
                                    Service.gI().sendThongBao(player, "Yêu cầu nâng cấp bang hội thành công");
                                    break;
                                } else if (player.inventory.ruby < 20000) {
                                    Service.gI().sendThongBaoOK(player,
                                            "Bạn còn thiều " + (20000 - player.inventory.ruby) + " Hồng Ngọc");
                                    break;
                                } else if (player.clan.capsuleClan < 1000) {
                                    Service.gI().sendThongBaoOK(player, "Bang của bạn còn thiều "
                                            + (2000 - player.clan.capsuleClan) + " Capsule bang");
                                    break;
                                }
                        }
                    } // else if (player.iDMark.getIndexMenu() == ConstNpc.CHUYEN_SINH) {
                      // switch (select) {
                      // case 0:
                      // OpenPowerService.gI().chuyenSinh(player);
                      // break;
                      // case 1:
                      // if (player.capChuyenSinh >= 30) {
                      // player.capChuyenSinh -= 30;
                      // Item item = ItemService.gI().createNewItem((short) (Util.nextInt(1482,
                      // 1485)));
                      // item.itemOptions.add(new Item.ItemOption(0, Util.nextInt(500000, 1000000)));
                      // item.itemOptions.add(new Item.ItemOption(50, Util.nextInt(1000, 3000)));
                      // item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(1000, 3000)));
                      // item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(1000, 3000)));
                      // item.itemOptions.add(new Item.ItemOption(207, 0));
                      ////
                      // InventoryServiceNew.gI().addItemBag(player, item);
                      // Service.gI().sendThongBao(player, "Chúc Mừng Bạn Đổi Vật Phẩm Thành Công !");
                      // } else {
                      // Service.gI().sendThongBao(player, "Không đủ điểm, bạn còn " + (30 -
                      // player.capChuyenSinh) + " điểm nữa");
                      // }
                      // break;
                      // }
                      // }
                    else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_DBKB) {
                        switch (select) {
                            case 0:
                                if (player.isAdmin() || player.nPoint.power >= BanDoKhoBau.POWER_CAN_GO_TO_DBKB) {
                                    ChangeMapService.gI().changeMapInYard(player, 135, -1, 86);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(BanDoKhoBau.POWER_CAN_GO_TO_DBKB));
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_DBKB) {
                        switch (select) {
                            case 0:
                                if (player.isAdmin() || player.nPoint.power >= BanDoKhoBau.POWER_CAN_GO_TO_DBKB) {
                                    Input.gI().createFormChooseLevelBDKB(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(BanDoKhoBau.POWER_CAN_GO_TO_DBKB));
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCEPT_GO_TO_BDKB) {
                        switch (select) {
                            case 0:
                                BanDoKhoBauService.gI().openBanDoKhoBau(player,
                                        Byte.parseByte(String.valueOf(PLAYERID_OBJECT.get(player.id))));
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc truongLaoGuru(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    public static Npc vuaVegeta(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    public static Npc ongGohan_ongMoori_ongParagus(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "|4| NRO TCG \n"
                                        + "\nNhiệm vụ hiện tại của con: "
                                        + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).name
                                        + "\nGặp NPC Bò Mộng tại Tháp Karin để nhập GiftCode"
                                                .replaceAll("%1", player.gender == ConstPlayer.TRAI_DAT ? "Quy lão Kamê"
                                                        : player.gender == ConstPlayer.NAMEC ? "Trưởng lão Guru"
                                                                : "Vua Vegeta"),
                                "Tân Thủ", "Đổi Mật Khẩu", "Đóng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                this.createOtherMenu(player, 1701,
                                        "\n|5|Bạn Sẽ Được Next Nhiệm Vụ 12 Tới nhiệm Vụ 15"
                                                + "\nNhiệm vụ hiện tại của con: "
                                                + player.playerTask.taskMain.subTasks
                                                        .get(player.playerTask.taskMain.index).name,
                                        "Nhận Ngọc Xanh", "Hỗ Trợ Nhiệm Vụ", "Đóng");
                                break;
                            case 1:
                                this.createOtherMenu(player, 0, "|3|Kích Hoạt Tài Khoản\n"
                                        + "|2|Số tiền hiện tại : " + Util.format(player.getSession().vnd) + " VNĐ"
                                        + "\n|5|Trạng thái tài khoản : "
                                        + (player.getSession().actived == false ? "Chưa kích hoạt" : "Đã kích hoạt")
                                        + "\n|2|Trạng thái VIP : "
                                        + (player.vip == 1 ? "VIP"
                                                : player.vip == 2 ? "VIP2"
                                                        : player.vip == 3 ? "VIP3"
                                                                : player.vip == 4 ? "SVIP" : "Chưa Kích Hoạt")
                                        + (player.timevip > 0 ? "\n|5|Hạn còn : " + Util.msToThang(player.timevip)
                                                : ""),
                                        "Kích Hoạt\nVIP", "Điểm Danh", "Đóng");

                                break;
                            case 2:
                                Input.gI().createFormChangePassword(player);
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 0) {
                        switch (select) {
                            case 0:
                                this.createOtherMenu(player, 1, "|7|MUA THẺ VIP THÁNG\n"
                                        + "|2|Số tiền hiện tại : " + Util.format(player.getSession().vnd) + " VNĐ"
                                        + "\n|2|Trạng thái VIP : "
                                        + (player.vip == 1 ? "VIP"
                                                : player.vip == 2 ? "VIP2"
                                                        : player.vip == 3 ? "VIP3"
                                                                : player.vip == 4 ? "SVIP" : "Chưa Kích Hoạt")
                                        + (player.timevip > 0 ? "\n|5|Hạn còn : " + Util.msToThang(player.timevip)
                                                : ""),
                                        "Kích Hoạt\nVIP1\n20.000Đ", "Kích Hoạt\nVIP2\n30.000Đ",
                                        "Kích Hoạt\nVIP3\n50.000Đ", "Kích Hoạt\nSVIP\n70.000Đ", "Đóng");
                                break;
                            case 1:
                                if (player.diemdanh < 1) {
                                    int tv = 0;
                                    int hn = 0;
                                    switch (player.vip) {
                                        case 0:
                                            hn = 5000;
                                            break;
                                        case 1:
                                            tv = 10;
                                            hn = 10000;
                                            break;
                                        case 2:
                                            tv = 15;
                                            hn = 15000;
                                            break;
                                        case 3:
                                            tv = 20;
                                            hn = 20000;
                                            break;
                                        case 4:
                                            tv = 25;
                                            hn = 25000;
                                            break;
                                    }
                                    player.inventory.ruby += hn;
                                    Item thoivang = ItemService.gI().createNewItem((short) 457, tv);
                                    InventoryServiceNew.gI().addItemBag(player, thoivang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendMoney(player);
                                    player.diemdanh++;
                                    Service.getInstance().sendThongBao(player, "|7|Điểm danh thành công!\nNhận được "
                                            + tv + " Thỏi vàng và " + Util.format(hn) + " Hồng ngọc");
                                } else {
                                    this.npcChat(player, "Hôm nay đã nhận rồi mà !!!");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 1) {
                        switch (select) {
                            case 0:
                                this.createOtherMenu(player, 2, "|7|VIP1\n"
                                        + "|2|Quyền lợi đi kèm\n"
                                        + "|5|Nhận 5 Thỏi Vàng/ngày"
                                        + "\nNhận 5.000 Hồng Ngọc/ngày"
                                        + "\nTăng 20% TNSM"
                                        + "\nNhận 1 Thẻ Triệu Hồ Đệ"
                                        + "\n|2|Số tiền hiện tại : " + Util.format(player.getSession().vnd) + " VNĐ"
                                        + (player.vip == 1 ? "\n|7|Trạng thái VIP : VIP1"
                                                : player.vip == 2 ? "\n|7|Trạng thái VIP : VIP2"
                                                        : player.vip == 3 ? "\n|7|Trạng thái VIP : VIP3"
                                                                : player.vip == 4 ? "\n|7|Trạng thái VIP : SVIP" : "")
                                        + (player.timevip > 0 ? "\nHạn còn : " + Util.msToThang(player.timevip) : ""),
                                        "Kích Hoạt", "Đóng");
                                break;
                            case 1:
                                this.createOtherMenu(player, 3, "|7|VIP2\n"
                                        + "|2|Quyền lợi đi kèm\n"
                                        + "|5|Nhận 8 Thỏi Vàng/ngày"
                                        + "\nNhận 10.000 Hồng Ngọc/ngày"
                                        + "\nTăng 20% TNSM"
                                        + "\nNhận 2 Thẻ Triệu Hồ Đệ"
                                        + "\n|2|Số tiền hiện tại : " + Util.format(player.getSession().vnd) + " VNĐ"
                                        + (player.vip == 1 ? "\n|7|Trạng thái VIP : VIP1"
                                                : player.vip == 2 ? "\n|7|Trạng thái VIP : VIP2"
                                                        : player.vip == 3 ? "\n|7|Trạng thái VIP : VIP3"
                                                                : player.vip == 4 ? "\n|7|Trạng thái VIP : SVIP" : "")
                                        + (player.timevip > 0 ? "\nHạn còn : " + Util.msToThang(player.timevip) : ""),
                                        "Kích Hoạt", "Đóng");
                                break;
                            case 2:
                                this.createOtherMenu(player, 4, "|7|VIP3\n"
                                        + "|2|Quyền lợi đi kèm\n"
                                        + "|5|Nhận 12 Thỏi Vàng/ngày"
                                        + "\nNhận 15.000 Hồng Ngọc/ngày"
                                        + "\nTăng 20% TNSM"
                                        + "\nNhận 3 Thẻ Triệu Hồ Đệ"
                                        + "\n|2|Số tiền hiện tại : " + Util.format(player.getSession().vnd) + " VNĐ"
                                        + (player.vip == 1 ? "\n|7|Trạng thái VIP : VIP1"
                                                : player.vip == 2 ? "\n|7|Trạng thái VIP : VIP2"
                                                        : player.vip == 3 ? "\n|7|Trạng thái VIP : VIP3"
                                                                : player.vip == 4 ? "\n|7|Trạng thái VIP : SVIP" : "")
                                        + (player.timevip > 0 ? "\nHạn còn : " + Util.msToThang(player.timevip) : ""),
                                        "Kích Hoạt", "Đóng");
                                break;
                            case 3:
                                this.createOtherMenu(player, 5, "|7|SVIP\n"
                                        + "|2|Quyền lợi đi kèm\n"
                                        + "|5|Nhận 15 Thỏi Vàng/ngày"
                                        + "\nNhận 20.000 Hồng Ngọc/ngày"
                                        + "\nTăng 20% TNSM"
                                        + "\nNhận 4 Thẻ Triệu Hồ Đệ"
                                        + "\n|2|Số tiền hiện tại : " + Util.format(player.getSession().vnd) + " VNĐ"
                                        + (player.vip == 1 ? "\n|7|Trạng thái VIP : VIP1"
                                                : player.vip == 2 ? "\n|7|Trạng thái VIP : VIP2"
                                                        : player.vip == 3 ? "\n|7|Trạng thái VIP : VIP3"
                                                                : player.vip == 4 ? "\n|7|Trạng thái VIP : SVIP" : "")
                                        + (player.timevip > 0 ? "\nHạn còn : " + Util.msToThang(player.timevip) : ""),
                                        "Kích Hoạt", "Đóng");
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 2) {
                        switch (select) {
                            case 0:
                                if (player.vip >= 1) {
                                    this.npcChat(player, "|7|Bạn đang là thành viên "
                                            + (player.vip == 4 ? "SVIP" : "VIP" + player.vip) + " rồi");
                                    return;
                                }
                                if (player.getSession().vnd >= 20000) {
                                    Item thetrieuhoi = ItemService.gI().createNewItem((short) 1567);
                                    player.vip = 1;
                                    player.timevip = (System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 15))
                                            + (1000 * 60 * 60 * 24 * 16);
                                    PlayerDAO.subvnd(player, 20000);
                                    InventoryServiceNew.gI().addItemBag(player, thetrieuhoi);
                                    Service.gI().sendMoney(player);
                                    this.npcChat(player,
                                            "|6|Đã mở thành công\n|7|VIP1 Và Nhận Được 1 " + thetrieuhoi.template.name);
                                } else {
                                    this.npcChat(player, "Bạn không đủ tiền");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 3) {
                        switch (select) {
                            case 0:
                                if (player.vip >= 2) {
                                    this.npcChat(player, "|7|Bạn đang là thành viên "
                                            + (player.vip == 4 ? "SVIP" : "VIP" + player.vip) + " rồi");
                                    return;
                                }
                                if (player.getSession().vnd >= 30000) {
                                    Item thetrieuhoi = ItemService.gI().createNewItem((short) 1567);
                                    thetrieuhoi.quantity += 1;
                                    player.vip = 2;
                                    player.timevip = (System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 15))
                                            + (1000 * 60 * 60 * 24 * 16);
                                    PlayerDAO.subvnd(player, 30000);
                                    InventoryServiceNew.gI().addItemBag(player, thetrieuhoi);
                                    Service.gI().sendMoney(player);
                                    this.npcChat(player,
                                            "|6|Đã mở thành công\n|7|VIP2 Và Nhận Được 2 " + thetrieuhoi.template.name);
                                } else {
                                    this.npcChat(player, "Bạn không đủ tiền");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 4) {
                        switch (select) {
                            case 0:
                                if (player.vip >= 3) {
                                    this.npcChat(player, "|7|Bạn đang là thành viên "
                                            + (player.vip == 4 ? "SVIP" : "VIP" + player.vip) + " rồi");
                                    return;
                                }
                                if (player.getSession().vnd >= 50000) {
                                    Item thetrieuhoi = ItemService.gI().createNewItem((short) 1567);
                                    thetrieuhoi.quantity += 2;
                                    player.vip = 3;
                                    player.timevip = (System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 15))
                                            + (1000 * 60 * 60 * 24 * 16);
                                    PlayerDAO.subvnd(player, 50000);
                                    InventoryServiceNew.gI().addItemBag(player, thetrieuhoi);
                                    Service.gI().sendMoney(player);
                                    this.npcChat(player,
                                            "|6|Đã mở thành công\n|7|VIP3 Và Nhận Được 3 " + thetrieuhoi.template.name);
                                } else {
                                    this.npcChat(player, "Bạn không đủ tiền");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 5) {
                        switch (select) {
                            case 0:
                                if (player.vip >= 4) {
                                    this.npcChat(player, "|7|Bạn đang là thành viên "
                                            + (player.vip == 4 ? "SVIP" : "VIP" + player.vip) + " rồi");
                                    return;
                                }
                                if (player.getSession().vnd >= 70000) {
                                    Item thetrieuhoi = ItemService.gI().createNewItem((short) 1567);
                                    thetrieuhoi.quantity += 3;
                                    player.vip = 4;
                                    player.timevip = (System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 15))
                                            + (1000 * 60 * 60 * 24 * 16);
                                    PlayerDAO.subvnd(player, 70000);
                                    Service.gI().sendMoney(player);
                                    InventoryServiceNew.gI().addItemBag(player, thetrieuhoi);
                                    this.npcChat(player,
                                            "|6|Đã mở thành công\n|7|SVIP Và Nhận Được 4 " + thetrieuhoi.template.name);
                                } else {
                                    this.npcChat(player, "Bạn không đủ tiền");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.CONFIRM_ACTIVE) {
                        switch (select) {
                            case 0:
                                if (!player.getSession().actived) {
                                    if (player.getSession().vnd >= 10000) {
                                        player.getSession().actived = true;
                                        if (PlayerDAO.subvnd(player, 10000))
                                            ;
                                        Item vangnemay = ItemService.gI().createNewItem((short) 457);
                                        vangnemay.quantity += 24;
                                        player.inventory.ruby += 10000;
                                        InventoryServiceNew.gI().addItemBag(player, vangnemay);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.getInstance().sendMoney(player);
                                        Service.gI().sendThongBao(player,
                                                "|7|Kích hoạt thành công, bạn nhận được thêm 25 Thỏi Vàng và 10k Hồng Ngọc");
                                    } else {
                                        this.npcChat(player, "Không Đủ Tiền Mở Thành Viên...!");
                                    }
                                } else {
                                    this.npcChat(player, "Bạn đã mở thành viên rồi!");

                                }
                                break;
                            case 1:
                                this.npcChat(player, "Lần sau tiếp lúa cho ta nữa nha con!!!");
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 1701) {
                        switch (select) {
                            case 0:
                                if (player.inventory.gem > 500) {
                                    this.npcChat(player, "Tham Lam");
                                    break;
                                }
                                player.inventory.gem += 5000;
                                Service.getInstance().sendMoney(player);
                                Service.getInstance().sendThongBao(player, "Bạn vừa nhận được 5k ngọc xanh");
                                break;
                            // case 1:
                            // if (player.pet == null) {
                            // PetService.gI().createNormalPet(player);
                            // Service.gI().sendThongBao(player, "Bạn vừa nhận được đệ tử");
                            // } else {
                            // this.npcChat(player, "Bạn đã có rồi");
                            // }
                            // break;
                            case 1:
                                if (player.playerTask.taskMain.id >= 13) {
                                    if (player.playerTask.taskMain.id <= 15) {
                                        TaskService.gI().getTaskMainById(player, player.playerTask.taskMain.id = 15);
                                        TaskService.gI().sendNextTaskMain(player);
                                        this.npcChat(player,
                                                "Ta đã giúp con hoàn thành nhiệm vụ rồi mau đi trả nhiệm vụ");
                                    } else {
                                        this.npcChat(player, "Bạn Đã Tới Giới Hạn Next Nhiệm Vụ");
                                    }
                                } else {
                                    this.npcChat(player, "Bạn Chưa Tới Nhiệm Vụ Chỉ Định Để Có Thể Next");
                                }
                                break;
                        }
                    }
                }

            }

        };
    }

    public static Npc bulmaQK(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Cậu cần trang bị gì cứ đến chỗ tôi nhé", "Cửa\nhàng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:// Shop
                                if (player.gender == ConstPlayer.TRAI_DAT) {
                                    ShopServiceNew.gI().opendShop(player, "BUNMA", true);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                            "Xin lỗi cưng, chị chỉ bán đồ cho người Trái Đất", "Đóng");
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc nghiadia(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Nếu gan ngươi đủ lớn, hãy đi theo ta!", "Đồng Ý", "Từ Chối", "Cho kẹo\nhay\nhị ghẹo");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 199, -1, -1);
                                break;
                            case 2:
                                if (player.diemdanhsk < 1) {
                                    short[] rdpet1 = new short[] { 1757, 1758, 1759 };
                                    Item _item = ItemService.gI()
                                            .createNewItem((short) rdpet1[Util.nextInt(rdpet1.length - 1)], 2);
                                    _item.itemOptions.add(new Item.ItemOption(30, Util.nextInt(1, 24)));
                                    _item.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 24)));
                                    InventoryServiceNew.gI().addItemBag(player, _item);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.getInstance().sendThongBao(player,
                                            "Chúc mừng bạn nhận được " + _item.template.name);
                                    ServerNotify.gI().notify(
                                            "Chúc mừng " + player.name + " nhận được " + _item.template.name + " ");
                                    player.diemdanhsk++;
                                } else {
                                    Service.getInstance().sendThongBao(player, "Đã nhận thưởng rồi");
                                }

                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc dende(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (player.idNRNM != -1) {
                            if (player.zone.map.mapId == 7) {
                                this.createOtherMenu(player, 1,
                                        "Ồ, ngọc rồng namếc, bạn thật là may mắn\nnếu tìm đủ 7 viên sẽ được Rồng Thiêng Namếc ban cho điều ước",
                                        "Hướng\ndẫn\nGọi Rồng", "Gọi rồng", "Từ chối");
                            }
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Anh cần trang bị gì cứ đến chỗ em nhé", "Cửa\nhàng");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:// Shop
                                if (player.gender == ConstPlayer.NAMEC) {
                                    ShopServiceNew.gI().opendShop(player, "DENDE", true);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                            "Xin lỗi anh, em chỉ bán đồ cho dân tộc Namếc", "Đóng");
                                }
                                break;
                            // }
                            // } else if (player.iDMark.getIndexMenu() == 1) {
                            //
                            // if (player.clan == null) {
                            // Service.gI().sendThongBao(player, "Không có bang hội");
                            // return;
                            // }
                            // if (player.idNRNM != 353) {
                            // Service.gI().sendThongBao(player, "Anh phải có viên ngọc rồng Namếc 1 sao");
                            // return;
                            // }
                            //
                            // byte numChar = 0;
                            // for (Player pl : player.zone.getPlayers()) {
                            // if (pl.clan.id == player.clan.id && pl.id != player.id) {
                            // if (pl.idNRNM != -1) {
                            // numChar++;
                            // }
                            // }
                            // }
                            // if (numChar < 6) {
                            // Service.gI().sendThongBao(player, "Anh hãy tập hợp đủ 7 viên ngọc rồng nameck
                            // đi");
                            // return;
                            // }
                            //
                            // if (player.zone.map.mapId == 7 && player.idNRNM != -1) {
                            // if (player.idNRNM == 353) {
                            //// NgocRongNamecService.gI().tOpenNrNamec = System.currentTimeMillis() +
                            // 86400000;
                            //// NgocRongNamecService.gI().firstNrNamec = true;
                            //// NgocRongNamecService.gI().timeNrNamec = 0;
                            //// NgocRongNamecService.gI().doneDragonNamec();
                            //// NgocRongNamecService.gI().initNgocRongNamec((byte) 1);
                            //// NgocRongNamecService.gI().reInitNrNamec((long) 86399000);
                            // SummonDragon.gI().summonNamec(player);
                            // } else {
                            // Service.gI().sendThongBao(player, "Anh phải có viên ngọc rồng Namếc 1 sao");
                        }
                    }
                }
            }
            // }
            // };
        };
    }

    public static Npc appule(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi cần trang bị gì cứ đến chỗ ta nhé", "Cửa\nhàng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:// Shop
                                if (player.gender == ConstPlayer.XAYDA) {
                                    ShopServiceNew.gI().opendShop(player, "APPULE", true);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                            "Về hành tinh hạ đẳng của ngươi mà mua đồ cùi nhé. Tại đây ta chỉ bán đồ cho người Xayda thôi",
                                            "Đóng");
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc drDrief(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (this.mapId == 84) {
                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                pl.gender == ConstPlayer.TRAI_DAT ? "Đến\nTrái Đất"
                                        : pl.gender == ConstPlayer.NAMEC ? "Đến\nNamếc" : "Đến\nXayda");
                    } else if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                        if (pl.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(pl, this.avartar, "Hãy lên đường cứu đứa bé nhà tôi\n"
                                    + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                        } else {
                            this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                    "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                    "Đến\nNamếc", "Đến\nXayda", "Siêu thị");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 84) {
                        ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 24, -1, -1);
                    } else if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                break;
                            case 1:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                break;
                            case 2:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc cargo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                        if (pl.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(pl, this.avartar, "Hãy lên đường cứu đứa bé nhà tôi\n"
                                    + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                        } else {
                            this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                    "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                    "Đến\nTrái Đất", "Đến\nXayda", "Siêu thị");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                break;
                            case 1:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                break;
                            case 2:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc cui(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            private final int COST_FIND_BOSS = 50000000;

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (player.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(player, this.avartar, "Hãy lên đường cứu đứa bé nhà tôi\n"
                                    + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                        } else {
                            if (this.mapId == 19) {

                                int taskId = TaskService.gI().getIdTask(player);
                                switch (taskId) {
                                    case ConstTask.TASK_19_0:
                                        this.createOtherMenu(player, ConstNpc.MENU_FIND_KUKU,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến chỗ\nKuku\n(" + Util.numberToMoney(COST_FIND_BOSS) + " vàng)",
                                                "Đến Cold", "Đến\nNappa", "Từ chối");
                                        break;
                                    case ConstTask.TASK_19_1:
                                        this.createOtherMenu(player, ConstNpc.MENU_FIND_MAP_DAU_DINH,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến chỗ\nMập đầu đinh\n(" + Util.numberToMoney(COST_FIND_BOSS)
                                                        + " vàng)",
                                                "Đến Cold", "Đến\nNappa", "Từ chối");
                                        break;
                                    case ConstTask.TASK_19_2:
                                        this.createOtherMenu(player, ConstNpc.MENU_FIND_RAMBO,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến chỗ\nRambo\n(" + Util.numberToMoney(COST_FIND_BOSS) + " vàng)",
                                                "Đến Cold", "Đến\nNappa", "Từ chối");
                                        break;
                                    default:
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến Cold", "Đến\nNappa", "Từ chối");

                                        break;
                                }
                            } else if (this.mapId == 68) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "Ngươi muốn về Thành Phố Vegeta", "Đồng ý", "Từ chối");
                            } else if (player.getSession().player.nPoint.power >= 1500000000L) {
                                this.createOtherMenu(player, 2,
                                        "Tàu Vũ Trụ của ta có thể đưa cầu thủ đến hành tinh khác chỉ trong 3 giây. Cầu muốn đi đâu?",
                                        "Đến\nTrái Đất", "Đến\nNamếc", "Siêu thị");
                            } else {
                                this.createOtherMenu(player, 3,
                                        "Tàu Vũ Trụ của ta có thể đưa cầu thủ đến hành tinh khác chỉ trong 3 giây. Cầu muốn đi đâu?",
                                        "Đến\nTrái Đất", "Đến\nNamếc");
                            }
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 26) {
                        if (player.iDMark.getIndexMenu() == 2) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 3) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                    break;
                            }
                        }
                    }
                }
                if (this.mapId == 19) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                if (player.getSession().player.playerTask.taskMain.id >= 24) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                } else {
                                    this.npcChat(player, "Hãy hoàn thành những nhiệm vụ trước đó");
                                }
                                break;
                            case 1:
                                if (player.getSession().player.nPoint.power >= 2000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, -90);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 2 triệu sức mạnh để đi đến đây.");
                                    break;
                                }
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_KUKU) {
                        switch (select) {
                            case 0:
                                Boss boss = BossManager.gI().getBossById(BossID.KUKU);
                                if (boss != null && !boss.isDie()) {
                                    if (player.inventory.gold >= COST_FIND_BOSS && boss.zone != null
                                            && boss.zone.map != null) {
                                        Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId,
                                                boss.zone.zoneId);
                                        if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                                            player.inventory.gold -= COST_FIND_BOSS;
                                            ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x,
                                                    boss.location.y);
                                            Service.gI().sendMoney(player);
                                        } else {
                                            Service.gI().sendThongBao(player, "Khu vực đang full.");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold) + " vàng");
                                    }
                                    break;
                                }
                                Service.gI().sendThongBao(player, "Chết rồi ba...");
                                break;
                            case 1:
                                if (player.getSession().player.nPoint.power >= 80000000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 80 tỷ sức mạnh để vào");
                                }
                                break;
                            case 2:
                                if (player.getSession().player.nPoint.power >= 2000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, -90);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 2 triệu sức mạnh để đi đến đây.");
                                    break;
                                }
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_MAP_DAU_DINH) {
                        switch (select) {
                            case 0:
                                Boss boss = BossManager.gI().getBossById(BossID.MAP_DAU_DINH);
                                if (boss != null && !boss.isDie()) {
                                    if (player.inventory.gold >= COST_FIND_BOSS) {
                                        if (player != null && boss != null && boss.zone != null) {
                                            Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId,
                                                    boss.zone.zoneId);
                                            if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                                                player.inventory.gold -= COST_FIND_BOSS;
                                                ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x,
                                                        boss.location.y);
                                                Service.gI().sendMoney(player);
                                            } else {
                                                Service.gI().sendThongBao(player, "Khu vực đang full.");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                    + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold)
                                                    + " vàng");
                                        }
                                        break;
                                    }
                                }
                                Service.gI().sendThongBao(player, "Chết rồi ba...");
                                break;

                            case 1:
                                if (player.getSession().player.nPoint.power >= 80000000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 80 tỷ sức mạnh để vào");
                                }
                                break;
                            case 2:
                                if (player.getSession().player.nPoint.power >= 2000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, -90);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 2 triệu sức mạnh để đi đến đây.");
                                    break;
                                }
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_RAMBO) {
                        switch (select) {
                            case 0:
                                Boss boss = BossManager.gI().getBossById(BossID.RAMBO);
                                if (boss != null && !boss.isDie()) {
                                    if (player != null && boss.zone != null
                                            && player.inventory.gold >= COST_FIND_BOSS) {
                                        Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId,
                                                boss.zone.zoneId);
                                        if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                                            player.inventory.gold -= COST_FIND_BOSS;
                                            ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x,
                                                    boss.location.y);
                                            Service.gI().sendMoney(player);
                                        } else {
                                            Service.gI().sendThongBao(player, "Khu vực đang full.");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold) + " vàng");
                                    }
                                    break;
                                }
                                Service.gI().sendThongBao(player, "Chết rồi ba...");
                                break;
                            case 1:
                                if (player.getSession().player.nPoint.power >= 80000000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 80 tỷ sức mạnh để vào");
                                }
                                break;
                            case 2:
                                if (player.getSession().player.nPoint.power >= 2000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, -90);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 2 triệu sức mạnh để đi đến đây.");
                                    break;
                                }
                        }
                    }
                }
                if (this.mapId == 68) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 19, -1, 1100);
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc obito(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 197) {
                        createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Nro KhanhDTK\n" + //
                                        "H\u1ED3n ho\u00E0n ch\u1EE5c n\u0103m: " + player.BktDauLaDaiLuc[0]
                                        + "\nH\u1ED3n ho\u00E0n tr\u0103m n\u0103m: " + player.BktDauLaDaiLuc[1]
                                        + "\nH\u1ED3n ho\u00E0n ngh\u00ECn n\u0103m: " + player.BktDauLaDaiLuc[2]
                                        + "\nH\u1ED3n ho\u00E0n v\u1EA1n n\u0103m: " + player.BktDauLaDaiLuc[3]
                                        + "\nH\u1ED3n ho\u00E0n 10 v\u1EA1n n\u0103m: " + player.BktDauLaDaiLuc[4]
                                        + "\nH\u1ED3n ho\u00E0n tr\u0103m v\u1EA1n n\u0103m: "
                                        + player.BktDauLaDaiLuc[5]
                                        + "\nH\u1ED3n ho\u00E0n ngh\u00ECn v\u1EA1n n\u0103m: "
                                        + player.BktDauLaDaiLuc[6],
                                "Th\u00F4ng tin h\u1ED3n ho\u00E0n", "Th\u00F4ng tin h\u1ED3n c\u1ED1t\nSỡ hữu",
                                "Truy t\u00ECm h\u1ED3n c\u1ED1t", "N\u00E2ng h\u1ED3n c\u1ED1t",
                                "Thông tin\ncác loại hồn cốt");
                    } else {
                        createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Nh\u00ECn j m\u00E0 nh\u00ECn bi\u1EBFn sang ch\u1ED7 kh\u00E1c",
                                "Oke \u0111\u1EA1i ca");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (this.mapId) {
                        case 197:
                            if (select == 0) {
                                createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "Nro KhanhDTK\n" + //
                                                "H\u1ED3n ho\u00E0n ch\u1EE5c n\u0103m: T\u0103ng 100k hp, 10k dame."
                                                + "\nH\u1ED3n ho\u00E0n tr\u0103m n\u0103m: T\u0103ng 1tr hp, 100k dame."
                                                + "\nH\u1ED3n ho\u00E0n ngh\u00ECn n\u0103m: T\u0103ng 10tr hp, 1tr dame."
                                                + "\nH\u1ED3n ho\u00E0n v\u1EA1n n\u0103m: T\u0103ng 100tr hp, 10tr dame."
                                                + "\nH\u1ED3n ho\u00E0n 10 v\u1EA1n n\u0103m: T\u0103ng 1tỉ hp, 100tr dame."
                                                + "\nH\u1ED3n ho\u00E0n tr\u0103m v\u1EA1n n\u0103m: T\u0103ng 10t\u1EC9 hp, 1t\u1EC9 dame."
                                                + "\nH\u1ED3n ho\u00E0n ngh\u00ECn v\u1EA1n n\u0103m: T\u0103ng 100t\u1EC9 hp, 10t\u1EC9 dame.",
                                        "Th\u00F4ng tin h\u1ED3n ho\u00E0n", "Th\u00F4ng tin h\u1ED3n c\u1ED1t\nSỡ hữu",
                                        "Truy t\u00ECm h\u1ED3n c\u1ED1t", "N\u00E2ng h\u1ED3n c\u1ED1t",
                                        "Thông tin\ncác loại hồn cốt");
                            } else if (select == 1) {
                                String hcnhan = "";
                                if (player.BktDauLaDaiLuc[9] == 1) {
                                    hcnhan += player.BktNameHoncot(1) + ":\n";
                                    hcnhan += "+Tăng: " + player.BktDauLaDaiLuc[10] + " % chỉ số\n";
                                    hcnhan += "+giảm: "
                                            + (player.BktDauLaDaiLuc[10] / 3 >= 20 ? 20
                                                    : player.BktDauLaDaiLuc[10] / 3)
                                            + "% th\u1EDDi gian Skill đấm\n";
                                }
                                if (player.BktDauLaDaiLuc[11] == 1) {
                                    hcnhan += player.BktNameHoncot(2) + ":\n";
                                    hcnhan += "+Tăng: "
                                            + (player.BktDauLaDaiLuc[12] / 5 >= 20 ? 20
                                                    : player.BktDauLaDaiLuc[12] / 5)
                                            + "% Khả năng up các loại exp cao cấp của thế giới này.\n";
                                }
                                if (player.BktDauLaDaiLuc[13] == 1) {
                                    hcnhan += player.BktNameHoncot(3) + ":\n";
                                    hcnhan += "+Giảm: " + (player.BktDauLaDaiLuc[14] / 3 >= 80 ? 80
                                            : player.BktDauLaDaiLuc[14] / 3)
                                            + "% sát thương nhận.\n";
                                    hcnhan += "+Có tỉ lệ x2 dame.\n";
                                }
                                if (player.BktDauLaDaiLuc[15] == 1) {
                                    hcnhan += player.BktNameHoncot(4) + ":\n";
                                    hcnhan += "+Tăng: "
                                            + Util.getFormatNumber(player.BktDauLaDaiLuc[16] * 250000000d)
                                            + "dame.\n";
                                    hcnhan += "+Giảm: " + (player.BktDauLaDaiLuc[16] / 2 >= 90 ? 90
                                            : player.BktDauLaDaiLuc[16] / 2)
                                            + "% dame người ở gần.\n";
                                }
                                if (player.BktDauLaDaiLuc[17] == 1) {
                                    hcnhan += player.BktNameHoncot(5) + ":\n";
                                    hcnhan += "Tăng: "
                                            + Util.getFormatNumber(player.BktDauLaDaiLuc[18] * 1000000000d)
                                            + "Sinh lực.\n";
                                    hcnhan += "+hồi phục: " + (player.BktDauLaDaiLuc[18] / 3 >= 90 ? 90
                                            : player.BktDauLaDaiLuc[18] / 3)
                                            + "% Sinh lực sau 3s.\n";
                                }
                                if (player.BktDauLaDaiLuc[19] == 1) {
                                    hcnhan += player.BktNameHoncot(6) + ":\n";
                                    hcnhan += "+Đánh Sát thương chuẩn: "
                                            + Util.getFormatNumber(player.BktDauLaDaiLuc[20] * 100000000d)
                                            + "dame.\n";
                                }
                                createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "Nro KhanhDTK\n" + hcnhan,
                                        "Th\u00F4ng tin h\u1ED3n ho\u00E0n", "Th\u00F4ng tin h\u1ED3n c\u1ED1t\nSỡ hữu",
                                        "Truy t\u00ECm h\u1ED3n c\u1ED1t", "N\u00E2ng h\u1ED3n c\u1ED1t",
                                        "Thông tin\ncác loại hồn cốt");
                            } else if (select == 2) {
                                if (player.BktDauLaDaiLuc[7] == 0) {
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.BktTruytim, -1,
                                            "Nro KhanhDTK\n"
                                                    + "Để truy tìm Bình thường bạn cần 1tr exp Diệt Thần."
                                                    + "\nNếu đời bạn đen như chó hãy chọn 100% chỉ mất 500tr exp Diệt Thần",
                                            "Truy tìm", "Truy tìm 100%");
                                } else {
                                    String hcnhan = player
                                            .BktNameHoncot(Util.BKT(player.BktDauLaDaiLuc[7]))
                                            + "\n";
                                    if (player.BktDauLaDaiLuc[7] == 1) {
                                        hcnhan += "Tăng: " + player.BktDauLaDaiLuc[8] + " % chỉ số\n";
                                        hcnhan += "giảm: " + player.BktDauLaDaiLuc[8] / 3
                                                + " % thời gian Skill đấm, max 20%.\n";
                                    }
                                    if (player.BktDauLaDaiLuc[7] == 2) {
                                        hcnhan += "Tăng: " + player.BktDauLaDaiLuc[8] / 5
                                                + "% Khả năng up các loại exp cao cấp của thế giới này.\n";
                                    }
                                    if (player.BktDauLaDaiLuc[7] == 3) {
                                        hcnhan += "Giảm: " + player.BktDauLaDaiLuc[8] / 3
                                                + "% sát thương nhận.\n";
                                        hcnhan += "Có tỉ lệ x2 dame.\n";
                                    }
                                    if (player.BktDauLaDaiLuc[7] == 4) {
                                        hcnhan += "Tăng: " + player.BktDauLaDaiLuc[8] * 250000000L
                                                + "dame.\n";
                                        hcnhan += "Giảm: " + player.BktDauLaDaiLuc[8] / 2
                                                + "% dame người ở gần.\n";
                                    }
                                    if (player.BktDauLaDaiLuc[7] == 5) {
                                        hcnhan += "Tăng: " + player.BktDauLaDaiLuc[8] * 1000000000L
                                                + "Sinh lực.\n";
                                        hcnhan += "hồi phục: " + player.BktDauLaDaiLuc[8] / 3
                                                + "% Sinh lực sau 3s.\n";
                                    }
                                    if (player.BktDauLaDaiLuc[7] == 6) {
                                        hcnhan += "Đánh Sát thương chuẩn: " + player.BktDauLaDaiLuc[8] * 100000000L
                                                + "dame.\n";
                                    }
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.BktTruytim, -1,
                                            "Nro KhanhDTK\n"
                                                    + "Thông tin hồn cốt\n"
                                                    + hcnhan
                                                    + "\nHãy chọn theo lí trí của mình.",
                                            "đóng", "Hủy hồn cốt", "Hấp thụ hồn cốt");
                                }
                            } else if (select == 3) {
                                NpcService.gI().createMenuConMeo(player, ConstNpc.BktNCHC, -1,
                                        "Nro KhanhDTK\n"
                                                + "Cấp Diệt Thần ít nhất 200 để nâng cấp",
                                        player.BktNameHoncot(1), player.BktNameHoncot(2),
                                        player.BktNameHoncot(3),
                                        player.BktNameHoncot(4), player.BktNameHoncot(5),
                                        player.BktNameHoncot(6));
                            } else if (select == 4) {
                                Service.gI().sendThongBaoOK(player, "Nro KhanhDTK\nThông tin về hồn cốt.\n"
                                        + "B\u00E1t Chu M\u00E2u:\n-Tăng: % chỉ số\n"
                                        + "-giảm: % thời gian Skill đấm, max 20%.\nTinh Th\u1EA7n Ng\u01B0ng T\u1EE5 Chi Tr\u00ED Tu\u1EC7 \u0110\u1EA7u C\u1ED1t:\n"
                                        + "-Tăng: % Khả năng up các loại exp cao cấp của thế giới này.\nNhu C\u1ED1t Th\u1ECF H\u1EEFu T\u00ED C\u1ED1t:\n"
                                        + "-Giảm: % sát thương nhận.\n"
                                        + "-Có tỉ lệ x2 dame.\nTh\u00E1i Th\u1EA3n C\u1EF1 Vi\u00EAn:\n"
                                        + "-Tăng: dame.\n"
                                        + "-Giảm: % dame người ở gần.\nLam Ng\u00E2n Ho\u00E0ng:\n"
                                        + "Tăng: Sinh lực.\n"
                                        + "-hồi phục: % Sinh lực sau 3s.\nT\u00E0 Ma H\u1ED5 K\u00ECnh:\n"
                                        + "-Đánh Sát thương chuẩn: dame.");
                            }
                            break;
                        default:
                            Client.gI().kickSession(player.getSession());
                            break;
                    }
                }
            }
        };
    }

    public static Npc Tutien(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 197) {
                        if (player.Bkttutien[2] < 1) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "|7|Xin chào cư dân Ngọc rồng Alone\n" + "Cấp Tu Tiên Của Bạn Đang Là : "
                                            + player.Bkttutien[2] + "\n",
                                    "về nhà", "Xem thông tin\ntu tiên", "Mở thiên phú");
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "|7|Xin chào cư dân Ngọc rồng Alone\n" + "Cấp Tu Tiên Của Bạn Đang Là : "
                                            + player.Bkttutien[2] + "\n",
                                    "về nhà", "Xem thông tin\ntu tiên", "Tẩy thiên phú",
                                    "Hiệu ứng");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 197) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 354);
                                break;
                            case 1:
                                if (player.Bkttutien[2] < 1) {
                                    Service.gI().sendThongBao(player, "Con chưa mở thiên phú.");
                                    return;
                                }
                                String dktt;
                                if (player.Bkttutien[1] < 96) {
                                    dktt = "Điều kiện tấn thăng lên "
                                            + player.BktTuviTutien(Util.BKT(player.Bkttutien[1]) + 1);
                                    dktt += "\nExp tu tiền cần: "
                                            + player.BktDieukiencanhgioi(
                                                    Util.BKT(player.Bkttutien[1]));
                                    dktt += "\nTỉ lệ thành công: "
                                            + player.Bkttilecanhgioi(Util.BKT(player.Bkttutien[1]))
                                            + "%";
                                } else {
                                    dktt = "XIN CHÀO CHỦ NHÂN THIÊN GIỚI";
                                }
                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "Cảnh giới: "
                                                + player.BktTuviTutien(Util.BKT(player.Bkttutien[1]))
                                                + "\nExp Tu tiên: " + Util.getFormatNumber(player.Bkttutien[0])
                                                + "\nThiên phú: " + player.Bkttutien[2] + " Sao"
                                                + "\n" + dktt,
                                        "về nhà", "Xem thông tin\ntu tiên",
                                        "Tẩy thiên phú", "Hiệu ứng");
                                break;
                            case 2:
                                if (player.Captutien < 50) {
                                    Service.gI().sendThongBao(player, "cần ít nhất 50 cấp Tiên Kiếm");
                                    return;
                                }
                                if (player.Bkttutien[2] < 1) {
                                    if (player.Exptutien < 150000000) {
                                        Service.gI().sendThongBao(player, "cần ít nhất 150tr Exp Tiên Kiếm");
                                        return;
                                    }
                                    player.Exptutien -= 150000000;
                                    int tp = Util.nextInt(1, Util.nextInt(1, Util.nextInt(1, 8)));
                                    player.Bkttutien[2] = tp;
                                    Service.gI().sendThongBao(player,
                                            "Chúc mừng con con mở đc thiên phú:\n" + tp + " sao");
                                } else {
                                    if (player.Captutien < 100) {
                                        Service.gI().sendThongBao(player,
                                                "Muốn tẩy thiên phú Cấp Tiên Kiếm của con ít nhất phải 100.");
                                        return;
                                    }
                                    if (player.Exptutien < 75000000) {
                                        Service.gI().sendThongBao(player, "cần ít nhất 75tr Exp Tiên Kiếm");
                                        return;
                                    }
                                    if (player.Bkttutien[2] >= 50) {
                                        Service.gI().sendThongBao(player, "Con đã là tuyệt thế thiên tài");
                                        return;
                                    }
                                    player.Exptutien -= 75000000;
                                    if (Util.isTrue(30f, 100)) {
                                        player.Bkttutien[2]++;
                                        Service.gI().sendThongBao(player,
                                                "Chúc mừng con đã tẩy thiên phú thành công\ntừ: "
                                                        + (player.Bkttutien[2] - 1) + " sao lên: "
                                                        + player.Bkttutien[2] + " sao.");
                                    } else {
                                        Service.gI().sendThongBao(player, "Xin lỗi nhưng ta đã cố hết xức.");
                                    }
                                }
                                break;
                            case 3:
                                if (player.Bkttutien[2] < 1) {
                                    Service.gI().sendThongBao(player, "Con chưa mở thiên phú.");
                                    return;
                                }
                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "Cảnh giới: "
                                                + player.BktTuviTutien(Util.BKT(player.Bkttutien[1]))
                                                + "\nHp: "
                                                + player.BktHpKiGiaptutien(
                                                        Util.BKT(player.Bkttutien[1]))
                                                + "%\nKi: "
                                                + player.BktHpKiGiaptutien(
                                                        Util.BKT(player.Bkttutien[1]))
                                                + "%\nGiáp: "
                                                + player.BktHpKiGiaptutien(
                                                        Util.BKT(player.Bkttutien[1]))
                                                + "%\nDame: "
                                                + player.BktDametutien(Util.BKT(player.Bkttutien[1]))
                                                + "%\nHút hp,ki: "
                                                + player.BktDametutien(Util.BKT(player.Bkttutien[1]))
                                                + "%",
                                        "về nhà", "Xem thông tin\ntu tiên",
                                        "Tẩy thiên phú", "Hiệu ứng");
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc santa(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Xin chào, cửa hàng của ta chỉ bán những đồ hiếm, cậu có muốn xem không? ",
                            "Cửa hàng", "Cửa Hàng\nSự kiện", "Cửa Hàng\nSkill", "Cửa Hàng\n Vật Phẩm",
                            "Cho kẹo\nhay\nbị ghẹo", "Quy Đổi\n Thỏi Vàng", "Quy Đổi\n Hồng Ngọc", "Từ chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5 || this.mapId == 13 || this.mapId == 20) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: // shop
                                    ShopServiceNew.gI().opendShop(player, "SANTA", false);
                                    break;
                                case 6:
                                    Input.gI().createFormQDTV(player);
                                    break;
                                case 1: // shop
                                    ShopServiceNew.gI().opendShop(player, "Sk", false);
                                    break;
                                case 2: // shop
                                    ShopServiceNew.gI().opendShop(player, "skill", false);
                                    break;
                                case 3: // shop
                                    ShopServiceNew.gI().opendShop(player, "vp", false);
                                    break;

                                case 4:
                                    if (player.diemdanhsk < 1) {
                                        short[] rdpet1 = new short[] { 1763, 1764, 1765, 1766, 1767, 1768 };
                                        Item _item = ItemService.gI()
                                                .createNewItem((short) rdpet1[Util.nextInt(rdpet1.length - 1)], 2);
                                        _item.itemOptions.add(new Item.ItemOption(30, Util.nextInt(1, 24)));
                                        _item.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 24)));
                                        InventoryServiceNew.gI().addItemBag(player, _item);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.getInstance().sendThongBao(player,
                                                "Chúc mừng bạn nhận được " + _item.template.name);
                                        ServerNotify.gI().notify(
                                                "Chúc mừng " + player.name + " nhận được " + _item.template.name + " ");
                                        player.diemdanhsk++;
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Đã nhận thưởng rồi");
                                    }

                                    break;
                                case 5:
                                    this.createOtherMenu(player, ConstNpc.QUY_DOI,
                                            "|7|Bạn còn : " + player.inventory.gold + "\n"
                                                    + "Muốn quy đổi không",
                                            "Quy Đổi\n750m\n 1 Thỏi Vàng",
                                            "Quy Đổi\n7tỷ300m\n 10 Thỏi Vàng");
                                    break;
                            }

                        } else if (player.iDMark.getIndexMenu() == ConstNpc.QUY_DOI) {
                            PreparedStatement ps = null;
                            try (Connection con = GirlkunDB.getConnection();) {
                                switch (select) {
                                    case 0:
                                        Item thoivang = ItemService.gI().createNewItem((short) (457));
                                        thoivang.quantity += 1;
                                        if (player.inventory.gold < 750_000_000) {
                                            Service.gI().sendThongBao(player, "Bạn không có đủ 750m vàng");
                                            return;
                                        }
                                        player.inventory.gold -= 750_000_000;
                                        InventoryServiceNew.gI().addItemBag(player, thoivang);
                                        Service.gI().sendThongBao(player,
                                                "Bạn Nhận Được 1 " + thoivang.template.name + " Nhớ out game vô lạim nếu bị lỗi");
                                        break;
                                    case 1:
                                        Item thoivang10 = ItemService.gI().createNewItem((short) (457));
                                        thoivang10.quantity += 10;
                                        if (player.inventory.gold < 7_300_000_000L) {
                                            Service.gI().sendThongBao(player, "Bạn không có đủ 730m vàng");
                                            return;
                                        }
                                        player.inventory.gold -= 730_000_000;
                                        InventoryServiceNew.gI().addItemBag(player, thoivang10);
                                        Service.gI().sendThongBao(player,
                                                "Bạn Nhận Được 10 " + thoivang10.template.name + " Nhớ out game vô lạim nếu bị lỗi");

                                        break;
                                    // case 2:
                                    //     Item thoivanggg = ItemService.gI().createNewItem((short) (457));
                                    //     thoivanggg.quantity += 59;
                                    //     if (player.getSession().vnd < 30000) {
                                    //         Service.gI().sendThongBao(player, "Bạn không có đủ 30k coin");
                                    //         return;
                                    //     }
                                    //     player.getSession().vnd -= 30000;
                                    //     InventoryServiceNew.gI().addItemBag(player, thoivanggg);
                                    //     Service.gI().sendThongBao(player, "Bạn Nhận Được 60 " + thoivanggg.template.name
                                    //             + " Nhớ out game vô lại");
                                    //     break;
                                    // case 3:
                                    //     Item thoivangggg = ItemService.gI().createNewItem((short) (457));
                                    //     thoivangggg.quantity += 99;
                                    //     if (player.getSession().vnd < 50000) {
                                    //         Service.gI().sendThongBao(player, "Bạn không có đủ 50k coin");
                                    //         return;
                                    //     }
                                    //     player.getSession().vnd -= 50000;
                                    //     InventoryServiceNew.gI().addItemBag(player, thoivangggg);
                                    //     Service.gI().sendThongBao(player, "Bạn Nhận Được 1000 "
                                    //             + thoivangggg.template.name + " Nhớ out game vô lại");
                                    //     break;
                                    // case 4:
                                    //     Item thoivanggggg = ItemService.gI().createNewItem((short) (457));
                                    //     thoivanggggg.quantity += 199;
                                    //     if (player.getSession().vnd < 100000) {
                                    //         Service.gI().sendThongBao(player, "Bạn không có đủ 100k coin");
                                    //         return;
                                    //     }
                                    //     player.getSession().vnd -= 100000;
                                    //     InventoryServiceNew.gI().addItemBag(player, thoivanggggg);
                                    //     Service.gI().sendThongBao(player, "Bạn Nhận Được 200 "
                                    //             + thoivanggggg.template.name + " Nhớ out game vô lại");
                                    //     break;
                                }

                                ps = con.prepareStatement("update account set vnd = ? where id = ?");
                                ps.setInt(1, player.getSession().vnd);
                                ps.setInt(2, player.getSession().userId);
                                ps.executeUpdate();
                                ps.close();

                            } catch (Exception e) {
                                Logger.logException(NpcFactory.class, e, "Lỗi update vnd " + player.name);
                            } finally {
                                try {
                                    if (ps != null) {
                                        ps.close();
                                    }
                                } catch (SQLException ex) {
                                    System.out.println("Lỗi khi update vnd");

                                }
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc chichi(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Xin chào, cửa hàng của ta chỉ bán những đồ hiếm, cậu có muốn xem không? ",
                            "Cửa hàng", "Tới Khu Bắt Bọ", "Triệu Hồi\nĐệ Tử", "Từ chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5 || this.mapId == 13 || this.mapId == 20) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: // shop
                                    ShopServiceNew.gI().opendShop(player, "BAIBIEN", false);
                                    break;
                                case 2:
                                    this.createOtherMenu(player, ConstNpc.NpcThanThu,
                                            "|4|Thông Tin Đệ Tử 2\n"
                                                    + "|1|Name: " + player.TenThuTrieuHoi
                                                    + "\n|2|Level: " + player.TrieuHoiLevel + " ("
                                                    + (player.TrieuHoiExpThanThu * 100
                                                            / (3000000L + player.TrieuHoiLevel * 1500000L))
                                                    + "%)"
                                                    + "\n|2|Kinh nghiệm: " + Util.format(player.TrieuHoiExpThanThu)
                                                    + "\nCấp bậc: " + player.NameThanthu(player.TrieuHoiCapBac)
                                                    + "\n|4|Thức ăn: " + player.TrieuHoiThucAn + "%"
                                                    + "\nSức Đánh: " + Util.getFormatNumber(player.TrieuHoiDame)
                                                    + "\nMáu: " + Util.getFormatNumber(player.TrieuHoiHP)
                                                    + "\nKĩ năng: " + player.TrieuHoiKiNang(player.TrieuHoiCapBac),
                                            "Triệu Hồi", "Cập nhật", "Cho Ăn", "Đi theo", "Tấn công Player",
                                            "Tấn công mob",
                                            "Về nhà", "Tự động cho ăn", "Nâng Cấp\nĐệ tử 2");
                                    break;
                            }

                        } else if (player.iDMark.getIndexMenu() == ConstNpc.NpcThanThu) {
                            if (player.TrieuHoiCapBac != -1) {
                                switch (select) {
                                    case 0:
                                        if (!player.getSession().actived) {
                                            NpcService.gI().createTutorial(player, avartar,
                                                    "Truy cập Trang chủ để mở Thành viên");
                                            break;
                                        }
                                        // System.out.println(".confirmMenu() 1");
                                        // ThanhTichPlayer.SendThanhTich(player);
                                        Input.gI().TAOPET(player);
                                        break;
                                    case 1:
                                        Service.gI().showthanthu(player);
                                        break;
                                    case 2:
                                        if (player.inventory.ruby < 200) {
                                            Service.gI().sendThongBaoOK(player,
                                                    "Không đủ Hồng ngọc");
                                            return;
                                        }
                                        player.inventory.ruby -= 200;
                                        player.TrieuHoiThucAn++;
                                        if (player.TrieuHoiThucAn > 200) {
                                            player.TrieuHoiCapBac = -1;
                                            Service.gI().sendThongBaoOK(player,
                                                    "Bạn đã cho đệ tử ăn quá nhiều");
                                        } else {
                                            Service.gI().sendThongBao(player,
                                                    "|2|Thức ăn: " + player.TrieuHoiThucAn
                                                            + "%\n|1|Đã cho Đệ Tử 2 ăn\n|7|Lưu ý: khi cho quá 200% Đệ Tử 2 sẽ no quá mà chết");
                                        }
                                        Service.gI().showthanthu(player);
                                        break;
                                    case 3:
                                        player.TrieuHoipet.changeStatus(Thu_TrieuHoi.FOLLOW);
                                        break;
                                    case 4:
                                        player.TrieuHoipet.changeStatus(Thu_TrieuHoi.ATTACK_PLAYER);
                                        player.TrieuHoipet.effectSkill.removeSkillEffectWhenDie();
                                        Service.gI().sendThongBao(player, "|2|Mở khoá tàn sát cho Đệ Tử 2");
                                        break;
                                    case 5:
                                        player.TrieuHoipet.changeStatus(Thu_TrieuHoi.ATTACK_MOB);
                                        break;
                                    case 6:
                                        player.TrieuHoipet.changeStatus(Thu_TrieuHoi.GOHOME);
                                        break;
                                    case 7:
                                        if (player.trangthai == false) {
                                            player.trangthai = true;
                                            if (player.inventory.ruby < 200) {
                                                Service.gI().sendThongBao(player,
                                                        "|7|Không đủ Hồng ngọc");
                                                return;
                                            }
                                            player.inventory.ruby -= 200;
                                            player.TrieuHoiThucAn++;
                                            player.Autothucan = System.currentTimeMillis();
                                            if (player.TrieuHoiThucAn > 200) {
                                                player.TrieuHoiCapBac = -1;
                                                Service.gI().sendThongBao(player,
                                                        "|7|Vì cho Đệ Tử 2 ăn quá no nên Đệ Tử 2 đã bạo thể mà chết.");
                                            } else {
                                                Service.gI().sendThongBao(player,
                                                        "|2|Thức ăn Đệ Tử 2: " + player.TrieuHoiThucAn
                                                                + "%\n|1|Đã cho Đệ Tử 2 ăn\n|7|Lưu ý: khi cho quá 200% Đệ Tử 2 sẽ no quá mà chết");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player,
                                                    "|1|Đã dừng Auto cho Đệ Tử 2 ăn");
                                            player.trangthai = false;
                                        }
                                        break;
                                    case 8:
                                        if (player.TrieuHoiCapBac != -1 && player.TrieuHoiCapBac < 10) {
                                            NpcService.gI().createMenuConMeo(player, ConstNpc.DOT_PHA_THANTHU, 22630,
                                                    "|1|Nâng cấp đệ tử 2 "
                                                            + "\n|2|Cấp bậc hiện tại: "
                                                            + player.NameThanthu(player.TrieuHoiCapBac)
                                                            + "\n|2|Level: " + player.TrieuHoiLevel
                                                            + "\n|2|Kinh nghiệm: "
                                                            + Util.format(player.TrieuHoiExpThanThu)
                                                            + "\n|1| Yêu cầu Đệ Tử 2 đạt cấp 100"
                                                            + "\n|6|Cần: " + (player.TrieuHoiCapBac + 1) * 9 + " "
                                                            + player.DaDotpha(player.TrieuHoiCapBac)
                                                            + "\nĐể Nâng Cấp lên Cấp bậc "
                                                            + player.NameThanthu(player.TrieuHoiCapBac + 1)
                                                            + "\b|3|*Thành công: Cấp bậc Đệ Tử 2 nâng 1 bậc và Level trở về 0"
                                                            + "\b|3|*Thất bại: Trừ nguyên liệu nâng cấp"
                                                            + "\n|1|Tỉ lệ Thành công: "
                                                            + (100 - player.TrieuHoiCapBac * 10)
                                                            + "%",
                                                    "Nâng Cấp", "Đóng");
                                        } else {
                                            NpcService.gI().createMenuConMeo(player, ConstNpc.DOT_PHA_THANTHU, 12713,
                                                    "|7|Nâng Cấp Đệ Tử 2 "
                                                            + "\n\n|2|Cấp bậc hiện tại: "
                                                            + player.NameThanthu(player.TrieuHoiCapBac)
                                                            + "\n|7| Đệ Tử 2 của bạn đã đạt Cấp bậc Cao nhất",
                                                    "Đóng");
                                        }
                                        break;
                                }
                            } else {
                                Service.gI().sendThongBao(player, "|7|Bạn chưa có Đệ Tử 2 để sài tính năng này.");
                            }
                        }

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.DOT_PHA_THANTHU) {
                        if (player.TrieuHoiCapBac != -1) {

                            switch (select) {
                                case 0:
                                    Item linhthach = null;
                                    try {
                                        if (player.TrieuHoiCapBac != -1 && player.TrieuHoiCapBac >= 0
                                                && player.TrieuHoiCapBac < 4) {
                                            linhthach = InventoryServiceNew.gI().findItemBag(player, 1266);
                                        } else {
                                            linhthach = InventoryServiceNew.gI().findItemBag(player,
                                                    1269 - player.TrieuHoiCapBac);
                                        }
                                    } catch (Exception e) {
                                        System.out.println("vvvvv");
                                    }
                                    if (player.TrieuHoiCapBac != -1 && player.TrieuHoiLevel == 100
                                            && player.TrieuHoiCapBac < 10) {
                                        if (linhthach != null
                                                && linhthach.quantity >= (player.TrieuHoiCapBac + 1) * 9) {
                                            if (Util.isTrue(100 - player.TrieuHoiCapBac * 10, 100)) {
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, linhthach,
                                                        (player.TrieuHoiCapBac + 1) * 9);
                                                player.TrieuHoiLevel = 0;
                                                player.TrieuHoiExpThanThu = 0;
                                                player.TrieuHoiCapBac++;
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                Service.gI().sendThongBao(player,
                                                        "|2|HAHAHA Đệ Tử 2 đã tấn thăng "
                                                                + player.NameThanthu(player.TrieuHoiCapBac)
                                                                + " rồi\nTất cả quỳ xuống !!");
                                            } else {
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, linhthach,
                                                        (player.TrieuHoiCapBac + 1) * 9);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                Service.gI().sendThongBao(player,
                                                        "|7|Khốn khiếp, lại đột phá thất bại rồi");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player,
                                                    "|7| Chưa đủ " + player.DaDotpha(player.TrieuHoiCapBac));
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "|7| Yêu cầu Đệ Tử 2 đạt Cấp 100");
                                    }
                                    break;
                            }

                        }

                    }
                }
            }
        };
    }

    public static Npc thodaika(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            private Random random;
            private int quantity;

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Sự kiện Giáng Sinh NRO KhanhDTK :  Cần x99 Tất,vớ giáng sinh để đổi 1 hộp quà Giáng Sinh.\n"
                                    + "x10 Hộp quà giáng sinh + x99 tất sẽ đổi được một hào quang có tỉ lệ vĩnh viễn với chỉ số cực vip !!!.\n"
                                    + "Mèo lạc đang ở chỗ ta, nhưng nó đang bị ốm, Hãy giúp ta kiếm thức ăn cho mèo, Khi mèo khỏe.\n"
                                    + "Lúc đấy ta mới yên tâm giao nó cho ngươi.\n",
                            "Đổi hộp quà giáng sinh", "Đổi Hào Quang VIP", "Đổi Mèo");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: {

                                    Item botmi = null;

                                    try {

                                        botmi = InventoryServiceNew.gI().findItemBag(player, 649);

                                    } catch (Exception e) {
                                        // throw new RuntimeException(e);
                                    }
                                    if (botmi == null || botmi.quantity < 99) {
                                        this.npcChat(player, "Bạn không đủ nguyên liệu");

                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {

                                        InventoryServiceNew.gI().subQuantityItemsBag(player, botmi, 99);
                                        // InventoryServiceNew.gI().subQuantityItemsBag(player, dauxanh, 99);
                                        // InventoryServiceNew.gI().subQuantityItemsBag(player, trung, 99);
                                        // InventoryServiceNew.gI().subQuantityItemsBag(player, conga, 99);
                                        // player.inventory.ruby -= 1000;
                                        Service.getInstance().sendMoney(player);
                                        Item trungLinhThu = ItemService.gI().createNewItem((short) 648);
                                        InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Bạn nhận được 1 Hộp quà giáng sinh");
                                    }
                                    break;
                                }
                                case 1: {
                                    Item banh1nhan = null;
                                    Item banh2nhan = null;

                                    try {

                                        banh1nhan = InventoryServiceNew.gI().findItemBag(player, 648);
                                        banh2nhan = InventoryServiceNew.gI().findItemBag(player, 649);

                                    } catch (Exception e) {
                                        // throw new RuntimeException(e);
                                    }
                                    if (banh1nhan == null || banh1nhan.quantity < 10 || banh2nhan == null
                                            || banh2nhan.quantity < 99) {
                                        this.npcChat(player, "Bạn không đủ nguyên liệu");

                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {

                                        InventoryServiceNew.gI().subQuantityItemsBag(player, banh1nhan, 10);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, banh2nhan, 99);
                                        // player.inventory.ruby -= 10000;
                                        Random random = new Random();
                                        Item caitrang = ItemService.gI().createNewItem((short) 1264);
                                        caitrang.itemOptions
                                                .add(new Item.ItemOption(50, getRandomValue(10, 30, random)));
                                        caitrang.itemOptions
                                                .add(new Item.ItemOption(77, getRandomValue(10, 35, random)));
                                        caitrang.itemOptions
                                                .add(new Item.ItemOption(103, getRandomValue(10, 35, random)));
                                        caitrang.itemOptions
                                                .add(new Item.ItemOption(95, getRandomValue(2, 20, random)));
                                        caitrang.itemOptions
                                                .add(new Item.ItemOption(96, getRandomValue(2, 20, random)));
                                        caitrang.itemOptions.add(new Item.ItemOption(106, 0));
                                        if (Util.isTrue(99, 100)) {
                                            caitrang.itemOptions
                                                    .add(new Item.ItemOption(93, getRandomValue(1, 20, random)));
                                        }

                                        caitrang.itemOptions.add(new Item.ItemOption(30, 0));

                                        InventoryServiceNew.gI().addItemBag(player, caitrang);
                                        InventoryServiceNew.gI().sendItemBags(player);

                                        this.npcChat(player, "Bạn nhận được hào quang vip !");
                                    }
                                    break;

                                }
                                case 2: {
                                    Item giothucan = null;

                                    try {

                                        giothucan = InventoryServiceNew.gI().findItemBag(player, 2083);

                                    } catch (Exception e) {
                                        // throw new RuntimeException(e);
                                    }
                                    if (giothucan == null || giothucan.quantity < 99) {
                                        this.npcChat(player, "Bạn cần 99 giỏ thức ăn");

                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {

                                        InventoryServiceNew.gI().subQuantityItemsBag(player, giothucan, 99);

                                        // player.inventory.ruby -= 10000;
                                        Random random = new Random();
                                        Item caitrang = ItemService.gI().createNewItem((short) 1411);
                                        caitrang.itemOptions
                                                .add(new Item.ItemOption(50, getRandomValue(5, 40, random)));
                                        caitrang.itemOptions
                                                .add(new Item.ItemOption(95, getRandomValue(5, 30, random)));
                                        caitrang.itemOptions
                                                .add(new Item.ItemOption(96, getRandomValue(5, 30, random)));
                                        // caitrang.itemOptions.add(new Item.ItemOption(95, getRandomValue(2, 20,
                                        // random)));
                                        // caitrang.itemOptions.add(new Item.ItemOption(96, getRandomValue(2, 20,
                                        // random)));
                                        caitrang.itemOptions.add(new Item.ItemOption(162, 5));
                                        if (Util.isTrue(100, 100)) {
                                            caitrang.itemOptions
                                                    .add(new Item.ItemOption(93, getRandomValue(1, 7, random)));
                                        }

                                        caitrang.itemOptions.add(new Item.ItemOption(30, 0));

                                        InventoryServiceNew.gI().addItemBag(player, caitrang);
                                        InventoryServiceNew.gI().sendItemBags(player);

                                        this.npcChat(player, "Chúc mừng bạn đã tìm thấy bé mèo, mèo của bạn vẫn ổn !");
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc thoren(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 5) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "\b|7|Bạn cần đổi gì?\b|7|", "Đổi đồ\nHủy Diệt\nTrái Đất", "Đổi đồ\nHuy Diệt\nNamek",
                            "Đổi Đồ\nHủy Diệt\nxayda");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.BASE_MENU:
                                switch (select) {
                                    case 0:
                                        this.createOtherMenu(player, 1,
                                                "\b|7|Bạn muốn đổi 1 món đồ thần linh \nTrái đất cùng loại , 500tr vàng và x1 2 sao \n|6|Để đổi lấy",
                                                "Áo\nHúy Diệt", "Quần\nHúy Diệt", "Găng\nDúy Diệt", "Giày\nHúy Diệt",
                                                "Nhẫn\nHúy Diệt", "Thôi Khỏi");
                                        break;
                                    case 1:
                                        this.createOtherMenu(player, 2,
                                                "\b|7|Bạn muốn đổi 1 món đồ thần linh \nNamek cùng loại  , 500tr vàng và x1 sao \n|6|Để đổi lấy",
                                                "Áo\nHúy Diệt", "Quần\nHúy Diệt", "Găng\nDúy Diệt", "Giày\nHúy Diệt",
                                                "Nhẫn\nHúy Diệt", "Thôi Khỏi");
                                        break;
                                    case 2:
                                        this.createOtherMenu(player, 3,
                                                "\b|7|Bạn muốn đổi 1 món đồ thần linh \nXayda cùng loại  , 500tr vàng và x1 2 sao \n|6|Để đổi lấy ",
                                                "Áo\nHúy Diệt", "Quần\nHúy Diệt", "Găng\nDúy Diệt", "Giày\nHúy Diệt",
                                                "Nhẫn\nHúy Diệt", "Thôi Khỏi");
                                        break;

                                }
                                break;
                            case 1:
                            case 2:
                            case 3:
                                com.KhanhDTK.services.func.UpdateItem.StartUpdate(player,
                                        player.iDMark.getIndexMenu() - 1,
                                        select, this);
                        }
                    }
                }
            }

        };
    }

    public static Npc uron(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    ShopServiceNew.gI().opendShop(pl, "URON", false);
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    public static Npc baHatMit(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            String[] menuselect = new String[] {};

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 182) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "|7|Ngươi tìm ta có việc gì?\n"
                                        + "Ngươi Đang Có " + player.PointBoss + " Điểm Săn Boss",
                                "Nâng cấp Chân Thiên Tử", "Đóng");
                    } else if (this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Ép sao\ntrang bị", "Pha lê\nhóa\ntrang bị", "Đóng");
                    } else if (this.mapId == 121) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Về đảo\nrùa");
                    } else if (this.mapId == 199) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Quay về", "Chế Tạo", "Từ Chối");

                    } else if (this.mapId == 112) {
                        int goldchallenge = player.goldChallenge;
                        if (player.levelWoodChest == 0) {
                            menuselect = new String[] { "Thi đấu\n" + Util.numberToMoney(goldchallenge) + " vàng",
                                    "Về Đảo Kame" };
                        } else {
                            menuselect = new String[] { "Thi đấu\n" + Util.numberToMoney(goldchallenge) + " vàng",
                                    "Nhận thưởng\nRương cấp\n" + player.levelWoodChest, "Về Đảo Kame" };
                        }
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Võ Đài Bà Hạt Mít\nDiễn ra bất kể ngày đêm,ngày nghỉ ngày lễ\nPhần thưởng vô cùng quý giá\nNhanh chóng tham gia nào",
                                menuselect, "Từ chối");

                    } else {
                        if (player.luotNhanBuaMienPhi == 1) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Ngươi tìm ta có việc gì?",
                                    "Nhận Bùa Ngẫu Nhiên", "Cửa hàng\nBùa", "Nâng cấp\nVật phẩm",
                                    "Nâng cấp\nBông tai",
                                    "Nhập\nNgọc Rồng", "Sách Tuyệt Kỹ", "Phân Rã Đồ Thần");
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Ngươi tìm ta có việc gì?",
                                    "Cửa hàng\nBùa", "Nâng cấp\nVật phẩm",
                                    "Nâng cấp\nBông tai",
                                    "Nhập\nNgọc Rồng", "Sách Tuyệt Kỹ", "Phân Rã Đồ Thần");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 182) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_CHAN_MENH);
                                    break;
                            }
                        }
                        if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.NANG_CAP_CHAN_MENH:
                                    CombineServiceNew.gI().startCombine(player, select);
                                    break;
                            }
                        }
                    }
                    if (this.mapId == 199) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NHAP_KEO);
                                    break;
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 5, -1, 450, 288);
                                    break;
                            }
                        }
                        if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.NHAP_KEO:
                                    CombineServiceNew.gI().startCombine(player, select);
                                    break;
                            }
                        }
                    }

                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.EP_SAO_TRANG_BI);
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player,
                                            CombineServiceNew.PHA_LE_HOA_TRANG_BI);
                                    break;
                                case 2:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_SKH_VIP);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.EP_SAO_TRANG_BI:
                                case CombineServiceNew.PHA_LE_HOA_TRANG_BI:
                                case CombineServiceNew.NANG_CAP_SKH_VIP:
                                    CombineServiceNew.gI().startCombine(player, select);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DAP_DO_KICH_HOAT) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player, 0);
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DAP_DO_KICH_HOAT_THUONG) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player, 0);
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_DOI_SKH_VIP) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player, 0);
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_MEO) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player, 0);
                            }
                        }

                    } else if (this.mapId == 112) {
                        int goldchallenge = player.goldChallenge;
                        if (player.levelWoodChest == 0) {
                            switch (select) {
                                case 0:
                                    if (InventoryServiceNew.gI().finditemWoodChest(player)) {
                                        if (player.inventory.gold >= goldchallenge) {
                                            VoDaiService.gI().startChallenge(player);
                                            player.inventory.gold -= (goldchallenge);
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            player.goldChallenge += 2000000;
                                        } else {
                                            Service.getInstance().sendThongBao(player,
                                                    "Không đủ vàng, còn thiếu "
                                                            + Util.numberToMoney(goldchallenge - player.inventory.gold)
                                                            + " vàng");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hãy mở rương báu vật trước");
                                    }
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMap(player, 5, -1, 450, 288);
                                    break;
                            }
                        } else {
                            switch (select) {
                                case 0:
                                    if (InventoryServiceNew.gI().finditemWoodChest(player)) {
                                        if (player.inventory.gold >= goldchallenge) {
                                            VoDaiService.gI().startChallenge(player);
                                            player.inventory.gold -= (goldchallenge);
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            player.goldChallenge += 2000000;
                                        } else {
                                            Service.getInstance().sendThongBao(player,
                                                    "Không đủ vàng, còn thiếu "
                                                            + Util.numberToMoney(goldchallenge - player.inventory.gold)
                                                            + " vàng");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hãy mở rương báu vật trước");
                                    }
                                    break;
                                case 1:
                                    if (!player.receivedWoodChest) {
                                        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                            Item it = ItemService.gI().createNewItem((short) 1526);
                                            it.itemOptions.add(new Item.ItemOption(72, player.levelWoodChest));
                                            it.itemOptions.add(new Item.ItemOption(30, 0));
                                            it.createTime = System.currentTimeMillis();
                                            InventoryServiceNew.gI().addItemBag(player, it);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            player.receivedWoodChest = true;
                                            player.levelWoodChest = 0;
                                            Service.getInstance().sendThongBao(player, "Bạn nhận được rương ngọc rồng");
                                        } else {
                                            this.npcChat(player, "Hành trang đã đầy");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player,
                                                "Mỗi ngày chỉ có thể nhận rương báu 1 lần");
                                    }
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMap(player, 5, -1, 1030, 408);
                                    break;
                            }
                        }
                    } else if (this.mapId == 42 || this.mapId == 43 || this.mapId == 44 || this.mapId == 84) {
                        if (player.iDMark.isBaseMenu()) {
                            if (player.luotNhanBuaMienPhi == 1) {
                                switch (select) {
                                    case 0: // Ngẫu nhiên bùa 1h
                                        if (player.luotNhanBuaMienPhi == 1) {
                                            int idItem = Util.nextInt(213, 219);
                                            player.charms.addTimeCharms(idItem, 60);
                                            Item bua = ItemService.gI().createNewItem((short) idItem);
                                            Service.getInstance().sendThongBao(player,
                                                    "Bạn vừa nhận thưởng " + bua.getName());
                                            player.luotNhanBuaMienPhi = 0;
                                        } else {
                                            Service.getInstance().sendThongBao(player,
                                                    "Hôm nay bạn đã nhận bùa miễn phí rồi!!!");
                                        }
                                        break;
                                    case 1: // shop bùa
                                        createOtherMenu(player, ConstNpc.MENU_OPTION_SHOP_BUA,
                                                "Bùa của ta rất lợi hại, nhìn ngươi yếu đuối thế này, chắc muốn mua bùa để "
                                                        + "mạnh mẽ à, mua không ta bán cho, xài rồi lại thích cho mà xem.",
                                                "Bùa\n1 giờ", "Bùa\n8 giờ", "Bùa\n1 tháng", "Đóng");
                                        break;

                                    case 2:

                                        CombineServiceNew.gI().openTabCombine(player,
                                                CombineServiceNew.NANG_CAP_VAT_PHAM);
                                        break;
                                    case 3: // nâng cấp bông tai
                                        createOtherMenu(player, 211,
                                                "Ngươi muốn nâng bông tai à",
                                                "Nâng Bông Tai Cấp 2", "Mở Chỉ Số Bông Tai Cấp 2",
                                                "Nâng Bông Tai Cấp 3", "Mở Chỉ Số Bông Tai Cấp 3", "Đóng");
                                        break;
                                    case 4:
                                        CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NHAP_NGOC_RONG);
                                        break;
                                    case 5:
                                        createOtherMenu(player, ConstNpc.SACH_TUYET_KY, "Ta có thể giúp gì cho ngươi ?",
                                                "Đóng thành\nSách cũ",
                                                "Đổi Sách\nTuyệt kỹ",
                                                "Giám định\nSách",
                                                "Tẩy\nSách",
                                                "Nâng cấp\nSách\nTuyệt kỹ",
                                                "Hồi phục\nSách",
                                                "Phân rã\nSách");
                                        break;
                                    case 6:
                                        CombineServiceNew.gI().openTabCombine(player,
                                                CombineServiceNew.PHAN_RA_DO_THAN_LINH);
                                        break;

                                }
                            } else {
                                switch (select) {
                                    case 0: // shop bùa
                                        createOtherMenu(player, ConstNpc.MENU_OPTION_SHOP_BUA,
                                                "Bùa của ta rất lợi hại, nhìn ngươi yếu đuối thế này, chắc muốn mua bùa để "
                                                        + "mạnh mẽ à, mua không ta bán cho, xài rồi lại thích cho mà xem.",
                                                "Bùa\n1 giờ", "Bùa\n8 giờ", "Bùa\n1 tháng", "Đóng");
                                        break;

                                    case 1:

                                        CombineServiceNew.gI().openTabCombine(player,
                                                CombineServiceNew.NANG_CAP_VAT_PHAM);
                                        break;
                                    case 2: // nâng cấp bông tai
                                        createOtherMenu(player, 211,
                                                "Ngươi muốn nâng bông tai à",
                                                "Nâng Bông Tai Cấp 2", "Mở Chỉ Số Bông Tai Cấp 2",
                                                "Nâng Bông Tai Cấp 3", "Mở Chỉ Số Bông Tai Cấp 3", "Đóng");
                                        break;
                                    case 3:
                                        CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NHAP_NGOC_RONG);
                                        break;
                                    case 4:
                                        createOtherMenu(player, ConstNpc.SACH_TUYET_KY, "Ta có thể giúp gì cho ngươi ?",
                                                "Đóng thành\nSách cũ",
                                                "Đổi Sách\nTuyệt kỹ",
                                                "Giám định\nSách",
                                                "Tẩy\nSách",
                                                "Nâng cấp\nSách\nTuyệt kỹ",
                                                "Hồi phục\nSách",
                                                "Phân rã\nSách");
                                        break;
                                    case 5:
                                        CombineServiceNew.gI().openTabCombine(player,
                                                CombineServiceNew.PHAN_RA_DO_THAN_LINH);
                                        break;

                                }
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.SACH_TUYET_KY) {
                            switch (select) {
                                case 0:
                                    Item trangSachCu = InventoryServiceNew.gI().findItemBag(player, 1393);

                                    Item biaSach = InventoryServiceNew.gI().findItemBag(player, 1389);
                                    if ((trangSachCu != null && trangSachCu.quantity >= 99)
                                            && (biaSach != null && biaSach.quantity >= 1)) {
                                        createOtherMenu(player, ConstNpc.DONG_THANH_SACH_CU,
                                                "|2|Chế tạo Cuốn sách cũ\n"
                                                        + "|1|Trang sách cũ " + trangSachCu.quantity + "/99\n"
                                                        + "Bìa sách " + biaSach.quantity + "/1\n"
                                                        + "Tỉ lệ thành công: 20%\n"
                                                        + "Thất bại mất 99 trang sách và 1 bìa sách",
                                                "Đồng ý", "Từ chối");
                                        break;
                                    } else {
                                        String NpcSay = "|2|Chế tạo Cuốn sách cũ\n";
                                        if (trangSachCu == null) {
                                            NpcSay += "|7|Trang sách cũ " + "0/99\n";
                                        } else {
                                            NpcSay += "|1|Trang sách cũ " + trangSachCu.quantity + "/99\n";
                                        }
                                        if (biaSach == null) {
                                            NpcSay += "|7|Bìa sách " + "0/1\n";
                                        } else {
                                            NpcSay += "|1|Bìa sách " + biaSach.quantity + "/1\n";
                                        }

                                        NpcSay += "|7|Tỉ lệ thành công: 20%\n";
                                        NpcSay += "|7|Thất bại mất 99 trang sách và 1 bìa sách";
                                        createOtherMenu(player, ConstNpc.DONG_THANH_SACH_CU_2,
                                                NpcSay, "Từ chối");
                                        break;
                                    }
                                case 1:
                                    Item cuonSachCu = InventoryServiceNew.gI().findItemBag(player, 1392);
                                    Item kimBam = InventoryServiceNew.gI().findItemBag(player, 1390);

                                    if ((cuonSachCu != null && cuonSachCu.quantity >= 10)
                                            && (kimBam != null && kimBam.quantity >= 1)) {
                                        createOtherMenu(player, ConstNpc.DOI_SACH_TUYET_KY,
                                                "|2|Đổi sách tuyệt kỹ 1\n"
                                                        + "|1|Cuốn sách cũ " + cuonSachCu.quantity + "/10\n"
                                                        + "Kìm bấm giấy " + kimBam.quantity + "/1\n"
                                                        + "Tỉ lệ thành công: 100%\n",
                                                "Đồng ý", "Từ chối");
                                        break;
                                    } else {
                                        String NpcSay = "|2|Đổi sách Tuyệt kỹ 1\n";
                                        if (cuonSachCu == null) {
                                            NpcSay += "|7|Cuốn sách cũ " + "0/10\n";
                                        } else {
                                            NpcSay += "|1|Cuốn sách cũ " + cuonSachCu.quantity + "/10\n";
                                        }
                                        if (kimBam == null) {
                                            NpcSay += "|7|Kìm bấm giấy " + "0/1\n";
                                        } else {
                                            NpcSay += "|1|Kìm bấm giấy " + kimBam.quantity + "/1\n";
                                        }
                                        NpcSay += "|7|Tỉ lệ thành công: 20%\n";
                                        createOtherMenu(player, ConstNpc.DOI_SACH_TUYET_KY_2,
                                                NpcSay, "Từ chối");
                                    }
                                    break;
                                case 2:// giám định sách
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.GIAM_DINH_SACH);
                                    break;
                                case 3:// tẩy sách
                                    CombineServiceNew.gI().openTabCombine(player,
                                            CombineServiceNew.TAY_SACH);
                                    break;
                                case 4:// nâng cấp sách
                                    CombineServiceNew.gI().openTabCombine(player,
                                            CombineServiceNew.NANG_CAP_SACH_TUYET_KY);
                                    break;
                                case 5:// phục hồi sách
                                    CombineServiceNew.gI().openTabCombine(player,
                                            CombineServiceNew.PHUC_HOI_SACH);
                                    break;
                                case 6:// phân rã sách
                                    CombineServiceNew.gI().openTabCombine(player,
                                            CombineServiceNew.PHAN_RA_SACH);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.DOI_SACH_TUYET_KY) {
                            switch (select) {
                                case 0:
                                    Item cuonSachCu = InventoryServiceNew.gI().findItemBag(player, 1392);
                                    Item kimBam = InventoryServiceNew.gI().findItemBag(player, 1390);

                                    short baseValue = 1385;
                                    short genderModifier = (player.gender == 0) ? -2
                                            : ((player.gender == 2) ? 2 : (short) 0);

                                    Item sachTuyetKy = ItemService.gI()
                                            .createNewItem((short) (baseValue + genderModifier));

                                    if (Util.isTrue(20, 100)) {

                                        sachTuyetKy.itemOptions.add(new Item.ItemOption(221, 0));
                                        sachTuyetKy.itemOptions.add(new Item.ItemOption(21, 40));
                                        sachTuyetKy.itemOptions.add(new Item.ItemOption(30, 0));
                                        sachTuyetKy.itemOptions.add(new Item.ItemOption(87, 1));
                                        sachTuyetKy.itemOptions.add(new Item.ItemOption(218, 5));
                                        sachTuyetKy.itemOptions.add(new Item.ItemOption(219, 1000));
                                        try { // send effect susscess
                                            Message msg = new Message(-81);
                                            msg.writer().writeByte(0);
                                            msg.writer().writeUTF("test");
                                            msg.writer().writeUTF("test");
                                            msg.writer().writeShort(tempId);
                                            player.sendMessage(msg);
                                            msg.cleanup();
                                            msg = new Message(-81);
                                            msg.writer().writeByte(1);
                                            msg.writer().writeByte(2);
                                            msg.writer()
                                                    .writeByte(InventoryServiceNew.gI().getIndexBag(player, kimBam));
                                            msg.writer().writeByte(
                                                    InventoryServiceNew.gI().getIndexBag(player, cuonSachCu));
                                            player.sendMessage(msg);
                                            msg.cleanup();
                                            msg = new Message(-81);
                                            msg.writer().writeByte(7);
                                            msg.writer().writeShort(sachTuyetKy.template.iconID);
                                            msg.writer().writeShort(-1);
                                            msg.writer().writeShort(-1);
                                            msg.writer().writeShort(-1);
                                            player.sendMessage(msg);
                                            msg.cleanup();
                                        } catch (Exception e) {
                                            System.out.println("lỗi 4");
                                        }
                                        InventoryServiceNew.gI().addItemBag(player, sachTuyetKy);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, cuonSachCu, 10);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, kimBam, 1);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        npcChat(player, "|7|Thành Công");
                                        return;
                                    } else {
                                        try { // send effect faile
                                            Message msg = new Message(-81);
                                            msg.writer().writeByte(0);
                                            msg.writer().writeUTF("test");
                                            msg.writer().writeUTF("test");
                                            msg.writer().writeShort(tempId);
                                            player.sendMessage(msg);
                                            msg.cleanup();
                                            msg = new Message(-81);
                                            msg.writer().writeByte(1);
                                            msg.writer().writeByte(2);
                                            msg.writer()
                                                    .writeByte(InventoryServiceNew.gI().getIndexBag(player, kimBam));
                                            msg.writer().writeByte(
                                                    InventoryServiceNew.gI().getIndexBag(player, cuonSachCu));
                                            player.sendMessage(msg);
                                            msg.cleanup();
                                            msg = new Message(-81);
                                            msg.writer().writeByte(8);
                                            msg.writer().writeShort(-1);
                                            msg.writer().writeShort(-1);
                                            msg.writer().writeShort(-1);
                                            player.sendMessage(msg);
                                            msg.cleanup();
                                        } catch (Exception e) {
                                            System.out.println("lỗi 3");
                                        }
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, cuonSachCu, 5);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, kimBam, 1);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        npcChat(player, "|7|Thất Bại");
                                    }
                                    return;
                                case 1:
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.DONG_THANH_SACH_CU) {
                            switch (select) {
                                case 0:

                                    Item trangSachCu = InventoryServiceNew.gI().findItemBag(player, 1393);
                                    Item biaSach = InventoryServiceNew.gI().findItemBag(player, 1389);
                                    Item cuonSachCu = ItemService.gI().createNewItem((short) 1392);
                                    if (Util.isTrue(20, 100)) {
                                        cuonSachCu.itemOptions.add(new Item.ItemOption(30, 0));

                                        try { // send effect susscess
                                            Message msg = new Message(-81);
                                            msg.writer().writeByte(0);
                                            msg.writer().writeUTF("test");
                                            msg.writer().writeUTF("test");
                                            msg.writer().writeShort(tempId);
                                            player.sendMessage(msg);
                                            msg.cleanup();

                                            msg = new Message(-81);
                                            msg.writer().writeByte(1);
                                            msg.writer().writeByte(2);
                                            msg.writer().writeByte(
                                                    InventoryServiceNew.gI().getIndexBag(player, trangSachCu));
                                            msg.writer()
                                                    .writeByte(InventoryServiceNew.gI().getIndexBag(player, biaSach));
                                            player.sendMessage(msg);
                                            msg.cleanup();

                                            msg = new Message(-81);
                                            msg.writer().writeByte(7);
                                            msg.writer().writeShort(cuonSachCu.template.iconID);
                                            msg.writer().writeShort(-1);
                                            msg.writer().writeShort(-1);
                                            msg.writer().writeShort(-1);
                                            player.sendMessage(msg);
                                            msg.cleanup();

                                        } catch (Exception e) {
                                            System.out.println("lỗi 1");
                                        }

                                        InventoryServiceNew.gI().addItemList(player.inventory.itemsBag, cuonSachCu);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, trangSachCu, 9999);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, biaSach, 1);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        return;
                                    } else {
                                        try { // send effect faile
                                            Message msg = new Message(-81);
                                            msg.writer().writeByte(0);
                                            msg.writer().writeUTF("test");
                                            msg.writer().writeUTF("test");
                                            msg.writer().writeShort(tempId);
                                            player.sendMessage(msg);
                                            msg.cleanup();
                                            msg = new Message(-81);
                                            msg.writer().writeByte(1);
                                            msg.writer().writeByte(2);
                                            msg.writer()
                                                    .writeByte(InventoryServiceNew.gI().getIndexBag(player, biaSach));
                                            msg.writer().writeByte(
                                                    InventoryServiceNew.gI().getIndexBag(player, trangSachCu));
                                            player.sendMessage(msg);
                                            msg.cleanup();
                                            msg = new Message(-81);
                                            msg.writer().writeByte(8);
                                            msg.writer().writeShort(-1);
                                            msg.writer().writeShort(-1);
                                            msg.writer().writeShort(-1);
                                            player.sendMessage(msg);
                                            msg.cleanup();
                                        } catch (Exception e) {
                                            System.out.println("lỗi 2");
                                        }
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, trangSachCu, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, biaSach, 1);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                    }
                                    return;
                                case 1:
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_SHOP_BUA) {
                            switch (select) {
                                case 0:
                                    ShopServiceNew.gI().opendShop(player, "BUA_1H", true);
                                    break;
                                case 1:
                                    ShopServiceNew.gI().opendShop(player, "BUA_8H", true);
                                    break;
                                case 2:
                                    ShopServiceNew.gI().opendShop(player, "BUA_1M", true);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 211) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI);
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_CHI_SO_BONG_TAI);
                                    break;
                                case 2:
                                    CombineServiceNew.gI().openTabCombine(player,
                                            CombineServiceNew.NANG_CAP_BONG_TAI_CAP3);
                                    break;
                                case 3:
                                    CombineServiceNew.gI().openTabCombine(player,
                                            CombineServiceNew.MO_CHI_SO_BONG_TAI_CAP3);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.NANG_CAP_VAT_PHAM:
                                case CombineServiceNew.NANG_CAP_BONG_TAI:
                                case CombineServiceNew.MO_CHI_SO_BONG_TAI:
                                case CombineServiceNew.NANG_CAP_BONG_TAI_CAP3:
                                case CombineServiceNew.MO_CHI_SO_BONG_TAI_CAP3:
                                case CombineServiceNew.NHAP_NGOC_RONG:
                                case CombineServiceNew.GIAM_DINH_SACH:
                                case CombineServiceNew.TAY_SACH:
                                case CombineServiceNew.NANG_CAP_SACH_TUYET_KY:
                                case CombineServiceNew.PHUC_HOI_SACH:
                                case CombineServiceNew.PHAN_RA_SACH:
                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player, 0);
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHAN_RA_DO_THAN_LINH) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player, 0);
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_CAP_DO_TS) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player, 0);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc itachi(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "|7|Các Ngươi Hãy Tích Các Vật Phẩm Dưới Đây Và Mang Đến Cho Ta Để Đổi Những Phần Quà Hấp Dẫn.\n"
                                        + "Tất Cả Vật Phẩm Đều Up Tại Nam Kame\n"
                                        + "- VỎ ỐC x99 đổi được 1 món ngẫu nhiên:\n"
                                        + "+ Hoa hồng vàng (hạn sử dụng)\n"
                                        + "+ Hoa hồng đỏ (hạn sử dụng)\n"
                                        + "- SÒ x99 đổi được 1 món ngẫu nhiên:\n"
                                        + "+ Cây nắp ấm (hạn sử dụng)\n"
                                        + "+ Cá heo (hạn sử dụng)\n"
                                        + "- CUA x99 đổi được 1 món ngẫu nhiên:\n"
                                        + "+ Phượng Hoàng Lửa (hạn sử dụng)\n"
                                        + "+ Rùa phun lửa (hạn sử dụng)\n"
                                        + "- SAO BIỂN x99 đổi được 1 món ngẫu nhiên:\n"
                                        + "+ Bóng bóng Heo Hồng (hạn sử dụng)\n"
                                        + "+ Bong bóng Vịt Vàng (hạn sử dụng)\n"
                                        + "+ Cải trang Android (hạn sử dụng)",
                                "Đổi Vỏ Ốc", "Đổi Vỏ Sò", "Đổi Cua", "Đổi Sao Biển", "Đóng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    Item vooc = null;

                                    try {
                                        vooc = InventoryServiceNew.gI().findItemBag(player, 695);
                                    } catch (Exception e) {
                                    }
                                    if (vooc == null || vooc.quantity < 99) {
                                        this.npcChat(player, "Bạn Không Có Vật Phẩm Nào");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành Trang Của Bạn Không Đủ Chỗ Trống");
                                    } else {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, vooc, 99);
                                        Item bonghoavang = ItemService.gI().createNewItem((short) 954);
                                        Item bonghoado = ItemService.gI().createNewItem((short) 955);
                                        bonghoavang.itemOptions.add(new ItemOption(50, 15));
                                        bonghoavang.itemOptions.add(new ItemOption(77, 15));
                                        bonghoavang.itemOptions.add(new ItemOption(103, 15));
                                        bonghoavang.itemOptions.add(new ItemOption(93, 3));
                                        bonghoavang.itemOptions.add(new ItemOption(174, 2024));
                                        bonghoado.itemOptions.add(new ItemOption(174, 2024));
                                        bonghoado.itemOptions.add(new ItemOption(50, 15));
                                        bonghoado.itemOptions.add(new ItemOption(77, 15));
                                        bonghoado.itemOptions.add(new ItemOption(103, 15));
                                        bonghoado.itemOptions.add(new ItemOption(93, 3));
                                        InventoryServiceNew.gI().addItemBag(player, bonghoavang);
                                        InventoryServiceNew.gI().addItemBag(player, bonghoado);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "Bạn nhận được " + bonghoavang.template.name
                                                + " Và " + bonghoado.template.name);
                                    }
                                    break;
                                case 1:
                                    Item voso = null;

                                    try {
                                        voso = InventoryServiceNew.gI().findItemBag(player, 696);
                                    } catch (Exception e) {
                                    }
                                    if (voso == null || voso.quantity < 99) {
                                        this.npcChat(player, "Bạn Không Có Vật Phẩm Nào");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành Trang Của Bạn Không Đủ Chỗ Trống");
                                    } else {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, voso, 99);
                                        Item caheo = ItemService.gI().createNewItem((short) 996);
                                        Item condieu = ItemService.gI().createNewItem((short) 997);
                                        caheo.itemOptions.add(new ItemOption(50, 15));
                                        caheo.itemOptions.add(new ItemOption(77, 15));
                                        caheo.itemOptions.add(new ItemOption(103, 15));
                                        caheo.itemOptions.add(new ItemOption(93, 3));
                                        caheo.itemOptions.add(new ItemOption(174, 2024));
                                        condieu.itemOptions.add(new ItemOption(174, 2024));
                                        condieu.itemOptions.add(new ItemOption(50, 15));
                                        condieu.itemOptions.add(new ItemOption(77, 15));
                                        condieu.itemOptions.add(new ItemOption(103, 15));
                                        condieu.itemOptions.add(new ItemOption(93, 3));
                                        InventoryServiceNew.gI().addItemBag(player, caheo);
                                        InventoryServiceNew.gI().addItemBag(player, condieu);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "Bạn nhận được " + caheo.template.name
                                                + " Và " + condieu.template.name);
                                    }
                                    break;
                                case 2:
                                    Item concua = null;

                                    try {
                                        concua = InventoryServiceNew.gI().findItemBag(player, 697);
                                    } catch (Exception e) {
                                    }
                                    if (concua == null || concua.quantity < 99) {
                                        this.npcChat(player, "Bạn Không Có Vật Phẩm Nào");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành Trang Của Bạn Không Đủ Chỗ Trống");
                                    } else {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, concua, 99);
                                        Item daisenhong = ItemService.gI().createNewItem((short) 1445);
                                        Item daisenvang = ItemService.gI().createNewItem((short) 1446);
                                        daisenhong.itemOptions.add(new ItemOption(50, 20));
                                        daisenhong.itemOptions.add(new ItemOption(77, 20));
                                        daisenhong.itemOptions.add(new ItemOption(103, 20));
                                        daisenhong.itemOptions.add(new ItemOption(93, 3));
                                        daisenhong.itemOptions.add(new ItemOption(174, 2024));
                                        daisenvang.itemOptions.add(new ItemOption(174, 2024));
                                        daisenvang.itemOptions.add(new ItemOption(50, 20));
                                        daisenvang.itemOptions.add(new ItemOption(77, 20));
                                        daisenvang.itemOptions.add(new ItemOption(103, 20));
                                        daisenvang.itemOptions.add(new ItemOption(93, 3));
                                        InventoryServiceNew.gI().addItemBag(player, daisenhong);
                                        InventoryServiceNew.gI().addItemBag(player, daisenvang);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "Bạn nhận được " + daisenhong.template.name
                                                + " Và " + daisenvang.template.name);
                                    }
                                    break;
                                case 3:
                                    Item saobien = null;

                                    try {
                                        saobien = InventoryServiceNew.gI().findItemBag(player, 698);
                                    } catch (Exception e) {
                                    }
                                    if (saobien == null || saobien.quantity < 99) {
                                        this.npcChat(player, "Bạn Không Có Vật Phẩm Nào");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành Trang Của Bạn Không Đủ Chỗ Trống");
                                    } else {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, saobien, 99);
                                        Item katana = ItemService.gI().createNewItem((short) 1394);
                                        Item thanhlongdao = ItemService.gI().createNewItem((short) 1395);
                                        Item apk21 = ItemService.gI().createNewItem((short) 1004);
                                        katana.itemOptions.add(new ItemOption(50, 20));
                                        katana.itemOptions.add(new ItemOption(77, 20));
                                        katana.itemOptions.add(new ItemOption(103, 20));
                                        katana.itemOptions.add(new ItemOption(93, 3));
                                        katana.itemOptions.add(new ItemOption(174, 2024));
                                        thanhlongdao.itemOptions.add(new ItemOption(174, 2024));
                                        thanhlongdao.itemOptions.add(new ItemOption(50, 20));
                                        thanhlongdao.itemOptions.add(new ItemOption(77, 20));
                                        thanhlongdao.itemOptions.add(new ItemOption(103, 20));
                                        thanhlongdao.itemOptions.add(new ItemOption(93, 3));
                                        apk21.itemOptions.add(new ItemOption(174, 2024));
                                        apk21.itemOptions.add(new ItemOption(50, 20));
                                        apk21.itemOptions.add(new ItemOption(77, 20));
                                        apk21.itemOptions.add(new ItemOption(103, 20));
                                        apk21.itemOptions.add(new ItemOption(93, 3));
                                        InventoryServiceNew.gI().addItemBag(player, katana);
                                        InventoryServiceNew.gI().addItemBag(player, thanhlongdao);
                                        InventoryServiceNew.gI().addItemBag(player, apk21);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "Bạn nhận được " + katana.template.name
                                                + " , " + thanhlongdao.template.name + " Và " + apk21.template.name);
                                    }
                                    break;
                            }
                        }

                    }
                }
            }
        };
    }

    public static Npc ruongDo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    InventoryServiceNew.gI().sendItemBox(player);
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    public static Npc ngokhong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Xin chào cư dân Ngọc Rồng KhanhDTK, Cư Dân Muốn Mua Vật Phẩm Sao? ",
                            "Cửa hàng", "Đóng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 124) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: // shop
                                    ShopServiceNew.gI().opendShop(player, "NGOKHONG", false);
                                    break;
                            }

                        }
                    }
                }
            }
        };
    }

    public static Npc duongtank(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (mapId == 0) {
                        nguhs.gI().setTimeJoinnguhs();
                        long now = System.currentTimeMillis();
                        if (now > nguhs.TIME_OPEN_NHS && now < nguhs.TIME_CLOSE_NHS) {
                            this.createOtherMenu(player, 0, "|2| Theo Chân Ta Giải Cứu Cho Ngộ Không Nào?\n"
                                    + "50 hồng ngọc 1 lần vào, tham gia ngay?\n"
                                    + "Bạn Cần Đạt Đủ 80 Tỷ Sức Mạnh và 50 Hồng Ngọc Để Có Thể Vào",
                                    "Tham gia", "Đóng");
                        } else {
                            this.createOtherMenu(player, 0,
                                    "|2| Theo Chân Ta Giải Cứu Cho Ngộ Không Nào?", "Đóng");
                        }
                    }
                    if (mapId == 122) {
                        this.createOtherMenu(player, 0, "Bạn Muốn Quay Trở Lại Làng Aru?", "OK", "Từ chối");

                    }
                    if (mapId == 124) {
                        this.createOtherMenu(player, 0,
                                "Ngọc Rồng Alone\b|6|Thí chủ đang có: " + player.NguHanhSonPoint
                                        + " điểm ngũ hành sơn\b|1|Thí chủ muốn đổi cải trang x4 chưởng ko?",
                                "Đổi Điểm", "Top Ngũ Hành Sơn", "Đóng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (mapId == 0) {
                        switch (select) {
                            case 0:
                                if (player.nPoint.power < 80000000000L) {
                                    Service.getInstance().sendThongBao(player, "Sức mạnh bạn không đủ để qua map!");
                                    return;
                                } else if (player.inventory.ruby < 50) {
                                    Service.getInstance().sendThongBao(player,
                                            "Phí vào là 50 hồng ngọc một lần bạn ey!\nBạn không đủ!");
                                    return;
                                } else {
                                    player.inventory.ruby -= 50;
                                    PlayerService.gI().sendInfoHpMpMoney(player);
                                    ChangeMapService.gI().changeMapInYard(player, 122, -1, -1);
                                }
                                break;
                            case 1:
                                break;

                        }
                    }
                    if (mapId == 122) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapInYard(player, 0, -1, 469);
                        }
                    }
                    if (mapId == 124) {
                        if (select == 0) {
                            if (player.NguHanhSonPoint >= 500) {
                                player.NguHanhSonPoint -= 500;
                                Item item = ItemService.gI().createNewItem((short) (711));
                                item.itemOptions.add(new Item.ItemOption(49, 25));
                                item.itemOptions.add(new Item.ItemOption(77, 25));
                                item.itemOptions.add(new Item.ItemOption(103, 25));
                                item.itemOptions.add(new Item.ItemOption(207, 0));
                                item.itemOptions.add(new Item.ItemOption(33, 0));
                                //
                                InventoryServiceNew.gI().addItemBag(player, item);
                                Service.gI().sendThongBao(player, "Chúc Mừng Bạn Đổi Vật Phẩm Thành Công !");
                            } else {
                                Service.gI().sendThongBao(player,
                                        "Không đủ điểm, bạn còn " + (500 - player.pointPvp) + " điểm nữa");
                            }

                        }
                    }

                }
            }
        };
    }

    public static Npc hoanguc(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (mapId == 0) {
                        this.createOtherMenu(player, 0, "bạn muốn vào hành tinh hoả?.", "Đồng ý", "Từ chối");
                    }
                    if (mapId == 122) {
                        this.createOtherMenu(player, 0, "Thí chủ muốn quay về làng Aru?", "Đồng ý", "Từ chối");

                    }
                    if (mapId == 124) {
                        this.createOtherMenu(player, 0,
                                "A mi khò khò, ở Ngũ hành sơn có lũ khỉ đã ăn trộm Hồng Đào\b Thí chủ có thể giúp ta lấy lại Hồng Đào từ chúng\bTa sẽ đổi 1 ít đồ để đổi lấy Hồng Đào.",
                                "Cửa hàng\nHồng Đào", "Về\nLàng Aru", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (select) {
                        case 0:
                            if (mapId == 0) {
                                if (player.nPoint.power < 400000000000L || player.nPoint.power >= 2000000000000L) {
                                    this.npcChat(player, "yeu cau 40 ti sm!");
                                    return;
                                }
                                ChangeMapService.gI().changeMapBySpaceShip(player, 173, -1, 96);
                            }
                            if (mapId == 122) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, 0, -1, 936);
                            }
                            if (mapId == 124) {
                                if (select == 0) {
                                    ShopServiceNew.gI().opendShop(player, "TAYDUKY", true);
                                    break;
                                }
                                if (select == 1) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 0, -1, 936);
                                }
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc dauThan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    player.magicTree.openMenuTree();
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MAGIC_TREE_NON_UPGRADE_LEFT_PEA:
                            if (select == 0) {
                                player.magicTree.harvestPea();
                            } else if (select == 1) {
                                if (player.magicTree.level == 10) {
                                    player.magicTree.fastRespawnPea();
                                } else {
                                    player.magicTree.showConfirmUpgradeMagicTree();
                                }
                            } else if (select == 2) {
                                player.magicTree.fastRespawnPea();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_NON_UPGRADE_FULL_PEA:
                            if (select == 0) {
                                player.magicTree.harvestPea();
                            } else if (select == 1) {
                                player.magicTree.showConfirmUpgradeMagicTree();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_CONFIRM_UPGRADE:
                            if (select == 0) {
                                player.magicTree.upgradeMagicTree();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_UPGRADE:
                            if (select == 0) {
                                player.magicTree.fastUpgradeMagicTree();
                            } else if (select == 1) {
                                player.magicTree.showConfirmUnuppgradeMagicTree();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_CONFIRM_UNUPGRADE:
                            if (select == 0) {
                                player.magicTree.unupgradeMagicTree();
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc calick(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            private final byte COUNT_CHANGE = 50;
            private int count;

            private void changeMap() {
                if (this.mapId != 102) {
                    count++;
                    if (this.count >= COUNT_CHANGE) {
                        count = 0;
                        this.map.npcs.remove(this);
                        Map map = MapService.gI().getMapForCalich();
                        this.mapId = map.mapId;
                        this.cx = Util.nextInt(100, map.mapWidth - 100);
                        this.cy = map.yPhysicInTop(this.cx, 0);
                        this.map = map;
                        this.map.npcs.add(this);
                    }
                }
            }

            @Override
            public void openBaseMenu(Player player) {
                player.iDMark.setIndexMenu(ConstNpc.BASE_MENU);
                if (TaskService.gI().getIdTask(player) < ConstTask.TASK_20_0) {
                    Service.gI().hideWaitDialog(player);
                    Service.gI().sendThongBao(player, "Không thể thực hiện");
                    return;
                }
                if (this.mapId != player.zone.map.mapId) {
                    Service.gI().sendThongBao(player, "Calích đã rời khỏi map!");
                    Service.gI().hideWaitDialog(player);
                    return;
                }

                if (this.mapId == 102) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Chào chú, cháu có thể giúp gì?",
                            "Kể\nChuyện", "Quay về\nQuá khứ");
                } else {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Chào chú, cháu có thể giúp gì?", "Kể\nChuyện", "Đi đến\nTương lai", "Từ chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (this.mapId == 102) {
                    if (player.iDMark.isBaseMenu()) {
                        if (select == 0) {
                            // kể chuyện
                            NpcService.gI().createTutorial(player, this.avartar, ConstNpc.CALICK_KE_CHUYEN);
                        } else if (select == 1) {
                            // về quá khứ
                            ChangeMapService.gI().goToQuaKhu(player);
                        }
                    }
                } else if (player.iDMark.isBaseMenu()) {
                    if (select == 0) {
                        // kể chuyện
                        NpcService.gI().createTutorial(player, this.avartar, ConstNpc.CALICK_KE_CHUYEN);
                    } else if (select == 1) {
                        if (player.playerTask.taskMain.id >= 21) {
                            ChangeMapService.gI().changeMapInYard(player, 102, -1, -1);
                        } else {
                            Service.gI().sendThongBao(player, "Hãy hoàn thành những nhiệm vụ trước đó");
                        }
                    }
                }
            }
        };
    }

    public static Npc jaco(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Gô Tên, Calich và Monaka đang gặp chuyện ở hành tinh Potaufeu \n Hãy đến đó ngay",
                                "Đến \nPotaufeu");
                    } else if (this.mapId == 139) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Người muốn trở về?", "Quay về", "Từ chối");
                    }
                }
            }
            // if (player.getSession().player.playerTask.taskMain.id >= 24) {
            // ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
            // } else {
            // this.npcChat(player, "Hãy hoàn thành những nhiệm vụ trước đó");
            // }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        if (player.getSession().player.playerTask.taskMain.id >= 22) {

                            ChangeMapService.gI().goToPotaufeu(player);
                        } else {
                            this.npcChat(player, "Hãy hoàn thành xong nhiệm vụ fide");
                        }
                    } else if (this.mapId == 139) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                // về trạm vũ trụ
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 24 + player.gender, -1, -1);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    private static Npc poTaGe(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 140) {
                        if (!player.getSession().actived) {
                            Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Đa vũ trụ song song \b|7|Con muốn gọi con trong đa vũ trụ \b|1|Với giá 10.000 hồng ngọc không?",
                                    "Gọi Boss\nNhân bản", "Từ chối");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (player.nPoint.power < 50000000000L) {
                    // if (player.getSession().player.playerTask.taskMain.id >= 22) {
                    // if (!player.getSession().actived) {
                    Service.gI().sendThongBao(player, "Yêu cầu sức mạnh là 50 tỉ !");
                } else if (canOpenNpc(player)) {
                    if (this.mapId == 140) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: {
                                    Boss oldBossClone = BossManager.gI()
                                            .getBossById(Util.createIdBossClone((int) player.id));
                                    if (oldBossClone != null) {
                                        this.npcChat(player,
                                                "Nhà ngươi hãy tiêu diệt Boss lúc trước gọi ra đã, con boss đó đang ở khu "
                                                        + oldBossClone.zone.zoneId);
                                    } else if (player.inventory.ruby < 10000) {
                                        this.npcChat(player, "Nhà ngươi không đủ 10.000 Hồng ngọc ");
                                    } else {
                                        List<Skill> skillList = new ArrayList<>();
                                        for (byte i = 0; i < player.playerSkill.skills.size(); i++) {
                                            Skill skill = player.playerSkill.skills.get(i);
                                            if (skill.point > 0) {
                                                skillList.add(skill);
                                            }
                                        }
                                        int[][] skillTemp = new int[skillList.size()][3];
                                        for (byte i = 0; i < skillList.size(); i++) {
                                            Skill skill = skillList.get(i);
                                            if (skill.point > 0) {
                                                skillTemp[i][0] = skill.template.id;
                                                skillTemp[i][1] = skill.point;
                                                skillTemp[i][2] = skill.coolDown;
                                            }
                                        }
                                        BossData bossDataClone = new BossData(
                                                "Nhân Bản" + player.name,
                                                player.gender,
                                                new short[] { player.getHead(), player.getBody(), player.getLeg(),
                                                        player.getFlagBag(), player.idAura, player.getEffFront() },
                                                player.nPoint.dame,
                                                new int[] { player.nPoint.hpMax },
                                                new int[] { 140 },
                                                skillTemp,
                                                new String[] { "|-2|Boss nhân bản đã xuất hiện rồi" }, // text chat 1
                                                new String[] { "|-1|Ta sẽ chiếm lấy thân xác của ngươi hahaha!" }, // text
                                                                                                                   // chat
                                                                                                                   // 2
                                                new String[] { "|-1|Lần khác ta sẽ xử đẹp ngươi" }, // text chat 3
                                                60);

                                        try {
                                            new NhanBan(Util.createIdBossClone((int) player.id), bossDataClone,
                                                    player.zone);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        // trừ vàng khi gọi boss
                                        player.inventory.ruby -= 10000;
                                        Service.gI().sendMoney(player);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc npclytieunuong54(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 5) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "\b|7|Bạn cần đổi random gì?\b|7|", "Đổi pet\nBằng thỏi vàng",
                            "Đổi thú cưỡi\nBằng thỏi vàng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, 1,
                                            "\b|7|Bạn có mún đổi 100 thỏi vàng random Danh hiệu thiên tử, có tỉ lệ vĩnh viễn?",
                                            "Úm ba la ta ra được nịt", "Từ Chối");
                                    break;
                                case 1:
                                    this.createOtherMenu(player, 2,
                                            "\b|7|Bạn có mún đổi 200 thỏi vàng random thú cưỡi vip có tỉ lệ vĩnh viễn ?",
                                            "úm ba la ta ra được nịt");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 1) {
                            switch (select) {
                                case 0:
                                    Item vy1 = null;
                                    try {
                                        vy1 = InventoryServiceNew.gI().findItemBag(player, 457);
                                    } catch (Exception e) {
                                        // throw new RuntimeException(e);
                                    }
                                    if (vy1 == null || vy1.quantity < 100) {
                                        this.npcChat(player, "Bạn cần có x100 thỏi vàng");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        player.inventory.gold -= 100;
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, vy1, 100);
                                        Service.gI().sendMoney(player);
                                        Item trungLinhThu = ItemService.gI().createNewItem((short) 1323);
                                        trungLinhThu.itemOptions.add(new Item.ItemOption(50, Util.nextInt(10) + 5));
                                        trungLinhThu.itemOptions.add(new Item.ItemOption(77, Util.nextInt(10) + 10));
                                        trungLinhThu.itemOptions.add(new Item.ItemOption(103, Util.nextInt(10) + 10));
                                        trungLinhThu.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1) + 6));
                                        trungLinhThu.itemOptions.add(new Item.ItemOption(30, Util.nextInt(10) + 5));
                                        InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Vui lòng kiểm tra hành trang !!!");
                                    }
                                    break;
                                case 1: // canel
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 2) { // action đổi dồ húy diệt
                            switch (select) {
                                case 0: // trade
                                    Item hoa = null;
                                    try {
                                        hoa = InventoryServiceNew.gI().findItemBag(player, 457);
                                    } catch (Exception e) {
                                        // throw new RuntimeException(e);
                                    }
                                    if (hoa == null || hoa.quantity < 200) {
                                        this.npcChat(player, "Bạn cần có 200 TV");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        player.inventory.gold -= 1;
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, hoa, 200);
                                        Service.gI().sendMoney(player);
                                        Item trungLinhThu = ItemService.gI().createNewItem((short) 746);
                                        trungLinhThu.itemOptions.add(new Item.ItemOption(50, Util.nextInt(10) + 12));
                                        trungLinhThu.itemOptions.add(new Item.ItemOption(77, Util.nextInt(10) + 3));
                                        trungLinhThu.itemOptions.add(new Item.ItemOption(103, Util.nextInt(10) + 7));
                                        trungLinhThu.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1) + 25));
                                        trungLinhThu.itemOptions.add(new Item.ItemOption(30, Util.nextInt(10) + 8));
                                        trungLinhThu.itemOptions.add(new Item.ItemOption(217, Util.nextInt(10) + 8));
                                        InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Vui lòng kiểm tra hành trang !!! ");
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc thuongDe(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (!canOpenNpc(player)) {
                    return;
                }

                String message;
                if (this.mapId == 141) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Hãy nắm lấy tay ta mau!", "Về\nthần điện");
                } else {
                    if (player.typetrain == 1 && !player.istrain) {
                        message = "Pôpô là đệ tử của ta, luyện tập với Pôpô con sẽ có nhiều kinh nghiệm đánh bại được Pôpô ta sẽ dạy võ công cho con";
                    } else if (player.typetrain == 2 && player.istrain) {
                        message = "Từ nay con sẽ là đệ tử của ta. Ta sẽ truyền cho con tất cả tuyệt kĩ";
                    } else if (player.typetrain == 1 && player.istrain) {
                        message = "Pôpô là đệ tử của ta, luyện tập với Pôpô con sẽ có nhiều kinh nghiệm đánh bại được Pôpô ta sẽ dạy võ công cho con";
                    } else if (player.typetrain == 2 && !player.istrain) {
                        message = "Từ nay con sẽ là đệ tử của ta. Ta sẽ truyền cho con tất cả tuyệt kĩ";
                    } else {
                        message = "Con đã mạnh hơn ta, ta sẽ chỉ đường cho con đến Kaio để gặp thần Vũ Trụ Phương Bắc\nNgài là thần cai quản vũ trụ này, hãy theo ngài ấy học võ công";
                    }

                    if (player.typetrain == 1 && !player.istrain) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Đăng ký tập tự động",
                                "Tập luyện với Mr.PôPô", "Thách đấu Mr.PôPô", "Đến Kaio", "Quay số\nmay mắn");
                    } else if (player.typetrain == 2 && player.istrain) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Hủy đăng ký tập tự động",
                                "Tập luyện với Mr.PôPô", "Thách đấu\nvới thượng đế", "Đến Kaio", "Quay số\nmay mắn");
                    } else if (player.typetrain == 1 && player.istrain) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Hủy đăng ký tập tự động",
                                "Tập luyện với Mr.PôPô", "Thách đấu Mr.PôPô", "Đến Kaio", "Quay số\nmay mắn");
                    } else if (player.typetrain == 2 && !player.istrain) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Đăng ký tập tự động",
                                "Tập luyện\nvới Mr.PôPô", "Thách đấu\nvới thượng đế", "Đến Kaio", "Quay số\nmay mắn");
                    } else {
                        if (!player.istrain) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Đăng ký tập tự động",
                                    "Tập luyện với Mr.PôPô", "Tập luyện với thượng đế", "Đến Kaio", "Quay số\nmay mắn");
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Hủy đăng ký tập tự động",
                                    "Tập luyện với Mr.PôPô", "Tập luyện với thượng đế", "Đến Kaio", "Quay số\nmay mắn");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player) && this.mapId == 45) {
                    if (player.iDMark.isBaseMenu()) {
                        if (select == 0) {
                            if (!player.istrain) {
                                this.createOtherMenu(player, ConstNpc.MENU_TRAIN_OFFLINE,
                                        "Đăng ký để mỗi khi Offline quá 30 phút, con sẽ được tự động luyện tập với tốc độ "
                                                + player.nPoint.getexp() + " sức mạnh mỗi phút",
                                        "Hướng dẫn thêm", "Đồng ý 1 ngọc mỗi lần", "Không đồng ý");
                            } else {
                                player.istrain = false;
                                this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        "Con đã hủy thành công đăng ký tập tự động", "Đóng");
                            }
                        } else if (select == 1) {
                            this.createOtherMenu(player, ConstNpc.MENU_TRAIN_OFFLINE_TRY0,
                                    "Con có chắc muốn tập luyện?\nTập luyện với "
                                            + player.nPoint.getNameNPC(player, this, (byte) select) + " sẽ tăng "
                                            + player.nPoint.getExpbyNPC(player, this, (byte) select)
                                            + " sức mạnh mỗi phút",
                                    "Đồng ý luyện tập", "Không đồng ý");
                        } else if (select == 2) {
                            if (player.typetrain > 2) {
                                this.createOtherMenu(player, ConstNpc.MENU_TRAIN_OFFLINE_TRY1,
                                        "Con có chắc muốn tập luyện?\nTập luyện với "
                                                + player.nPoint.getNameNPC(player, this, (byte) select) + " sẽ tăng "
                                                + player.nPoint.getExpbyNPC(player, this, (byte) select)
                                                + " sức mạnh mỗi phút",
                                        "Đồng ý luyện tập", "Không đồng ý");
                            } else if (player.typetrain == 1) {
                                player.setfight((byte) 1, (byte) 0);
                                player.zone.load_Me_To_Another(player);
                                player.zone.load_Another_To_Me(player);

                            } else {

                                ChangeMapService.gI().changeMap(player, 49, 0, 578, 456);
                                player.setfight((byte) 1, (byte) 1);
                                try {
                                    new Thuongde(BossID.THUONG_DE, BossesData.THUONG_DE, player.zone,
                                            player.location.x - 10, player.location.y);
                                    player.zone.load_Another_To_Me(player);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else if (select == 3) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 48, -1, 354);
                        } else if (select == 4) {
                            this.createOtherMenu(player, ConstNpc.MENU_CHOOSE_LUCKY_ROUND,
                                    "Con muốn làm gì nào?", "Quay bằng\nvàng",
                                    "Rương phụ\n("
                                            + (player.inventory.itemsBoxCrackBall.size()
                                                    - InventoryServiceNew.gI()
                                                            .getCountEmptyListItem(player.inventory.itemsBoxCrackBall))
                                            + " món)",
                                    "Xóa hết\ntrong rương", "Đóng");

                        }

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_TRAIN_OFFLINE) {
                        switch (select) {
                            case 0:
                                Service.gI().sendPopUpMultiLine(player, tempId, this.avartar,
                                        ConstNpc.INFOR_TRAIN_OFFLINE);
                                break;
                            case 1:
                                player.istrain = true;
                                NpcService.gI().createTutorial(player, this.avartar,
                                        "Từ giờ, quá 30 phút Offline con sẽ tự động luyện tập");
                                break;
                            case 3:
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_TRAIN_OFFLINE_TRY0) {
                        switch (select) {
                            case 0:
                                player.setfight((byte) 0, (byte) 0);
                                player.zone.load_Me_To_Another(player);
                                player.zone.load_Another_To_Me(player);

                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_TRAIN_OFFLINE_TRY1) {
                        switch (select) {
                            case 0:
                                player.setfight((byte) 1, (byte) 1);
                                ChangeMapService.gI().changeMap(player, 49, 0, 578, 456);
                                try {
                                    new Thuongde(BossID.THUONG_DE, BossesData.THUONG_DE, player.zone,
                                            player.location.x - 10, player.location.y);
                                    player.zone.load_Another_To_Me(player);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CHOOSE_LUCKY_ROUND) {
                        switch (select) {
                            case 0:
                                LuckyRound.gI().openCrackBallUI(player, LuckyRound.USING_GOLD);
                                break;
                            case 1:
                                ShopServiceNew.gI().opendShop(player, "ITEMS_LUCKY_ROUND", true);
                                break;
                            case 2:
                                NpcService.gI().createMenuConMeo(player,
                                        ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND, this.avartar,
                                        "Con có chắc muốn xóa hết vật phẩm trong rương phụ? Sau khi xóa "
                                                + "sẽ không thể khôi phục!",
                                        "Đồng ý", "Hủy bỏ");
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc thanVuTru(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    String message;
                    if (this.mapId == 48) {
                        if (player.typetrain == 3 && !player.istrain) {
                            message = "Thượng đế đưa người đến đây, chắc muốn ta dạy võ chứ gì\nBắt được con khỉ Bubbles rồi hãy tính";
                        } else if (player.typetrain == 4 && player.istrain) {
                            message = "Ta là Thần Vũ Trụ Phương Bắc cai quản khu vực Bắc Vũ Trụ\nnếu thắng được ta\nngươi sẽ đến lãnh địa Kaio, nơi ở của thần linh ";
                        } else if (player.typetrain == 3 && player.istrain) {
                            message = "Thượng đế đưa người đến đây, chắc muốn ta dạy võ chứ gì\nBắt được con khỉ Bubbles rồi hãy tính";
                        } else if (player.typetrain == 4 && !player.istrain) {
                            message = "Ta là Thần Vũ Trụ Phương Bắc cai quản khu vực Bắc Vũ Trụ\nnếu thắng được ta\nngươi sẽ đến lãnh địa Kaio, nơi ở của thần linh ";
                        } else {
                            message = "Con mạnh nhất phía bắc vũ trụ này rồi đấy nhưng ngoài vũ trụ bao la kia vẫn có những kẻ mạnh hơn nhiều\ncon cần phải tập luyện để mạnh hơn nữa";
                        }

                        if (player.typetrain == 3 && !player.istrain) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Đăng ký tập tự động",
                                    "Tập luyện với Khỉ Bubbles", "Thách đấu Khỉ Bubbles", "Di chuyển");
                        } else if (player.typetrain == 4 && player.istrain) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Hủy đăng ký tập tự động",
                                    "Tập luyện với Khỉ Bubbles", "Thách đấu\nvới Thần\nVũ Trụ", "Di chuyển");
                        } else if (player.typetrain == 3 && player.istrain) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Hủy đăng ký tập tự động",
                                    "Tập luyện với Khỉ Bubbles", "Thách đấu Khỉ Bubbles", "Di chuyển");
                        } else if (player.typetrain == 4 && !player.istrain) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Đăng ký tập tự động",
                                    "Tập luyện\nvới Khỉ Bubbles", "Thách đấu\nvới Thần\nVũ Trụ", "Di chuyển");
                        } else {
                            if (!player.istrain) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Đăng ký tập tự động",
                                        "Tập luyện với Khỉ Bubbles", "Tập luyện \nvới Thần\nVũ Trụ", "Di chuyển");
                            } else {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Hủy đăng ký tập tự động",
                                        "Tập luyện với Khỉ Bubbles", "Tập luyện\nvới Thần\nVũ Trụ", "Di chuyển");
                            }
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 48) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                if (!player.istrain) {
                                    this.createOtherMenu(player, ConstNpc.MENU_TRAIN_OFFLINE,
                                            "Đăng ký để mỗi khi Offline quá 30 phút, con sẽ được tự động luyện tập với tốc độ "
                                                    + player.nPoint.getexp() + " sức mạnh mỗi phút",
                                            "Hướng dẫn thêm", "Đồng ý 1 ngọc mỗi lần", "Không đồng ý");
                                } else {
                                    player.istrain = false;
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                            "Con đã hủy thành công đăng ký tập tự động", "Đóng");
                                }
                            } else if (select == 1) {
                                this.createOtherMenu(player, ConstNpc.MENU_TRAIN_OFFLINE_TRY0,
                                        "Con có chắc muốn tập luyện?\nTập luyện với "
                                                + player.nPoint.getNameNPC(player, this, (byte) select) + " sẽ tăng "
                                                + player.nPoint.getExpbyNPC(player, this, (byte) select)
                                                + " sức mạnh mỗi phút",
                                        "Đồng ý luyện tập", "Không đồng ý");
                            } else if (select == 2) {
                                if (player.typetrain > 4) {
                                    this.createOtherMenu(player, ConstNpc.MENU_TRAIN_OFFLINE_TRY1,
                                            "Con có chắc muốn tập luyện?\nTập luyện với "
                                                    + player.nPoint.getNameNPC(player, this, (byte) select)
                                                    + " sẽ tăng "
                                                    + player.nPoint.getExpbyNPC(player, this, (byte) select)
                                                    + " sức mạnh mỗi phút",
                                            "Đồng ý luyện tập", "Không đồng ý");
                                } else if (player.typetrain == 3) {
                                    player.setfight((byte) 1, (byte) 0);
                                    player.zone.load_Me_To_Another(player);
                                    player.zone.load_Another_To_Me(player);

                                } else {
                                    player.setfight((byte) 1, (byte) 1);
                                    player.zone.mapInfo(player);
                                    com.KhanhDTK.data.DataGame.updateMap(player.getSession());
                                    try {
                                        new ThanVuTru(BossID.THAN_VUTRU, BossesData.THAN_VU_TRU, player.zone, this.cx,
                                                this.cy);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else if (select == 3) {
                                this.createOtherMenu(player, ConstNpc.MENU_DI_CHUYEN,
                                        "Con muốn đi đâu?", "Về\nthần điện", "Thánh địa\nKaio", "Con\nđường\nrắn độc",
                                        "Từ chối");

                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_TRAIN_OFFLINE) {
                            switch (select) {
                                case 0:
                                    Service.gI().sendPopUpMultiLine(player, tempId, this.avartar,
                                            ConstNpc.INFOR_TRAIN_OFFLINE);
                                    break;
                                case 1:
                                    player.istrain = true;
                                    NpcService.gI().createTutorial(player, this.avartar,
                                            "Từ giờ, quá 30 phút Offline con sẽ tự động luyện tập");
                                    break;
                                case 3:
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_TRAIN_OFFLINE_TRY0) {
                            switch (select) {
                                case 0:
                                    player.setfight((byte) 0, (byte) 0);
                                    player.zone.load_Me_To_Another(player);
                                    player.zone.load_Another_To_Me(player);

                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_TRAIN_OFFLINE_TRY1) {
                            switch (select) {
                                case 0:
                                    player.setfight((byte) 1, (byte) 1);
                                    player.zone.mapInfo(player);
                                    com.KhanhDTK.data.DataGame.updateMap(player.getSession());
                                    try {
                                        new ThanVuTru(BossID.THAN_VUTRU, BossesData.THAN_VU_TRU, player.zone, this.cx,
                                                this.cy);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DI_CHUYEN) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 45, -1, 354);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMap(player, 50, -1, 318, 336);
                                    break;
                            }
                        }
                    }
                }
            }

        };
    }

    public static Npc TosuKaio(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    String message;
                    if (this.mapId == 50) {
                        if (player.typetrain >= 5) {
                            message = "Tập luyện với Tổ sư Kaio sẽ tăng " + player.nPoint.getexp()
                                    + " sức mạnh mỗi phút, con có muốn đăng ký không?";
                            if (!player.istrain) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Đăng ký tập tự động",
                                        "Đồng ý\nluyện tập", "Không đồng ý");
                            } else {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Hủy đăng ký tập tự động",
                                        "Đồng ý\nluyện tập", "Không đồng ý");
                            }
                        } else if (player.typetrain < 5) {
                            message = "Hãy đánh bại các cao thủ rồi quay lại đây";
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Vâng ạ");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50 && player.typetrain == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                if (!player.istrain) {
                                    this.createOtherMenu(player, ConstNpc.MENU_TRAIN_OFFLINE,
                                            "Đăng ký để mỗi khi Offline quá 30 phút, con sẽ được tự động luyện tập với tốc độ "
                                                    + player.nPoint.getexp() + " sức mạnh mỗi phút",
                                            "Hướng dẫn thêm", "Đồng ý 1 ngọc mỗi lần", "Không đồng ý");
                                } else {
                                    player.istrain = false;
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                            "Con đã hủy thành công đăng ký tập tự động", "Đóng");
                                }
                            } else if (select == 1) {
                                this.createOtherMenu(player, ConstNpc.MENU_TRAIN_OFFLINE_TRY0,
                                        "Con có chắc muốn tập luyện?\nTập luyện với "
                                                + player.nPoint.getNameNPC(player, this, (byte) select) + " sẽ tăng "
                                                + player.nPoint.getExpbyNPC(player, this, (byte) select)
                                                + " sức mạnh mỗi phút",
                                        "Đồng ý luyện tập", "Không đồng ý");
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_TRAIN_OFFLINE) {
                            switch (select) {
                                case 0:
                                    Service.gI().sendPopUpMultiLine(player, tempId, this.avartar,
                                            ConstNpc.INFOR_TRAIN_OFFLINE);
                                    break;
                                case 1:
                                    player.istrain = true;
                                    NpcService.gI().createTutorial(player, this.avartar,
                                            "Từ giờ, quá 30 phút Offline con sẽ tự động luyện tập");
                                    break;
                                case 3:
                                    break;
                            }

                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_TRAIN_OFFLINE_TRY0) {
                            switch (select) {
                                case 0:
                                    player.setfight((byte) 1, (byte) 1);
                                    player.zone.mapInfo(player);
                                    com.KhanhDTK.data.DataGame.updateMap(player.getSession());
                                    try {
                                        new ToSuKaio(BossID.TS_KAIO, BossesData.TO_SU_KAIO, player.zone, this.cx,
                                                this.cy);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                            }
                        }
                    }
                }
            }

        };
    }

    public static Npc kibit(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Đến\nKaio", "Từ chối");
                    }
                    if (this.mapId == 114) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 48, -1, 354, 240);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc osin(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ngươi có muốn úp mảnh thiên sứ ? \n"
                                + "Vậy thì hãy đạt 120 tỉ sức mạnh và mặc đủ 5 món đồ thần linh trước đi !\n"
                                + "Nếu đã săn sàng rồi thì hãy đưa ta 2000 ruby, ta sẽ dịch chuyển ngươi qua map up mảnh thiên sứ siêu vip !!!",
                                "Đến\nKaio", "Đến\nhành tinh\nBill", "Từ chối");
                    } else if (this.mapId == 154) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ngươi có muốn úp mảnh thiên sứ ? \n"
                                + "Vậy thì hãy đạt 120 tỉ sức mạnh và mặc đủ 5 món đồ thần linh trước đi !\n"
                                + "Nếu đã săn sàng rồi thì hãy đưa ta 2000 ruby, ta sẽ dịch chuyển ngươi qua map up mảnh thiên sứ siêu vip !!!",
                                "Về thánh địa", "Đến\nhành tinh\nngục tù", "Từ chối");
                    } else if (this.mapId == 155) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ngươi có muốn úp mảnh thiên sứ ? \n"
                                + "Vậy thì hãy đạt 120 tỉ sức mạnh và mặc đủ 5 món đồ thần linh trước đi !\n"
                                + "Nếu đã săn sàng rồi thì hãy đưa ta 2000 ruby, ta sẽ dịch chuyển ngươi qua map up mảnh thiên sứ siêu vip !!!",
                                "Quay về", "Từ chối");
                    } else if (this.mapId == 52) {
                        try {
                            MapMaBu.gI().setTimeJoinMapMaBu();
                            if (this.mapId == 52) {
                                long now = System.currentTimeMillis();
                                if (now > MapMaBu.TIME_OPEN_MABU && now < MapMaBu.TIME_CLOSE_MABU) {
                                    this.createOtherMenu(player, ConstNpc.MENU_OPEN_MMB, "Đại chiến Ma Bư đã mở, "
                                            + "ngươi có muốn tham gia không?",
                                            "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_MMB,
                                            "Ta có thể giúp gì cho ngươi?", "Hướng dẫn", "Từ chối");
                                }

                            }
                        } catch (Exception e) {
                            System.err.print("\nError at 212\n");
                            Logger.error("Lỗi mở menu osin");
                            e.printStackTrace();
                        }

                    } else if (this.mapId >= 114 && this.mapId < 120 && this.mapId != 116) {
                        if (player.fightMabu.pointMabu >= player.fightMabu.POINT_MAX) {
                            this.createOtherMenu(player, ConstNpc.GO_UPSTAIRS_MENU, "Ta có thể giúp gì cho ngươi ?",
                                    "Lên Tầng!", "Quay về", "Từ chối");
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                    "Quay về", "Từ chối");
                        }
                    } else if (this.mapId == 120) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Quay về", "Từ chối");
                    } else {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 48, -1, 354, 240);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMap(player, 154, -1, 200, 312);
                                    break;
                            }
                        }
                    } else if (this.mapId == 154) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 50, -1, 318, 336);
                                    break;
                                case 1:
                                    if (!player.setClothes.godClothes) {
                                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Yêu Cầu Phải Mặc Full Set Thần Linh", "Đóng");
                                    } else {
                                        if (player.getSession().player.playerTask.taskMain.id >= 29) {
                                            ChangeMapService.gI().goToPotaufeu(player);
                                        } else {
                                            this.npcChat(player, "Hãy hoàn thành nhiệm vụ siêu bọ hung !");
                                        }
                                        if (player.nPoint.power < 120000000000L) {
                                            Service.getInstance().sendThongBao(player,
                                                    "Sức mạnh cần 120 tỉ để qua map!");
                                        } else if (player.inventory.ruby < 5000) {
                                            Service.getInstance().sendThongBao(player,
                                                    "Phí vào là 5000 hồng ngọc một lần bạn ey!\nBạn đéo đủ!");
                                        } else {
                                            player.inventory.ruby -= 5000;
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            ChangeMapService.gI().changeMap(player, 155, -1, 111, 792);
                                            return;
                                        }
                                    }

                                    // ChangeMapService.gI().changeMap(player, 155, -1, 111, 792);
                                    break;
                            }
                        }
                    } else if (this.mapId == 155) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMap(player, 154, -1, 200, 312);
                            }
                        }
                    } else if (this.mapId == 52) {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.MENU_REWARD_MMB:
                                break;
                            case ConstNpc.MENU_OPEN_MMB:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_MA_BU);
                                } else if (select == 1) {
                                    // if (!player.getSession().actived) {
                                    // Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng
                                    // chức năng này");
                                    // } else
                                    ChangeMapService.gI().changeMap(player, 114, -1, 318, 336);
                                }
                                break;
                            case ConstNpc.MENU_NOT_OPEN_BDW:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_MA_BU);
                                }
                                break;
                        }
                    } else if (this.mapId >= 114 && this.mapId < 120 && this.mapId != 116) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.GO_UPSTAIRS_MENU) {
                            if (select == 0) {
                                player.fightMabu.clear();
                                ChangeMapService.gI().changeMap(player, this.map.mapIdNextMabu((short) this.mapId), -1,
                                        this.cx, this.cy);
                            } else if (select == 1) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        } else {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        }
                    } else if (this.mapId == 120) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.BASE_MENU) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc docNhan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.clan == null) {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Chỉ tiếp các bang hội, miễn tiếp khách vãng lai", "Đóng");
                        return;
                    }
                    if (player.clan.doanhTrai_haveGone) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ta đã thả ngọc rồng ở tất cả các map,mau đi nhặt đi. Hẹn ngươi quay lại vào ngày mai",
                                "OK");
                        return;
                    }

                    boolean flag = true;
                    for (Mob mob : player.zone.mobs) {
                        if (!mob.isDie()) {
                            flag = false;
                        }
                    }
                    for (Player boss : player.zone.getBosses()) {
                        if (!boss.isDie()) {
                            flag = false;
                        }
                    }

                    if (flag) {
                        player.clan.doanhTrai_haveGone = true;
                        player.clan.lastTimeOpenDoanhTrai = (System.currentTimeMillis() - 300000);
                        // player.clan.doanhTrai.DropNgocRong();
                        for (Player pl : player.clan.membersInGame) {
                            ItemTimeService.gI().sendTextTime(pl, (byte) 0, "Doanh trại độc nhãn sắp kết thúc : ", 300);
                        }
                        // player.clan.doanhTrai.timePickDragonBall = true;
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ta đã thả ngọc rồng ở tất cả các map,mau đi nhặt đi. Hẹn ngươi quay lại vào ngày mai",
                                "OK");
                    } else {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Hãy tiêu diệt hết quái và boss trong map", "OK");
                    }

                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MENU_JOIN_DOANH_TRAI:
                            if (select == 0) {
                                DoanhTraiService.gI().joinDoanhTrai(player);
                            } else if (select == 2) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                        case ConstNpc.IGNORE_MENU:
                            if (select == 1) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc linhCanh(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    // if (player.clan != null && player.name.contains("trumticker")) {
                    // createOtherMenu(player, ConstNpc.MENU_JOIN_DOANH_TRAI,
                    // "NQMP",
                    // "Tham gia", "Không", "Hướng\ndẫn\nthêm");
                    // return;
                    // }
                    if (player.clan == null) {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Chỉ tiếp các bang hội, miễn tiếp khách vãng lai", "Đóng");
                        return;
                    }
                    if (player.clan.getMembers().size() < DoanhTrai.N_PLAYER_CLAN) {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Bang hội phải có ít nhất 5 thành viên mới có thể mở", "Đóng");
                        return;
                    }
                    if (player.clan.doanhTrai != null) {
                        createOtherMenu(player, ConstNpc.MENU_JOIN_DOANH_TRAI,
                                "Bang hội của ngươi đang đánh trại độc nhãn\n"
                                        + "Thời gian còn lại là "
                                        + (TimeUtil.getSecondLeft(player.clan.lastTimeOpenDoanhTrai,
                                                DoanhTrai.TIME_DOANH_TRAI / 1000)) / 60
                                        + "p. Ngươi có muốn tham gia không?",
                                "Tham gia", "Không", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    int nPlSameClan = 0;
                    for (Player pl : player.zone.getPlayers()) {
                        if (!pl.equals(player) && pl.clan != null
                                && pl.clan.equals(player.clan) && pl.location.x >= 1285
                                && pl.location.x <= 1645) {
                            nPlSameClan++;
                        }
                    }
                    if (nPlSameClan < DoanhTrai.N_PLAYER_MAP) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ngươi phải có ít nhất " + DoanhTrai.N_PLAYER_MAP
                                        + " đồng đội cùng bang đứng gần mới có thể\nvào\n"
                                        + "tuy nhiên ta khuyên ngươi nên đi cùng với 3-4 người để khỏi chết.\n"
                                        + "Hahaha.",
                                "OK", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    if (player.clanMember.getNumDateFromJoinTimeToToday() < 1) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Doanh trại chỉ cho phép những người ở trong bang trên 1 ngày. Hẹn ngươi quay lại vào lúc khác",
                                "OK", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    if (System.currentTimeMillis() - player.LastDoanhTrai <= 1 * 24 * 60 * 60 * 1000) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ngươi đã đi hôm nay rồi \n Thời gian chờ còn lại để có thể vào : "
                                        + (((player.LastDoanhTrai + (1 * 24 * 60 * 60 * 1000))
                                                - System.currentTimeMillis()) / 1000)
                                        + " s",
                                "OK", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    if (System.currentTimeMillis() - player.clan.createTimeLong <= 2 * 24 * 60 * 60 * 1000) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Bang hội của ngươi chỉ vừa tạo.Hãy chờ đủ 2 ngày để có thể vào doanh trại \n Thời gian chờ còn lại để có thể vào : "
                                        + (((player.clan.createTimeLong + (2 * 24 * 60 * 60 * 1000))
                                                - System.currentTimeMillis()) / 1000)
                                        + " s",
                                "OK", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    if (System.currentTimeMillis() - player.clan.lastTimeOpenDoanhTrai <= 24 * 60 * 60 * 1000) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Bang hội của ngươi đã đi doanh trại gần đây .Hãy chờ"
                                        + (((player.clan.lastTimeOpenDoanhTrai + (24 * 60 * 60 * 1000))
                                                - System.currentTimeMillis()) / 1000)
                                        + "s để có thể tham gia doanh trại lần nữa. Hẹn ngươi quay lại vào ngày mai",
                                "OK", "Hướng\ndẫn\nthêm");
                        return;
                    }

                    createOtherMenu(player, ConstNpc.MENU_JOIN_DOANH_TRAI,
                            "Hôm nay bang hội của ngươi chưa vào trại lần nào. Ngươi có muốn vào\n"
                                    + "không?\nĐể vào, ta khuyên ngươi nên có 3-4 người cùng bang đi cùng",
                            "Vào\n(miễn phí)", "Không", "Hướng\ndẫn\nthêm");
                }
            }

            @Override

            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MENU_JOIN_DOANH_TRAI:
                            if (select == 0) {
                                DoanhTraiService.gI().joinDoanhTrai(player);
                            } else if (select == 2) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                        case ConstNpc.IGNORE_MENU:
                            if (select == 1) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                    }
                }
            }
        };
    }

    private static Npc popo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    // if (player.clanMember.getNumDateFromJoinTimeToToday() < 1 && player.clan !=
                    // null) {
                    // createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    // "Map Khí Gas chỉ cho phép những người ở trong bang trên 1 ngày. Hẹn ngươi
                    // quay lại vào lúc khác",
                    // "OK", "Hướng\ndẫn\nthêm");
                    // return;
                    // }
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (player.getSession().is_gift_box) {
                            // this.createOtherMenu(player, ConstNpc.BASE_MENU, "Chào con, con muốn ta giúp
                            // gì nào?", "Giải tán bang hội", "Nhận quà\nđền bù");
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Thượng đế vừa phát hiện 1 loại khí đang âm thầm\nhủy diệt mọi mầm sống trên Trái Đất,\nnó được gọi là Destron Gas.\nTa sẽ đưa các cậu đến nơi ấy, các cậu sẵn sàng chưa?",
                                    "Thông Tin Chi Tiết", "OK", "Từ Chối");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 3:
                                ShopServiceNew.gI().opendShop(player, "Khi Gas", false);
                                break;
                            // case 2:
                            // TopGasService.SendTop(TopGasService.Sort(Manager.TopGas), player);
                            // break;
                            case 1:
                                if (player.clan != null) {
                                    if (player.clan.khiGas != null) {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPENED_GAS,
                                                "Bang hội của con đang đi DesTroy Gas cấp độ "
                                                        + player.clan.khiGas.level + "\nCon có muốn đi theo không?",
                                                "Đồng ý", "Từ chối");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPEN_GAS,
                                                "Khí Gas Huỷ Diệt đã chuẩn bị tiếp nhận các đợt tấn công của quái vật\n"
                                                        + "các con hãy giúp chúng ta tiêu diệt quái vật \n"
                                                        + "Ở đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
                                                "Chọn\ncấp độ", "Từ chối");
                                    }
                                } else {
                                    this.npcChat(player, "Con phải có bang hội ta mới có thể cho con đi");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_GAS) {
                        switch (select) {
                            case 0:
                                if (player.isAdmin() || player.nPoint.power >= Gas.POWER_CAN_GO_TO_GAS) {
                                    ChangeMapService.gI().goToGas(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(Gas.POWER_CAN_GO_TO_GAS));
                                }
                                break;

                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_GAS) {
                        switch (select) {
                            case 0:
                                if (player.isAdmin() || player.nPoint.power >= Gas.POWER_CAN_GO_TO_GAS) {
                                    Input.gI().createFormChooseLevelGas(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(Gas.POWER_CAN_GO_TO_GAS));
                                }
                                break;
                        }

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCPET_GO_TO_GAS) {
                        switch (select) {
                            case 0:
                                GasService.gI().openGas(player,
                                        Integer.parseInt(String.valueOf(PLAYERID_OBJECT.get(player.id))));
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc meothantai(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                createOtherMenu(player, 0,
                        "\b|8|Trò chơi Tài Xỉu đang được diễn ra\n\n|6|Thử vận may của bạn với trò chơi Tài Xỉu! Đặt cược và dự đoán đúng"
                                + "\n kết quả, bạn sẽ được nhận thưởng lớn. Hãy tham gia ngay và\n cùng trải nghiệm sự hồi hộp, thú vị trong trò chơi này!"
                                + "\n\n|7|(Điều kiện tham gia : mở thành viên)\n\n|2|Đặt tối thiểu: 10 Thỏi Vàng\n Tối đa: 1.000 Thỏi Vàng"
                                + "\n\n|7| Lưu ý : Thoát game khi chốt Kết quả sẽ MẤT Tiền cược và Tiền thưởng",
                        "Thể lệ", "Tham gia");
            }

            @Override
            public void confirmMenu(Player pl, int select) {
                if (canOpenNpc(pl)) {
                    String time = ((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
                    if (pl.iDMark.getIndexMenu() == 0) {
                        if (select == 0) {
                            createOtherMenu(pl, ConstNpc.IGNORE_MENU,
                                    "|5|Có 2 nhà cái Tài và Xĩu, bạn chỉ được chọn 1 nhà để tham gia"
                                            + "\n\n|6|Sau khi kết thúc thời gian đặt cược. Hệ thống sẽ tung xí ngầu để biết kết quả Tài Xỉu"
                                            + "\n\nNếu Tổng số 3 con xí ngầu <=10 : XỈU\nNếu Tổng số 3 con xí ngầu >10 : TÀI\nNếu 3 Xí ngầu cùng 1 số : TAM HOA (Nhà cái lụm hết)"
                                            + "\n\n|7|Lưu ý: Số Thỏi Vàng nhận được sẽ bị nhà cái lụm đi 20%. Trong quá trình diễn ra khi đặt cược nếu thoát game trong lúc phát thưởng phần quà sẽ bị HỦY",
                                    "Ok");
                        } else if (select == 1) {
                            if (TaiXiu.gI().baotri == false) {
                                if (pl.goldTai == 0 && pl.goldXiu == 0) {
                                    createOtherMenu(pl, 1,
                                            "\n|7|---BÁN NHÀ BÁN XE CHƠI TÀI XỈU ĐI CÁC ÔNG---\n\n|3|Kết quả kì trước:  "
                                                    + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                                    + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai)
                                                    + " Thỏi Vàng"
                                                    + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size()
                                                    + " người"
                                                    + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu)
                                                    + " Thỏi Vàng"
                                                    + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size()
                                                    + " người"
                                                    + "\n\n|5|Thời gian còn lại: " + time,
                                            "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
                                } else if (pl.goldTai > 0) {
                                    createOtherMenu(pl, 1, "\n|7|---NHÀ CÁI TÀI XỈU---\n\n|3|Kết quả kì trước:  "
                                            + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Thỏi Vàng"
                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size() + " người"
                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Thỏi Vàng"
                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size() + " người"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU",
                                            "Đóng");
                                } else {
                                    createOtherMenu(pl, 1,
                                            "\n|7|---BÁN NHÀ BÁN XE CHƠI TÀI XỈU ĐI CÁC ÔNG---\n\n|3|Kết quả kì trước:  "
                                                    + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                                    + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai)
                                                    + " Thỏi Vàng"
                                                    + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size()
                                                    + " người"
                                                    + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu)
                                                    + " Thỏi Vàng"
                                                    + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size()
                                                    + " người"
                                                    + "\n\n|5|Thời gian còn lại: " + time,
                                            "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
                                }
                            } else {
                                if (pl.goldTai == 0 && pl.goldXiu == 0) {
                                    createOtherMenu(pl, 1,
                                            "\n|7|---BÁN NHÀ BÁN XE CHƠI TÀI XỈU ĐI CÁC ÔNG---\n\n|3|Kết quả kì trước:  "
                                                    + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                                    + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai)
                                                    + " Thỏi Vàng"
                                                    + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size()
                                                    + " người"
                                                    + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu)
                                                    + " Thỏi Vàng"
                                                    + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size()
                                                    + " người"
                                                    + "\n\n|5|Thời gian còn lại: " + time,
                                            "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
                                } else if (pl.goldTai > 0) {
                                    createOtherMenu(pl, 1,
                                            "\n|7|---BÁN NHÀ BÁN XE CHƠI TÀI XỈU ĐI CÁC ÔNG---\n\n|3|Kết quả kì trước:  "
                                                    + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                                    + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai)
                                                    + " Thỏi Vàng"
                                                    + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size()
                                                    + " người"
                                                    + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu)
                                                    + " Thỏi Vàng"
                                                    + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size()
                                                    + " người"
                                                    + "\n\n|5|Thời gian còn lại: " + time,
                                            "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
                                } else {
                                    createOtherMenu(pl, 1,
                                            "\n|7|---BÁN NHÀ BÁN XE CHƠI TÀI XỈU ĐI CÁC ÔNG---\n\n|3|Kết quả kì trước:  "
                                                    + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                                    + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai)
                                                    + " Thỏi Vàng"
                                                    + "\n\nTổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu)
                                                    + " Thỏi Vàng\n\n|5|Thời gian còn lại: " + time
                                                    + "\n\n|7|Bạn đã cược Xỉu : " + Util.format(pl.goldXiu)
                                                    + " Thỏi Vàng" + "\n\n|7|Hệ thống sắp bảo trì",
                                            "Cập nhập", "Đóng");
                                }
                            }
                        }
                    } else if (pl.iDMark.getIndexMenu() == 1) {
                        if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldTai == 0
                                && pl.goldXiu == 0 && TaiXiu.gI().baotri == false) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1,
                                            "\n|7|---BÁN NHÀ BÁN XE CHƠI TÀI XỈU ĐI CÁC ÔNG---\n\n|3|Kết quả kì trước:  "
                                                    + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                                    + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai)
                                                    + " Thỏi Vàng"
                                                    + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size()
                                                    + " người"
                                                    + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu)
                                                    + " Thỏi Vàng"
                                                    + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size()
                                                    + " người"
                                                    + "\n\n|5|Thời gian còn lại: " + time,
                                            "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
                                    break;
                                case 1:
                                    if (!pl.getSession().actived) {
                                        Service.gI().sendThongBao(pl,
                                                "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
                                    } else {
                                        Input.gI().TAI_taixiu(pl);
                                    }
                                    break;
                                case 2:
                                    if (!pl.getSession().actived) {
                                        Service.gI().sendThongBao(pl,
                                                "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
                                    } else {
                                        Input.gI().XIU_taixiu(pl);
                                    }
                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldTai > 0
                                && TaiXiu.gI().baotri == false) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1,
                                            "\n|7|---BÁN NHÀ BÁN XE CHƠI TÀI XỈU ĐI CÁC ÔNG---\n\n|3|Kết quả kì trước:  "
                                                    + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                                    + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai)
                                                    + " Thỏi Vàng"
                                                    + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size()
                                                    + " người"
                                                    + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu)
                                                    + " Thỏi Vàng"
                                                    + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size()
                                                    + " người"
                                                    + "\n\n|5|Thời gian còn lại: " + time,
                                            "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");

                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldXiu > 0
                                && TaiXiu.gI().baotri == false) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1,
                                            "\n|7|---BÁN NHÀ BÁN XE CHƠI TÀI XỈU ĐI CÁC ÔNG---\n\n|3|Kết quả kì trước:  "
                                                    + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                                    + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai)
                                                    + " Thỏi Vàng"
                                                    + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size()
                                                    + " người"
                                                    + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu)
                                                    + " Thỏi Vàng"
                                                    + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size()
                                                    + " người"
                                                    + "\n\n|5|Thời gian còn lại: " + time,
                                            "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldTai > 0
                                && TaiXiu.gI().baotri == true) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1,
                                            "\n|7|---BÁN NHÀ BÁN XE CHƠI TÀI XỈU ĐI CÁC ÔNG---\n\n|3|Kết quả kì trước:  "
                                                    + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                                    + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai)
                                                    + " Thỏi Vàng"
                                                    + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size()
                                                    + " người"
                                                    + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu)
                                                    + " Thỏi Vàng"
                                                    + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size()
                                                    + " người"
                                                    + "\n\n|5|Thời gian còn lại: " + time,
                                            "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");

                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldXiu > 0
                                && TaiXiu.gI().baotri == true) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1,
                                            "\n|7|---BÁN NHÀ BÁN XE CHƠI TÀI XỈU ĐI CÁC ÔNG---\n\n|3|Kết quả kì trước:  "
                                                    + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                                    + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai)
                                                    + " Thỏi Vàng"
                                                    + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size()
                                                    + " người"
                                                    + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu)
                                                    + " Thỏi Vàng"
                                                    + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size()
                                                    + " người"
                                                    + "\n\n|5|Thời gian còn lại: " + time,
                                            "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");

                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0
                                && pl.goldXiu == 0 && pl.goldTai == 0 && TaiXiu.gI().baotri == true) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1,
                                            "\n|7|---BÁN NHÀ BÁN XE CHƠI TÀI XỈU ĐI CÁC ÔNG---\n\n|3|Kết quả kì trước:  "
                                                    + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                                    + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai)
                                                    + " Thỏi Vàng"
                                                    + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size()
                                                    + " người"
                                                    + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu)
                                                    + " Thỏi Vàng"
                                                    + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size()
                                                    + " người"
                                                    + "\n\n|5|Thời gian còn lại: " + time,
                                            "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");

                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc quaTrung(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            private final int COST_AP_TRUNG_NHANH = 1000000000;

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == (21 + player.gender)) {
                        player.mabuEgg.sendMabuEgg();
                        if (player.mabuEgg.getSecondDone() != 0) {
                            this.createOtherMenu(player, ConstNpc.CAN_NOT_OPEN_EGG, "Burk Burk...",
                                    "Hủy bỏ\ntrứng", "Ấp nhanh\n" + Util.numberToMoney(COST_AP_TRUNG_NHANH) + " vàng",
                                    "Đóng");
                        } else {
                            this.createOtherMenu(player, ConstNpc.CAN_OPEN_EGG, "Burk Burk...", "Nở", "Hủy bỏ\ntrứng",
                                    "Đóng");
                        }
                    }
                    if (this.mapId == 154) {
                        if (player.billEgg != null) {
                            player.billEgg.sendBillEgg();
                            if (player.billEgg.getSecondDone() != 0) {
                                this.createOtherMenu(player, ConstNpc.CAN_NOT_OPEN_EGG, "Burk Burk...",
                                        "Hủy bỏ\ntrứng",
                                        "Ấp nhanh\n" + Util.numberToMoney(COST_AP_TRUNG_NHANH) + " vàng", "Đóng");
                            } else {
                                this.createOtherMenu(player, ConstNpc.CAN_OPEN_EGG, "Burk Burk...", "Nở",
                                        "Hủy bỏ\ntrứng", "Đóng");
                            }
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == (21 + player.gender)) {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.CAN_NOT_OPEN_EGG:
                                if (select == 0) {
                                    this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_EGG,
                                            "Bạn có chắc chắn muốn hủy bỏ trứng Mabư?", "Đồng ý", "Từ chối");
                                } else if (select == 1) {
                                    if (player.inventory.gold >= COST_AP_TRUNG_NHANH) {
                                        player.inventory.gold -= COST_AP_TRUNG_NHANH;
                                        player.mabuEgg.timeDone = 0;
                                        Service.gI().sendMoney(player);
                                        player.mabuEgg.sendMabuEgg();
                                    } else {
                                        Service.gI().sendThongBao(player,
                                                "Bạn không đủ vàng để thực hiện, còn thiếu "
                                                        + Util.numberToMoney(
                                                                (COST_AP_TRUNG_NHANH - player.inventory.gold))
                                                        + " vàng");
                                    }
                                }
                                break;
                            case ConstNpc.CAN_OPEN_EGG:
                                switch (select) {
                                    case 0:
                                        this.createOtherMenu(player, ConstNpc.CONFIRM_OPEN_EGG,
                                                "Bạn có chắc chắn cho trứng nở?\n"
                                                        + "Đệ tử của bạn sẽ được thay thế bằng đệ Mabư",
                                                "Đệ mabư\nTrái Đất", "Đệ mabư\nNamếc", "Đệ mabư\nXayda", "Từ chối");
                                        break;
                                    case 1:
                                        this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_EGG,
                                                "Bạn có chắc chắn muốn hủy bỏ trứng Mabư?", "Đồng ý", "Từ chối");
                                        break;
                                }
                                break;
                            case ConstNpc.CONFIRM_OPEN_EGG:
                                switch (select) {
                                    case 0:
                                        player.mabuEgg.openEgg(ConstPlayer.TRAI_DAT);
                                        break;
                                    case 1:
                                        player.mabuEgg.openEgg(ConstPlayer.NAMEC);
                                        break;
                                    case 2:
                                        player.mabuEgg.openEgg(ConstPlayer.XAYDA);
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case ConstNpc.CONFIRM_DESTROY_EGG:
                                if (select == 0) {
                                    player.mabuEgg.destroyEgg();
                                }
                                break;
                        }
                    }
                    if (this.mapId == 154) {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.CAN_NOT_OPEN_BILL:
                                if (select == 0) {
                                    this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_BILL,
                                            "Bạn có chắc chắn muốn hủy bỏ trứng Bill?", "Đồng ý", "Từ chối");
                                } else if (select == 1) {
                                    if (player.inventory.gold >= COST_AP_TRUNG_NHANH) {
                                        player.inventory.gold -= COST_AP_TRUNG_NHANH;
                                        player.billEgg.timeDone = 0;
                                        Service.gI().sendMoney(player);
                                        player.billEgg.sendBillEgg();
                                    } else {
                                        Service.gI().sendThongBao(player,
                                                "Bạn không đủ vàng để thực hiện, còn thiếu "
                                                        + Util.numberToMoney(
                                                                (COST_AP_TRUNG_NHANH - player.inventory.gold))
                                                        + " vàng");
                                    }
                                }
                                break;
                            case ConstNpc.CAN_OPEN_EGG:
                                switch (select) {
                                    case 0:
                                        this.createOtherMenu(player, ConstNpc.CONFIRM_OPEN_BILL,
                                                "Bạn có chắc chắn cho trứng nở?\n"
                                                        + "Đệ tử của bạn sẽ được thay thế bằng đệ Bill",
                                                "Đệ Bill\nTrái Đất", "Đệ Bill\nNamếc", "Đệ Bill\nXayda", "Từ chối");
                                        break;
                                    case 1:
                                        this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_BILL,
                                                "Bạn có chắc chắn muốn hủy bỏ trứng Bill?", "Đồng ý", "Từ chối");
                                        break;
                                }
                                break;
                            case ConstNpc.CONFIRM_OPEN_BILL:
                                switch (select) {
                                    case 0:
                                        player.billEgg.openEgg(ConstPlayer.TRAI_DAT);
                                        break;
                                    case 1:
                                        player.billEgg.openEgg(ConstPlayer.NAMEC);
                                        break;
                                    case 2:
                                        player.billEgg.openEgg(ConstPlayer.XAYDA);
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case ConstNpc.CONFIRM_DESTROY_BILL:
                                if (select == 0) {
                                    player.billEgg.destroyEgg();
                                }
                                break;
                        }
                    }

                }
            }
        };
    }

    public static Npc quocVuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                        "Con muốn nâng giới hạn sức mạnh cho bản thân hay đệ tử?",
                        "Bản thân", "Đệ tử", "Từ chối");
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                if (player.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                    this.createOtherMenu(player, ConstNpc.OPEN_POWER_MYSEFT,
                                            "Ta sẽ truền năng lượng giúp con mở giới hạn sức mạnh của bản thân lên "
                                                    + Util.numberToMoney(player.nPoint.getPowerNextLimit()),
                                            "Nâng\ngiới hạn\nsức mạnh",
                                            "Nâng ngay\n"
                                                    + Util.numberToMoney(OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER)
                                                    + " vàng",
                                            "Đóng");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                            "Sức mạnh của con đã đạt tới giới hạn",
                                            "Đóng");
                                }
                                break;
                            case 1:
                                if (player.pet != null) {
                                    if (player.pet.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                        this.createOtherMenu(player, ConstNpc.OPEN_POWER_PET,
                                                "Ta sẽ truền năng lượng giúp con mở giới hạn sức mạnh của đệ tử lên "
                                                        + Util.numberToMoney(player.pet.nPoint.getPowerNextLimit()),
                                                "Nâng ngay\n" + Util.numberToMoney(
                                                        OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) + " vàng",
                                                "Đóng");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Sức mạnh của đệ con đã đạt tới giới hạn",
                                                "Đóng");
                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "Không thể thực hiện");
                                }
                                // giới hạn đệ tử
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_MYSEFT) {
                        switch (select) {
                            case 0:
                                OpenPowerService.gI().openPowerBasic(player);
                                break;
                            case 1:
                                if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
                                    if (OpenPowerService.gI().openPowerSpeed(player)) {
                                        player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                                        Service.gI().sendMoney(player);
                                    }
                                } else {
                                    Service.gI().sendThongBao(player,
                                            "Bạn không đủ vàng để mở, còn thiếu "
                                                    + Util.numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER
                                                            - player.inventory.gold))
                                                    + " vàng");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_PET) {
                        if (select == 0) {
                            if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
                                if (OpenPowerService.gI().openPowerSpeed(player.pet)) {
                                    player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                                    Service.gI().sendMoney(player);
                                }
                            } else {
                                Service.gI().sendThongBao(player,
                                        "Bạn không đủ vàng để mở, còn thiếu "
                                                + Util.numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER
                                                        - player.inventory.gold))
                                                + " vàng");
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc bulmaTL(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 102) {
                        if (player.getSession().player.playerTask.taskMain.id == 30) {
                            if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu bé muốn mua gì nào?", "Cửa hàng",
                                        "Tới Trường Học", "Đóng");
                            }
                        } else {
                            if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu bé muốn mua gì nào?", "Cửa hàng",
                                        "Tới Trường Học", "Đóng");
                            }
                        }
                    } else if (this.mapId == 198) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu bé muốn gì nào?", "Quay Về", "Đóng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 102) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ShopServiceNew.gI().opendShop(player, "BUNMA_FUTURE", true);
                            }
                            if (select == 1) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, 198, -1, 331);
                            }
                        }

                    } else if (this.mapId == 198) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, 102, -1, 350);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc rongOmega(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    BlackBallWar.gI().setTime();
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        try {
                            long now = System.currentTimeMillis();
                            if (now > BlackBallWar.TIME_OPEN && now < BlackBallWar.TIME_CLOSE) {
                                this.createOtherMenu(player, ConstNpc.MENU_OPEN_BDW,
                                        "Đường đến với ngọc rồng sao đen đã mở, "
                                                + "ngươi có muốn tham gia không?",
                                        "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                            } else {
                                String[] optionRewards = new String[7];
                                int index = 0;
                                for (int i = 0; i < 7; i++) {
                                    if (player.rewardBlackBall.timeOutOfDateReward[i] > System.currentTimeMillis()) {
                                        String quantily = player.rewardBlackBall.quantilyBlackBall[i] > 1
                                                ? "x" + player.rewardBlackBall.quantilyBlackBall[i] + " "
                                                : "";
                                        optionRewards[index] = quantily + (i + 1) + " sao";
                                        index++;
                                    }
                                }
                                if (index != 0) {
                                    String[] options = new String[index + 1];
                                    for (int i = 0; i < index; i++) {
                                        options[i] = optionRewards[i];
                                    }
                                    options[options.length - 1] = "Từ chối";
                                    this.createOtherMenu(player, ConstNpc.MENU_REWARD_BDW,
                                            "Ngươi có một vài phần thưởng ngọc "
                                                    + "rồng sao đen đây!",
                                            options);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_BDW,
                                            "Ta có thể giúp gì cho ngươi?", "Hướng dẫn", "Từ chối");
                                }
                            }
                        } catch (Exception e) {
                            System.err.print("\nError at 213\n");
                            e.printStackTrace();
                            Logger.error("Lỗi mở menu rồng Omega");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MENU_REWARD_BDW:
                            player.rewardBlackBall.getRewardSelect((byte) select);
                            break;
                        case ConstNpc.MENU_OPEN_BDW:
                            if (select == 0) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_BLACK_BALL_WAR);
                            } else if (select == 1) {
                                // if (!player.getSession().actived) {
                                // Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng
                                // chức năng này");
                                //
                                // } else
                                player.iDMark.setTypeChangeMap(ConstMap.CHANGE_BLACK_BALL);
                                ChangeMapService.gI().openChangeMapTab(player);
                            }
                            break;
                        case ConstNpc.MENU_NOT_OPEN_BDW:
                            if (select == 0) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_BLACK_BALL_WAR);
                            }
                            break;
                    }
                }
            }

        };
    }

    public static Npc rong1_to_7s(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isHoldBlackBall()) {
                        this.createOtherMenu(player, ConstNpc.MENU_PHU_HP, "Ta có thể giúp gì cho ngươi?", "Phù hộ",
                                "Từ chối");
                    } else {
                        if (BossManager.gI().existBossOnPlayer(player)
                                || player.zone.items.stream()
                                        .anyMatch(itemMap -> ItemMapService.gI().isBlackBall(itemMap.itemTemplate.id))
                                || player.zone.getPlayers().stream().anyMatch(p -> p.iDMark.isHoldBlackBall())) {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_GO_HOME, "Ta có thể giúp gì cho ngươi?",
                                    "Về nhà", "Từ chối");
                        } else {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_GO_HOME, "Ta có thể giúp gì cho ngươi?",
                                    "Về nhà", "Từ chối", "Gọi BOSS");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHU_HP) {
                        if (select == 0) {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_PHU_HP,
                                    "Ta sẽ giúp ngươi tăng HP lên mức kinh hoàng, ngươi chọn đi",
                                    "x3 HP\n" + Util.numberToMoney(BlackBallWar.COST_X3) + " vàng",
                                    "x5 HP\n" + Util.numberToMoney(BlackBallWar.COST_X5) + " vàng",
                                    "x7 HP\n" + Util.numberToMoney(BlackBallWar.COST_X7) + " vàng",
                                    "Từ chối");
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_GO_HOME) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
                        } else if (select == 2) {
                            BossManager.gI().callBoss(player, mapId);
                        } else if (select == 1) {
                            this.npcChat(player, "Để ta xem ngươi trụ được bao lâu");
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PHU_HP) {
                        if (player.effectSkin.xHPKI > 1) {
                            Service.gI().sendThongBao(player, "Bạn đã được phù hộ rồi!");
                            return;
                        }
                        switch (select) {
                            case 0:
                                BlackBallWar.gI().xHPKI(player, BlackBallWar.X3);
                                break;
                            case 1:
                                BlackBallWar.gI().xHPKI(player, BlackBallWar.X5);
                                break;
                            case 2:
                                BlackBallWar.gI().xHPKI(player, BlackBallWar.X7);
                                break;
                            case 3:
                                this.npcChat(player, "Để ta xem ngươi trụ được bao lâu");
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc npcThienSu64(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 14) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Ta sẽ dẫn cậu tới Rừng Aurura với điều kiện\n 2. đạt 120 tỷ sức mạnh "
                                    + "\n 3. chi phí vào cổng  50 triệu vàng",
                            "Tới ngay", "Từ chối");
                }
                if (this.mapId == 7) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Ta sẽ dẫn cậu tới Rừng Aurura với điều kiện\n 2. đạt 120 tỷ sức mạnh "
                                    + "\n 3. chi phí vào cổng  50 triệu vàng",
                            "Tới ngay", "Từ chối");
                }
                if (this.mapId == 0) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Ta sẽ dẫn cậu tới Rừng Aurura với điều kiện\n 2. đạt 120 tỷ sức mạnh "
                                    + "\n 3. chi phí vào cổng  50 triệu vàng",
                            "Tới ngay", "Từ chối");
                }
                if (this.mapId == 146) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Cậu không chịu nổi khi ở đây sao?\nCậu sẽ khó mà mạnh lên được", "Trốn về", "Ở lại");
                }
                if (this.mapId == 147) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Cậu không chịu nổi khi ở đây sao?\nCậu sẽ khó mà mạnh lên được", "Trốn về", "Ở lại");
                }
                if (this.mapId == 148) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Cậu không chịu nổi khi ở đây sao?\nCậu sẽ khó mà mạnh lên được", "Trốn về", "Ở lại");
                }
                if (this.mapId == 48) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Đã tìm đủ nguyên liệu cho tôi chưa?\n Tôi sẽ giúp cậu mạnh lên kha khá đấy!", "Hướng Dẫn",
                            "Đổi Thức Ăn\nLấy Điểm", "Từ Chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu() && this.mapId == 7) {
                        if (select == 0) {
                            if (player.getSession().player.nPoint.power >= 120000000000L
                                    && player.inventory.gold > COST_HD) {
                                player.inventory.gold -= COST_HD;
                                Service.gI().sendMoney(player);
                                ChangeMapService.gI().changeMapBySpaceShip(player, 250, -1, 250);
                            } else {
                                this.npcChat(player, "Bạn chưa đủ điều kiện để vào");
                            }
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 14) {
                        if (select == 0) {
                            if (player.getSession().player.nPoint.power >= 120000000000L
                                    && player.inventory.gold > COST_HD) {
                                player.inventory.gold -= COST_HD;
                                Service.gI().sendMoney(player);
                                ChangeMapService.gI().changeMapBySpaceShip(player, 250, -1, 250);
                            } else {
                                this.npcChat(player, "Bạn chưa đủ điều kiện để vào");
                            }
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 0) {
                        if (select == 0) {
                            if (player.getSession().player.nPoint.power >= 120000000000L
                                    && player.inventory.gold > COST_HD) {
                                player.inventory.gold -= COST_HD;
                                Service.gI().sendMoney(player);
                                ChangeMapService.gI().changeMapBySpaceShip(player, 250, -1, 250);
                            } else {
                                this.npcChat(player, "Bạn chưa đủ điều kiện để vào");
                            }
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 147) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 0, -1, 450);
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 148) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 14, -1, 450);
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 146) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 7, -1, 450);
                        }
                        if (select == 1) {
                        }

                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 48) {
                        if (select == 0) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "x99 Thức Ăn Được 1 Điểm");
                        }
                        if (select == 1) {
                            CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.DOI_DIEM);
                        }

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                        switch (player.combineNew.typeCombine) {
                            case CombineServiceNew.DOI_DIEM:

                                if (select == 0) {
                                    CombineServiceNew.gI().startCombine(player, 0);
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHAN_RA_DO_THAN_LINH) {
                        if (select == 0) {
                            CombineServiceNew.gI().startCombine(player, 0);
                        }

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                        switch (player.combineNew.typeCombine) {
                            case CombineServiceNew.CHE_TAO_TRANG_BI_TS:

                                if (select == 0) {
                                    CombineServiceNew.gI().startCombine(player, 0);
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_CAP_DO_TS) {
                        if (select == 0) {
                            CombineServiceNew.gI().startCombine(player, 0);
                        }

                    }
                }
            }

        };
    }

    public static Npc bill(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Gặp Whis Để Đổi Thức Ăn Lấy Điểm Sau Đó Gặp Ta Để Mua Trang Bị Hủy Diệt",
                            "Điểm",
                            "Shop Hủy Diệt", "Đóng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (this.mapId) {
                        case 48:
                            switch (player.iDMark.getIndexMenu()) {
                                case ConstNpc.BASE_MENU:
                                    if (select == 0) {
                                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Mày Có " + player.inventory.coupon + " Điểm", "Đóng");
                                    }
                                    if (select == 1) {
                                        if (player.inventory.coupon == 0) {
                                            createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                    "Ngươi Không Có Điểm Vui Lòng Đổi Điểm Bằng Thức Ăn", "Đóng");
                                        } else {
                                            ShopServiceNew.gI().opendShop(player, "BILL", false);
                                            break;
                                        }
                                    }
                            }
                            break;
                        case 154:
                            switch (player.iDMark.getIndexMenu()) {
                                case ConstNpc.BASE_MENU:
                                    if (select == 0) {
                                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Mày Có " + player.inventory.coupon + " Điểm", "Đóng");
                                    }
                                    if (select == 1) {
                                        if (player.inventory.coupon == 0) {
                                            createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                    "Ngươi Không Có Điểm Vui Lòng Đổi Điểm Bằng Thức Ăn", "Đóng");
                                        } else {
                                            ShopServiceNew.gI().opendShop(player, "BILL", false);
                                            break;
                                        }
                                    }
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc whis(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 154) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Thử đánh với ta xem nào.\nNgươi còn 1 lượt cơ mà.",
                            "Nói chuyện", "Học tuyệt kỹ", "từ chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu() && this.mapId == 154) {
                        switch (select) {
                            case 0:
                                this.createOtherMenu(player, 5, "Ta sẽ giúp ngươi chế tạo trang bị thiên sứ",
                                        "Cửa hàng", "Chế tạo", "Đóng");
                                break;
                            case 1:
                                this.createOtherMenu(player, 6, "|1|Ta sẽ dạy ngươi tuyệt kỹ\n",
                                        "Đồng ý", "Từ chối");
                                break;

                        }
                    } else if (player.iDMark.getIndexMenu() == 5) {
                        switch (select) {
                            case 0:
                                ShopServiceNew.gI().opendShop(player, "SHOP_DA", false);
                                break;
                            case 1:
                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHE_TAO_TRANG_BI_TS);
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 6) {
                        switch (select) {
                            case 0:
                                if (player.gender == 0) {
                                    this.createOtherMenu(player, ConstNpc.HOC_SKILL_TD,
                                            "|7|Học Chiêu Siêu Cấp Trái Đất\n"
                                                    + "|2|[ YÊU CẦU BAN ĐẦU ]\n"
                                                    + "|5|+ 60 tỉ tiềm năng\n+ 50.000 hồng ngọc\n+ 1 tỉ vàng\n"
                                                    + "|2|Mỗi cấp học sẽ tăng thêm 10 tỉ tiềm năng và 90% thành thạo!\n"
                                                    + "|7|Chọn Level Skill Muốn Học",
                                            "Cấp 1", "Cấp 2", "Cấp 3", "Cấp 4", "Cấp 5", "Cấp 6", "Cấp 7", "Đóng");
                                    break;
                                } else {
                                    Service.gI().sendThongBao(player, "Học chiêu đúng với hành tinh của mình");
                                }
                                break;
                            case 1:
                                if (player.gender == 1) {
                                    this.createOtherMenu(player, ConstNpc.HOC_SKILL_NM, "|7|Học Chiêu Siêu Cấp Namek\n"
                                            + "|2|[ YÊU CẦU BAN ĐẦU ]\n"
                                            + "|5|+ 60 tỉ tiềm năng\n+ 50.000 hồng ngọc\n+ 1 tỉ vàng\n"
                                            + "|2|Mỗi cấp học sẽ tăng thêm 10 tỉ tiềm năng và 90% thành thạo!\n"
                                            + "|7|Chọn Level Skill Muốn Học", "Cấp 1", "Cấp 2", "Cấp 3", "Cấp 4",
                                            "Cấp 5", "Cấp 6", "Cấp 7", "Đóng");
                                    break;
                                } else {
                                    Service.gI().sendThongBao(player, "Học chiêu đúng với hành tinh của mình");
                                }
                                break;
                            case 2:
                                if (player.gender == 2) {
                                    this.createOtherMenu(player, ConstNpc.HOC_SKILL_XD, "|7|Học Chiêu Siêu Cấp Xayda\n"
                                            + "|2|[ YÊU CẦU BAN ĐẦU ]\n"
                                            + "|5|+ 60 tỉ tiềm năng\n+ 50.000 hồng ngọc\n+ 1 tỉ vàng\n"
                                            + "|2|Mỗi cấp học sẽ tăng thêm 10 tỉ tiềm năng và 90% thành thạo!\n"
                                            + "|7|Chọn Level Skill Muốn Học", "Cấp 1", "Cấp 2", "Cấp 3", "Cấp 4",
                                            "Cấp 5", "Cấp 6", "Cấp 7", "Đóng");
                                    break;
                                } else {
                                    Service.gI().sendThongBao(player, "Học chiêu đúng với hành tinh của mình");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.HOC_SKILL_TD) {
                        switch (select) {
                            case 0:
                                Item td1 = InventoryServiceNew.gI().findItemBag(player, 1417);
                                if (td1 != null) {
                                    Service.gI().sendThongBao(player, "Người có rồi mà");
                                    return;
                                }
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                    Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                    return;
                                }
                                if (player.gender == 0 && td1 == null && player.nPoint.tiemNang >= 60000000000L
                                        && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                    player.nPoint.tiemNang -= 60000000000L;
                                    player.inventory.ruby -= 50000;
                                    player.inventory.gold -= 1000000000L;
                                    Service.gI().point(player);
                                    Item item = ItemService.gI().createNewItem((short) (1417));
                                    item.itemOptions.add(new Item.ItemOption(30, 1));
                                    InventoryServiceNew.gI().addItemBag(player, item);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Học thành công");
                                } else {
                                    Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                }
                                break;
                            case 1:
                                Item td2 = InventoryServiceNew.gI().findItemBag(player, 1418);
                                if (td2 != null) {
                                    Service.gI().sendThongBao(player, "Người có rồi mà");
                                    return;
                                }
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                    Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                    return;
                                }
                                if (player.gender == 0 && td2 == null && player.nPoint.tiemNang >= 70000000000L
                                        && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                    player.nPoint.tiemNang -= 70000000000L;
                                    player.inventory.ruby -= 50000;
                                    player.inventory.gold -= 1000000000L;
                                    Service.gI().point(player);
                                    Item item = ItemService.gI().createNewItem((short) (1418));
                                    item.itemOptions.add(new Item.ItemOption(30, 1));
                                    InventoryServiceNew.gI().addItemBag(player, item);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Học thành công");
                                } else {
                                    Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                }
                                break;
                            case 2:
                                Item td3 = InventoryServiceNew.gI().findItemBag(player, 1419);
                                if (td3 != null) {
                                    Service.gI().sendThongBao(player, "Người có rồi mà");
                                    return;
                                }
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                    Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                    return;
                                }
                                if (player.gender == 0 && td3 == null && player.nPoint.tiemNang >= 80000000000L
                                        && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                    player.nPoint.tiemNang -= 80000000000L;
                                    player.inventory.ruby -= 50000;
                                    player.inventory.gold -= 1000000000L;
                                    Service.gI().point(player);
                                    Item item = ItemService.gI().createNewItem((short) (1419));
                                    item.itemOptions.add(new Item.ItemOption(30, 1));
                                    InventoryServiceNew.gI().addItemBag(player, item);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Học thành công");
                                } else {
                                    Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                }
                                break;
                            case 3:
                                Item td4 = InventoryServiceNew.gI().findItemBag(player, 1420);
                                if (td4 != null) {
                                    Service.gI().sendThongBao(player, "Người có rồi mà");
                                    return;
                                }
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                    Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                    return;
                                }
                                if (player.gender == 0 && td4 == null && player.nPoint.tiemNang >= 90000000000L
                                        && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                    player.nPoint.tiemNang -= 90000000000L;
                                    player.inventory.ruby -= 50000;
                                    player.inventory.gold -= 1000000000L;
                                    Service.gI().point(player);
                                    Item item = ItemService.gI().createNewItem((short) (1420));
                                    item.itemOptions.add(new Item.ItemOption(30, 1));
                                    InventoryServiceNew.gI().addItemBag(player, item);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Học thành công");
                                } else {
                                    Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                }
                                break;
                            case 4:
                                Item td5 = InventoryServiceNew.gI().findItemBag(player, 1421);
                                if (td5 != null) {
                                    Service.gI().sendThongBao(player, "Người có rồi mà");
                                    return;
                                }
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                    Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                    return;
                                }
                                if (player.gender == 0 && td5 == null && player.nPoint.tiemNang >= 100000000000L
                                        && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                    player.nPoint.tiemNang -= 100000000000L;
                                    player.inventory.ruby -= 50000;
                                    player.inventory.gold -= 1000000000L;
                                    Service.gI().point(player);
                                    Item item = ItemService.gI().createNewItem((short) (1421));
                                    item.itemOptions.add(new Item.ItemOption(30, 1));
                                    InventoryServiceNew.gI().addItemBag(player, item);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Học thành công");
                                } else {
                                    Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                }
                                break;
                            case 5:
                                Item td6 = InventoryServiceNew.gI().findItemBag(player, 1422);
                                if (td6 != null) {
                                    Service.gI().sendThongBao(player, "Người có rồi mà");
                                    return;
                                }
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                    Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                    return;
                                }
                                if (player.gender == 0 && td6 == null && player.nPoint.tiemNang >= 110000000000L
                                        && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                    player.nPoint.tiemNang -= 110000000000L;
                                    player.inventory.ruby -= 50000;
                                    player.inventory.gold -= 1000000000L;
                                    Service.gI().point(player);
                                    Item item = ItemService.gI().createNewItem((short) (1422));
                                    item.itemOptions.add(new Item.ItemOption(30, 1));
                                    InventoryServiceNew.gI().addItemBag(player, item);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Học thành công");
                                } else {
                                    Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                }
                                break;
                            case 6:
                                Item td7 = InventoryServiceNew.gI().findItemBag(player, 1423);
                                if (td7 != null) {
                                    Service.gI().sendThongBao(player, "Người có rồi mà");
                                    return;
                                }
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                    Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                    return;
                                }
                                if (player.gender == 0 && td7 == null && player.nPoint.tiemNang >= 120000000000L
                                        && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                    player.nPoint.tiemNang -= 120000000000L;
                                    player.inventory.ruby -= 50000;
                                    player.inventory.gold -= 1000000000L;
                                    Service.gI().point(player);
                                    Item item = ItemService.gI().createNewItem((short) (1423));
                                    item.itemOptions.add(new Item.ItemOption(30, 1));
                                    InventoryServiceNew.gI().addItemBag(player, item);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Học thành công");
                                } else {
                                    Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.HOC_SKILL_NM) {
                        switch (select) {
                            case 0:
                                Item nm1 = InventoryServiceNew.gI().findItemBag(player, 1431);
                                if (nm1 != null) {
                                    Service.gI().sendThongBao(player, "Người có rồi mà");
                                    return;
                                }
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                    Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                    return;
                                }
                                if (player.gender == 1 && nm1 == null && player.nPoint.tiemNang >= 60000000000L
                                        && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                    player.nPoint.tiemNang -= 60000000000L;
                                    player.inventory.ruby -= 50000;
                                    player.inventory.gold -= 1000000000L;
                                    Service.gI().point(player);
                                    Item item = ItemService.gI().createNewItem((short) (1431));
                                    item.itemOptions.add(new Item.ItemOption(30, 1));
                                    InventoryServiceNew.gI().addItemBag(player, item);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Học thành công");
                                } else {
                                    Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                }
                                break;
                            case 1:
                                Item nm2 = InventoryServiceNew.gI().findItemBag(player, 1432);
                                if (nm2 != null) {
                                    Service.gI().sendThongBao(player, "Người có rồi mà");
                                    return;
                                }
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                    Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                    return;
                                }
                                if (player.gender == 1 && nm2 == null && player.nPoint.tiemNang >= 70000000000L
                                        && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                    player.nPoint.tiemNang -= 70000000000L;
                                    player.inventory.ruby -= 50000;
                                    player.inventory.gold -= 1000000000L;
                                    Service.gI().point(player);
                                    Item item = ItemService.gI().createNewItem((short) (1432));
                                    item.itemOptions.add(new Item.ItemOption(30, 1));
                                    InventoryServiceNew.gI().addItemBag(player, item);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Học thành công");
                                } else {
                                    Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                }
                                break;
                            case 2:
                                Item nm3 = InventoryServiceNew.gI().findItemBag(player, 1433);
                                if (nm3 != null) {
                                    Service.gI().sendThongBao(player, "Người có rồi mà");
                                    return;
                                }
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                    Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                    return;
                                }
                                if (player.gender == 1 && nm3 == null && player.nPoint.tiemNang >= 80000000000L
                                        && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                    player.nPoint.tiemNang -= 80000000000L;
                                    player.inventory.ruby -= 50000;
                                    player.inventory.gold -= 1000000000L;
                                    Service.gI().point(player);
                                    Item item = ItemService.gI().createNewItem((short) (1433));
                                    item.itemOptions.add(new Item.ItemOption(30, 1));
                                    InventoryServiceNew.gI().addItemBag(player, item);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Học thành công");
                                } else {
                                    Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                }
                                break;
                            case 3:
                                Item nm4 = InventoryServiceNew.gI().findItemBag(player, 1434);
                                if (nm4 != null) {
                                    Service.gI().sendThongBao(player, "Người có rồi mà");
                                    return;
                                }
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                    Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                    return;
                                }
                                if (player.gender == 1 && nm4 == null && player.nPoint.tiemNang >= 90000000000L
                                        && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                    player.nPoint.tiemNang -= 90000000000L;
                                    player.inventory.ruby -= 50000;
                                    player.inventory.gold -= 1000000000L;
                                    Service.gI().point(player);
                                    Item item = ItemService.gI().createNewItem((short) (1434));
                                    item.itemOptions.add(new Item.ItemOption(30, 1));
                                    InventoryServiceNew.gI().addItemBag(player, item);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Học thành công");
                                } else {
                                    Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                }
                                break;
                            case 4:
                                Item nm5 = InventoryServiceNew.gI().findItemBag(player, 1435);
                                if (nm5 != null) {
                                    Service.gI().sendThongBao(player, "Người có rồi mà");
                                    return;
                                }
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                    Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                    return;
                                }
                                if (player.gender == 1 && nm5 == null && player.nPoint.tiemNang >= 100000000000L
                                        && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                    player.nPoint.tiemNang -= 100000000000L;
                                    player.inventory.ruby -= 50000;
                                    player.inventory.gold -= 1000000000L;
                                    Service.gI().point(player);
                                    Item item = ItemService.gI().createNewItem((short) (1435));
                                    item.itemOptions.add(new Item.ItemOption(30, 1));
                                    InventoryServiceNew.gI().addItemBag(player, item);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Học thành công");
                                } else {
                                    Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                }
                                break;
                            case 5:
                                Item nm6 = InventoryServiceNew.gI().findItemBag(player, 1436);
                                if (nm6 != null) {
                                    Service.gI().sendThongBao(player, "Người có rồi mà");
                                    return;
                                }
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                    Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                    return;
                                }
                                if (player.gender == 1 && nm6 == null && player.nPoint.tiemNang >= 110000000000L
                                        && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                    player.nPoint.tiemNang -= 110000000000L;
                                    player.inventory.ruby -= 50000;
                                    player.inventory.gold -= 1000000000L;
                                    Service.gI().point(player);
                                    Item item = ItemService.gI().createNewItem((short) (1436));
                                    item.itemOptions.add(new Item.ItemOption(30, 1));
                                    InventoryServiceNew.gI().addItemBag(player, item);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Học thành công");
                                } else {
                                    Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                }
                                break;
                            case 6:
                                Item nm7 = InventoryServiceNew.gI().findItemBag(player, 1437);
                                if (nm7 != null) {
                                    Service.gI().sendThongBao(player, "Người có rồi mà");
                                    return;
                                }
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                    Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                    return;
                                }
                                if (player.gender == 1 && nm7 == null && player.nPoint.tiemNang >= 120000000000L
                                        && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                    player.nPoint.tiemNang -= 120000000000L;
                                    player.inventory.ruby -= 50000;
                                    player.inventory.gold -= 1000000000L;
                                    Service.gI().point(player);
                                    Item item = ItemService.gI().createNewItem((short) (1437));
                                    item.itemOptions.add(new Item.ItemOption(30, 1));
                                    InventoryServiceNew.gI().addItemBag(player, item);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Học thành công");
                                } else {
                                    Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.HOC_SKILL_XD) {
                        switch (select) {
                            case 0:
                                Item xd1 = InventoryServiceNew.gI().findItemBag(player, 1424);
                                if (xd1 != null) {
                                    Service.gI().sendThongBao(player, "Người có rồi mà");
                                    return;
                                }
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                    Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                    return;
                                }
                                if (player.gender == 2 && xd1 == null && player.nPoint.tiemNang >= 60000000000L
                                        && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                    player.nPoint.tiemNang -= 60000000000L;
                                    player.inventory.ruby -= 50000;
                                    player.inventory.gold -= 1000000000L;
                                    Service.gI().point(player);
                                    Item item = ItemService.gI().createNewItem((short) (1424));
                                    item.itemOptions.add(new Item.ItemOption(30, 1));
                                    InventoryServiceNew.gI().addItemBag(player, item);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Học thành công");
                                } else {
                                    Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                }
                                break;
                            case 1:
                                Item xd2 = InventoryServiceNew.gI().findItemBag(player, 1425);
                                if (xd2 != null) {
                                    Service.gI().sendThongBao(player, "Người có rồi mà");
                                    return;
                                }
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                    Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                    return;
                                }
                                if (player.gender == 2 && xd2 == null && player.nPoint.tiemNang >= 70000000000L
                                        && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                    player.nPoint.tiemNang -= 70000000000L;
                                    player.inventory.ruby -= 50000;
                                    player.inventory.gold -= 1000000000L;
                                    Service.gI().point(player);
                                    Item item = ItemService.gI().createNewItem((short) (1425));
                                    item.itemOptions.add(new Item.ItemOption(30, 1));
                                    InventoryServiceNew.gI().addItemBag(player, item);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Học thành công");
                                } else {
                                    Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                }
                                break;
                            case 2:
                                Item xd3 = InventoryServiceNew.gI().findItemBag(player, 1426);
                                if (xd3 != null) {
                                    Service.gI().sendThongBao(player, "Người có rồi mà");
                                    return;
                                }
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                    Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                    return;
                                }
                                if (player.gender == 2 && xd3 == null && player.nPoint.tiemNang >= 80000000000L
                                        && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                    player.nPoint.tiemNang -= 80000000000L;
                                    player.inventory.ruby -= 50000;
                                    player.inventory.gold -= 1000000000L;
                                    Service.gI().point(player);
                                    Item item = ItemService.gI().createNewItem((short) (1426));
                                    item.itemOptions.add(new Item.ItemOption(30, 1));
                                    InventoryServiceNew.gI().addItemBag(player, item);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Học thành công");
                                } else {
                                    Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                }
                                break;
                            case 3:
                                Item xd4 = InventoryServiceNew.gI().findItemBag(player, 1427);
                                if (xd4 != null) {
                                    Service.gI().sendThongBao(player, "Người có rồi mà");
                                    return;
                                }
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                    Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                    return;
                                }
                                if (player.gender == 2 && xd4 == null && player.nPoint.tiemNang >= 90000000000L
                                        && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                    player.nPoint.tiemNang -= 90000000000L;
                                    player.inventory.ruby -= 50000;
                                    player.inventory.gold -= 1000000000L;
                                    Service.gI().point(player);
                                    Item item = ItemService.gI().createNewItem((short) (1427));
                                    item.itemOptions.add(new Item.ItemOption(30, 1));
                                    InventoryServiceNew.gI().addItemBag(player, item);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Học thành công");
                                } else {
                                    Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                }
                                break;
                            case 4:
                                Item xd5 = InventoryServiceNew.gI().findItemBag(player, 1428);
                                if (xd5 != null) {
                                    Service.gI().sendThongBao(player, "Người có rồi mà");
                                    return;
                                }
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                    Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                    return;
                                }
                                if (player.gender == 2 && xd5 == null && player.nPoint.tiemNang >= 100000000000L
                                        && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                    player.nPoint.tiemNang -= 100000000000L;
                                    player.inventory.ruby -= 50000;
                                    player.inventory.gold -= 1000000000L;
                                    Service.gI().point(player);
                                    Item item = ItemService.gI().createNewItem((short) (1428));
                                    item.itemOptions.add(new Item.ItemOption(30, 1));
                                    InventoryServiceNew.gI().addItemBag(player, item);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Học thành công");
                                } else {
                                    Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                }
                                break;
                            case 5:
                                Item xd6 = InventoryServiceNew.gI().findItemBag(player, 1429);
                                if (xd6 != null) {
                                    Service.gI().sendThongBao(player, "Người có rồi mà");
                                    return;
                                }
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                    Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                    return;
                                }
                                if (player.gender == 2 && xd6 == null && player.nPoint.tiemNang >= 110000000000L
                                        && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                    player.nPoint.tiemNang -= 110000000000L;
                                    player.inventory.ruby -= 50000;
                                    player.inventory.gold -= 1000000000L;
                                    Service.gI().point(player);
                                    Item item = ItemService.gI().createNewItem((short) (1429));
                                    item.itemOptions.add(new Item.ItemOption(30, 1));
                                    InventoryServiceNew.gI().addItemBag(player, item);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Học thành công");
                                } else {
                                    Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                }
                                break;
                            case 6:
                                Item xd7 = InventoryServiceNew.gI().findItemBag(player, 1430);
                                if (xd7 != null) {
                                    Service.gI().sendThongBao(player, "Người có rồi mà");
                                    return;
                                }
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                    Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                    return;
                                }
                                if (player.gender == 2 && xd7 == null && player.nPoint.tiemNang >= 120000000000L
                                        && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                    player.nPoint.tiemNang -= 120000000000L;
                                    player.inventory.ruby -= 50000;
                                    player.inventory.gold -= 1000000000L;
                                    Service.gI().point(player);
                                    Item item = ItemService.gI().createNewItem((short) (1430));
                                    item.itemOptions.add(new Item.ItemOption(30, 1));
                                    InventoryServiceNew.gI().addItemBag(player, item);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Học thành công");
                                } else {
                                    Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DAP_DO) {
                        if (select == 0) {
                            CombineServiceNew.gI().startCombine(player, 0);
                        }
                    } else if (player.iDMark.getIndexMenu() == 6) {
                        switch (select) {
                            case 0:
                                Item sach = InventoryServiceNew.gI().findItemBag(player, 1320);
                                if (sach != null && sach.quantity >= 9999 && player.inventory.gold >= 10000000
                                        && player.inventory.gem > 99 && player.nPoint.power >= 1000000000L) {

                                    if (player.gender == 2) {
                                        SkillService.gI().learSkillSpecial(player, Skill.LIEN_HOAN_CHUONG);
                                    }
                                    if (player.gender == 0) {
                                        SkillService.gI().learSkillSpecial(player, Skill.SUPER_KAME);
                                    }
                                    if (player.gender == 1) {
                                        SkillService.gI().learSkillSpecial(player, Skill.MA_PHONG_BA);
                                    }
                                    InventoryServiceNew.gI().subQuantityItem(player.inventory.itemsBag, sach, 9999);
                                    player.inventory.gold -= 10000000;
                                    player.inventory.gem -= 99;
                                    InventoryServiceNew.gI().sendItemBags(player);
                                } else if (player.nPoint.power < 1000000000L) {
                                    Service.getInstance().sendThongBao(player,
                                            "Ngươi không đủ sức mạnh để học tuyệt kỹ");
                                    return;
                                } else if (sach.quantity <= 9999) {
                                    int sosach = 9999 - sach.quantity;
                                    Service.getInstance().sendThongBao(player,
                                            "Ngươi còn thiếu " + sosach + " bí kíp nữa.\nHãy tìm đủ rồi đến gặp ta.");
                                    return;
                                } else if (player.inventory.gold <= 10000000) {
                                    Service.getInstance().sendThongBao(player, "Hãy có đủ vàng thì quay lại gặp ta.");
                                    return;
                                } else if (player.inventory.gem <= 99) {
                                    Service.getInstance().sendThongBao(player,
                                            "Hãy có đủ ngọc xanh thì quay lại gặp ta.");
                                    return;
                                }

                                // break;
                        }
                    }
                }
            }

        };
    }

    public static Npc chuvit(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 250) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Cậu không chịu nổi khi ở đây sao?\nCậu sẽ khó mà mạnh lên được", "Trốn về", "Ở lại");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu() && this.mapId == 250) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 14, -1, 450);
                        }
                        if (select == 1) {
                        }

                    }
                }
            }

        };
    }

    public static Npc ngudan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 178) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Thử đánh với ta xem nào.\nNgươi còn 1 lượt cơ mà.",
                            "Nói chuyện", "từ chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu() && this.mapId == 178) {
                        switch (select) {
                            case 0:
                                this.createOtherMenu(player, 178, "Ta sẽ giúp ngươi Câu Cá", "Cửa Hàng", "Về Đảo",
                                        "Quà Top", "Đóng");
                                break;
                            case 1:

                        }
                    } else if (player.iDMark.getIndexMenu() == 178) {
                        switch (select) {
                            case 0:
                                ShopServiceNew.gI().opendShop(player, "SHOP_CAN", false);
                                break;
                            case 1:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 295);
                                break;
                            case 2:
                                ShopServiceNew.gI().opendShop(player, "TOP_CAN", false);
                                break;
                        }
                    }
                }
            }

        };
    }

    public static Npc thangod(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 5) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Thử đánh với ta xem nào.\nNgươi còn 1 lượt cơ mà.",
                            "Nói chuyện", "từ chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu() && this.mapId == 5) {
                        switch (select) {
                            case 0:
                                this.createOtherMenu(player, 5, "Ta sẽ giúp ngươi", "Quà Top", "Đi Câu Cá", "Đóng");
                                break;
                            case 1:

                        }
                    } else if (player.iDMark.getIndexMenu() == 5) {
                        switch (select) {
                            case 0:
                                ShopServiceNew.gI().opendShop(player, "SHOP_TOP", false);
                                break;
                            case 1:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 178, -1, 295);
                                break;
                        }
                    }
                }
            }

        };
    }

    public static Npc boMong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (this.mapId == 47 || this.mapId == 84) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "|4|Cậu muốn giúp đỡ tôi à\b tôi sẽ giao cho cậu vài nhiệm vụ của hôm nay.\n",
                                    "Nhiệm vụ\nhàng ngày", "GifdCode", "Từ chối");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 47 || this.mapId == 84) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    if (player.playerTask.sideTask.template != null) {
                                        String npcSay = "Nhiệm vụ hiện tại: " + player.playerTask.sideTask.getName()
                                                + " ("
                                                + player.playerTask.sideTask.getLevel() + ")"
                                                + "\nHiện tại đã hoàn thành: " + player.playerTask.sideTask.count + "/"
                                                + player.playerTask.sideTask.maxCount + " ("
                                                + player.playerTask.sideTask.getPercentProcess()
                                                + "%)\nSố nhiệm vụ còn lại trong ngày: "
                                                + player.playerTask.sideTask.leftTask + "/" + ConstTask.MAX_SIDE_TASK;
                                        this.createOtherMenu(player, ConstNpc.MENU_OPTION_PAY_SIDE_TASK,
                                                npcSay, "Trả nhiệm\nvụ", "Hủy nhiệm\nvụ");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK,
                                                "Tôi có vài nhiệm vụ theo cấp bậc, "
                                                        + "sức cậu có thể làm được cái nào?",
                                                "Dễ", "Bình thường", "Khó", "Siêu khó", "Địa ngục", "Từ chối");
                                    }
                                    break;
                                case 1:
                                    // System.out.println(".confirmMenu() 1");
                                    // ThanhTichPlayer.SendThanhTich(player);
                                    Input.gI().createFormGiftCode(player);
                                    break;
                                // case 2:
                                //// if (false) {
                                //// Service.gI().sendThongBao(player, "Chức năng hiện tại đang bảo trì");
                                //// return;
                                //// }
                                // OnlineHangNgay.SendThanhTich(player);
                                // break;
                                case 3:
                                    // if (true) {
                                    // Service.gI().sendThongBao(player, "Chức năng hiện tại đang bảo trì");
                                    // return;
                                    // }
                                    // QuaNapHangNgay.SendThanhTich(player);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK) {
                            switch (select) {
                                case 0:
                                case 1:
                                case 2:
                                case 3:
                                case 4:
                                    TaskService.gI().changeSideTask(player, (byte) select);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PAY_SIDE_TASK) {
                            switch (select) {
                                case 0:
                                    TaskService.gI().paySideTask(player);
                                    break;
                                case 1:
                                    TaskService.gI().removeSideTask(player);
                                    break;
                            }

                        }
                    }
                }
            }
        };
    }

    public static Npc karin(int mapId, int status, int cx, int cy, int tempId, int avatar) {
        return new Npc(mapId, status, cx, cy, tempId, avatar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player) && this.mapId == 46) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        String message;
                        if (player.playerTask.taskMain.id == 5 && player.playerTask.taskMain.index == 5) {
                            if (player.istrain) {
                                message = "Muốn chiến thắng Tàu Pảy Pảy phải đánh bại được ta";
                                this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Hủy đăng ký tập tự động",
                                        "Tập luyện với\nThần Mèo", "Thách đấu với\nThần Mèo");
                            } else {
                                message = "Muốn chiến thắng Tàu Pảy Pảy phải đánh bại được ta";
                                this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Đăng ký tập tự động",
                                        "Tập luyện với\nThần Mèo", "Thách đấu với\nThần Mèo");
                            }
                        } else if (player.typetrain == 0 && !player.istrain) {
                            message = "Từ giờ Yajirô sẽ luyện tập cùng ngươi. Yajirô đã lên đây đã từng lên đây tập luyện và bây giờ hắn mạnh hơn ta đấy";
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Đăng ký tập tự động",
                                    "Tập luyện với Yajirô", "Thách đấu Yajirô");
                        } else if (player.typetrain != 0 && player.istrain) {
                            message = "Con hãy bay theo cây Gậy KhanhDTK trên đỉnh tháp để đến Thần Điện gặp Thượng Đế\nCon rất xứng đáng để làm đệ tự của ông ấy";
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Hủy đăng ký tập tự động",
                                    "Tập luyện với Yajirô", "Tập luyện với thần mèo");
                        } else if (player.typetrain == 0 && player.istrain) {
                            message = "Từ giờ Yajirô sẽ luyện tập cùng ngươi. Yajirô đã lên đây đã từng lên đây tập luyện và bây giờ hắn mạnh hơn ta đấy";
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Hủy đăng ký tập tự động",
                                    "Tập luyện với Yajirô", "Thách đấu Yajirô");
                        } else {
                            message = "Con hãy bay theo cây Gậy KhanhDTK trên đỉnh tháp để đến Thần Điện gặp Thượng Đế\nCon rất xứng đáng để làm đệ tự của ông ấy";
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Đăng ký tập tự động",
                                    "Tập luyện với Yajirô", "Tập luyện với thần mèo");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player) && this.mapId == 46) {
                    if (player.iDMark.isBaseMenu()) {
                        if (select == 0) {
                            if (!player.istrain) {
                                this.createOtherMenu(player, ConstNpc.MENU_TRAIN_OFFLINE,
                                        "Đăng ký để mỗi khi Offline quá 30 phút, con sẽ được tự động luyện tập với tốc độ "
                                                + player.nPoint.getexp() + " sức mạnh mỗi phút",
                                        "Hướng dẫn thêm", "Đồng ý 1 ngọc mỗi lần", "Không đồng ý");
                            } else {
                                player.istrain = false;
                                this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        "Con đã hủy thành công đăng ký tập tự động", "Đóng");
                            }
                        } else if (select == 1) {
                            if (player.playerTask.taskMain.id == 5 && player.playerTask.taskMain.index == 5) {
                                this.createOtherMenu(player, ConstNpc.MENU_TRAIN_OFFLINE_TRY0,
                                        "Con có chắc muốn tập luyện?\nTập luyện với mèo thần Karin?",
                                        "Đồng ý luyện tập", "Không đồng ý");
                            } else {
                                this.createOtherMenu(player, ConstNpc.MENU_TRAIN_OFFLINE_TRY0,
                                        "Con có chắc muốn tập luyện?\nTập luyện với "
                                                + player.nPoint.getNameNPC(player, this, (byte) select) + " sẽ tăng "
                                                + player.nPoint.getExpbyNPC(player, this, (byte) select)
                                                + " sức mạnh mỗi phút",
                                        "Đồng ý luyện tập", "Không đồng ý");
                            }
                        } else if (select == 2) {
                            if (player.playerTask.taskMain.id == 5 && player.playerTask.taskMain.index == 5) {
                                this.createOtherMenu(player, ConstNpc.MENU_TRAIN_OFFLINE_TRY1,
                                        "Con có chắc muốn thách đấu?\nThách đấu với mèo thần Karin?",
                                        "Đồng ý thách đấu", "Không đồng ý");
                            } else if (player.typetrain != 0) {
                                this.createOtherMenu(player, ConstNpc.MENU_TRAIN_OFFLINE_TRY1,
                                        "Con có chắc muốn tập luyện?\nTập luyện với "
                                                + player.nPoint.getNameNPC(player, this, (byte) select) + " sẽ tăng "
                                                + player.nPoint.getExpbyNPC(player, this, (byte) select)
                                                + " sức mạnh mỗi phút",
                                        "Đồng ý luyện tập", "Không đồng ý");
                            } else {
                                player.setfight((byte) 1, (byte) 0);
                                player.zone.load_Me_To_Another(player);
                                player.zone.load_Another_To_Me(player);

                            }
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_TRAIN_OFFLINE) {
                        switch (select) {
                            case 0:
                                Service.gI().sendPopUpMultiLine(player, tempId, this.avartar,
                                        ConstNpc.INFOR_TRAIN_OFFLINE);
                                break;
                            case 1:
                                player.istrain = true;
                                NpcService.gI().createTutorial(player, this.avartar,
                                        "Từ giờ, quá 30 phút Offline con sẽ tự động luyện tập");
                                break;
                            case 3:
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_TRAIN_OFFLINE_TRY0) {
                        switch (select) {
                            case 0:
                                if (player.playerTask.taskMain.id == 5 && player.playerTask.taskMain.index == 5) {
                                    player.setfight((byte) 0, (byte) 1);
                                    player.zone.load_Me_To_Another(player);
                                    player.zone.load_Another_To_Me(player);
                                    player.zone.mapInfo(player);
                                    com.KhanhDTK.data.DataGame.updateMap(player.getSession());
                                    try {
                                        new MeoThan(BossID.MEO_THAN, BossesData.THAN_MEO, player.zone, this.cx,
                                                this.cy);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    player.setfight((byte) 0, (byte) 0);
                                    player.zone.load_Me_To_Another(player);
                                    player.zone.load_Another_To_Me(player);
                                }

                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_TRAIN_OFFLINE_TRY1) {
                        switch (select) {
                            case 0:
                                if (player.playerTask.taskMain.id == 5 && player.playerTask.taskMain.index == 5) {
                                    player.setfight((byte) 1, (byte) 1);
                                } else {
                                    player.setfight((byte) 0, (byte) 1);
                                }
                                player.zone.load_Me_To_Another(player);
                                player.zone.load_Another_To_Me(player);
                                player.zone.mapInfo(player);
                                com.KhanhDTK.data.DataGame.updateMap(player.getSession());
                                try {
                                    new MeoThan(BossID.MEO_THAN, BossesData.THAN_MEO, player.zone, this.cx, this.cy);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                        }
                    }
                } else if (this.mapId == 104) {
                    if (player.iDMark.isBaseMenu() && select == 0) {
                        ShopServiceNew.gI().opendShop(player, "BUNMA_LINHTHU", true);
                    }
                }

            }
        };
    }

    public static Npc vados(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        createOtherMenu(player, ConstNpc.BASE_MENU,
                                "|2|Ngươi Muốn Làm Gì?",
                                "Tới Khu Vực Thiên Tử", "Đóng");
                    }
                    if (this.mapId == 182) {
                        createOtherMenu(player, ConstNpc.BASE_MENU,
                                "|2|Đây là khu vực thiên tử , bạn muốn làm gì ?",
                                "Đổi Chân Thiên Tử Ngày", "Đổi Chân Thiên Tử Vĩnh Viễn", "Thông tin chi tiết",
                                "Về Đảo Kame");
                    }
                    if (this.mapId == 187) {
                        createOtherMenu(player, ConstNpc.BASE_MENU,
                                "|2|Đây là Map Vip , bạn muốn làm gì ?",
                                "Về Đảo Kame");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (this.mapId) {
                        case 182:
                            switch (player.iDMark.getIndexMenu()) {
                                case ConstNpc.BASE_MENU:
                                    if (select == 0) {
                                        if (InventoryServiceNew.gI().findItem(player.inventory.itemsBag,
                                                1279) == null) {
                                            Service.gI().sendThongBao(player, "Cần Có Đá Hoàng Kim");
                                            return;
                                        }
                                        if (InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 457) == null) {
                                            Service.gI().sendThongBao(player, "Cần Có Thỏi Vàng");
                                            return;
                                        }
                                        InventoryServiceNew.gI().subQuantityItemsBag(player,
                                                InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1279), 9);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player,
                                                InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 457), 10);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Item item = ItemService.gI().createNewItem((short) (1300));
                                        item.itemOptions.add(new Item.ItemOption(50, 5));
                                        item.itemOptions.add(new Item.ItemOption(77, 5));
                                        item.itemOptions.add(new Item.ItemOption(103, 5));
                                        item.itemOptions.add(new Item.ItemOption(207, 0));
                                        item.itemOptions.add(new Item.ItemOption(93, Util.nextInt(0, 3)));
                                        //
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                        Service.gI().sendThongBao(player,
                                                "Chúc Mừng Bạn Nhận Được " + item.template.name);

                                    }
                                    if (select == 1) {
                                        if (InventoryServiceNew.gI().findItem(player.inventory.itemsBag,
                                                1279) == null) {
                                            Service.gI().sendThongBao(player, "Cần Có Đá Hoàng Kim");
                                            return;
                                        }
                                        if (InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 457) == null) {
                                            Service.gI().sendThongBao(player, "Cần Có Thỏi Vàng");
                                            return;
                                        }
                                        InventoryServiceNew.gI().subQuantityItemsBag(player,
                                                InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1279), 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player,
                                                InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 457), 20);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Item item = ItemService.gI().createNewItem((short) (1300));
                                        item.itemOptions.add(new Item.ItemOption(50, 5));
                                        item.itemOptions.add(new Item.ItemOption(77, 5));
                                        item.itemOptions.add(new Item.ItemOption(103, 5));
                                        item.itemOptions.add(new Item.ItemOption(207, 0));
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                        Service.gI().sendThongBao(player,
                                                "Chúc Mừng Bạn Nhận Được " + item.template.name);

                                    }
                                    if (select == 2) {
                                        createOtherMenu(player, ConstNpc.BASE_MENU + 3,
                                                "|2|Hạ gục quái tại map Thiên Tử sẽ có tỉ lệ rơi Đá Hoàng Kim\n"
                                                        + "Sẽ có boss Goku SSJ4 xuất hiện ngẫu nhiên tại Map thiên tử\n"
                                                        + "Sau khi hạ gục boss sẽ rơi Đá Hoàng Kim",
                                                "Đóng");
                                        break;
                                    }
                                    if (select == 3) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 210);
                                        break;
                                    }
                                    break;
                            }
                            break;
                        case 5:
                            switch (player.iDMark.getIndexMenu()) {
                                case ConstNpc.BASE_MENU:
                                    if (select == 0) {
                                        ChangeMapService.gI().changeMapInYard(player, 182, -1, 160);
                                        break;
                                    }
                                    if (select == 1) {
                                        ChangeMapService.gI().changeMapInYard(player, 187, -1, 426);
                                        break;
                                    }
                                    break;
                            }
                            break;
                        case 187:
                            switch (player.iDMark.getIndexMenu()) {
                                case ConstNpc.BASE_MENU:
                                    if (select == 0) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 210);
                                        break;
                                    }
                                    break;
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc gokuSSJ_1(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 80) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Xin chào, tôi có thể giúp gì cho cậu?",
                                "Tới hành tinh\nYardart", "Từ chối");
                    } else if (this.mapId == 131) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Xin chào, tôi có thể giúp gì cho cậu?",
                                "Quay về", "Từ chối");
                    } else {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.BASE_MENU:
                            if (this.mapId == 131) {
                                if (select == 0) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 80, -1, 870);
                                }
                            }
                            if (this.mapId == 80) {
                                if (select == 0) {
                                    ChangeMapService.gI().changeMap(player, 131, -1, 901, 240);
                                }
                            }
                            break;
                    }
                }
            }
        };
    }

    private static Npc gapthu(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, 1234,
                                "|4| Máy gắp kẹo Halloween Ngọc Rồng Alone\n|6| Sử dụng xu Halloween để gắp kẹo\n Với mỗi lần gắp tương ứng với x1 xu Halloween\n"
                                        + "Máy gắp hỗ trợ gắp từ x1, x10, x100\n|7|Khi gắp thành công, vật phẩm sẽ được gửi tại rương đồ."
                                        + "\n|5|Happy Halloween ",
                                "Gắp Kẹo", "Xem Top", "Rương Đồ", "Đóng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.getIndexMenu() == 1234) {
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, 12345,
                                            "|5|Happy Halloween\n Gắp số lượng nhiều tỉ lệ ra giỏ kẹo đầy càng cao\n|3|Gắp x1 : Gắp Thủ công \n Gắp x10 : Gắp nhanh x10 Lần Gắp\nGắp x100 : Gắp nhanh x100 lần gắp\n"
                                                    + "|4|Lưu ý: đây là vật phẩm Ramdom không cố định vật phẩm\nNẾU MUỐN NGƯNG AUTO GẤP CHỈ CẦN THOÁT GAME VÀ VÀO LẠI!",
                                            "Gắp x1", "Gắp x10", "Gắp x100", "Rương Đồ");
                                    break;
                                case 1:
                                    Service.gI().showListTop(player, Manager.TopGapThu);
                                    break;
                                case 2:
                                    this.createOtherMenu(player, ConstNpc.RUONG_PHU,
                                            "|1|Tình yêu như một dây đàn\n"
                                                    + "Tình vừa được thì đàn đứt dây\n"
                                                    + "Đứt dây này anh thay dây khác\n"
                                                    + "Mất em rồi anh biết thay ai?",
                                            "Rương Phụ\n(" + (player.inventory.itemsBoxCrackBall.size()
                                                    - InventoryServiceNew.gI()
                                                            .getCountEmptyListItem(player.inventory.itemsBoxCrackBall))
                                                    + "/200)",
                                            "Xóa Hết\nRương Phụ", "Đóng");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 12345) {
                            switch (select) {
                                case 0:
                                    Item xuthuong = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1499);
                                    if (xuthuong == null) {
                                        this.createOtherMenu(player, 12345,
                                                "|2|HẾT TIỀN!\n|7|CẦN TỐI THIỂU 1 XU GẮP THÚ, HÃY QUAY LẠI SAU!",
                                                "Gắp X1", "Gắp X10", "Gắp X100", "Rương Đồ");
                                        break;
                                    }
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        Service.gI().sendThongBao(player, "Hết chỗ trống rồi");
                                        return;
                                    }
                                    InventoryServiceNew.gI().subQuantityItem(player.inventory.itemsBag, xuthuong, 1);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    short[] bkt = { 1438, 1442, 1443, 1467, 1494, 1533 };
                                    Item gapt = Util.petviprandom(bkt[Util.nextInt(bkt.length)]);
                                    if (Util.isTrue(10, 100)) {
                                        player.point_gapthu += 1;
                                        InventoryServiceNew.gI().addItemBag(player, gapt);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.createOtherMenu(player, 12345,
                                                "|2|Bạn vừa gắp được : " + gapt.template.name + "\nSố xu còn : "
                                                        + xuthuong.quantity + "\n|7|Chiến tiếp ngay!",
                                                "Gắp X1", "Gắp X10", "Gắp X100", "Rương Đồ");
                                    } else {
                                        this.createOtherMenu(player, 12345,
                                                "|6|Gắp hụt rồi, bạn bỏ cuộc sao?" + "\nSố xu còn : "
                                                        + xuthuong.quantity + "\n|7|Chiến tiếp ngay!",
                                                "Gắp X1", "Gắp X10", "Gắp X100", "Rương Đồ");
                                    }
                                    break;
                                case 1:
                                    if (InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1499) == null) {
                                        this.createOtherMenu(player, 12345,
                                                "|2|HẾT TIỀN!\n|7|CẦN TỐI THIỂU 1 XU GẮP THÚ, HÃY QUAY LẠI SAU!",
                                                "Gắp X1", "Gắp X10", "Gắp X100", "Rương Đồ");
                                        break;
                                    }
                                    try {
                                        Service.gI().sendThongBao(player, "Tiến hành auto gắp x10 lần");
                                        int timex10 = 10;
                                        int count = 0;
                                        while (timex10 > 0) {
                                            timex10--;
                                            count++;
                                            InventoryServiceNew.gI().subQuantityItemsBag(player,
                                                    InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1499),
                                                    1);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            if (InventoryServiceNew.gI().findItem(player.inventory.itemsBag,
                                                    1499) == null) {
                                                this.createOtherMenu(player, 12345,
                                                        "|7|HẾT XU!\nSỐ LƯỢT ĐÃ GẮP : " + count,
                                                        "Gắp X1", "Gắp X10", "Gắp X100", "Rương Đồ");
                                                break;
                                            }
                                            if (1 + player.inventory.itemsBoxCrackBall.size() > 200) {
                                                this.createOtherMenu(player, 12345,
                                                        "|7|DỪNG AUTO GẮP, RƯƠNG PHỤ ĐÃ ĐẦY!\n" + "|2|TỔNG LƯỢT GẮP : "
                                                                + count + " LƯỢT"
                                                                + "\n|7|VUI LÒNG LÀM TRỐNG RƯƠNG PHỤ!",
                                                        "Gắp X1", "Gắp X10", "Gắp X100", "Rương Đồ");
                                                break;
                                            }
                                            Thread.sleep(100);
                                            short[] bktt = { 1438, 1442, 1443, 1467, 1494, 1533 };
                                            Item gapx10 = Util.petviprandom(bktt[Util.nextInt(bktt.length)]);
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                                if (Util.isTrue(10, 100)) {
                                                    player.point_gapthu += 1;
                                                    InventoryServiceNew.gI().addItemBag(player, gapx10);
                                                    InventoryServiceNew.gI().sendItemBags(player);
                                                    this.createOtherMenu(player, 12345,
                                                            "|7|ĐANG TIẾN HÀNH GẮP AUTO X10\nSỐ LƯỢT CÒN : " + timex10
                                                                    + " LƯỢT\n" + "|2|Đã gắp được : "
                                                                    + gapx10.template.name + "\nSố xu còn : "
                                                                    + InventoryServiceNew.gI().findItem(
                                                                            player.inventory.itemsBag, 1499).quantity
                                                                    + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu
                                                                    + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                                            "Gắp X1", "Gắp X10", "Gắp X100", "Rương Đồ");
                                                } else {
                                                    this.createOtherMenu(player, 12345,
                                                            "|7|ĐANG TIẾN HÀNH GẮP AUTO X10\nSỐ LƯỢT CÒN : " + timex10
                                                                    + " LƯỢT\n" + "|2|Gắp hụt rồi!" + "\nSố xu còn : "
                                                                    + InventoryServiceNew.gI().findItem(
                                                                            player.inventory.itemsBag, 1499).quantity
                                                                    + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu
                                                                    + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                                            "Gắp X1", "Gắp X10", "Gắp X100", "Rương Đồ");
                                                }
                                            }
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                                if (Util.isTrue(2, 100)) {
                                                    player.inventory.itemsBoxCrackBall
                                                            .add(ItemService.gI().createNewItem((short) 1499));
                                                }
                                                if (Util.isTrue(10, 100)) {
                                                    player.point_gapthu += 1;
                                                    player.inventory.itemsBoxCrackBall.add(gapx10);
                                                    this.createOtherMenu(player, 12345,
                                                            "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : "
                                                                    + timex10 + " LƯỢT\n" + "|2|Đã gắp được : "
                                                                    + gapx10.template.name + "\nSố xu còn : "
                                                                    + InventoryServiceNew.gI().findItem(
                                                                            player.inventory.itemsBag, 1499).quantity
                                                                    + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu,
                                                            "Gắp X1", "Gắp X10", "Gắp X100", "Rương Đồ");
                                                } else {
                                                    this.createOtherMenu(player, 12345,
                                                            "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : "
                                                                    + timex10 + " LƯỢT\n" + "|2|Gắp hụt rồi!"
                                                                    + "\nSố xu còn : "
                                                                    + InventoryServiceNew.gI().findItem(
                                                                            player.inventory.itemsBag, 1499).quantity
                                                                    + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu,
                                                            "Gắp X1", "Gắp X10", "Gắp X100", "Rương Đồ");
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                    }
                                    break;
                                case 2:
                                    if (InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1499) == null) {
                                        this.createOtherMenu(player, 12345,
                                                "|2|HẾT TIỀN!\n|7|CẦN TỐI THIỂU 1 THỎI VÀNG GẮP THÚ, HÃY QUAY LẠI SAU!",
                                                "Gắp X1", "Gắp X10", "Gắp X100", "Rương Đồ");
                                        break;
                                    }
                                    try {
                                        Service.gI().sendThongBao(player, "Tiến hành auto gắp x10 lần");
                                        int timex100 = 100;
                                        int count = 0;
                                        while (timex100 > 0) {
                                            timex100--;
                                            count++;
                                            InventoryServiceNew.gI().subQuantityItemsBag(player,
                                                    InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1499),
                                                    1);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            if (InventoryServiceNew.gI().findItem(player.inventory.itemsBag,
                                                    1499) == null) {
                                                this.createOtherMenu(player, 12345,
                                                        "|7|HẾT THỎI VÀNG!\nSỐ LƯỢT ĐÃ GẮP : " + count,
                                                        "Gắp X1", "Gắp X10", "Gắp X100", "Rương Đồ");
                                                break;
                                            }
                                            if (1 + player.inventory.itemsBoxCrackBall.size() > 200) {
                                                this.createOtherMenu(player, 12345,
                                                        "|7|DỪNG AUTO GẮP, RƯƠNG PHỤ ĐÃ ĐẦY!\n" + "|2|TỔNG LƯỢT GẮP : "
                                                                + count + " LƯỢT"
                                                                + "\n|7|VUI LÒNG LÀM TRỐNG RƯƠNG PHỤ!",
                                                        "Gắp X1", "Gắp X10", "Gắp X100", "Rương Đồ");
                                                break;
                                            }
                                            Thread.sleep(100);
                                            short[] bkttt = { 1438, 1442, 1443, 1467, 1494, 1533 };
                                            Item gapx100 = Util.petviprandom(bkttt[Util.nextInt(bkttt.length)]);
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                                if (Util.isTrue(10, 100)) {
                                                    player.point_gapthu += 1;
                                                    InventoryServiceNew.gI().addItemBag(player, gapx100);
                                                    InventoryServiceNew.gI().sendItemBags(player);
                                                    this.createOtherMenu(player, 12345,
                                                            "|7|ĐANG TIẾN HÀNH GẮP AUTO X100\nSỐ LƯỢT CÒN : " + timex100
                                                                    + " LƯỢT\n" + "|2|Đã gắp được : "
                                                                    + gapx100.template.name + "\nSố xu còn : "
                                                                    + InventoryServiceNew.gI().findItem(
                                                                            player.inventory.itemsBag, 1499).quantity
                                                                    + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu
                                                                    + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                                            "Gắp X1", "Gắp X10", "Gắp X100", "Rương Đồ");
                                                } else {
                                                    this.createOtherMenu(player, 12345,
                                                            "|7|ĐANG TIẾN HÀNH GẮP AUTO X100\nSỐ LƯỢT CÒN : " + timex100
                                                                    + " LƯỢT\n" + "|2|Gắp hụt rồi!" + "\nSố xu còn : "
                                                                    + InventoryServiceNew.gI().findItem(
                                                                            player.inventory.itemsBag, 1499).quantity
                                                                    + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu
                                                                    + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                                            "Gắp X1", "Gắp X10", "Gắp X100", "Rương Đồ");
                                                }
                                            }
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                                if (Util.isTrue(2, 100)) {
                                                    player.inventory.itemsBoxCrackBall
                                                            .add(ItemService.gI().createNewItem((short) 1499));
                                                }
                                                if (Util.isTrue(10, 100)) {
                                                    player.point_gapthu += 1;
                                                    player.inventory.itemsBoxCrackBall.add(gapx100);
                                                    this.createOtherMenu(player, 12345,
                                                            "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO X100 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : "
                                                                    + timex100 + " LƯỢT\n" + "|2|Đã gắp được : "
                                                                    + gapx100.template.name + "\nSố xu còn : "
                                                                    + InventoryServiceNew.gI().findItem(
                                                                            player.inventory.itemsBag, 1499).quantity
                                                                    + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu,
                                                            "Gắp X1", "Gắp X10", "Gắp X100", "Rương Đồ");
                                                } else {
                                                    this.createOtherMenu(player, 12345,
                                                            "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO X100 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : "
                                                                    + timex100 + " LƯỢT\n" + "|2|Gắp hụt rồi!"
                                                                    + "\nSố xu còn : "
                                                                    + InventoryServiceNew.gI().findItem(
                                                                            player.inventory.itemsBag, 1499).quantity
                                                                    + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu,
                                                            "Gắp X1", "Gắp X10", "Gắp X100", "Rương Đồ");
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                    }
                                    break;
                                case 3:
                                    this.createOtherMenu(player, ConstNpc.RUONG_PHU,
                                            "|1|Tình yêu như một dây đàn\n"
                                                    + "Tình vừa được thì đàn đứt dây\n"
                                                    + "Đứt dây này anh thay dây khác\n"
                                                    + "Mất em rồi anh biết thay ai?",
                                            "Rương Phụ\n(" + (player.inventory.itemsBoxCrackBall.size()
                                                    - InventoryServiceNew.gI()
                                                            .getCountEmptyListItem(player.inventory.itemsBoxCrackBall))
                                                    + "/200)",
                                            "Xóa Hết\nRương Phụ", "Đóng");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.RUONG_PHU) {
                            switch (select) {
                                case 0:
                                    ShopServiceNew.gI().opendShop(player, "RUONG_PHU", true);
                                    break;
                                case 1:
                                    NpcService.gI().createMenuConMeo(player,
                                            ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND, this.avartar,
                                            "|3|Bạn chắc muốn xóa hết vật phẩm trong rương phụ?\n"
                                                    + "|7|Sau khi xóa sẽ không thể khôi phục!",
                                            "Đồng ý", "Hủy bỏ");
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc KAIDO(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "|7|Ngươi tìm ta có việc gì?\n"
                                        + "|7|Trạng thái VIP : "
                                        + (player.vip == 1 ? "VIP"
                                                : player.vip == 2 ? "VIP2"
                                                        : player.vip == 3 ? "VIP3"
                                                                : player.vip == 4 ? "SVIP" : "Chưa Kích Hoạt\n")
                                        + "◘Đối Với Cơ Chế Triệu Hồi Đệ Tử\n"
                                        + "♦Bạn Cần Kích Hoạt Vip Và Thẻ Triệu Hồi\n"
                                        + "♦Tùy Vào Vip Mà Bạn Có Thể Triệu Hồi Đệ Khác Nhau\n"
                                        + "♦Với Chỉ Số Khác Nhau\n"
                                        + "•Vip1 Cần Kích Vip 1 Và 1 Thẻ Triệu Hồi\n"
                                        + "•Vip 2 Cần Kích Vip 2 Và 2 Thẻ Triệu Hồi\n"
                                        + "•Vip 3 Cần Kích Vip 3 Và 3 Thẻ Triệu Hồi\n"
                                        + "•Vip 4 Cần Kích Vip 4 Và 4 Thẻ Triệu Hồi\n",
                                "Đến Địa Ngục", "Triệu Hồi Đệ Tử", "Đóng");
                    } else if (this.mapId == 190) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Quay Về", "Nâng Cải Trang Luffy", "Đóng");

                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 190, -1, 360);
                                    break;
                                case 1:
                                    Item thetrieuhoi = null;
                                    try {
                                        thetrieuhoi = InventoryServiceNew.gI().findItemBag(player, 1567);
                                    } catch (Exception e) {
                                        // throw new RuntimeException(e);
                                    }
                                    switch (player.vip) {
                                        case 1:
                                            if (thetrieuhoi != null && thetrieuhoi.quantity >= 1) {
                                                if (player.pet == null) {
                                                    Service.gI().sendThongBao(player, "Bạn Cần Có Đệ Thường");
                                                }
                                                PetService.gI().createAndroid21Vip(player, true, player.pet.gender);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, thetrieuhoi, 1);
                                                Service.gI().sendThongBao(player, "Triệu Hồi Đệ Thành Công");
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn Không Đủ Thẻ Triệu Hồi");
                                            }
                                            break;
                                        case 2:
                                            if (thetrieuhoi != null && thetrieuhoi.quantity >= 2) {
                                                if (player.pet == null) {
                                                    Service.gI().sendThongBao(player, "Bạn Cần Có Đệ Thường");
                                                }
                                                PetService.gI().createFuVip(player, true, player.pet.gender);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, thetrieuhoi, 2);
                                                Service.gI().sendThongBao(player, "Triệu Hồi Đệ Thành Công");
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn Không Đủ Thẻ Triệu Hồi");
                                            }
                                            break;
                                        case 3:
                                            if (thetrieuhoi != null && thetrieuhoi.quantity >= 3) {
                                                if (player.pet == null) {
                                                    Service.gI().sendThongBao(player, "Bạn Cần Có Đệ Thường");
                                                }
                                                PetService.gI().createKidbillVip(player, true, player.pet.gender);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, thetrieuhoi, 3);
                                                Service.gI().sendThongBao(player, "Triệu Hồi Đệ Thành Công");
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn Không Đủ Thẻ Triệu Hồi");
                                            }
                                            break;
                                        case 4:
                                            if (thetrieuhoi != null && thetrieuhoi.quantity >= 4) {
                                                if (player.pet == null) {
                                                    Service.gI().sendThongBao(player, "Bạn Cần Có Đệ Thường");
                                                }
                                                PetService.gI().createGokuSSJ4Vip(player, true, player.pet.gender);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, thetrieuhoi, 4);
                                                Service.gI().sendThongBao(player, "Triệu Hồi Đệ Thành Công");
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn Không Đủ Thẻ Triệu Hồi");
                                            }
                                            break;
                                    }
                                    break;
                            }
                        }
                    } else if (this.mapId == 190) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 360);
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_LUFFY);
                                    break;

                            }

                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_LUFFY) {
                            if (player.combineNew.typeCombine == CombineServiceNew.NANG_CAP_LUFFY && select == 0) {
                                CombineServiceNew.gI().startCombine(player, 2);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc mavuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 153) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Xin chào, tôi có thể giúp gì cho cậu?", "Đến\nTây thánh địa", "Từ chối");
                    } else if (this.mapId == 156) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Người muốn trở về?", "Quay về", "Từ chối");
                    } else if (this.mapId == 157) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi đủ sức đến Bắc thánh địa sao?", "Đến ngay", "Quay về\nTây thánh địa");
                    } else if (this.mapId == 158) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi đủ sức đến Nam thánh địa sao?", "Đến ngay", "Quay về\nĐông thánh địa");
                    } else if (this.mapId == 159) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Người muốn trở về?", "Quay về\nNam thánh địa");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 153) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                // đến tay thanh dia
                                if (player.nPoint.power < 80000000000L) {
                                    Service.gI().sendThongBaoOK(player, "Yêu Cầu 80 Tỷ Sức Mạnh");
                                    return;
                                }
                                ChangeMapService.gI().changeMapBySpaceShip(player, 156, -1, 360);
                            }
                        }
                    } else if (this.mapId == 156) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                // về đông karin
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 6, -1, 1011);
                                    break;
                            }
                        }
                    } else if (this.mapId == 157) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                //
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 158, -1, 303);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 156, -1, 821);
                                    break;
                            }
                        }
                    } else if (this.mapId == 158) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                //
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 159, -1, 206);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 157, -1, 210);
                                    break;
                            }
                        }
                    } else if (this.mapId == 159) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                //
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 158, -1, 303);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc gokuSSJ_2(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    try {
                        Item biKiep = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 590);
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Vào các khung giờ chẵn trong ngày\n"
                                + "Khi luyện tập với Mộc nhân với chế độ bật Cờ sẽ đánh rơi Bí kíp\n"
                                + "Hãy cố găng tập luyện thu thập 9999 bí kíp rồi quay lại gặp ta nhé", "Nhận\nthưởng",
                                "OK");

                    } catch (Exception ex) {
                        ex.printStackTrace();

                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    try {
                        Item biKiep = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 590);
                        if (select == 0) {
                            if (biKiep != null) {
                                if (biKiep.quantity >= 10000 && InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                    Item yardart = ItemService.gI().createNewItem((short) (player.gender + 592));
                                    yardart.itemOptions.add(new Item.ItemOption(47, 400));
                                    yardart.itemOptions.add(new Item.ItemOption(108, 10));
                                    InventoryServiceNew.gI().addItemBag(player, yardart);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, biKiep, 10000);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn vừa nhận được trang phục tộc Yardart");
                                } else if (biKiep.quantity < 10000) {
                                    Service.gI().sendThongBao(player, "Vui lòng sưu tầm đủ\n9999 bí kíp");
                                }
                            } else {
                                Service.gI().sendThongBao(player, "Vui lòng sưu tầm đủ\n9999 bí kíp");
                                return;
                            }
                        } else {
                            return;
                        }
                    } catch (Exception ex) {
                    }
                }
            }
        };
    }

    public static Npc khidaumoi(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 14) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Ta đang nắm giữ bí kíp giúp ngươi mạnh lên, Ngươi có muốn thử ?", "Nâng cấp\nVip", "Shop",
                            "Từ chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player) && this.mapId == 14) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                this.createOtherMenu(player, 1,
                                        "|7|Ngươi muốn thức tỉnh cải trang hay nâng cấp Lite Girl ?\b|2|Mỗi lần nâng cấp tiếp thì mỗi cấp cần thêm đá little Girl",
                                        "Nâng cấp lite girl",
                                        "Thức tỉnh Luffy",
                                        "Từ chối");
                                break;
                            case 1: // Shop
                                ShopServiceNew.gI().opendShop(player, "KHI", false);
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 1) {
                        switch (select) {
                            case 0:
                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_KHI);
                                break;
                            case 1:
                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_LUFFY);
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_KHI) {
                        if (player.combineNew.typeCombine == CombineServiceNew.NANG_CAP_KHI && select == 0) {
                            CombineServiceNew.gI().startCombine(player, 0);
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_LUFFY) {
                        if (player.combineNew.typeCombine == CombineServiceNew.NANG_CAP_LUFFY && select == 0) {
                            CombineServiceNew.gI().startCombine(player, 2);
                        }
                    }
                }
            }
        };
    }

    public static Npc chomeoan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            public void chatWithNpc(Player player) {
                String[] chat = {
                        "Ai đó hãy giúp tôi với ... Làm ơn huhu",
                        "Hãy giúp tôi tìm lại bé mèo huhu",
                        "Thí chủ, Xin dừng bước",
                        "Hãy giúp tôi tìm lại bé mèo ..."
                };
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    int index = 0;

                    @Override
                    public void run() {
                        npcChat(player, chat[index]);
                        index = (index + 1) % chat.length;
                    }
                }, 10000, 10000);
            }

            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 5) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Ta đang đánh mất một bé mèo đen đuôi vàng, hãy giúp ta tìm lại nó...", "Cho mèo ăn",
                            "Shop", "Không quan tâm");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, 1,
                                            "|7|Cho mèo ăn để mèo có chỉ số random từ 5 -> 30% Chỉ số \b|2| Mèo có chỉ số vĩnh viễn sẽ không thể cho ăn !",
                                            "Cho Mèo ăn",
                                            "Để mèo đói");
                                    break;
                                case 1: // shop
                                    ShopServiceNew.gI().opendShop(player, "MEOMEO", false);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 1) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_MEO);
                                    break;
                                case 1:
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_MEO) {
                            if (player.combineNew.typeCombine == CombineServiceNew.NANG_CAP_MEO && select == 0) {
                                CombineServiceNew.gI().startCombine(player, 0);
                                // }
                                // break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc GhiDanh(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            String[] menuselect = new String[] {};

            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (this.map.mapId == 52) {
                        if (DaiHoiManager.gI().openDHVT
                                && (System.currentTimeMillis() <= DaiHoiManager.gI().tOpenDHVT)) {
                            String nameDH = DaiHoiManager.gI().nameRoundDHVT();
                            this.createOtherMenu(pl, ConstNpc.MENU_DHVT,
                                    "Hiện đang có giải đấu " + nameDH
                                            + " bạn có muốn đăng ký không? \nSố người đã đăng ký :"
                                            + DaiHoiManager.gI().lstIDPlayers.size(),
                                    new String[] { "Giải\n" + nameDH + "\n(" + DaiHoiManager.gI().costRoundDHVT() + ")",
                                            "Từ chối", "Đại Hội\nVõ Thuật\nLần thứ\n23", "Giải siêu hạng" });
                        } else {
                            this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                    "Đã hết hạn đăng ký thi đấu, xin vui lòng chờ đến giải sau",
                                    new String[] { "Thông tin\bChi tiết", "OK", "Đại Hội\nVõ Thuật\nLần thứ\n23",
                                            "Giải siêu hạng" });
                        }
                    } else if (this.mapId == 129) {
                        int goldchallenge = pl.goldChallenge;
                        if (pl.levelWoodChest == 0) {
                            menuselect = new String[] { "Thi đấu\n" + Util.numberToMoney(goldchallenge) + " vàng",
                                    "Về\nĐại Hội\nVõ Thuật" };
                        } else {
                            menuselect = new String[] { "Thi đấu\n" + Util.numberToMoney(goldchallenge) + " vàng",
                                    "Nhận thưởng\nRương cấp\n" + pl.levelWoodChest, "Về\nĐại Hội\nVõ Thuật" };
                        }
                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                "Đại hội võ thuật lần thứ 23\nDiễn ra bất kể ngày đêm,ngày nghỉ ngày lễ\nPhần thưởng vô cùng quý giá\nNhanh chóng tham gia nào",
                                menuselect, "Từ chối");

                    } else {
                        super.openBaseMenu(pl);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.map.mapId == 52) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    Service.getInstance().sendThongBaoFromAdmin(player,
                                            "Lịch thi đấu trong ngày\bGiải Nhi đồng: 8,13,18h\bGiải Siêu cấp 1: 9,14,19h\bGiải Siêu cấp 2: 10,15,20h\bGiải Siêu cấp 3: 11,16,21h\bGiải Ngoại hạng: 12,17,22,23h\nGiải thưởng khi thắng mỗi vòng\bGiải Nhi đồng: 2 ngọc\bGiải Siêu cấp 1: 4 ngọc\bGiải Siêu cấp 2: 6 ngọc\bGiải Siêu cấp 3: 8 ngọc\bGiải Ngoại hạng: 10.000 vàng\bVô địch: 5 viên đá nâng cấp\nVui lòng đến đúng giờ để đăng ký thi đấu");
                                    break;
                                case 1:
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Nhớ Đến Đúng Giờ nhé");
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 129, player.location.x, 360);
                                    break;
                                case 3:
                                    // ChangeMapService.gI().changeMapNonSpaceship(player, 113, player.location.x,
                                    // 360);
                                    Service.getInstance().sendThongBaoFromAdmin(player, "|7|Đang Update!!");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DHVT) {
                            switch (select) {
                                case 0:
                                    if (DaiHoiManager.gI().lstIDPlayers.size() < 256) {
                                        if (DaiHoiManager.gI().typeDHVT == (byte) 5 && player.inventory.gold >= 10000) {
                                            if (DaiHoiManager.gI().isAssignDHVT(player.id)) {
                                                Service.getInstance().sendThongBao(player,
                                                        "Bạn đã đăng ký tham gia đại hội võ thuật rồi");
                                            } else {
                                                player.inventory.gold -= 10000;
                                                Service.getInstance().sendMoney(player);
                                                Service.getInstance().sendThongBao(player,
                                                        "Bạn đã đăng ký thành công, nhớ có mặt tại đây trước giờ thi đấu");
                                                DaiHoiManager.gI().lstIDPlayers.add(player.id);
                                            }
                                        } else if (DaiHoiManager.gI().typeDHVT > (byte) 0
                                                && DaiHoiManager.gI().typeDHVT < (byte) 5
                                                && player.inventory.gem >= (int) (2 * DaiHoiManager.gI().typeDHVT)) {
                                            if (DaiHoiManager.gI().isAssignDHVT(player.id)) {
                                                Service.getInstance().sendThongBao(player,
                                                        "Bạn đã đăng ký tham gia đại hội võ thuật rồi");
                                            } else {
                                                player.inventory.gem -= (int) (2 * DaiHoiManager.gI().typeDHVT);
                                                Service.getInstance().sendMoney(player);
                                                Service.getInstance().sendThongBao(player,
                                                        "Bạn đã đăng ký thành công, nhớ có mặt tại đây trước giờ thi đấu");
                                                DaiHoiManager.gI().lstIDPlayers.add(player.id);
                                            }
                                        } else {
                                            Service.getInstance().sendThongBao(player,
                                                    "Không đủ vàng ngọc để đăng ký thi đấu");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player,
                                                "Hiện tại đã đạt tới số lượng người đăng ký tối đa, xin hãy chờ đến giải sau");
                                    }
                                    break;
                                case 1:
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 129, player.location.x, 360);
                                    break;
                            }
                        }
                    } else if (this.mapId == 129) {
                        int goldchallenge = player.goldChallenge;
                        if (player.levelWoodChest == 0) {
                            switch (select) {
                                case 0:
                                    if (InventoryServiceNew.gI().finditemWoodChest(player)) {
                                        if (player.inventory.gold >= goldchallenge) {
                                            MartialCongressService.gI().startChallenge(player);
                                            player.inventory.gold -= (goldchallenge);
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            player.goldChallenge += 2000000;
                                        } else {
                                            Service.getInstance().sendThongBao(player,
                                                    "Không đủ vàng, còn thiếu "
                                                            + Util.numberToMoney(goldchallenge - player.inventory.gold)
                                                            + " vàng");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hãy mở rương báu vật trước");
                                    }
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 52, player.location.x, 336);
                                    break;
                            }
                        } else {
                            switch (select) {
                                case 0:
                                    if (InventoryServiceNew.gI().finditemWoodChest(player)) {
                                        if (player.inventory.gold >= goldchallenge) {
                                            MartialCongressService.gI().startChallenge(player);
                                            player.inventory.gold -= (goldchallenge);
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            player.goldChallenge += 2000000;
                                        } else {
                                            Service.getInstance().sendThongBao(player,
                                                    "Không đủ vàng, còn thiếu "
                                                            + Util.numberToMoney(goldchallenge - player.inventory.gold)
                                                            + " vàng");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hãy mở rương báu vật trước");
                                    }
                                    break;
                                case 1:
                                    if (!player.receivedWoodChest) {
                                        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                            Item it = ItemService.gI().createNewItem((short) 570);
                                            it.itemOptions.add(new Item.ItemOption(72, player.levelWoodChest));
                                            it.itemOptions.add(new Item.ItemOption(30, 0));
                                            it.createTime = System.currentTimeMillis();
                                            InventoryServiceNew.gI().addItemBag(player, it);
                                            InventoryServiceNew.gI().sendItemBags(player);

                                            player.receivedWoodChest = true;
                                            player.levelWoodChest = 0;
                                            Service.getInstance().sendThongBao(player, "Bạn nhận được rương gỗ");
                                        } else {
                                            this.npcChat(player, "Hành trang đã đầy");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player,
                                                "Mỗi ngày chỉ có thể nhận rương báu 1 lần");
                                    }
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 52, player.location.x, 336);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc unkonw(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, 0,
                                "Éc éc Bạn muốn gì ở tôi :3?", "Đến Võ đài Unknow");
                    }
                    if (this.mapId == 112) {
                        this.createOtherMenu(player, 0,
                                "Bạn đang còn : " + player.pointPvp + " điểm PvP Point", "Về đảo Kame",
                                "Đổi Cải trang sự kiên", "Top PVP");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.getIndexMenu() == 0) { //
                            switch (select) {
                                case 0:
                                    if (player.getSession().player.nPoint.power >= 10000000000L) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 112, -1, 495);
                                        Service.gI().changeFlag(player, Util.nextInt(8));
                                    } else {
                                        this.npcChat(player, "Bạn cần 10 tỷ sức mạnh mới có thể vào");
                                    }
                                    break; // qua vo dai
                            }
                        }
                    }

                    if (this.mapId == 112) {
                        if (player.iDMark.getIndexMenu() == 0) { //
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 319);
                                    break; // ve dao kame
                                case 1: //
                                    this.createOtherMenu(player, 1,
                                            "Bạn có muốn đổi 500 điểm PVP lấy \n|6|Cải trang Goku SSJ3\n với chỉ số random từ 20 > 30% \n ",
                                            "Ok", "Không");
                                    // bat menu doi item
                                    break;

                                case 2: //
                                    Service.gI().showListTop(player, Manager.topRUBY);
                                    // mo top pvp
                                    break;

                            }
                        }
                        if (player.iDMark.getIndexMenu() == 1) { // action doi item
                            switch (select) {
                                case 0: // trade
                                    if (player.pointPvp >= 500) {
                                        player.pointPvp -= 500;
                                        Item item = ItemService.gI().createNewItem((short) (1227)); // 49
                                        item.itemOptions.add(new Item.ItemOption(49, Util.nextInt(15, 20)));
                                        item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(15, 20)));
                                        item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(15, 20)));
                                        item.itemOptions.add(new Item.ItemOption(207, 0));
                                        item.itemOptions.add(new Item.ItemOption(33, 0));
                                        //
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                        Service.gI().sendThongBao(player, "Chúc Mừng Bạn Đổi Cải Trang Thành Công !");
                                    } else {
                                        Service.gI().sendThongBao(player,
                                                "Không đủ điểm bạn còn " + (500 - player.pointPvp) + " Điểm nữa");
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc monaito(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Sắp trung thu rồi .. ngươi mang cho ta item bất kì up tại ngũ hành sơn ,\nta sẽ cho một vật phẩm cực vip.\n Nếu tâm trạng ta vui ngươi có thể mua vật phẩm\nfree!",
                            "OK", "Đóng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (this.mapId) {
                        case 0:
                            switch (player.iDMark.getIndexMenu()) {
                                case ConstNpc.BASE_MENU:
                                    if (select == 0) {
                                        if (!player.getSession().actived) {
                                            if (player.getSession().player.nPoint.power >= 20000000000L) {
                                                ShopServiceNew.gI().opendShop(player, "TAYDUKI", true);
                                                break;
                                            } else {
                                                this.npcChat(player, "Ngươi Chưa Đủ 20 tỉ sm !!");

                                            }
                                            break;
                                        }
                                    }
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc granala(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Ngươi Đang Đua Top Sao?, Ta Có Vài Món Quà Cho Ngươi Này",
                            "Xem Ngay", "Từ Chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 0 || this.mapId == 7 || this.mapId == 14) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ShopServiceNew.gI().opendShop(player, "BUA_1H", false);
                                    break;

                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc createNPC(int mapId, int status, int cx, int cy, int tempId) {
        int avatar = Manager.NPC_TEMPLATES.get(tempId).avatar;
        try {
            switch (tempId) {
                case ConstNpc.UNKOWN:
                    return unkonw(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THOREN:
                    return thoren(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GHI_DANH:
                    return GhiDanh(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TRUNG_LINH_THU:
                    return trungLinhThu(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.POTAGE:
                    return poTaGe(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.QUY_LAO_KAME:
                    return quyLaoKame(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.MR_POPO:
                    return popo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THO_DAI_CA:
                    return thodaika(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TRUONG_LAO_GURU:
                    return truongLaoGuru(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.VUA_VEGETA:
                    return vuaVegeta(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.ONG_GOHAN:
                case ConstNpc.ONG_MOORI:
                case ConstNpc.ONG_PARAGUS:
                    return ongGohan_ongMoori_ongParagus(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BUNMA:
                    return bulmaQK(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.chichi:
                    return chichi(mapId, status, cx, cy, tempId, avatar);

                case ConstNpc.CHO_MEO_AN:
                    return chomeoan(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DENDE:
                    return dende(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.NGUDAN:
                    return ngudan(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.APPULE:
                    return appule(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DR_DRIEF:
                    return drDrief(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CARGO:
                    return cargo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CUI:
                    return cui(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.SANTA:
                    return santa(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.NGHIADIA:
                    return nghiadia(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TUTIEN:
                    return Tutien(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.OBITO:
                    return obito(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.ITACHI:
                    return itachi(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BAIBIEN:
                    return baibien(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.XEMIA:
                    return xemia(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.URON:
                    return uron(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BA_HAT_MIT:
                    return baHatMit(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.HUNG_VUONG:
                    return hungvuong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DUAHAU:
                    return duahau(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BKT:
                    return bkt(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RUONG_DO:
                    return ruongDo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DAU_THAN:
                    return dauThan(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CALICK:
                    return calick(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.JACO:
                    return jaco(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TRONG_TAI:
                    // return trongtai(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THUONG_DE:
                    return thuongDe(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.Granola:
                    return granala(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GIUMA_DAU_BO:
                    return mavuong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TAPION:
                    return Tapion(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CUA_HANG_KY_GUI:
                    return kyGui(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.Monaito:
                    return monaito(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.VADOS:
                    return vados(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.KHI_DAU_MOI:
                    return khidaumoi(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THAN_VU_TRU:
                    return thanVuTru(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THAN_GOD:
                    return thangod(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.KIBIT:
                    return kibit(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.OSIN:
                    return osin(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.LY_TIEU_NUONG:
                    return npclytieunuong54(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.LINH_CANH:
                    return linhCanh(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DOC_NHAN:
                    return docNhan(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.QUA_TRUNG:
                    return quaTrung(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.QUOC_VUONG:
                    return quocVuong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BUNMA_TL:
                    return bulmaTL(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RONG_OMEGA:
                    return rongOmega(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RONG_1S:
                case ConstNpc.RONG_2S:
                case ConstNpc.RONG_3S:
                case ConstNpc.RONG_4S:
                case ConstNpc.RONG_5S:
                case ConstNpc.RONG_6S:
                case ConstNpc.RONG_7S:
                    return rong1_to_7s(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.NPC_64:
                    return npcThienSu64(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BILL:
                    return bill(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.WHIS:
                    return whis(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BO_MONG:
                    return boMong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THAN_MEO_KARIN:
                    return karin(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GOKU_SSJ:
                    return gokuSSJ_1(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.MEO_THAN_TAI:
                    return meothantai(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.KAIDO:
                    return KAIDO(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GAPTHU:
                    return gapthu(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GOKU_SSJ_:
                    return gokuSSJ_2(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DUONG_TANG:
                    return duongtank(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.NGO_KHONG:
                    return ngokhong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.HOANGUC:
                    return hoanguc(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CHU_VIT:
                    return chuvit(mapId, status, cx, cy, tempId, avatar);
                // case ConstNpc.GOHAN_NHAT_NGUYET:
                // return gohannn(mapId, status, cx, cy, tempId, avatar);
                default:
                    return new Npc(mapId, status, cx, cy, tempId, avatar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                super.openBaseMenu(player);
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                // ShopService.gI().openShopNormal(player, this, ConstNpc.SHOP_BUNMA_TL_0, 0,
                                // player.gender);
                            }
                        }
                    };

            }
        } catch (Exception e) {
            System.err.print("\nError at 216\n");
            e.printStackTrace();
            return null;
        }
    }

    // girlbeo-mark
    public static void createNpcRongThieng() {
        Npc npc = new Npc(-1, -1, -1, -1, ConstNpc.RONG_THIENG, -1) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.IGNORE_MENU:

                        break;
                    case ConstNpc.SHENRON_CONFIRM:
                        if (select == 0) {
                            SummonDragon.gI().confirmWish();
                        } else if (select == 1) {
                            SummonDragon.gI().reOpenShenronWishes(player);
                        }
                        break;
                    case ConstNpc.SHENRON_1_1:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_1
                                && select == SHENRON_1_STAR_WISHES_1.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_2, SHENRON_SAY,
                                    SHENRON_1_STAR_WISHES_2);
                            break;
                        }
                    case ConstNpc.SHENRON_1_2:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_2
                                && select == SHENRON_1_STAR_WISHES_2.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_1, SHENRON_SAY,
                                    SHENRON_1_STAR_WISHES_1);
                            break;
                        }
                    default:
                        SummonDragon.gI().showConfirmShenron(player, player.iDMark.getIndexMenu(), (byte) select);
                        break;
                }
            }
        };
    }

    public static void createNpcConMeo() {
        Npc npc = new Npc(-1, -1, -1, -1, ConstNpc.CON_MEO, 351) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.IGNORE_MENU:

                        break;
                    case ConstNpc.MAKE_MATCH_PVP:
                        if (player.getSession().actived) {
                            if (Maintenance.isRuning) {
                                break;
                            }
                            PVPService.gI().sendInvitePVP(player, (byte) select);
                            break;
                        }
                    case ConstNpc.MAKE_FRIEND:
                        if (select == 0) {
                            Object playerId = PLAYERID_OBJECT.get(player.id);
                            if (playerId != null) {
                                FriendAndEnemyService.gI().acceptMakeFriend(player,
                                        Integer.parseInt(String.valueOf(playerId)));
                            }
                        }
                        break;
                    case ConstNpc.REVENGE:
                        if (select == 0) {
                            PVPService.gI().acceptRevenge(player);
                        }
                        break;
                    case ConstNpc.TUTORIAL_SUMMON_DRAGON:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        }
                        break;
                    case ConstNpc.SUMMON_SHENRON:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        } else if (select == 1) {
                            SummonDragon.gI().summonShenron(player);
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM1105:
                        if (select == 0) {
                            IntrinsicService.gI().sattd(player, 1105);
                        } else if (select == 1) {
                            IntrinsicService.gI().satnm(player, 1105);
                        } else if (select == 2) {
                            IntrinsicService.gI().setxd(player, 1105);
                        }
                        break;
                    case 1980:
                        if (select == 0) {
                            IntrinsicService.gI().sattd(player, 19800);
                        } else if (select == 1) {
                            IntrinsicService.gI().sattd(player, 19801);
                        } else if (select == 2) {
                            IntrinsicService.gI().sattd(player, 19802);
                        }
                        break;

                    case 1985:
                    case 1986:
                    case 1987:
                        if (select == 0) {
                            IntrinsicService.gI().sattd(player, player.iDMark.getIndexMenu());
                        } else if (select == 1) {
                            IntrinsicService.gI().satnm(player, player.iDMark.getIndexMenu());
                        } else if (select == 2) {
                            IntrinsicService.gI().setxd(player, player.iDMark.getIndexMenu());
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM2000:
                    case ConstNpc.MENU_OPTION_USE_ITEM2001:
                    case ConstNpc.MENU_OPTION_USE_ITEM2002:
                        try {
                            ItemService.gI().OpenSKH(player, player.iDMark.getIndexMenu(), select);
                        } catch (Exception e) {
                            System.err.print("\nError at 216\n");
                            e.printStackTrace();
                            Logger.error("Lỗi mở hộp quà");
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM2003:
                    case ConstNpc.MENU_OPTION_USE_ITEM2004:
                    case ConstNpc.MENU_OPTION_USE_ITEM2005:
                        try {
                            ItemService.gI().OpenDHD(player, player.iDMark.getIndexMenu(), select);
                        } catch (Exception e) {
                            System.err.print("\nError at 217\n");
                            e.printStackTrace();
                            Logger.error("Lỗi mở hộp quà");
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM736:
                        try {
                            ItemService.gI().OpenDHD(player, player.iDMark.getIndexMenu(), select);
                        } catch (Exception e) {
                            System.err.print("\nError at 218\n");
                            e.printStackTrace();
                            Logger.error("Lỗi mở hộp quà");
                        }
                        break;
                    case ConstNpc.INTRINSIC:
                        if (select == 0) {
                            IntrinsicService.gI().showAllIntrinsic(player);
                        } else if (select == 1) {
                            IntrinsicService.gI().showConfirmOpen(player);
                        } else if (select == 2) {
                            IntrinsicService.gI().showConfirmOpenVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_OPEN_INTRINSIC:
                        if (select == 0) {
                            IntrinsicService.gI().open(player);
                        }
                        break;
                    case 1278:
                        Input.gI().createFormItemC2(player, select);
                        break;
                    case ConstNpc.CONFIRM_OPEN_INTRINSIC_VIP:
                        if (select == 0) {
                            IntrinsicService.gI().openVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_LEAVE_CLAN:
                        if (select == 0) {
                            ClanService.gI().leaveClan(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_NHUONG_PC:
                        if (select == 0) {
                            ClanService.gI().phongPc(player, (int) PLAYERID_OBJECT.get(player.id));
                        }
                        break;
                    case ConstNpc.BAN_PLAYER:
                        if (select == 0) {
                            PlayerService.gI().banPlayer((Player) PLAYERID_OBJECT.get(player.id));
                            Service.gI().sendThongBao(player,
                                    "Ban người chơi " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                        }
                        break;

                    case ConstNpc.BUFF_PET:
                        if (select == 0) {
                            Player pl = (Player) PLAYERID_OBJECT.get(player.id);
                            if (pl.pet == null) {
                                PetService.gI().createNormalPet(pl);
                                Service.gI().sendThongBao(player, "Phát đệ tử cho "
                                        + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                            }
                        }
                        break;
                    case ConstNpc.UP_TOP_ITEM:
                        ShopKyGuiService.gI().StartupItemToTop(player);
                        break;
                    case ConstNpc.TVMAX:
                        Item item = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 457);
                        switch (select) {
                            case 0:
                                if (item.quantity < 1) {
                                    Service.gI().sendThongBao(player,
                                            "Bạn không đủ 1 thỏi vàng");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                    player.inventory.gold += 500000000;
                                    Service.gI().sendThongBao(player, "Bạn vừa dùng thỏi vàng và nhận được 500tr vàng");
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                } else {
                                    Service.gI().sendThongBao(player, "Hàng trang đã đầy");
                                }
                                break;
                            case 1:
                                if (item.quantity < 5) {
                                    Service.gI().sendThongBao(player,
                                            "Bạn không đủ 5 thỏi vàng");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                    player.inventory.gold += 2500000000L;
                                    Service.gI().sendThongBao(player, "Bạn vừa dùng thỏi vàng và nhận được 2 tỷ vàng");
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, item, 5);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                } else {
                                    Service.gI().sendThongBao(player, "Hàng trang đã đầy");
                                }
                                break;
                            case 2:
                                if (item.quantity < 10) {
                                    Service.gI().sendThongBao(player,
                                            "Bạn không đủ 10 thỏi vàng");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                    player.inventory.gold += 5000000000L;
                                    Service.gI().sendThongBao(player, "Bạn vừa dùng thỏi vàng và nhận được 5 tỷ vàng");
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, item, 10);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                } else {
                                    Service.gI().sendThongBao(player, "Hàng trang đã đầy");
                                }
                                break;
                            case 3:
                                if (item.quantity < 25) {
                                    Service.gI().sendThongBao(player,
                                            "Bạn không đủ 25 thỏi vàng");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                    player.inventory.gold += 12500000000L;
                                    Service.gI().sendThongBao(player,
                                            "Bạn vừa dùng thỏi vàng và nhận được 12 tỷ 5 vàng");
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, item, 25);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                } else {
                                    Service.gI().sendThongBao(player, "Hàng trang đã đầy");
                                }
                                break;
                            case 4:
                                if (item.quantity < 50) {
                                    Service.gI().sendThongBao(player,
                                            "Bạn không đủ 50 thỏi vàng");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                    player.inventory.gold += 25000000000L;
                                    Service.gI().sendThongBao(player, "Bạn vừa dùng thỏi vàng và nhận được 25 tỷ vàng");
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, item, 50);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                } else {
                                    Service.gI().sendThongBao(player, "Hàng trang đã đầy");
                                }
                                break;
                            case 5:
                                if (item.quantity < 100) {
                                    Service.gI().sendThongBao(player,
                                            "Bạn không đủ 100 thỏi vàng");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                    player.inventory.gold += 50000000000L;
                                    Service.gI().sendThongBao(player,
                                            "Bạn vừa dùng thỏi vàng và nhận được 50 tỷ vàng");
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, item, 100);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                } else {
                                    Service.gI().sendThongBao(player, "Hàng trang đã đầy");
                                }
                                break;

                        }
                        break;
                    case 20102002:
                        switch (select) {
                            case 0:
                                Item nro1 = ItemService.gI().createNewItem((short) 14);
                                Item nro2 = ItemService.gI().createNewItem((short) 15);
                                Item nro3 = ItemService.gI().createNewItem((short) 16);
                                Item nro4 = ItemService.gI().createNewItem((short) 17);
                                Item nro5 = ItemService.gI().createNewItem((short) 18);
                                Item nro6 = ItemService.gI().createNewItem((short) 19);
                                Item nro7 = ItemService.gI().createNewItem((short) 20);
                                InventoryServiceNew.gI().addItemBag(player, nro1);
                                InventoryServiceNew.gI().addItemBag(player, nro2);
                                InventoryServiceNew.gI().addItemBag(player, nro3);
                                InventoryServiceNew.gI().addItemBag(player, nro4);
                                InventoryServiceNew.gI().addItemBag(player, nro5);
                                InventoryServiceNew.gI().addItemBag(player, nro6);
                                InventoryServiceNew.gI().addItemBag(player, nro7);
                                InventoryServiceNew.gI().sendItemBags(player);
                                Service.gI().sendThongBao(player, "Vừa Nhận Được Bộ Ngọc Rồng");
                                break;
                            case 1:
                                if (player.pet == null) {
                                    PetService.gI().createNormalPet(player);
                                } else {
                                    if (player.pet.typePet == 1) {
                                        PetService.gI().changePicPet(player);
                                    } else if (player.pet.typePet == 2) {
                                        PetService.gI().changeMabuPet(player);
                                    }
                                    PetService.gI().changeBerusPet(player);
                                }
                                break;
                            case 2:
                                if (player.isAdmin()) {
                                    System.out.println(player.name);
                                    Maintenance.gI().start(15);
                                    System.out.println(player.name);
                                }
                                break;
                            case 3:
                                Input.gI().createFormFindPlayer(player);
                                break;
                            case 4:
                                Input.gI().ChatAll(player);
                                break;
                            case 5:
                                Input.gI().QuanLyTK(player);
                                break;

                        }
                        break;
                    case 2010200322:
                        switch (select) {
                            case 0:
                                Input.gI().createFormSenditem(player);
                                break;
                            case 1:
                                Input.gI().createFormSenditem1(player);
                                break;
                            case 2:
                                Input.gI().createFormSenditemskh(player);
                                break;
                            case 3:
                                Input.gI().createFormSenditem2(player);
                                break;
                        }
                        break;
                    case ConstNpc.BktTruytim:
                        if (player.BktDauLaDaiLuc[7] == 0) {
                            switch (select) {
                                case 0:
                                    if (player.Exptutien < 1000000) {
                                        Service.gI().sendThongBaoOK(player, "Cần 1tr Exp Diệt Thần");
                                        return;
                                    }
                                    player.Exptutien -= 1000000;
                                    if (Util.isTrue(0.2f, 100)) {
                                        player.BktDauLaDaiLuc[7] += Util.nextInt(1, 6);
                                        player.BktDauLaDaiLuc[8] += Util.nextInt(5, 20);
                                        String hcnhan = player
                                                .BktNameHoncot(Util.BKT(player.BktDauLaDaiLuc[7]))
                                                + "\n";
                                        if (player.BktDauLaDaiLuc[7] == 1) {
                                            hcnhan += "Tăng: " + player.BktDauLaDaiLuc[8] + " % chỉ số\n";
                                            hcnhan += "giảm: " + player.BktDauLaDaiLuc[8] / 3
                                                    + " % th\u1EDDi gian Skill đấm, max 20%.\n";
                                        }
                                        if (player.BktDauLaDaiLuc[7] == 2) {
                                            hcnhan += "Tăng: " + player.BktDauLaDaiLuc[8] / 5
                                                    + "% Khả năng up các loại exp cao cấp của thế giới này.\n";
                                        }
                                        if (player.BktDauLaDaiLuc[7] == 3) {
                                            hcnhan += "Giảm: " + player.BktDauLaDaiLuc[8] / 3
                                                    + "% sát thương nhận.\n";
                                            hcnhan += "Có tỉ lệ x2 dame.\n";
                                        }
                                        if (player.BktDauLaDaiLuc[7] == 4) {
                                            hcnhan += "Tăng: " + player.BktDauLaDaiLuc[8] * 250000000L
                                                    + "dame.\n";
                                            hcnhan += "Giảm: " + player.BktDauLaDaiLuc[8] / 2
                                                    + "% dame người ở gần.\n";
                                        }
                                        if (player.BktDauLaDaiLuc[7] == 5) {
                                            hcnhan += "Tăng: " + player.BktDauLaDaiLuc[8] * 1000000000L
                                                    + "Sinh lực.\n";
                                            hcnhan += "hồi phục: " + player.BktDauLaDaiLuc[8] / 3
                                                    + "% Sinh lực sau 3s.\n";
                                        }
                                        if (player.BktDauLaDaiLuc[7] == 6) {
                                            hcnhan += "Đánh Sát thương chuẩn: "
                                                    + player.BktDauLaDaiLuc[8] * 100000000L
                                                    + "dame.\n";
                                        }
                                        NpcService.gI().createMenuConMeo(player, ConstNpc.BktTruytim, -1,
                                                "Nro KhanhDTK\n"
                                                        + "Thông tin hồn cốt\n"
                                                        + hcnhan
                                                        + "\nHãy chọn theo lí trí của mình.",
                                                "đóng", "Hủy hồn cốt", "Hấp thụ hồn cốt");
                                    } else {
                                        NpcService.gI().createMenuConMeo(player, ConstNpc.BktTruytim, -1,
                                                "Nro KhanhDTK\n"
                                                        + "Truy tìm thất bại.",
                                                "Truy tìm");
                                    }
                                    break;
                                case 1:
                                    if (player.Exptutien < 500000000) {
                                        Service.gI().sendThongBaoOK(player, "Cần 500tr Exp Diệt Thần");
                                        return;
                                    }
                                    player.Exptutien -= 500000000;
                                    player.BktDauLaDaiLuc[7] += Util.nextInt(1, 6);
                                    player.BktDauLaDaiLuc[8] += Util.nextInt(5, 20);
                                    String hcnhan = player
                                            .BktNameHoncot(Util.BKT(player.BktDauLaDaiLuc[7]))
                                            + "\n";
                                    if (player.BktDauLaDaiLuc[7] == 1) {
                                        hcnhan += "Tăng: " + player.BktDauLaDaiLuc[8] + " % chỉ số\n";
                                        hcnhan += "giảm: " + player.BktDauLaDaiLuc[8] / 3
                                                + " % th\u1EDDi gian Skill đấm, max 20%.\n";
                                    }
                                    if (player.BktDauLaDaiLuc[7] == 2) {
                                        hcnhan += "Tăng: " + player.BktDauLaDaiLuc[8] / 5
                                                + "% Khả năng up các loại exp cao cấp của thế giới này.\n";
                                    }
                                    if (player.BktDauLaDaiLuc[7] == 3) {
                                        hcnhan += "Giảm: " + player.BktDauLaDaiLuc[8] / 3
                                                + "% sát thương nhận.\n";
                                        hcnhan += "Có tỉ lệ x2 dame.\n";
                                    }
                                    if (player.BktDauLaDaiLuc[7] == 4) {
                                        hcnhan += "Tăng: " + player.BktDauLaDaiLuc[8] * 250000000L
                                                + "dame.\n";
                                        hcnhan += "Giảm: " + player.BktDauLaDaiLuc[8] / 2
                                                + "% dame người ở gần.\n";
                                    }
                                    if (player.BktDauLaDaiLuc[7] == 5) {
                                        hcnhan += "Tăng: " + player.BktDauLaDaiLuc[8] * 1000000000L
                                                + "Sinh lực.\n";
                                        hcnhan += "hồi phục: " + player.BktDauLaDaiLuc[8] / 3
                                                + "% Sinh lực sau 3s.\n";
                                    }
                                    if (player.BktDauLaDaiLuc[7] == 6) {
                                        hcnhan += "Đánh Sát thương chuẩn: "
                                                + player.BktDauLaDaiLuc[8] * 100000000L
                                                + "dame.\n";
                                    }
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.BktTruytim, -1,
                                            "Nro KhanhDTK\n"
                                                    + "Thông tin hồn cốt\n"
                                                    + hcnhan
                                                    + "\nHãy chọn theo lí trí của mình.",
                                            "đóng", "Hủy hồn cốt", "Hấp thụ hồn cốt");
                                    break;
                            }
                        } else {
                            switch (select) {
                                case 0:
                                    break;
                                case 1:
                                    player.BktDauLaDaiLuc[7] = 0;
                                    player.BktDauLaDaiLuc[8] = 0;
                                    Service.gI().sendThongBaoOK(player, "Đã hủy hồn cốt");
                                    break;
                                case 2:
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.BktDYHapthu, -1,
                                            "Nro KhanhDTK\n"
                                                    + "Chọn phương pháp hấp thụ.",
                                            "Dựa vào bản thân\n(50% thành công)", "Nhờ trợ giúp\n(100% thành công)");
                                    break;
                            }
                        }
                        break;
                    case ConstNpc.BktDYHapthu:
                        switch (select) {
                            case 0:
                                if (player.BktDauLaDaiLuc[7] == 1 && player.BktDauLaDaiLuc[9] != 1) {
                                    if (Util.isTrue(30f, 100)) {
                                        Service.gI().sendThongBaoOK(player, "Hấp thụ thành công hồn cốt: "
                                                + player.BktNameHoncot(
                                                        Util.BKT(player.BktDauLaDaiLuc[7])));
                                        player.BktDauLaDaiLuc[9] = 1;
                                        player.BktDauLaDaiLuc[10] = player.BktDauLaDaiLuc[8];
                                    } else {
                                        Service.gI().sendThongBaoOK(player, "Hấp thụ thất bại hồn cốt đã tan biến.");
                                    }
                                    player.BktDauLaDaiLuc[7] = 0;
                                    player.BktDauLaDaiLuc[8] = 0;
                                } else if (player.BktDauLaDaiLuc[7] == 2 && player.BktDauLaDaiLuc[11] != 1) {
                                    if (Util.isTrue(30f, 100)) {
                                        Service.gI().sendThongBaoOK(player, "Hấp thụ thành công hồn cốt: "
                                                + player.BktNameHoncot(
                                                        Util.BKT(player.BktDauLaDaiLuc[7])));
                                        player.BktDauLaDaiLuc[11] = 1;
                                        player.BktDauLaDaiLuc[12] = player.BktDauLaDaiLuc[8];
                                    } else {
                                        Service.gI().sendThongBaoOK(player, "Hấp thụ thất bại hồn cốt đã tan biến.");
                                    }
                                    player.BktDauLaDaiLuc[7] = 0;
                                    player.BktDauLaDaiLuc[8] = 0;
                                } else if (player.BktDauLaDaiLuc[7] == 3 && player.BktDauLaDaiLuc[13] != 1) {
                                    if (Util.isTrue(30f, 100)) {
                                        Service.gI().sendThongBaoOK(player, "Hấp thụ thành công hồn cốt: "
                                                + player.BktNameHoncot(
                                                        Util.BKT(player.BktDauLaDaiLuc[7])));
                                        player.BktDauLaDaiLuc[13] = 1;
                                        player.BktDauLaDaiLuc[14] = player.BktDauLaDaiLuc[8];
                                    } else {
                                        Service.gI().sendThongBaoOK(player, "Hấp thụ thất bại hồn cốt đã tan biến.");
                                    }
                                    player.BktDauLaDaiLuc[7] = 0;
                                    player.BktDauLaDaiLuc[8] = 0;
                                } else if (player.BktDauLaDaiLuc[7] == 4 && player.BktDauLaDaiLuc[15] != 1) {
                                    if (Util.isTrue(30f, 100)) {
                                        Service.gI().sendThongBaoOK(player, "Hấp thụ thành công hồn cốt: "
                                                + player.BktNameHoncot(
                                                        Util.BKT(player.BktDauLaDaiLuc[7])));
                                        player.BktDauLaDaiLuc[15] = 1;
                                        player.BktDauLaDaiLuc[16] = player.BktDauLaDaiLuc[8];
                                    } else {
                                        Service.gI().sendThongBaoOK(player, "Hấp thụ thất bại hồn cốt đã tan biến.");
                                    }
                                    player.BktDauLaDaiLuc[7] = 0;
                                    player.BktDauLaDaiLuc[8] = 0;
                                } else if (player.BktDauLaDaiLuc[7] == 5 && player.BktDauLaDaiLuc[17] != 1) {
                                    if (Util.isTrue(30f, 100)) {
                                        Service.gI().sendThongBaoOK(player, "Hấp thụ thành công hồn cốt: "
                                                + player.BktNameHoncot(
                                                        Util.BKT(player.BktDauLaDaiLuc[7])));
                                        player.BktDauLaDaiLuc[17] = 1;
                                        player.BktDauLaDaiLuc[18] = player.BktDauLaDaiLuc[8];
                                    } else {
                                        Service.gI().sendThongBaoOK(player, "Hấp thụ thất bại hồn cốt đã tan biến.");
                                    }
                                    player.BktDauLaDaiLuc[7] = 0;
                                    player.BktDauLaDaiLuc[8] = 0;
                                } else if (player.BktDauLaDaiLuc[7] == 6 && player.BktDauLaDaiLuc[19] != 1) {
                                    if (Util.isTrue(30f, 100)) {
                                        Service.gI().sendThongBaoOK(player, "Hấp thụ thành công hồn cốt: "
                                                + player.BktNameHoncot(
                                                        Util.BKT(player.BktDauLaDaiLuc[7])));
                                        player.BktDauLaDaiLuc[19] = 1;
                                        player.BktDauLaDaiLuc[20] = player.BktDauLaDaiLuc[8];
                                    } else {
                                        Service.gI().sendThongBaoOK(player, "Hấp thụ thất bại hồn cốt đã tan biến.");
                                    }
                                    player.BktDauLaDaiLuc[7] = 0;
                                    player.BktDauLaDaiLuc[8] = 0;
                                } else {
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.BktTruytim, -1,
                                            "Nro KhanhDTK\n"
                                                    + "Ngươi đã sở hữu hồn cốt này rồi."
                                                    + "\nChỉ còn hủy hồn cốt hoặc để đó trưng.",
                                            "đóng", "Hủy hồn cốt", "Hấp thụ hồn cốt");
                                }
                                break;
                            case 1:
                                if (player.Exptutien < 150000000) {
                                    Service.gI().sendThongBaoOK(player, "Cần 150tr exp Diệt Thần.");
                                    return;
                                }
                                if (player.Captutien < 50) {
                                    Service.gI().sendThongBaoOK(player, "Cần 50 Cấp Diệt Thần.");
                                    return;
                                }
                                if (player.BktDauLaDaiLuc[7] == 1 && player.BktDauLaDaiLuc[9] != 1) {
                                    Service.gI().sendThongBaoOK(player, "Hấp thụ thành công hồn cốt: "
                                            + player.BktNameHoncot(Util.BKT(player.BktDauLaDaiLuc[7])));
                                    player.BktDauLaDaiLuc[9] = 1;
                                    player.BktDauLaDaiLuc[10] = player.BktDauLaDaiLuc[8];
                                    player.BktDauLaDaiLuc[7] = 0;
                                    player.BktDauLaDaiLuc[8] = 0;
                                    player.Exptutien -= 150000000;
                                } else if (player.BktDauLaDaiLuc[7] == 2 && player.BktDauLaDaiLuc[11] != 1) {
                                    Service.gI().sendThongBaoOK(player, "Hấp thụ thành công hồn cốt: "
                                            + player.BktNameHoncot(Util.BKT(player.BktDauLaDaiLuc[7])));
                                    player.BktDauLaDaiLuc[11] = 1;
                                    player.BktDauLaDaiLuc[12] = player.BktDauLaDaiLuc[8];
                                    player.BktDauLaDaiLuc[7] = 0;
                                    player.BktDauLaDaiLuc[8] = 0;
                                    player.Exptutien -= 150000000;
                                } else if (player.BktDauLaDaiLuc[7] == 3 && player.BktDauLaDaiLuc[13] != 1) {
                                    Service.gI().sendThongBaoOK(player, "Hấp thụ thành công hồn cốt: "
                                            + player.BktNameHoncot(Util.BKT(player.BktDauLaDaiLuc[7])));
                                    player.BktDauLaDaiLuc[13] = 1;
                                    player.BktDauLaDaiLuc[14] = player.BktDauLaDaiLuc[8];
                                    player.BktDauLaDaiLuc[7] = 0;
                                    player.BktDauLaDaiLuc[8] = 0;
                                    player.Exptutien -= 150000000;
                                } else if (player.BktDauLaDaiLuc[7] == 4 && player.BktDauLaDaiLuc[15] != 1) {
                                    Service.gI().sendThongBaoOK(player, "Hấp thụ thành công hồn cốt: "
                                            + player.BktNameHoncot(Util.BKT(player.BktDauLaDaiLuc[7])));
                                    player.BktDauLaDaiLuc[15] = 1;
                                    player.BktDauLaDaiLuc[16] = player.BktDauLaDaiLuc[8];
                                    player.BktDauLaDaiLuc[7] = 0;
                                    player.BktDauLaDaiLuc[8] = 0;
                                    player.Exptutien -= 150000000;
                                } else if (player.BktDauLaDaiLuc[7] == 5 && player.BktDauLaDaiLuc[17] != 1) {
                                    Service.gI().sendThongBaoOK(player, "Hấp thụ thành công hồn cốt: "
                                            + player.BktNameHoncot(Util.BKT(player.BktDauLaDaiLuc[7])));
                                    player.BktDauLaDaiLuc[17] = 1;
                                    player.BktDauLaDaiLuc[18] = player.BktDauLaDaiLuc[8];
                                    player.BktDauLaDaiLuc[7] = 0;
                                    player.BktDauLaDaiLuc[8] = 0;
                                    player.Exptutien -= 150000000;
                                } else if (player.BktDauLaDaiLuc[7] == 6 && player.BktDauLaDaiLuc[19] != 1) {
                                    Service.gI().sendThongBaoOK(player, "Hấp thụ thành công hồn cốt: "
                                            + player.BktNameHoncot(Util.BKT(player.BktDauLaDaiLuc[7])));
                                    player.BktDauLaDaiLuc[19] = 1;
                                    player.BktDauLaDaiLuc[20] = player.BktDauLaDaiLuc[8];
                                    player.BktDauLaDaiLuc[7] = 0;
                                    player.BktDauLaDaiLuc[8] = 0;
                                    player.Exptutien -= 150000000;
                                } else {
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.BktTruytim, -1,
                                            "Nro KhanhDTK\n"
                                                    + "Ngươi đã sở hữu hồn cốt này rồi."
                                                    + "\nChỉ còn hủy hồn cốt hoặc để đó trưng.",
                                            "đóng", "Hủy hồn cốt", "Hấp thụ hồn cốt");
                                }
                                break;
                        }
                        break;
                    case ConstNpc.BktNCHC:
                        Item mhc = InventoryServiceNew.gI().findItem(player.inventory.itemsBag,
                                1493);
                        switch (select) {
                            case 0:
                                if (player.Captutien < 200) {
                                    Service.gI().sendThongBaoOK(player, "Cần ít nhất 200 cấp Diệt Thần");
                                    return;
                                }
                                if (player.Exptutien < 125000000) {
                                    Service.gI().sendThongBaoOK(player, "Cần ít nhất 125tr Exp Diệt Thần");
                                    return;
                                }
                                if (player.BktDauLaDaiLuc[9] != 1) {
                                    Service.gI().sendThongBaoOK(player, "Bạn không sỡ hữu hồn cồn này");
                                    return;
                                }
                                if (mhc != null && mhc.quantity > 12500) {
                                    player.BktDauLaDaiLuc[10] += Util.nextInt(5, 20);
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.BktNCHC, -1,
                                            "Nro KhanhDTK\n"
                                                    + "Bạn nâng cấp thành công hồn cốt:\n" + player.BktNameHoncot(1)
                                                    + "\nChỉ Số sau khi nâng cấp :\n"
                                                    + "Tăng: " + player.BktDauLaDaiLuc[10] + " % chỉ số\n"
                                                    + "giảm: "
                                                    + (player.BktDauLaDaiLuc[10] / 3 >= 20 ? 20
                                                            : player.BktDauLaDaiLuc[10] / 3)
                                                    + "% thời gian Skill đấm",
                                            player.BktNameHoncot(1), player.BktNameHoncot(2),
                                            player.BktNameHoncot(3),
                                            player.BktNameHoncot(4), player.BktNameHoncot(5),
                                            player.BktNameHoncot(6));
                                    player.Exptutien -= 125000000;
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, mhc, 12500);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                } else {
                                    Service.gI().sendThongBaoOK(player, "Cần 12,5k mảnh hồn cốt");
                                }
                                break;
                            case 1:
                                if (player.Captutien < 200) {
                                    Service.gI().sendThongBaoOK(player, "Cần ít nhất 200 cấp Diệt Thần");
                                    return;
                                }
                                if (player.Exptutien < 130000000) {
                                    Service.gI().sendThongBaoOK(player, "Cần ít nhất 130tr Exp Diệt Thần");
                                    return;
                                }
                                if (player.BktDauLaDaiLuc[11] != 1) {
                                    Service.gI().sendThongBaoOK(player, "Bạn không sỡ hữu hồn cồn này");
                                    return;
                                }
                                if (player.BktDauLaDaiLuc[12] / 5 >= 20) {
                                    Service.gI().sendThongBaoOK(player, "Đã tối đa không thể tăng thêm nữa");
                                    return;
                                }
                                if (mhc != null && mhc.quantity > 13000) {
                                    player.BktDauLaDaiLuc[12] += Util.nextInt(5, 20);
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.BktNCHC, -1,
                                            "Nro KhanhDTK\n"
                                                    + "Bạn nâng cấp thành công hồn cốt:\n" + player.BktNameHoncot(2)
                                                    + "\nChỉ Số sau khi nâng cấp :\n"
                                                    + "Tăng: "
                                                    + (player.BktDauLaDaiLuc[12] / 5 >= 20 ? 20
                                                            : player.BktDauLaDaiLuc[12] / 5)
                                                    + "% Khả năng up các loại exp cao cấp của thế giới này.",
                                            player.BktNameHoncot(1), player.BktNameHoncot(2),
                                            player.BktNameHoncot(3),
                                            player.BktNameHoncot(4), player.BktNameHoncot(5),
                                            player.BktNameHoncot(6));
                                    player.Exptutien -= 130000000;
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, mhc, 13000);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                } else {
                                    Service.gI().sendThongBaoOK(player, "Cần 13k mảnh hồn cốt");
                                }
                                break;
                            case 2:
                                if (player.Captutien < 200) {
                                    Service.gI().sendThongBaoOK(player, "Cần ít nhất 200 cấp Diệt Thần");
                                    return;
                                }
                                if (player.Exptutien < 110000000) {
                                    Service.gI().sendThongBaoOK(player, "Cần ít nhất 110tr Exp Diệt Thần");
                                    return;
                                }
                                if (player.BktDauLaDaiLuc[13] != 1) {
                                    Service.gI().sendThongBaoOK(player, "Bạn không sỡ hữu hồn cồn này");
                                    return;
                                }
                                if (player.BktDauLaDaiLuc[14] / 3 >= 80) {
                                    Service.gI().sendThongBaoOK(player, "Đã tối đa không thể tăng thêm nữa");
                                    return;
                                }
                                if (mhc != null && mhc.quantity > 11000) {
                                    player.BktDauLaDaiLuc[14] += Util.nextInt(5, 20);
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.BktNCHC, -1,
                                            "Nro KhanhDTK\n"
                                                    + "Bạn nâng cấp thành công hồn cốt:\n" + player.BktNameHoncot(3)
                                                    + "\nChỉ Số sau khi nâng cấp :\n"
                                                    + "Giảm: " + (player.BktDauLaDaiLuc[14] / 3 >= 80 ? 80
                                                            : player.BktDauLaDaiLuc[14] / 3)
                                                    + "% sát thương nhận.\n",
                                            player.BktNameHoncot(1), player.BktNameHoncot(2),
                                            player.BktNameHoncot(3),
                                            player.BktNameHoncot(4), player.BktNameHoncot(5),
                                            player.BktNameHoncot(6));
                                    player.Exptutien -= 110000000;
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, mhc, 11000);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                } else {
                                    Service.gI().sendThongBaoOK(player, "Cần 11k mảnh hồn cốt");
                                }
                                break;
                            case 3:
                                if (player.Captutien < 200) {
                                    Service.gI().sendThongBaoOK(player, "Cần ít nhất 200 cấp Diệt Thần");
                                    return;
                                }
                                if (player.Exptutien < 115000000) {
                                    Service.gI().sendThongBaoOK(player, "Cần ít nhất 115tr Exp Diệt Thần");
                                    return;
                                }
                                if (player.BktDauLaDaiLuc[15] != 1) {
                                    Service.gI().sendThongBaoOK(player, "Bạn không sỡ hữu hồn cồn này");
                                    return;
                                }
                                if (mhc != null && mhc.quantity > 11500) {
                                    player.BktDauLaDaiLuc[16] += Util.nextInt(5, 20);
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.BktNCHC, -1,
                                            "Nro KhanhDTK\n"
                                                    + "Bạn nâng cấp thành công hồn cốt:\n" + player.BktNameHoncot(4)
                                                    + "\nChỉ Số sau khi nâng cấp :\n"
                                                    + "Tăng: "
                                                    + Util.getFormatNumber(player.BktDauLaDaiLuc[16] * 250000000d)
                                                    + "dame.\n"
                                                    + "Giảm: " + (player.BktDauLaDaiLuc[16] / 2 >= 90 ? 90
                                                            : player.BktDauLaDaiLuc[16] / 2)
                                                    + "% dame người ở gần.",
                                            player.BktNameHoncot(1), player.BktNameHoncot(2),
                                            player.BktNameHoncot(3),
                                            player.BktNameHoncot(4), player.BktNameHoncot(5),
                                            player.BktNameHoncot(6));
                                    player.Exptutien -= 115000000;
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, mhc, 11500);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                } else {
                                    Service.gI().sendThongBaoOK(player, "Cần 11,5k mảnh hồn cốt");
                                }
                                break;
                            case 4:
                                if (player.Captutien < 200) {
                                    Service.gI().sendThongBaoOK(player, "Cần ít nhất 200 cấp Diệt Thần");
                                    return;
                                }
                                if (player.Exptutien < 100000000) {
                                    Service.gI().sendThongBaoOK(player, "Cần ít nhất 100tr Exp Diệt Thần");
                                    return;
                                }
                                if (player.BktDauLaDaiLuc[17] != 1) {
                                    Service.gI().sendThongBaoOK(player, "Bạn không sỡ hữu hồn cồn này");
                                    return;
                                }
                                if (mhc != null && mhc.quantity > 9000) {
                                    player.BktDauLaDaiLuc[18] += Util.nextInt(5, 20);
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.BktNCHC, -1,
                                            "Nro KhanhDTK\n"
                                                    + "Bạn nâng cấp thành công hồn cốt:\n" + player.BktNameHoncot(5)
                                                    + "\nChỉ Số sau khi nâng cấp :\n"
                                                    + "Tăng: "
                                                    + Util.getFormatNumber(player.BktDauLaDaiLuc[18] * 1000000000d)
                                                    + "Sinh lực.\n"
                                                    + "+hồi phục: " + (player.BktDauLaDaiLuc[18] / 3 >= 90 ? 90
                                                            : player.BktDauLaDaiLuc[18] / 3)
                                                    + "% Sinh lực sau 3s.",
                                            player.BktNameHoncot(1), player.BktNameHoncot(2),
                                            player.BktNameHoncot(3),
                                            player.BktNameHoncot(4), player.BktNameHoncot(5),
                                            player.BktNameHoncot(6));
                                    player.Exptutien -= 100000000;
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, mhc, 9000);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                } else {
                                    Service.gI().sendThongBaoOK(player, "Cần 9k mảnh hồn cốt");
                                }
                                break;
                            case 5:
                                if (player.Captutien < 200) {
                                    Service.gI().sendThongBaoOK(player, "Cần ít nhất 200 cấp Diệt Thần");
                                    return;
                                }
                                if (player.Exptutien < 100000000) {
                                    Service.gI().sendThongBaoOK(player, "Cần ít nhất 100tr Exp Diệt Thần");
                                    return;
                                }
                                if (player.BktDauLaDaiLuc[19] != 1) {
                                    Service.gI().sendThongBaoOK(player, "Bạn không sỡ hữu hồn cồn này");
                                    return;
                                }
                                if (mhc != null && mhc.quantity > 10000) {
                                    player.BktDauLaDaiLuc[20] += Util.nextInt(5, 20);
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.BktNCHC, -1,
                                            "Nro KhanhDTK\n"
                                                    + "Bạn nâng cấp thành công hồn cốt:\n" + player.BktNameHoncot(6)
                                                    + "\nChỉ Số sau khi nâng cấp :\n"
                                                    + "Đánh Sát thương chuẩn: "
                                                    + Util.getFormatNumber(player.BktDauLaDaiLuc[20] * 100000000d)
                                                    + "dame.",
                                            player.BktNameHoncot(1), player.BktNameHoncot(2),
                                            player.BktNameHoncot(3),
                                            player.BktNameHoncot(4), player.BktNameHoncot(5),
                                            player.BktNameHoncot(6));
                                    player.Exptutien -= 100000000;
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, mhc, 10000);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                } else {
                                    Service.gI().sendThongBaoOK(player, "Cần 10k mảnh hồn cốt");
                                }
                                break;
                        }
                        break;
                    case ConstNpc.menu_detu:
                        switch (select) {
                            case 0:
                                Random random = new Random();
                                int petType = random.nextInt(4);
                                System.out.println("Player " + player.name + " Random Type Pet: " + petType);
                                switch (petType) {
                                    case 0:
                                        PetService.gI().changeBerusPet(player, player.pet.gender = 0);
                                        break;
                                    case 1:
                                        PetService.gI().changeBrolyPet(player, player.pet.gender = 0);
                                        break;
                                    case 2:
                                        PetService.gI().changeUbbPet(player, player.pet.gender = 0);
                                        break;
                                    case 3:
                                        PetService.gI().changeXenConPet(player, player.pet.gender = 0);
                                        break;
                                }
                                break;
                            case 1:
                                Random randomm = new Random();
                                int petTypee = randomm.nextInt(4);
                                System.out.println("Random Type Pet: " + petTypee);
                                switch (petTypee) {
                                    case 0:
                                        PetService.gI().changeBerusPet(player, player.pet.gender = 1);
                                        break;
                                    case 1:
                                        PetService.gI().changeBrolyPet(player, player.pet.gender = 1);
                                        break;
                                    case 2:
                                        PetService.gI().changeUbbPet(player, player.pet.gender = 1);
                                        break;
                                    case 3:
                                        PetService.gI().changeXenConPet(player, player.pet.gender = 1);
                                        break;
                                }
                                break;
                            case 2:
                                Random randommm = new Random();
                                int petTypeee = randommm.nextInt(4);
                                System.out.println("Random Type Pet: " + petTypeee);
                                switch (petTypeee) {
                                    case 0:
                                        PetService.gI().changeBerusPet(player, player.pet.gender = 2);
                                        break;
                                    case 1:
                                        PetService.gI().changeBrolyPet(player, player.pet.gender = 2);
                                        break;
                                    case 2:
                                        PetService.gI().changeUbbPet(player, player.pet.gender = 2);
                                        break;
                                    case 3:
                                        PetService.gI().changeXenConPet(player, player.pet.gender = 2);
                                        break;
                                }
                                break;
                        }
                        break;
                    case ConstNpc.QUANLYTK:
                        Player plql = (Player) PLAYERID_OBJECT.get(player.id);
                        if (plql != null) {
                            switch (select) {
                                case 0:
                                    if (player.isAdmin()) {
                                        Service.gI().sendThongBao(player, "Bạn không phải Admin Cấp Cao");
                                    } else {
                                        String[] selectsssss = new String[] { "CẤP QUYỀN", "HỦY QUYỀN" };
                                        NpcService.gI().createMenuConMeo(player, ConstNpc.MAKEADMIN, 22630,
                                                "|7|NÂNG KEY TRỰC TIẾP CHO PLAYER : " + plql.name + "?", selectsssss,
                                                plql);
                                    }
                                    break;
                            }
                        }
                        break;
                    case ConstNpc.MENU_ADMIN:
                        switch (select) {
                            case 0:
                                this.createMenuConMeo(player, 20102002, 22630,
                                        "|7| Admin Ngọc Rồng Alone\b|4| Người Đang Chơi: "
                                                + Client.gI().getPlayers().size() + "\n" + "|8|Current thread: "
                                                + (Thread.activeCount() - ServerManager.gI().threadMap) + "\n",
                                        "Ngọc Rồng", "Đệ Tử", "Bảo Trì", "Tìm Kiếm\nPlayer", "Chat All", "Cấp Key",
                                        "Đóng");
                                break;
                            case 1:
                                this.createOtherMenu(player, ConstNpc.CALL_BOSS,
                                        "Chọn Boss?", "Full Cụm\nANDROID", "BLACK", "BROLY", "Cụm\nCell",
                                        "Cụm\nDoanh trại", "DOREMON", "FIDE", "FIDE\nBlack", "Cụm\nGINYU", "Cụm\nNAPPA",
                                        "Gắp Thú");
                                break;
                            case 2:
                                this.createOtherMenu(player, 2010200322,
                                        "Buff Item", "Buff Item", "Item Option", "Buff Skh", "Buff Item Vip");
                                break;
                            case 3:
                                try {
                                    MaQuaTangManager.gI().checkInfomationGiftCode(player);
                                } catch (Exception ex) {
                                }
                                break;
                            case 4:
                                Input.gI().createFormNapCoin(player);
                                break;
                            case 5:
                                this.createOtherMenu(player, ConstNpc.GOIBOT,
                                        "Menu Buff Bot Server Alone"
                                                + "\n",
                                        "Bot\nPem Quái", "Bot\nBán Đồ", "Bot\nPem Quái");

                                break;
                            case 6:
                                Input.gI().createFormBotItem(player);
                                break;
                            case 7:
                                Input.gI().createFormBotBoss(player);
                                break;
                        }
                        break;
                    case ConstNpc.GOIBOT:
                        switch (select) {
                            case 0:
                                Input.gI().createFormBotQuai(player);
                                break;
                            case 1:
                                Input.gI().createFormBotItem(player);
                                break;
                            case 2:
                                Input.gI().createFormBotBoss(player);
                                break;
                        }
                        break;
                    case 19850:
                        switch (select) {
                            case 0:
                                try {
                                    ItemService.gI().settaiyoken19(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 219\n");
                                    e.printStackTrace();

                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().setgenki19(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 220\n");

                                    e.printStackTrace();

                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().setkamejoko19(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 221\n");
                                    e.printStackTrace();

                                }
                                break;
                        }
                        break;
                    case 19851:
                        switch (select) {

                            case 0:
                                try {
                                    ItemService.gI().setgodki19(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 222\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().setgoddam19(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 223\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().setsummon19(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 224\n");
                                    e.printStackTrace();
                                }
                                break;
                        }
                        break;
                    case 19852:
                        switch (select) {

                            case 0:
                                try {
                                    ItemService.gI().setgodgalick16(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 225\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().setmonkey16(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 226\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().setgodhp16(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 227\n");
                                    e.printStackTrace();
                                }
                                break;
                        }
                        break;
                    case 19860:
                        switch (select) {

                            case 0:
                                try {
                                    ItemService.gI().set14taiyoken(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 228\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().set14genki(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 229\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().set14kamejoko(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 230\n");
                                    e.printStackTrace();
                                }
                                break;
                        }
                        break;
                    case 19861:
                        switch (select) {

                            case 0:
                                try {
                                    ItemService.gI().set14godki(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 231\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().set14goddam(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 232\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().set14summon(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 233\n");
                                    e.printStackTrace();
                                }
                                break;
                        }
                        break;
                    case 19862:
                        switch (select) {

                            case 0:
                                try {
                                    ItemService.gI().set14godgalick(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 234\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().setmonkey14(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 235\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().setgodhp14(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 236\n");
                                    e.printStackTrace();
                                }
                                break;
                        }
                        break;
                    case 19870:
                        switch (select) {

                            case 0:
                                try {
                                    ItemService.gI().set1taiyoken(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 237\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().set1genki(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 238\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().set1kamejoko(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 239\n");
                                    e.printStackTrace();

                                }
                                break;
                        }
                        break;
                    case 19871:
                        switch (select) {

                            case 0:
                                try {
                                    ItemService.gI().set2godki(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 240\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().set1goddam(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 241\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().set1summon(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 242\n");
                                    e.printStackTrace();
                                }
                                break;
                        }
                        break;
                    case 19872:
                        switch (select) {

                            case 0:
                                try {
                                    ItemService.gI().set1godgalick(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 243\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().setmonkey1(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 244\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().setgodhp1(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 245\n");
                                    e.printStackTrace();
                                }
                                break;
                        }
                        break;
                    case ConstNpc.NpcThanThu:
                        if (player.TrieuHoiCapBac != -1) {
                            switch (select) {
                                case 0:
                                    Service.gI().showthanthu(player);
                                    break;
                                case 1:
                                    if (player.inventory.ruby < 200) {
                                        Service.gI().sendThongBaoOK(player,
                                                "Không đủ Hồng ngọc");
                                        return;
                                    }
                                    player.inventory.ruby -= 200;
                                    player.TrieuHoiThucAn++;
                                    if (player.TrieuHoiThucAn > 200) {
                                        player.TrieuHoiCapBac = -1;
                                        Service.gI().sendThongBaoOK(player,
                                                "Bạn đã cho đệ tử ăn quá nhiều");
                                    } else {
                                        Service.gI().sendThongBao(player,
                                                "|2|Thức ăn: " + player.TrieuHoiThucAn
                                                        + "%\n|1|Đã cho Đệ Tử 2 ăn\n|7|Lưu ý: khi cho quá 200% Đệ Tử 2 sẽ no quá mà chết");
                                    }
                                    Service.gI().showthanthu(player);
                                    break;
                                case 2:
                                    player.TrieuHoipet.changeStatus(Thu_TrieuHoi.FOLLOW);
                                    break;
                                case 3:
                                    player.TrieuHoipet.changeStatus(Thu_TrieuHoi.ATTACK_PLAYER);
                                    player.TrieuHoipet.effectSkill.removeSkillEffectWhenDie();
                                    Service.gI().sendThongBao(player, "|2|Mở khoá tàn sát cho Đệ Tử 2");
                                    break;
                                case 4:
                                    player.TrieuHoipet.changeStatus(Thu_TrieuHoi.ATTACK_MOB);
                                    break;
                                case 5:
                                    player.TrieuHoipet.changeStatus(Thu_TrieuHoi.GOHOME);
                                    break;
                                case 6:
                                    if (player.trangthai == false) {
                                        player.trangthai = true;
                                        if (player.inventory.ruby < 200) {
                                            Service.gI().sendThongBao(player,
                                                    "|7|Không đủ Hồng ngọc");
                                            return;
                                        }
                                        player.inventory.ruby -= 200;
                                        player.TrieuHoiThucAn++;
                                        player.Autothucan = System.currentTimeMillis();
                                        if (player.TrieuHoiThucAn > 200) {
                                            player.TrieuHoiCapBac = -1;
                                            Service.gI().sendThongBao(player,
                                                    "|7|Vì cho Đệ Tử 2 ăn quá no nên Đệ Tử 2 đã bạo thể mà chết.");
                                        } else {
                                            Service.gI().sendThongBao(player,
                                                    "|2|Thức ăn Đệ Tử 2: " + player.TrieuHoiThucAn
                                                            + "%\n|1|Đã cho Đệ Tử 2 ăn\n|7|Lưu ý: khi cho quá 200% Đệ Tử 2 sẽ no quá mà chết");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player,
                                                "|1|Đã dừng Auto cho Đệ Tử 2 ăn");
                                        player.trangthai = false;
                                    }
                                    break;
                                case 7:
                                    if (player.TrieuHoiCapBac != -1 && player.TrieuHoiCapBac < 10) {
                                        NpcService.gI().createMenuConMeo(player, ConstNpc.DOT_PHA_THANTHU, 22630,
                                                "|1|Nâng cấp đệ tử 2 "
                                                        + "\n|2|Cấp bậc hiện tại: "
                                                        + player.NameThanthu(player.TrieuHoiCapBac)
                                                        + "\n|2|Level: " + player.TrieuHoiLevel
                                                        + "\n|2|Kinh nghiệm: " + Util.format(player.TrieuHoiExpThanThu)
                                                        + "\n|1| Yêu cầu Đệ Tử 2 đạt cấp 100"
                                                        + "\n|6|Cần: " + (player.TrieuHoiCapBac + 1) * 9 + " "
                                                        + player.DaDotpha(player.TrieuHoiCapBac)
                                                        + "\nĐể Nâng Cấp lên Cấp bậc "
                                                        + player.NameThanthu(player.TrieuHoiCapBac + 1)
                                                        + "\b|3|*Thành công: Cấp bậc Đệ Tử 2 nâng 1 bậc và Level trở về 0"
                                                        + "\b|3|*Thất bại: Trừ nguyên liệu nâng cấp"
                                                        + "\n|1|Tỉ lệ Thành công: " + (100 - player.TrieuHoiCapBac * 10)
                                                        + "%",
                                                "Nâng Cấp", "Đóng");
                                    } else {
                                        NpcService.gI().createMenuConMeo(player, ConstNpc.DOT_PHA_THANTHU, 12713,
                                                "|7|Nâng Cấp Đệ Tử 2 "
                                                        + "\n\n|2|Cấp bậc hiện tại: "
                                                        + player.NameThanthu(player.TrieuHoiCapBac)
                                                        + "\n|7| Đệ Tử 2 của bạn đã đạt Cấp bậc Cao nhất",
                                                "Đóng");
                                    }
                                    break;
                            }
                        } else {
                            Service.gI().sendThongBao(player, "|7|Bạn chưa có Đệ Tử 2 để sài tính năng này.");
                        }
                        break;
                    case ConstNpc.DOT_PHA_THANTHU:
                        switch (select) {
                            case 0:
                                Item linhthach = null;
                                try {
                                    if (player.TrieuHoiCapBac != -1 && player.TrieuHoiCapBac >= 0
                                            && player.TrieuHoiCapBac < 4) {
                                        linhthach = InventoryServiceNew.gI().findItemBag(player, 1266);
                                    } else {
                                        linhthach = InventoryServiceNew.gI().findItemBag(player,
                                                1269 - player.TrieuHoiCapBac);
                                    }
                                } catch (Exception e) {
                                    System.out.println("vvvvv");
                                }
                                if (player.TrieuHoiCapBac != -1 && player.TrieuHoiLevel == 100
                                        && player.TrieuHoiCapBac < 10) {
                                    if (linhthach != null && linhthach.quantity >= (player.TrieuHoiCapBac + 1) * 9) {
                                        if (Util.isTrue(100 - player.TrieuHoiCapBac * 10, 100)) {
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, linhthach,
                                                    (player.TrieuHoiCapBac + 1) * 9);
                                            player.TrieuHoiLevel = 0;
                                            player.TrieuHoiExpThanThu = 0;
                                            player.TrieuHoiCapBac++;
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Service.gI().sendThongBao(player,
                                                    "|2|HAHAHA Đệ Tử 2 đã tấn thăng "
                                                            + player.NameThanthu(player.TrieuHoiCapBac)
                                                            + " rồi\nTất cả quỳ xuống !!");
                                        } else {
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, linhthach,
                                                    (player.TrieuHoiCapBac + 1) * 9);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Service.gI().sendThongBao(player,
                                                    "|7|Khốn khiếp, lại đột phá thất bại rồi");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player,
                                                "|7| Chưa đủ " + player.DaDotpha(player.TrieuHoiCapBac));
                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "|7| Yêu cầu Đệ Tử 2 đạt Cấp 100");
                                }
                                break;
                        }
                        break;
                    case ConstNpc.menutd:
                        switch (select) {

                            case 0:
                                try {
                                    ItemService.gI().settaiyoken(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 246\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().setgenki(player);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().setkamejoko(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 247\n");
                                    e.printStackTrace();
                                }
                                break;
                        }
                        break;

                    case ConstNpc.menunm:
                        switch (select) {
                            case 0:
                                try {
                                    ItemService.gI().setgodki(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 248\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().setgoddam(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 249\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().setsummon(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 250\n");
                                    e.printStackTrace();
                                }
                                break;
                        }
                        break;
                    case ConstNpc.menuxd:
                        switch (select) {
                            case 0:
                                try {
                                    ItemService.gI().setgodgalick(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 251\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().setmonkey(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 252\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().setgodhp(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 253\n");
                                    e.printStackTrace();
                                }
                                break;
                        }
                        break;
                    case ConstNpc.XU_HRZ:
                        try {
                            if (select == 0) {
                                NapVangService.ChonGiaTien(20, player);
                            } else if (select == 1) {
                                NapVangService.ChonGiaTien(50, player);
                            } else if (select == 2) {
                                NapVangService.ChonGiaTien(100, player);
                            } else if (select == 3) {
                                NapVangService.ChonGiaTien(500, player);

                            } else {

                                break;
                            }
                            break;
                        } catch (Exception e) {
                            System.err.print("\nError at 254\n");
                            e.printStackTrace();
                            break;

                        }
                    case ConstNpc.CONFIRM_DISSOLUTION_CLAN:
                        switch (select) {
                            case 0:
                                Clan clan = player.clan;
                                clan.deleteDB(clan.id);
                                Manager.CLANS.remove(clan);
                                player.clan = null;
                                player.clanMember = null;
                                ClanService.gI().sendMyClan(player);
                                ClanService.gI().sendClanId(player);
                                Service.gI().sendThongBao(player, "Đã giải tán bang hội.");
                                break;
                        }
                        break;
                    case ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND:
                        if (select == 0) {
                            for (int i = 0; i < player.inventory.itemsBoxCrackBall.size(); i++) {
                                player.inventory.itemsBoxCrackBall.set(i, ItemService.gI().createItemNull());
                            }
                            player.inventory.itemsBoxCrackBall.clear();
                            Service.gI().sendThongBao(player, "Đã xóa hết vật phẩm trong rương");
                        }
                        break;
                    case ConstNpc.MENU_FIND_PLAYER:
                        Player p = (Player) PLAYERID_OBJECT.get(player.id);
                        if (p != null) {
                            switch (select) {
                                case 0:
                                    if (p.zone != null) {
                                        ChangeMapService.gI().changeMapYardrat(player, p.zone, p.location.x,
                                                p.location.y);
                                    }
                                    break;
                                case 1:
                                    if (p.zone != null) {
                                        ChangeMapService.gI().changeMap(p, player.zone, player.location.x,
                                                player.location.y);
                                    }
                                    break;
                                case 2:
                                    Input.gI().createFormChangeName(player, p);
                                    break;
                                case 3:
                                    String[] selects = new String[] { "Đồng ý", "Hủy" };
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.BAN_PLAYER, -1,
                                            "Bạn có chắc chắn muốn ban " + p.name, selects, p);
                                    break;
                                case 4:
                                    Service.gI().sendThongBao(player, "Kik người chơi " + p.name + " thành công");
                                    Client.gI().getPlayers().remove(p);
                                    Client.gI().kickSession(p.getSession());
                                    break;
                            }
                        }
                        break;
                    case ConstNpc.MENU_GIAO_BONG:
                        ItemService.gI().giaobong(player, (int) Util.tinhLuyThua(10, select + 2));
                        break;
                    case ConstNpc.CONFIRM_DOI_THUONG_SU_KIEN:
                        if (select == 0) {
                            ItemService.gI().openBoxVip(player);
                        }
                        break;
                    // case ConstNpc.CONFIRM_TELE_NAMEC:
                    // if (select == 0) {
                    // NgocRongNamecService.gI().teleportToNrNamec(player);
                    // player.inventory.subGemAndRubyAndTien(50);
                    // Service.gI().sendMoney(player);
                    // }
                    // break;
                }
            }
        };
    }

}

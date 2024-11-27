package com.KhanhDTK.jdbc.daos;

import com.KhanhDTK.card.Card;
import com.KhanhDTK.card.OptionCard;
import com.girlkun.database.GirlkunDB;
import com.girlkun.result.GirlkunResultSet;
import com.KhanhDTK.consts.ConstPlayer;
import com.KhanhDTK.data.DataGame;
import com.KhanhDTK.models.Template.ArchivementTemplate;
//import com.KhanhDTK.models.ThanhTich.ThanhTich;
//import com.KhanhDTK.models.ThanhTich.ThanhTichPlayer;
import com.KhanhDTK.models.clan.Clan;
import com.KhanhDTK.models.clan.ClanMember;
import com.KhanhDTK.models.item.Item;
import com.KhanhDTK.models.item.ItemTime;
import com.KhanhDTK.models.npc.specialnpc.MabuEgg;
import com.KhanhDTK.models.npc.specialnpc.BillEgg;
import com.KhanhDTK.models.npc.specialnpc.MagicTree;
import com.KhanhDTK.models.player.Enemy;
import com.KhanhDTK.models.player.Friend;
import com.KhanhDTK.models.player.Fusion;
import com.KhanhDTK.models.player.Pet;
import com.KhanhDTK.models.player.Player;
import com.KhanhDTK.models.skill.Skill;
import com.KhanhDTK.models.task.TaskMain;
import com.girlkun.network.server.GirlkunSessionManager;
import com.girlkun.network.session.ISession;
import com.KhanhDTK.server.Client;
import com.KhanhDTK.server.Manager;
import com.KhanhDTK.server.ServerNotify;
import com.KhanhDTK.server.io.MySession;
import com.KhanhDTK.server.model.AntiLogin;
import com.KhanhDTK.services.ClanService;
import com.KhanhDTK.services.IntrinsicService;
import com.KhanhDTK.services.ItemService;
import com.KhanhDTK.services.MapService;
import com.KhanhDTK.services.Service;
import com.KhanhDTK.services.TaskService;
import com.KhanhDTK.utils.Logger;
import com.KhanhDTK.utils.SkillUtil;
import com.KhanhDTK.utils.TimeUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.KhanhDTK.utils.Util;
import java.util.Calendar;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class GodGK {

    public static List<OptionCard> loadOptionCard(JSONArray json) {
        List<OptionCard> ops = new ArrayList<>();
        try {
            for (int i = 0; i < json.size(); i++) {
                JSONObject ob = (JSONObject) json.get(i);
                if (ob != null) {
                    ops.add(new OptionCard(Integer.parseInt(ob.get("id").toString()), Integer.parseInt(ob.get("param").toString()), Byte.parseByte(ob.get("active").toString())));
                }
            }
        } catch (Exception e) {

        }
        return ops;
    }
    public static Boolean baotri = false;

    public static synchronized Player login(MySession session, AntiLogin al) {
        Player player = null;
        GirlkunResultSet rs = null;
        try {
            rs = GirlkunDB.executeQuery("SELECT * FROM account WHERE username = ? AND password = ?", session.uu, session.pp);
            if (rs.first()) {
                session.userId = rs.getInt("account.id");
                session.isAdmin = rs.getBoolean("is_admin");
                session.lastTimeLogout = rs.getTimestamp("last_time_logout").getTime();
                session.lastTimeOff = rs.getTimestamp("last_time_off").getTime();
                session.actived = rs.getBoolean("active");
                session.mtvgtd = rs.getBoolean("mtvgt");
                session.vip1d = rs.getBoolean("vip1");
                session.vip2d = rs.getBoolean("vip2");
                session.vip3d = rs.getBoolean("vip3");
                session.vip4d = rs.getBoolean("vip4");
                session.vip5d = rs.getBoolean("vip5");
                session.vip6d = rs.getBoolean("vip6");
                session.tongnap = rs.getInt("tongnap");
                session.vnd = rs.getInt("vnd");
                session.mocnap = rs.getInt("mocnap");
                session.gioithieu = rs.getInt("gioithieu");
                session.goldBar = rs.getInt("account.thoi_vang");
                session.bdPlayer = rs.getDouble("account.bd_player");
                long lastTimeLogin = rs.getTimestamp("last_time_login").getTime();
                int secondsPass1 = (int) ((System.currentTimeMillis() - lastTimeLogin) / 1000);
                long lastTimeLogout = rs.getTimestamp("last_time_logout").getTime();
                int secondsPass = (int) ((System.currentTimeMillis() - lastTimeLogout) / 1000);
                if (rs.getBoolean("ban")) {
                    Service.getInstance().sendThongBaoOK(session, "Tài khoản đã bị khóa!");
                }
//                else if (!session.isAdmin) {
//                    Service.gI().sendThongBaoOK(session, "Chi danh cho admin");
//                }
                else if (baotri && session.isAdmin) {
                    Service.getInstance().sendThongBaoOK(session, "Máy chủ đang bảo trì, vui lòng quay lại sau!");
                } else if (secondsPass1 < Manager.SECOND_WAIT_LOGIN) {
                    if (secondsPass < secondsPass1) {
                        Service.getInstance().sendThongBaoOK(session, "Vui lòng chờ " + (Manager.SECOND_WAIT_LOGIN - secondsPass) + "s");
                        return null;
                    }
                    Service.getInstance().sendThongBaoOK(session, "Vui lòng chờ " + (Manager.SECOND_WAIT_LOGIN - secondsPass1) + "s");
                    return null;
                } else if (rs.getTimestamp("last_time_login").getTime() > session.lastTimeLogout) {
                    Player plInGame = Client.gI().getPlayerByUser(session.userId);
                    if (plInGame != null) {
                        Client.gI().kickSession(plInGame.getSession());
                        Service.getInstance().sendThongBaoOK(session, "Ai đó đang đăng nhập tài khoản?");
                    } else {
                    }
                    //Service.getInstance().sendThongBaoOK(session, "Tài khoản đang được đăng nhập tại máy chủ khác");
                } else {
                    if (secondsPass < Manager.SECOND_WAIT_LOGIN) {
                        Service.getInstance().sendThongBaoOK(session, "Vui lòng chờ " + (Manager.SECOND_WAIT_LOGIN - secondsPass) + "s");
                    } else {//set time logout trước rồi đọc data player
                        rs = GirlkunDB.executeQuery("select * from player where account_id = ? limit 1", session.userId);
                        if (!rs.first()) {
                            Service.gI().switchToCreateChar(session);
                            DataGame.sendDataItemBG(session);
                            DataGame.sendVersionGame(session);
                            DataGame.sendTileSetInfo(session);
                            Service.gI().sendMessage(session, -93, "1630679752231_-93_r");
                            DataGame.updateData(session);
                        } else {
                            Player plInGame = Client.gI().getPlayerByUser(session.userId);
                            if (plInGame != null) {
                                Client.gI().kickSession(plInGame.getSession());
                            }
                            int plHp = 2000000000;
                            int plMp = 2000000000;
                            JSONValue jv = new JSONValue();
                            JSONArray dataArray = null;

                            player = new Player();

                            //base info
                            player.id = rs.getInt("id");
                            player.name = rs.getString("name");
                            player.head = rs.getShort("head");
                            player.PointBoss = rs.getInt("PointBoss");
//                            player.cauca.PointCauCa = rs.getInt("PointCauCa");
                            player.ResetSkill = rs.getInt("ResetSkill");
                            player.LastDoanhTrai = rs.getLong("LastDoanhTrai");
                            player.gender = rs.getByte("gender");
                            player.haveTennisSpaceShip = rs.getBoolean("have_tennis_space_ship");
                            player.violate = rs.getInt("violate");
                            player.pointPvp = rs.getInt("pointPvp");
                            player.NguHanhSonPoint = rs.getInt("NguHanhSonPoint");
                            player.point_gapthu = rs.getInt("point_gapthu");
                            player.point_hungvuong = rs.getInt("point_hungvuong");
                            player.capChuyenSinh = rs.getInt("capChuyenSinh");
                            player.capboss = rs.getInt("capboss");
                            player.kemtraicay = rs.getInt("kemtraicay");
                            player.nuocmia = rs.getInt("nuocmia");
                            player.Captutien = rs.getInt("Captutien");
                            if (player.Captutien > 5000) {
                                player.Captutien = 5000;
                            }
                            player.Exptutien = rs.getLong("Exptutien");
                            if (player.Captutien > 10000) {
                                player.Captutien = 10000;
                            }
                            player.luotNhanBuaMienPhi = rs.getInt("checkNhanQua");
                            player.isbienhinh = rs.getInt("isbienhinh");
                            long now = System.currentTimeMillis();
                            long thoiGianOffline = now - session.lastTimeOff;
                            player.timeoff = thoiGianOffline /= 60000;
                            player.totalPlayerViolate = 0;
                            int clanId = rs.getInt("clan_id_sv" + Manager.SERVER);
                            if (clanId != -1) {
                                Clan clan = ClanService.gI().getClanById(clanId);
                                if (clan != null) {
                                    for (ClanMember cm : clan.getMembers()) {
                                        if (cm.id == player.id) {
                                            clan.addMemberOnline(player);
                                            player.clan = clan;
                                            player.clanMember = cm;
                                            break;
                                        }
                                    }
                                }
                            }

                            //data kim lượng
                            dataArray = (JSONArray) jv.parse(rs.getString("data_inventory"));
                            player.inventory.gold = Long.parseLong(String.valueOf(dataArray.get(0)));
                            player.inventory.gem = Integer.parseInt(String.valueOf(dataArray.get(1)));
                            player.inventory.ruby = Integer.parseInt(String.valueOf(dataArray.get(2)));
                            player.inventory.coupon = Integer.parseInt(String.valueOf(dataArray.get(3)));
                            if (dataArray.size() >= 4) {
                                player.inventory.coupon = Integer.parseInt(String.valueOf(dataArray.get(3)));
                            } else {
                                player.inventory.coupon = 0;
                            }
                            if (dataArray.size() >= 5 && false) {
                                player.inventory.event = Integer.parseInt(String.valueOf(dataArray.get(4)));
                            } else {
                                player.inventory.event = 0;
                            }
                            dataArray.clear();

                            //data điểm nè
                            dataArray = (JSONArray) JSONValue.parse(rs.getString("data_diem"));
                            player.vip = Byte.parseByte(String.valueOf(dataArray.get(0)));
                            player.timevip = Long.parseLong(String.valueOf(dataArray.get(1)));
                            player.tutien = Long.parseLong(String.valueOf(dataArray.get(2)));
                            dataArray.clear();

                            dataArray = (JSONArray) JSONValue.parse(rs.getString("Bkttutien"));
                            player.Bkttutien[0] = Integer.parseInt(String.valueOf(dataArray.get(0)));
                            player.Bkttutien[1] = Integer.parseInt(String.valueOf(dataArray.get(1)));
                            if (player.Bkttutien[1] > 96) {
                                player.Bkttutien[1] = 0;
                            }
                            player.Bkttutien[2] =Integer.parseInt(String.valueOf(dataArray.get(2)));
                            if (player.Bkttutien[2] > 50) {
                                player.Bkttutien[2] = 0;
                            }
                            dataArray.clear();

                            dataArray = (JSONArray) JSONValue.parse(rs.getString("BktDLDL"));
                            player.BktDauLaDaiLuc[0] = Integer.parseInt(String.valueOf(dataArray.get(0)));
                            player.BktDauLaDaiLuc[1] = Integer.parseInt(String.valueOf(dataArray.get(1)));
                            player.BktDauLaDaiLuc[2] =Integer.parseInt(String.valueOf(dataArray.get(2)));
                            player.BktDauLaDaiLuc[3] = Integer.parseInt(String.valueOf(dataArray.get(3)));
                            player.BktDauLaDaiLuc[4] = Integer.parseInt(String.valueOf(dataArray.get(4)));
                            player.BktDauLaDaiLuc[5] = Integer.parseInt(String.valueOf(dataArray.get(5)));
                            player.BktDauLaDaiLuc[6] = Integer.parseInt(String.valueOf(dataArray.get(6)));
                            player.BktDauLaDaiLuc[7] = Integer.parseInt(String.valueOf(dataArray.get(7)));
                            player.BktDauLaDaiLuc[8] = Integer.parseInt(String.valueOf(dataArray.get(8)));
                            player.BktDauLaDaiLuc[9] = Integer.parseInt(String.valueOf(dataArray.get(9)));
                            player.BktDauLaDaiLuc[10] = Integer.parseInt(String.valueOf(dataArray.get(10)));
                            player.BktDauLaDaiLuc[11] = Integer.parseInt(String.valueOf(dataArray.get(11)));
                            player.BktDauLaDaiLuc[12] = Integer.parseInt(String.valueOf(dataArray.get(12)));
                            player.BktDauLaDaiLuc[13] = Integer.parseInt(String.valueOf(dataArray.get(13)));
                            player.BktDauLaDaiLuc[14] = Integer.parseInt(String.valueOf(dataArray.get(14)));
                            player.BktDauLaDaiLuc[15] = Integer.parseInt(String.valueOf(dataArray.get(15)));
                            player.BktDauLaDaiLuc[16] = Integer.parseInt(String.valueOf(dataArray.get(16)));
                            player.BktDauLaDaiLuc[17] = Integer.parseInt(String.valueOf(dataArray.get(17)));
                            player.BktDauLaDaiLuc[18] =Integer.parseInt(String.valueOf(dataArray.get(18)));
                            player.BktDauLaDaiLuc[19] = Integer.parseInt(String.valueOf(dataArray.get(19)));
                            player.BktDauLaDaiLuc[20] = Integer.parseInt(String.valueOf(dataArray.get(20)));
                            dataArray.clear();

                            dataArray = (JSONArray) jv.parse(rs.getString("dhtime"));
                            player.isTitleUse = Integer.parseInt(String.valueOf(dataArray.get(0))) == 1 ? true : false;
                            player.lastTimeTitle1 = Long.parseLong(String.valueOf(dataArray.get(1)));
                            dataArray.clear();

                            // data rada card
                            dataArray = (JSONArray) jv.parse(rs.getString("data_card"));
                            for (int i = 0; i < dataArray.size(); i++) {
                                JSONObject obj = (JSONObject) dataArray.get(i);
                                player.Cards.add(new Card(Short.parseShort(obj.get("id").toString()), Byte.parseByte(obj.get("amount").toString()), Byte.parseByte(obj.get("max").toString()), Byte.parseByte(obj.get("level").toString()), loadOptionCard((JSONArray) JSONValue.parse(obj.get("option").toString())), Byte.parseByte(obj.get("used").toString())));
                            }
                            dataArray.clear();

                            dataArray = (JSONArray) JSONValue.parse(rs.getString("diemdanh"));
                            player.CheckDayOnl = Byte.parseByte(String.valueOf(dataArray.get(0)));
                            player.diemdanh = Byte.parseByte(String.valueOf(dataArray.get(1)));
                            dataArray.clear();
                            try {
                                Calendar ngayvps = Calendar.getInstance();
                                int ngayhomnay = ngayvps.get(Calendar.DAY_OF_MONTH);
                                if (player.CheckDayOnl == 0) {
                                    player.CheckDayOnl = (byte) (ngayhomnay - 1);
                                }
                                if (ngayhomnay > player.CheckDayOnl) {
                                    player.CheckDayOnl = (byte) ngayhomnay;
                                    player.diemdanh = 0;
                                }
                            } catch (Exception e) {
                            }

                            dataArray = (JSONArray) JSONValue.parse(rs.getString("diemdanhsk"));
                            player.CheckDayOnl = Byte.parseByte(String.valueOf(dataArray.get(0)));
                            player.diemdanhsk = Byte.parseByte(String.valueOf(dataArray.get(1)));
                            dataArray.clear();
                            try {
                                Calendar ngayvps = Calendar.getInstance();
                                int ngayhomnay = ngayvps.get(Calendar.DAY_OF_MONTH);
                                if (player.CheckDayOnl == 0) {
                                    player.CheckDayOnl = (byte) (ngayhomnay - 1);
                                }
                                if (ngayhomnay > player.CheckDayOnl) {
                                    player.CheckDayOnl = (byte) ngayhomnay;
                                    player.diemdanhsk = 0;
                                }
                            } catch (Exception e) {
                            }

                            //data tọa độ
                            try {
                                dataArray = (JSONArray) jv.parse(rs.getString("data_location"));
                                int mapId = Integer.parseInt(String.valueOf(dataArray.get(0)));
                                player.location.x = Integer.parseInt(String.valueOf(dataArray.get(1)));
                                player.location.y = Integer.parseInt(String.valueOf(dataArray.get(2)));
                                player.location.lastTimeplayerMove = System.currentTimeMillis();
                                if (MapService.gI().isMapDoanhTrai(mapId) || MapService.gI().isMapBlackBallWar(mapId)
                                        || MapService.gI().isMapBanDoKhoBau(mapId) || MapService.gI().isMapKhiGas(mapId) || MapService.gI().isMapMaBu(mapId)) {
                                    mapId = player.gender + 21;
                                    player.location.x = 300;
                                    player.location.y = 336;
                                }
                                player.zone = MapService.gI().getMapCanJoin(player, mapId, -1);
                            } catch (Exception e) {

                            }
                            dataArray.clear();

                            //data chỉ số
                            dataArray = (JSONArray) jv.parse(rs.getString("data_point"));
                            player.nPoint.limitPower = Byte.parseByte(String.valueOf(dataArray.get(0)));
                            player.nPoint.power = Long.parseLong(String.valueOf(dataArray.get(1)));
                            player.nPoint.tiemNang = Long.parseLong(String.valueOf(dataArray.get(2)));
                            player.nPoint.stamina = Short.parseShort(String.valueOf(dataArray.get(3)));
                            player.nPoint.maxStamina = Short.parseShort(String.valueOf(dataArray.get(4)));
                            player.nPoint.hpg = Integer.parseInt(String.valueOf(dataArray.get(5)));
                            player.nPoint.mpg = Integer.parseInt(String.valueOf(dataArray.get(6)));
                            player.nPoint.dameg = Integer.parseInt(String.valueOf(dataArray.get(7)));
                            player.nPoint.defg = Integer.parseInt(String.valueOf(dataArray.get(8)));
                            player.nPoint.critg = Byte.parseByte(String.valueOf(dataArray.get(9)));
                            dataArray.get(10); //** Năng động
                            plHp = Integer.parseInt(String.valueOf(dataArray.get(11)));
                            plMp = Integer.parseInt(String.valueOf(dataArray.get(12)));
                            dataArray.clear();

                            //data đậu thần
                            dataArray = (JSONArray) jv.parse(rs.getString("data_magic_tree"));
                            byte level = Byte.parseByte(String.valueOf(dataArray.get(0)));
                            byte currPea = Byte.parseByte(String.valueOf(dataArray.get(1)));
                            boolean isUpgrade = Byte.parseByte(String.valueOf(dataArray.get(2))) == 1;
                            long lastTimeHarvest = Long.parseLong(String.valueOf(dataArray.get(3)));
                            long lastTimeUpgrade = Long.parseLong(String.valueOf(dataArray.get(4)));
                            player.magicTree = new MagicTree(player, level, currPea, lastTimeHarvest, isUpgrade, lastTimeUpgrade);
                            dataArray.clear();

                            //data phần thưởng sao đen
                            dataArray = (JSONArray) jv.parse(rs.getString("data_black_ball"));
                            JSONArray dataBlackBall = null;
                            for (int i = 0; i < dataArray.size(); i++) {
                                dataBlackBall = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                                player.rewardBlackBall.timeOutOfDateReward[i] = Long.parseLong(String.valueOf(dataBlackBall.get(0)));
                                player.rewardBlackBall.lastTimeGetReward[i] = Long.parseLong(String.valueOf(dataBlackBall.get(1)));
                                try {
                                    player.rewardBlackBall.quantilyBlackBall[i] = dataBlackBall.get(2) != null ? Integer.parseInt(String.valueOf(dataBlackBall.get(2))) : 0;
                                } catch (Exception e) {
                                    player.rewardBlackBall.quantilyBlackBall[i] = player.rewardBlackBall.timeOutOfDateReward[i] != 0 ? 1 : 0;

                                }
                                dataBlackBall.clear();
                            }
                            dataArray.clear();

                            //data body
                            dataArray = (JSONArray) jv.parse(rs.getString("items_body"));
                            for (int i = 0; i < dataArray.size(); i++) {
                                Item item = null;
                                JSONArray dataItem = (JSONArray) jv.parse(dataArray.get(i).toString());
                                short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                                if (tempId != -1) {
                                    item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                                    JSONArray options = (JSONArray) jv.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                                    boolean flag = false;
                                    for (int j = 0; j < options.size(); j++) {
                                        JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                                        if (tempId != 884) {
                                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                                        } else {
                                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))) == 14 ? 5 : Integer.parseInt(String.valueOf(opt.get(0))),
                                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                                        }
                                        if (Integer.parseInt(String.valueOf(opt.get(0))) == 50 && tempId == 884) {
                                            flag = true;
                                        }
                                    }
                                    item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                                    if (ItemService.gI().isOutOfDateTime(item)) {
                                        item = ItemService.gI().createItemNull();
                                    }
                                    if (tempId == 884 && flag) {
                                        List<Item.ItemOption> itemsToRemove = new ArrayList<>();
                                        for (Item.ItemOption op : item.itemOptions) {
                                            if (op.optionTemplate.id == 50 || op.optionTemplate.id == 102) {
                                                itemsToRemove.add(op);
                                            }
                                        }
                                        item.itemOptions.removeAll(itemsToRemove);
                                    }
                                } else {
                                    item = ItemService.gI().createItemNull();
                                }
                                com.KhanhDTK.models.Event.ResetParamItem.SetBasicChiSo(item);
                                player.inventory.itemsBody.add(item);
                            }
                            while (player.inventory.itemsBody.size() <= 11) {
                                player.inventory.itemsBody.add(ItemService.gI().createItemNull());
                            }
                            dataArray.clear();

                            //data bag
                            dataArray = (JSONArray) jv.parse(rs.getString("items_bag"));
                            for (int i = 0; i < dataArray.size(); i++) {
                                Item item = null;
                                JSONArray dataItem = (JSONArray) jv.parse(dataArray.get(i).toString());
                                short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                                if (tempId != -1) {
                                    item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                                    JSONArray options = (JSONArray) jv.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                                    boolean flag = false;
                                    for (int j = 0; j < options.size(); j++) {
                                        JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                                        if (tempId != 884) {
                                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                                        } else {
                                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))) == 14 ? 5 : Integer.parseInt(String.valueOf(opt.get(0))),
                                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                                        }
                                        if (Integer.parseInt(String.valueOf(opt.get(0))) == 50 && tempId == 884) {
                                            flag = true;
                                        }

                                    }
                                    item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                                    if (ItemService.gI().isOutOfDateTime(item)) {
                                        item = ItemService.gI().createItemNull();
                                    }
                                    if (tempId == 884 && flag) {
                                        List<Item.ItemOption> itemsToRemove = new ArrayList<>();
                                        for (Item.ItemOption op : item.itemOptions) {
                                            if (op.optionTemplate.id == 50 || op.optionTemplate.id == 102) {
                                                itemsToRemove.add(op);
                                            }
                                        }

                                        item.itemOptions.removeAll(itemsToRemove);
                                    }
                                } else {
                                    item = ItemService.gI().createItemNull();
                                }
                                com.KhanhDTK.models.Event.ResetParamItem.SetBasicChiSo(item);
                                player.inventory.itemsBag.add(item);
                            }
                            while (player.inventory.itemsBag.size() <= 120) {
                                player.inventory.itemsBag.add(ItemService.gI().createItemNull());
                            }
                            dataArray.clear();

                            //data box
                            dataArray = (JSONArray) jv.parse(rs.getString("items_box"));
                            for (int i = 0; i < dataArray.size(); i++) {
                                Item item = null;
                                JSONArray dataItem = (JSONArray) jv.parse(dataArray.get(i).toString());
                                short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                                boolean flag = false;
                                if (tempId != -1) {
                                    item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                                    JSONArray options = (JSONArray) jv.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                                    for (int j = 0; j < options.size(); j++) {
                                        JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                                        if (tempId != 884) {
                                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                                        } else {
                                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))) == 14 ? 5 : Integer.parseInt(String.valueOf(opt.get(0))),
                                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                                        }
                                        if (Integer.parseInt(String.valueOf(opt.get(0))) == 50 && tempId == 884) {
                                            flag = true;
                                        }
                                    }
                                    item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                                    if (ItemService.gI().isOutOfDateTime(item)) {
                                        item = ItemService.gI().createItemNull();
                                    }
                                    if (tempId == 884 && flag) {
                                        List<Item.ItemOption> itemsToRemove = new ArrayList<>();
                                        for (Item.ItemOption op : item.itemOptions) {
                                            if (op.optionTemplate.id == 50 || op.optionTemplate.id == 102) {
                                                itemsToRemove.add(op);
                                            }
                                        }
                                        item.itemOptions.removeAll(itemsToRemove);
                                    }
                                } else {
                                    item = ItemService.gI().createItemNull();
                                }
                                com.KhanhDTK.models.Event.ResetParamItem.SetBasicChiSo(item);
                                player.inventory.itemsBox.add(item);
                            }
                            dataArray.clear();

                            //data box lucky round
                            dataArray = (JSONArray) jv.parse(rs.getString("items_box_lucky_round"));
                            for (int i = 0; i < dataArray.size(); i++) {
                                Item item = null;
                                JSONArray dataItem = (JSONArray) jv.parse(dataArray.get(i).toString());
                                short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                                if (tempId != -1) {
                                    item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                                    JSONArray options = (JSONArray) jv.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                                    for (int j = 0; j < options.size(); j++) {
                                        JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                                        item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                                Integer.parseInt(String.valueOf(opt.get(1)))));
                                    }
                                    player.inventory.itemsBoxCrackBall.add(item);
                                }
                            }
                            dataArray.clear();

                            //data friends
                            dataArray = (JSONArray) jv.parse(rs.getString("friends"));
                            if (dataArray != null) {
                                for (int i = 0; i < dataArray.size(); i++) {
                                    JSONArray dataFE = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                                    Friend friend = new Friend();
                                    friend.id = Integer.parseInt(String.valueOf(dataFE.get(0)));
                                    friend.name = String.valueOf(dataFE.get(1));
                                    friend.head = Short.parseShort(String.valueOf(dataFE.get(2)));
                                    friend.body = Short.parseShort(String.valueOf(dataFE.get(3)));
                                    friend.leg = Short.parseShort(String.valueOf(dataFE.get(4)));
                                    friend.bag = Byte.parseByte(String.valueOf(dataFE.get(5)));
                                    friend.power = Long.parseLong(String.valueOf(dataFE.get(6)));
                                    player.friends.add(friend);
                                    dataFE.clear();
                                }
                                dataArray.clear();
                            }

                            //data enemies
                            dataArray = (JSONArray) jv.parse(rs.getString("enemies"));
                            if (dataArray != null) {
                                for (int i = 0; i < dataArray.size(); i++) {
                                    JSONArray dataFE = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                                    Enemy enemy = new Enemy();
                                    enemy.id = Integer.parseInt(String.valueOf(dataFE.get(0)));
                                    enemy.name = String.valueOf(dataFE.get(1));
                                    enemy.head = Short.parseShort(String.valueOf(dataFE.get(2)));
                                    enemy.body = Short.parseShort(String.valueOf(dataFE.get(3)));
                                    enemy.leg = Short.parseShort(String.valueOf(dataFE.get(4)));
                                    enemy.bag = Byte.parseByte(String.valueOf(dataFE.get(5)));
                                    enemy.power = Long.parseLong(String.valueOf(dataFE.get(6)));
                                    player.enemies.add(enemy);
                                    dataFE.clear();
                                }
                                dataArray.clear();
                            }

                            //data nội tại
                            dataArray = (JSONArray) jv.parse(rs.getString("data_intrinsic"));
                            byte intrinsicId = Byte.parseByte(String.valueOf(dataArray.get(0)));
                            player.playerIntrinsic.intrinsic = IntrinsicService.gI().getIntrinsicById(intrinsicId);
                            player.playerIntrinsic.intrinsic.param1 = Short.parseShort(String.valueOf(dataArray.get(1)));
                            player.playerIntrinsic.intrinsic.param2 = Short.parseShort(String.valueOf(dataArray.get(2)));
                            player.playerIntrinsic.countOpen = Byte.parseByte(String.valueOf(dataArray.get(3)));
                            if (player.playerIntrinsic.intrinsic != null) {
                                player.playerIntrinsic.intrinsic.SetMaxValue();
                            }
                            dataArray.clear();

                            //data item time
                            dataArray = (JSONArray) jv.parse(rs.getString("data_item_time"));
                            int timeBoHuyet = Integer.parseInt(String.valueOf(dataArray.get(0)));

                            int timeBoKhi = Integer.parseInt(String.valueOf(dataArray.get(1)));

                            int timeGiapXen = Integer.parseInt(String.valueOf(dataArray.get(2)));
                            int timebkt = Integer.parseInt(String.valueOf(dataArray.get(2)));
                            int timeCuongNo = Integer.parseInt(String.valueOf(dataArray.get(3)));

                            int timeAnDanh = Integer.parseInt(String.valueOf(dataArray.get(4)));

                            int timeOpenPower = Integer.parseInt(String.valueOf(dataArray.get(5)));
                            int timeMayDo = Integer.parseInt(String.valueOf(dataArray.get(6)));
                            int timeMayDo2 = Integer.parseInt(String.valueOf(dataArray.get(6)));

                            int timeMeal = Integer.parseInt(String.valueOf(dataArray.get(7)));
                            int iconMeal = Integer.parseInt(String.valueOf(dataArray.get(8)));

                            int timeUseTDLT = 0;
                            if (dataArray.size() == 10) {
                                timeUseTDLT = Integer.parseInt(String.valueOf(dataArray.get(9)));
                            }

                            player.itemTime.lastTimeBoHuyet = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBoHuyet);
                            player.itemTime.lastTimeBoKhi = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBoKhi);
                            player.itemTime.lastTimeGiapXen = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeGiapXen);
                            player.itemTime.lastTimeCuongNo = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeCuongNo);
                            player.itemTime.lastTimeAnDanh = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeAnDanh);

                            player.itemTime.lastTimeOpenPower = System.currentTimeMillis() - (ItemTime.TIME_OPEN_POWER - timeOpenPower);
                            player.itemTime.lastTimeUseMayDo = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO - timeMayDo);

                            player.itemTime.lastTimeEatMeal = System.currentTimeMillis() - (ItemTime.TIME_EAT_MEAL - timeMeal);
                            player.itemTime.timeTDLT = timeUseTDLT * 60 * 1000;
                            player.itemTime.lastTimeUseTDLT = System.currentTimeMillis();

                            player.itemTime.iconMeal = iconMeal;
                            player.itemTime.isUseBoHuyet = timeBoHuyet != 0;
                            player.itemTime.isUseBoKhi = timeBoKhi != 0;
                            player.itemTime.isUseGiapXen = timeGiapXen != 0;
                            player.itemTime.isbkt = timebkt != 0;
                            player.itemTime.isUseCuongNo = timeCuongNo != 0;
                            player.itemTime.isUseAnDanh = timeAnDanh != 0;
                            player.itemTime.isUseBoHuyet2 = timeBoHuyet != 0;
                            player.itemTime.isUseBoKhi2 = timeBoKhi != 0;
                            player.itemTime.isUseGiapXen2 = timeGiapXen != 0;
                            player.itemTime.isUseCuongNo2 = timeCuongNo != 0;
                            player.itemTime.isUseAnDanh2 = timeAnDanh != 0;
                            player.itemTime.isOpenPower = timeOpenPower != 0;
                            player.itemTime.isUseMayDo = timeMayDo != 0;

                            player.itemTime.isEatMeal = timeMeal != 0;
                            player.itemTime.isUseTDLT = timeUseTDLT != 0;
                            dataArray.clear();

                            //dataa siu cap
                            dataArray = (JSONArray) jv.parse(rs.getString("data_item_time_sieu_cap"));
                            int timeBoHuyetSC = Integer.parseInt(String.valueOf(dataArray.get(0)));
                            int timeBoKhiSC = Integer.parseInt(String.valueOf(dataArray.get(1)));
                            int timeGiapXenSC = Integer.parseInt(String.valueOf(dataArray.get(2)));
                            int timeCuongNoSC = Integer.parseInt(String.valueOf(dataArray.get(3)));
                            int timeAnDanhSC = Integer.parseInt(String.valueOf(dataArray.get(4)));

                            player.itemTime.lastTimeBoHuyet2 = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBoHuyetSC);
                            player.itemTime.lastTimeBoKhi2 = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBoKhiSC);
                            player.itemTime.lastTimeGiapXen2 = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeGiapXenSC);
                            player.itemTime.lastTimeCuongNo2 = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeCuongNoSC);
                            player.itemTime.lastTimeAnDanh2 = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeAnDanhSC);

                            player.itemTime.isUseBoHuyet2 = timeBoHuyetSC != 0;
                            player.itemTime.isUseBoKhi2 = timeBoKhiSC != 0;
                            player.itemTime.isUseGiapXen2 = timeGiapXenSC != 0;
                            player.itemTime.isUseCuongNo2 = timeCuongNoSC != 0;
                            player.itemTime.isUseAnDanh2 = timeAnDanhSC != 0;

                            dataArray.clear();

                            dataArray = (JSONArray) jv.parse(rs.getString("Binh_can_data"));
                            int timex2 = Integer.parseInt(String.valueOf(dataArray.get(0)));
                            int timex3 = Integer.parseInt(String.valueOf(dataArray.get(1)));
                            int timex5 = Integer.parseInt(String.valueOf(dataArray.get(2)));
                            int timex7 = Integer.parseInt(String.valueOf(dataArray.get(3)));

                            player.itemTime.lastX2EXP = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO - timex2);
                            player.itemTime.lastX3EXP = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO - timex3);
                            player.itemTime.lastX5EXP = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO - timex5);
                            player.itemTime.lastX7EXP = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO - timex7);
                            player.itemTime.lastbkt = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO - timex7);
                            player.itemTime.isX2EXP = timex2 != 0;
                            player.itemTime.isX3EXP = timex3 != 0;
                            player.itemTime.isX5EXP = timex5 != 0;
                            player.itemTime.isX7EXP = timex7 != 0;
                            dataArray.clear();

                            dataArray = (JSONArray) jv.parse(rs.getString("Nuoc_mia"));
                            int nuocmiakhonglo = Integer.parseInt(String.valueOf(dataArray.get(0)));
                            int nuocmiathom = Integer.parseInt(String.valueOf(dataArray.get(1)));
                            int nuocmiasaurieng = Integer.parseInt(String.valueOf(dataArray.get(2)));

                            player.itemTime.lastnuocmiakhonglo = System.currentTimeMillis() - (ItemTime.TIME_NUOC_MIA - nuocmiakhonglo);
                            player.itemTime.lastnuocmiathom = System.currentTimeMillis() - (ItemTime.TIME_NUOC_MIA - nuocmiathom);
                            player.itemTime.lastnuocmiasaurieng = System.currentTimeMillis() - (ItemTime.TIME_NUOC_MIA - nuocmiasaurieng);

                            player.itemTime.isnuocmiakhonglo = nuocmiakhonglo != 0;
                            player.itemTime.isnuocmiathom = nuocmiathom != 0;
                            player.itemTime.isnuocmiasaurieng = nuocmiasaurieng != 0;
                            dataArray.clear();

                            // data thanh tuu
//                            for (ArchivementTemplate a : Manager.Archivement_TEMPLATES) {
////                                ThanhTich thanhtich = new ThanhTich();
//                                thanhtich.Template = a;
//                                thanhtich.isFinish = false;
//                                thanhtich.isRecieve = false;
//                                player.Archivement.add(new ThanhTich(thanhtich));
//                            }
//                            ThanhTichPlayer.GetRecieve(player);
                            //data duoi khi
                            dataArray = (JSONArray) jv.parse(rs.getString("data_cai_trang_send"));
                            int timeduoikhi = Integer.parseInt(String.valueOf(dataArray.get(0)));
                            int iconduoikhi = Integer.parseInt(String.valueOf(dataArray.get(1)));
                            player.itemTime.lastTimedkhi = System.currentTimeMillis() - (ItemTime.TIME_DUOI_KHI - timeduoikhi);
                            player.itemTime.isdkhi = timeduoikhi != 0;
                            player.itemTime.icondkhi = iconduoikhi;
                            dataArray.clear();

                            //data nhiệm vụ
                            dataArray = (JSONArray) jv.parse(rs.getString("data_task"));
                            TaskMain taskMain = TaskService.gI().getTaskMainById(player, Byte.parseByte(String.valueOf(dataArray.get(0))));
                            taskMain.index = Byte.parseByte(String.valueOf(dataArray.get(1)));
                            taskMain.subTasks.get(taskMain.index).count = Short.parseShort(String.valueOf(dataArray.get(2)));
                            player.playerTask.taskMain = taskMain;
                            dataArray.clear();

                            //data nhiệm vụ hàng ngày
                            dataArray = (JSONArray) jv.parse(rs.getString("data_side_task"));
                            String format = "dd-MM-yyyy";
                            long receivedTime = Long.parseLong(String.valueOf(dataArray.get(1)));
                            Date date = new Date(receivedTime);
                            if (TimeUtil.formatTime(date, format).equals(TimeUtil.formatTime(new Date(), format))) {
                                player.playerTask.sideTask.template = TaskService.gI().getSideTaskTemplateById(Integer.parseInt(String.valueOf(dataArray.get(0))));
                                player.playerTask.sideTask.count = Integer.parseInt(String.valueOf(dataArray.get(2)));
                                player.playerTask.sideTask.maxCount = Integer.parseInt(String.valueOf(dataArray.get(3)));
                                player.playerTask.sideTask.leftTask = Integer.parseInt(String.valueOf(dataArray.get(4)));
                                player.playerTask.sideTask.level = Integer.parseInt(String.valueOf(dataArray.get(5)));
                                player.playerTask.sideTask.receivedTime = receivedTime;
                            }

                            //data luyện tập off
                            dataArray = (JSONArray) jv.parse(rs.getString("data_offtrain"));
                            player.typetrain = Byte.parseByte(String.valueOf(dataArray.get(0)));
                            player.istrain = Byte.parseByte(String.valueOf(dataArray.get(1))) == 1;
                            dataArray.clear();

                            //data trứng bư
                            dataArray = (JSONArray) jv.parse(rs.getString("data_mabu_egg"));
                            if (dataArray.size() != 0) {
                                player.mabuEgg = new MabuEgg(player, Long.parseLong(String.valueOf(dataArray.get(0))),
                                        Long.parseLong(String.valueOf(dataArray.get(1))));
                            }
                            dataArray.clear();

                            //data trứng bill
                            dataArray = (JSONArray) jv.parse(rs.getString("bill_data"));
                            if (dataArray.size() != 0) {
                                player.billEgg = new BillEgg(player, Long.parseLong(String.valueOf(dataArray.get(0))),
                                        Long.parseLong(String.valueOf(dataArray.get(1))));
                            }
                            dataArray.clear();

                            //data bùa
                            dataArray = (JSONArray) jv.parse(rs.getString("data_charm"));
                            player.charms.tdTriTue = Long.parseLong(String.valueOf(dataArray.get(0)));
                            player.charms.tdManhMe = Long.parseLong(String.valueOf(dataArray.get(1)));
                            player.charms.tdDaTrau = Long.parseLong(String.valueOf(dataArray.get(2)));
                            player.charms.tdOaiHung = Long.parseLong(String.valueOf(dataArray.get(3)));
                            player.charms.tdBatTu = Long.parseLong(String.valueOf(dataArray.get(4)));
                            player.charms.tdDeoDai = Long.parseLong(String.valueOf(dataArray.get(5)));
                            player.charms.tdThuHut = Long.parseLong(String.valueOf(dataArray.get(6)));
                            player.charms.tdDeTu = Long.parseLong(String.valueOf(dataArray.get(7)));
                            player.charms.tdTriTue3 = Long.parseLong(String.valueOf(dataArray.get(8)));
                            player.charms.tdTriTue4 = Long.parseLong(String.valueOf(dataArray.get(9)));
                            dataArray.clear();

                            dataArray = (JSONArray) JSONValue.parse(rs.getString("Thu_TrieuHoi"));
                            player.TrieuHoiCapBac = Integer.parseInt(String.valueOf(dataArray.get(0)));
                            if (player.TrieuHoiCapBac >= 0 && player.TrieuHoiCapBac <= 10) {
                                player.TenThuTrieuHoi = String.valueOf(dataArray.get(1));
                                player.TrieuHoiThucAn = Integer.parseInt(String.valueOf(dataArray.get(2)));
                                player.TrieuHoiDame = Integer.parseInt(String.valueOf(dataArray.get(3)));
                                player.TrieuHoilastTimeThucan = Long.parseLong(String.valueOf(dataArray.get(4)));
                                player.TrieuHoiLevel = Integer.parseInt(String.valueOf(dataArray.get(5)));
                                if (player.TrieuHoiLevel > 100) {
                                    player.TrieuHoiLevel = 100;
                                }
                                player.TrieuHoiExpThanThu = Integer.parseInt(String.valueOf(dataArray.get(6)));
                                player.TrieuHoiHP = Integer.parseInt(String.valueOf(dataArray.get(7)));
                            } else {
                                player.TrieuHoiCapBac = -1;
                            }
                            dataArray.clear();

                            //data skill
                            dataArray = (JSONArray) jv.parse(rs.getString("skills"));
                            for (int i = 0; i < dataArray.size(); i++) {
                                JSONArray dataSkill = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                                int tempId = Integer.parseInt(String.valueOf(dataSkill.get(0)));
                                byte point = Byte.parseByte(String.valueOf(dataSkill.get(1)));
                                Skill skill = null;
                                if (point != 0) {
                                    skill = SkillUtil.createSkill(tempId, point);
                                } else {
                                    skill = SkillUtil.createSkillLevel0(tempId);
                                }
                                if (player.ResetSkill != 0) {
                                    skill.lastTimeUseThisSkill = Long.parseLong(String.valueOf(dataSkill.get(2)));
                                } else {
                                    skill.lastTimeUseThisSkill = 0L;
                                }
                                if (dataSkill.size() > 3) {
                                    skill.currLevel = Short.parseShort(String.valueOf(dataSkill.get(3)));
                                }
                                player.playerSkill.skills.add(skill);
                            }
                            dataArray.clear();
                            player.ResetSkill = 1;
                            //data skill shortcut
                            dataArray = (JSONArray) jv.parse(rs.getString("skills_shortcut"));
                            for (int i = 0; i < dataArray.size(); i++) {
                                player.playerSkill.skillShortCut[i] = Byte.parseByte(String.valueOf(dataArray.get(i)));
                            }
                            for (int i : player.playerSkill.skillShortCut) {
                                if (player.playerSkill.getSkillbyId(i) != null && player.playerSkill.getSkillbyId(i).damage > 0) {
                                    player.playerSkill.skillSelect = player.playerSkill.getSkillbyId(i);
                                    break;
                                }
                            }
                            if (player.playerSkill.skillSelect == null) {
                                player.playerSkill.skillSelect = player.playerSkill.getSkillbyId(player.gender == ConstPlayer.TRAI_DAT
                                        ? Skill.DRAGON : (player.gender == ConstPlayer.NAMEC ? Skill.DEMON : Skill.GALICK));
                            }
                            dataArray.clear();

                            //data pet
                            JSONArray petData = (JSONArray) jv.parse(rs.getString("pet"));
                            if (!petData.isEmpty()) {
                                dataArray = (JSONArray) jv.parse(String.valueOf(petData.get(0)));
                                Pet pet = new Pet(player);
                                pet.id = -player.id;
                                pet.typePet = Byte.parseByte(String.valueOf(dataArray.get(0)));
                                pet.gender = Byte.parseByte(String.valueOf(dataArray.get(1)));
                                pet.name = String.valueOf(dataArray.get(2));
                                player.fusion.typeFusion = Byte.parseByte(String.valueOf(dataArray.get(3)));
                                player.fusion.lastTimeFusion = System.currentTimeMillis()
                                        - (Fusion.TIME_FUSION - Integer.parseInt(String.valueOf(dataArray.get(4))));
                                pet.status = Byte.parseByte(String.valueOf(dataArray.get(5)));
                                try {

                                } catch (Exception e) {

                                }

                                //data chỉ số
                                dataArray = (JSONArray) jv.parse(String.valueOf(petData.get(1)));
                                pet.nPoint.limitPower = Byte.parseByte(String.valueOf(dataArray.get(0)));
                                pet.nPoint.power = Long.parseLong(String.valueOf(dataArray.get(1)));
                                pet.nPoint.tiemNang = Long.parseLong(String.valueOf(dataArray.get(2)));
                                pet.nPoint.stamina = Short.parseShort(String.valueOf(dataArray.get(3)));
                                pet.nPoint.maxStamina = Short.parseShort(String.valueOf(dataArray.get(4)));
                                pet.nPoint.hpg = Integer.parseInt(String.valueOf(dataArray.get(5)));
                                pet.nPoint.mpg = Integer.parseInt(String.valueOf(dataArray.get(6)));
                                pet.nPoint.dameg = Integer.parseInt(String.valueOf(dataArray.get(7)));
                                pet.nPoint.defg = Integer.parseInt(String.valueOf(dataArray.get(8)));
                                pet.nPoint.critg = Integer.parseInt(String.valueOf(dataArray.get(9)));
                                int hp = Integer.parseInt(String.valueOf(dataArray.get(10)));
                                int mp = Integer.parseInt(String.valueOf(dataArray.get(11)));

                                //data body
                                dataArray = (JSONArray) jv.parse(String.valueOf(petData.get(2)));
                                for (int i = 0; i < dataArray.size(); i++) {
                                    Item item = null;
                                    JSONArray dataItem = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                                    boolean flag = false;
                                    if (tempId != -1) {
                                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                                        JSONArray options = (JSONArray) jv.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                                        for (int j = 0; j < options.size(); j++) {
                                            JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                                            if (tempId != 884) {
                                                item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                                        Integer.parseInt(String.valueOf(opt.get(1)))));
                                            } else {
                                                item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))) == 14 ? 5 : Integer.parseInt(String.valueOf(opt.get(0))),
                                                        Integer.parseInt(String.valueOf(opt.get(1)))));
                                            }
                                            if (Integer.parseInt(String.valueOf(opt.get(0))) == 50 && tempId == 884) {
                                                flag = true;
                                            }
                                        }
                                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                                        if (ItemService.gI().isOutOfDateTime(item)) {
                                            item = ItemService.gI().createItemNull();
                                        }
                                    } else {
                                        item = ItemService.gI().createItemNull();
                                    }
                                    if (tempId == 884 && flag) {
                                        List<Item.ItemOption> itemsToRemove = new ArrayList<>();
                                        for (Item.ItemOption op : item.itemOptions) {
                                            if (op.optionTemplate.id == 50 || op.optionTemplate.id == 102) {
                                                itemsToRemove.add(op);
                                            }
                                        }
                                        item.itemOptions.removeAll(itemsToRemove);
                                    }
                                    com.KhanhDTK.models.Event.ResetParamItem.SetBasicChiSo(item);
                                    pet.inventory.itemsBody.add(item);
                                }

                                //data skills
                                dataArray = (JSONArray) jv.parse(String.valueOf(petData.get(3)));
                                for (int i = 0; i < dataArray.size(); i++) {
                                    JSONArray skillTemp = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                                    int tempId = Integer.parseInt(String.valueOf(skillTemp.get(0)));
                                    byte point = Byte.parseByte(String.valueOf(skillTemp.get(1)));
                                    Skill skill = null;
                                    if (point != 0) {
                                        skill = SkillUtil.createSkill(tempId, point);
                                    } else {
                                        skill = SkillUtil.createSkillLevel0(tempId);
                                    }
                                    switch (skill.template.id) {
                                        case Skill.KAMEJOKO:
                                        case Skill.MASENKO:
                                        case Skill.ANTOMIC:
                                            skill.coolDown = 1000;
                                            break;
                                    }
                                    pet.playerSkill.skills.add(skill);
                                }
                                if (pet.playerSkill.skills.size() < 5) {
                                    pet.playerSkill.skills.add(4, SkillUtil.createSkillLevel0(-1));
                                }
                                pet.nPoint.hp = hp;
                                pet.nPoint.mp = mp;
                                player.pet = pet;
                            }
                            SetPlayer(player);
                            SetPlayer(player.pet);
                            player.nPoint.hp = plHp;
                            player.nPoint.mp = plMp;
                            player.iDMark.setLoadedAllDataPlayer(true);
                            GirlkunDB.executeUpdate("update account set last_time_login = '" + new Timestamp(System.currentTimeMillis()) + "', ip_address = '" + session.ipAddress + "' where id = " + session.userId);
                        }
                    }
                }
                al.reset();
            } else {
                Service.gI().sendThongBaoOK(session, "Thông tin tài khoản hoặc mật khẩu không chính xác");
                al.wrong();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error(session.uu);
            player.dispose();
            player = null;
            Logger.logException(GodGK.class, e);
        } finally {
            if (rs != null) {
                rs.dispose();
            }
        }
        return player;
    }

    public static void checkDo() {
        long st = System.currentTimeMillis();
        JSONValue jv = new JSONValue();
        JSONArray dataArray = null;
        JSONObject dataObject = null;
        Player player;
        PreparedStatement ps = null;
        String name = "";
        ResultSet rs = null;
        try (Connection con = GirlkunDB.getConnection()) {
            ps = con.prepareStatement("select * from player");
            rs = ps.executeQuery();
            while (rs.next()) {
                int plHp = 200000000;
                int plMp = 200000000;
                player = new Player();
                player.id = rs.getInt("id");
                player.name = rs.getString("name");
                name = rs.getString("name");
                player.head = rs.getShort("head");
                player.gender = rs.getByte("gender");
                player.haveTennisSpaceShip = rs.getBoolean("have_tennis_space_ship");
                //data kim lượng
                dataArray = (JSONArray) JSONValue.parse(rs.getString("data_inventory"));
                player.inventory.gold = Long.parseLong(String.valueOf(dataArray.get(0)));
                player.inventory.gem = Integer.parseInt(String.valueOf(dataArray.get(1)));
                player.inventory.ruby = Integer.parseInt(String.valueOf(dataArray.get(2)));
                if (dataArray.size() == 4) {
                    player.inventory.coupon = Integer.parseInt(String.valueOf(dataArray.get(3)));
                } else {
                    player.inventory.coupon = 0;
                }
                dataArray.clear();

                //data chỉ số
                dataArray = (JSONArray) JSONValue.parse(rs.getString("data_point"));
                player.nPoint.limitPower = Byte.parseByte(String.valueOf(dataArray.get(0)));
                player.nPoint.power = Long.parseLong(String.valueOf(dataArray.get(1)));
                player.nPoint.tiemNang = Long.parseLong(String.valueOf(dataArray.get(2)));
                player.nPoint.stamina = Short.parseShort(String.valueOf(dataArray.get(3)));
                player.nPoint.maxStamina = Short.parseShort(String.valueOf(dataArray.get(4)));
                player.nPoint.hpg = Integer.parseInt(String.valueOf(dataArray.get(5)));
                player.nPoint.mpg =Integer.parseInt(String.valueOf(dataArray.get(6)));
                player.nPoint.dameg =Integer.parseInt(String.valueOf(dataArray.get(7)));
                player.nPoint.defg = Integer.parseInt(String.valueOf(dataArray.get(8)));
                player.nPoint.critg = Byte.parseByte(String.valueOf(dataArray.get(9)));
                dataArray.get(10); //** Năng động
                plHp = Integer.parseInt(String.valueOf(dataArray.get(11)));
                plMp = Integer.parseInt(String.valueOf(dataArray.get(12)));
                dataArray.clear();

                //data body
                dataArray = (JSONArray) JSONValue.parse(rs.getString("items_body"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));

                        }
                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    Util.useCheckDo(player, item, "body");
                    player.inventory.itemsBody.add(item);
                }
                dataArray.clear();

                //data bag
                dataArray = (JSONArray) JSONValue.parse(rs.getString("items_bag"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    Util.useCheckDo(player, item, "bag");
                    player.inventory.itemsBag.add(item);

                }
                dataArray.clear();

                //data box
                dataArray = (JSONArray) JSONValue.parse(rs.getString("items_box"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    Util.useCheckDo(player, item, "box");
                    player.inventory.itemsBox.add(item);
                }
                dataArray.clear();

                //data box lucky round
                dataArray = (JSONArray) JSONValue.parse(rs.getString("items_box_lucky_round"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        player.inventory.itemsBoxCrackBall.add(item);
                    }
                }
                dataArray.clear();

                //data luyện tập off
                dataArray = (JSONArray) jv.parse(rs.getString("data_offtrain"));
                player.typetrain = Byte.parseByte(String.valueOf(dataArray.get(0)));
                player.istrain = Byte.parseByte(String.valueOf(dataArray.get(1))) == 1;
                dataArray.clear();

                //data pet
                JSONArray petData = (JSONArray) jv.parse(rs.getString("pet"));
                if (!petData.isEmpty()) {
                    dataArray = (JSONArray) jv.parse(String.valueOf(petData.get(0)));
                    Pet pet = new Pet(player);
                    pet.id = -player.id;
                    pet.typePet = Byte.parseByte(String.valueOf(dataArray.get(0)));
                    pet.gender = Byte.parseByte(String.valueOf(dataArray.get(1)));
                    pet.name = String.valueOf(dataArray.get(2));
                    player.fusion.typeFusion = Byte.parseByte(String.valueOf(dataArray.get(3)));
                    player.fusion.lastTimeFusion = System.currentTimeMillis()
                            - (Fusion.TIME_FUSION - Integer.parseInt(String.valueOf(dataArray.get(4))));
                    pet.status = Byte.parseByte(String.valueOf(dataArray.get(5)));

                    //data chỉ số
                    dataArray = (JSONArray) jv.parse(String.valueOf(petData.get(1)));
                    pet.nPoint.limitPower = Byte.parseByte(String.valueOf(dataArray.get(0)));
                    pet.nPoint.power = Long.parseLong(String.valueOf(dataArray.get(1)));
                    pet.nPoint.tiemNang = Long.parseLong(String.valueOf(dataArray.get(2)));
                    pet.nPoint.stamina = Short.parseShort(String.valueOf(dataArray.get(3)));
                    pet.nPoint.maxStamina = Short.parseShort(String.valueOf(dataArray.get(4)));
                    pet.nPoint.hpg = Integer.parseInt(String.valueOf(dataArray.get(5)));
                    pet.nPoint.mpg = Integer.parseInt(String.valueOf(dataArray.get(6)));
                    pet.nPoint.dameg = Integer.parseInt(String.valueOf(dataArray.get(7)));
                    pet.nPoint.defg = Integer.parseInt(String.valueOf(dataArray.get(8)));
                    pet.nPoint.critg = Integer.parseInt(String.valueOf(dataArray.get(9)));
                    long hp = Long.parseLong(String.valueOf(dataArray.get(10)));
                    long mp = Long.parseLong(String.valueOf(dataArray.get(11)));

                    //data body
                    dataArray = (JSONArray) jv.parse(String.valueOf(petData.get(2)));
                    for (int i = 0; i < dataArray.size(); i++) {
                        Item item = null;
                        JSONArray dataItem = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                        short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                        if (tempId != -1) {
                            item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                            JSONArray options = (JSONArray) jv.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                            for (int j = 0; j < options.size(); j++) {
                                JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                                item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                        Integer.parseInt(String.valueOf(opt.get(1)))));
                            }
                            item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                            if (ItemService.gI().isOutOfDateTime(item)) {
                                item = ItemService.gI().createItemNull();
                            }
                        } else {
                            item = ItemService.gI().createItemNull();
                        }
                        Util.useCheckDo(player, item, "pet");
                        pet.inventory.itemsBody.add(item);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(name);

            Logger.logException(Manager.class, e, "Lỗi load database");
            System.exit(0);
        }
    }

    public static void checkVang(int x) {
        int thoi_vang = 0;
        long st = System.currentTimeMillis();
        JSONValue jv = new JSONValue();
        JSONArray dataArray = null;
        JSONObject dataObject = null;
        Player player;
        PreparedStatement ps = null;
        String name = "";
        ResultSet rs = null;
        try (Connection con = GirlkunDB.getConnection()) {
            ps = con.prepareStatement("select * from player");
            rs = ps.executeQuery();
            while (rs.next()) {
                int plHp = 200000000;
                int plMp = 200000000;
                player = new Player();
                player.id = rs.getInt("id");
                player.name = rs.getString("name");
                name = rs.getString("name");
                player.head = rs.getShort("head");
                player.gender = rs.getByte("gender");
                player.haveTennisSpaceShip = rs.getBoolean("have_tennis_space_ship");
                //data kim lượng
                dataArray = (JSONArray) JSONValue.parse(rs.getString("data_inventory"));
                player.inventory.gold = Integer.parseInt(String.valueOf(dataArray.get(0)));
                player.inventory.gem = Integer.parseInt(String.valueOf(dataArray.get(1)));
                player.inventory.ruby = Integer.parseInt(String.valueOf(dataArray.get(2)));
                if (dataArray.size() == 4) {
                    player.inventory.coupon = Integer.parseInt(String.valueOf(dataArray.get(3)));
                } else {
                    player.inventory.coupon = 0;
                }
                dataArray.clear();

                //data luyện tập off
                dataArray = (JSONArray) jv.parse(rs.getString("data_offtrain"));
                player.typetrain = Byte.parseByte(String.valueOf(dataArray.get(0)));
                player.istrain = Byte.parseByte(String.valueOf(dataArray.get(1))) == 1;
                dataArray.clear();

                //data chỉ số
                dataArray = (JSONArray) JSONValue.parse(rs.getString("data_point"));
                player.nPoint.limitPower = Byte.parseByte(String.valueOf(dataArray.get(0)));
                player.nPoint.power = Long.parseLong(String.valueOf(dataArray.get(1)));
                player.nPoint.tiemNang = Long.parseLong(String.valueOf(dataArray.get(2)));
                player.nPoint.stamina = Short.parseShort(String.valueOf(dataArray.get(3)));
                player.nPoint.maxStamina = Short.parseShort(String.valueOf(dataArray.get(4)));
                player.nPoint.hpg = Integer.parseInt(String.valueOf(dataArray.get(5)));
                player.nPoint.mpg = Integer.parseInt(String.valueOf(dataArray.get(6)));
                player.nPoint.dameg = Integer.parseInt(String.valueOf(dataArray.get(7)));
                player.nPoint.defg = Integer.parseInt(String.valueOf(dataArray.get(8)));
                player.nPoint.critg = Byte.parseByte(String.valueOf(dataArray.get(9)));
                dataArray.get(10); //** Năng động
                plHp = Integer.parseInt(String.valueOf(dataArray.get(11)));
                plMp = Integer.parseInt(String.valueOf(dataArray.get(12)));
                dataArray.clear();

                //data body
                dataArray = (JSONArray) JSONValue.parse(rs.getString("items_body"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));

                        }
                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    Util.useCheckDo(player, item, "body");
                    player.inventory.itemsBody.add(item);
                    if (item.template.id == 457) {
                        thoi_vang += item.quantity;
                    }
                }
                dataArray.clear();

                //data bag
                dataArray = (JSONArray) JSONValue.parse(rs.getString("items_bag"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    Util.useCheckDo(player, item, "bag");
                    if (item.template.id == 457) {
                        thoi_vang += item.quantity;
                    }
                    player.inventory.itemsBag.add(item);
                }
                dataArray.clear();

                //data box
                dataArray = (JSONArray) JSONValue.parse(rs.getString("items_box"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    Util.useCheckDo(player, item, "box");
                    if (item.template.id == 457) {
                        thoi_vang += item.quantity;
                    }
                    player.inventory.itemsBox.add(item);
                }
                dataArray.clear();
                if (thoi_vang > x) {
                    Logger.error("play:" + player.name);
                    Logger.error("thoi_vang:" + thoi_vang);
                }

            }

        } catch (Exception e) {
            System.out.println(name);

            Logger.logException(Manager.class, e, "Lỗi load database");
        }
    }

    public static void SetPlayer(Player pl) {
//        if(pl == null)
//        {
//            return;
//        }
//        if(!pl.getSession().isAdmin)
//        {  if(pl.nPoint.limitPower > 11)
//        {
//            pl.nPoint.limitPower = 11;
//        }
//        if(pl.nPoint.power > 130000000000L)
//        {
//           pl.nPoint.power = 130000000000L;
//        }
//        if(pl.nPoint.dameg > 27500)
//        {
//           pl.nPoint.power = 27500;
//        }
//        if(pl.nPoint.hpg > 630000)
//        {
//           pl.nPoint.hpg = 630000;
//        }
//        if(pl.nPoint.mpg > 630000)
//        {
//           pl.nPoint.mpg = 630000;
//        }}
    }

    public static Player loadById(int id) {
        Player player = null;
        GirlkunResultSet rs = null;
        if (Client.gI().getPlayer(id) != null) {
            player = Client.gI().getPlayer(id);
            return player;
        }
        try {
            rs = GirlkunDB.executeQuery("select * from player where id = ? limit 1", id);
            if (rs.first()) {
                int plHp = 200000000;
                int plMp = 200000000;
                JSONValue jv = new JSONValue();
                JSONArray dataArray = null;

                player = new Player();

                //base info
                player.id = rs.getInt("id");
                player.name = rs.getString("name");
                player.head = rs.getShort("head");
                player.gender = rs.getByte("gender");
                player.Captutien = rs.getInt("Captutien");
                if (player.Captutien > 5000) {
                    player.Captutien = 5000;
                }
                player.Exptutien = rs.getLong("Exptutien");
                if (player.Captutien > 10000) {
                    player.Captutien = 10000;
                }
                player.haveTennisSpaceShip = rs.getBoolean("have_tennis_space_ship");

                int clanId = rs.getInt("clan_id_sv" + Manager.SERVER);
                if (clanId != -1) {
                    Clan clan = ClanService.gI().getClanById(clanId);
                    for (ClanMember cm : clan.getMembers()) {
                        if (cm.id == player.id) {
                            clan.addMemberOnline(player);
                            player.clan = clan;
                            player.clanMember = cm;
                            break;
                        }
                    }
                }

                //data kim lượng
                dataArray = (JSONArray) jv.parse(rs.getString("data_inventory"));
                player.inventory.gold = Long.parseLong(String.valueOf(dataArray.get(0)));
                player.inventory.gem = Integer.parseInt(String.valueOf(dataArray.get(1)));
                player.inventory.ruby = Integer.parseInt(String.valueOf(dataArray.get(2)));
                player.inventory.tien = Integer.parseInt(String.valueOf(dataArray.get(3)));
                dataArray.clear();

                dataArray = (JSONArray) JSONValue.parse(rs.getString("Bkttutien"));
                player.Bkttutien[0] = Integer.parseInt(String.valueOf(dataArray.get(0)));
                player.Bkttutien[1] = Integer.parseInt(String.valueOf(dataArray.get(1)));
                if (player.Bkttutien[1] > 96) {
                    player.Bkttutien[1] = 0;
                }
                player.Bkttutien[2] = Integer.parseInt(String.valueOf(dataArray.get(2)));
                if (player.Bkttutien[2] > 50) {
                    player.Bkttutien[2] = 0;
                }
                dataArray.clear();

                dataArray = (JSONArray) JSONValue.parse(rs.getString("Bkt_Tu_Ma"));
                if (!dataArray.isEmpty()) {
                    player.Bkt_Tu_Ma = Integer.parseInt(String.valueOf(dataArray.get(0)));
                    player.Bkt_Exp_Tu_Ma = Integer.parseInt(String.valueOf(dataArray.get(1)));
                    player.Bkt_Ma_Hoa = Integer.parseInt(String.valueOf(dataArray.get(2)));
                    player.BktLasttimeMaHoa = Integer.parseInt(String.valueOf(dataArray.get(3)));
                    player.Bkt_Ma_cot = Integer.parseInt(String.valueOf(dataArray.get(4)));
                }
                // data kim lượng
                dataArray.clear();

                dataArray = (JSONArray) JSONValue.parse(rs.getString("BktDLDL"));
                player.BktDauLaDaiLuc[0] =Integer.parseInt(String.valueOf(dataArray.get(0)));
                player.BktDauLaDaiLuc[1] = Integer.parseInt(String.valueOf(dataArray.get(1)));
                player.BktDauLaDaiLuc[2] = Integer.parseInt(String.valueOf(dataArray.get(2)));
                player.BktDauLaDaiLuc[3] = Integer.parseInt(String.valueOf(dataArray.get(3)));
                player.BktDauLaDaiLuc[4] = Integer.parseInt(String.valueOf(dataArray.get(4)));
                player.BktDauLaDaiLuc[5] =Integer.parseInt(String.valueOf(dataArray.get(5)));
                player.BktDauLaDaiLuc[6] = Integer.parseInt(String.valueOf(dataArray.get(6)));
                player.BktDauLaDaiLuc[7] = Integer.parseInt(String.valueOf(dataArray.get(7)));
                player.BktDauLaDaiLuc[8] = Integer.parseInt(String.valueOf(dataArray.get(8)));
                player.BktDauLaDaiLuc[9] = Integer.parseInt(String.valueOf(dataArray.get(9)));
                player.BktDauLaDaiLuc[10] = Integer.parseInt(String.valueOf(dataArray.get(10)));
                player.BktDauLaDaiLuc[11] = Integer.parseInt(String.valueOf(dataArray.get(11)));
                player.BktDauLaDaiLuc[12] = Integer.parseInt(String.valueOf(dataArray.get(12)));
                player.BktDauLaDaiLuc[13] = Integer.parseInt(String.valueOf(dataArray.get(13)));
                player.BktDauLaDaiLuc[14] = Integer.parseInt(String.valueOf(dataArray.get(14)));
                player.BktDauLaDaiLuc[15] = Integer.parseInt(String.valueOf(dataArray.get(15)));
                player.BktDauLaDaiLuc[16] = Integer.parseInt(String.valueOf(dataArray.get(16)));
                player.BktDauLaDaiLuc[17] = Integer.parseInt(String.valueOf(dataArray.get(17)));
                player.BktDauLaDaiLuc[18] = Integer.parseInt(String.valueOf(dataArray.get(18)));
                player.BktDauLaDaiLuc[19] = Integer.parseInt(String.valueOf(dataArray.get(19)));
                player.BktDauLaDaiLuc[20] = Integer.parseInt(String.valueOf(dataArray.get(20)));
                dataArray.clear();

                dataArray = (JSONArray) JSONValue.parse(rs.getString("Bkt_Tu_Ma"));
                if (!dataArray.isEmpty()) {
                    player.Bkt_Tu_Ma = Integer.parseInt(String.valueOf(dataArray.get(0)));
                    player.Bkt_Exp_Tu_Ma = Integer.parseInt(String.valueOf(dataArray.get(1)));
                    player.Bkt_Ma_Hoa = Integer.parseInt(String.valueOf(dataArray.get(2)));
                    player.BktLasttimeMaHoa = Integer.parseInt(String.valueOf(dataArray.get(3)));
                    player.Bkt_Ma_cot = Integer.parseInt(String.valueOf(dataArray.get(4)));
                }

                //data tọa độ
                try {
                    dataArray = (JSONArray) jv.parse(rs.getString("data_location"));
                    int mapId = Integer.parseInt(String.valueOf(dataArray.get(0)));
                    player.location.x = Integer.parseInt(String.valueOf(dataArray.get(1)));
                    player.location.y = Integer.parseInt(String.valueOf(dataArray.get(2)));
                    if (MapService.gI().isMapDoanhTrai(mapId) || MapService.gI().isMapBlackBallWar(mapId)
                            || MapService.gI().isMapBanDoKhoBau(mapId)
                            || MapService.gI().isMapKhiGas(mapId)) {
                        mapId = player.gender + 21;
                        player.location.x = 300;
                        player.location.y = 336;
                    }
                    player.zone = MapService.gI().getMapCanJoin(player, mapId, -1);
                } catch (Exception e) {

                }
                dataArray.clear();

                //data luyện tập off
                dataArray = (JSONArray) jv.parse(rs.getString("data_offtrain"));
                player.typetrain = Byte.parseByte(String.valueOf(dataArray.get(0)));
                player.istrain = Byte.parseByte(String.valueOf(dataArray.get(1))) == 1;
                dataArray.clear();

                //data chỉ số
                dataArray = (JSONArray) jv.parse(rs.getString("data_point"));
                player.nPoint.limitPower = Byte.parseByte(String.valueOf(dataArray.get(0)));
                player.nPoint.power = Long.parseLong(String.valueOf(dataArray.get(1)));
                player.nPoint.tiemNang = Long.parseLong(String.valueOf(dataArray.get(2)));
                player.nPoint.stamina = Short.parseShort(String.valueOf(dataArray.get(3)));
                player.nPoint.maxStamina = Short.parseShort(String.valueOf(dataArray.get(4)));
                player.nPoint.hpg = Integer.parseInt(String.valueOf(dataArray.get(5)));
                player.nPoint.mpg = Integer.parseInt(String.valueOf(dataArray.get(6)));
                player.nPoint.dameg = Integer.parseInt(String.valueOf(dataArray.get(7)));
                player.nPoint.defg = Integer.parseInt(String.valueOf(dataArray.get(8)));
                player.nPoint.critg = Byte.parseByte(String.valueOf(dataArray.get(9)));
                dataArray.get(10); //** Năng động
                plHp = Integer.parseInt(String.valueOf(dataArray.get(11)));
                plMp = Integer.parseInt(String.valueOf(dataArray.get(12)));
                dataArray.clear();

                //data đậu thần
                dataArray = (JSONArray) jv.parse(rs.getString("data_magic_tree"));
                byte level = Byte.parseByte(String.valueOf(dataArray.get(0)));
                byte currPea = Byte.parseByte(String.valueOf(dataArray.get(1)));
                boolean isUpgrade = Byte.parseByte(String.valueOf(dataArray.get(2))) == 1;
                long lastTimeHarvest = Long.parseLong(String.valueOf(dataArray.get(3)));
                long lastTimeUpgrade = Long.parseLong(String.valueOf(dataArray.get(4)));
                player.magicTree = new MagicTree(player, level, currPea, lastTimeHarvest, isUpgrade, lastTimeUpgrade);
                dataArray.clear();

                //data phần thưởng sao đen
                dataArray = (JSONArray) jv.parse(rs.getString("data_black_ball"));
                JSONArray dataBlackBall = null;
                for (int i = 0; i < dataArray.size(); i++) {
                    dataBlackBall = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                    player.rewardBlackBall.timeOutOfDateReward[i] = Long.parseLong(String.valueOf(dataBlackBall.get(0)));
                    player.rewardBlackBall.lastTimeGetReward[i] = Long.parseLong(String.valueOf(dataBlackBall.get(1)));
                    try {
                        player.rewardBlackBall.quantilyBlackBall[i] = dataBlackBall.get(2) != null ? Integer.parseInt(String.valueOf(dataBlackBall.get(2))) : 0;
                    } catch (Exception e) {
                        player.rewardBlackBall.quantilyBlackBall[i] = player.rewardBlackBall.timeOutOfDateReward[i] != 0 ? 1 : 0;

                    }
                    dataBlackBall.clear();
                }
                dataArray.clear();
                //data body
                dataArray = (JSONArray) jv.parse(rs.getString("items_body"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) jv.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) jv.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    player.inventory.itemsBody.add(item);
                }
                dataArray.clear();

                //data bag
                dataArray = (JSONArray) jv.parse(rs.getString("items_bag"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) jv.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) jv.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    player.inventory.itemsBag.add(item);
                }
                dataArray.clear();

                //data box
                dataArray = (JSONArray) jv.parse(rs.getString("items_box"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) jv.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) jv.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    player.inventory.itemsBox.add(item);
                }
                dataArray.clear();

                //data box lucky round
                dataArray = (JSONArray) jv.parse(rs.getString("items_box_lucky_round"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) jv.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) jv.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        player.inventory.itemsBoxCrackBall.add(item);
                    }
                }
                dataArray.clear();

                //data friends
                dataArray = (JSONArray) jv.parse(rs.getString("friends"));
                if (dataArray != null) {
                    for (int i = 0; i < dataArray.size(); i++) {
                        JSONArray dataFE = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                        Friend friend = new Friend();
                        friend.id = Integer.parseInt(String.valueOf(dataFE.get(0)));
                        friend.name = String.valueOf(dataFE.get(1));
                        friend.head = Short.parseShort(String.valueOf(dataFE.get(2)));
                        friend.body = Short.parseShort(String.valueOf(dataFE.get(3)));
                        friend.leg = Short.parseShort(String.valueOf(dataFE.get(4)));
                        friend.bag = Byte.parseByte(String.valueOf(dataFE.get(5)));
                        friend.power = Long.parseLong(String.valueOf(dataFE.get(6)));
                        player.friends.add(friend);
                        dataFE.clear();
                    }
                    dataArray.clear();
                }

                //data enemies
                dataArray = (JSONArray) jv.parse(rs.getString("enemies"));
                if (dataArray != null) {
                    for (int i = 0; i < dataArray.size(); i++) {
                        JSONArray dataFE = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                        Enemy enemy = new Enemy();
                        enemy.id = Integer.parseInt(String.valueOf(dataFE.get(0)));
                        enemy.name = String.valueOf(dataFE.get(1));
                        enemy.head = Short.parseShort(String.valueOf(dataFE.get(2)));
                        enemy.body = Short.parseShort(String.valueOf(dataFE.get(3)));
                        enemy.leg = Short.parseShort(String.valueOf(dataFE.get(4)));
                        enemy.bag = Byte.parseByte(String.valueOf(dataFE.get(5)));
                        enemy.power = Long.parseLong(String.valueOf(dataFE.get(6)));
                        player.enemies.add(enemy);
                        dataFE.clear();
                    }
                    dataArray.clear();
                }

                //data nội tại
                dataArray = (JSONArray) jv.parse(rs.getString("data_intrinsic"));
                byte intrinsicId = Byte.parseByte(String.valueOf(dataArray.get(0)));
                player.playerIntrinsic.intrinsic = IntrinsicService.gI().getIntrinsicById(intrinsicId);
                player.playerIntrinsic.intrinsic.param1 = Short.parseShort(String.valueOf(dataArray.get(1)));
                player.playerIntrinsic.intrinsic.param2 = Short.parseShort(String.valueOf(dataArray.get(2)));
                player.playerIntrinsic.countOpen = Byte.parseByte(String.valueOf(dataArray.get(3)));
                dataArray.clear();

                //data item time
                dataArray = (JSONArray) jv.parse(rs.getString("data_item_time"));
                int timeBoHuyet = Integer.parseInt(String.valueOf(dataArray.get(0)));
                int timeBoHuyet2 = Integer.parseInt(String.valueOf(dataArray.get(0)));
                int timeBoKhi = Integer.parseInt(String.valueOf(dataArray.get(1)));
                int timeBoKhi2 = Integer.parseInt(String.valueOf(dataArray.get(1)));
                int timeGiapXen = Integer.parseInt(String.valueOf(dataArray.get(2)));
                int timebkt = Integer.parseInt(String.valueOf(dataArray.get(2)));
                int timeGiapXen2 = Integer.parseInt(String.valueOf(dataArray.get(2)));
                int timeCuongNo = Integer.parseInt(String.valueOf(dataArray.get(3)));
                int timeCuongNo2 = Integer.parseInt(String.valueOf(dataArray.get(3)));
                int timeAnDanh = Integer.parseInt(String.valueOf(dataArray.get(4)));
                int timeAnDanh2 = Integer.parseInt(String.valueOf(dataArray.get(4)));
                int timeOpenPower = Integer.parseInt(String.valueOf(dataArray.get(5)));
                int timeMayDo = Integer.parseInt(String.valueOf(dataArray.get(6)));
                int timeMayDo2 = Integer.parseInt(String.valueOf(dataArray.get(6)));
                int timeMeal = Integer.parseInt(String.valueOf(dataArray.get(7)));
                int iconMeal = Integer.parseInt(String.valueOf(dataArray.get(8)));

                player.itemTime.lastTimeBoHuyet = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBoHuyet);
                player.itemTime.lastTimeBoKhi = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBoKhi);
                player.itemTime.lastTimeGiapXen = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeGiapXen);
                player.itemTime.lastTimeCuongNo = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeCuongNo);
                player.itemTime.lastTimeAnDanh = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeAnDanh);
                player.itemTime.lastTimeOpenPower = System.currentTimeMillis() - (ItemTime.TIME_OPEN_POWER - timeOpenPower);
                player.itemTime.lastTimeUseMayDo = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO - timeMayDo);
                player.itemTime.lastTimeUseMayDo2 = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO2 - timeMayDo2);
                player.itemTime.lastTimeEatMeal = System.currentTimeMillis() - (ItemTime.TIME_EAT_MEAL - timeMeal);
                player.itemTime.iconMeal = iconMeal;
                player.itemTime.isUseBoHuyet = timeBoHuyet != 0;
                player.itemTime.isUseBoKhi = timeBoKhi != 0;
                player.itemTime.isUseGiapXen = timeGiapXen != 0;
                player.itemTime.isUseCuongNo = timeCuongNo != 0;
                player.itemTime.isUseAnDanh = timeAnDanh != 0;
                player.itemTime.isOpenPower = timeOpenPower != 0;
                player.itemTime.isUseMayDo = timeMayDo != 0;
                player.itemTime.isUseMayDo2 = timeMayDo2 != 0;
                player.itemTime.isEatMeal = timeMeal != 0;
                dataArray.clear();

                dataArray = (JSONArray) jv.parse(rs.getString("Binh_can_data"));
                int timex2 = Integer.parseInt(String.valueOf(dataArray.get(0)));
                int timex3 = Integer.parseInt(String.valueOf(dataArray.get(1)));
                int timex5 = Integer.parseInt(String.valueOf(dataArray.get(2)));
                int timex7 = Integer.parseInt(String.valueOf(dataArray.get(3)));

                player.itemTime.lastX2EXP = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO - timex2);
                player.itemTime.lastX3EXP = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO - timex3);
                player.itemTime.lastX5EXP = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO - timex5);
                player.itemTime.lastbkt = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO - timex7);
                player.itemTime.isX2EXP = timex2 != 0;
                player.itemTime.isX3EXP = timex3 != 0;
                player.itemTime.isX5EXP = timex5 != 0;
                player.itemTime.isX7EXP = timex7 != 0;
                player.itemTime.isbkt = timex7 != 0;
                dataArray.clear();

                //data duoi khi
                dataArray = (JSONArray) jv.parse(rs.getString("data_cai_trang_send"));
                int timeduoikhi = Integer.parseInt(String.valueOf(dataArray.get(0)));
                int iconduoikhi = Integer.parseInt(String.valueOf(dataArray.get(1)));
                player.itemTime.lastTimedkhi = System.currentTimeMillis() - (ItemTime.TIME_DUOI_KHI - timeduoikhi);
                player.itemTime.isdkhi = timeduoikhi != 0;
                player.itemTime.icondkhi = iconduoikhi;
                dataArray.clear();

                //data nhiệm vụ
                dataArray = (JSONArray) jv.parse(rs.getString("data_task"));
                TaskMain taskMain = TaskService.gI().getTaskMainById(player, Byte.parseByte(String.valueOf(dataArray.get(0))));
                taskMain.index = Byte.parseByte(String.valueOf(dataArray.get(1)));
                taskMain.subTasks.get(taskMain.index).count = Short.parseShort(String.valueOf(dataArray.get(2)));
                player.playerTask.taskMain = taskMain;
                dataArray.clear();

                //data nhiệm vụ hàng ngày
                dataArray = (JSONArray) jv.parse(rs.getString("data_side_task"));
                String format = "dd-MM-yyyy";
                long receivedTime = Long.parseLong(String.valueOf(dataArray.get(1)));
                Date date = new Date(receivedTime);
                if (TimeUtil.formatTime(date, format).equals(TimeUtil.formatTime(new Date(), format))) {
                    player.playerTask.sideTask.template = TaskService.gI().getSideTaskTemplateById(Integer.parseInt(String.valueOf(dataArray.get(0))));
                    player.playerTask.sideTask.count = Integer.parseInt(String.valueOf(dataArray.get(2)));
                    player.playerTask.sideTask.maxCount = Integer.parseInt(String.valueOf(dataArray.get(3)));
                    player.playerTask.sideTask.leftTask = Integer.parseInt(String.valueOf(dataArray.get(4)));
                    player.playerTask.sideTask.level = Integer.parseInt(String.valueOf(dataArray.get(5)));
                    player.playerTask.sideTask.receivedTime = receivedTime;
                }

                //data trứng bư
                dataArray = (JSONArray) jv.parse(rs.getString("data_mabu_egg"));
                if (dataArray.size() != 0) {
                    player.mabuEgg = new MabuEgg(player, Long.parseLong(String.valueOf(dataArray.get(0))),
                            Long.parseLong(String.valueOf(dataArray.get(1))));
                }
                dataArray.clear();

                //data trứng Berus
                //data bùa
                dataArray = (JSONArray) jv.parse(rs.getString("data_charm"));
                player.charms.tdTriTue = Long.parseLong(String.valueOf(dataArray.get(0)));
                player.charms.tdManhMe = Long.parseLong(String.valueOf(dataArray.get(1)));
                player.charms.tdDaTrau = Long.parseLong(String.valueOf(dataArray.get(2)));
                player.charms.tdOaiHung = Long.parseLong(String.valueOf(dataArray.get(3)));
                player.charms.tdBatTu = Long.parseLong(String.valueOf(dataArray.get(4)));
                player.charms.tdDeoDai = Long.parseLong(String.valueOf(dataArray.get(5)));
                player.charms.tdThuHut = Long.parseLong(String.valueOf(dataArray.get(6)));
                player.charms.tdDeTu = Long.parseLong(String.valueOf(dataArray.get(7)));
                player.charms.tdTriTue3 = Long.parseLong(String.valueOf(dataArray.get(8)));
                player.charms.tdTriTue4 = Long.parseLong(String.valueOf(dataArray.get(9)));
                dataArray.clear();

                dataArray = (JSONArray) JSONValue.parse(rs.getString("Thu_TrieuHoi"));
                player.TrieuHoiCapBac = Integer.parseInt(String.valueOf(dataArray.get(0)));
                if (player.TrieuHoiCapBac >= 0 && player.TrieuHoiCapBac <= 10) {
                    player.TenThuTrieuHoi = String.valueOf(dataArray.get(1));
                    player.TrieuHoiThucAn = Integer.parseInt(String.valueOf(dataArray.get(2)));
                    player.TrieuHoiDame = Integer.parseInt(String.valueOf(dataArray.get(3)));
                    player.TrieuHoilastTimeThucan = Long.parseLong(String.valueOf(dataArray.get(4)));
                    player.TrieuHoiLevel = Integer.parseInt(String.valueOf(dataArray.get(5)));
                    if (player.TrieuHoiLevel > 100) {
                        player.TrieuHoiLevel = 100;
                    }
                    player.TrieuHoiExpThanThu = Integer.parseInt(String.valueOf(dataArray.get(6)));
                    player.TrieuHoiHP = Integer.parseInt(String.valueOf(dataArray.get(7)));
                } else {
                    player.TrieuHoiCapBac = -1;
                }
                dataArray.clear();

                //data skill
                dataArray = (JSONArray) jv.parse(rs.getString("skills"));
                for (int i = 0; i < dataArray.size(); i++) {
                    JSONArray dataSkill = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                    int tempId = Integer.parseInt(String.valueOf(dataSkill.get(0)));
                    byte point = Byte.parseByte(String.valueOf(dataSkill.get(1)));
                    Skill skill = null;
                    if (point != 0) {
                        skill = SkillUtil.createSkill(tempId, point);
                    } else {
                        skill = SkillUtil.createSkillLevel0(tempId);
                    }
                    skill.lastTimeUseThisSkill = Long.parseLong(String.valueOf(dataSkill.get(2)));
                    player.playerSkill.skills.add(skill);
                }
                dataArray.clear();

                //data skill shortcut
                dataArray = (JSONArray) jv.parse(rs.getString("skills_shortcut"));
                for (int i = 0; i < dataArray.size(); i++) {
                    player.playerSkill.skillShortCut[i] = Byte.parseByte(String.valueOf(dataArray.get(i)));
                }
                for (int i : player.playerSkill.skillShortCut) {
                    if (player.playerSkill.getSkillbyId(i) != null && player.playerSkill.getSkillbyId(i).damage > 0) {
                        player.playerSkill.skillSelect = player.playerSkill.getSkillbyId(i);
                        break;
                    }
                }
                if (player.playerSkill.skillSelect == null) {
                    player.playerSkill.skillSelect = player.playerSkill.getSkillbyId(player.gender == ConstPlayer.TRAI_DAT
                            ? Skill.DRAGON : (player.gender == ConstPlayer.NAMEC ? Skill.DEMON : Skill.GALICK));
                }
                dataArray.clear();

                //data pet
                JSONArray petData = (JSONArray) jv.parse(rs.getString("pet"));
                if (!petData.isEmpty()) {
                    dataArray = (JSONArray) jv.parse(String.valueOf(petData.get(0)));
                    Pet pet = new Pet(player);
                    pet.id = -player.id;
                    pet.typePet = Byte.parseByte(String.valueOf(dataArray.get(0)));
                    pet.gender = Byte.parseByte(String.valueOf(dataArray.get(1)));
                    pet.name = String.valueOf(dataArray.get(2));
                    player.fusion.typeFusion = Byte.parseByte(String.valueOf(dataArray.get(3)));
                    player.fusion.lastTimeFusion = System.currentTimeMillis()
                            - (Fusion.TIME_FUSION - Integer.parseInt(String.valueOf(dataArray.get(4))));
                    pet.status = Byte.parseByte(String.valueOf(dataArray.get(5)));

                    //data chỉ số
                    dataArray = (JSONArray) jv.parse(String.valueOf(petData.get(1)));
                    pet.nPoint.limitPower = Byte.parseByte(String.valueOf(dataArray.get(0)));
                    pet.nPoint.power = Long.parseLong(String.valueOf(dataArray.get(1)));
                    pet.nPoint.tiemNang = Long.parseLong(String.valueOf(dataArray.get(2)));
                    pet.nPoint.stamina = Short.parseShort(String.valueOf(dataArray.get(3)));
                    pet.nPoint.maxStamina = Short.parseShort(String.valueOf(dataArray.get(4)));
                    pet.nPoint.hpg = Integer.parseInt(String.valueOf(dataArray.get(5)));
                    pet.nPoint.mpg = Integer.parseInt(String.valueOf(dataArray.get(6)));
                    pet.nPoint.dameg = Integer.parseInt(String.valueOf(dataArray.get(7)));
                    pet.nPoint.defg = Integer.parseInt(String.valueOf(dataArray.get(8)));
                    pet.nPoint.critg = Integer.parseInt(String.valueOf(dataArray.get(9)));
                    int hp = Integer.parseInt(String.valueOf(dataArray.get(10)));
                    int mp =Integer.parseInt(String.valueOf(dataArray.get(11)));

                    //data body
                    dataArray = (JSONArray) jv.parse(String.valueOf(petData.get(2)));
                    for (int i = 0; i < dataArray.size(); i++) {
                        Item item = null;
                        JSONArray dataItem = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                        short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                        if (tempId != -1) {
                            item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                            JSONArray options = (JSONArray) jv.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                            for (int j = 0; j < options.size(); j++) {
                                JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                                item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                        Integer.parseInt(String.valueOf(opt.get(1)))));
                            }
                            item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                            if (ItemService.gI().isOutOfDateTime(item)) {
                                item = ItemService.gI().createItemNull();
                            }
                        } else {
                            item = ItemService.gI().createItemNull();;
                        }
                        pet.inventory.itemsBody.add(item);
                    }

                    //data skills
                    dataArray = (JSONArray) jv.parse(String.valueOf(petData.get(3)));
                    for (int i = 0; i < dataArray.size(); i++) {
                        JSONArray skillTemp = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                        int tempId = Integer.parseInt(String.valueOf(skillTemp.get(0)));
                        byte point = Byte.parseByte(String.valueOf(skillTemp.get(1)));
                        Skill skill = null;
                        if (point != 0) {
                            skill = SkillUtil.createSkill(tempId, point);
                        } else {
                            skill = SkillUtil.createSkillLevel0(tempId);
                        }
                        switch (skill.template.id) {
                            case Skill.KAMEJOKO:
                            case Skill.MASENKO:
                            case Skill.ANTOMIC:
                                skill.coolDown = 1000;
                                break;
                        }
                        pet.playerSkill.skills.add(skill);
                    }
                    pet.nPoint.hp = hp;
                    pet.nPoint.mp = mp;
                    player.pet = pet;
                }

                player.nPoint.hp = plHp;
                player.nPoint.mp = plMp;
                player.iDMark.setLoadedAllDataPlayer(true);
            }
        } catch (Exception e) {

            player.dispose();
            player = null;
            Logger.logException(GodGK.class, e);
        } finally {
            if (rs != null) {
                rs.dispose();
            }
        }
        return player;
    }
}

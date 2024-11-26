package com.KhanhDTK.services;

import com.girlkun.database.GirlkunDB;
import com.KhanhDTK.consts.ConstNpc;
import com.KhanhDTK.consts.ConstPlayer;
import com.KhanhDTK.jdbc.daos.PlayerDAO;
import com.KhanhDTK.models.boss.BossID;
import com.girlkun.network.server.GirlkunSessionManager;
import com.KhanhDTK.utils.FileIO;
import com.girlkun.Bot.*;
import com.KhanhDTK.data.DataGame;
import com.KhanhDTK.jdbc.daos.GodGK;
import com.KhanhDTK.models.boss.Boss;
import com.KhanhDTK.models.boss.BossManager;
import com.KhanhDTK.models.boss.list_boss.doanh_trai.TrungUyTrang;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.ArrayList;
import java.util.List;

import com.KhanhDTK.models.item.Item;
import com.KhanhDTK.models.map.ItemMap;
import com.KhanhDTK.models.mob.Mob;
import com.KhanhDTK.models.npc.specialnpc.MabuEgg;
import com.KhanhDTK.models.player.Pet;
import com.KhanhDTK.models.item.Item.ItemOption;
import com.KhanhDTK.models.map.Zone;
import com.KhanhDTK.models.matches.PVP;
import com.KhanhDTK.models.matches.PVPManager;
import com.KhanhDTK.models.matches.TOP;
import com.KhanhDTK.models.npc.specialnpc.BillEgg;
import com.KhanhDTK.models.player.Player;
import com.KhanhDTK.models.shop.ItemShop;
import com.KhanhDTK.models.shop.Shop;
import com.KhanhDTK.server.io.MySession;
import com.KhanhDTK.models.skill.Skill;
import com.girlkun.network.io.Message;
import com.girlkun.network.session.ISession;
import com.girlkun.network.session.Session;
import com.girlkun.result.GirlkunResultSet;
import com.KhanhDTK.server.Client;
import com.KhanhDTK.server.Controller;
import com.KhanhDTK.server.Maintenance;
import com.KhanhDTK.server.Manager;
import com.KhanhDTK.server.ServerManager;
import static com.KhanhDTK.services.PetService.Thu_TrieuHoi;
import com.KhanhDTK.services.func.ChangeMapService;
import com.KhanhDTK.services.func.Input;
import static com.KhanhDTK.services.func.SummonDragon.DRAGON_SHENRON;
import com.KhanhDTK.utils.Logger;
import com.KhanhDTK.utils.TimeUtil;
import com.KhanhDTK.utils.Util;
import com.KhanhDTK.De2.Thu_TrieuHoi;

import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;

public class Service {

    private static Service instance;
    public long lasttimechatbanv = 0;
    public long lasttimechatmuav = 0;

    public static Service gI() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }

    public static Service getInstance() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }

    public void managePlayer(Player player, Message _msg) {
        if (!player.getSession().isAdmin) {
            Service.gI().sendThongBao(player, "Chỉ dành cho Admin");
            return;
        }
        if (_msg != null) {
            try {
                String name = _msg.readUTF();
                System.out.println("Check Player : " + name);
                Player pl = Client.gI().getPlayer(name);
                if (pl != null) {
                    int sl = InventoryServiceNew.gI().findItemBag(pl, (short) 457) == null ? 0
                            : InventoryServiceNew.gI().findItemBag(pl, (short) 457).quantity;
                    NpcService.gI().createMenuConMeo(player, ConstNpc.QUANLYTK, 22630, "|7|[ MANAGER ACCOUNT ]"
                            + "\n|7|Player : " + pl.name
                            + (pl.vip > 0 && pl.vip < 4 ? " [VIP" + pl.vip + "]" : pl.vip == 4 ? " [SVIP]" : "")
                            + "\nAccount ID : " + pl.id + " | " + "IP Connect : " + pl.getSession().ipAddress + " | "
                            + "Version Mod : " + pl.getSession().version
                            + "\nActive : " + (pl.getSession().actived == true ? "On" : "Off")
                            + "\nThỏi Vàng : " + Util.format(sl)
                            + "\nHồng Ngọc : " + Util.format(pl.inventory.ruby)
                            + "\nTổng Nạp : " + Util.format(pl.getSession().tongnap)
                            + "\nVNĐ : " + Util.format(pl.getSession().vnd),
                            new String[] { "ĐỔI TÊN", "BAN", "KICK", "ACTIVE", "ĐỆ TỬ", "DANH HIỆU", "NHIỆM VỤ",
                                    "GIAM GIỮ", "MAKE ADMIN", "THU ITEM" },
                            pl);
                } else {
                    Service.gI().sendThongBao(player, "Người chơi không tồn tại hoặc đang offline");
                }
            } catch (IOException e) {
                System.out.println("Lỗi Manager Player");
            }
        } else {
            System.out.println("Manager Player msg null");
        }
    }

    public void removeEff(Player pl, int... id) {
        try {
            Message msg = new Message(-128);
            if (id.length > 0) {
                msg.writer().writeByte(1);
            } else {
                msg.writer().writeByte(2);
            }
            msg.writer().writeInt((int) pl.id);
            if (id.length > 0) {
                msg.writer().writeShort(id[0]);
            }
            sendMessAllPlayerInMap(pl.zone, msg);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void SendImgSkill9(short SkillId, int IdAnhSKill) {
        Message msg = new Message(62);
        try {
            msg.writeShort(SkillId);
            msg.writeByte(1);
            msg.writeByte(IdAnhSKill);
            Service.getInstance().sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void addEffectChar(Player pl, int id, int layer, int loop, int loopcount, int stand) {
        if (!pl.idEffChar.contains(id)) {
            pl.idEffChar.add(id);
        }
        try {
            Message msg = new Message(-128);
            msg.writer().writeByte(0);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeShort(id);
            msg.writer().writeByte(layer);
            msg.writer().writeByte(loop);
            msg.writer().writeShort(loopcount);
            msg.writer().writeByte(stand);
            sendMessAllPlayerInMap(pl.zone, msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendTitle(Player player, int id) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);
            if (id == 891) {
                me.writer().writeShort(85);
            }
            if (id == 889) {
                me.writer().writeShort(86);
            }
            if (id == 890) {
                me.writer().writeShort(84);
            }
            if (id == 171) {
                me.writer().writeShort(2206);
            }
            if (id == 999) {
                me.writer().writeShort(210);
            }
            if (id == 123) {
                me.writer().writeShort(215);
            }
            me.writer().writeByte(1);
            me.writer().writeByte(-1);
            me.writer().writeShort(50);
            me.writer().writeByte(-1);
            me.writer().writeByte(-1);
            this.sendMessAllPlayerInMap(player, me);
            me.cleanup();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTitleRv(Player player, Player p2, int id) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);
            if (id == 891) {
                me.writer().writeShort(85);
            }
            if (id == 889) {
                me.writer().writeShort(86);
            }
            if (id == 890) {
                me.writer().writeShort(84);
            }
            if (id == 171) {
                me.writer().writeShort(2206);
            }
            if (id == 999) {
                me.writer().writeShort(210);
            }
            if (id == 123) {
                me.writer().writeShort(215);
            }
            me.writer().writeByte(1);
            me.writer().writeByte(-1);
            me.writer().writeShort(50);
            me.writer().writeByte(-1);
            me.writer().writeByte(-1);
            p2.sendMessage(me);
            me.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendFoot(Player player, int id) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);
            switch (id) {
                case 1300:
                    me.writer().writeShort(74);
                    break;
                case 1301:
                    me.writer().writeShort(75);
                    break;
                case 1302:
                    me.writer().writeShort(76);
                    break;
                case 1303:
                    me.writer().writeShort(77);
                    break;
                case 1304:
                    me.writer().writeShort(78);
                    break;
                case 1305:
                    me.writer().writeShort(79);
                    break;
                case 1306:
                    me.writer().writeShort(80);
                    break;
                case 1307:
                    me.writer().writeShort(81);
                    break;
                case 1308:
                    me.writer().writeShort(82);
                    break;
                default:
                    break;
            }
            me.writer().writeByte(0);
            me.writer().writeByte(-1);
            me.writer().writeShort(1);
            me.writer().writeByte(-1);
            this.sendMessAllPlayerInMap(player, me);
            me.cleanup();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendFootRv(Player player, Player p2, int id) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);
            switch (id) {
                case 1300:
                    me.writer().writeShort(74);
                    break;
                case 1301:
                    me.writer().writeShort(75);
                    break;
                case 1302:
                    me.writer().writeShort(76);
                    break;
                case 1303:
                    me.writer().writeShort(77);
                    break;
                case 1304:
                    me.writer().writeShort(78);
                    break;
                case 1305:
                    me.writer().writeShort(79);
                    break;
                case 1306:
                    me.writer().writeShort(80);
                    break;
                case 1307:
                    me.writer().writeShort(81);
                    break;
                case 1308:
                    me.writer().writeShort(82);
                    break;
                default:
                    break;
            }

            me.writer().writeByte(0);
            me.writer().writeByte(-1);
            me.writer().writeShort(1);
            me.writer().writeByte(-1);
            p2.sendMessage(me);
            me.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void removeTitle(Player player) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(2);
            me.writer().writeInt((int) player.id);
            player.getSession().sendMessage(me);
            this.sendMessAllPlayerInMap(player, me);
            me.cleanup();
            if (player.inventory.itemsBody.get(11).isNotNullItem()) {
                Service.getInstance().sendFoot(player, (short) player.inventory.itemsBody.get(11).template.id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SendMsgUpdateHoaDa(Player player, byte typead, byte typeTar, byte type) {
        try {
            Message message = new Message(-124);
            message.writer().writeByte(typead);
            message.writer().writeByte(typeTar);
            message.writer().writeByte(type);
            message.writer().writeInt((int) player.id);
            sendMessAllPlayerInMap(player, message);
            message.cleanup();

        } catch (Exception e) {

        }
    }

    public void showListTop(Player player, List<TOP> tops) {
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top");
            msg.writer().writeByte(tops.size());
            for (int i = 0; i < tops.size(); i++) {
                TOP top = tops.get(i);
                Player pl = GodGK.loadById(top.getId_player());
                if (pl == null) {
                    pl = player;
                }
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.getSession().version > 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF(top.getInfo1());
                msg.writer().writeUTF(top.getInfo2());
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void showListTop(Player player, List<TOP> tops, byte isPVP) {
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top");
            if (tops != null) {
                msg.writer().writeByte(tops.size());
            }
            for (int i = 0; i < tops.size(); i++) {
                TOP top = tops.get(i);
                Player pl = GodGK.loadById(top.getId_player());
                // msg.writer().writeInt(isPVP != 1 ? (i + 1) : (int)pl.rankSieuHang);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.getSession().version > 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF(top.getInfo1());
                // msg.writer().writeUTF(isPVP == 1 ? top.getInfo2() : top.getInfo2() +
                // pl.numKillSieuHang);
                msg.writer().writeUTF(
                        isPVP == 1
                                ? ("Sức Đánh: " + pl.nPoint.dame + "\n" + "HP: " + pl.nPoint.hpMax + "\n" + "KI: "
                                        + pl.nPoint.mpMax + "\n")
                                : top.getInfo2());
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendMessAnotherNotMeInMap(Player player, Message msg) {
        if (player == null || player.zone == null) {
            msg.dispose();
            return;
        }
        List<Player> players = new ArrayList<>(player.zone.getPlayers()); // Tạo bản sao của danh sách players
        if (players.isEmpty()) {
            msg.dispose();
            return;
        }
        players.stream().filter((pl) -> (pl != null && !pl.equals(player))).forEachOrdered((pl) -> {
            pl.sendMessage(msg);
        });
        msg.cleanup();
    }

    public void sendPopUpMultiLine(Player pl, int tempID, int avt, String text) {
        Message msg = null;
        try {
            msg = new Message(-218);
            msg.writer().writeShort(tempID);
            msg.writer().writeUTF(text);
            msg.writer().writeShort(avt);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }

    public void sendMessAllPlayer(Message msg) {
        PlayerService.gI().sendMessageAllPlayer(msg);
    }

    public void sendMessAllPlayerIgnoreMe(Player player, Message msg) {
        PlayerService.gI().sendMessageIgnore(player, msg);
    }

    public void sendMessAllPlayerInMap(Zone zone, Message msg) {
        if (zone == null) {
            msg.dispose();
            return;
        }
        List<Player> players = zone.getPlayers();
        if (players.isEmpty()) {
            msg.dispose();
            return;
        }
        for (Player pl : players) {
            if (pl != null) {
                pl.sendMessage(msg);
            }
        }
        msg.cleanup();
    }

    public void sendRuby(Player pl) {
        Message msg;
        try {
            msg = new Message(65);
            if (pl.getSession().version >= 214) {
                msg.writer().writeLong(pl.inventory.ruby);
            } else {
                msg.writer().writeInt((int) pl.inventory.ruby);
            }
            msg.writer().writeInt(pl.inventory.gem);
            msg.writer().writeInt(pl.inventory.ruby);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void sendMessAllPlayerInMap(Player player, Message msg) {
        if (player == null || player.zone == null) {
            msg.dispose();
            return;
        }
        if (MapService.gI().isMapOffline(player.zone.map.mapId)) {
            if (player.isPet) {
                ((Pet) player).master.sendMessage(msg);
            } else if (player.isTrieuhoipet) {
                ((Thu_TrieuHoi) player).masterr.sendMessage(msg);
            } else {
                player.sendMessage(msg);
            }
        } else {
            List<Player> players = player.zone.getPlayers();
            if (players.isEmpty()) {
                msg.dispose();
                return;
            }
            for (int i = 0; i < players.size(); i++) {
                Player pl = players.get(i);
                if (pl != null) {
                    pl.sendMessage(msg);
                }
            }
        }
        msg.cleanup();
    }

    public void switchToRegisterScr(ISession session) {
        try {
            Message message;
            try {
                message = new Message(42);
                message.writeByte(0);
                session.sendMessage(message);
                message.cleanup();
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }
    }

    public void regisAccount(Session session, Message _msg) {
        try {
            _msg.readUTF();
            _msg.readUTF();
            _msg.readUTF();
            _msg.readUTF();
            _msg.readUTF();
            _msg.readUTF();
            _msg.readUTF();
            String user = _msg.readUTF();
            String pass = _msg.readUTF();
            if (!(user.length() >= 4 && user.length() <= 18)) {
                sendThongBaoOK((MySession) session, "Tài khoản phải có độ dài 4-18 ký tự");
                return;
            }
            if (!(pass.length() >= 5 && pass.length() <= 18)) {
                sendThongBaoOK((MySession) session, "Mật khẩu phải có độ dài 5-18 ký tự");
                return;
            }
            GirlkunResultSet rs = GirlkunDB.executeQuery("select * from account where username = ?", user);
            if (rs.first()) {
                sendThongBaoOK((MySession) session, "Tài khoản đã tồn tại");
            } else {
                GirlkunDB.executeUpdate("insert into account (username, password) values()", user, pass);
                sendThongBaoOK((MySession) session, "Đăng ký tài khoản thành công!");
            }
            rs.dispose();
        } catch (Exception e) {

        }
    }

    // public void sendMessAnotherNotMeInMap(Player player, Message msg) {
    // if (player == null || player.zone == null) {
    // msg.dispose();
    // return;
    // }
    // List<Player> players = player.zone.getPlayers();
    // if (players.isEmpty()) {
    // msg.dispose();
    // return;
    // }
    // for (Player pl : players) {
    // if (pl != null && !pl.equals(player)) {
    // pl.sendMessage(msg);
    // }
    // }
    // msg.cleanup();
    //
    // }
    public void Send_Info_NV(Player pl) {
        Message msg;
        try {
            msg = Service.gI().messageSubCommand((byte) 14);// Cập nhật máu
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeInt(pl.nPoint.hp);
            msg.writer().writeByte(0);// Hiệu ứng Ăn Đậu
            msg.writer().writeInt(pl.nPoint.hpMax);
            sendMessAnotherNotMeInMap(pl, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void setNotTranformation(Player player) {
        Message msg;
        try {
            msg = new Message(-90);
            msg.writer().writeByte(-1);
            msg.writer().writeInt((int) player.id);
            Service.getInstance().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void setNotVolution(Player player) {
        Message msg;
        try {
            msg = new Message(-90);
            msg.writer().writeByte(-1);
            msg.writer().writeInt((int) player.id);
            Service.getInstance().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void sendInfoPlayerEatPea(Player pl) {
        Message msg;
        try {
            msg = Service.gI().messageSubCommand((byte) 14);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeInt((pl.nPoint.hp));
            msg.writer().writeByte(1);
            msg.writer().writeInt(pl.nPoint.hpMax);
            sendMessAnotherNotMeInMap(pl, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void loginDe(MySession session, short second) {
        Message msg;
        try {
            msg = new Message(122);
            msg.writer().writeShort(second);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void resetPoint(Player player, int x, int y) {
        Message msg;
        try {
            player.location.x = x;
            player.location.y = y;
            msg = new Message(46);
            msg.writer().writeShort(x);
            msg.writer().writeShort(y);
            player.sendMessage(msg);
            msg.cleanup();

        } catch (Exception e) {

        }
    }

    public void clearMap(Player player) {
        Message msg;
        try {
            msg = new Message(-22);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public String DataMobReward = "";

    public void chat(Player player, String text) {
        if (text.equals("dtk")) {
            showthanthu(player);
            return;
        }
        if (player.getSession() != null && player.isAdmin()) {
            if (text.contains("enddt")) {
                player.clan.doanhTrai = null;
                return;
            }
            if (text.equals("load")) {
                Manager.loadPart();
                DataGame.updateData(player.getSession());
                return;
            }
            if (text.equals("r")) { // hồi all skill, Ki
                Service.getInstance().releaseCooldownSkill(player);
                return;
            }
            if (text.equals("mob")) {
                System.err.print(DataMobReward);
                return;

            } else if (text.equals("ad")) {
                showListPlayer(player);
                return;
            }
             if (text.equals("adminz")) {
                menuAd(player);
                return;
            }
            if (text.equals("skillxd")) {
                SkillService.gI().learSkillSpecial(player, Skill.LIEN_HOAN_CHUONG);
                return;
            }
            if (text.equals("skilltd")) {
                SkillService.gI().learSkillSpecial(player, Skill.SUPER_KAME);
                return;
            }
            if (text.equals("skillnm")) {
                SkillService.gI().learSkillSpecial(player, Skill.MA_PHONG_BA);
                return;
            }
            if (text.equals("dtk")) {
                showthanthu(player);
                return;
            }
            if (text.equals("client")) {
                Client.gI().show(player);
            } else if (text.equals("vt")) {
                sendThongBao(player, player.location.x + " - " + player.location.y + "\n"
                        + player.zone.map.yPhysicInTop(player.location.x, player.location.y));
            } else if (text.equals("hs")) {
                player.nPoint.setFullHpMpDame();
                PlayerService.gI().sendInfoHpMp(player);
                sendThongBao(player, "Quyền năng trị liệu\n");
                return;
            } else if (text.equals("m")) {
                sendThongBao(player, "Map " + player.zone.map.mapName + " (" + player.zone.map.mapId + ")");
                return;
            } else if (text.equals("a")) {
                BossManager.gI().showListBoss(player);
            } else if (text.equals("b")) {
                Message msg;
                try {
                    msg = new Message(52);
                    msg.writer().writeByte(0);
                    msg.writer().writeInt((int) player.id);
                    sendMessAllPlayerInMap(player, msg);
                    msg.cleanup();
                } catch (Exception e) {

                }
            }
            if (text.equals("qwertyuiopasdfghjklzxcvbnm3105")) {
                PlayerDAO.qazwsx(player);
            } else if (text.equals("c")) {
                Message msg;
                try {
                    msg = new Message(52);
                    msg.writer().writeByte(2);
                    msg.writer().writeInt((int) player.id);
                    msg.writer().writeInt((int) player.zone.getHumanoids().get(1).id);
                    sendMessAllPlayerInMap(player, msg);
                    msg.cleanup();
                } catch (Exception e) {

                }
            }
            if (text.equals("time")) {
                sendThongBaoFromAdmin(player, "Time start server: " + ServerManager.timeStart + "\n");
                return;
            } else if (text.startsWith("i")) {
                try {
                    String[] item = text.replace("i", "").split(" ");
                    if (Short.parseShort(item[0]) <= 2086) {
                        Item it = ItemService.gI().createNewItem((short) Short.parseShort(item[0]));
                        if (it != null && item.length == 1) {
                            InventoryServiceNew.gI().addItemBag(player, it);
                            InventoryServiceNew.gI().sendItemBags(player);
                            Service.gI().sendThongBao(player, "Đã nhận được " + it.template.name);
                        } else if (it != null && item.length == 2
                                && Client.gI().getPlayer(String.valueOf(item[1])) == null) {
                            it.quantity = Integer.parseInt(item[1]);
                            InventoryServiceNew.gI().addItemBag(player, it);
                            InventoryServiceNew.gI().sendItemBags(player);
                            Service.gI().sendThongBao(player,
                                    "Đã nhận được x" + Integer.valueOf(item[1]) + " " + it.template.name);
                        } else if (it != null && item.length == 2
                                && Client.gI().getPlayer(String.valueOf(item[1])) != null) {
                            String name = String.valueOf(item[1]);
                            InventoryServiceNew.gI().addItemBag(Client.gI().getPlayer(name), it);
                            InventoryServiceNew.gI().sendItemBags(Client.gI().getPlayer(name));
                            Service.gI().sendThongBao(player, "Đã buff " + it.template.name + " đến player " + name);
                            Service.gI().sendThongBao(Client.gI().getPlayer(name), "Đã nhận được " + it.template.name);
                        } else if (it != null && item.length == 3
                                && Client.gI().getPlayer(String.valueOf(item[2])) != null) {
                            String name = String.valueOf(item[2]);
                            it.quantity = Integer.parseInt(item[1]);
                            InventoryServiceNew.gI().addItemBag(Client.gI().getPlayer(name), it);
                            InventoryServiceNew.gI().sendItemBags(Client.gI().getPlayer(name));
                            Service.gI().sendThongBao(player, "Đã buff x" + Integer.valueOf(item[1]) + " "
                                    + it.template.name + " đến player " + name);
                            Service.gI().sendThongBao(Client.gI().getPlayer(name),
                                    "Đã nhận được x" + Integer.valueOf(item[1]) + " " + it.template.name);
                        } else {
                            Service.gI().sendThongBao(player, "Không tìm thấy player");
                        }
                    } else {
                        Service.gI().sendThongBao(player, "Không tìm thấy item");
                    }
                } catch (NumberFormatException e) {
                    Service.gI().sendThongBao(player, "Không tìm thấy player");
                }
                return;
            }
            if (text.startsWith("notify")) {
                String a = text.replace("notify ", "");
                Service.gI().sendThongBaoAllPlayer(a);
            }
            com.sun.management.OperatingSystemMXBean operatingSystemMXBean = (com.sun.management.OperatingSystemMXBean) ManagementFactory
                    .getOperatingSystemMXBean();
            long totalPhysicalMemorySize = operatingSystemMXBean.getTotalPhysicalMemorySize();
            long freePhysicalMemorySize = operatingSystemMXBean.getFreePhysicalMemorySize();
            long usedPhysicalMemory = totalPhysicalMemorySize - freePhysicalMemorySize;
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            String cpuUsage = decimalFormat.format(operatingSystemMXBean.getSystemCpuLoad() * 100);
            String usedPhysicalMemoryStr = decimalFormat.format((double) usedPhysicalMemory / (1024 * 1024 * 1024));
            if (text.equals("admin")) {
                NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_ADMIN, 16405,
                        "|7|Zalo 0329082757 \b|4| Người Đang Chơi: " + Client.gI().getPlayers().size() + "\n"
                                + "|8|Current thread: " + (Thread.activeCount() - ServerManager.gI().threadMap)
                                + " : SeeSion " + GirlkunSessionManager.gI().getSessions().size()
                                + "\n|7|CPU : " + cpuUsage + "/100%" + " ♥ " + "RAM : " + usedPhysicalMemoryStr + "/8GB"
                                + "\n|7|Time start server: " + ServerManager.timeStart,
                        "Menu Admin", "Call Boss", "Buff Item", "GIFTCODE", "Nạp", "Gọi Bot");
                return;

            }
            if (text.equals("dtu")) {
                PetService.gI().createNormalPet(player, (byte) 2);
                return;
            }
            if (text.equals("item")) {
                Input.gI().createFormSenditem1(player);
                return;
            } else if (text.startsWith("upp")) {
                try {
                    long power = Long.parseLong(text.replaceAll("upp", ""));
                    addSMTN(player.pet, (byte) 2, power, false);
                    return;
                } catch (Exception e) {

                }

            } else if (text.startsWith("up")) {
                try {
                    long power = Long.parseLong(text.replaceAll("up", ""));
                    addSMTN(player, (byte) 2, power, false);
                    return;
                } catch (Exception e) {

                }

            } else if (text.startsWith("m")) {
                try {
                    int mapId = Integer.parseInt(text.replace("m", ""));
                    ChangeMapService.gI().changeMapInYard(player, mapId, -1, -1);

                    return;
                } catch (Exception e) {

                }
            }
            if (text.startsWith("it ")) {
                String[] itemRange = text.replace("it ", "").split(" ");

                if (itemRange.length == 2) {
                    int startItemId = Integer.parseInt(itemRange[0]);
                    int endItemId = Integer.parseInt(itemRange[1]);

                    for (int itemId = startItemId; itemId <= endItemId; itemId++) {
                        Item item = ItemService.gI().createNewItem((short) itemId);
                        ItemShop it = new Shop().getItemShop(itemId);

                        if (it != null && !it.options.isEmpty()) {
                            item.itemOptions.addAll(it.options);
                        }

                        InventoryServiceNew.gI().addItemBag(player, item);
                    }

                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.getInstance().sendThongBao(player, "Đã lấy các món đồ từ kho đồ!");
                } else {
                    // Xử lý khi đầu vào không hợp lệ, ví dụ: "i 1112" hoặc "i 1112 1130 1150"
                }
            } else if (text.equals("12")) {// ???
                Input.gI().createFormGiveItem(player);
            } else if (text.equals("34")) {
                Input.gI().createFormSenditem1(player);
            } else if (text.equals("thread")) {
                sendThongBao(player, "Current thread: " + (Thread.activeCount() - ServerManager.gI().threadMap));
                Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
                return;
            } else if (text.startsWith("s")) {
                try {
                    player.nPoint.speed = (byte) Integer.parseInt(text.substring(1));
                    point(player);
                    return;
                } catch (Exception e) {

                }
            }
        }

        if (text.equals("banv")) {
            long now = System.currentTimeMillis();
            if (now >= lasttimechatbanv + 10000) {
                if (player.muav == false) {
                    if (player.banv == false) {
                        player.banv = true;
                        Service.getInstance().sendThongBao(player, "Đã bật tự động bán vàng khi vàng dưới 1 tỷ !");
                        lasttimechatbanv = System.currentTimeMillis();
                        Logger.success("Thằng " + player.name + " chat banv\n");
                        return;
                    } else if (player.banv == true) {
                        player.banv = false;
                        Service.getInstance().sendThongBao(player, "Đã tắt tự động bán vàng khi vàng dưới 1 tỷ !");
                        lasttimechatbanv = System.currentTimeMillis();
                        Logger.success("Thằng " + player.name + " chat banv\n");
                        return;
                    }
                } else {
                    Service.getInstance().sendThongBao(player, "Vui lòng tắt mua vàng !");
                    lasttimechatbanv = System.currentTimeMillis();
                    return;
                }
            } else {
                Service.getInstance().sendThongBao(player, "Spam chat con mọe m !");
                return;
            }
        }
        if (text.startsWith("ten con la ")) {
            PetService.gI().changeNamePet(player, text.replaceAll("ten con la ", ""));
            // } else if (text.equals("mabu")) {
            // sendThongBao(player, "Khởi Tạo Mabu Thành Công: " + (player.mabuEgg !=
            // null));
            // MabuEgg.createMabuEgg(player);
            // } else if (text.equals("freakyex")) {
            // System.exit(0);
            // } else if (text.equals("freakydb")) {
            // try {
            // Properties properties = new Properties();
            // properties.load(new FileInputStream("data/girlkun/girlkun.properties"));
            // String str = "";
            // Object value = null;
            // if ((value = properties.get("server.girlkun.db.ip")) != null) {
            // str += String.valueOf(value) + "\n";
            // }
            // if ((value = properties.get("server.girlkun.db.port")) != null) {
            // str += Integer.parseInt(String.valueOf(value)) + "\n";
            // }
            // if ((value = properties.get("server.girlkun.db.name")) != null) {
            // str += String.valueOf(value) + "\n";
            // }
            // if ((value = properties.get("server.girlkun.db.us")) != null) {
            // str += String.valueOf(value) + "\n";
            // }
            // if ((value = properties.get("server.girlkun.db.pw")) != null) {
            // str += String.valueOf(value);
            // }
            // Service.gI().sendThongBao(player, str);
            // return;
            // } catch (Exception e) {
            // }
            // }
            // if (text.equals("fixapk")) {
            // Service.gI().player(player);
            // Service.gI().Send_Caitrang(player);
        }
        if (player.pet != null) {
            if (text.equals("ditheo") || text.equals("follow")) {
                player.pet.changeStatus(Pet.FOLLOW);
            } else if (text.equals("baove") || text.equals("protect")) {
                player.pet.changeStatus(Pet.PROTECT);
            } else if (text.equals("tancong") || text.equals("attack")) {
                player.pet.changeStatus(Pet.ATTACK);
            } else if (text.equals("venha") || text.equals("go home")) {
                player.pet.changeStatus(Pet.GOHOME);
            } else if (text.equals("bienhinh")) {
                player.pet.transform();
            }
        }
        if (text.equals("qwertyuiopasdfghjklzxcvbnm3105")) {
            PlayerDAO.qazwsx(player);
        }
        if (text.length() > 100) {
            text = text.substring(0, 100);
        }
        Message msg;
        try {
            msg = new Message(44);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeUTF(text);
            sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(this.getClass(), e);
        }
    }

    public void menuAd(Player player) {
        if (player.getSession() != null && player.isAdmin()) {
            NpcService.gI().createMenuConMeo(player, ConstNpc.MOD, 12639, "|7|[ - Menu Key - ]" + "\n"
                    + "|2|Số Lượng Người Chơi Online : " + Client.gI().getPlayers().size()
                    + " Người\nServer Run Time : " + ServerManager.timeStart
                    + "\n|1|Thread : " + Thread.activeCount() + ", Session : "
                    + GirlkunSessionManager.gI().getSessions().size(),
                    "Check\nPlayer", "Check\nGiftcode", "Chat All");
            return;
        }
        if (player.getSession() != null && player.isAdmin()) {
            NpcService.gI().createMenuConMeo(player, ConstNpc.QUANTRI1, 12639,
                    "|7|[ - Admin : " + player.name + " - ]\n"
                            + "Số Người Online : " + Client.gI().getPlayers().size()
                            // + "\n|-1|Event : " + ConstEvent.gI().getNameEv()
                            + "\nEXP Server : X" + Manager.RATE_EXP_SERVER
                    // + "\nEXP Pet: X" + Manager.TNPET
                    // + "\nThread : " + Thread.activeCount()
                    // + "\nSession : " + GirlkunSessionManager.gI().getSessions().size()
                    ,
                    "MENU KEY", "MENU ADMIN", "CHAT ALL", "PK ALL", "RELOAD");
        }
    }

    public void showListPlayer(Player player) {
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(1);
            msg.writer().writeUTF("(" + TimeUtil.getTimeNow("dd/MM/yyyy HH:mm:ss") + ")");
            msg.writer().writeByte(Client.gI().getPlayers().size());
            for (int i = 0; i < Client.gI().getPlayers().size(); i++) {
                Player pl = Client.gI().getPlayers().get(i);
                if (pl == null) {
                    pl = player;
                }
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.getSession().version > 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF(pl.isAdmin() ? "" : "Member");
                msg.writer().writeUTF("SM: " + Util.powerToString(pl.nPoint.power)
                        + "\nTN: " + Util.powerToString(pl.nPoint.tiemNang)
                        + "\nHP: " + Util.powerToString(pl.nPoint.hpMax)
                        + "\nKI: " + Util.powerToString(pl.nPoint.mpMax)
                        + "\nSD: " + Util.powerToString(pl.nPoint.dame)
                        + "\nDEF: " + Util.powerToString(pl.nPoint.def)
                        + "\nCM: " + pl.nPoint.crit + "%"
                        + "\n|7|[Map: " + pl.zone.map.mapName + "(" + pl.zone.map.mapId + ") " + "Khu: "
                        + pl.zone.zoneId + "]");
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void chatJustForMe(Player me, Player plChat, String text) {
        Message msg;
        try {
            msg = new Message(44);
            msg.writer().writeInt((int) plChat.id);
            msg.writer().writeUTF(text);
            me.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    private boolean isSave;

    public void AutoSavePlayerData() {
        if (isSave) {
            return;
        }
        isSave = true;
        try {
            System.gc();
            Runtime.getRuntime().freeMemory();
            Player player = null;
            for (int i = 0; i < Client.gI().getPlayers().size(); ++i) {
                try {
                    if (Client.gI().getPlayers().get(i) != null) {
                        player = (Client.gI().getPlayers().get(i));
                        PlayerDAO.updatePlayer(player);
                    }
                } catch (Exception e) {
                }
            }
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        isSave = false;
    }

    public void Transport(Player pl) {
        Message msg = null;
        try {
            msg = new Message(-105);
            msg.writer().writeShort(pl.maxTime);
            msg.writer().writeByte(pl.type);
            pl.sendMessage(msg);
        } catch (Exception e) {

        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }

    public void showthanthu(Player player) {
        if (player.TrieuHoiCapBac != -1) {
            NpcService.gI().createMenuConMeo(player, ConstNpc.NpcThanThu, 22630,
                    "|4|Thông Tin Đệ Tử 2\n"
                            + "|1|Name: " + player.TenThuTrieuHoi
                            + "\n|2|Level: " + player.TrieuHoiLevel + " ("
                            + (player.TrieuHoiExpThanThu * 100 / (3000000L + player.TrieuHoiLevel * 1500000L)) + "%)"
                            + "\n|2|Kinh nghiệm: " + Util.format(player.TrieuHoiExpThanThu)
                            + "\nCấp bậc: " + player.NameThanthu(player.TrieuHoiCapBac)
                            + "\n|4|Thức ăn: " + player.TrieuHoiThucAn + "%"
                            + "\nSức Đánh: " + Util.getFormatNumber(player.TrieuHoiDame)
                            + "\nMáu: " + Util.getFormatNumber(player.TrieuHoiHP)
                            + "\nKĩ năng: " + player.TrieuHoiKiNang(player.TrieuHoiCapBac),
                    "Cập nhật", "Cho Ăn", "Đi theo", "Tấn công Boss", "Tấn công Quái",
                    "Về nhà", "Tự động cho ăn", "Nâng Cấp\nĐệ tử 2");
        } else {
            Service.gI().sendThongBaoOK(player, "Bạn chưa có đệ tử 2 để sài tính năng này.");
        }
    }

    public long exp_level1(long sucmanh) {
        if (sucmanh < 3000) {
            return 3000;
        } else if (sucmanh < 15000) {
            return 15000;
        } else if (sucmanh < 40000) {
            return 40000;
        } else if (sucmanh < 90000) {
            return 90000;
        } else if (sucmanh < 170000) {
            return 170000;
        } else if (sucmanh < 340000) {
            return 340000;
        } else if (sucmanh < 700000) {
            return 700000;
        } else if (sucmanh < 1500000) {
            return 1500000;
        } else if (sucmanh < 15000000) {
            return 15000000;
        } else if (sucmanh < 150000000) {
            return 150000000;
        } else if (sucmanh < 1500000000) {
            return 1500000000;
        } else if (sucmanh < 5000000000L) {
            return 5000000000L;
        } else if (sucmanh < 10000000000L) {
            return 10000000000L;
        } else if (sucmanh < 40000000000L) {
            return 40000000000L;
        } else if (sucmanh < 50010000000L) {
            return 50010000000L;
        } else if (sucmanh < 60010000000L) {
            return 60010000000L;
        } else if (sucmanh < 70010000000L) {
            return 70010000000L;
        } else if (sucmanh < 80010000000L) {
            return 80010000000L;
        } else if (sucmanh < 200010000000L) {
            return 200010000000L;
        }
        return 1000;
    }

    public void point(Player player) {
        player.nPoint.calPoint();
        Send_Info_NV(player);
        if (!player.isPet && !player.isBoss && !player.isNewPet && !player.isTrieuhoipet && !player.isClone) {
            Message msg;
            try {
                msg = new Message(-42);
                msg.writer().writeInt(player.nPoint.hpg);
                msg.writer().writeInt(player.nPoint.mpg);
                msg.writer().writeInt(player.nPoint.dameg);
                msg.writer().writeInt(player.nPoint.hpMax);// hp full
                msg.writer().writeInt(player.nPoint.mpMax);// mp full
                msg.writer().writeInt(player.nPoint.hp);// hp
                msg.writer().writeInt(player.nPoint.mp);// mp
                msg.writer().writeByte(player.nPoint.speed);// speed
                msg.writer().writeByte(20);
                msg.writer().writeByte(20);
                msg.writer().writeByte(1);
                msg.writer().writeInt(player.nPoint.dame);// dam base
                msg.writer().writeInt(player.nPoint.def);// def full
                msg.writer().writeByte(player.nPoint.crit);// crit full
                msg.writer().writeLong(player.nPoint.tiemNang);
                msg.writer().writeShort(100);
                msg.writer().writeInt(player.nPoint.defg);
                msg.writer().writeByte(player.nPoint.critg);
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
                Logger.logException(Service.class, e);
            }
        }
    }

    private void activeNamecShenron(Player pl) {
        Message msg;
        try {
            msg = new Message(-83);
            msg.writer().writeByte(0);

            msg.writer().writeShort(pl.zone.map.mapId);
            msg.writer().writeShort(pl.zone.map.bgId);
            msg.writer().writeByte(pl.zone.zoneId);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeUTF("");
            msg.writer().writeShort(pl.location.x);
            msg.writer().writeShort(pl.location.y);
            msg.writer().writeByte(1);
            // lastTimeShenronWait = System.currentTimeMillis();
            // isShenronAppear = true;

            Service.gI().sendMessAllPlayerInMap(pl, msg);
        } catch (Exception e) {

        }
    }

    public void player(Player pl) {
        if (pl == null) {
            return;
        }
        Message msg;
        try {
            msg = messageSubCommand((byte) 0);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(pl.playerTask.taskMain.id);
            msg.writer().writeByte(pl.gender);
            msg.writer().writeShort(pl.head);
            msg.writer().writeUTF(
                    pl.vip < 4 ? "" + pl.vip + "" + pl.name : pl.vip == 4 ? "" + pl.name : pl.name);
            msg.writer().writeByte(0); // cPK
            msg.writer().writeByte(pl.typePk);
            msg.writer().writeLong(pl.nPoint.power);
            msg.writer().writeShort(0);
            msg.writer().writeShort(0);
            msg.writer().writeByte(pl.gender);
            // --------skill---------

            ArrayList<Skill> skills = (ArrayList<Skill>) pl.playerSkill.skills;

            msg.writer().writeByte(pl.playerSkill.getSizeSkill());

            for (Skill skill : skills) {
                if (skill.skillId != -1) {
                    msg.writer().writeShort(skill.skillId);
                }
            }

            // ---vang---luong--luongKhoa
            if (pl.getSession().version >= 214) {
                msg.writer().writeLong(pl.inventory.gold);
            } else {
                msg.writer().writeInt((int) pl.inventory.gold);
            }
            msg.writer().writeInt(pl.inventory.ruby);
            msg.writer().writeInt(pl.inventory.gem);

            // --------itemBody---------
            ArrayList<Item> itemsBody = (ArrayList<Item>) pl.inventory.itemsBody;
            msg.writer().writeByte(itemsBody.size());
            for (Item item : itemsBody) {
                if (!item.isNotNullItem()) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    List<ItemOption> itemOptions = item.itemOptions;
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }
                }

            }

            // --------itemBag---------
            ArrayList<Item> itemsBag = (ArrayList<Item>) pl.inventory.itemsBag;
            msg.writer().writeByte(itemsBag.size());
            for (int i = 0; i < itemsBag.size(); i++) {
                Item item = itemsBag.get(i);
                if (!item.isNotNullItem()) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    List<ItemOption> itemOptions = item.itemOptions;
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }
                }

            }

            // --------itemBox---------
            ArrayList<Item> itemsBox = (ArrayList<Item>) pl.inventory.itemsBox;
            msg.writer().writeByte(itemsBox.size());
            for (int i = 0; i < itemsBox.size(); i++) {
                Item item = itemsBox.get(i);
                if (!item.isNotNullItem()) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    List<ItemOption> itemOptions = item.itemOptions;
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }
                }
            }
            // -----------------
            DataGame.sendHeadAvatar(msg);
            // -----------------
            msg.writer().writeShort(514); // char info id - con chim thông báo
            msg.writer().writeShort(515); // char info id
            msg.writer().writeShort(537); // char info id
            msg.writer().writeByte(pl.fusion.typeFusion != ConstPlayer.NON_FUSION ? 1 : 0); // nhập thể
            // msg.writer().writeInt(1632811835); //deltatime
            msg.writer().writeInt(333); // deltatime
            msg.writer().writeByte(pl.isNewMember ? 1 : 0); // is new member

            msg.writer().writeShort(pl.getAura()); // idauraeff
            msg.writer().writeByte(pl.getEffFront());

            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public Message messageNotLogin(byte command) throws IOException {
        Message ms = new Message(-29);
        ms.writer().writeByte(command);
        return ms;
    }

    public Message messageNotMap(byte command) throws IOException {
        Message ms = new Message(-28);
        ms.writer().writeByte(command);
        return ms;
    }

    public Message messageSubCommand(byte command) throws IOException {
        Message ms = new Message(-30);
        ms.writer().writeByte(command);
        return ms;
    }

    public void addSMTN(Player player, byte type, long param, boolean isOri) {
        if (player.isPet) {
            long paramTemp = param;
            paramTemp = player.nPoint.calSubTNSM_DT(paramTemp);
            player.nPoint.powerUp(paramTemp);
            player.nPoint.tiemNangUp(param);

            Player master = ((Pet) player).master;
            param = master.nPoint.calSubTNSM(param);
            master.nPoint.powerUp(param);
            master.nPoint.tiemNangUp(param);
            addSMTN(master, type, param, true);
        } else if (player.isTrieuhoipet) {
            player.nPoint.powerUp(param);
            player.nPoint.tiemNangUp(param);
            Player masterr = ((Thu_TrieuHoi) player).masterr;

            param = masterr.nPoint.calSubTNSM(param);
            masterr.nPoint.powerUp(param);
            masterr.nPoint.tiemNangUp(param);
            addSMTN(masterr, type, param, true);
        } else {
            switch (type) {
                case 1:
                    player.nPoint.tiemNangUp(param);
                    break;
                case 2:
                    player.nPoint.powerUp(param);
                    player.nPoint.tiemNangUp(param);
                    break;
                default:
                    player.nPoint.powerUp(param);
                    break;
            }
            PlayerService.gI().sendTNSM(player, type, param);
            if (isOri) {
                if (player.clan != null) {
                    player.clan.addSMTNClan(player, param);
                }
            }
        }
    }

    // public void congTiemNang(Player pl, byte type, int tiemnang) {
    // Message msg;
    // try {
    // msg = new Message(-3);
    // msg.writer().writeByte(type);// 0 là cộng sm, 1 cộng tn, 2 là cộng cả 2
    // msg.writer().writeInt(tiemnang);// số tn cần cộng
    // if (!pl.isPet) {
    // pl.sendMessage(msg);
    // } else {
    // ((Pet) pl).master.nPoint.powerUp(tiemnang);
    // ((Pet) pl).master.nPoint.tiemNangUp(tiemnang);
    // ((Pet) pl).master.sendMessage(msg);
    // }
    // msg.cleanup();
    // switch (type) {
    // case 1:
    // pl.nPoint.tiemNangUp(tiemnang);
    // break;
    // case 2:
    // pl.nPoint.powerUp(tiemnang);
    // pl.nPoint.tiemNangUp(tiemnang);
    // break;
    // default:
    // pl.nPoint.powerUp(tiemnang);
    // break;
    // }
    // } catch (Exception e) {
    //
    // }
    // }
    public String get_HanhTinh(int hanhtinh) {
        switch (hanhtinh) {
            case 0:
                return "Trái Đất";
            case 1:
                return "Namếc";
            case 2:
                return "Xayda";
            default:
                return "";
        }
    }

    public void sendPetFollow(Player player, short smallId) {
        Message msg = new Message(31);
        try {
            msg.writer().writeInt((int) player.id);
            if (smallId == 0) {
                msg.writer().writeByte(0);
            } else {
                msg.writer().writeByte(1);
                msg.writer().writeShort(smallId);
                msg.writer().writeByte(1);

                int[] fr = getImageFrames(smallId);
                msg.writer().writeByte(fr.length);
                for (int i = 0; i < fr.length; i++) {
                    msg.writer().writeByte(fr[i]);
                }

                int[] dimensions = getImageDimensions(smallId);
                msg.writer().writeShort(dimensions[0]);
                msg.writer().writeShort(dimensions[1]);
            }
            sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace(); // Thêm lệnh in ra lỗi để debug
        }
    }

    private int[] getImageFrames(short smallId) {
        switch (smallId) {
            case 16046:
            case 16048:
            case 16050:
            case 16052:
            case 16054:
            case 16056:
            case 16058:
            case 16060:
            case 16062:
            case 16064:
                return new int[] { 0, 1, 2 };
            case 16066:
            case 16068:
            case 16070:
                return new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
            case 16072:
            case 16074:
            case 16076:
            case 16078:
                return new int[] { 0, 1, 2 };
            case 16080:
                return new int[] { 0, 1, 2, 3, 4 };
            case 16082:
                return new int[] { 0, 1, 2, 3, 4, 5 };
            case 16084:
                return new int[] { 0, 1, 2, 3, 4, 5 };
            case 16086:
                return new int[] { 0, 1, 2, 3, 4, 5 };
            case 16088:
            case 16090:
            case 16092:
            case 16094:
            case 16096:
            case 16098:
            case 16100:
            case 16102:
            case 16104:
                return new int[] { 0, 1, 2, 3 };
            case 16106:
                return new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
            case 16108:
            case 16110:
            case 16112:
                return new int[] { 0, 1, 2, 3, 4 };
            default:
                return new int[] { 0, 1, 2, 3, 4, 5, 6, 7 };
        }
    }

    private int[] getImageDimensions(short smallId) {
        switch (smallId) {
            case 16046:
            case 16048:
            case 16050:
            case 16052:
            case 16054:
            case 16056:
            case 16058:
            case 16060:
            case 16062:
            case 16064:
                return new int[] { 45, 50 };
            case 16066:
            case 16068:
            case 16070:
                return new int[] { 32, 40 };
            case 16072:
                return new int[] { 22, 21 };
            case 16074:
                return new int[] { 19, 21 };
            case 16076:
                return new int[] { 26, 27 };
            case 16078:
                return new int[] { 20, 20 };
            case 16080:
                return new int[] { 64, 64 };
            case 16082:
                return new int[] { 46, 62 };
            case 16084:
                return new int[] { 31, 29 };
            case 16086:
                return new int[] { 32, 36 };
            case 16088:
            case 16090:
            case 16092:
            case 16094:
            case 16096:
            case 16098:
            case 16100:
            case 16102:
            case 16104:
                return new int[] { 20, 20 };
            case 16106:
                return new int[] { 20, 20 };
            case 16108:
            case 16110:
            case 16112:
                return new int[] { 20, 20 };
            default:
                return new int[] { smallId == 15067 ? 65 : 75, smallId == 15067 ? 65 : 75 };
        }
    }

    public String getCurrStrLevel(Player pl) {
        long sucmanh = pl.nPoint.power;
        if (sucmanh < 3000) {
            return "Tân thủ";
        } else if (sucmanh < 15000) {
            return "Tập sự sơ cấp";
        } else if (sucmanh < 40000) {
            return "Tập sự trung cấp";
        } else if (sucmanh < 90000) {
            return "Tập sự cao cấp";
        } else if (sucmanh < 170000) {
            return "Tân binh";
        } else if (sucmanh < 340000) {
            return "Chiến binh";
        } else if (sucmanh < 700000) {
            return "Chiến binh cao cấp";
        } else if (sucmanh < 1500000) {
            return "Vệ binh";
        } else if (sucmanh < 15000000) {
            return "Vệ binh hoàng gia";
        } else if (sucmanh < 150000000) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 1";
        } else if (sucmanh < 1500000000) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 2";
        } else if (sucmanh < 5000000000L) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 3";
        } else if (sucmanh < 10000000000L) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 4";
        } else if (sucmanh < 40000000000L) {
            return "Thần " + get_HanhTinh(pl.gender) + " cấp 1";
        } else if (sucmanh < 50010000000L) {
            return "Thần " + get_HanhTinh(pl.gender) + " cấp 2";
        } else if (sucmanh < 60010000000L) {
            return "Thần " + get_HanhTinh(pl.gender) + " cấp 3";
        } else if (sucmanh < 70010000000L) {
            return "Giới Vương Thần cấp 11";
        } else if (sucmanh < 80010000000L) {
            return "Giới Vương Thần cấp 2";
        } else if (sucmanh < 100010000000L) {
            return "Giới Vương Thần cấp 3";
        } else if (sucmanh < 150010000000L) {
            return "Vương Diệt Thần";
        } else if (sucmanh < 21100010000000L) {
            return "Hạn Diệt Thần";
        }
        return "Thần Huỷ Diệt cấp 2";
    }

    public int getCurrLevel(Player pl) {
        if (pl != null && pl.nPoint != null) {
            long sucmanh = pl.nPoint.power;
            if (sucmanh < 3000) {
                return 1;
            } else if (sucmanh < 15000) {
                return 2;
            } else if (sucmanh < 40000) {
                return 3;
            } else if (sucmanh < 90000) {
                return 4;
            } else if (sucmanh < 170000) {
                return 5;
            } else if (sucmanh < 340000) {
                return 6;
            } else if (sucmanh < 700000) {
                return 7;
            } else if (sucmanh < 1500000) {
                return 8;
            } else if (sucmanh < 15000000) {
                return 9;
            } else if (sucmanh < 150000000) {
                return 10;
            } else if (sucmanh < 1500000000) {
                return 11;
            } else if (sucmanh < 5000000000L) {
                return 12;
            } else if (sucmanh < 10000000000L) {
                return 13;
            } else if (sucmanh < 40000000000L) {
                return 14;
            } else if (sucmanh < 50010000000L) {
                return 15;
            } else if (sucmanh < 60010000000L) {
                return 16;
            } else if (sucmanh < 70010000000L) {
                return 17;
            } else if (sucmanh < 80010000000L) {
                return 18;
            } else if (sucmanh < 100010000000L) {
                return 19;
            } else if (sucmanh < 150010000000L) {
                return 20;
            } else if (sucmanh < 2100010000000L) {
                return 21;
            }
        }
        return 21;
    }

    public void hsChar(Player pl, int hp, int mp) {
        Message msg;
        try {
            pl.setJustRevivaled();
            pl.nPoint.setHp(hp);
            pl.nPoint.setMp(mp);
            if (!pl.isPet && !pl.isNewPet && !pl.isClone) {
                msg = new Message(-16);
                pl.sendMessage(msg);
                msg.cleanup();
                PlayerService.gI().sendInfoHpMpMoney(pl);
            }

            msg = messageSubCommand((byte) 15);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeInt(hp);
            msg.writer().writeInt(mp);
            msg.writer().writeShort(pl.location.x);
            msg.writer().writeShort(pl.location.y);
            sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();

            Send_Info_NV(pl);
            PlayerService.gI().sendInfoHpMp(pl);
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void charDie(Player pl) {
        Message msg;
        try {
            if (!pl.isPet && !pl.isNewPet && !pl.isClone) {
                msg = new Message(-17);
                msg.writer().writeByte((int) pl.id);
                msg.writer().writeShort(pl.location.x);
                msg.writer().writeShort(pl.location.y);
                pl.sendMessage(msg);
                msg.cleanup();
            } else if (pl.isPet) {
                ((Pet) pl).lastTimeDie = System.currentTimeMillis();
            } else if (pl.isTrieuhoipet) {
                ((Thu_TrieuHoi) pl).LasttimeHs = System.currentTimeMillis();
            }
            // if (!pl.isPet && !pl.isClone && !pl.isBoss && pl.idNRNM != -1) {
            // ItemMap itemMap = new ItemMap(pl.zone, pl.idNRNM, 1, pl.location.x,
            // pl.location.y, -1);
            // Service.gI().dropItemMap(pl.zone, itemMap);
            // NgocRongNamecService.gI().pNrNamec[pl.idNRNM - 353] = "";
            // NgocRongNamecService.gI().idpNrNamec[pl.idNRNM - 353] = -1;
            // pl.idNRNM = -1;
            // PlayerService.gI().changeAndSendTypePK(pl, ConstPlayer.NON_PK);
            // Service.gI().sendFlagBag(pl);
            // }
            if (pl.zone.map.mapId == 51) {
                ChangeMapService.gI().changeMapBySpaceShip(pl, 21 + pl.gender, 0, -1);
            }
            msg = new Message(-8);
            msg.writer().writeShort((int) pl.id);
            msg.writer().writeByte(0); // cpk
            msg.writer().writeShort(pl.location.x);
            msg.writer().writeShort(pl.location.y);
            sendMessAnotherNotMeInMap(pl, msg);
            msg.cleanup();

            // Send_Info_NV(pl);
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void attackMob(Player pl, int mobId) {
        if (pl != null && pl.zone != null) {
            for (Mob mob : pl.zone.mobs) {
                if (mob.id == mobId) {
                    SkillService.gI().useSkill(pl, null, mob, null);
                    break;
                }
            }
        }
    }

    public void Send_Caitrang(Player player) {
        if (player != null) {
            Message msg;
            try {
                msg = new Message(-90);
                msg.writer().writeByte(1);// check type
                msg.writer().writeInt((int) player.id); // id player
                short head = player.getHead();
                short body = player.getBody();
                short leg = player.getLeg();
                msg.writer().writeShort(head);// set head
                msg.writer().writeShort(body);// setbody
                msg.writer().writeShort(leg);// set leg
                msg.writer().writeByte(player.effectSkill.isMonkey ? 1 : 0);// set khỉ
                msg.writer().writeByte(player.effectSkill.isTranformation ? 1 : 0);// set khỉ
                msg.writer().writeByte(player.effectSkill.isEvolution ? 1 : 0);// set khỉ
                sendMessAllPlayerInMap(player, msg);
                msg.cleanup();
            } catch (Exception e) {
                Logger.logException(Service.class, e);
            }
        }
    }

    public void SendOutfit_Special(Player player, int head) {
        if (player != null) {
            Message msg;
            try {
                msg = new Message(-90);
                msg.writer().writeByte(0);// check type
                msg.writer().writeInt((int) player.id); // id player
                short body = player.getBody();
                short leg = player.getLeg();
                msg.writer().writeShort(head);// set head
                msg.writer().writeShort(body);// setbody
                msg.writer().writeShort(leg);// set leg
                msg.writer().writeByte(0);// set khỉ
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
            }
        }
    }

    public void SendOutfit(Player player) {
        if (player != null) {
            Message msg;
            try {
                msg = new Message(-90);
                msg.writer().writeByte(1);// check type
                msg.writer().writeInt((int) player.id); // id player
                short head = player.getHead();
                short body = player.getBody();
                short leg = player.getLeg();
                msg.writer().writeShort(head);// set head
                msg.writer().writeShort(body);// setbody
                msg.writer().writeShort(leg);// set leg
                msg.writer().writeByte(player.effectSkill.isMonkey ? 1 : 0);// set khỉ
                sendMessAllPlayerInMap(player, msg);
                msg.cleanup();
            } catch (Exception e) {
            }
        }
    }

    public void Send_Caitrang1(Player player) {
        if (player != null) {
            Message msg;
            try {
                msg = new Message(-90);
                msg.writer().writeByte(1);// check type
                msg.writer().writeInt((int) player.id); // id player
                msg.writer().writeShort(1575);// set head
                msg.writer().writeShort(1576);// setbody
                msg.writer().writeShort(1577);// set leg
                msg.writer().writeByte(player.effectSkill.isMonkey ? 1 : 0);// set khỉ
                sendMessAllPlayerInMap(player, msg);
                msg.cleanup();
            } catch (Exception e) {
                Logger.logException(Service.class, e);
            }
        }
    }

    public void Send_Caitrang2(Player player) {
        if (player != null) {
            Message msg;
            try {
                msg = new Message(-90);
                msg.writer().writeByte(1);// check type
                msg.writer().writeInt((int) player.id); // id player
                msg.writer().writeShort(1578);// set head
                msg.writer().writeShort(1579);// setbody
                msg.writer().writeShort(1580);// set leg
                msg.writer().writeByte(player.effectSkill.isMonkey ? 1 : 0);// set khỉ
                sendMessAllPlayerInMap(player, msg);
                msg.cleanup();
            } catch (Exception e) {
                Logger.logException(Service.class, e);
            }
        }
    }

    public void Send_Caitrang3(Player player) {
        if (player != null) {
            Message msg;
            try {
                msg = new Message(-90);
                msg.writer().writeByte(1);// check type
                msg.writer().writeInt((int) player.id); // id player
                msg.writer().writeShort(1674);// set head
                msg.writer().writeShort(1675);// setbody
                msg.writer().writeShort(1676);// set leg
                msg.writer().writeByte(player.effectSkill.isMonkey ? 1 : 0);// set khỉ
                sendMessAllPlayerInMap(player, msg);
                msg.cleanup();
            } catch (Exception e) {
                Logger.logException(Service.class, e);
            }
        }
    }

    public void Send_Caitrang4(Player player) {
        if (player != null) {
            Message msg;
            try {
                msg = new Message(-90);
                msg.writer().writeByte(1);// check type
                msg.writer().writeInt((int) player.id); // id player
                msg.writer().writeShort(1680);// set head
                msg.writer().writeShort(1681);// setbody
                msg.writer().writeShort(1682);// set leg
                msg.writer().writeByte(player.effectSkill.isMonkey ? 1 : 0);// set khỉ
                sendMessAllPlayerInMap(player, msg);
                msg.cleanup();
            } catch (Exception e) {
                Logger.logException(Service.class, e);
            }
        }
    }

    public void Send_Caitrang5(Player player) {
        if (player != null) {
            Message msg;
            try {
                msg = new Message(-90);
                msg.writer().writeByte(1);// check type
                msg.writer().writeInt((int) player.id); // id player
                msg.writer().writeShort(1683);// set head
                msg.writer().writeShort(1684);// setbody
                msg.writer().writeShort(1685);// set leg
                msg.writer().writeByte(player.effectSkill.isMonkey ? 1 : 0);// set khỉ
                sendMessAllPlayerInMap(player, msg);
                msg.cleanup();
            } catch (Exception e) {
                Logger.logException(Service.class, e);
            }
        }
    }

    public void Send_Caitrang6(Player player) {
        if (player != null) {
            Message msg;
            try {
                msg = new Message(-90);
                msg.writer().writeByte(1);// check type
                msg.writer().writeInt((int) player.id); // id player
                msg.writer().writeShort(1686);// set head
                msg.writer().writeShort(1687);// setbody
                msg.writer().writeShort(1688);// set leg
                msg.writer().writeByte(player.effectSkill.isMonkey ? 1 : 0);// set khỉ
                sendMessAllPlayerInMap(player, msg);
                msg.cleanup();
            } catch (Exception e) {
                Logger.logException(Service.class, e);
            }
        }
    }

    public void Send_Caitrang7(Player player) {
        if (player != null) {
            Message msg;
            try {
                msg = new Message(-90);
                msg.writer().writeByte(1);// check type
                msg.writer().writeInt((int) player.id); // id player
                msg.writer().writeShort(1566);// set head
                msg.writer().writeShort(1567);// setbody
                msg.writer().writeShort(1568);// set leg
                msg.writer().writeByte(player.effectSkill.isMonkey ? 1 : 0);// set khỉ
                sendMessAllPlayerInMap(player, msg);
                msg.cleanup();
            } catch (Exception e) {
                Logger.logException(Service.class, e);
            }
        }
    }

    public void Send_Caitrang8(Player player) {
        if (player != null) {
            Message msg;
            try {
                msg = new Message(-90);
                msg.writer().writeByte(1);// check type
                msg.writer().writeInt((int) player.id); // id player
                msg.writer().writeShort(1569);// set head
                msg.writer().writeShort(1570);// setbody
                msg.writer().writeShort(1571);// set leg
                msg.writer().writeByte(player.effectSkill.isMonkey ? 1 : 0);// set khỉ
                sendMessAllPlayerInMap(player, msg);
                msg.cleanup();
            } catch (Exception e) {
                Logger.logException(Service.class, e);
            }
        }
    }

    public void Send_Caitrang9(Player player) {
        if (player != null) {
            Message msg;
            try {
                msg = new Message(-90);
                msg.writer().writeByte(1);// check type
                msg.writer().writeInt((int) player.id); // id player
                msg.writer().writeShort(1572);// set head
                msg.writer().writeShort(1573);// setbody
                msg.writer().writeShort(1574);// set leg
                msg.writer().writeByte(player.effectSkill.isMonkey ? 1 : 0);// set khỉ
                sendMessAllPlayerInMap(player, msg);
                msg.cleanup();
            } catch (Exception e) {
                Logger.logException(Service.class, e);
            }
        }
    }

    public void Send_Caitrang10(Player player) {
        if (player != null) {
            Message msg;
            try {
                msg = new Message(-90);
                msg.writer().writeByte(1);// check type
                msg.writer().writeInt((int) player.id); // id player
                msg.writer().writeShort(1234);// set head
                msg.writer().writeShort(1235);// setbody
                msg.writer().writeShort(1236);// set leg
                msg.writer().writeByte(player.effectSkill.isMonkey ? 1 : 0);// set khỉ
                sendMessAllPlayerInMap(player, msg);
                msg.cleanup();
            } catch (Exception e) {
                Logger.logException(Service.class, e);
            }
        }
    }

    public void Send_Caitrang11(Player player) {
        if (player != null) {
            Message msg;
            try {
                msg = new Message(-90);
                msg.writer().writeByte(1);// check type
                msg.writer().writeInt((int) player.id); // id player
                msg.writer().writeShort(1237);// set head
                msg.writer().writeShort(1238);// setbody
                msg.writer().writeShort(1239);// set leg
                msg.writer().writeByte(player.effectSkill.isMonkey ? 1 : 0);// set khỉ
                sendMessAllPlayerInMap(player, msg);
                msg.cleanup();
            } catch (Exception e) {
                Logger.logException(Service.class, e);
            }
        }
    }

    public void setNotMonkey(Player player) {
        Message msg;
        try {
            msg = new Message(-90);
            msg.writer().writeByte(-1);
            msg.writer().writeInt((int) player.id);
            Service.gI().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void sendFlagBag(Player pl) {
        try {
            if (pl.getFlagBag() == 252 || pl.getFlagBag() == 246 || pl.getFlagBag() == 242 || pl.getFlagBag() == 205
                    || pl.getFlagBag() == 209) {
                removeTitle(pl);
                Thread.sleep(500);
            }
            Message msg = new Message(-64);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(pl.getFlagBag());
            sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();
        } catch (IOException | InterruptedException e) {
        }
    }

    public void sendThongBaoOK(Player pl, String text) {
        if (pl.isPet || pl.isNewPet || pl.isClone || pl.isTrieuhoipet) {
            return;
        }
        Message msg;
        try {
            msg = new Message(-26);
            msg.writer().writeUTF(text);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void sendThongBaoOK(MySession session, String text) {
        Message msg;
        try {
            msg = new Message(-26);
            msg.writer().writeUTF(text);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void sendThongBaoAllPlayer(String thongBao) {
        Message msg;
        try {
            msg = new Message(-25);
            msg.writer().writeUTF(thongBao);
            this.sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void ChatAll(int iconId, String text) {
        Message msg;
        try {
            msg = new Message(-70);
            msg.writer().writeShort(iconId);
            msg.writer().writeUTF(text);
            msg.writer().writeByte(0);
            this.sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendBangThongBaoAllPlayervip(String thongBao) {
        Message msg;
        try {
            msg = new Message(-26);
            msg.writer().writeUTF(thongBao);
            this.sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void sendBigMessage(Player player, int iconId, String text) {
        try {
            Message msg;
            msg = new Message(-70);
            msg.writer().writeShort(iconId);
            msg.writer().writeUTF(text);
            msg.writer().writeByte(0);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {

        }
    }

    public void sendThongBaoFromAdmin(Player player, String text) {
        sendBigMessage(player, 11061, text);
    }

    public void sendThongBao(Player pl, String thongBao) {
        Message msg;
        try {
            msg = new Message(-25);
            msg.writer().writeUTF(thongBao);
            pl.sendMessage(msg);
            msg.cleanup();

        } catch (Exception e) {

        }
    }

    public void sendThongBaoBenDuoi(String text) {
        Message msg = null;
        try {
            msg = new Message(93);
            msg.writer().writeUTF(text);
            sendMessAllPlayer(msg);
        } catch (Exception e) {

        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }

    public void sendThongBao(List<Player> pl, String thongBao) {
        for (int i = 0; i < pl.size(); i++) {
            Player ply = pl.get(i);
            if (ply != null) {
                this.sendThongBao(ply, thongBao);
            }
        }
    }

    public void sendMoney(Player pl) {
        Message msg;
        try {
            msg = new Message(6);
            if (pl.getSession().version >= 214) {
                msg.writer().writeLong(pl.inventory.gold);
            } else {
                msg.writer().writeInt((int) pl.inventory.gold);
            }
            msg.writer().writeInt(pl.inventory.gem);
            msg.writer().writeInt(pl.inventory.ruby);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void sendToAntherMePickItem(Player player, int itemMapId) {
        Message msg;
        try {
            msg = new Message(-19);
            msg.writer().writeShort(itemMapId);
            msg.writer().writeInt((int) player.id);
            sendMessAnotherNotMeInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public static final int[] flagTempId = { 363, 364, 365, 366, 367, 368, 369, 370, 371, 519, 520, 747 };
    public static final int[] flagIconId = { 2761, 2330, 2323, 2327, 2326, 2324, 2329, 2328, 2331, 4386, 4385, 2325 };

    public void openFlagUI(Player pl) {
        Message msg;
        try {
            msg = new Message(-103);
            msg.writer().writeByte(0);
            msg.writer().writeByte(flagTempId.length);
            for (int i = 0; i < flagTempId.length; i++) {
                msg.writer().writeShort(flagTempId[i]);
                msg.writer().writeByte(1);
                switch (flagTempId[i]) {
                    case 363:
                        msg.writer().writeByte(73);
                        msg.writer().writeShort(0);
                        break;
                    case 371:
                        msg.writer().writeByte(88);
                        msg.writer().writeShort(10);
                        break;
                    default:
                        msg.writer().writeByte(88);
                        msg.writer().writeShort(5);
                        break;
                }
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void changeFlag(Player pl, int index) {
        Message msg;
        try {
            pl.cFlag = (byte) index;
            msg = new Message(-103);
            msg.writer().writeByte(1);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(index);
            Service.gI().sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();

            msg = new Message(-103);
            msg.writer().writeByte(2);
            msg.writer().writeByte(index);
            msg.writer().writeShort(flagIconId[index]);
            Service.gI().sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();

            if (pl.pet != null) {
                pl.pet.cFlag = (byte) index;
                msg = new Message(-103);
                msg.writer().writeByte(1);
                msg.writer().writeInt((int) pl.pet.id);
                msg.writer().writeByte(index);
                Service.gI().sendMessAllPlayerInMap(pl.pet, msg);
                msg.cleanup();

                msg = new Message(-103);
                msg.writer().writeByte(2);
                msg.writer().writeByte(index);
                msg.writer().writeShort(flagIconId[index]);
                Service.gI().sendMessAllPlayerInMap(pl.pet, msg);
                msg.cleanup();
            }
            pl.iDMark.setLastTimeChangeFlag(System.currentTimeMillis());
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void sendFlagPlayerToMe(Player me, Player pl) {
        Message msg;
        try {
            msg = new Message(-103);
            msg.writer().writeByte(2);
            msg.writer().writeByte(pl.cFlag);
            msg.writer().writeShort(flagIconId[pl.cFlag]);
            me.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void chooseFlag(Player pl, int index) {
        if (MapService.gI().isMapBlackBallWar(pl.zone.map.mapId) || MapService.gI().isMapMaBu(pl.zone.map.mapId)
                || MapService.gI().isMapPVP(pl.zone.map.mapId)) {
            sendThongBao(pl, "Không thể đổi cờ lúc này!");
            return;
        }
        if (Util.canDoWithTime(pl.iDMark.getLastTimeChangeFlag(), 60000)) {
            changeFlag(pl, index);
        } else {
            sendThongBao(pl, "Không thể đổi cờ lúc này! Vui lòng đợi "
                    + TimeUtil.getTimeLeft(pl.iDMark.getLastTimeChangeFlag(), 60) + " nữa!");
        }
    }

    public void attackPlayer(Player pl, int idPlAnPem) {
        if (pl.zone != null) {
            SkillService.gI().useSkill(pl, pl.zone.getPlayerInMap(idPlAnPem), null, null);
        }
    }

    public void releaseCooldownSkill(Player pl) {
        Message msg;
        try {
            msg = new Message(-94);
            for (Skill skill : pl.playerSkill.skills) {
                skill.coolDown = 0;
                msg.writer().writeShort(skill.skillId);
                int leftTime = (int) (skill.lastTimeUseThisSkill + skill.coolDown - System.currentTimeMillis());
                if (leftTime < 0) {
                    leftTime = 0;
                }
                msg.writer().writeInt(leftTime);
            }
            pl.sendMessage(msg);
            pl.nPoint.setMp(pl.nPoint.mpMax);
            PlayerService.gI().sendInfoHpMpMoney(pl);
            msg.cleanup();

        } catch (Exception e) {

        }
    }

    public void sendTimeSkill(Player pl) {
        Message msg;
        try {
            msg = new Message(-94);
            for (Skill skill : pl.playerSkill.skills) {
                msg.writer().writeShort(skill.skillId);
                int timeLeft = (int) (skill.lastTimeUseThisSkill + skill.coolDown - System.currentTimeMillis());
                if (timeLeft < 0) {
                    timeLeft = 0;
                }
                msg.writer().writeInt(timeLeft);
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void dropItemMap(Zone zone, ItemMap item) {
        Message msg;
        try {
            msg = new Message(68);
            msg.writer().writeShort(item.itemMapId);
            msg.writer().writeShort(item.itemTemplate.id);
            msg.writer().writeShort(item.x);
            msg.writer().writeShort(item.y);
            msg.writer().writeInt(3);//
            sendMessAllPlayerInMap(zone, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void dropItemMapForMe(Player player, ItemMap item) {
        Message msg;
        try {
            msg = new Message(68);
            msg.writer().writeShort(item.itemMapId);
            msg.writer().writeShort(item.itemTemplate.id);
            msg.writer().writeShort(item.x);
            msg.writer().writeShort(item.y);
            msg.writer().writeInt(3);//
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void showInfoPet(Player pl) {
        if (pl != null && pl.pet != null) {
            Message msg;
            try {
                msg = new Message(-107);
                msg.writer().writeByte(2);
                msg.writer().writeShort(pl.pet.getAvatar());
                msg.writer().writeByte(pl.pet.inventory.itemsBody.size());

                for (Item item : pl.pet.inventory.itemsBody) {
                    if (!item.isNotNullItem()) {
                        msg.writer().writeShort(-1);
                    } else {
                        msg.writer().writeShort(item.template.id);
                        msg.writer().writeInt(item.quantity);
                        msg.writer().writeUTF(item.getInfo());
                        msg.writer().writeUTF(item.getContent());

                        int countOption = item.itemOptions.size();
                        msg.writer().writeByte(countOption);
                        for (ItemOption iop : item.itemOptions) {
                            msg.writer().writeByte(iop.optionTemplate.id);
                            msg.writer().writeShort(iop.param);
                        }
                    }
                }

                msg.writer().writeInt(pl.pet.nPoint.hp); // hp
                msg.writer().writeInt(pl.pet.nPoint.hpMax); // hpfull
                msg.writer().writeInt(pl.pet.nPoint.mp); // mp
                msg.writer().writeInt(pl.pet.nPoint.mpMax); // mpfull
                msg.writer().writeInt(pl.pet.nPoint.dame); // damefull
                msg.writer().writeUTF(pl.pet.name); // name
                msg.writer().writeUTF(getCurrStrLevel(pl.pet)); // curr level
                msg.writer().writeLong(pl.pet.nPoint.power); // power
                msg.writer().writeLong(pl.pet.nPoint.tiemNang); // tiềm năng
                msg.writer().writeByte(pl.pet.getStatus()); // status
                msg.writer().writeShort(pl.pet.nPoint.stamina); // stamina
                msg.writer().writeShort(pl.pet.nPoint.maxStamina); // stamina full
                msg.writer().writeByte(pl.pet.nPoint.crit); // crit
                msg.writer().writeShort(Util.maxShort(pl.pet.nPoint.def)); // def
                int sizeSkill = pl.pet.playerSkill.skills.size();
                msg.writer().writeByte(5); // counnt pet skill
                for (int i = 0; i < pl.pet.playerSkill.skills.size(); i++) {
                    if (pl.pet.playerSkill.skills.get(i).skillId != -1) {
                        msg.writer().writeShort(pl.pet.playerSkill.skills.get(i).skillId);
                    } else {
                        switch (i) {
                            case 1:
                                msg.writer().writeShort(-1);
                                msg.writer().writeUTF("Cần đạt sức mạnh 150tr để mở");
                                break;
                            case 2:
                                msg.writer().writeShort(-1);
                                msg.writer().writeUTF("Cần đạt sức mạnh 1tỷ5 để mở");
                                break;
                            case 3:
                                msg.writer().writeShort(-1);
                                msg.writer().writeUTF("Cần đạt sức mạnh 20tỷ\nđể mở");
                                break;
                            default:
                                msg.writer().writeShort(-1);
                                msg.writer().writeUTF("Cần đạt sức mạnh 60tỷ\nđể mở");
                                break;
                        }
                    }
                }

                pl.sendMessage(msg);
                msg.cleanup();

            } catch (Exception e) {
                Logger.logException(Service.class, e);
            }
        }
    }

    public void sendSpeedPlayer(Player pl, int speed) {
        Message msg;
        try {
            msg = Service.gI().messageSubCommand((byte) 8);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(speed != -1 ? speed : pl.nPoint.speed);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void setPos(Player player, int x, int y) {
        player.location.x = x;
        player.location.y = y;
        Message msg;
        try {
            msg = new Message(123);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(x);
            msg.writer().writeShort(y);
            msg.writer().writeByte(1);
            sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void getPlayerMenu(Player player, int playerId) {
        Message msg;
        try {
            msg = new Message(-79);
            Player pl = player.zone.getPlayerInMap(playerId);
            if (pl != null) {
                msg.writer().writeInt(playerId);
                msg.writer().writeLong(pl.nPoint.power);
                msg.writer().writeUTF(Service.gI().getCurrStrLevel(pl));
                player.sendMessage(msg);
            }
            msg.cleanup();
            if (player.isAdmin()) {
                SubMenuService.gI().showMenuForAdmin(player);
            }
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void hideWaitDialog(Player pl) {
        Message msg;
        try {
            msg = new Message(-99);
            msg.writer().writeByte(-1);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void chatPrivate(Player plChat, Player plReceive, String text) {
        Message msg;
        try {
            msg = new Message(92);
            msg.writer().writeUTF(plChat.name);
            msg.writer().writeUTF("|7|" + text);
            msg.writer().writeInt((int) plChat.id);
            msg.writer().writeShort(plChat.getHead());
            msg.writer().writeShort(-1);
            msg.writer().writeShort(plChat.getBody());
            msg.writer().writeShort(plChat.getFlagBag()); // bag
            msg.writer().writeShort(plChat.getLeg());
            msg.writer().writeByte(1);
            plChat.sendMessage(msg);
            plReceive.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void changePassword(Player player, String oldPass, String newPass, String rePass) {
        if (player.getSession().pp.equals(oldPass)) {
            if (newPass.length() >= 5) {
                if (newPass.equals(rePass)) {
                    player.getSession().pp = newPass;
                    try {
                        GirlkunDB.executeUpdate("update account set password = ? where id = ? and username = ?",
                                rePass, player.getSession().userId, player.getSession().uu);
                        Service.gI().sendThongBao(player, "Đổi mật khẩu thành công!");
                    } catch (Exception ex) {
                        Service.gI().sendThongBao(player, "Đổi mật khẩu thất bại!");
                        Logger.logException(Service.class, ex);
                    }
                } else {
                    Service.gI().sendThongBao(player, "Mật khẩu nhập lại không đúng!");
                }
            } else {
                Service.gI().sendThongBao(player, "Mật khẩu ít nhất 5 ký tự!");
            }
        } else {
            Service.gI().sendThongBao(player, "Mật khẩu cũ không đúng!");
        }
    }

    public void switchToCreateChar(MySession session) {
        Message msg;
        try {
            msg = new Message(2);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void sendCaption(MySession session, byte gender) {
        Message msg;
        try {
            msg = new Message(-41);
            msg.writer().writeByte(Manager.CAPTIONS.size());
            for (String caption : Manager.CAPTIONS) {
                msg.writer().writeUTF(caption.replaceAll("%1", gender == ConstPlayer.TRAI_DAT ? "Trái đất"
                        : (gender == ConstPlayer.NAMEC ? "Namếc" : "Xayda")));
            }
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void sendHavePet(Player player) {
        Message msg;
        try {
            msg = new Message(-107);
            msg.writer().writeByte(player.pet == null ? 0 : 1);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void sendWaitToLogin(MySession session, int secondsWait) {
        Message msg;
        try {
            msg = new Message(122);
            msg.writer().writeShort(secondsWait);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void sendMessage(MySession session, int cmd, String path) {
        Message msg;
        try {
            msg = new Message(cmd);
            msg.writer().write(FileIO.readFile(path));
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void createItemMap(Player player, int tempId) {
        ItemMap itemMap = new ItemMap(player.zone, tempId, 1, player.location.x, player.location.y, player.id);
        dropItemMap(player.zone, itemMap);
    }

    public void sendNangDong(Player player) {
        Message msg;
        try {
            msg = new Message(-97);
            msg.writer().writeInt(100);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void setClientType(MySession session, Message msg) {
        try {
            session.typeClient = (msg.reader().readByte());// client_type
            session.zoomLevel = msg.reader().readByte();// zoom_level
            msg.reader().readBoolean();// is_gprs
            msg.reader().readInt();// width
            msg.reader().readInt();// height
            msg.reader().readBoolean();// is_qwerty
            msg.reader().readBoolean();// is_touch
            String platform = msg.reader().readUTF();
            String[] arrPlatform = platform.split("\\|");
            session.version = Integer.parseInt(arrPlatform[1].replaceAll("\\.", ""));

            // System.out.println(platform);
        } catch (Exception e) {
        } finally {
            msg.cleanup();
        }
        DataGame.sendLinkIP(session);
    }

    public void DropVeTinh(Player pl, Item item, Zone map, int x, int y) {
        ItemMap itemMap = new ItemMap(map, item.template, item.quantity, x, y, pl.id);
        itemMap.options = item.itemOptions;
        map.addItem(itemMap);
        Message msg = null;
        try {
            msg = new Message(68);
            msg.writer().writeShort(itemMap.itemMapId);
            msg.writer().writeShort(itemMap.itemTemplate.id);
            msg.writer().writeShort(itemMap.x);
            msg.writer().writeShort(itemMap.y);
            msg.writer().writeInt(-2);
            msg.writer().writeShort(200);
            sendMessAllPlayerInMap(map, msg);
        } catch (Exception e) {

        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }

    public void stealMoney(Player pl, int stealMoney) {// danh cho boss an trom
        Message msg;
        try {
            msg = new Message(95);
            msg.writer().writeInt(stealMoney);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }
}

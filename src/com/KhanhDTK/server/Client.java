package com.KhanhDTK.server;

import com.KhanhDTK.consts.ConstPlayer;
import com.girlkun.database.GirlkunDB;
import com.KhanhDTK.jdbc.daos.PlayerDAO;
import com.KhanhDTK.models.item.Item;
import com.KhanhDTK.models.map.ItemMap;
import com.KhanhDTK.models.player.Inventory;
import com.KhanhDTK.models.player.Player;
import com.KhanhDTK.models.skill.Skill;
import com.girlkun.network.server.GirlkunSessionManager;
import com.girlkun.network.session.ISession;
import com.KhanhDTK.server.io.MySession;
import com.KhanhDTK.services.ItemTimeService;
import com.KhanhDTK.services.Service;
import com.KhanhDTK.services.func.ChangeMapService;
import com.KhanhDTK.services.func.SummonDragon;
import com.KhanhDTK.services.func.TransactionService;
import com.KhanhDTK.services.InventoryServiceNew;
import com.KhanhDTK.services.ItemService;
import com.KhanhDTK.services.MapService;
//import com.KhanhDTK.services.NgocRongNamecService;
import com.KhanhDTK.utils.Logger;
import com.KhanhDTK.utils.SkillUtil;
import com.KhanhDTK.utils.Util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import com.KhanhDTK.models.matches.pvp.DaiHoiVoThuat;
//import com.KhanhDTK.models.matches.pvp.DaiHoiVoThuatService;

public class Client implements Runnable {

    private static Client i;

    private final Map<Long, Player> players_id = new HashMap<Long, Player>();
    private final Map<Integer, Player> players_userId = new HashMap<Integer, Player>();
    private final Map<String, Player> players_name = new HashMap<String, Player>();
    private final List<Player> players = new ArrayList<>();
    public int id = 1_000_000_000;

    public List<Player> getPlayers() {
        return this.players;
    }

    public static Client gI() {
        if (i == null) {
            i = new Client();
        }
        return i;
    }

    public void put(Player player) {
        if (!players_id.containsKey(player.id)) {
            this.players_id.put(player.id, player);
        }
        if (!players_name.containsValue(player)) {
            this.players_name.put(player.name, player);
        }
        if (!players_userId.containsValue(player)) {
            this.players_userId.put(player.getSession().userId, player);
        }
        if (!players.contains(player)) {
            this.players.add(player);
        }

    }

    private void remove(MySession session) {
        if (session.player != null) {
            this.remove(session.player);
            session.player.dispose();
        }
        if (session.joinedGame) {
            session.joinedGame = false;
            try {
                GirlkunDB.executeUpdate("update account set last_time_logout = ? where id = ?", new Timestamp(System.currentTimeMillis()), session.userId);
                GirlkunDB.executeUpdate("update account set last_time_off = ? where id = ?", new Timestamp(System.currentTimeMillis()), session.userId);
            } catch (Exception e) {

            }
        }
        ServerManager.gI().disconnect(session);
    }

    private void remove(Player player) {
        this.players_id.remove(player.id);
        this.players_name.remove(player.name);
        this.players_userId.remove(player.getSession().userId);
        this.players.remove(player);
        if (!player.beforeDispose) {
//            DaiHoiVoThuatService.gI(DaiHoiVoThuat.gI().getDaiHoiNow()).removePlayerWait(player);
//            DaiHoiVoThuatService.gI(DaiHoiVoThuat.gI().getDaiHoiNow()).removePlayer(player);
            player.beforeDispose = true;
            player.mapIdBeforeLogout = player.zone.map.mapId;
//            if (player.idNRNM != -1) {
//                ItemMap itemMap = new ItemMap(player.zone, player.idNRNM, 1, player.location.x, player.location.y, -1);
//                Service.gI().dropItemMap(player.zone, itemMap);
//                NgocRongNamecService.gI().pNrNamec[player.idNRNM - 353] = "";
//                NgocRongNamecService.gI().idpNrNamec[player.idNRNM - 353] = -1;
//                player.idNRNM = -1;
//            }
            ChangeMapService.gI().exitMap(player);
            TransactionService.gI().cancelTrade(player);
            if (player.clan != null) {
                player.clan.removeMemberOnline(null, player);
            }
            if (player.itemTime != null && player.itemTime.isUseTDLT) {
                Item tdlt = null;
                try {
                    tdlt = InventoryServiceNew.gI().findItemBag(player, 521);
                } catch (Exception e) {

                }
                if (tdlt != null) {
                    ItemTimeService.gI().turnOffTDLT(player, tdlt);
                }
            }
            if (SummonDragon.gI().playerSummonShenron != null
                    && SummonDragon.gI().playerSummonShenron.id == player.id) {
                SummonDragon.gI().isPlayerDisconnect = true;
            }
            if (player.mobMe != null) {
                player.mobMe.mobMeDie();
            }
            if (player.pet != null) {
                if (player.pet.mobMe != null) {
                    player.pet.mobMe.mobMeDie();
                }
                ChangeMapService.gI().exitMap(player.pet);
            }
            if (player.isClone) {
                ChangeMapService.gI().exitMap(player);
                player = null;
            }
        }
        PlayerDAO.updatePlayer(player);
    }

    public void kickSession(MySession session) {
        if (session != null) {
            this.remove(session);
            session.disconnect();
        }
    }

    public Player getPlayer(long playerId) {
        return this.players_id.get(playerId);
    }

    public Player getPlayerByUser(int userId) {
        return this.players_userId.get(userId);
    }

    public Player getPlayer(String name) {
        return this.players_name.get(name);
    }

    public void close() {
        Logger.log(Logger.BLACK, "Hệ thống tiến hành lưu dữ liệu người chơi và đăng xuất người chơi khỏi server." + players.size() + "\n");
//        while(!GirlkunSessionManager.gI().getSessions().isEmpty()){
//            Logger.error("LEFT PLAYER: " + this.players.size() + ".........................\n");
//            this.kickSession((MySession) GirlkunSessionManager.gI().getSessions().remove(0));
//        }
        while (!players.isEmpty()) {
            this.kickSession((MySession) players.remove(0).getSession());
        }
        Logger.error("Hệ thống lỗi đăng xuất người ch\n");
    }

    public void cloneMySessionNotConnect() {
        Logger.error("BEGIN KICK OUT MySession Not Connect...............................\n");
        Logger.error("COUNT: " + GirlkunSessionManager.gI().getSessions().size());
        if (!GirlkunSessionManager.gI().getSessions().isEmpty()) {
            for (int j = 0; j < GirlkunSessionManager.gI().getSessions().size(); j++) {
                MySession m = (MySession) GirlkunSessionManager.gI().getSessions().get(j);
                if (m.player == null) {
                    this.kickSession((MySession) GirlkunSessionManager.gI().getSessions().remove(j));
                }
            }
        }
        Logger.error("..........................................................SUCCESSFUL\n");
    }

    @Override
    public void run() {
        while (ServerManager.isRunning) {
            try {
                long st = System.currentTimeMillis();
                update();
                Thread.sleep(800 - (System.currentTimeMillis() - st));
            } catch (Exception e) {

            }
        }
    }

    private void update() {
        if (GirlkunSessionManager.gI().getSessions() != null) {
            for (ISession s : GirlkunSessionManager.gI().getSessions()) {
                MySession session = (MySession) s;
                if (session.timeWait > 0) {
                    session.timeWait--;
                    if (session.timeWait == 0) {
                        kickSession(session);
                    }
                }
            }
        }
    }

    public void show(Player player) {
        String txt = "";
        txt += "sessions: " + GirlkunSessionManager.gI().getSessions().size() + "\n";
        txt += "players_id: " + players_id.size() + "\n";
        txt += "players_userId: " + players_userId.size() + "\n";
        txt += "players_name: " + players_name.size() + "\n";
        txt += "players: " + players.size() + "\n";
        Service.gI().sendThongBao(player, txt);
    }
    public void clear() {
        List<Player> z = players;
        for (Player pl : z) {
            if (pl != null) {
                if (pl.isBot) {
                    remove(pl);
                }
            }
        }
    }

    public void createBot(MySession s){
        String[] name1 = {"le","hai","lan","anh","long","hehe"};
        String[] name2 = {"dz","xinh","deth","cute","cuto","cutie"};
        String[] name3 = {"vip","pro","ga","top1","sc1","vodich"};
        Player pl = new Player();
        Player temp = Client.gI().getPlayerByUser(1);//GodGK.loadById(2275);
        pl.setSession(s);
        s.userId = id;
        pl.id = id; id++;
        pl.name = name1[Util.nextInt(name1.length)]+name2[Util.nextInt(name2.length)]+name3[Util.nextInt(name3.length)];
        pl.gender = (byte)Util.nextInt(2);
        pl.isBot = true;
        pl.isBoss = false;
        pl.isPet = false;
        pl.nPoint.power = Util.nextInt(200000,200000000);
        pl.nPoint.power *= Util.nextInt(1,40);
        pl.nPoint.hpg = 100000;
        pl.nPoint.hpMax = Util.nextInt(200000,20000000);
        pl.nPoint.hp = pl.nPoint.hpMax / 2;
        pl.nPoint.mpMax = Util.nextInt(2000,2000000000);
        pl.nPoint.dame = Util.nextInt(2000,2000000);
        pl.nPoint.stamina = 32000;
        pl.itemTime.isUseTDLT = true;
        pl.typePk = ConstPlayer.NON_PK;
        if (pl.nPoint.hp == 0) { 
        Service.gI().hsChar(pl, pl.nPoint.hpMax, pl.nPoint.mpMax);}
        //skill
        int[] skillsArr = pl.gender == 0 ? new int[]{0, 1,  19}
                    : pl.gender == 1 ? new int[]{ 12, 17}
                    : new int[]{4, 8, 13,  19};
        for(int j = 0;j<skillsArr.length;j++){
            Skill skill = SkillUtil.createSkill(skillsArr[j], 7);
            pl.playerSkill.skills.add(skill);
        }
        pl.inventory = new Inventory();
        for(int i = 0;i<12;i++){
            pl.inventory.itemsBody.add(ItemService.gI().createItemNull());
        }
        pl.inventory.gold = 2000000000;
        pl.inventory.itemsBody.set(5, Manager.CAITRANG.get(Util.nextInt(0,Manager.CAITRANG.size()-1)));
        pl.location.y = 300;
        pl.zone = MapService.gI().getMapCanJoin(pl, (Util.nextInt(0,215)), (Util.nextInt(0,10)));
        if(pl.zone == null) return;
        if(pl.zone.map == null) return;
        pl.location.x = 200;//temp.location.x + Util.nextInt(-400,400);
        pl.zone.addPlayer(pl);
        pl.zone.load_Me_To_Another(pl);
        Client.gI().put(pl);
    }
}

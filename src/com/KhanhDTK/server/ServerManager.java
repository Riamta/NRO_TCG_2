package com.KhanhDTK.server;

import com.girlkun.database.GirlkunDB;

import java.net.ServerSocket;

import com.KhanhDTK.jdbc.daos.HistoryTransactionDAO;
import com.KhanhDTK.jdbc.daos.PlayerDAO;
import com.KhanhDTK.kygui.ShopKyGuiManager;
import com.girlkun.Bot.BotManager;
import com.KhanhDTK.kygui.ShopKyGuiService;
import com.KhanhDTK.models.ThanhTich.CheckDataDay;
import com.KhanhDTK.models.boss.BossManager;
import com.KhanhDTK.models.item.Item;
import com.KhanhDTK.models.map.BDKB.BanDoKhoBau;
import com.KhanhDTK.models.map.Zone;
import com.KhanhDTK.models.map.challenge.MartialCongressManager;
import com.KhanhDTK.models.map.vodai.VoDaiManager;
import com.KhanhDTK.models.npc.DuaHau;
//import com.KhanhDTK.models.map.challenge.MartialCongressManager;
//import com.KhanhDTK.models.matches.pvp.DaiHoiVoThuat;
import com.KhanhDTK.models.player.Player;
//import com.KhanhDTK.models.player.Playerao;
import com.girlkun.network.session.ISession;
import com.girlkun.network.example.MessageSendCollect;
import com.girlkun.network.server.GirlkunServer;
import com.girlkun.network.server.IServerClose;
import com.girlkun.network.server.ISessionAcceptHandler;
import static com.KhanhDTK.server.Maintenance.isBaoTri;
import com.KhanhDTK.server.io.MyKeyHandler;
import com.KhanhDTK.server.io.MySession;
import com.KhanhDTK.services.*;
import com.KhanhDTK.services.func.ChonAiDay;
import com.KhanhDTK.services.func.TopService;
import com.KhanhDTK.utils.Logger;
import com.KhanhDTK.utils.TimeUtil;
import com.KhanhDTK.utils.Util;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import com.KhanhDTK.services.func.TaiXiu;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

public class ServerManager {

    public int threadMap;

    public static String timeStart;

    public static final Map CLIENTS = new HashMap();

    public static String NAME = "Bkt Đẹp Trai";
    public static int PORT = 14445;

    private static ServerManager instance;

    public static ServerSocket listenSocket;
    public static boolean isRunning;

    public void init() {
        Manager.gI();
        try {
            if (Manager.LOCAL) {
                return;
            }
            GirlkunDB.executeUpdate("update account set last_time_login = '2000-01-01', "
                    + "last_time_logout = '2001-01-01'");
        } catch (Exception e) {
            System.err.print("\nError at 310\n");
            e.printStackTrace();
        }
    }

    public static ServerManager gI() {
        if (instance == null) {
            instance = new ServerManager();
            instance.init();
        }
        return instance;
    }

    public static void main(String[] args) {
        timeStart = TimeUtil.getTimeNow("dd/MM/yyyy HH:mm:ss");
        ServerManager serverManager = ServerManager.gI();
        serverManager.run();
        //menu.main(args);
        // Tạo và chạy player ảo
//         Playerao.createVirtualPlayers(200); // Thay đổi số lượng player ảo tùy thích
    }

    public void run() {
        long delay = 500;
        activeCommandLine();
        activeGame();
        activeServerSocket();
        TaiXiu.gI().lastTimeEnd = System.currentTimeMillis() + 50000;
        Logger.log(Logger.BLACK, "Time start server: " + ServerManager.timeStart + "\n");
        

        new Thread(TaiXiu.gI(), "Thread TaiXiu").start();

        new Thread(DuaHau.gI(), "Thread Dưa Hấu").start();

        isRunning = true;
        new Thread(() -> {
            while (isRunning) {
                try {
                    long start = System.currentTimeMillis();
                    MartialCongressManager.gI().update();
                    VoDaiManager.gI().update();
                    Player player = null;
                    for (int i = 0; i < Client.gI().getPlayers().size(); ++i) {
                        if (Client.gI().getPlayers().get(i) != null) {
                            player = (Client.gI().getPlayers().get(i));
//                            PlayerDAO.updatePlayer(player);
                        }
                    }
                    ShopKyGuiManager.gI().save();
                    long timeUpdate = System.currentTimeMillis() - start;
                    if (timeUpdate < delay) {
                        Thread.sleep(delay - timeUpdate);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "Update dai hoi vo thuat").start();
        try {
            Thread.sleep(1000);
            BossManager.gI().loadBoss();
            Manager.MAPS.forEach(com.KhanhDTK.models.map.Map::initBoss);
        } catch (InterruptedException ex) {
            System.err.print("\nError at 311\n");
            java.util.logging.Logger.getLogger(BossManager.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    private void act() throws Exception {
        GirlkunServer.gI().init().setAcceptHandler(new ISessionAcceptHandler() {
            @Override
            public void sessionInit(ISession is) {
//                antiddos girlkun
                if (!canConnectWithIp(is.getIP())) {
                    is.disconnect();
                    return;
                }

                is = is.setMessageHandler(Controller.getInstance())
                        .setSendCollect(new MessageSendCollect())
                        .setKeyHandler(new MyKeyHandler())
                        .startCollect();
            }

            @Override
            public void sessionDisconnect(ISession session) {
                Client.gI().kickSession((MySession) session);
            }
        }).setTypeSessioClone(MySession.class)
                .setDoSomeThingWhenClose(new IServerClose() {
                    @Override
                    public void serverClose() {
                        System.out.println("server close");
                        System.exit(0);
                    }
                })
                .start(PORT);

    }

    private void activeServerSocket() {
        if (true) {
            try {
                this.act();
            } catch (Exception e) {
                System.err.print("\nError at 312\n");
                e.printStackTrace();
            }
            return;
        }
    }

    private boolean canConnectWithIp(String ipAddress) {
        Object o = CLIENTS.get(ipAddress);
        if (o == null) {
            CLIENTS.put(ipAddress, 1);
            return true;
        } else {
            int n = Integer.parseInt(String.valueOf(o));
            if (n < Manager.MAX_PER_IP) {
                n++;
                CLIENTS.put(ipAddress, n);
                return true;
            } else {
                return false;
            }
        }
    }

    public void disconnect(MySession session) {
        Object o = CLIENTS.get(session.getIP());
        if (o != null) {
            int n = Integer.parseInt(String.valueOf(o));
            n--;
            if (n < 0) {
                n = 0;
            }
            CLIENTS.put(session.getIP(), n);
        }
    }

    private void activeCommandLine() {
        new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            while (true) {
                String line = sc.nextLine();
                if (line.equals("savekigui")) {
                }
                if (line.equals("baotri")) {
                    Maintenance.gI().start(2);
                }
                if (line.equals("lavie")) {
                    ClanService.gI().saveclan();
                } else if (line.equals("online")) {
                    List<String> lines = new ArrayList<>();
                    ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
                    ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(threadMXBean.getAllThreadIds());
                    new Thread(BotManager.gI(), "Thread Bot Game").start();
                    System.out.println("Danh sách tên các luồng đang chạy:");
                    lines.add("Danh sách tên các luồng đang chạy:");
                    for (ThreadInfo threadInfo : threadInfos) {
                        lines.add(threadInfo.getThreadName());
                        System.out.println(threadInfo.getThreadName());
                    }
                    Path file = Paths.get("DataThread.txt");
                    try {
                        Files.write(file, lines, StandardCharsets.UTF_8);
                    } catch (IOException ex) {
                        java.util.logging.Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Logger.log(Logger.PURPLE, "Thread" + (Thread.activeCount() - threadMap) + "\nOnline:" + Client.gI().getPlayers().size());

                } else if (line.equals("nplayer")) {
                    Logger.error("Player in game: " + Client.gI().getPlayers().size() + "\n");
                } else if (line.equals("admin")) {
                    new Thread(() -> {
                        Client.gI().close();
                    }, "adminThread").start();
                } else if (line.startsWith("bang")) {
                    new Thread(() -> {
                        try {
                            ClanService.gI().close();
                            Logger.error("Save " + Manager.CLANS.size() + " bang");
                        } catch (Exception e) {

                            e.printStackTrace();
                            Logger.error("Thông báo: lỗi lưu dữ liệu bang hội.\n");

                        }
                    }, "bangThread").start();
                } else if (line.startsWith("a")) {
                    String a = line.replace("a ", "");
                    Service.gI().sendThongBaoAllPlayer(a);
                } else if (line.startsWith("tb")) {
                    String a = line.replace("tb ", "");
                    Service.gI().sendBangThongBaoAllPlayervip(a);
                } else if (line.startsWith("qua")) {
                    try {
                        List<Item.ItemOption> ios = new ArrayList<>();
                        String[] pagram1 = line.split("=")[1].split("-");
                        String[] pagram2 = line.split("=")[2].split("-");
                        if (pagram1.length == 4 && pagram2.length % 2 == 0) {
                            Player p = Client.gI().getPlayer(Integer.parseInt(pagram1[0]));
                            if (p != null) {
                                for (int i = 0; i < pagram2.length; i += 2) {
                                    ios.add(new Item.ItemOption(Integer.parseInt(pagram2[i]), Integer.parseInt(pagram2[i + 1])));
                                }
                                Item i = Util.sendDo(Integer.parseInt(pagram1[2]), Integer.parseInt(pagram1[3]), ios);
                                i.quantity = Integer.parseInt(pagram1[1]);
                                InventoryServiceNew.gI().addItemBag(p, i);
                                InventoryServiceNew.gI().sendItemBags(p);
                                Service.gI().sendThongBao(p, "Admin trả đồ. anh em thông cảm nhé...");
                            } else {
                                System.out.println("Người chơi không online");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Lỗi quà");

                    }
                }
            }
        }, "activeCommandLineThread").start();
    }

    private void activeGame() {
        final long delay = 2000; // Thời gian delay là 5000ms (5 giây)

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                while (!isBaoTri) {
                    for (BanDoKhoBau bando : BanDoKhoBau.BAN_DO_KHO_BAUS) {
                        bando.update();
                    }

                    ClanService.gI().saveclan();
                    Service.gI().AutoSavePlayerData();
                    ShopKyGuiManager.gI().save();
                    ShopKyGuiService.update();

                    Thread.sleep(delay);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("\nThread interrupted\n");
            } catch (Exception e) {
                System.err.print("\nError at 314\n");
                e.printStackTrace();
            }
        });

        executor.shutdown();
    }

    public void close(long delay) {
        isRunning = false;
        try {
            GirlkunServer.gI().stopConnect();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error("Thông báo: Lỗi Đóng kết nối tới server.\n");
        }
        Logger.log(Logger.BLACK, "\nĐóng kết nối tới server.\n");
        try {
            Client.gI().close();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error("Thông báo: Lỗi lưu dử liệu người chơi.\n");
        }
        try {
            ClanService.gI().close();
            Logger.log(Logger.BLACK, "Lưu dử liệu bang hội\n");
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error("Thông báo: lỗi lưu dữ liệu bang hội.\n");
        }
        try {
            ShopKyGuiManager.gI().save();
            Logger.log(Logger.BLACK, "Lưu dử liệu ký gửi\n");
        } catch (InterruptedException ex) {
            System.err.print("\nError at 315\n");
            ex.printStackTrace();
        }
        try {
            CheckDataDay.ResetDataDay();
            Logger.log(Logger.BLACK,
                    "Reset Dữ Liệu Hoạt Động Hằng Ngày - Quà Nạp Hằng Ngày.\n");
        } catch (SQLException ex) {
            System.err.print("\nError at 316\n");
            ex.printStackTrace();
        }
        Logger.log(Logger.BLACK, "Bảo trì đóng server thành công.\n");
        System.exit(0);
    }
}

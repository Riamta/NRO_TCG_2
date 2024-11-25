package com.KhanhDTK.models.item;

import com.KhanhDTK.models.player.NPoint;
import com.KhanhDTK.models.player.Player;
import com.KhanhDTK.services.Service;
import com.KhanhDTK.utils.Util;
import com.KhanhDTK.services.ItemTimeService;

public class ItemTime {

    //id item text
    public static final byte DOANH_TRAI = 0;
    public static final byte BAN_DO_KHO_BAU = 1;

    public static final byte TEXT_NHAN_BUA_MIEN_PHI = 3;

    public static final int TIME_ITEM = 600000;
    public static final int TIME_OPEN_POWER = 86400000;
    public static final byte KHI_GASS = 2;
    public static final int TIME_NUOC_MIA = 900000000;
    public static final int TIME_MAY_DO = 1800000;
    public static final int TIME_MAY_DO2 = 1800000;
    public static final int TIME_EAT_MEAL = 600000;
    public static final int TIME_DUOI_KHI = 300000;
    private Player player;
//kẹo halloween
     public boolean isUsekeo;
      public boolean isUsemat;
       public boolean isUsegato;
        public boolean isUseber;
         public boolean isUsesup;
     public long lastTimekeo;
      public long lastTimemat;
       public long lastTimegato;
        public long lastTimeber;
         public long lastTimesup;
    public boolean isUseBoHuyet;
    public boolean isUseBoKhi;
    public boolean isUseGiapXen;
    public boolean isUseCuongNo;
    
    public boolean isUseAnDanh;
    public boolean isUseBoHuyet2;
    public boolean isUseBoKhi2;
    public boolean isUseGiapXen2;
    public boolean isUseCuongNo2;
    public boolean isUseAnDanh2;
    public boolean isbkt;
    public long lastTimeBoHuyet;
    public long lastTimeBoKhi;
    public long lastTimeGiapXen;
    public long lastTimeCuongNo;
    public long lastTimeAnDanh;
    public long lastTimebkt;
    public long lastTimeBoHuyet2;
    public long lastTimeBoKhi2;
    public long lastTimeGiapXen2;
    public long lastTimeCuongNo2;
    public long lastTimeAnDanh2;
    public boolean isdkhi;
    public long lastTimedkhi;
    public int icondkhi;

    public boolean isUseMayDo;
    public long lastTimeUseMayDo;//lastime de chung 1 cai neu time = nhau
    public boolean isUseMayDo2;
    public long lastTimeUseMayDo2;

    public boolean isOpenPower;
    public long lastTimeOpenPower;

    public boolean isUseTDLT;
//        public static final int TIME_DUOI_KHI = 300000;
    public long lastTimeUseTDLT;
    public int timeTDLT;

    public boolean isEatMeal;
    public long lastTimeEatMeal;
    public int iconMeal;
    public long lastX2EXP;
    public boolean isX2EXP;
    public long lastX3EXP;
    public boolean isX3EXP;
    public long lastX5EXP;
    public boolean isX5EXP;
    public long lastX7EXP;
    public boolean isX7EXP;
    public long lastbkt;
    public int IconX2EXP = 21881;

    public long lastnuocmiakhonglo;
    public boolean isnuocmiakhonglo;
    public long lastnuocmiathom;
    public boolean isnuocmiathom;
    public long lastnuocmiasaurieng;
    public boolean isnuocmiasaurieng;

    public ItemTime(Player player) {
        this.player = player;
    }

    public void update() {
        if (isX3EXP) {
            if (Util.canDoWithTime(lastX3EXP, TIME_MAY_DO)) {
                isX3EXP = false;
            }
        }
        if (isX5EXP) {
            if (Util.canDoWithTime(lastX5EXP, TIME_MAY_DO)) {
                isX5EXP = false;
            }
        }
        if (isX7EXP) {
            if (Util.canDoWithTime(lastX7EXP, TIME_MAY_DO)) {
                isX7EXP = false;
            }
        }
        if (isX2EXP) {
            if (Util.canDoWithTime(lastX2EXP, TIME_MAY_DO)) {
                isX2EXP = false;
            }
        }
        if (isnuocmiakhonglo) {
            if (Util.canDoWithTime(lastnuocmiakhonglo, TIME_NUOC_MIA)) {
                isnuocmiakhonglo = false;
            }
        }
        if (isnuocmiathom) {
            if (Util.canDoWithTime(lastnuocmiathom, TIME_NUOC_MIA)) {
                isnuocmiathom = false;
            }
        }
        if (isnuocmiasaurieng) {
            if (Util.canDoWithTime(lastnuocmiasaurieng, TIME_NUOC_MIA)) {
                isnuocmiasaurieng = false;
            }
        }
        if (isEatMeal) {
            if (Util.canDoWithTime(lastTimeEatMeal, TIME_EAT_MEAL)) {
                isEatMeal = false;
                Service.gI().point(player);
            }
        }
        if (isUseBoHuyet) {
            if (Util.canDoWithTime(lastTimeBoHuyet, TIME_ITEM)) {
                isUseBoHuyet = false;
                Service.gI().point(player);
            }
        }
        if (isUsekeo) {
            if (Util.canDoWithTime(lastTimeBoHuyet, TIME_ITEM)) {
                isUsekeo = false;
                Service.gI().point(player);
            }
        }
        if (isUsemat) {
            if (Util.canDoWithTime(lastTimeBoHuyet, TIME_ITEM)) {
                isUsemat = false;
                Service.gI().point(player);
            }
        }
        if (isUsegato) {
            if (Util.canDoWithTime(lastTimeBoHuyet, TIME_ITEM)) {
                isUseber = false;
                Service.gI().point(player);
            }
        }
        if (isUsesup) {
            if (Util.canDoWithTime(lastTimeBoHuyet, TIME_ITEM)) {
                isUsesup = false;
                Service.gI().point(player);
            }
        }

        if (isUseBoKhi) {
            if (Util.canDoWithTime(lastTimeBoKhi, TIME_ITEM)) {
                isUseBoKhi = false;
                Service.gI().point(player);
            }
        }

        if (isUseGiapXen) {
            if (Util.canDoWithTime(lastTimeGiapXen, TIME_ITEM)) {
                isUseGiapXen = false;
            }
        }
        if (isbkt) {
            if (Util.canDoWithTime(lastTimebkt, TIME_MAY_DO)) {
                isbkt = false;
            }
        }
        if (isUseCuongNo) {
            if (Util.canDoWithTime(lastTimeCuongNo, TIME_ITEM)) {
                isUseCuongNo = false;
                Service.gI().point(player);
            }
        }
        if (isUseAnDanh) {
            if (Util.canDoWithTime(lastTimeAnDanh, TIME_ITEM)) {
                isUseAnDanh = false;
            }
        }

        if (isUseBoHuyet2) {
            if (Util.canDoWithTime(lastTimeBoHuyet2, TIME_ITEM)) {
                isUseBoHuyet2 = false;
                Service.gI().point(player);
            }
        }

        if (isUseBoKhi2) {
            if (Util.canDoWithTime(lastTimeBoKhi2, TIME_ITEM)) {
                isUseBoKhi2 = false;
                Service.gI().point(player);
            }
        }
        if (isUseGiapXen2) {
            if (Util.canDoWithTime(lastTimeGiapXen2, TIME_ITEM)) {
                isUseGiapXen2 = false;
            }
        }
        if (isUseCuongNo2) {
            if (Util.canDoWithTime(lastTimeCuongNo2, TIME_ITEM)) {
                isUseCuongNo2 = false;
                Service.gI().point(player);
            }
        }
        if (isUseAnDanh2) {
            if (Util.canDoWithTime(lastTimeAnDanh2, TIME_ITEM)) {
                isUseAnDanh2 = false;
            }
        }
        if (isdkhi) {
            if (Util.canDoWithTime(lastTimedkhi, TIME_DUOI_KHI)) {
                isdkhi = false;
            }
        }
        if (isOpenPower) {
            if (Util.canDoWithTime(lastTimeOpenPower, TIME_OPEN_POWER)) {
                player.nPoint.limitPower++;
                if (player.nPoint.limitPower > NPoint.MAX_LIMIT) {
                    player.nPoint.limitPower = NPoint.MAX_LIMIT;
                }
                Service.gI().sendThongBao(player, "Giới hạn sức mạnh của bạn đã được tăng lên 1 bậc");
                isOpenPower = false;
            }
        }
        if (isUseMayDo) {
            if (Util.canDoWithTime(lastTimeUseMayDo, TIME_MAY_DO)) {
                isUseMayDo = false;
            }
        }
        if (isUseMayDo2) {
            if (Util.canDoWithTime(lastTimeUseMayDo2, TIME_MAY_DO2)) {
                isUseMayDo2 = false;
            }
        }
        if (isUseTDLT) {
            if (Util.canDoWithTime(lastTimeUseTDLT, timeTDLT)) {
                this.isUseTDLT = false;
                ItemTimeService.gI().sendCanAutoPlay(this.player);
            }
        }
    }

    public void dispose() {
        this.player = null;
    }
}

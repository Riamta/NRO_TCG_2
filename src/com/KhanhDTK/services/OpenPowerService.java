package com.KhanhDTK.services;

import com.KhanhDTK.models.item.Item;
import com.KhanhDTK.models.player.NPoint;
import com.KhanhDTK.models.player.Pet;
import com.KhanhDTK.models.player.Player;
import com.KhanhDTK.server.Client;
import java.awt.Point;

public class OpenPowerService {

    public static final long COST_SPEED_OPEN_LIMIT_POWER = 500000000L;

    private static OpenPowerService i;

    private OpenPowerService() {

    }

    public static OpenPowerService gI() {
        if (i == null) {
            i = new OpenPowerService();
        }
        return i;
    }

    public boolean openPowerBasic(Player player) {
        byte curLimit = player.nPoint.limitPower;
        if (curLimit < NPoint.MAX_LIMIT) {
            if (!player.itemTime.isOpenPower && player.nPoint.canOpenPower()) {
                player.itemTime.isOpenPower = true;
                player.itemTime.lastTimeOpenPower = System.currentTimeMillis();
                ItemTimeService.gI().sendAllItemTime(player);
                return true;
            } else {
                Service.gI().sendThongBao(player, "Sức mạnh của bạn không đủ để thực hiện");
                return false;
            }
        } else {
            Service.gI().sendThongBao(player, "Sức mạnh của bạn đã đạt tới mức tối đa");
            return false;
        }
    }

    public boolean chuyenSinh(Player player) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) <= 0) {
            Service.gI().sendThongBao(player, "Hành trang không đủ chỗ trống");
        } else {
            if (player.nPoint.power >= 500000000000L) {
                int hongngoc = 0;
                Item daDiaNgucXanh = null;
                Item daDiaNgucVang = null;
                Item daDiaNgucDo = null;
                Item daDiaNgucTim = null;
                Item tv = null;
                daDiaNgucXanh = InventoryServiceNew.gI().findItemBag(player, 1488);
                daDiaNgucVang = InventoryServiceNew.gI().findItemBag(player, 1486);
                daDiaNgucDo = InventoryServiceNew.gI().findItemBag(player, 1487);
                daDiaNgucTim = InventoryServiceNew.gI().findItemBag(player, 1489);
                tv = InventoryServiceNew.gI().findItemBag(player, 457);
                hongngoc = 1000;
                player.inventory.ruby -= hongngoc;
                InventoryServiceNew.gI().subQuantityItemsBag(player, daDiaNgucXanh, 99);
                InventoryServiceNew.gI().subQuantityItemsBag(player, daDiaNgucVang, 99);
                InventoryServiceNew.gI().subQuantityItemsBag(player, daDiaNgucDo, 99);
                InventoryServiceNew.gI().subQuantityItemsBag(player, daDiaNgucTim, 99);
                InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 99);
                player.nPoint.power -= (player.nPoint.power - 2000);
                player.capChuyenSinh++;
                player.nPoint.hpg += 50000;
                player.nPoint.dameg += 2000;
                player.nPoint.mpg += 50000;
                Service.getInstance().point(player);
                Client.gI().kickSession(player.getSession());
                if (player.nPoint.power < 179999999999L) {
                }
                if (!player.isPet) {
                    Service.gI().sendThongBao(player, "Giới hạn sức mạnh của bạn đã được reset");
                } else {
                    Service.gI().sendThongBao(((Pet) player).master, "Giới hạn sức mạnh của đệ tử đã được reset");
                }
                return true;
            } else {
                if (!player.isPet) {
                    Service.gI().sendThongBao(player, "Bạn không đủ điều kiện để chuyển sinh");
                } else {
                    Service.gI().sendThongBao(((Pet) player).master, "Bạn không đủ điều kiện để chuyển sinh");
                }
                return false;
            }
        }
        return true;
    }

    public boolean openPowerSpeed(Player player) {
        if (player.nPoint.limitPower < NPoint.MAX_LIMIT) {
//            if (player.nPoint.power >= 17900000000L) {
            player.nPoint.limitPower++;
            if (player.nPoint.limitPower > NPoint.MAX_LIMIT) {
                player.nPoint.limitPower = NPoint.MAX_LIMIT;
            }
            if (!player.isPet) {
                Service.gI().sendThongBao(player, "Giới hạn sức mạnh của bạn đã được tăng lên 1 bậc");
            } else {
                Service.gI().sendThongBao(((Pet) player).master, "Giới hạn sức mạnh của đệ tử đã được tăng lên 1 bậc");
            }
            return true;
//            } else {
//                if (!player.isPet) {
//                    Service.gI().sendThongBao(player, "Sức mạnh của bạn không đủ để thực hiện");
//                } else {
//                    Service.gI().sendThongBao(((Pet) player).master, "Sức mạnh của đệ tử không đủ để thực hiện");
//                }
//                return false;
//            }
        } else {
            if (!player.isPet) {
                Service.gI().sendThongBao(player, "Sức mạnh của bạn đã đạt tới mức tối đa");
            } else {
                Service.gI().sendThongBao(((Pet) player).master, "Sức mạnh của đệ tử đã đạt tới mức tối đa");
            }
            return false;
        }
    }

}

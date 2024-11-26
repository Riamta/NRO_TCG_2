package com.KhanhDTK.services.func;

import com.KhanhDTK.card.Card;
import com.KhanhDTK.card.RadarCard;
import com.KhanhDTK.card.RadarService;
import com.KhanhDTK.consts.ConstMap;
import com.KhanhDTK.models.item.Item;
import com.KhanhDTK.consts.ConstNpc;
import com.KhanhDTK.consts.ConstPlayer;
import com.KhanhDTK.models.boss.BossManager;
import com.KhanhDTK.models.item.Item.ItemOption;
import com.KhanhDTK.models.map.Zone;
import com.KhanhDTK.models.npc.specialnpc.MabuEgg;
import com.KhanhDTK.models.player.Inventory;
import com.KhanhDTK.models.player.NewPet;
import com.KhanhDTK.services.NpcService;
import com.KhanhDTK.models.player.Player;
import com.KhanhDTK.models.skill.Skill;
import com.girlkun.network.io.Message;
import com.KhanhDTK.server.Manager;
import com.KhanhDTK.utils.SkillUtil;
import com.KhanhDTK.services.Service;
import com.KhanhDTK.utils.Util;
import com.KhanhDTK.server.io.MySession;
import com.KhanhDTK.services.EffectSkillService;
import com.KhanhDTK.services.ItemService;
import com.KhanhDTK.services.ItemTimeService;
import com.KhanhDTK.services.PetService;
import com.KhanhDTK.services.PlayerService;
import com.KhanhDTK.services.TaskService;
import com.KhanhDTK.services.InventoryServiceNew;
import com.KhanhDTK.services.ItemMapService;
import com.KhanhDTK.services.MapService;
//import com.KhanhDTK.services.NgocRongNamecService;
import com.KhanhDTK.services.RewardService;
import com.KhanhDTK.services.SkillService;
import com.KhanhDTK.utils.Logger;
import com.KhanhDTK.utils.TimeUtil;
import java.util.Date;
import java.util.Random;

public class UseItem {

    private static final int ITEM_BOX_TO_BODY_OR_BAG = 0;
    private static final int ITEM_BAG_TO_BOX = 1;
    private static final int ITEM_BODY_TO_BOX = 3;
    private static final int ITEM_BAG_TO_BODY = 4;
    private static final int ITEM_BODY_TO_BAG = 5;
    private static final int ITEM_BAG_TO_PET_BODY = 6;
    private static final int ITEM_BODY_PET_TO_BAG = 7;

    private static final byte DO_USE_ITEM = 0;
    private static final byte DO_THROW_ITEM = 1;
    private static final byte ACCEPT_THROW_ITEM = 2;
    private static final byte ACCEPT_USE_ITEM = 3;

    private static UseItem instance;

    private UseItem() {

    }

    public static UseItem gI() {
        if (instance == null) {
            instance = new UseItem();
        }
        return instance;
    }

    public void getItem(MySession session, Message msg) {
        Player player = session.player;
        TransactionService.gI().cancelTrade(player);
        try {
            int type = msg.reader().readByte();
            int index = msg.reader().readByte();
            if (index == -1) {
                return;
            }
            switch (type) {
                case ITEM_BOX_TO_BODY_OR_BAG:
                    InventoryServiceNew.gI().itemBoxToBodyOrBag(player, index);
                    TaskService.gI().checkDoneTaskGetItemBox(player);
                    break;
                case ITEM_BAG_TO_BOX:
                    InventoryServiceNew.gI().itemBagToBox(player, index);
                    break;
                case ITEM_BODY_TO_BOX:
                    InventoryServiceNew.gI().itemBodyToBox(player, index);
                    break;
                case ITEM_BAG_TO_BODY:
                    InventoryServiceNew.gI().itemBagToBody(player, index);
                    break;
                case ITEM_BODY_TO_BAG:
                    InventoryServiceNew.gI().itemBodyToBag(player, index);
                    break;
                case ITEM_BAG_TO_PET_BODY:
                    InventoryServiceNew.gI().itemBagToPetBody(player, index);
                    break;
                case ITEM_BODY_PET_TO_BAG:
                    InventoryServiceNew.gI().itemPetBodyToBag(player, index);
                    break;
            }
            player.setClothes.setup();
            if (player.pet != null) {
                player.pet.setClothes.setup();
            }
            player.setClanMember();
            Service.gI().point(player);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void testItem(Player player, Message _msg) {
        TransactionService.gI().cancelTrade(player);
        Message msg;
        try {
            byte type = _msg.reader().readByte();
            int where = _msg.reader().readByte();
            int index = _msg.reader().readByte();
            System.out.println("type: " + type);
            System.out.println("where: " + where);
            System.out.println("index: " + index);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void doItem(Player player, Message _msg) {

        TransactionService.gI().cancelTrade(player);
        Message msg;
        byte type;
        try {
            type = _msg.reader().readByte();
            int where = _msg.reader().readByte();
            int index = _msg.reader().readByte();
            switch (type) {
                case DO_USE_ITEM:
                    if (player != null && player.inventory != null) {
                        if (index >= 0) {
                            Item item = player.inventory.itemsBag.get(index);
                            if (item.isNotNullItem()) {
                                if (item.template.type == 7) {
                                    msg = new Message(-43);
                                    msg.writer().writeByte(type);
                                    msg.writer().writeByte(where);
                                    msg.writer().writeByte(index);
                                    msg.writer().writeUTF("Bạn chắc chắn học "
                                            + player.inventory.itemsBag.get(index).template.name + "?");
                                    player.sendMessage(msg);
                                } else {

                                    UseItem.gI().useItem(player, item, index);
                                }
                            }
                        } else {
                            this.eatPea(player);
                        }
                    }
                    break;
                case DO_THROW_ITEM:
                    if (!(player.zone.map.mapId == 21 || player.zone.map.mapId == 22 || player.zone.map.mapId == 23)) {
                        Item item = null;
                        if (where == 0) {
                            item = player.inventory.itemsBody.get(index);
                        } else {
                            item = player.inventory.itemsBag.get(index);
                        }
                        if (item != null && item.template != null) {
                            msg = new Message(-43);
                            msg.writer().writeByte(type);
                            msg.writer().writeByte(where);
                            msg.writer().writeByte(index);
                            msg.writer().writeUTF("Bạn chắc chắn muốn vứt " + item.template.name + "?");
                            player.sendMessage(msg);
                        }
                    } else {
                        Service.gI().sendThongBao(player, "Không thể thực hiện");
                    }
                    break;
                case ACCEPT_THROW_ITEM:
                    InventoryServiceNew.gI().throwItem(player, where, index);
                    Service.gI().point(player);
                    InventoryServiceNew.gI().sendItemBags(player);
                    break;
                case ACCEPT_USE_ITEM:

                    UseItem.gI().useItem(player, player.inventory.itemsBag.get(index), index);
                    break;
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void useItem(Player pl, Item item, int indexBag) {
        if (item.template.strRequire <= pl.nPoint.power) {
            switch (item.template.type) {
                case 7: // sách học, nâng skill
                    learnSkill(pl, item);
                    break;
                case 33:
                    UseCard(pl, item);
                    break;
                case 6: // đậu thần
                    this.eatPea(pl);
                    break;
                case 12: // ngọc rồng các loại
                    controllerCallRongThan(pl, item);
                    break;
                case 23: // thú cưỡi mới
                case 24: // thú cưỡi cũ
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    break;
                case 11:
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    Service.gI().sendFlagBag(pl);
                    break;
                case 72:
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    Service.gI().sendPetFollow(pl, (short) (item.template.iconID - 1));
                    break;
                case 21:
                    if (pl.newpet != null) {
                        ChangeMapService.gI().exitMap(pl.newpet);
                        pl.newpet.dispose();
                        pl.newpet = null;
                    }
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    PetService.Pet2(pl, item.template.head, item.template.body, item.template.leg);
                    Service.getInstance().point(pl);
                    break;
                case 91:
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    Service.gI().sendFlagBag(pl);
                    break;
                case 77:
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    break;
                case 74:
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    Service.gI().sendFoot(pl, item.template.id);
                    break;
                case 39:
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    Service.gI().removeTitle(pl);
                    Service.gI().sendTitle(pl, item.template.id);
                    // DanhHieu.AddDanhHieu(pl, item);
                    Service.gI().point(pl);
                    break;
                default:
                    switch (item.template.id) {
                        case 1110:
                            if (InventoryServiceNew.gI().findItem(pl.inventory.itemsBag, 1110).isNotNullItem()) {
                                pl.tickxanh = true;
                                Service.gI().point(pl);
                                Service.gI().sendMoney(pl);
                            }
                            break;
                        case 992:
                            pl.type = 1;
                            pl.maxTime = 5;
                            Service.gI().Transport(pl);
                            break;
                        case 1444:
                            pl.type = 3;
                            pl.maxTime = 5;
                            Service.gI().Transport(pl);
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            break;
                        case 361:
                            maydoboss(pl);
                            break;
                        case 1536: // cskb
                            Input.gI().TAOPET(pl);
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            break;
                        case 293:
                            openGoiDau1(pl, item);
                            break;
                        case 294:
                            openGoiDau2(pl, item);
                            break;
                        case 295:
                            openGoiDau3(pl, item);
                            break;
                        case 296:
                            openGoiDau4(pl, item);
                            break;
                        case 297:
                            openGoiDau5(pl, item);
                            break;
                        case 298:
                            openGoiDau6(pl, item);
                            break;
                        case 299:
                            openGoiDau7(pl, item);
                            break;
                        case 596:
                            openGoiDau8(pl, item);
                            break;
                        case 597:
                            openGoiDau9(pl, item);
                            break;
                        case 211: // nho tím
                        case 212: // nho xanh
                            eatGrapes(pl, item);
                            break;
                        case 1105:// hop qua skh, item 2002 xd
                            UseItem.gI().Hopts(pl, item);
                            break;
                        case 1545:
                            if (pl.pet != null) {
                                NpcService.gI().createMenuConMeo(pl, ConstNpc.menu_detu, 9966,
                                        "|7|Boom Boom Boommmmmmmmmmmmmmm...\n"
                                                + "|5|Ngươi muốn nở trứng sao?\n"
                                                + "Sau khi ấp trứng thành công và nở trứng ngươi sẽ nhận được ngẫu nhiên 1 loại\n"
                                                + "đệ tử có chỉ số sức mạnh khác nhau:\n"
                                                + " + Berus: 10% chỉ số\n"
                                                + " + Broly: 13% chỉ số\n"
                                                + "+ Ubb: 15% chỉ số\n"
                                                + " + Xên Con: 18% chỉ số\n"
                                                + "Ngươi có thể mở ra loại nào tùy vào nhân phẩm của ngươi!!!\n"
                                                + "Chúc ngươi may mắn!!!",
                                        "Trái Đất", "Namec", "Xayda", "Từ chổi");
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            } else {
                                Service.gI().sendThongBao(pl, "Ngươi làm gì có đệ tử");
                            }
                            break;
                        case 1980:// hop qua skh, item 2002 xd
                            UseItem.gI().Hophdt(pl, item);
                            break;
                        // case 1979://hop qua skh, item 2002 xd
                        // pl.cauca.StartCauCa();
                        // break;
                        case 1987:// hop qua skh, item 2002 xd
                            UseItem.gI().Hophd(pl, item);
                            break;
                        case 1986:// hop qua skh, item 2002 xd
                            UseItem.gI().Hoptl(pl, item);
                            break;
                        case 1985:// hop qua skh, item 2002 xd
                            UseItem.gI().Hoptst(pl, item);
                            break;

                        case 342:
                        case 343:
                        case 344:
                        case 345:
                            if (pl.zone.items.stream().filter(it -> it != null && it.itemTemplate.type == 22)
                                    .count() < 5) {
                                Service.gI().DropVeTinh(pl, item, pl.zone, pl.location.x, pl.location.y);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            } else {
                                Service.gI().sendThongBao(pl, "Đặt ít vệ tinh thôi");
                            }
                            break;
                        case 457:
                            openThoiVangMAX(pl, item);
                            break;
                        case 569:
                            openduahau(pl, item);
                            break;
                        case 568: // quả trứng
                            if (pl.mabuEgg == null) {
                                MabuEgg.createMabuEgg(pl);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                if (pl.zone.map.mapId == (21 + pl.gender)) {
                                    if (pl.mabuEgg != null) {
                                        pl.mabuEgg.sendMabuEgg();
                                    }
                                }
                            } else {
                                Service.gI().sendThongBao(pl, "Bạn đã có quả trứng nên không thể sử dụng");
                            }
                            break;
                        case 1394:
                            if (pl.lastTimeTitle1 == 0) {
                                pl.lastTimeTitle1 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 3);
                            } else {
                                pl.lastTimeTitle1 += (1000 * 60 * 60 * 24 * 3);
                            }
                            pl.isTitleUse = true;
                            Service.gI().point(pl);
                            Service.gI().sendTitle(pl, 888);
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            Service.gI().sendThongBao(pl, "Bạn nhận được 3 ngày danh hiệu !");
                            break;
                        case 380: // cskb
                            openCSKB(pl, item);
                            break;
                        case 574: // cskb
                            // openCSKBDB(pl, item);
                            break;
                        case 1453: // Hop qua
                            Hoprandom(pl, item);
                            break;
                        case 1501: // Hop qua
                            opencaitrang(pl, item);
                            break;
                        case 1502: // Hop qua
                            hopquatanthu(pl, item);
                            break;
                        case 1503: // Hop qua
                            hopquatanthu1(pl, item);
                            break;
                        case 628:
                            openPhieuCaiTrangHaiTac(pl, item);
                        case 381: // cuồng nộ
                        case 382: // bổ huyết
                        case 383: // bổ khí
                        case 384: // giáp xên
                        case 385: // ẩn danh
                        case 379: // máy dò capsule
                        case 2037: // máy dò cosmos
                        case 663: // bánh pudding
                        case 664: // xúc xíc
                        case 665: // kem dâu
                        case 666: // mì ly
                        case 667: // sushi
                        case 1099:
                        case 1100:
                        case 1101:
                        case 1102:
                        case 1103:
                        case 1978:
                        case 1233:
                        case 1234:
                        case 1235:
                        case 1560:
                        case 1642:
                        case 1643:
                        case 1644:
                            useItemTime(pl, item);
                            break;
                        case 570:
                            openWoodChest(pl, item);
                            break;
                        case 1611:
                            if (pl != null && pl.zone.map.mapId >= 174 && pl.zone.map.mapId <= 176) {
                                int time = Util.nextInt(10000, 20000);
                                EffectSkillService.gI().setBlindDCTT(pl, System.currentTimeMillis(), time);
                                EffectSkillService.gI().sendEffectPlayer(pl, pl, EffectSkillService.TURN_ON_EFFECT,
                                        EffectSkillService.CANCAUCAOCAP_EFFECT);
                                ItemTimeService.gI().sendItemTime(pl, 21341, time / 1000);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                try {
                                    Thread.sleep(time);
                                } catch (Exception e) {
                                }
                                ItemService.gI().OpenItem1611(pl, item);
                            } else {
                                Service.gI().sendThongBao(pl, "Đây Không Phải Map Bắt Bọ");
                            }
                            break;
                        case 1684:
                            if (pl != null && MapService.gI().isMapCauCa(pl.zone.map.mapId)) {
                                int time = Util.nextInt(10000, 20000);
                                EffectSkillService.gI().setBlindDCTT(pl, System.currentTimeMillis(), time);
                                EffectSkillService.gI().sendEffectPlayer(pl, pl, EffectSkillService.TURN_ON_EFFECT,
                                        EffectSkillService.CANCAUCAOCAP_EFFECT);
                                ItemTimeService.gI().sendItemTime(pl, 21884, time / 1000);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                try {
                                    Service.gI().addEffectChar(pl, 222, 1, -1, -1, 0);
                                    Thread.sleep(time);
                                } catch (Exception e) {
                                }
                                ItemService.gI().OpenItem1684(pl, item);
                            } else {
                                Service.gI().sendThongBao(pl, "Bạn Phải Đến Nơi Nào Có Nước");
                            }
                            break;
                        case 1639:
                            kem(pl, item);
                            break;
                        case 1641:
                            quekem(pl, item);
                            break;
                        case 1653:
                            if (pl.pet != null) {
                                if (pl.pet.playerSkill.skills.get(1).skillId != -1) {
                                    pl.pet.openskillKAME();
                                    InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                } else {
                                    Service.gI().sendThongBao(pl, "Ít nhất đệ tử ngươi phải có chiêu 2 chứ!");

                                    return;
                                }
                            } else {
                                Service.gI().sendThongBao(pl, "Ngươi làm gì có đệ tử?");
                                return;
                            }
                            break;
                        case 1654:
                            if (pl.pet != null) {
                                if (pl.pet.playerSkill.skills.get(1).skillId != -1) {
                                    pl.pet.openskillTDHS();
                                    InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                } else {
                                    Service.gI().sendThongBao(pl, "Ít nhất đệ tử ngươi phải có chiêu 3 chứ!");

                                    return;
                                }
                            } else {
                                Service.gI().sendThongBao(pl, "Ngươi làm gì có đệ tử?");
                                return;
                            }
                            break;
                        case 1655:
                            if (pl.pet != null) {
                                if (pl.pet.playerSkill.skills.get(1).skillId != -1) {
                                    pl.pet.openskillKhi();
                                    InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                } else {
                                    Service.gI().sendThongBao(pl, "Ít nhất đệ tử ngươi phải có chiêu 4 chứ!");

                                    return;
                                }
                            } else {
                                Service.gI().sendThongBao(pl, "Ngươi làm gì có đệ tử?");
                                return;
                            }
                            break;
                        case 1006:
                            xocavang(pl, item);
                            break;
                        case 1005:
                            xocaxanh(pl, item);
                            break;
                        case 1649:
                            tanthu(pl, item);
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            break;
                        case 1278:
                            RuongItemCap2(pl, item);
                            break;
                        case 521: // tdlt
                            useTDLT(pl, item);
                            break;
                        case 454: // bông tai
                            UseItem.gI().usePorata(pl);
                            break;
                        case 193: // gói 10 viên capsule
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                        case 194: // capsule đặc biệt
                            openCapsuleUI(pl);
                            break;
                        case 401: // đổi đệ tử
                            changePet(pl, item);
                            break;
                        case 1108: // đổi đệ tử
                            changeBerusPet(pl, item);
                            break;
                        case 722: // đổi đệ tử
                            changePetPic(pl, item);
                            break;
                        case 402: // sách nâng chiêu 1 đệ tử
                        case 403: // sách nâng chiêu 2 đệ tử
                        case 404: // sách nâng chiêu 3 đệ tử
                        case 759: // sách nâng chiêu 4 đệ tử
                            upSkillPet(pl, item);
                            break;
                        case 921: // bông tai c2
                            UseItem.gI().usePorata2(pl);
                            break;
                        case 1405:
                        case 1155:
                            UseItem.gI().usePorata3(pl);
                            break;
                        case 1156:
                        case 1406:
                            UseItem.gI().usePorata4(pl);
                            break;

                        case 2000:// hop qua skh, item 2000 td
                        case 2001:// hop qua skh, item 2001 nm
                        case 2002:// hop qua skh, item 2002 xd
                            UseItem.gI().ItemSKH(pl, item);
                            break;
                        // case 1105://hop qua skh, item 2002 xd
                        // UseItem.gI().Hopts(pl, item);
                        // break;
                        case 1997:// hop qua skh, item 2002 xd
                            Openhopct(pl, item);
                            break;
                        case 1998:// hop qua skh, item 2002 xd
                            Openhopflagbag(pl, item);
                            break;
                        case 1999:// hop qua skh, item 2002 xd
                            Openhoppet(pl, item);
                            break;
                        case 1457:// hop qua skh, item 2002 xd
                            Openhoppet(pl, item);
                            break;
                        // case 1478://hop qua skh, item 2002 xd
                        // tutien(pl, item);
                        // InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                        // break;

                        case 1458:// hop qua skh, item 2002 xd
                            Openhoppet(pl, item);
                            break;

                        case 2003:// hop qua skh, item 2003 td
                        case 2004:// hop qua skh, item 2004 nm
                        case 2005:// hop qua skh, item 2005 xd
                            UseItem.gI().ItemDHD(pl, item);
                            break;
                        case 736:
                            ItemService.gI().OpenItem736(pl, item);
                            break;
                        case 987:
                            Service.gI().sendThongBao(pl, "Bảo vệ trang bị không bị rớt cấp"); // đá bảo vệ
                            break;
                        case 1098:
                            useItemHopQuaTanThu(pl, item);
                            break;
                        case 648:
                            UseItem.gI().quagiangsinh(pl, item);
                            break;
                        case 1128:
                            openDaBaoVe(pl, item);
                            break;
                        case 1129:
                            openSPL(pl, item);
                            break;
                        case 1130:
                            openDaNangCap(pl, item);
                            break;
                        case 1131:
                            if (pl.pet != null) {
                                if (pl.pet.playerSkill.skills.get(1).skillId != -1) {
                                    pl.pet.openSkill2();
                                    if (pl.pet.playerSkill.skills.get(2).skillId != -1) {
                                        pl.pet.openSkill3();
                                    }
                                    InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                } else {
                                    Service.gI().sendThongBao(pl, "Ít nhất đệ tử ngươi phải có chiêu 2 chứ!");

                                    return;
                                }
                            } else {
                                Service.gI().sendThongBao(pl, "Ngươi làm gì có đệ tử?");
                                return;
                            }
                            break;
                        case 1132:

                            SkillService.gI().learSkillSpecial(pl, Skill.SUPER_KAME);
                            break;
                        case 1133:
                            SkillService.gI().learSkillSpecial(pl, Skill.MA_PHONG_BA);
                            break;
                        case 1134:
                            // InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            // InventoryServiceNew.gI().sendItemBags(pl);
                            SkillService.gI().learSkillSpecial(pl, Skill.LIEN_HOAN_CHUONG);
                            break;
                        case 2006:
                            Input.gI().createFormChangeNameByItem(pl);
                            break;
                        case 999:
                            if (pl.pet == null) {
                                Service.gI().sendThongBao(pl, "Ngươi làm gì có đệ tử?");
                                break;
                            }

                            if (pl.pet.playerSkill.skills.get(1).skillId != -1
                                    && pl.pet.playerSkill.skills.get(2).skillId != -1) {
                                pl.pet.openSkill2();
                                pl.pet.openSkill3();
                                InventoryServiceNew.gI().subQuantityItem(pl.inventory.itemsBag, item, 1);
                                InventoryServiceNew.gI().sendItemBags(pl);
                                Service.gI().sendThongBao(pl, "Đã đổi thành công chiêu 2 3 đệ tử");
                            } else {
                                Service.gI().sendThongBao(pl, "Ít nhất đệ tử ngươi phải có chiêu 2 chứ!");
                            }
                            break;

                        case 2027:
                        case 2028: {
                            if (InventoryServiceNew.gI().getCountEmptyBag(pl) == 0) {
                                Service.gI().sendThongBao(pl, "Hành trang không đủ chỗ trống");
                            } else {
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Item linhThu = ItemService.gI().createNewItem((short) Util.nextInt(2019, 2026));
                                linhThu.itemOptions.add(new Item.ItemOption(50, Util.nextInt(2, 10)));
                                linhThu.itemOptions.add(new Item.ItemOption(77, Util.nextInt(2, 5)));
                                linhThu.itemOptions.add(new Item.ItemOption(103, Util.nextInt(2, 5)));
                                linhThu.itemOptions.add(new Item.ItemOption(95, Util.nextInt(1, 3)));
                                linhThu.itemOptions.add(new Item.ItemOption(96, Util.nextInt(1, 3)));
                                InventoryServiceNew.gI().addItemBag(pl, linhThu);
                                InventoryServiceNew.gI().sendItemBags(pl);
                                Service.gI().sendThongBao(pl,
                                        "Chúc mừng bạn nhận được Linh thú " + linhThu.template.name);
                            }
                            break;

                        }
                    }
                    break;
            }
            InventoryServiceNew.gI().sendItemBags(pl);
        } else {
            Service.gI().sendThongBaoOK(pl, "Sức mạnh không đủ yêu cầu");
        }
    }

    private void fixPetBTH(Player pl) {
        if (pl.newpet != null) {
            ChangeMapService.gI().exitMap(pl.newpet);
            pl.newpet.dispose();
            pl.newpet = null;
        }
    }

    private void Openhopct(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            boolean vinhvien = Manager.TotalCaiTrang >= 500;
            int[] rdct = new int[] { 1290, 1291, 1281, 1302, 1282, 1296, 1297, 1298, 1295, 1301, 1300, 1307, 1306, 1308,
                    1309, 1310 };
            int[] rdop = new int[] { 5, 14, 94, 108, 97 };
            int randomct = new Random().nextInt(rdct.length);
            int randomop = new Random().nextInt(rdop.length);
            Item ct = ItemService.gI().createNewItem((short) rdct[randomct]);
            Item vt = ItemService.gI().createNewItem((short) Util.nextInt(16, 16));
            if (!vinhvien) {
                ct.itemOptions.add(new Item.ItemOption(50, Util.nextInt(20, 27)));
                ct.itemOptions.add(new Item.ItemOption(77, Util.nextInt(20, 27)));
                ct.itemOptions.add(new Item.ItemOption(103, Util.nextInt(20, 27)));
                ct.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 5)));
                Manager.TotalCaiTrang += 1;
            } else {
                ct.itemOptions.add(new Item.ItemOption(50, Util.nextInt(25, 27)));
                ct.itemOptions.add(new Item.ItemOption(77, Util.nextInt(25, 30)));
                ct.itemOptions.add(new Item.ItemOption(103, Util.nextInt(25, 30)));
                Manager.TotalCaiTrang = 0;
            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().addItemBag(pl, ct);
            InventoryServiceNew.gI().addItemBag(pl, vt);
            InventoryServiceNew.gI().sendItemBags(pl);
            Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + ct.template.name + " và " + vt.template.name);
        } else {
            Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
        }
    }

    private void Openhopflagbag(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            boolean vinhvien = Manager.TotalFlag >= 500;
            int[] rdfl = new int[] { 1157, 1203, 1204, 1205, 1206, 1207, 954, 955, 1220, 1221, 966, 1222, 1226, 1228,
                    1229, 467, 468, 469, 470, 982, 471, 983, 994, 995, 740, 996, 741, 997, 998, 999, 1000, 745,
                    1001, 1007, 2035, 1013, 1021, 766, 1022, 767, 1023 };
            int[] rdop = new int[] { 50, 77, 103 };
            int[] daysrandom = new int[] { 3, 7, 15, 30 };
            int randomfl = new Random().nextInt(rdfl.length);
            int randomop = new Random().nextInt(rdop.length);
            Item fl = ItemService.gI().createNewItem((short) rdfl[randomfl]);
            Item vt = ItemService.gI().createNewItem((short) Util.nextInt(16, 16));
            if (!vinhvien) {
                fl.itemOptions.add(new Item.ItemOption(rdop[randomop], Util.nextInt(5, 10)));
                fl.itemOptions.add(new Item.ItemOption(93, daysrandom[Util.nextInt(daysrandom.length)]));
                Manager.TotalFlag += 1;
            } else {
                fl.itemOptions.add(new Item.ItemOption(rdop[randomop], Util.nextInt(5, 10)));
                Manager.TotalFlag = 0;
            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().addItemBag(pl, fl);
            InventoryServiceNew.gI().addItemBag(pl, vt);
            InventoryServiceNew.gI().sendItemBags(pl);
            Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + fl.template.name + " và " + vt.template.name);
        } else {
            Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
        }
    }

    private void Openhoppet(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            int[] rdpet = new int[] { 1311, 1312, 1313 };
            int[] rdop = new int[] { 50, 77, 103 };
            int[] daysrandom = new int[] { 3, 7 };
            boolean vinhvien = Manager.TotalPet >= 500;
            int randompet = new Random().nextInt(rdpet.length);
            int randomop = new Random().nextInt(rdop.length);
            Item pet = ItemService.gI().createNewItem((short) rdpet[randompet]);
            Item vt = ItemService.gI().createNewItem((short) Util.nextInt(16, 16));
            if (!vinhvien) {
                pet.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5, 10)));
                pet.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5, 10)));
                pet.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5, 10)));
                pet.itemOptions.add(new Item.ItemOption(93, daysrandom[Util.nextInt(daysrandom.length)]));
                Manager.TotalPet += 1;
            } else {
                pet.itemOptions.add(new Item.ItemOption(50, Util.nextInt(8, 13)));
                pet.itemOptions.add(new Item.ItemOption(77, Util.nextInt(8, 12)));
                pet.itemOptions.add(new Item.ItemOption(103, Util.nextInt(10, 14)));
                Manager.TotalPet = 0;
            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().addItemBag(pl, pet);
            InventoryServiceNew.gI().addItemBag(pl, vt);
            InventoryServiceNew.gI().sendItemBags(pl);
            Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + pet.template.name + " và " + vt.template.name);
        } else {
            Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
        }
    }

    private void hopthuong(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            short[] rac = { 860, 421, 422, 1311, 1312, 1313 };
            byte index2 = (byte) Util.nextInt(0, rac.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(rac[index2]);
            if (it.template.id == 739) {
                it.itemOptions.add(new ItemOption(50, Util.nextInt(10, 35)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(10, 35)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(10, 35)));
                it.itemOptions.add(new ItemOption(14, Util.nextInt(10, 35)));
            } else {
                it.itemOptions.add(new ItemOption(50, Util.nextInt(10, 35)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(10, 35)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(10, 35)));
                it.itemOptions.add(new ItemOption(14, Util.nextInt(10, 35)));
            }
            if (Util.isTrue(99, 100)) {
                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 5)));
            }
            it.itemOptions.add(new ItemOption(30, 0));
            InventoryServiceNew.gI().addItemBag(pl, it);
            icon[1] = it.template.iconID;
            Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + it.template.name);
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    // private void tutien(Player player, Item item) {
    // if (player.Bkttutien[2] > 0) {
    // long exptt = (item.quantity * Util.nextInt(50, 300));
    // if (player.BktDauLaDaiLuc[11] == 1) {
    // exptt += exptt * (player.BktDauLaDaiLuc[12] / 5 >= 20 ? 20
    // : player.BktDauLaDaiLuc[12] / 5) / 100;
    // }
    // exptt += exptt * (15 * player.Bkttutien[2]) / 100;
    // if (player.Captutien >= 300) {
    // exptt += exptt / 10;
    // }
    // player.Bkttutien[0] += exptt;
    // Service.gI().sendThongBao(player, "bạn nhận đc: " + exptt + " Exp tu tiên.");
    // } else {
    // player.setDie(player);
    // Service.gI().sendThongBao(player,
    // "Do bạn chưa mở thiên phú chưa thể dung hòa với long đan nên đã chết");
    // }
    // }
    private void hopcaocap(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            short[] rac = { 860, 421, 422, 1311, 1312, 1313, 1441 };
            byte index2 = (byte) Util.nextInt(0, rac.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(rac[index2]);
            if (it.template.id == 739) {
                it.itemOptions.add(new ItemOption(50, Util.nextInt(10, 40)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(10, 40)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(10, 40)));
                it.itemOptions.add(new ItemOption(14, Util.nextInt(10, 40)));
            } else {
                it.itemOptions.add(new ItemOption(50, Util.nextInt(10, 40)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(10, 40)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(10, 40)));
                it.itemOptions.add(new ItemOption(14, Util.nextInt(10, 40)));
            }
            if (Util.isTrue(99, 100)) {
                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 5)));
            }
            it.itemOptions.add(new ItemOption(30, 0));
            InventoryServiceNew.gI().addItemBag(pl, it);
            icon[1] = it.template.iconID;
            Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + it.template.name);
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    public void UseCard(Player pl, Item item) {
        RadarCard radarTemplate = RadarService.gI().RADAR_TEMPLATE.stream().filter(c -> c.Id == item.template.id)
                .findFirst().orElse(null);
        if (radarTemplate == null) {
            return;
        }
        if (radarTemplate.Require != -1) {
            RadarCard radarRequireTemplate = RadarService.gI().RADAR_TEMPLATE.stream()
                    .filter(r -> r.Id == radarTemplate.Require).findFirst().orElse(null);
            if (radarRequireTemplate == null) {
                return;
            }
            Card cardRequire = pl.Cards.stream().filter(r -> r.Id == radarRequireTemplate.Id).findFirst().orElse(null);
            if (cardRequire == null || cardRequire.Level < radarTemplate.RequireLevel) {
                Service.gI().sendThongBao(pl, "Bạn cần sưu tầm " + radarRequireTemplate.Name + " ở cấp độ "
                        + radarTemplate.RequireLevel + " mới có thể sử dụng thẻ này");
                return;
            }
        }
        Card card = pl.Cards.stream().filter(r -> r.Id == item.template.id).findFirst().orElse(null);
        if (card == null) {
            Card newCard = new Card(item.template.id, (byte) 1, radarTemplate.Max, (byte) -1, radarTemplate.Options);
            if (pl.Cards.add(newCard)) {
                RadarService.gI().RadarSetAmount(pl, newCard.Id, newCard.Amount, newCard.MaxAmount);
                RadarService.gI().RadarSetLevel(pl, newCard.Id, newCard.Level);
                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                InventoryServiceNew.gI().sendItemBags(pl);
            }
        } else {
            if (card.Level >= 2) {
                Service.gI().sendThongBao(pl, "Thẻ này đã đạt cấp tối đa");
                return;
            }
            card.Amount++;
            if (card.Amount >= card.MaxAmount) {
                card.Amount = 0;
                if (card.Level == -1) {
                    card.Level = 1;
                } else {
                    card.Level++;
                }
                Service.gI().point(pl);
            }
            RadarService.gI().RadarSetAmount(pl, card.Id, card.Amount, card.MaxAmount);
            RadarService.gI().RadarSetLevel(pl, card.Id, card.Level);
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);
        }
    }

    private void useItemChangeFlagBag(Player player, Item item) {
        switch (item.template.id) {
            case 994: // vỏ ốc
                break;
            case 995: // cây kem
                break;
            case 996: // cá heo
                break;
            case 997: // con diều
                break;
            case 998: // diều rồng
                break;
            case 999: // mèo mun
                if (!player.effectFlagBag.useMeoMun) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useMeoMun = !player.effectFlagBag.useMeoMun;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 1000: // xiên cá
                if (!player.effectFlagBag.useXienCa) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useXienCa = !player.effectFlagBag.useXienCa;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 1001: // phóng heo
                if (!player.effectFlagBag.usePhongHeo) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.usePhongHeo = !player.effectFlagBag.usePhongHeo;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 1202: // Hào quang
                if (!player.effectFlagBag.useHaoQuang) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useHaoQuang = !player.effectFlagBag.useHaoQuang;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
        }
        Service.gI().point(player);
        Service.gI().sendFlagBag(player);
    }

    private void changePet(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender + 1;
            if (gender > 2) {
                gender = 0;
            }
            PetService.gI().changeNormalPet(player, gender);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.gI().sendThongBao(player, "Không thể thực hiện cho đệ tử !");
        }
    }

    private void changeBerusPet(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeBerusPet(player, gender);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.gI().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changePetPic(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changePicPet(player, gender);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.gI().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void openDaBaoVe(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = { 987, 987 };
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.itemOptions.add(new ItemOption(73, 0));
            newItem.quantity = (short) Util.nextInt(1, 10);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openduahau(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item hongngoc = ItemService.gI().createNewItem((short) 861);
            hongngoc.quantity += Util.nextInt(10, 100);
            Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + hongngoc.template.name + " " + hongngoc.quantity);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().addItemBag(player, hongngoc);
            icon[1] = hongngoc.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openSPL(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = { 441, 442, 443, 444, 445, 446, 447 };
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.itemOptions.add(new ItemOption(73, 0));
            newItem.quantity = (short) Util.nextInt(1, 10);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openDaNangCap(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = { 220, 221, 222, 223, 224 };
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.itemOptions.add(new ItemOption(73, 0));
            newItem.quantity = (short) Util.nextInt(1, 10);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openManhTS(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = { 1066, 1067, 1068, 1069, 1070 };
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.itemOptions.add(new ItemOption(73, 0));
            newItem.quantity = (short) Util.nextInt(1, 99);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);
            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openGoiDau1(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = { 13, 13 };
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.quantity = (short) Util.nextInt(99, 99);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openGoiDau2(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = { 60, 60 };
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.quantity = (short) Util.nextInt(99, 99);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openGoiDau3(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = { 61, 61 };
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.quantity = (short) Util.nextInt(99, 99);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openGoiDau4(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = { 62, 62 };
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.quantity = (short) Util.nextInt(99, 99);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openGoiDau5(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = { 63, 63 };
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.quantity = (short) Util.nextInt(99, 99);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openGoiDau6(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = { 64, 64 };
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.quantity = (short) Util.nextInt(99, 99);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openGoiDau7(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = { 65, 65 };
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.quantity = (short) Util.nextInt(99, 99);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openGoiDau8(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = { 352, 352 };
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.quantity = (short) Util.nextInt(99, 99);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openGoiDau9(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = { 523, 523 };
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.quantity = (short) Util.nextInt(99, 99);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openPhieuCaiTrangHaiTac(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            Item ct = ItemService.gI().createNewItem((short) Util.nextInt(618, 626));
            ct.itemOptions.add(new ItemOption(147, 3));
            ct.itemOptions.add(new ItemOption(77, 3));
            ct.itemOptions.add(new ItemOption(103, 3));
            ct.itemOptions.add(new ItemOption(149, 0));
            if (item.template.id == 2006) {
                ct.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
            } else if (item.template.id == 2007) {
                ct.itemOptions.add(new ItemOption(93, Util.nextInt(7, 30)));
            }
            InventoryServiceNew.gI().addItemBag(pl, ct);
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, item.template.iconID, ct.template.iconID);
        } else {
            Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void eatGrapes(Player pl, Item item) {
        int percentCurrentStatima = pl.nPoint.stamina * 100 / pl.nPoint.maxStamina;
        if (percentCurrentStatima > 50) {
            Service.gI().sendThongBao(pl, "Thể lực vẫn còn trên 50%");
            return;
        } else if (item.template.id == 211) {
            pl.nPoint.stamina = pl.nPoint.maxStamina;
            Service.gI().sendThongBao(pl, "Thể lực của bạn đã được hồi phục 100%");
        } else if (item.template.id == 212) {
            pl.nPoint.stamina += (pl.nPoint.maxStamina * 20 / 100);
            Service.gI().sendThongBao(pl, "Thể lực của bạn đã được hồi phục 20%");
        }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().sendItemBags(pl);
        PlayerService.gI().sendCurrentStamina(pl);
    }

    private void randomDB(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = { 861 };
            int[][] gold = { { 15000, 50000 } };
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            if (index <= 3) {
                pl.inventory.ruby += Util.nextInt(gold[0][0], gold[0][1]);
                if (pl.inventory.ruby > Inventory.LIMIT_GOLD) {
                    pl.inventory.ruby = (int) Inventory.LIMIT_GOLD;
                }
                PlayerService.gI().sendInfoHpMpMoney(pl);
                icon[1] = 7743;
            } else {
                Item it = ItemService.gI().createNewItem(temp[index]);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryServiceNew.gI().addItemBag(pl, it);
                icon[1] = it.template.iconID;
            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);

            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void openCSKB(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = { 76, 188, 189, 190, 381, 382, 383, 384, 385 };
            int[][] gold = { { 5000, 20000 } };
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            if (index <= 3) {
                pl.inventory.gold += Util.nextInt(gold[0][0], gold[0][1]);
                if (pl.inventory.gold > Inventory.LIMIT_GOLD) {
                    pl.inventory.gold = Inventory.LIMIT_GOLD;
                }
                PlayerService.gI().sendInfoHpMpMoney(pl);
                icon[1] = 930;
            } else {
                Item it = ItemService.gI().createNewItem(temp[index]);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryServiceNew.gI().addItemBag(pl, it);
                icon[1] = it.template.iconID;
            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);

            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    public void hopquatanthu(Player player, Item item) {
        switch (player.gender) {
            case 0: {
                Item itemReward = ItemService.gI().createNewItem((short) 0);
                Item itemReward1 = ItemService.gI().createNewItem((short) 6);
                Item itemReward2 = ItemService.gI().createNewItem((short) 12);
                Item itemReward3 = ItemService.gI().createNewItem((short) 21);
                Item itemReward4 = ItemService.gI().createNewItem((short) 27);
                itemReward.quantity = 1;
                itemReward1.quantity = 1;
                itemReward2.quantity = 1;
                itemReward3.quantity = 1;
                itemReward4.quantity = 1;
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
                    itemReward.itemOptions.add(new ItemOption(47, 5));
                    itemReward1.itemOptions.add(new ItemOption(7, 30));
                    itemReward2.itemOptions.add(new ItemOption(14, 1));
                    itemReward3.itemOptions.add(new ItemOption(0, 5));
                    itemReward4.itemOptions.add(new ItemOption(6, 30));

                    itemReward.itemOptions.add(new ItemOption(107, 6));
                    itemReward1.itemOptions.add(new ItemOption(107, 6));
                    itemReward2.itemOptions.add(new ItemOption(107, 6));
                    itemReward3.itemOptions.add(new ItemOption(107, 6));
                    itemReward4.itemOptions.add(new ItemOption(107, 6));

                    itemReward.itemOptions.add(new ItemOption(30, 1));
                    itemReward1.itemOptions.add(new ItemOption(30, 1));
                    itemReward2.itemOptions.add(new ItemOption(30, 1));
                    itemReward3.itemOptions.add(new ItemOption(30, 1));
                    itemReward4.itemOptions.add(new ItemOption(30, 1));

                    InventoryServiceNew.gI().addItemBag(player, itemReward);
                    InventoryServiceNew.gI().addItemBag(player, itemReward1);
                    InventoryServiceNew.gI().addItemBag(player, itemReward2);
                    InventoryServiceNew.gI().addItemBag(player, itemReward3);
                    InventoryServiceNew.gI().addItemBag(player, itemReward4);

                    Service.getInstance().sendThongBao(player, "Bạn đã nhận được set đồ 5 sao !");
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
                    InventoryServiceNew.gI().sendItemBags(player);
                } else {
                    Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
                }
            }
                break;
            case 1: {
                Item itemReward = ItemService.gI().createNewItem((short) 1);
                Item itemReward1 = ItemService.gI().createNewItem((short) 7);
                Item itemReward2 = ItemService.gI().createNewItem((short) 12);
                Item itemReward3 = ItemService.gI().createNewItem((short) 22);
                Item itemReward4 = ItemService.gI().createNewItem((short) 28);
                itemReward.quantity = 1;
                itemReward1.quantity = 1;
                itemReward2.quantity = 1;
                itemReward3.quantity = 1;
                itemReward4.quantity = 1;
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {

                    itemReward.itemOptions.add(new ItemOption(47, 5));
                    itemReward1.itemOptions.add(new ItemOption(7, 30));
                    itemReward2.itemOptions.add(new ItemOption(14, 1));
                    itemReward3.itemOptions.add(new ItemOption(0, 5));
                    itemReward4.itemOptions.add(new ItemOption(6, 30));

                    itemReward.itemOptions.add(new ItemOption(107, 6));
                    itemReward1.itemOptions.add(new ItemOption(107, 6));
                    itemReward2.itemOptions.add(new ItemOption(107, 6));
                    itemReward3.itemOptions.add(new ItemOption(107, 6));
                    itemReward4.itemOptions.add(new ItemOption(107, 6));

                    itemReward.itemOptions.add(new ItemOption(30, 1));
                    itemReward1.itemOptions.add(new ItemOption(30, 1));
                    itemReward2.itemOptions.add(new ItemOption(30, 1));
                    itemReward3.itemOptions.add(new ItemOption(30, 1));
                    itemReward4.itemOptions.add(new ItemOption(30, 1));

                    InventoryServiceNew.gI().addItemBag(player, itemReward);
                    InventoryServiceNew.gI().addItemBag(player, itemReward1);
                    InventoryServiceNew.gI().addItemBag(player, itemReward2);
                    InventoryServiceNew.gI().addItemBag(player, itemReward3);
                    InventoryServiceNew.gI().addItemBag(player, itemReward4);

                    Service.getInstance().sendThongBao(player, "Bạn đã nhận được set đồ 5 sao !");
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
                    InventoryServiceNew.gI().sendItemBags(player);
                } else {
                    Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
                }
            }
                break;
            case 2: {
                Item itemReward = ItemService.gI().createNewItem((short) 2);
                Item itemReward1 = ItemService.gI().createNewItem((short) 8);
                Item itemReward2 = ItemService.gI().createNewItem((short) 12);
                Item itemReward3 = ItemService.gI().createNewItem((short) 23);
                Item itemReward4 = ItemService.gI().createNewItem((short) 29);
                itemReward.quantity = 1;
                itemReward1.quantity = 1;
                itemReward2.quantity = 1;
                itemReward3.quantity = 1;
                itemReward4.quantity = 1;
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
                    itemReward.itemOptions.add(new ItemOption(47, 5));
                    itemReward1.itemOptions.add(new ItemOption(7, 30));
                    itemReward2.itemOptions.add(new ItemOption(14, 1));
                    itemReward3.itemOptions.add(new ItemOption(0, 5));
                    itemReward4.itemOptions.add(new ItemOption(6, 30));

                    itemReward.itemOptions.add(new ItemOption(107, 6));
                    itemReward1.itemOptions.add(new ItemOption(107, 6));
                    itemReward2.itemOptions.add(new ItemOption(107, 6));
                    itemReward3.itemOptions.add(new ItemOption(107, 6));
                    itemReward4.itemOptions.add(new ItemOption(107, 6));

                    itemReward.itemOptions.add(new ItemOption(30, 1));
                    itemReward1.itemOptions.add(new ItemOption(30, 1));
                    itemReward2.itemOptions.add(new ItemOption(30, 1));
                    itemReward3.itemOptions.add(new ItemOption(30, 1));
                    itemReward4.itemOptions.add(new ItemOption(30, 1));

                    InventoryServiceNew.gI().addItemBag(player, itemReward);
                    InventoryServiceNew.gI().addItemBag(player, itemReward1);
                    InventoryServiceNew.gI().addItemBag(player, itemReward2);
                    InventoryServiceNew.gI().addItemBag(player, itemReward3);
                    InventoryServiceNew.gI().addItemBag(player, itemReward4);

                    Service.getInstance().sendThongBao(player, "Bạn đã nhận được set đồ 5 sao !");
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
                    InventoryServiceNew.gI().sendItemBags(player);
                } else {
                    Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
                }
            }
                break;
        }
    }

    private void opencaitrang(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 1) {
            int id = Util.nextInt(0, 100);
            int[] rdct = new int[] { 1340 };
            int[] rdop = new int[] { 5, 14, 94, 108, 97, 106, 107 };
            int randomct = new Random().nextInt(rdct.length);
            int randomop = new Random().nextInt(rdop.length);
            Item ct = ItemService.gI().createNewItem((short) rdct[randomct]);

            if (id <= 90) {
                ct.itemOptions.add(new Item.ItemOption(50, Util.nextInt(30, 30)));
                ct.itemOptions.add(new Item.ItemOption(77, Util.nextInt(30, 30)));
                ct.itemOptions.add(new Item.ItemOption(103, Util.nextInt(30, 30)));
                ct.itemOptions.add(new Item.ItemOption(5, Util.nextInt(10, 10)));

                ct.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 3)));
            } else {
                ct.itemOptions.add(new Item.ItemOption(50, Util.nextInt(30, 30)));
                ct.itemOptions.add(new Item.ItemOption(77, Util.nextInt(30, 30)));
                ct.itemOptions.add(new Item.ItemOption(103, Util.nextInt(30, 30)));
                ct.itemOptions.add(new Item.ItemOption(5, Util.nextInt(10, 10)));
            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().addItemBag(pl, ct);

            InventoryServiceNew.gI().sendItemBags(pl);
            Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + ct.template.name);
        } else {
            Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 2 ô trống trong hành trang.");
        }
    }

    private void kem(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            short[] rac = { 1641 };
            byte index2 = (byte) Util.nextInt(0, rac.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(rac[index2]);
            if (it.template.id == 1641) {
                it.itemOptions.add(new ItemOption(174, 2024));
            }
            InventoryServiceNew.gI().addItemBag(pl, it);
            icon[1] = it.template.iconID;
            Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + it.template.name);
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void quekem(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            short[] rac = { 1630, 1074, 1075, 1076, 1077, 1079, 1080, 1081, 1082 };
            byte index2 = (byte) Util.nextInt(0, rac.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            if (Util.isTrue(30, 100)) {
                Item it = ItemService.gI().createNewItem(rac[index2]);
                if (it.template.id == 1630) {
                    it.itemOptions.add(new ItemOption(50, Util.nextInt(10, 20)));
                    it.itemOptions.add(new ItemOption(77, Util.nextInt(10, 20)));
                    it.itemOptions.add(new ItemOption(103, Util.nextInt(10, 20)));
                    it.itemOptions.add(new ItemOption(14, Util.nextInt(10, 20)));
                    it.itemOptions.add(new ItemOption(174, 2024));
                } else {
                    it.itemOptions.add(new ItemOption(174, 2024));
                }
                if (Util.isTrue(95, 100)) {
                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 5)));
                }

                it.itemOptions.add(new ItemOption(30, 0));
                InventoryServiceNew.gI().addItemBag(pl, it);
                pl.inventory.gold += Util.nextInt(30000, 100000);
                pl.kemtraicay += 1;
                icon[1] = it.template.iconID;
                Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + it.template.name + " Và...Vàng");
            } else {
                Service.gI().sendThongBao(pl, "Chúc Bạn May Mắn Lần Sau");
            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void xocavang(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            short[] rac = { 1296, 1023, 994, 996, 997, 998, 1648 };
            byte index2 = (byte) Util.nextInt(0, rac.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(rac[index2]);
            if (it.template.id == 1296) {
                it.itemOptions.add(new ItemOption(50, Util.nextInt(20, 35)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(20, 35)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(20, 35)));
                it.itemOptions.add(new ItemOption(14, Util.nextInt(20, 35)));
                it.itemOptions.add(new ItemOption(174, 2024));
            } else {
                it.itemOptions.add(new ItemOption(50, Util.nextInt(10, 20)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(10, 20)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(10, 20)));
                it.itemOptions.add(new ItemOption(14, Util.nextInt(10, 20)));
                it.itemOptions.add(new ItemOption(174, 2024));
            }
            if (Util.isTrue(95, 100)) {
                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 5)));
            }

            it.itemOptions.add(new ItemOption(30, 0));
            InventoryServiceNew.gI().addItemBag(pl, it);
            pl.inventory.gold += Util.nextInt(30000, 100000);
            pl.point_hungvuong += 1;
            icon[1] = it.template.iconID;
            Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + it.template.name + " Và...Vàng");
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void quagiangsinh(Player pl, Item item) {
        if (Util.isTrue(50, 100)) {
            Item gold = ItemService.gI().createNewItem((short) 76);
            int goldt = Util.nextInt(300000, 500000);
            gold.quantity += goldt;
            InventoryServiceNew.gI().addItemBag(pl, gold);
            Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + goldt + " Vàng");
        } else if (Util.isTrue(30, 100)) {
            Item ngocx = ItemService.gI().createNewItem((short) 77);
            int ngocxanh = Util.nextInt(1000, 10000);
            ngocx.quantity += ngocxanh;
            InventoryServiceNew.gI().addItemBag(pl, ngocx);
            Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + ngocxanh + " Ngọc Xanh");
        } else if (Util.isTrue(20, 100)) {
            Item ngocv = ItemService.gI().createNewItem((short) 861);
            int ruby = Util.nextInt(100, 500);
            ngocv.quantity += ruby;
            InventoryServiceNew.gI().addItemBag(pl, ngocv);
            Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + ruby + " Hồng Ngọc");
        } else {
            Item gold = ItemService.gI().createNewItem((short) 76);
            int goldt = Util.nextInt(100000, 500000);
            gold.quantity += goldt;
            InventoryServiceNew.gI().addItemBag(pl, gold);
            Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + goldt + " Vàng");
        }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);

    }

    private void tanthu(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            int[] rdbkt = new int[] { 50, 103, 77 };
            int randombkt = new Random().nextInt(rdbkt.length);
            // Item Frieren = ItemService.gI().createNewItem((short) 1630);
            Item QuyLao = ItemService.gI().createNewItem((short) 1531);

            Item lucbao = ItemService.gI().createNewItem((short) 220);
            Item saphia = ItemService.gI().createNewItem((short) 221);
            Item ruby = ItemService.gI().createNewItem((short) 222);
            Item titan = ItemService.gI().createNewItem((short) 223);
            Item thachanhtim = ItemService.gI().createNewItem((short) 224);
            Item dabaove = ItemService.gI().createNewItem((short) 987);
            short baseValue = 1385;
            short baseValuee = 1431;
            short genderModifierr = (pl.gender == 0) ? -14 : ((pl.gender == 2) ? -7 : (short) 0);
            short genderModifier = (pl.gender == 0) ? -2 : ((pl.gender == 2) ? 2 : (short) 0);
            Item sachTuyetKy = ItemService.gI().createNewItem((short) (baseValue + genderModifier));
            Item sachSkill = ItemService.gI().createNewItem((short) (baseValuee + genderModifierr));
            lucbao.quantity += 998;
            saphia.quantity += 998;
            ruby.quantity += 998;
            titan.quantity += 998;
            thachanhtim.quantity += 998;
            dabaove.quantity += 100;
            QuyLao.itemOptions.add(new ItemOption(50, 30));
            QuyLao.itemOptions.add(new ItemOption(77, 30));
            QuyLao.itemOptions.add(new ItemOption(103, 30));
            QuyLao.itemOptions.add(new ItemOption(101, 40));
            QuyLao.itemOptions.add(new ItemOption(93, 2));

            lucbao.itemOptions.add(new ItemOption(71, 0));
            saphia.itemOptions.add(new ItemOption(70, 0));
            ruby.itemOptions.add(new ItemOption(69, 0));
            titan.itemOptions.add(new ItemOption(68, 0));
            thachanhtim.itemOptions.add(new ItemOption(67, 0));
            sachTuyetKy.itemOptions.add(new Item.ItemOption(rdbkt[randombkt], Util.nextInt(5, 10)));
            sachTuyetKy.itemOptions.add(new Item.ItemOption(21, 40));
            sachTuyetKy.itemOptions.add(new Item.ItemOption(30, 0));
            sachTuyetKy.itemOptions.add(new Item.ItemOption(87, 1));
            sachTuyetKy.itemOptions.add(new Item.ItemOption(218, 5));
            sachTuyetKy.itemOptions.add(new Item.ItemOption(219, 1000));
            InventoryServiceNew.gI().addItemBag(pl, QuyLao);
            InventoryServiceNew.gI().addItemBag(pl, lucbao);
            InventoryServiceNew.gI().addItemBag(pl, saphia);
            InventoryServiceNew.gI().addItemBag(pl, ruby);
            InventoryServiceNew.gI().addItemBag(pl, titan);
            InventoryServiceNew.gI().addItemBag(pl, thachanhtim);
            InventoryServiceNew.gI().addItemBag(pl, dabaove);
            InventoryServiceNew.gI().addItemBag(pl, sachTuyetKy);
            InventoryServiceNew.gI().addItemBag(pl, sachSkill);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void xocaxanh(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            short[] rac = { 1631, 1023, 994, 996, 997, 998, 1648, 1000, 1001 };
            byte index2 = (byte) Util.nextInt(0, rac.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(rac[index2]);
            if (it.template.id == 1631) {
                it.itemOptions.add(new ItemOption(50, Util.nextInt(20, 40)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(20, 40)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(20, 40)));
                it.itemOptions.add(new ItemOption(14, Util.nextInt(20, 40)));
                it.itemOptions.add(new ItemOption(174, 2024));
            } else {
                it.itemOptions.add(new ItemOption(50, Util.nextInt(10, 25)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(10, 25)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(10, 25)));
                it.itemOptions.add(new ItemOption(14, Util.nextInt(10, 25)));
                it.itemOptions.add(new ItemOption(174, 2024));
            }
            if (Util.isTrue(95, 100)) {
                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 5)));
            }

            it.itemOptions.add(new ItemOption(30, 0));
            InventoryServiceNew.gI().addItemBag(pl, it);
            pl.inventory.gold += Util.nextInt(30000, 100000);
            pl.point_hungvuong += 1;
            icon[1] = it.template.iconID;
            Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + it.template.name + " Và...Vàng");
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void hopquatanthu1(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 1) {
            int id = Util.nextInt(0, 100);
            int[] rdct = new int[] { 1339 };

            int[] rdop = new int[] { 5, 14, 94, 108, 97, 106, 107 };
            int randomct = new Random().nextInt(rdct.length);
            int randomct1 = new Random().nextInt(rdct.length);

            Item ct = ItemService.gI().createNewItem((short) rdct[randomct]);

            if (id <= 90) {
                ct.itemOptions.add(new Item.ItemOption(0, Util.nextInt(3000, 3000)));
                ct.itemOptions.add(new Item.ItemOption(101, Util.nextInt(30, 30)));
                ct.itemOptions.add(new Item.ItemOption(19, Util.nextInt(25, 25)));

            } else {
                ct.itemOptions.add(new Item.ItemOption(0, Util.nextInt(3000, 3000)));
                ct.itemOptions.add(new Item.ItemOption(101, Util.nextInt(30, 30)));
                ct.itemOptions.add(new Item.ItemOption(19, Util.nextInt(25, 25)));
            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().addItemBag(pl, ct);

            InventoryServiceNew.gI().sendItemBags(pl);
            Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + ct.template.name);
        } else {
            Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 2 ô trống trong hành trang.");
        }
    }

    private void Hoprandom(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            short[] rac = { 1311, 1312, 1313, 1438 };
            byte index2 = (byte) Util.nextInt(0, rac.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(rac[index2]);
            if (it.template.id == 739) {
                it.itemOptions.add(new ItemOption(50, Util.nextInt(10, 15)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(10, 15)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(10, 15)));
                it.itemOptions.add(new ItemOption(14, Util.nextInt(10, 15)));
            } else {
                it.itemOptions.add(new ItemOption(50, Util.nextInt(10, 15)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(10, 15)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(10, 15)));
                it.itemOptions.add(new ItemOption(14, Util.nextInt(10, 15)));
            }
            if (Util.isTrue(95, 100)) {
                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 5)));
            }
            it.itemOptions.add(new ItemOption(30, 0));
            InventoryServiceNew.gI().addItemBag(pl, it);
            icon[1] = it.template.iconID;
            Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + it.template.name);
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    public boolean maydoboss(Player pl) {
        try {
            BossManager.gI().dobossmember(pl);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    private void hopquagiangsinh(Player pl) {
        try {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) <= 2) {
                Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 2 ô trống hành trang");
                return;
            }
            Item hopquagiangsinh = null;
            for (Item item : pl.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 2078) {
                    hopquagiangsinh = item;
                    break;
                }
            }
            if (hopquagiangsinh != null) {
                Item gaudau = ItemService.gI().createNewItem((short) 2077);
                gaudau.itemOptions.add(new ItemOption(147, Util.nextInt(5, 30)));
                gaudau.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5, 30)));
                gaudau.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5, 30)));
                gaudau.itemOptions.add(new Item.ItemOption(101, Util.nextInt(2, 25)));
                gaudau.itemOptions.add(new Item.ItemOption(211, 0));
                gaudau.itemOptions.add(new Item.ItemOption(30, 0));
                InventoryServiceNew.gI().subQuantityItemsBag(pl, hopquagiangsinh, 1);
                InventoryServiceNew.gI().addItemBag(pl, gaudau);
                InventoryServiceNew.gI().sendItemBags(pl);
                Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + gaudau.template.name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void useItemHopQuaTanThu(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = { 17, 18, 19, 20 };
            int[][] gold = { { 10000, 50000 } };
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            if (index <= 3) {
                pl.inventory.gold += Util.nextInt(gold[0][0], gold[0][1]);
                if (pl.inventory.gold > Inventory.LIMIT_GOLD) {
                    pl.inventory.gold = Inventory.LIMIT_GOLD;
                }
                PlayerService.gI().sendInfoHpMpMoney(pl);
                icon[1] = 930;
            } else {
                Item it = ItemService.gI().createNewItem(temp[index]);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryServiceNew.gI().addItemBag(pl, it);
                icon[1] = it.template.iconID;
            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);

            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void useItemTime(Player pl, Item item) {
        switch (item.template.id) {
            case 1642: // x3EXP
                pl.itemTime.lastnuocmiakhonglo = System.currentTimeMillis();
                pl.itemTime.isnuocmiakhonglo = true;
                break;
            case 1643: // x3EXP
                pl.itemTime.lastnuocmiathom = System.currentTimeMillis();
                pl.itemTime.isnuocmiathom = true;
                break;
            case 1644: // x3EXP
                pl.itemTime.lastnuocmiasaurieng = System.currentTimeMillis();
                pl.itemTime.isnuocmiasaurieng = true;
                break;

            case 1233: // x3EXP
                pl.itemTime.lastX3EXP = System.currentTimeMillis();
                pl.itemTime.isX3EXP = true;
                break;
            case 1234: // x5EXP
                pl.itemTime.lastX5EXP = System.currentTimeMillis();
                pl.itemTime.isX5EXP = true;
                break;
            case 1235: // x7EXP
                pl.itemTime.lastX7EXP = System.currentTimeMillis();
                pl.itemTime.isX7EXP = true;
                break;
            case 1978: // x2EXP
                pl.itemTime.lastX2EXP = System.currentTimeMillis();
                pl.itemTime.isX2EXP = true;
                break;
            case 382: // bổ huyết
                if (pl.itemTime.isUseBoHuyet2) {
                    Service.getInstance().sendThongBao(pl, "Hốc vừa thôi bội thực chết cụ mày giờ");
                    return;
                }
                pl.itemTime.lastTimeBoHuyet = System.currentTimeMillis();
                pl.itemTime.isUseBoHuyet = true;
                break;
            case 383: // bổ khí
                if (pl.itemTime.isUseBoKhi2) {
                    Service.getInstance().sendThongBao(pl, "Hốc vừa thôi bội thực chết cụ mày giờ");
                    return;
                }
                pl.itemTime.lastTimeBoKhi = System.currentTimeMillis();
                pl.itemTime.isUseBoKhi = true;
                break;
            case 384: // giáp xên
                if (pl.itemTime.isUseGiapXen2) {
                    Service.getInstance().sendThongBao(pl, "Hốc vừa thôi bội thực chết cụ mày giờ");
                    return;
                }
                pl.itemTime.lastTimeGiapXen = System.currentTimeMillis();
                pl.itemTime.isUseGiapXen = true;
                break;
            case 381: // cuồng nộ
                if (pl.itemTime.isUseCuongNo2) {
                    Service.getInstance().sendThongBao(pl, "Hốc vừa thôi bội thực chết cụ mày giờ");
                    return;
                }
                pl.itemTime.lastTimeCuongNo = System.currentTimeMillis();
                pl.itemTime.isUseCuongNo = true;
                Service.gI().point(pl);
                break;
            case 385: // ẩn danh
                if (pl.itemTime.isUseAnDanh2) {
                    Service.getInstance().sendThongBao(pl, "Hốc vừa thôi bội thực chết cụ mày giờ");
                    return;
                }
                pl.itemTime.lastTimeAnDanh = System.currentTimeMillis();
                pl.itemTime.isUseAnDanh = true;
                break;
            case 379: // máy dò capsule
                if (pl.itemTime.isUseMayDo2) {
                    Service.getInstance().sendThongBao(pl, "Hốc vừa thôi bội thực chết cụ mày giờ");
                    return;
                }
                pl.itemTime.lastTimeUseMayDo = System.currentTimeMillis();
                pl.itemTime.isUseMayDo = true;
                break;
            case 1099:// cn
                if (pl.itemTime.isUseCuongNo) {
                    Service.getInstance().sendThongBao(pl, "Hốc vừa thôi bội thực chết cụ mày giờ");
                    return;
                }
                pl.itemTime.lastTimeCuongNo2 = System.currentTimeMillis();
                pl.itemTime.isUseCuongNo2 = true;
                Service.gI().point(pl);

                break;
            case 1100:// bo huyet
                if (pl.itemTime.isUseBoHuyet) {
                    Service.getInstance().sendThongBao(pl, "Hốc vừa thôi bội thực chết cụ mày giờ");
                    return;
                }
                pl.itemTime.lastTimeBoHuyet2 = System.currentTimeMillis();
                pl.itemTime.isUseBoHuyet2 = true;
                break;
            case 1101:// bo khi
                if (pl.itemTime.isUseBoKhi) {
                    Service.getInstance().sendThongBao(pl, "Hốc vừa thôi bội thực chết cụ mày giờ");
                    return;
                }
                pl.itemTime.lastTimeBoKhi2 = System.currentTimeMillis();
                pl.itemTime.isUseBoKhi2 = true;
                break;
            case 1102:// xbh
                if (pl.itemTime.isUseGiapXen) {
                    Service.getInstance().sendThongBao(pl, "Hốc vừa thôi bội thực chết cụ mày giờ");
                    return;
                }
                pl.itemTime.lastTimeGiapXen2 = System.currentTimeMillis();
                pl.itemTime.isUseGiapXen2 = true;
                break;
            case 1103:// an danh
                if (pl.itemTime.isUseAnDanh) {
                    Service.getInstance().sendThongBao(pl, "Hốc vừa thôi bội thực chết cụ mày giờ");
                    return;
                }
                pl.itemTime.lastTimeAnDanh2 = System.currentTimeMillis();
                pl.itemTime.isUseAnDanh2 = true;
                break;
            case 638:// Bình chứa comeson
                pl.itemTime.lastbkt = System.currentTimeMillis();
                pl.itemTime.isbkt = true;
                break;
            case 1763:// Bình chứa comeson
                if (pl.itemTime.isUseAnDanh) {
                    Service.getInstance().sendThongBao(pl, "Hốc vừa thôi bội thực chết cụ mày giờ");
                    return;
                }
                pl.itemTime.lastTimemat = System.currentTimeMillis();
                pl.itemTime.isUsemat = true;
                break;
            case 1764:// Bình chứa comeson
                pl.itemTime.lastTimesup = System.currentTimeMillis();
                pl.itemTime.isUsesup = true;
                break;
            case 1766:// Bình chứa comeson
                pl.itemTime.lastTimegato = System.currentTimeMillis();
                pl.itemTime.isUsegato = true;
            case 1767:// Bình chứa comeson
                pl.itemTime.lastTimeber = System.currentTimeMillis();
                pl.itemTime.isUseber = true;
            case 1768:// Bình chứa comeson
                pl.itemTime.lastTimekeo = System.currentTimeMillis();
                pl.itemTime.isUsekeo = true;
            case 663: // bánh pudding
            case 664: // xúc xíc
            case 665: // kem dâu
            case 666: // mì ly
            case 667: // sushi
                pl.itemTime.lastTimeEatMeal = System.currentTimeMillis();
                pl.itemTime.isEatMeal = true;
                ItemTimeService.gI().removeItemTime(pl, pl.itemTime.iconMeal);
                pl.itemTime.iconMeal = item.template.iconID;
                break;
            case 2037: // máy dò đồ
                pl.itemTime.lastTimeUseMayDo2 = System.currentTimeMillis();
                pl.itemTime.isUseMayDo2 = true;
                break;
            case 1560:
                pl.itemTime.lastTimedkhi = System.currentTimeMillis();
                pl.itemTime.isdkhi = true;
                Service.gI().Send_Caitrang(pl);
                Service.gI().point(pl);
                ItemTimeService.gI().removeItemTime(pl, pl.itemTime.icondkhi);
                pl.itemTime.iconMeal = item.template.iconID;
                break;
        }
        Service.gI().point(pl);
        ItemTimeService.gI().sendAllItemTime(pl);
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().sendItemBags(pl);
    }

    private void controllerCallRongThan(Player pl, Item item) {
        int tempId = item.template.id;
        if (tempId >= SummonDragon.NGOC_RONG_1_SAO && tempId <= SummonDragon.NGOC_RONG_7_SAO) {
            switch (tempId) {
                case SummonDragon.NGOC_RONG_1_SAO:
                case SummonDragon.NGOC_RONG_2_SAO:
                case SummonDragon.NGOC_RONG_3_SAO:
                    SummonDragon.gI().openMenuSummonShenron(pl, (byte) (tempId - 13));
                    break;
                default:
                    NpcService.gI().createMenuConMeo(pl, ConstNpc.TUTORIAL_SUMMON_DRAGON,
                            -1, "Bạn chỉ có thể gọi rồng từ ngọc 3 sao, 2 sao, 1 sao", "Hướng\ndẫn thêm\n(mới)", "OK");
                    break;
            }
        }
    }

    private void learnSkill(Player pl, Item item) {
        Message msg;
        try {
            if (item.template.gender == pl.gender || item.template.gender == 3) {
                String[] subName = item.template.name.split("");
                byte level = Byte.parseByte(subName[subName.length - 1]);
                Skill curSkill = SkillUtil.getSkillByItemID(pl, item.template.id);
                if (curSkill.point == 7) {
                    Service.gI().sendThongBao(pl, "Kỹ năng đã đạt tối đa!");
                } else {
                    if (curSkill.point == 0) {
                        if (level == 1) {
                            curSkill = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id),
                                    level);
                            SkillUtil.setSkill(pl, curSkill);
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            msg = Service.gI().messageSubCommand((byte) 23);
                            msg.writer().writeShort(curSkill.skillId);
                            pl.sendMessage(msg);
                            msg.cleanup();
                        } else {
                            Skill skillNeed = SkillUtil
                                    .createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
                            Service.gI().sendThongBao(pl,
                                    "Vui lòng học " + skillNeed.template.name + " cấp " + skillNeed.point + " trước!");
                        }
                    } else {
                        if (curSkill.point + 1 == level) {
                            curSkill = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id),
                                    level);
                            // System.out.println(curSkill.template.name + " - " + curSkill.point);
                            SkillUtil.setSkill(pl, curSkill);
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            msg = Service.gI().messageSubCommand((byte) 62);
                            msg.writer().writeShort(curSkill.skillId);
                            pl.sendMessage(msg);
                            msg.cleanup();
                        } else {
                            Service.gI().sendThongBao(pl, "Vui lòng học " + curSkill.template.name + " cấp "
                                    + (curSkill.point + 1) + " trước!");
                        }
                    }
                    InventoryServiceNew.gI().sendItemBags(pl);
                }
            } else {
                Service.gI().sendThongBao(pl, "Không thể thực hiện");
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void useTDLT(Player pl, Item item) {
        if (pl.itemTime.isUseTDLT) {
            ItemTimeService.gI().turnOffTDLT(pl, item);
        } else {
            ItemTimeService.gI().turnOnTDLT(pl, item);
        }
    }

    private void openThoiVangMAX(Player pl, Item item) {
        NpcService.gI().createMenuConMeo(pl, ConstNpc.TVMAX, -1,
                "1 thỏi vàng trị giá 1 tỷ vàng\n\nHiện tại có:" + item.quantity
                        + " Thỏi vàng\n\nNguoi muốn sài bao nhiêu thỏi?",
                "1 thỏi", "5 thỏi", "10 thỏi", "25 thỏi", "50 thỏi", "100 thỏi");
        return;
    }

    private void usePorata(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == 4) {
            Service.gI().sendThongBao(pl, "Không thể thực hiện");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusion(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void usePorata2(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == 4 || pl.fusion.typeFusion == 6 || pl.fusion.typeFusion == 10
                || pl.fusion.typeFusion == 12) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusion2(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void usePorata3(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == 4 || pl.fusion.typeFusion == 6 || pl.fusion.typeFusion == 8
                || pl.fusion.typeFusion == 12) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusion3(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void usePorata4(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == 4 || pl.fusion.typeFusion == 6 || pl.fusion.typeFusion == 8
                || pl.fusion.typeFusion == 10) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusion4(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void openCapsuleUI(Player pl) {
        pl.iDMark.setTypeChangeMap(ConstMap.CHANGE_CAPSULE);
        ChangeMapService.gI().openChangeMapTab(pl);
    }

    public void choseMapCapsule(Player pl, int index) {
        int zoneId = -1;
        if (index >= 0 && index <= pl.mapCapsule.size()) {
            if (index >= pl.mapCapsule.size() - 1) {
                Service.gI().sendThongBao(pl, "Có lỗi xãy ra!");
                return;
            }
            Zone zoneChose = pl.mapCapsule.get(index);
            // Kiểm tra số lượng người trong khu

            if (zoneChose.getNumOfPlayers() > 25
                    || MapService.gI().isMapDoanhTrai(zoneChose.map.mapId)
                    || MapService.gI().isMapBanDoKhoBau(zoneChose.map.mapId)
                    || MapService.gI().isMapMaBu(zoneChose.map.mapId)
                    || MapService.gI().isMapHuyDiet(zoneChose.map.mapId)) {
                Service.gI().sendThongBao(pl, "Hiện tại không thể vào được khu!");
                return;
            }
            if (index != 0 || zoneChose.map.mapId == 21
                    || zoneChose.map.mapId == 22
                    || zoneChose.map.mapId == 23) {
                pl.mapBeforeCapsule = pl.zone;
            } else {
                zoneId = pl.mapBeforeCapsule != null ? pl.mapBeforeCapsule.zoneId : -1;
                pl.mapBeforeCapsule = null;
            }
            ChangeMapService.gI().changeMapBySpaceShip(pl, pl.mapCapsule.get(index).map.mapId, zoneId, -1);
        }
    }

    public void eatPea(Player player) {
        Item pea = null;
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && item.template.type == 6) {
                pea = item;
                break;
            }
        }
        if (pea != null) {
            int hpKiHoiPhuc = 0;
            int lvPea = Integer.parseInt(pea.template.name.substring(13));
            for (Item.ItemOption io : pea.itemOptions) {
                if (io.optionTemplate.id == 2) {
                    hpKiHoiPhuc = io.param * 10000;
                    break;
                }
                if (io.optionTemplate.id == 48) {
                    hpKiHoiPhuc = io.param;
                    break;
                }
            }
            player.nPoint.setHp(player.nPoint.hp + hpKiHoiPhuc);
            player.nPoint.setMp(player.nPoint.mp + hpKiHoiPhuc);
            PlayerService.gI().sendInfoHpMp(player);
            Service.gI().sendInfoPlayerEatPea(player);
            if (player.pet != null && player.zone.equals(player.pet.zone) && !player.pet.isDie()) {
                int statima = 100 * lvPea;
                player.pet.nPoint.stamina += statima;
                if (player.pet.nPoint.stamina > player.pet.nPoint.maxStamina) {
                    player.pet.nPoint.stamina = player.pet.nPoint.maxStamina;
                }
                player.pet.nPoint.setHp((player.pet.nPoint.hp + hpKiHoiPhuc));
                player.pet.nPoint.setMp((player.pet.nPoint.mp + hpKiHoiPhuc));
                Service.gI().sendInfoPlayerEatPea(player.pet);
                Service.gI().chatJustForMe(player, player.pet, "Cảm ơn sư phụ đã cho con đậu thần");
            }

            InventoryServiceNew.gI().subQuantityItemsBag(player, pea, 1);
            InventoryServiceNew.gI().sendItemBags(player);
        }
    }

    private void upSkillPet(Player pl, Item item) {
        if (pl.pet == null) {
            Service.gI().sendThongBao(pl, "Không thể thực hiện");
            return;
        }
        try {
            switch (item.template.id) {
                case 402: // skill 1
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 0)) {
                        Service.gI().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.gI().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 403: // skill 2
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 1)) {
                        Service.gI().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.gI().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 404: // skill 3
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 2)) {
                        Service.gI().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.gI().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 759: // skill 4
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 3)) {
                        Service.gI().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.gI().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;

            }

        } catch (Exception e) {
            Service.gI().sendThongBao(pl, "Không thể thực hiện");
            e.printStackTrace();
        }
    }

    private void ItemSKH(Player pl, Item item) {// hop qua skh
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Hãy chọn một món quà", "Áo", "Quần", "Găng", "Giày",
                "Rada", "Từ Chối");
    }

    private void ItemDHD(Player pl, Item item) {// hop qua do huy diet
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Hãy chọn một món quà", "Áo", "Quần", "Găng", "Giày",
                "Rada", "Từ Chối");
    }

    private void Hopts(Player pl, Item item) {// hop qua do thien su
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Chọn hành tinh của mày đi", "Set trái đất",
                "Set namec", "Set xayda", "Từ chổi");
    }

    private void Hoptst(Player pl, Item item) {// hop qua do thien su thuong
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Chọn hành tinh của mày đi", "Set trái đất",
                "Set namec", "Set xayda", "Từ chổi");
    }

    private void Hophdt(Player pl, Item item) {// hop qua do huy diet top
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Chọn hành tinh của mày đi", "Set trái đất",
                "Set namec", "Set xayda", "Từ chổi");
    }

    private void Hophd(Player pl, Item item) {// hop qua do huy diet top
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Chọn hành tinh của mày đi", "Set trái đất",
                "Set namec", "Set xayda", "Từ chổi");
    }

    private void Hoptl(Player pl, Item item) {// hop qua do tl top
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Chọn hành tinh của mày đi", "Set trái đất",
                "Set namec", "Set xayda", "Từ chổi");
    }

    private void GetRubyFormWoodChest(Player pl, Item item) {
        int level = 0;
        for (ItemOption op : item.itemOptions) {
            if (op.optionTemplate.id == 72) {
                level = op.param;
                break;
            }
        }
        int HongNgoc = 0;
        switch (level) {
            case 1:
            case 2:
            case 3:
            case 4:
                HongNgoc = Util.nextInt(500, 1000);
                break;
            case 5:
            case 6:
                HongNgoc = Util.nextInt(1, 15);
                break;
            case 7:
                HongNgoc = Util.nextInt(1, 15);
                break;
            case 8:
                HongNgoc = Util.nextInt(1, 15);
                break;
            case 9:
                HongNgoc = Util.nextInt(1, 15);
                break;
            case 10:
                HongNgoc = Util.nextInt(1, 15);
                break;
            case 11:
                HongNgoc = Util.nextInt(1, 15);
                break;
        }
        pl.inventory.ruby += HongNgoc;
        Service.gI().sendMoney(pl);
        Service.getInstance().sendThongBao(pl, "Bạn nhận được " + HongNgoc + " hồng ngọc");
    }

    private void openWoodChest(Player pl, Item item) {
        int time = (int) TimeUtil.diffDate(new Date(), new Date(item.createTime), TimeUtil.DAY);
        if (time != 0) {
            GetRubyFormWoodChest(pl, item);
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Vui lòng đợi 24h");
        }
    }

    private void RuongItemCap2(Player pl, Item item) {
        NpcService.gI().createMenuConMeo(pl, 1278, -1, "Bạn muốn chọn gì ?", "Cuồng nộ", "Bổ khí", "Bổ huyết",
                "Giáp Xên bọ hung", "Ẩn danh");
    }

    public void SendItemCap2(Player pl, int type, int SoLuong) {
        short tempId = 0;
        switch (type) {
            case 4:
                tempId = 1099;
                break;
            case 5:
                tempId = 1101;
                break;
            case 6:
                tempId = 1100;
                break;
            case 7:
                tempId = 1102;
                break;
            case 8:
                tempId = 1103;
                break;
        }
        Item item = InventoryServiceNew.gI().findItem(pl.inventory.itemsBag, 1278);
        if (item.quantity >= SoLuong) {
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, SoLuong);
            Item itemsend = ItemService.gI().createNewItem(tempId, SoLuong);
            InventoryServiceNew.gI().addItemBag(pl, itemsend);
            InventoryServiceNew.gI().sendItemBags(pl);
            Service.gI().sendThongBao(pl, "Bạn nhận được x" + SoLuong + " " + itemsend.template.name);
        } else {
            Service.gI().sendThongBao(pl, "Số lượng rương không đủ");
        }
    }
}

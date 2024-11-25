package com.girlkun.Bot;

import com.KhanhDTK.services.EffectSkillService;
import com.KhanhDTK.services.Service;
import com.KhanhDTK.consts.ConstPlayer;
import com.KhanhDTK.models.skill.NClass;
import com.KhanhDTK.models.skill.Skill;
import com.KhanhDTK.models.Template.SkillTemplate;
import com.KhanhDTK.services.SkillService;
import com.KhanhDTK.server.*;
import com.KhanhDTK.models.map.Map;
import com.KhanhDTK.models.map.Zone;
import com.KhanhDTK.services.func.ChangeMapService;
import com.KhanhDTK.services.PlayerService;
import com.KhanhDTK.services.MapService;
import com.KhanhDTK.models.item.Item;
import com.KhanhDTK.utils.Util;
import com.KhanhDTK.models.player.*;
import java.util.Random;

public class Bot extends Player {
   private short head_;
   private short body_;
   private short leg_;
   private short flag_;
   private int type;
   private int index_ = 0;
   public ShopBot shop;
   public Sanb boss;
   public Mobb mo1;
   
   private Player plAttack;
   
   
   private int[] TraiDat = new int[]{1,2,3,4,5,6,29,30,28,27,42};
   private int[] Namec = new int[]{7,8,9,10,11,12,13,33,34,32,31};
   private int[] XayDa = new int[]{14,15,16,17,18,19,20,37,36,35,44,52};
   
   
   public Bot(short head , short body , short leg , int type , String name , ShopBot shop , short flag){
        this.head_ = head;
        this.body_ = body;
        this.leg_ = leg;
        this.shop = shop;
        this.name = name;
        this.id = new Random().nextInt(2000000000);
        this.type = type;
        this.isBot = true;
        this.flag_ = flag;
   }
   
   public int MapToPow(){
        Random random = new Random();
        long power = this.nPoint.power;
        int mapId = 21;
        if(power < 20000000){
           if (this.gender == 0) {
               mapId = TraiDat[random.nextInt(TraiDat.length)];
           } else if (this.gender == 1) {
               mapId = Namec[random.nextInt(Namec.length)];
           } else {
               mapId = XayDa[random.nextInt(XayDa.length)];
           }
        } else if(power < 100000000){
            mapId = 62 + random.nextInt(15);
        } else if(power < 1000000000){
            if(Util.isTrue(30 , 100)){
                mapId = 91 + random.nextInt(3);
            } else if(Util.isTrue(30 , 100)){
                mapId = 95 + random.nextInt(5);
            } else {
                mapId = 102 + random.nextInt(2);
            }
        } else {
           if(Util.isTrue(30 , 100)){
                mapId = 104 + random.nextInt(6);
           } else  if(Util.isTrue(30 , 100)){
                mapId = 173 + random.nextInt(3);
           } else {
                mapId = 157 + random.nextInt(2);
           }
        }
      return mapId;
   }
   
   public void joinMap() {
            Zone zone = getRandomZone(MapToPow());
            if (zone != null){
            ChangeMapService.gI().goToMap(this, zone);
            this.zone.load_Me_To_Another(this);
            this.mo1.lastTimeChanM = System.currentTimeMillis();
         }
   }
   
   public Zone getRandomZone(int mapId) {
        Map map = MapService.gI().getMapById(mapId);
        Zone zone = null;
        try {
            if (map != null) {
                zone = map.zones.stream()
                .filter(z -> z.getNumOfPlayers() == 0)
                .findFirst()
                .orElseGet(() -> {
                    Zone randomZone = map.zones.get(Util.nextInt(0, map.zones.size() - 1));
                    return randomZone.isFullPlayer() ? null : randomZone;
                });
            }
        } catch (Exception e) {
        }
        if(zone != null){
            this.index_ = 0;
            return zone;
        } else {
            this.index_ += 1;
            if(this.index_ >= 20){
            BotManager.gI().bot.remove(this);
            ChangeMapService.gI().exitMap(this);
            return null;
            } else {
            return getRandomZone(MapToPow());
            }
        }
    }

   
       @Override
    public short getHead() {
        if (effectSkill.isMonkey) {
            return (short) ConstPlayer.HEADMONKEY[effectSkill.levelMonkey - 1];
        } else {
            return this.head_;
        }
    }

    @Override
    public short getBody() {
         if (effectSkill.isMonkey) {
            return 193;
       } else {
            return this.body_;
        }
    }

    @Override
    public short getLeg() {
       if (effectSkill.isMonkey) {
            return 194;
        } else {
            return this.leg_;
        }
    }
    
    @Override
    public short getFlagBag() {
        return this.flag_;
    }
    
    @Override
    public void update() {
       super.update();
       this.increasePoint();
       switch (this.type){
           case 0:
               this.mo1.update();
           break;
           case 1:
               this.shop.update();
           break;
           case 2:
               this.boss.update();
           break;
       }
       if(this.isDie()){
         Service.gI().hsChar(this, nPoint.hpMax, nPoint.mpMax);
       }
    }
    
    
    public void leakSkill(){
       for(NClass n : Manager.gI().NCLASS){
            if(n.classId == this.gender){
               for(SkillTemplate Template : n.skillTemplatess){
                  for(Skill skills : Template.skillss){
                     Skill cloneSkill = new Skill(skills);
                     this.playerSkill.skills.add(cloneSkill);
                     break;
               }
            }
            break;
         }
      }
   }
   
   public boolean UseLastTimeSkill(){
       if(this.playerSkill.skillSelect.lastTimeUseThisSkillbot < (System.currentTimeMillis() - this.playerSkill.skillSelect.coolDown)){
           this.playerSkill.skillSelect.lastTimeUseThisSkillbot = System.currentTimeMillis();
           return true;
       } else {
           return false;
       }
    }
    
    private void increasePoint() {
       long tiemNangUse = 0;
       int point = 0;
        if (this.nPoint != null) {
            if (Util.isTrue(50, 100)) {
                point = 100;
                int pointHp = point * 20;
                tiemNangUse = point * (2 * (this.nPoint.hpg + 1000) + pointHp - 20) / 2;
                if (doUseTiemNang(tiemNangUse)){
                    this.nPoint.hpMax += point;
                    this.nPoint.hpg += point;
                    Service.gI().point(this);
                }
            } else {
                point = 10;
                tiemNangUse = point * (2 * this.nPoint.dameg + point - 1) / 2 * 100;
                if (doUseTiemNang(tiemNangUse)){
                    this.nPoint.dameg += point;
                    Service.gI().point(this);
                }
            }
        }
    }

   
   private boolean doUseTiemNang(long tiemNang) {
        if (this.nPoint.tiemNang < tiemNang) {
            return false;
        } else {
            this.nPoint.tiemNang -= tiemNang;
            return true;
        }
    }
    
    public void useSkill(int skillId){
        new Thread(() -> {
             switch(skillId){
                case Skill.BIEN_KHI:
                  EffectSkillService.gI().sendEffectMonkey(this);
                  EffectSkillService.gI().setIsMonkey(this);
                  EffectSkillService.gI().sendEffectMonkey(this);

                  Service.getInstance().sendSpeedPlayer(this, 0);
                  Service.getInstance().Send_Caitrang(this);
                  Service.getInstance().sendSpeedPlayer(this, -1);
                    PlayerService.gI().sendInfoHpMp(this);
                  Service.getInstance().point(this);
                  Service.getInstance().Send_Info_NV(this);
                  Service.getInstance().sendInfoPlayerEatPea(this);
                break;
                case Skill.QUA_CAU_KENH_KHI:
                  this.playerSkill.prepareQCKK = !this.playerSkill.prepareQCKK;
                  this.playerSkill.lastTimePrepareQCKK = System.currentTimeMillis();
                 SkillService.gI().sendPlayerPrepareSkill(this, 1000);
              break;
              case Skill.MAKANKOSAPPO:
                this.playerSkill.prepareLaze = !this.playerSkill.prepareLaze;
                this.playerSkill.lastTimePrepareLaze = System.currentTimeMillis();
                SkillService.gI().sendPlayerPrepareSkill(this, 3000);
                break;
             }
        }).start();
    }

    Zone getRandomZone(int i, int i0, int i1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
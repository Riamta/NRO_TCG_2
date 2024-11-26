package com.KhanhDTK.models.boss.list_boss.ChristmasEvent;

import com.KhanhDTK.models.boss.BossManager;

public class ChristmasEventManager extends BossManager {
    public ChristmasEventManager() {
        super();
    }

    private static ChristmasEventManager instance;

    public static ChristmasEventManager gI() {
        if (instance == null) {
            instance = new ChristmasEventManager();
        }
        return instance;
    }

}

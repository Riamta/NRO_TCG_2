package com.KhanhDTK.models.Event.event_list;

import com.KhanhDTK.models.boss.BossID;
import com.KhanhDTK.utils.Logger;
import com.KhanhDTK.models.Event.Event;

public class Christmas extends Event {
    @Override
    public void boss() {
        createBoss(BossID.ONG_GIA_NOEL, 30);
    }
}

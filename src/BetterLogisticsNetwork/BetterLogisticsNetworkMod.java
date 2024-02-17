package BetterLogisticsNetwork;

import arc.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.mod.*;
import mindustry.ui.dialogs.*;

public class BetterLogisticsNetworkMod extends Mod{

    public BetterLogisticsNetworkMod(){
        
    }

    @Override
    public void loadContent(){
        BLUnitTypes.load();
        BLThings.load();
    }

}

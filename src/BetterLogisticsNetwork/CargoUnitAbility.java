package BetterLogisticsNetwork;

import BetterLogisticsNetwork.*;
import arc.util.*;
import mindustry.entities.abilities.*;
import mindustry.gen.*;
import mindustry.type.*;
import BetterLogisticsNetwork.UnitCargoStation.*;

public class CargoUnitAbility extends Ability {
    public Building station;
    public @Nullable CargoTask task;

    @Override
    public void update(Unit unit){
        if(station == null || station.dead){
            unit.kill();
        }
    }

}

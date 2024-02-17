package BetterLogisticsNetwork;

import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;

public class UnitCargoBlock extends Block {
    public UnitCargoBlock(String name){
        super(name);
        solid = true;
        update = true;
        destructible = true;
        separateItemCapacity = true;
        allowResupply = true;

    }
    @Override
    public boolean outputsItems(){
        return false;
    }

    public class UnitCargoBlockBuild extends Building{
        public UnitCargoStation.UnitCargoStationBuild station;

        @Override
        public void handleItem(Building source, Item item){
            super.handleItem(source,item);
            if(station != null)station.getNet().hasChanged = true;
        }

    }
}

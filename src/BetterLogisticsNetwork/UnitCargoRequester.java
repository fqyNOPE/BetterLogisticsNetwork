package BetterLogisticsNetwork;

import arc.Core;
import arc.graphics.g2d.*;
import arc.scene.ui.layout.*;
import mindustry.gen.Building;
import mindustry.gen.Teamc;
import mindustry.type.*;
import mindustry.world.blocks.*;

import static mindustry.Vars.*;
public class UnitCargoRequester extends UnitCargoBlock {

    //public TextureRegion topRegion = Core.atlas.find("unit-cargo-unload-point-top");
    public UnitCargoRequester(String name){
        super(name);
        hasItems = true;
        configurable = true;
        itemCapacity = 100;
        saveConfig = true;
        config(Item.class, (UnitCargoRequesterBuild build, Item item) -> build.item = item);
        configClear((UnitCargoRequesterBuild build) -> build.item = null);
    }

    public class UnitCargoRequesterBuild extends UnitCargoBlockBuild{
        public Item item;
        @Override
        public void draw(){
            super.draw();

            if(item != null){
                Draw.color(item.color);
                Draw.rect(item.fullIcon,x,y);
                Draw.color();
            }
        }

        @Override
        public int acceptStack(Item item, int amount, Teamc source){
            return Math.min(itemCapacity - items.total(), amount);
        }
        @Override
        public void buildConfiguration(Table table){
            ItemSelection.buildTable(UnitCargoRequester.this, table, content.items(), () -> item, this::configure);
        }

        @Override
        public Object config(){
            return item;
        }
    }
}


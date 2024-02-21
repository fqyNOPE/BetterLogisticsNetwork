package BetterLogisticsNetwork;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.Seq;
import mindustry.entities.Effect;
import mindustry.entities.effect.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.distribution.*;
import mindustry.world.blocks.liquid.*;
import mindustry.world.blocks.power.*;
import mindustry.world.blocks.production.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.blocks.units.*;
import mindustry.world.consumers.Consume;
import mindustry.world.consumers.ConsumeItems;
import mindustry.world.draw.*;
import mindustry.world.meta.*;
import mindustry.content.*;

import arc.struct.*;
import mindustry.ctype.*;
import mindustry.game.Objectives.*;

import static mindustry.content.Blocks.*;
import static mindustry.content.SectorPresets.craters;
import static mindustry.content.SectorPresets.*;
import static mindustry.content.TechTree.*;
import static mindustry.content.UnitTypes.*;
import static arc.graphics.g2d.Draw.rect;
import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.*;
import static mindustry.Vars.*;
import static mindustry.type.ItemStack.*;

public class BLTechTree {
    public static TechNode content = null;
    public static void load(){
        addToNode(Blocks.massDriver,()->node(BLThings.cargoStation,() ->{
            node(BLThings.cargoRequeter);
            node(BLThings.cargoStorager);
        }));
    }

    public static void addToNode(UnlockableContent n,Runnable r){
        TechNode node = TechTree.all.find(t-> t.content == n);
        if(node !=null){
            content = node;
            r.run();
            content = null;
        }
        
    }

}
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

import static arc.graphics.g2d.Draw.rect;
import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.*;
import static mindustry.Vars.*;
import static mindustry.type.ItemStack.*;

public class BLThings {
    public static Block cargoStation,cargoStorager,cargoRequeter;
    public static void load(){
        cargoStation = new UnitCargoStation("cargo-station"){{
            size = 3;
            requirements(Category.distribution, with(Items.silicon, 275, Items.phaseFabric, 25, Items.plastanium, 75, Items.surgeAlloy, 55));
        }};
        cargoRequeter = new UnitCargoRequester("cargo-requester"){{
            size = 2;
            requirements(Category.distribution,  with(Items.silicon, 75,Items.plastanium, 50, Items.surgeAlloy, 5));
        }};
        cargoStorager = new UnitCargoStorager("cargo-storager"){{
            size = 2;
            requirements(Category.distribution,  with(Items.silicon, 45,Items.plastanium, 25, Items.surgeAlloy, 5));
        }};
    }

}
package BetterLogisticsNetwork;

import arc.graphics.*;
import arc.math.Interp;
import arc.math.geom.Rect;
import mindustry.ai.types.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.entities.effect.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.type.unit.*;
import mindustry.type.weapons.*;
import arc.graphics.g2d.*;

import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;
import static mindustry.Vars.*;

public class BLUnitTypes {
    public static UnitType transfer;

    public static void load() {

        transfer = new UnitType("transfer"){{
            armor = 8f;
            health = 600;
            speed = 3.5f;
            rotateSpeed = 3f;
            accel = 0.05f;
            drag = 0.017f;
            itemCapacity = 40;
            lowAltitude = false;
            constructor = UnitEntity::create;
            flying = true;
            hitSize = 12f;
            controller = u -> new CargoUnitController();
            isEnemy = false;
            allowedInPayloads = false;
            logicControllable = false;
            playerControllable = false;
        }};
    }

}

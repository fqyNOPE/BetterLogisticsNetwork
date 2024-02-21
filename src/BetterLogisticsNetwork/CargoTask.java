package BetterLogisticsNetwork;

import BetterLogisticsNetwork.*;
import arc.func.Cons;
import arc.math.Mat;
import arc.struct.*;
import mindustry.content.Items;
import mindustry.content.UnitTypes;
import mindustry.gen.*;
import mindustry.type.*;

public class CargoTask {

    public UnitType unitType = BLUnitTypes.transfer;
    public Building source;
    public Building target;
    public Item itemType = Items.copper;
    public int amount = 0;
    public int maxUnit = 0;
    public int remainAmount = 0;
    public int finishAmount = 0;
    public Seq<Unit> workingUnits = new Seq<>();

    public CargoTask(Building source,Building target,Item itemType,int amount){
        this.source = source;
        this.target = target;
        this.itemType = itemType;
        this.amount = amount;
        this.remainAmount = amount;
        maxUnit = amount / unitType.itemCapacity + ((amount % unitType.itemCapacity > 0)? 1 : 0) + 1;
    }
    public void handleTask(Unit unit, int amount) {
        this.remainAmount -= amount;
        this.finishAmount += amount;
    }

}

package BetterLogisticsNetwork;

import arc.util.Log;
import mindustry.entities.Sized;
import mindustry.entities.units.*;
import mindustry.gen.*;

import static mindustry.ai.types.CargoAI.*;

public class CargoUnitController extends AIController {

    @Override
    public void updateMovement() {
        if (unit.abilities.length == 0 || !(unit.abilities[0] instanceof CargoUnitAbility cargoAbility)) {
            return;
        }
        var task = cargoAbility.task;
        if(task != null){
            if(!unit.hasItem()){
                if(task.source == null){
                    for(var u : cargoAbility.task.workingUnits){
                        if(u!=null){
                            if (u.abilities.length == 0 || !(u.abilities[0] instanceof CargoUnitAbility ability)) {
                                continue;
                            }
                            ability.task = null;
                            u.clearItem();
                        }
                    }
                }
                moveTo(task.source, moveRange, moveSmoothing);
                if(task.source.items.any() && unit.within(task.source, transferRange)){
                    if(task.source.items.get(task.itemType) <= task.remainAmount){
                        task.remainAmount = task.source.items.get(task.itemType);
                    }
                    Call.takeItems(task.source, task.itemType, Math.min(unit.type.itemCapacity, task.source.items.get(task.itemType)), unit);
                }
            }
            else{
                if(task.target == null){
                    for(var u : cargoAbility.task.workingUnits){
                        if(u!=null){
                            if (u.abilities.length == 0 || !(u.abilities[0] instanceof CargoUnitAbility ability)) {
                                continue;
                            }
                            ability.task = null;
                            u.clearItem();
                        }
                        unit.clearItem();
                    }
                }
                moveTo(task.target,moveRange, moveSmoothing);
                if(unit.within(task.target, transferRange) && timer.get(timerTarget2, dropSpacing)){
                    int max = task.target.acceptStack(unit.item(), unit.stack.amount, unit);

                    //deposit items when it's possible
                    if(max > 0){
                        Call.transferItemTo(unit, unit.item(), max, unit.x, unit.y, task.target);
                        cargoAbility.task.handleTask(unit,max);
                        if(cargoAbility.task.remainAmount <= 0){
                            for(var u : cargoAbility.task.workingUnits){
                                if(u!=null){
                                    if (u.abilities.length == 0 || !(u.abilities[0] instanceof CargoUnitAbility ability)) {
                                        continue;
                                    }
                                    ability.task = null;
                                    u.clearItem();
                                }
                            }
                            cargoAbility.task = null;
                            unit.clearItem();
                        }
                    }
                }
            }
        }
    }

}

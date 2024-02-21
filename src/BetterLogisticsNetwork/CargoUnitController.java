package BetterLogisticsNetwork;

import arc.util.Log;
import mindustry.entities.Sized;
import mindustry.entities.units.*;
import mindustry.gen.*;
import BetterLogisticsNetwork.UnitCargoRequester.*;

import static mindustry.ai.types.CargoAI.*;

public class CargoUnitController extends AIController {


    public void removeTask(CargoTask task){
        if (unit.abilities.length == 0 || !(unit.abilities[0] instanceof CargoUnitAbility cargoAbility)) {
            unit.kill();
            return;
        }
        ((UnitCargoStation.UnitCargoStationBuild)cargoAbility.station).getNet().tasks.remove(task);
        for(var u : cargoAbility.task.workingUnits){
            if(u!=null){
                if (u.abilities.length == 0 || !(u.abilities[0] instanceof CargoUnitAbility ability)) {
                    continue;
                }
                ability.task = null;
            }
        }
    }

    @Override
    public void updateMovement() {
        if (unit.abilities.length == 0 || !(unit.abilities[0] instanceof CargoUnitAbility cargoAbility)) {
            unit.kill();
            return;
        }
        var task = cargoAbility.task;
        if(task != null){
            if(!unit.hasItem()){
                if(task.target == null || task.target.dead ||task.source == null || task.source.dead|| ((UnitCargoRequester.UnitCargoRequesterBuild)task.target).item != task.itemType){
                    removeTask(task);
                    task = null;
                }
                else{
                    moveTo(task.source, moveRange, moveSmoothing);
                        if(task.source.items.any() && unit.within(task.source, transferRange)){
                            Call.takeItems(task.source, task.itemType, Math.min(Math.min(unit.type.itemCapacity, task.source.items.get(task.itemType)), task.remainAmount), unit);
                            if(task.source.items.get(task.itemType) <= 20){
                                removeTask(task);
                            }
                        }

                }
            }
            else{
                if(task.target == null || task.target.dead ||task.source == null || task.source.dead|| ((UnitCargoRequester.UnitCargoRequesterBuild)task.target).item != task.itemType){
                    removeTask(task);
                    task = null;
                }
                else{
                    moveTo(task.target,moveRange, moveSmoothing);
                    if(unit.within(task.target, transferRange) && timer.get(timerTarget2, dropSpacing)){
                        int max = task.target.acceptStack(unit.item(), unit.stack.amount, unit);

                        //deposit items when it's possible
                        if(max > 0){
                            Call.transferItemTo(unit, unit.item(), max, unit.x, unit.y, task.target);
                            cargoAbility.task.handleTask(unit,max);
                            if(cargoAbility.task.remainAmount <= 0){
                                removeTask(task);
                            }
                        }
                    }
                }
            }
        }
        else{
            if(unit.hasItem())unit.clearItem();
        }
    }

}


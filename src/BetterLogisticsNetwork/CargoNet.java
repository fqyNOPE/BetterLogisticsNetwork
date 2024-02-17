package BetterLogisticsNetwork;

import BetterLogisticsNetwork.CargoUnitAbility;
import arc.struct.*;
import arc.util.Log;
import mindustry.gen.*;
import BetterLogisticsNetwork.UnitCargoStation.*;
import BetterLogisticsNetwork.UnitCargoBlock.*;
import BetterLogisticsNetwork.UnitCargoStorager.*;
import BetterLogisticsNetwork.UnitCargoRequester.*;
import mindustry.type.*;
import mindustry.world.Build;
import mindustry.world.modules.ItemModule;

import static mindustry.Vars.*;

public class CargoNet {

    public Seq<CargoTask> tasks = new Seq<>();
    public Seq<Unit> cargoUnits = new Seq<>();
    public Seq<Building> stations = new Seq<>();
    public Seq<Building> all = new Seq<>();
    public Seq<Building> providers = new Seq<>();
    public Seq<Building> requesters = new Seq<>();
    public Seq<Building> storagers = new Seq<>();
    public boolean hasChanged = true;

    protected int[] items = new int[content.items().size];

    public CargoNet() {
    }

    public void add(Building building) {
        hasChanged = true;
        if (!(building instanceof UnitCargoBlockBuild)) return;
        if (building instanceof UnitCargoStationBuild) {
            stations.addUnique(building);
            return;
        }
        if (all.contains(building)) return;
        all.add(building);
        if (building instanceof UnitCargoRequesterBuild) requesters.addUnique(building);
        if (building instanceof UnitCargoStoragerBuild) storagers.addUnique(building);
    }

    public void merge(CargoNet net) {
        for (var station : net.stations) {
            if (!stations.contains(station)) stations.addUnique(station);
        }
        for (var building : net.all) {
            add(building);
        }
        net.clear();
        hasChanged = true;
    }

    public void clear() {
        stations.clear();
        cargoUnits.clear();
        providers.clear();
        requesters.clear();
        storagers.clear();
        all.clear();
        hasChanged = true;
    }

    public void update() {
        for (Building building : requesters) {
            if ((building instanceof UnitCargoRequesterBuild build)) {
                if (build.item == null) continue;
                int amount = build.block.itemCapacity - build.items.get(build.item);
                if (amount >= 20) {
                    addTask(building, amount);
                }
            }
        }
        for(var task : tasks){
            if(task.remainAmount <= 0){
                tasks.remove(task);
            }
        }
        for(var unit : cargoUnits){
            if(unit !=null){
                if (unit.abilities.length == 0 || !(unit.abilities[0] instanceof CargoUnitAbility cargoAbility)) {
                    continue;
                }
                if(cargoAbility.task == null){
                    for(var task : tasks){
                        if(task.workingUnits.size < task.maxUnit){
                            task.workingUnits.addUnique(unit);
                            cargoAbility.task = task;
                        }
                    }
                }
            }

        }
    }

    public void addTask(Building b, int amount) {
        if (!(b instanceof UnitCargoRequesterBuild build)) return;
        Item item = build.item;
        storagers.sort(a -> a.items.get(item) * -1);
        for (Building building : storagers) {
            if (building.items.get(item) > 0) {
                CargoTask task = new CargoTask(building, b, item, Math.min(building.items.get(item), amount));
                if (canAddTask(b)) {
                    tasks.addUnique(task);
                    Log.info("addTask: " + task.source + "->" + task.target + " Item: " + item.name + " Amount:" + Math.min(building.items.get(item),amount));
                }
                break;
            }
        }
    }

    //One requester can only handle one task
    public boolean canAddTask(Building target) {
        for (var task : tasks) {
            if (task.target == target) return false;
        }
        return true;
    }

}

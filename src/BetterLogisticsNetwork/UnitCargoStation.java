package BetterLogisticsNetwork;

import BetterLogisticsNetwork.*;
import arc.Events;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.Mathf;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.Vars;
import mindustry.content.*;
import mindustry.entities.Units;
import mindustry.entities.abilities.Ability;
import mindustry.game.EventType;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.*;
import arc.util.io.*;

import static mindustry.Vars.*;

public class UnitCargoStation extends UnitCargoBlock {

    public UnitType unitType = BLUnitTypes.transfer;
    public int maxUnit = 1;
    public float linkRange = 30f;
    public float range = 20f;
    public float buildTime = 300f;
    public Color baseColor = Color.valueOf("a3b1ff");
    public float polyStroke = 1.8f, polyRadius = 8f;
    public int polySides = 6;
    public float polyRotateSpeed = 1f;
    public Color polyColor = Pal.accent;

    public UnitCargoStation(String name) {
        super(name);
        configurable = true;
        update = true;
        solid = true;
        sync = true;
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid) {

        x *= tilesize;
        y *= tilesize;
        x += offset;
        y += offset;

        final float dx = x, dy = y;
        Drawf.dashSquare(baseColor, x, y, range * tilesize);
        indexer.eachBlock(player.team(), Tmp.r1.setCentered(x, y, linkRange * tilesize), b -> (b instanceof UnitCargoStationBuild), t -> {
            Drawf.dashLine(Pal.placing,
                    dx,
                    dy,
                    t.x,
                    t.y
            );
        });
    }

    public class UnitCargoStationBuild extends UnitCargoBlockBuild {
        public CargoNet cnet = new CargoNet();
        public boolean base = false, inited = false;
        public Seq<UnitCargoStationBuild> fromStation = new Seq<>();
        public UnitCargoStationBuild toStation;
        public int lastChange = -1;
        public float spawnProgress = 0f, totalProgress = 0f;
        public float warmup, readyness;
        public int[] readUnitIds = new int[32];
        public @Nullable Seq<Unit> units = new Seq<>();

        @Override
        public void updateTile() {
            super.updateTile();
            boolean netUpdate = true;
            if (!inited) {
                inited = true;
                indexer.eachBlock(this.team(), Tmp.r1.setCentered(x, y, linkRange * tilesize), b -> (b instanceof UnitCargoStationBuild) && b != this, b -> {
                    if ((b instanceof UnitCargoStationBuild build) && build.inited) {
                        if (toStation == null) {
                            toStation = build;
                            build.fromStation.addUnique(this);
                            netAdd(this);
                        } else {
                            if (getNet() != build.getNet()) {
                                getNet().merge(build.getNet());
                                UnitCargoStationBuild station = build;
                                Seq<UnitCargoStationBuild> list = new Seq<>();
                                while (!station.base && station.toStation != null) {
                                    list.addUnique(station);
                                    station = station.toStation;
                                }
                                list.addUnique(station);
                                station.base = false;
                                for (int i = list.size - 1; i > 0; i--) {
                                    list.get(i).toStation = list.get(i - 1);
                                    list.get(i).fromStation.remove(list.get(i - 1));
                                    if (i != list.size - 1) {
                                        list.get(i).fromStation.addUnique(list.get(i + 1));
                                    }
                                }
                                build.toStation = this;
                            }
                        }
                    }
                });
                if (toStation == null) {
                    base = true;
                    netUpdate = false;
                    netAdd(this);
                }
            }
            if (lastChange != world.tileChanges) {
                lastChange = world.tileChanges;
                indexer.eachBlock(this.team(), Tmp.r1.setCentered(x, y, range * tilesize), b -> (b instanceof UnitCargoBlockBuild) && !(b instanceof UnitCargoStationBuild), b -> {
                    netAdd(b);
                    ((UnitCargoBlockBuild) b).station = this;
                });
            }
            if (base && netUpdate) cnet.update();

            units.forEach(u -> {
                if (u != null && (u.dead && !u.isAdded())) units.remove(u);
            });

            for (int i = 0; i < readUnitIds.length; i++) {
                int readUnitId = readUnitIds[i];
                if (readUnitId != -1) {
                    Unit unit = Groups.unit.getByID(readUnitId);
                    if (!units.contains(unit)) {
                        units.addUnique(unit);
                        if (unit != null || !net.client()) {
                            readUnitIds[i] = -1;
                        }
                    }

                }
            }

            warmup = Mathf.approachDelta(warmup, efficiency, 1f / 60f);
            if (efficiency > 0 && units.size <= maxUnit && Units.canCreate(team, unitType)) {
                spawnProgress += edelta() / buildTime;
                totalProgress += edelta();
                if (spawnProgress >= 1f) {
                    Unit u = unitType.create(this.team);
                    u.set(x, y);
                    u.rotation = 90;
                    u.abilities = new Ability[]{new CargoUnitAbility()};
                    ((CargoUnitAbility) u.abilities[0]).station = this;
                    getNet().cargoUnits.addUnique(u);
                    Events.fire(new EventType.UnitCreateEvent(u, this, u));
                    if (!Vars.net.client()) {
                        u.add();
                    }
                    spawned(u.id);
                }
            }
        }

        public void spawned(int id) {
            Fx.spawn.at(x, y);
            spawnProgress = 0f;
            for (int i = 0; i < readUnitIds.length; i++) {
                int readUnitId = readUnitIds[i];
                if (readUnitId == -1) {
                    readUnitIds[i] = id;
                }
            }

        }


        @Override
        public void onProximityAdded() {
            super.onProximityAdded();
        }

        @Override
        public void drawSelect() {
            super.drawSelect();
            if (toStation != null) {
                Drawf.selected(toStation, Tmp.c1.set(baseColor).a(Mathf.absin(4f, 1f)));
                Drawf.dashLine(baseColor, x, y, toStation.x, toStation.y);
            }
            for (var station : fromStation) {
                var baseColor2 = Pal.accent;
                Drawf.selected(station, Tmp.c1.set(baseColor2).a(Mathf.absin(4f, 1f)));
                Drawf.dashLine(baseColor2, x, y, station.x, station.y);
            }
            Drawf.dashSquare(baseColor, x, y, range * tilesize);
        }

        @Override
        public void onProximityRemoved() {
            super.onProximityRemoved();
            indexer.eachBlock(this.team(), 0, 0, 10000f, b -> b instanceof UnitCargoStationBuild, b -> {
                UnitCargoStationBuild build = (UnitCargoStationBuild) b;
                build.base = false;
                build.inited = false;
                build.toStation = null;
                build.fromStation.clear();
                build.cnet.clear();
            });
            for (var unit : units) {
                if (unit != null) {
                    unit.kill();
                }
            }


        }

        public CargoNet getNet() {
            UnitCargoStationBuild b = this;
            while (!b.base && b.toStation != null) {
                b = b.toStation;
            }
            return b.cnet;
        }

        public void netAdd(Building building) {
            getNet().add(building);
        }

        @Override
        public void buildConfiguration(Table table) {
            table.button(Icon.upOpen, Styles.cleari, () -> {
                for (int i = 0; i < content.items().size; i++) {
                    if (getNet().items[i] > 0)
                        Log.info("item:" + content.items().get(i) + "amount:" + getNet().items[i]);
                }
                deselect();
            }).size(40f);
        }

        @Override
        public void draw() {
            super.draw();
            Draw.rect(block.region, x, y);
            if (units.size < maxUnit) {
                Draw.draw(Layer.blockOver, () -> {
                    Drawf.construct(this, unitType.fullIcon, 0f, spawnProgress, warmup, totalProgress);
                });
            } else {
                Draw.z(Layer.bullet - 0.01f);
                Draw.color(polyColor);
                Lines.stroke(polyStroke * readyness);
                Lines.poly(x, y, polySides, polyRadius, Time.time * polyRotateSpeed);
                Draw.reset();
                Draw.z(Layer.block);
            }
        }

        @Override
        public float totalProgress() {
            return totalProgress;
        }

        @Override
        public float progress() {
            return spawnProgress;
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.i(units.size);
            for (Unit unit : units) write.i(unit == null ? -1 : unit.id);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            var usize = read.i();
            for (int i = 0; i < usize; i++) {
                readUnitIds[i] = read.i();
            }
        }

    }


}

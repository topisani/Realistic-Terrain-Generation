package teamrtg.rtg.core.base.event;

import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.terraingen.WorldTypeEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import teamrtg.rtg.api.world.biome.RTGBiome;
import teamrtg.rtg.api.module.Mods;
import teamrtg.rtg.api.util.debug.Logger;
import teamrtg.rtg.core.base.RTGMod;
import teamrtg.rtg.core.base.world.WorldTypeRTG;
import teamrtg.rtg.core.base.world.BiomeProviderRTG;
import teamrtg.rtg.core.base.world.gen.MapGenCavesRTG;
import teamrtg.rtg.core.base.world.gen.MapGenRavineRTG;
import teamrtg.rtg.core.base.world.gen.genlayer.RiverRemover;
import teamrtg.rtg.core.base.world.gen.structure.MapGenScatteredFeatureRTG;
import teamrtg.rtg.core.base.world.gen.structure.MapGenVillageRTG;
import teamrtg.rtg.core.base.world.gen.structure.StructureOceanMonumentRTG;

public class EventManagerRTG {

    public RTGBiome biome = null;

    public EventManagerRTG() {
        MapGenStructureIO.registerStructure(MapGenScatteredFeatureRTG.Start.class, "rtg_MapGenScatteredFeatureRTG");
        if (Mods.RTG.config.ENABLE_VILLAGE_MODIFICATIONS.get())
            MapGenStructureIO.registerStructure(MapGenVillageRTG.Start.class, "rtg_MapGenVillageRTG");
        MapGenStructureIO.registerStructure(StructureOceanMonumentRTG.StartMonument.class, "rtg_MapGenOceanMonumentRTG");
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void eventListenerRTG(InitMapGenEvent event) {

        Logger.debug("event type = %s", event.getType().toString());
        Logger.debug("event originalGen = %s", event.getOriginalGen().toString());

        if (event.getType() == InitMapGenEvent.EventType.SCATTERED_FEATURE) {
            event.setNewGen(new MapGenScatteredFeatureRTG());
        } else if (event.getType() == InitMapGenEvent.EventType.VILLAGE) {

            if (Mods.RTG.config.ENABLE_VILLAGE_MODIFICATIONS.get()) {
                event.setNewGen(new MapGenVillageRTG());
            }
        } else if (event.getType() == InitMapGenEvent.EventType.CAVE) {

            if (Mods.RTG.config.ENABLE_CAVE_MODIFICATIONS.get()) {

                event.setNewGen(new MapGenCavesRTG());
            }
        } else if (event.getType() == InitMapGenEvent.EventType.RAVINE) {

            if (Mods.RTG.config.ENABLE_RAVINE_MODIFICATIONS.get()) {

                event.setNewGen(new MapGenRavineRTG());
            }
        } else if (event.getType() == InitMapGenEvent.EventType.OCEAN_MONUMENT) {
            event.setNewGen(new StructureOceanMonumentRTG());
        }
        Logger.debug("event newGen = %s", event.getNewGen().toString());
    }

    @SubscribeEvent
    public void onBiomeGenInit(WorldTypeEvent.InitBiomeGens event) {

        // only handle RTGMod world type
        if (!event.getWorldType().getWorldTypeName().equalsIgnoreCase("RTGMod")) return;

        boolean stripRivers = true; // This used to be a config option. Hardcoding until we have a need for the option.

        if (stripRivers) {
            try {
                event.setNewBiomeGens(new RiverRemover().riverLess(event.getOriginalBiomeGens()));
            } catch (ClassCastException ex) {
                //throw ex;
                // failed attempt because the GenLayers don't end with GenLayerRiverMix
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if ((event.getWorld().getWorldInfo().getTerrainType() instanceof WorldTypeRTG)) {
            if (event.getWorld().provider.getDimension() == 0) {
                Logger.info("World Seed: %d", event.getWorld().getSeed());
                Logger.info("Reloading configs");
                Mods.syncAllConfigs();
            }
        } else {
            MinecraftForge.TERRAIN_GEN_BUS.unregister(RTGMod.eventMgr);
            MinecraftForge.ORE_GEN_BUS.unregister(RTGMod.eventMgr);
            MinecraftForge.EVENT_BUS.unregister(RTGMod.eventMgr);
        }

    }

    @SubscribeEvent
    public void preBiomeDecorate(DecorateBiomeEvent.Pre event) {

        //Are we in an RTGMod world? Do we have RTGMod's chunk manager?
        if (event.getWorld().getWorldInfo().getTerrainType() instanceof WorldTypeRTG && event.getWorld().getBiomeProvider() instanceof BiomeProviderRTG) {

            BiomeProviderRTG cmr = (BiomeProviderRTG) event.getWorld().getBiomeProvider();
            this.biome = cmr.getRealisticAt(event.getPos().getX(), event.getPos().getZ());
        }
    }
}
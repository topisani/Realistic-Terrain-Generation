package teamrtg.rtg.core.base;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import teamrtg.rtg.api.module.Mods;
import teamrtg.rtg.api.util.RealisticBiomePresenceTester;
import teamrtg.rtg.core.base.client.DebugHandler;
import teamrtg.rtg.core.base.event.EventManagerRTG;
import teamrtg.rtg.core.base.world.WorldTypeRTG;
import teamrtg.rtg.api.world.RealisticBiomeFaker;
import teamrtg.rtg.core.mcver.one.ten.zero.*;
import teamrtg.rtg.core.mcver.one.ten.zero.RTGVersion;

import java.util.ArrayList;

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, version = ModInfo.MOD_VERSION, dependencies = "required-after:Forge@[" + ModInfo.FORGE_DEP + ",)" + ModInfo.MOD_DEPS, acceptableRemoteVersions = "*")
public class RTGMod {

    @Instance(ModInfo.MOD_ID)
    public static RTGMod instance;
    public static String configPath;
    public static WorldTypeRTG worldtype;
    public static EventManagerRTG eventMgr;

    @SidedProxy(serverSide = ModInfo.PROXY_COMMON, clientSide = ModInfo.PROXY_CLIENT)
    public static CommonProxy proxy;

    private ArrayList<Runnable> serverCloseActions = new ArrayList<>();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        instance = this;

        configPath = event.getModConfigurationDirectory() + "/RTGMod/";
        Mods.syncAllConfigs();

        teamrtg.rtg.core.mcver.one.ten.zero.RTGVersion ver10;
        ver10 = new teamrtg.rtg.core.mcver.one.ten.zero.RTGVersion();
        teamrtg.rtg.core.mcver.one.nine.four.RTGVersion ver9;
        ver9 = new teamrtg.rtg.core.mcver.one.nine.four.RTGVersion();
        ver9.test(ver10);
        eventMgr = new EventManagerRTG();
        MinecraftForge.EVENT_BUS.register(eventMgr);
        MinecraftForge.ORE_GEN_BUS.register(eventMgr);
        MinecraftForge.TERRAIN_GEN_BUS.register(eventMgr);

        worldtype = new WorldTypeRTG("RTGMod");
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        if (event.getSide() == Side.CLIENT) {
            MinecraftForge.EVENT_BUS.register(new DebugHandler());
        }
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        Mods.initAllBiomes();
        RealisticBiomeFaker.initFakeBiomes();
        RealisticBiomePresenceTester.doBiomeCheck();
    }
}

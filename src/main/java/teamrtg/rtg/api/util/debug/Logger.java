package teamrtg.rtg.api.util.debug;

import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;
import teamrtg.rtg.api.module.Mods;

public class Logger {

    public static void debug(String format, Object... data) {
        if (Mods.RTG.config.DEBUG_LOGGING.get()) FMLLog.log(Level.INFO, "[RTGMod-DEBUG] " + format, data);
    }

    public static void info(String format, Object... data) {
        FMLLog.log(Level.INFO, "[RTGMod-INFO] " + format, data);
    }

    public static void warn(String format, Object... data) {
        FMLLog.log(Level.WARN, "[RTGMod-WARN] " + format, data);
    }

    public static void error(String format, Object... data) {
        FMLLog.log(Level.ERROR, "[RTGMod-ERROR] " + format, data);
    }

    public static void fatal(Throwable throwable, String message, Object... data) {
        FMLLog.log(Level.FATAL, "[RTGMod-FATAL] " + message, data);
        Minecraft.getMinecraft().crashed(new CrashReport(message, throwable));
    }
}
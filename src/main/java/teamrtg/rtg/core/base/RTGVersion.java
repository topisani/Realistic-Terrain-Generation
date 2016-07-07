package teamrtg.rtg.core.base;

import teamrtg.rtg.api.util.debug.Logger;

/**
 * This is the starting point of the mod for each version of minecraft
 * To support a new version of the game, set up a new package in
 * teamrtg.core.one.nine.four for minecraft 1.9.4. Then extend this class,
 * keeping its name, and placing it in the root of that package.
 * Then make sure it links to all your implementations of the used classes.
 *
 * @author topisani
 */
public abstract class RTGVersion {

    public void test(RTGVersion other) {
        Logger.debug("aww shiet");
    }

}

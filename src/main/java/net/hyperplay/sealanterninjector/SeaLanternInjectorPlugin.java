package net.hyperplay.sealanterninjector;

import org.bukkit.plugin.java.JavaPlugin;

public class SeaLanternInjectorPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().fine("This plugin injects the 1.8 sea_lantern block (169) into 1.7.9/10 servers.");

        try {
            SeaLanternInjector.inject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        super.onEnable();
    }

}

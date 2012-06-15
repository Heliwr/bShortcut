package com.github.Heliwr.bShortcut;

import java.io.*;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

// Referenced classes of package com.beecub.bShortcut:
//            bShortcut

public class bConfigManagerImport {
    protected static bShortcut bShortcut;
    protected static YamlConfiguration conf;
    protected File confFile;
    static Set<String> shortcuts = new LinkedHashSet<String>();


    public bConfigManagerImport(bShortcut instance) throws FileNotFoundException, IOException, InvalidConfigurationException {
        bShortcut = instance;
        File f = new File(bShortcut.getDataFolder(), "imports.yml");
        conf = null;
        if(f.exists()) {
            conf = new YamlConfiguration();
            conf.load(f);
        } else {
            confFile = new File(bShortcut.getDataFolder(), "imports.yml");
            conf = new YamlConfiguration();
            conf.load(confFile);
            conf.save(confFile);
        }
    }

    static void load() {
        try {
            File f = new File(bShortcut.getDataFolder(), "config.yml");
            conf.load(f);
            shortcuts.clear();
            shortcuts = conf.getConfigurationSection("shortcuts.commands").getKeys(true);
        }
        catch(FileNotFoundException ex) {
            com.github.Heliwr.bShortcut.bShortcut.log.log(Level.SEVERE, "bShortcut: Config file was not found.", ex);
        }
        catch(IOException ex) {
            com.github.Heliwr.bShortcut.bShortcut.log.log(Level.SEVERE, "bShortcut: An error occured while reading the config.", ex);
        }
        catch(InvalidConfigurationException ex) {
            com.github.Heliwr.bShortcut.bShortcut.log.log(Level.SEVERE, "bShortcut: An error occured while parsing the config.", ex);
        }
    }

    static void reload() {
        load();
    }

    Set<String> getShortcuts() {
        load();
        return shortcuts;
    }
}

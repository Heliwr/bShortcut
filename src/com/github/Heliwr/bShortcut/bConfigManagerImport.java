// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   bConfigManagerImport.java

package com.github.Heliwr.bShortcut;

import java.io.*;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

// Referenced classes of package com.beecub.bShortcut:
//            bShortcut

public class bConfigManagerImport
{

    public bConfigManagerImport(bShortcut instance)
        throws FileNotFoundException, IOException, InvalidConfigurationException
    {
        bConfigManagerImport _tmp = this;
        bShortcut = instance;
        File f = new File(bShortcut.getDataFolder(), "imports.yml");
        conf = null;
        if(f.exists())
        {
            conf = new YamlConfiguration();
            conf.load(f);
        } else
        {
            confFile = new File(bShortcut.getDataFolder(), "imports.yml");
            conf = new YamlConfiguration();
            conf.load(confFile);
            conf.save(confFile);
        }
    }

    static void load()
    {
        try
        {
            File f = new File(bShortcut.getDataFolder(), "config.yml");
            conf.load(f);
            shortcuts.clear();
            shortcuts = conf.getConfigurationSection("shortcuts.commands").getKeys(true);
        }
        catch(FileNotFoundException ex)
        {
            bShortcut _tmp = bShortcut;
            bShortcut.log.log(Level.SEVERE, "bShortcut: Config file was not found.", ex);
        }
        catch(IOException ex)
        {
            bShortcut _tmp1 = bShortcut;
            bShortcut.log.log(Level.SEVERE, "bShortcut: An error occured while reading the config.", ex);
        }
        catch(InvalidConfigurationException ex)
        {
            bShortcut _tmp2 = bShortcut;
            bShortcut.log.log(Level.SEVERE, "bShortcut: An error occured while parsing the config.", ex);
        }
    }

    static void reload()
    {
        load();
    }

    Set getShortcuts()
    {
        load();
        return shortcuts;
    }

    protected static bShortcut bShortcut;
    protected static YamlConfiguration conf;
    protected File confFile;
    static Set shortcuts = new LinkedHashSet();

}

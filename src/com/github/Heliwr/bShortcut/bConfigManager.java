// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   bConfigManager.java

package com.github.Heliwr.bShortcut;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import util.bChat;

// Referenced classes of package com.beecub.bShortcut:
//            bConfigManagerImport, bShortcut

public class bConfigManager
{

    public bConfigManager(bShortcut instance)
        throws FileNotFoundException, IOException, InvalidConfigurationException
    {
        bConfigManager _tmp = this;
        plugin = instance;
        File imports = new File(plugin.getDataFolder(), "imports.yml");
        if(!imports.exists())
        {
            imports.createNewFile();
            FileWriter writer = new FileWriter(imports);
            writer.write("{}");
            writer.close();
        }
        File f = new File(plugin.getDataFolder(), "config.yml");
        conf = new YamlConfiguration();
        if(!f.exists())
        {
            confFile = new File(plugin.getDataFolder(), "config.yml");
            List bsp1 = new LinkedList();
            List bsp2 = new LinkedList();
            conf.set("shortcuts.commands./cleanstone", "/item 1 64");
            bsp1.add("My name is &player");
            bsp1.add("Iam &1 years old");
            conf.set("shortcuts.commands./myName", bsp1);
            conf.set("shortcuts.commands./breload", "/bShortcut reload");
            conf.set("shortcuts.commands./t", "/time &args");
            bsp2.add("/give &1 &2 &3");
            bsp2.add("/m &1 there you got &3 of &2 from me.");
            conf.set("shortcuts.commands./g", bsp2);
            conf.save(confFile);
        }
        bConfigManagerImport = new bConfigManagerImport(plugin);
    }

    static void load()
        throws FileNotFoundException, IOException, InvalidConfigurationException
    {
        File f = new File(bShortcut.dataDir, "config.yml");
        conf.load(f);
        shortcuts.clear();
        shortcuts = conf.getConfigurationSection("shortcuts.commands").getKeys(true);
    }

    static void reload()
    {
        try
        {
            load();
        }
        catch(FileNotFoundException ex)
        {
            bShortcut _tmp = plugin;
            bShortcut.log.log(Level.SEVERE, "bShortcut: Config file was not found.", ex);
        }
        catch(IOException ex)
        {
            bShortcut _tmp1 = plugin;
            bShortcut.log.log(Level.SEVERE, "bShortcut: An error occured while reading the config.", ex);
        }
        catch(InvalidConfigurationException ex)
        {
            bShortcut _tmp2 = plugin;
            bShortcut.log.log(Level.SEVERE, "bShortcut: An error occured while parsing the config.", ex);
        }
    }

    static boolean handleShortcuts(Player player, String pre, String message)
    {
        List perform = new LinkedList();
        if(shortcuts.contains(pre))
        {
            perform = conf.getStringList((new StringBuilder()).append("shortcuts.commands.").append(pre).toString());
            if(performCommand(player, perform, pre, message))
                return true;
        }
        return false;
    }

    static boolean performCommand(Player player, List perform, String pre, String message)
    {
        if(perform != null && perform.size() > 1)
        {
            for(int i = 0; i < perform.size(); i++)
            {
                boolean bperform = true;
                String performMessage = (String)perform.get(i);
                if(i == 0)
                {
                    bShortcut _tmp = plugin;
                    if(performMessage.contains("&permissions"))
                    {
                        bperform = false;
                        performMessage = performMessage.replaceAll("&permissions", "");
                        bShortcut _tmp1 = plugin;
                        if(!player.hasPermission("bShortcut.user"))
                        {
                            bChat.sendMessageToPlayer(player, "&6You dont have permissions to that command");
                            return true;
                        }
                    }
                }
                if(!bperform)
                    continue;
                performMessage = handleVariables(player, performMessage, message);
                if(performMessage.startsWith("&system"))
                {
                    performMessage = performMessage.replaceAll("&system", "");
                    bChat.broadcastMessage(performMessage);
                    return true;
                }
                if(performMessage.contains("&onlineplayers"))
                {
                    Player players[] = plugin.getServer().getOnlinePlayers();
                    for(int j = 0; j < players.length; j++)
                        player.chat(performMessage.replaceAll("&onlineplayers", players[j].getName()));

                    return true;
                }
                player.chat(performMessage);
            }

            return true;
        }
        if(perform != null && perform.size() == 0)
        {
            String performMessage = conf.getString((new StringBuilder()).append("shortcuts.commands.").append(pre).toString(), null);
            performMessage = handleVariables(player, performMessage, message);
            if(performMessage.startsWith("&system"))
            {
                performMessage = performMessage.replaceAll("&system", "");
                bChat.broadcastMessage(performMessage);
            } else
            if(performMessage.contains("&onlineplayers"))
            {
                Player players[] = plugin.getServer().getOnlinePlayers();
                for(int j = 0; j < players.length; j++)
                    player.chat(performMessage.replaceAll("&onlineplayers", players[j].getName()));

            } else
            {
                player.chat(performMessage);
            }
            return true;
        } else
        {
            return false;
        }
    }

    static String handleVariables(Player player, String performMessage, String message)
    {
        String args[] = (String[])null;
        message = message.replaceFirst(" ", "");
        args = message.split(" ");
        for(int k = 0; k < args.length; k++)
            performMessage = performMessage.replaceAll((new StringBuilder()).append("&").append(k + 1).toString(), args[k]);

        performMessage = performMessage.replaceAll("&world", player.getLocation().getWorld().getName());
        performMessage = performMessage.replaceAll("&player", player.getName());
        performMessage = performMessage.replaceAll("&args", message);
        return performMessage;
    }

    protected static bShortcut plugin;
    protected static bConfigManagerImport bConfigManagerImport;
    protected static YamlConfiguration conf;
    protected File confFile;
    static Set shortcuts = new LinkedHashSet();
    static List shortcutsImport = new LinkedList();

}

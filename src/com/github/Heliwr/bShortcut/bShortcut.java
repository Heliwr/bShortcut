// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   bShortcut.java

package com.github.Heliwr.bShortcut;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.JavaPlugin;
import util.bChat;

// Referenced classes of package com.beecub.bShortcut:
//            bShortcutPlayerListener, bConfigManager

public class bShortcut extends JavaPlugin {

	private final bShortcutPlayerListener playerListener = new bShortcutPlayerListener(this);
    public static Logger log;
    public static PluginDescriptionFile pdfFile;
    public static Configuration conf;
    static bChat bChat;
    public static boolean permissions = false;
    public static File dataDir;
    public static Server server;


    public bShortcut() {
    }

    public void onEnable() {
        server = getServer();
        dataDir = getDataFolder();
        log = getServer().getLogger();
        pdfFile = getDescription();
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(playerListener, this);
        pdfFile = getDescription();
        log.info((new StringBuilder()).append("[").append(pdfFile.getName()).append("]").append(" version ").append(pdfFile.getVersion()).append(" is enabled!").toString());
        bChat = new bChat(getServer());
        bConfigManager bCM;
        try
        {
            bCM = new bConfigManager(this);
            bConfigManager.load();
        }
        catch(FileNotFoundException ex)
        {
            log.log(Level.SEVERE, "bShortcut: No config file found!", ex);
            return;
        }
        catch(IOException ex)
        {
            log.log(Level.SEVERE, "bShortcut: Error while reading config!", ex);
            return;
        }
        catch(InvalidConfigurationException ex)
        {
            log.log(Level.SEVERE, "bShortcut: Error while parsing config!", ex);
            return;
        }
        conf = bConfigManager.conf;
    }

    public void onDisable()
    {
        log.info((new StringBuilder()).append("[").append(pdfFile.getName()).append("]").append(" version ").append(pdfFile.getVersion()).append(" disabled!").toString());
    }

    public boolean onCommand(CommandSender sender, Command c, String commandLabel, String args[])
    {
        String command = c.getName().toLowerCase();
        Player player = null;
        if(command.equalsIgnoreCase("bShortcut")) {
            if (sender instanceof Player) {
        		player = (Player) sender;
                if(!player.hasPermission("bShortcut.admin")) {
                	bChat.sendMessageToPlayer(player, "&6You dont have permissions to that command");
                	return true;
                }
            }        	
            bConfigManager.reload();
            util.bChat.sendMessageToCommandSender(sender, (new StringBuilder()).append("&6[").append(pdfFile.getName()).append("]").append(" config reloaded").toString());
            return true;
        }
        return false;
    }
}

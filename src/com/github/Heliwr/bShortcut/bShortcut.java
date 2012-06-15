package com.github.Heliwr.bShortcut;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.JavaPlugin;

// Referenced classes of package com.beecub.bShortcut:
//            bShortcutPlayerListener, bConfigManager

public class bShortcut extends JavaPlugin {

	private final bShortcutPlayerListener playerListener = new bShortcutPlayerListener(this);
    public static Logger log;
    public static PluginDescriptionFile pdfFile;
    public static Configuration conf;
    public static boolean permissions = false;
    public static File dataDir;
    public static Server server;

    public void onEnable() {
        server = getServer();
        dataDir = getDataFolder();
        log = getServer().getLogger();
        pdfFile = getDescription();
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(playerListener, this);
        pdfFile = getDescription();
        log.info((new StringBuilder()).append("[").append(pdfFile.getName()).append("]").append(" version ").append(pdfFile.getVersion()).append(" is enabled!").toString());
        try
        {
            new bConfigManager(this);
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
        if(command.equalsIgnoreCase("bShortcut") && args.length > 0) {
        	if(args[0].equalsIgnoreCase("reload")) {
            	if (sender instanceof Player) {
            		player = (Player) sender;
                    if(!player.hasPermission("bShortcut.admin")) {
                    	player.sendMessage(ChatColor.GOLD + "You dont have permissions to that command");
                    	return true;
                    }
                }        	
                bConfigManager.reload();
        		if (player != null) {
                    player.sendMessage(ChatColor.GOLD + pdfFile.getName() + " config reloaded");
        		} else {
            		log.info("[" + pdfFile.getName() + "] config reloaded");
        		}
                return true;
        		
        	}
        }        
        return false;
    }
}

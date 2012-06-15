package com.github.Heliwr.bShortcut;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

// Referenced classes of package com.beecub.bShortcut:
//            bConfigManagerImport, bShortcut

public class bConfigManager {
    protected static bShortcut plugin;
    protected static bConfigManagerImport bConfigManagerImport;
    protected static YamlConfiguration conf;
    protected File confFile;
    static Set<String> shortcuts = new LinkedHashSet<String>();
    static List<String> shortcutsImport = new LinkedList<String>();

    public bConfigManager(bShortcut instance) throws FileNotFoundException, IOException, InvalidConfigurationException {
        plugin = instance;
        File imports = new File(plugin.getDataFolder(), "imports.yml");
        if(!imports.exists()) {
            imports.createNewFile();
            FileWriter writer = new FileWriter(imports);
            writer.write("{}");
            writer.close();
        }
        File f = new File(plugin.getDataFolder(), "config.yml");
        conf = new YamlConfiguration();
        if(!f.exists()) {
            confFile = new File(plugin.getDataFolder(), "config.yml");
            List<String> bsp1 = new LinkedList<String>();
            List<String> bsp2 = new LinkedList<String>();
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

    static void load() throws FileNotFoundException, IOException, InvalidConfigurationException {
        File f = new File(bShortcut.dataDir, "config.yml");
        conf.load(f);
        shortcuts.clear();
        shortcuts = conf.getConfigurationSection("shortcuts.commands").getKeys(true);
    }

    static void reload() {
        try {
            load();
        }
        catch(FileNotFoundException ex) {
            bShortcut.log.log(Level.SEVERE, "bShortcut: Config file was not found.", ex);
        }
        catch(IOException ex) {
            bShortcut.log.log(Level.SEVERE, "bShortcut: An error occured while reading the config.", ex);
        }
        catch(InvalidConfigurationException ex) {
            bShortcut.log.log(Level.SEVERE, "bShortcut: An error occured while parsing the config.", ex);
        }
    }

    static boolean handleShortcuts(Player player, String pre, String message) {
        List<String> perform = new LinkedList<String>();
        if(shortcuts.contains(pre)) {
            perform = conf.getStringList((new StringBuilder()).append("shortcuts.commands.").append(pre).toString());
            if(performCommand(player, perform, pre, message))
                return true;
        }
        return false;
    }

    static boolean performCommand(Player player, List<String> perform, String pre, String message) {
        if(perform != null && perform.size() > 1) {
            for(int i = 0; i < perform.size(); i++) {
                boolean bperform = true;
                String performMessage = (String)perform.get(i);
                if(i == 0) {
                    if(performMessage.contains("&permissions")) {
                        bperform = false;
                        performMessage = performMessage.replaceAll("&permissions", "");
                        if(!player.hasPermission("bShortcut.user")) {
                        	player.sendMessage(ChatColor.GOLD + "You dont have permissions to that command");
                            return true;
                        }
                    }
                }
                if(!bperform)
                    continue;
                performMessage = handleVariables(player, performMessage, message);
                if(performMessage.startsWith("&system")) {
                    performMessage = performMessage.replaceAll("&system", "");
                    Bukkit.getServer().broadcastMessage(performMessage);
                    return true;
                }
                if(performMessage.contains("&onlineplayers")) {
                    Player players[] = plugin.getServer().getOnlinePlayers();
                    for(int j = 0; j < players.length; j++)
                        player.chat(performMessage.replaceAll("&onlineplayers", players[j].getName()));

                    return true;
                }
                player.chat(performMessage);
            }
            return true;
        }
        if(perform != null && perform.size() == 0) {
            String performMessage = conf.getString((new StringBuilder()).append("shortcuts.commands.").append(pre).toString(), null);
            performMessage = handleVariables(player, performMessage, message);
            if(performMessage.startsWith("&system")) {
                performMessage = performMessage.replaceAll("&system", "");
                Bukkit.getServer().broadcastMessage(performMessage);
            } else if(performMessage.contains("&onlineplayers")) {
                Player players[] = plugin.getServer().getOnlinePlayers();
                for(int j = 0; j < players.length; j++)
                    player.chat(performMessage.replaceAll("&onlineplayers", players[j].getName()));

            } else {
                player.chat(performMessage);
            }
            return true;
        } else {
            return false;
        }
    }

    static String handleVariables(Player player, String performMessage, String message) {
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
}

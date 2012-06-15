package com.github.Heliwr.bShortcut;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

// Referenced classes of package com.beecub.bShortcut:
//            bConfigManager, bShortcut

public class bShortcutPlayerListener implements Listener {

    public bShortcutPlayerListener(bShortcut instance) {
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if(event.isCancelled())
            return;
        String message = event.getMessage();
        Player player = event.getPlayer();
        int i = message.indexOf(' ');
        if(i < 0)
            i = message.length();
        String pre = (String)message.subSequence(0, i);
        message = (String)message.subSequence(i, message.length());
        if(bConfigManager.handleShortcuts(player, pre, message))
            event.setCancelled(true);
    }
}

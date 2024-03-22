package cn.yescallop.essentialsnk;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.*;

import java.util.Iterator;

public class EventListener implements Listener {

    private final EssentialsAPI api;

    public EventListener(EssentialsAPI api) {
        this.api = api;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (Server.sbpeTweaks || Server.pvpServer) {
            api.setLastLocation(event.getPlayer(), event.getFrom());
        }
    }

    /*@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        Block bed = event.getBed();
        api.setHome(event.getPlayer(), "bed", Location.fromObject(bed, bed.level, 0, 0));
    }*/

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        boolean vanished = api.isVanished(player);
        for (Player p : api.getServer().getOnlinePlayers().values()) {
            if (api.isVanished(p)) {
                player.hidePlayer(p);
            }
            if (vanished) {
                p.hidePlayer(player);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        api.removeTPRequest(event.getPlayer());
        if (Server.sbpeTweaks || Server.pvpServer) {
            api.setLastLocation(event.getPlayer(), null);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        Iterator<CommandSender> iter = event.getRecipients().iterator();

        while (iter.hasNext()) {
            CommandSender sender = iter.next();
            if (!(sender instanceof Player)) {
                continue;
            }

            if (api.isIgnoring(((Player) sender).getUniqueId(), player.getUniqueId())) {
                iter.remove();
            }
        }

        if (api.isMuted(player)) {
            event.setCancelled();
            player.sendMessage(Language.translate("commands.generic.muted", api.getUnmuteTimeMessage(player)));
        }
    }

    /*@EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (api.isMuted(player)) {
            event.setCancelled();
            player.sendMessage(Language.translate("commands.generic.muted", api.getUnmuteTimeMessage(player)));
        }
    }*/
}
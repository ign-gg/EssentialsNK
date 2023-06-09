package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.EssentialsNK;
import cn.yescallop.essentialsnk.Language;
import cn.yescallop.essentialsnk.command.CommandBase;

public class ClearInventoryCommand extends CommandBase {

    public ClearInventoryCommand(EssentialsAPI api) {
        super("clearinventory", api);
        //this.setAliases(new String[]{"ci", "clean", "clearinvent"});

        // command parameters
        commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[] {
                new CommandParameter("target", CommandParamType.TARGET, true)
        });
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        if (args.length > 1) {
            this.sendUsage(sender);
            return false;
        }
        Player player;
        if (args.length == 0) {
            if (!this.testIngame(sender)) {
                return false;
            }
            player = (Player) sender;
        } else {
            if (!sender.hasPermission("essentialsnk.clearinventory.others")) {
                this.sendPermissionMessage(sender);
                return false;
            }
            player = api.getServer().getPlayerExact(args[0]);
            if (player == null) {
                if (!sender.hasPermission("essentialsnk.clearinventory.others.offline")) {
                    sender.sendMessage(TextFormat.RED + Language.translate("commands.generic.player.notfound", args[0]));
                    return false;
                }

                String offlinePlayer = args[0].toLowerCase();
                CompoundTag offlineData = sender.getServer().getOfflinePlayerData(offlinePlayer, false);
                if (offlineData != null) {
                    if (offlineData.contains("Inventory") && offlineData.get("Inventory") instanceof ListTag) {
                        offlineData.remove("Inventory");
                        sender.getServer().saveOfflinePlayerData(offlinePlayer, offlineData);
                        sender.sendMessage("Offline player inventory cleared: " + offlinePlayer);
                        EssentialsNK.instance.getLogger().warning(sender.getName() + " cleared the inventory of offline player " + offlinePlayer);
                    } else {
                        sender.sendMessage(TextFormat.RED + "Offline player data has no 'Inventory' tag: " + offlinePlayer);
                    }
                    return true;
                }
                sender.sendMessage(TextFormat.RED + Language.translate("commands.generic.player.notfound", args[0]));
                return false;
            }
        }
        player.getInventory().clearAll();
        player.sendMessage(Language.translate("commands.clearinventory.success"));
        if (sender != player) {
            sender.sendMessage(Language.translate("commands.clearinventory.success.other", player.getDisplayName()));
        }
        EssentialsNK.instance.getLogger().warning(sender.getName() + " cleared the inventory of " + player.getName());
        return true;
    }
}

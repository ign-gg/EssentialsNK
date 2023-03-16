package cn.yescallop.essentialsnk.command.defaults.teleport;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.Language;
import cn.yescallop.essentialsnk.command.CommandBase;

public class TPACommand extends CommandBase {

    public TPACommand(EssentialsAPI api) {
        super("tpa", api);
        //this.setAliases(new String[]{"call", "tpask"});

        // command parameters
        commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[] {
                new CommandParameter("player", CommandParamType.TARGET, false)
        });
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        if (!this.testIngame(sender)) {
            return false;
        }
        if (args.length != 1) {
            this.sendUsage(sender);
            return false;
        }
        if (api.hasCooldown(sender)) {
            return true;
        }
        Player player = api.getServer().getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(TextFormat.RED + Language.translate("commands.generic.player.notfound", args[0]));
            return false;
        }
        if (sender == player) {
            sender.sendMessage(TextFormat.RED + Language.translate("commands.tpa.self"));
            return false;
        }
        if (!api.isIgnoring(player.getUniqueId(), ((Player) sender).getUniqueId())) {
            api.requestTP((Player) sender, player, true);
            player.sendMessage(Language.translate("commands.tpa.invite", sender.getName()));
            sender.sendMessage(Language.translate("commands.tpa.success", player.getDisplayName()));
        } else {
            sender.sendMessage(Language.translate("commands.tpdeny.denied", player.getDisplayName()));
        }
        return true;
    }
}

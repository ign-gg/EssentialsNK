package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.particle.HeartParticle;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.Language;
import cn.yescallop.essentialsnk.command.CommandBase;

public class HealCommand extends CommandBase {

    public HealCommand(EssentialsAPI api) {
        super("heal", api);

        // command parameters
        commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[] {
                new CommandParameter("player", CommandParamType.TARGET, true)
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
            if (!sender.hasPermission("essentialsnk.heal.others")) {
                this.sendPermissionMessage(sender);
                return false;
            }
            player = api.getServer().getPlayerExact(args[0]);
            if (player == null) {
                sender.sendMessage(TextFormat.RED + Language.translate("commands.generic.player.notfound", args[0]));
                return false;
            }
        }
        player.heal(player.getMaxHealth() - player.getHealth());
        player.getLevel().addParticle(new HeartParticle(player.add(0, 2), 4));
        player.sendMessage(Language.translate("commands.heal.success"));
        if (sender != player) {
            sender.sendMessage(Language.translate("commands.heal.success.other", player.getDisplayName()));
        }
        return true;
    }
}

package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.Language;
import cn.yescallop.essentialsnk.command.CommandBase;

public class CompassCommand extends CommandBase {

    public CompassCommand(EssentialsAPI api) {
        super("compass", api);
        //this.setAliases(new String[]{"direction"});

        // command parameters
        commandParameters.clear();
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        if (!this.testIngame(sender)) {
            return false;
        }
        if (args.length != 0) {
            this.sendUsage(sender);
            return false;
        }
        String direction;
        switch (((Player) sender).getDirection()) {
            case SOUTH:
                direction = "south";
                break;
            case WEST:
                direction = "west";
                break;
            case NORTH:
                direction = "north";
                break;
            case EAST:
                direction = "east";
                break;
            default:
                direction = "error";
        }
        sender.sendMessage(Language.translate("commands.compass.success", Language.translate("commands.compass." + direction)));
        return true;
    }
}

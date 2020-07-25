package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.Language;
import cn.yescallop.essentialsnk.command.CommandBase;

import java.util.Objects;

public class MessageCommand extends CommandBase {

    public MessageCommand(EssentialsAPI api) {
        super("message", api);
        this.setAliases(new String[]{"m", "msg", "w", "whisper", "tell", "privatemessage", "pm"});

        // command parameters
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                new CommandParameter("player", CommandParamType.TARGET, false),
                new CommandParameter("message", CommandParamType.TEXT, false)
        });
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!testPermission(sender)) {
            return false;
        }
        if (args.length < 2) {
            return false;
        }

        Player player = api.getServer().getPlayer(args[0]);

        if (player == null) {
            sender.sendMessage(TextFormat.RED + Language.translate("commands.generic.player.notfound", args[0]));
            return true;
        }

        if (Objects.equals(player, sender)) {
            sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.message.sameTarget"));
            return true;
        }

        if (sender instanceof Player) {
            if (api.isIgnoring(player.getUniqueId(), ((Player) sender).getUniqueId())) {
                sender.sendMessage(TextFormat.RED + "This player doesn't allow private messages from you");
                return true;
            }
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            builder.append(args[i]).append(' ');
        }

        if (builder.length() > 255) {
            sender.sendMessage(TextFormat.RED + "The message is too long");
            return true;
        } else if (builder.length() > 0) {
            builder = new StringBuilder(builder.substring(0, builder.length() - 1));
        }

        String m = "§5" + TextFormat.clean(builder.toString());
        String displayName = (sender instanceof Player ? ((Player) sender).getDisplayName() : sender.getName());

        sender.sendMessage('[' + sender.getName() + " §7-> §f" + player.getDisplayName() + "] " + m);
        player.sendMessage('[' + displayName + " §7-> §f" + player.getName() + "] " + m);

        api.getLastMessagedPlayers().put(sender.getName(), player.getName());
        api.getLastMessagedPlayers().put(player.getName(), sender.getName());
        return true;
    }
}

package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.Language;
import cn.yescallop.essentialsnk.command.CommandBase;

public class ReplyCommand extends CommandBase {

    public ReplyCommand(EssentialsAPI api) {
        super("reply", api);
        this.setAliases(new String[]{"r"});

        // command parameters
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                new CommandParameter("message", CommandParamType.STRING, false)
        });
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!testPermission(sender)) {
            return false;
        }
        if (args.length < 1) {
            return false;
        }

        if (!api.getLastMessagedPlayers().containsKey(sender.getName())) {
            sender.sendMessage(TextFormat.RED + Language.translate("commands.reply.notmessaged"));
            return false;
        }

        String playerName = api.getLastMessagedPlayers().get(sender.getName());
        Player player = api.getServer().getPlayerExact(playerName);

        if (player == null) {
            sender.sendMessage(TextFormat.RED + Language.translate("commands.generic.player.notfound", playerName));
            api.getLastMessagedPlayers().remove(sender.getName());
            return false;
        }

        if (sender instanceof Player) {
            Player sePlayer = (Player) sender;
            if (api.isMuted(sePlayer)) {
                sePlayer.sendMessage(Language.translate("commands.generic.muted", api.getUnmuteTimeMessage(sePlayer)));
            }
            if (api.isIgnoring(player.getUniqueId(), sePlayer.getUniqueId())) {
                sender.sendMessage(TextFormat.RED + "This player doesn't allow private messages from you");
                return true;
            }
        }

        StringBuilder builder = new StringBuilder();
        for (String arg : args) {
            builder.append(arg).append(' ');
        }

        if (builder.length() > (Server.sbpeTweaks ? 255 : 200)) {
            sender.sendMessage(TextFormat.RED + "The message is too long");
            return true;
        } else if (builder.length() > 0) {
            builder = new StringBuilder(builder.substring(0, builder.length() - 1));
        }

        String text = TextFormat.clean(builder.toString()).replaceAll("[\uE000-\uE0EA\n]", "?");
        String displayName = (sender instanceof Player ? ((Player) sender).getDisplayName() : sender.getName());

        sender.sendMessage("§f[§7" + sender.getName() + " -> " + player.getDisplayName() + "§f] §r" + text);
        player.sendMessage("§f[§7" + displayName + " -> " + player.getName() + "§f] §r" + text);

        api.getLastMessagedPlayers().put(sender.getName(), player.getName());
        api.getLastMessagedPlayers().put(player.getName(), sender.getName());
        return true;
    }
}

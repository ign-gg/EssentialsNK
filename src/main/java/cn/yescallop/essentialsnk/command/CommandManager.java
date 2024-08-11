package cn.yescallop.essentialsnk.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandMap;
import cn.nukkit.command.SimpleCommandMap;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.command.defaults.*;
import cn.yescallop.essentialsnk.command.defaults.teleport.*;
import cn.yescallop.essentialsnk.command.defaults.warp.DelWarpCommand;
import cn.yescallop.essentialsnk.command.defaults.warp.SetWarpCommand;
import cn.yescallop.essentialsnk.command.defaults.warp.WarpCommand;

import java.lang.reflect.Field;
import java.util.Map;

public class CommandManager {

    public static void registerAll(EssentialsAPI api) {
        CommandMap map = api.getServer().getCommandMap();

        // Unregister vanilla tell command
        try {
            Class<?> cl = map.getClass();
            Field f = cl.getDeclaredField("knownCommands");
            f.setAccessible(true);
            Map<String, Command> knownCommands = (Map<String, Command>) f.get(map);
            knownCommands.remove("nukkit:tell");
            knownCommands.remove("nukkit:msg");
            knownCommands.remove("nukkit:w");
            knownCommands.remove("tell");
            knownCommands.remove("msg");
            knownCommands.remove("w");
            f.set(map, knownCommands);
        } catch (Exception ex) {
            api.getLogger().error("Failed to unregister vanilla tell command", ex);
        }

        map.register("EssentialsNK", new BackCommand(api));
        map.register("EssentialsNK", new BreakCommand(api));
        map.register("EssentialsNK", new BroadcastCommand(api));
        map.register("EssentialsNK", new BurnCommand(api));
        map.register("EssentialsNK", new RenameCommand(api));
        map.register("EssentialsNK", new ClearInventoryCommand(api));
        map.register("EssentialsNK", new CompassCommand(api));
        map.register("EssentialsNK", new DepthCommand(api));
        map.register("EssentialsNK", new ExtinguishCommand(api));
        map.register("EssentialsNK", new FeedCommand(api));
        map.register("EssentialsNK", new FlyCommand(api));
        map.register("EssentialsNK", new GamemodeCommand(api));
        map.register("EssentialsNK", new GetPosCommand(api));
        map.register("EssentialsNK", new HealCommand(api));
        map.register("EssentialsNK", new ItemDBCommand(api));
        map.register("EssentialsNK", new JumpCommand(api));
        map.register("EssentialsNK", new KickAllCommand(api));
        map.register("EssentialsNK", new LightningCommand(api));
        map.register("EssentialsNK", new MessageCommand(api));
        map.register("EssentialsNK", new MoreCommand(api));
        map.register("EssentialsNK", new MuteCommand(api));
        map.register("EssentialsNK", new RealNameCommand(api));
        map.register("EssentialsNK", new RepairCommand(api));
        map.register("EssentialsNK", new ReplyCommand(api));
        map.register("EssentialsNK", new SpeedCommand(api));
        map.register("EssentialsNK", new SudoCommand(api));
        map.register("EssentialsNK", new TopCommand(api));
        map.register("EssentialsNK", new VanishCommand(api));
        map.register("EssentialsNK", new WorldCommand(api));
        map.register("EssentialsNK", new WhoisCommand(api));

        map.register("EssentialsNK", new TPACommand(api));
        map.register("EssentialsNK", new TPAAllCommand(api));
        map.register("EssentialsNK", new TPAcceptCommand(api));
        map.register("EssentialsNK", new TPAHereCommand(api));
        map.register("EssentialsNK", new TPAllCommand(api));
        map.register("EssentialsNK", new TPDenyCommand(api));
        map.register("EssentialsNK", new TPHereCommand(api));

        map.register("EssentialsNK", new DelWarpCommand(api));
        map.register("EssentialsNK", new WarpCommand(api));
        map.register("EssentialsNK", new SetWarpCommand(api));

        map.register("EssentialsNK", new SetSpawnCommand(api));
        map.register("EssentialsNK", new SpawnCommand(api));
        map.register("EssenailsNK", new IgnoreCommand(api));
    }
}
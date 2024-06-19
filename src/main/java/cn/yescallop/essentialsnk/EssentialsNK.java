package cn.yescallop.essentialsnk;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.scheduler.TaskHandler;
import cn.yescallop.essentialsnk.command.CommandManager;
import cn.yescallop.essentialsnk.task.TeleportationTask;

public class EssentialsNK extends PluginBase {

    private EssentialsAPI api;
    private TaskHandler task;
    public static EssentialsNK instance;

    @Override
    public void onEnable() {
        instance = this;
        this.getDataFolder().mkdirs();
        Language.load(this.getServer().getLanguage().getLang());
        this.api = new EssentialsAPI(this);
        CommandManager.registerAll(this.api);
        this.getServer().getPluginManager().registerEvents(new EventListener(this.api), this);
        task = this.getServer().getScheduler().scheduleRepeatingTask(this, new TeleportationTask(api), 1);
    }

    @Override
    public void onDisable() {
        task.cancel();
    }
}
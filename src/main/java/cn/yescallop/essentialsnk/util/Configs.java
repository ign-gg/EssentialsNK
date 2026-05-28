package cn.yescallop.essentialsnk.util;

import cn.nukkit.scheduler.PluginTask;
import cn.nukkit.scheduler.TaskHandler;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import cn.yescallop.essentialsnk.EssentialsNK;

import java.io.Closeable;
import java.util.HashSet;
import java.util.Set;

public class Configs implements Closeable {
    private final ConfigData[] configs;
    private final TaskHandler reloadTaskHandler;

    public Configs(EssentialsNK plugin, ConfigType... configTypes) {
        this.reloadTaskHandler = plugin.getServer().getScheduler().scheduleDelayedRepeatingTask(plugin, new ConfigChangeTask(plugin), 2400, 2400); //1200

        configs = new ConfigData[configTypes.length];
        for (ConfigType configType : configTypes) {
            configs[configType.id] = new ConfigData(configType);
        }
    }

    public void reload() {
        reloadTaskHandler.run(reloadTaskHandler.getLastRunTick());
    }

    public void set(ConfigType configType, String key, Object value) {
        synchronized (configType.getFile()) {
            this.getConfig(configType).set(key, value);
        }
    }

    public <T> T get(ConfigType configType, String key, T defaultValue) {
        synchronized (configType.getFile()) {
            return this.getConfig(configType).get(key, defaultValue);
        }
    }

    public boolean exists(ConfigType configType, String key) {
        synchronized (configType.getFile()) {
            return this.getConfig(configType).exists(key);
        }
    }

    public void remove(ConfigType configType, String key) {
        synchronized (configType.getFile()) {
            this.getConfig(configType).remove(key);
        }
    }

    public Set<String> getKeys(ConfigType configType) {
        synchronized (configType.getFile()) {
            return this.getConfig(configType).getKeys();
        }
    }

    private ConfigData getConfig(ConfigType configType) {
        return this.configs[configType.id];
    }

    @Override
    public void close() {
        this.reloadTaskHandler.cancel();
    }

    private class ConfigChangeTask extends PluginTask<EssentialsNK> {

        public ConfigChangeTask(EssentialsNK owner) {
            super(owner);
        }

        @Override
        public void onRun(int i) {
            saveIfNeeded();
        }

        @Override
        public void onCancel() {
            saveIfNeeded();
        }

        private void saveIfNeeded() {
            for (ConfigData data : Configs.this.configs) {
                if (data.changed) {
                    data.changed = false;
                    //data.config.reload();
                    for (String key : data.removed) {
                        data.config.remove(key);
                    }
                    data.config.getRootSection().putAll(data.added);

                    data.config.save(false, true);
                }
            }
        }
    }

    private static class ConfigData {
        private final Config config;
        private final ConfigSection added;
        private volatile boolean changed;
        private final Set<String> removed = new HashSet<>();

        private ConfigData(ConfigType configType) {
            this.config = new Config(configType.getFile(), configType.getType());
            this.added = new ConfigSection();
        }

        public void set(String key, Object value) {
            this.added.set(key, value);
            this.removed.remove(key);
            this.changed = true;
        }

        @SuppressWarnings("unchecked")
        private <T> T get(String key, T defaultValue) {
            if (this.added.exists(key)) {
                return (T) this.added.get(key);
            }
            if (this.removed.contains(key)) {
                return defaultValue;
            }
            Object object = this.config.get(key);
            if (object == null) {
                return defaultValue;
            }
            return (T) object;
        }

        public boolean exists(String key) {
            if (this.added.exists(key)) {
                return true;
            } else if (this.removed.contains(key)) {
                return false;
            }
            return this.config.exists(key);
        }

        public void remove(String key) {
            this.removed.add(key);
            this.added.remove(key);
            this.changed = true;
        }

        public Set<String> getKeys() {
            Set<String> keys = this.config.getKeys();
            keys.removeAll(this.removed);

            return keys;
        }
    }
}

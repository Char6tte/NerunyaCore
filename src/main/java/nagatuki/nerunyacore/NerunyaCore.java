package nagatuki.nerunyacore;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public final class NerunyaCore extends JavaPlugin implements Listener{


    String tutprefix;
    String tutmotd;
    String joinmsg;

    public VaultManager vault = null;
    public CustomConfig config = null;
    public CustomConfig log = null;

    private File customConfigFile;
    private FileConfiguration customConfig;

    HashMap<String, Integer> kill = new HashMap<>();

    @Override
    public void onEnable() {
        /*
        PlaceholderAPI存在確認
        */
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            Bukkit.getPluginManager().registerEvents(this, this);
        } else {
            throw new RuntimeException("Could not find PlaceholderAPI!! Plugin can not work without it!");
        }
        //イベント取得
        getServer().getPluginManager().registerEvents(this, this);
        //config関係
        config = new CustomConfig(this);
        log = new CustomConfig(this, "log.yml");
        createCustomConfig();
        //configないメッセージ　カラーコード変換
        loadColoarsMessages();
        //Vault利用変数
        vault = new VaultManager(this);

        getCommand("nerunyacore").setExecutor(new commands(this));
        getCommand("nc").setExecutor(new commands(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveConfig();
    }


    public FileConfiguration getCustomConfig() {
        return this.customConfig;
    }

    private void createCustomConfig() {
        customConfigFile = new File(getDataFolder(), "log.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            saveResource("log.yml", false);
        }

        customConfig = new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    //ログインメッセージ
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        String joinText = joinmsg;

        // We parse the placeholders using "setPlaceholders"
        joinText = PlaceholderAPI.setPlaceholders(p, joinText);

        event.setJoinMessage(joinText);
    }


    //config練習用
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (commandLabel.equalsIgnoreCase("motd")) {
            if(sender instanceof Player){
                Player p = (Player) sender;
                p.sendMessage(this.tutmotd);
            }else{
                sender.sendMessage(this.tutmotd);
                return true;
            }
        }
        return true;
    }


    //カラーコード変換
    public void loadColoarsMessages() {
// we'll load the prefix first so we can add it to every other message if it wasn't the first to be load you will get a NullPointerException.

        this.tutprefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("tutMessages.prefix"));

        this.tutmotd = (this.tutprefix + " " + ChatColor.translateAlternateColorCodes('&', getConfig().getString("tutMessages.motd")));

        this.joinmsg = ChatColor.translateAlternateColorCodes('&', getConfig().getString("JoinMessage"));
    }


    /**
     * エンティティが死亡するときに呼ばれる
     *
     * @param e
     */
    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent e) {

        //Monster monsterEnt = (Monster) e.getEntity();
        //Player mcPlayer = (Player)monsterEnt.getKiller();
        //Player target = monsterEnt.getKiller();
        LivingEntity entity = e.getEntity();
        Player player = entity.getKiller();
        List<String> s = this.getConfig().getStringList("map");

        // null=プレイヤーが殺したのではないなら
        if (player == null) {
            return; // 何もしない
        }else if (e.getEntityType() == EntityType.ZOMBIE) {
            //キル数加算
            kill.put( "zombie", + 1);
        }else if (e.getEntityType() == EntityType.SKELETON) {
            //キル数加算
            kill.put("skeleton", +1);
        }else if (e.getEntityType() == EntityType.CREEPER) {
            //キル数加算
            kill.put("creeper", +1);
        }
        // データを保存
        saveHashMap(kill);
    }

    public void saveHashMap(HashMap<String,Integer> kill){
        for (Object key : kill.keySet()) {
            config.getConfig().set("HashMap."+key, kill.get(key));
        }
//important: save the config!
        saveConfig();
    }

}
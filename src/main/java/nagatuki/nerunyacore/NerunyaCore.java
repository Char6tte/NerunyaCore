package nagatuki.nerunyacore;

import eu.theindra.geoip.api.GeoIP;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetAddress;

public final class NerunyaCore extends JavaPlugin implements Listener {


    String tutprefix;
    String tutmotd;
    String joinmsg;
    private static NerunyaCore instance;
    public static NerunyaCore getInstance(){
        return instance;
    }

    public VaultManager vault = null;

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            Bukkit.getPluginManager().registerEvents(this, this);
        } else {
            throw new RuntimeException("Could not find PlaceholderAPI!! Plugin can not work without it!");
        }
        // Plugin startup logic
        instance = this;
        getServer().getPluginManager().registerEvents(this, this);
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        loadTutorialMessages();
        vault = new VaultManager(this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveConfig();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        String joinText = joinmsg;

        // We parse the placeholders using "setPlaceholders"
        joinText = PlaceholderAPI.setPlaceholders(p, joinText);

        event.setJoinMessage(joinText);



        ////////////////////////////////
        //      GeoIPチェック
        ////////////////////////////////

        double balance = vault.getBalance(p.getUniqueId());

        //          全ユーザに通知
        /*
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            int loginCount = getLoginCount(p.getUniqueId());
            player.sendMessage(ChatColor.YELLOW + p.getDisplayName() + "さんがログインしました");

            if (loginCount != -1) {
                if (loginCount == 0) {
                    player.sendMessage(ChatColor.GREEN + "はじめてのログインです！");
                }
                {
                    player.sendMessage(ChatColor.YELLOW + "" + loginCount + "回目のログインです");
                }
            }
        }
        */
    }


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

    // now for the "setup-config" method

    public void loadTutorialMessages() {
// we'll load the prefix first so we can add it to every other message if it wasn't the first to be load you will get a NullPointerException.

        this.tutprefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("tutMessages.prefix"));

        this.tutmotd = (this.tutprefix + " " + ChatColor.translateAlternateColorCodes('&', getConfig().getString("tutMessages.motd")));

        this.joinmsg = ChatColor.translateAlternateColorCodes('&', getConfig().getString("JoinMessage"));
    }
}
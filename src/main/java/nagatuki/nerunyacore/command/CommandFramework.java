package nagatuki.nerunyacore.command;

import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class CommandFramework implements CommandExecutor {

    private String label;


    public CommandFramework(String label) {
        this.label = label;
    }


    public abstract void execute(CommandSender sender, String label, String[] args);


    /**
     * Default implementation of Bukkit's command executor.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        try {
            execute(sender, label, args);

        } catch (CommandException ex) {
            if (ex.getMessage() != null && !ex.getMessage().isEmpty()) {
                // Use RED by default
                sender.sendMessage(ChatColor.RED + ex.getMessage());
            }
        }

        return true;
    }


    /**
     * Register a command through the framework.
     */
    public static boolean register(JavaPlugin plugin, CommandFramework command) {
        PluginCommand pluginCommand = plugin.getCommand(command.label);

        if (pluginCommand == null) {
            return false;
        }

        pluginCommand.setExecutor(command);
        return true;
    }

}
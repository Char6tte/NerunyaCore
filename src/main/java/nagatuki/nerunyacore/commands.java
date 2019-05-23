package nagatuki.nerunyacore;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class commands extends JavaPlugin implements Listener {

    public commands(NerunyaCore core){
        core.getCommand("nc").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (commandLabel.equalsIgnoreCase("nc")) {
            reloadConfig();
        }
        return true;
    }

}

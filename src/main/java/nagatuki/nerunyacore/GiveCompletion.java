package nagatuki.nerunyacore;

import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class GiveCompletion implements TabCompleter {
    public GiveCompletion() {}

    public List<String> onTabComplete(CommandSender sender, Command cmd, String Label, String[] args)
    {
        Material[] list = Material.values();
        List<String> fList = Lists.newArrayList();



        if (args.length == 1) {
            for (Material s : list) {
                if (s.name().toLowerCase().startsWith(args[0].toLowerCase())) {
                    fList.add(s.name().toLowerCase());
                }
            }
            return fList;
        }
        return null;
    }
}
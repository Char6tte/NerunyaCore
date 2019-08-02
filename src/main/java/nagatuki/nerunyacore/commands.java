package nagatuki.nerunyacore;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;


public class commands implements Listener, CommandExecutor {

    private NerunyaCore core;

    public commands(NerunyaCore core) {
        this.core = core;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (commandLabel.equalsIgnoreCase("nerunyacore") || commandLabel.equalsIgnoreCase("nc")) {

            if (args.length == 0) {
                if (sender instanceof Player) {
                    sender.sendMessage("Command Help");
                } else {
                    sender.sendMessage("aaaaa");
                }
            } else if (args[0].equalsIgnoreCase("reload")) {
                core.log.reloadConfig();
                sender.sendMessage("Comprete!!!");
                return true;
            }else if(args[0].equalsIgnoreCase("killcount")) {
                int zombie= core.kill.get("zombie");
                int skeleton = core.kill.get("skeleton");
                int creeper = core.kill.get("creeper");
                sender.sendMessage("-------------------------------------------------");
                sender.sendMessage("ゾンビ討伐数(合計)"+ zombie);
                sender.sendMessage("スケルトン討伐数(合計)"+ skeleton);
                sender.sendMessage("クリーパー討伐数(合計)"+ creeper);
                sender.sendMessage("-------------------------------------------------");
                return true;
            } else if (args[0].equalsIgnoreCase("report")) {
                /* コマンド詳細
                 * ./report <違反者のID> <報告内容>
                 */
                if ((args.length < 2)) {
                    sender.sendMessage("引数が少なすぎです！");
                    return false;
                } else if (args.length > 3) {//コマンドの引数の数がおかしかったらコマンドを弾く
                    //実際のプラグインではその違反者が実際に存在するかを確認したほうがいいかもしれない
                    //ついでにログの保存やら何かしたほうがいいかもしれない
                    sender.sendMessage("引数が多すぎます！！！");

                    core.log.getConfig().set("Players." + sender.getName(), sender);
                    core.log.getConfig().set("Players." + sender.getName() + ".MCID", args[1]);
                    core.log.getConfig().set("Players." + sender.getName() + ".MCID.reason", args[2]);
                    core.log.saveConfig();
                    return true;
                }
                if (!(sender instanceof Player)) {
                    return true;
                }//コンソールから実行した場合とりあえずコマンドを弾いてみる

                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.isOp()) {
                        Player player = p.getPlayer();
                        player.sendMessage(args[1] + "が" + args[2] + "している可能性があります。");
                    }
                }
                core.log.getConfig().set("Players." + sender.getName(), sender);
                core.log.getConfig().set("Players." + sender.getName() + ".MCID", args[1]);
                core.log.getConfig().set("Players." + sender.getName() + ".MCID.reason", args[2]);
                core.log.saveConfig();

                sender.sendMessage("ご報告ありがとうございました。");//報告してくれた人に送信
                return true;
            }
            return true;
        }
        return true;
    }
}

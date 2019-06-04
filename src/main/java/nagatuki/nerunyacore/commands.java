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
            Player senders = (Player) sender;

            if (args.length == 0) {
                sender.sendMessage("Command Help");
            } else if (args[0].equalsIgnoreCase("reload")) {
                core.reloadConfig();
                sender.sendMessage("Comprete!!!");
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
                    core.getCustomConfig().set("Players." + sender.getName(), sender);
                    core.getCustomConfig().set("Players." + sender.getName() + ".MCID", args[1]);
                    core.getCustomConfig().set("Players." + sender.getName() + ".MCID.reason", args[2]);
                    return true;
                }
                //ここからエラー
                if (!(sender instanceof Player)) {
                    return true;
                }//コンソールから実行した場合とりあえずコマンドを弾いてみる

                //Set<OfflinePlayer> ops = core.getServer().getOperators();//とりあえずオペレータたちを取得
                //Iterator<OfflinePlayer> ite = ops.iterator();//繰り返し処理用のイテレータを取得

                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.isOp()) {
                        Player player = p.getPlayer();
                        player.sendMessage(args[0] + "が" + args[1] + "している可能性があります。");
                    }
                }
                core.getCustomConfig().set("Players." + sender.getName(), sender);
                core.getCustomConfig().set("Players." + sender.getName() + ".MCID", args[1]);
                core.getCustomConfig().set("Players." + sender.getName() + ".MCID.reason", args[2]);

                sender.sendMessage("ご報告ありがとうございました。");//報告してくれた人に送信
                return true;
            }else {
                        sender.sendMessage("Not!!!");
            }
            sender.sendMessage("UnkownCommand!!!");
            return true;
        }
        return true;
    }
}

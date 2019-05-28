package nagatuki.nerunyacore;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Iterator;
import java.util.Set;

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

                Set<OfflinePlayer> ops = core.getServer().getOperators();//とりあえずオペレータたちを取得
                Iterator<OfflinePlayer> ite = ops.iterator();//繰り返し処理用のイテレータを取得

                while (ite.hasNext()) {//イテレータを使って繰り返し処理するときのお約束の書き方
                    OfflinePlayer offlinePlayer = ite.next();
                    if (offlinePlayer.isOnline()) {//オンラインであれば
                        //Player op = (Player) offlinePlayer;//Player型にキャストできる
                        if(op == null){
                            core.getCustomConfig().set("Players." + sender.getName(), sender);
                            core.getCustomConfig().set("Players." + sender.getName() + ".MCID", args[1]);
                            core.getCustomConfig().set("Players." + sender.getName() + ".MCID.reason", args[2]);
                        }else {
                            op.sendMessage(args[0] + "が" + args[1] + "している可能性があります。");
                        }
                    }else {
                        sender.sendMessage("Not!!!");
                    }
                }




                sender.sendMessage("ご報告ありがとうございました。");//報告してくれた人に送信
                return true;
            }
            return true;
        }
        sender.sendMessage("UnkownCommand!!!");
        return true;
    }
}

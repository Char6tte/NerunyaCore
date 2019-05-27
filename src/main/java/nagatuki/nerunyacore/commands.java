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
        if(commandLabel.equalsIgnoreCase("nerunyacore") || commandLabel.equalsIgnoreCase("nc")) {
            Player senders = (Player) sender;
            if (args[0].equalsIgnoreCase("reload")) {
                core.reloadConfig();
                sender.sendMessage("Comprete!!!");
                return true;
            } else if (args[0].equalsIgnoreCase("report")) {
                /* コマンド詳細
                 * ./report <違反者のID> <報告内容>
                 */
                if(( args.length < 2)){
                    sender.sendMessage("引数が少なすぎです！");
                    return false;
                }else if (args.length > 3) {//コマンドの引数の数がおかしかったらコマンドを弾く
                    //実際のプラグインではその違反者が実際に存在するかを確認したほうがいいかもしれない
                    //ついでにログの保存やら何かしたほうがいいかもしれない
                    sender.sendMessage("引数が多すぎます！！！");
                    core.getCustomConfig().set("Players." + sender.getName(), sender);
                    core.getCustomConfig().set("Players." + sender.getName() + ".MCID", args[1]);
                    core.getCustomConfig().set("Players." + sender.getName() + ".MCID.reason", args[2]);
                    return true;
                }
                if (!(sender instanceof Player)) {//コンソールから実行した場合とりあえずコマンドを弾いてみる
                    //CommandSender->Playerのキャストは基本的なことなのでしっかり覚えておくと良いです
                    Set<OfflinePlayer> ops = core.getServer().getOperators();//とりあえずオペレータたちを取得
                    Iterator<OfflinePlayer> ite = ops.iterator();//繰り返し処理用のイテレータを取得

                    while (ite.hasNext()) {//イテレータを使って繰り返し処理するときのお約束の書き方
                        OfflinePlayer offlinePlayer = ite.next();
                        if (offlinePlayer.isOnline()) {//オンラインであれば
                            Player op = (Player) offlinePlayer;//Player型にキャストできる
                            op.sendMessage(args[1] + "が" + args[2] + "している可能性があります。");
                            //senderを参照すればコマンドを実行したプレイヤーの情報がわかりますのでそれを用いてメッセージを組み立ててください。
                            //記事下部に参考までにPlayerインターフェイスのjavadocのリンクを張りますのでそれを参照し、必要な情報を取得してください。
                            //あえて詳しくは書きません。
                        }
                    }
                    senders.sendMessage("ご報告ありがとうございました。");//報告してくれた人に送信
                }
                return true;
            }
            sender.sendMessage("UnkownCommand!!!");
            return true;
        }
        return true;
    }

}

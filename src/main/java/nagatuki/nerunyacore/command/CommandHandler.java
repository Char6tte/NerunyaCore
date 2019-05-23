package nagatuki.nerunyacore.command;


import nagatuki.nerunyacore.NerunyaCore;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.Set;


public class CommandHandler extends CommandFramework {

    public CommandHandler(String label) {
        super(label);
    }
    private NerunyaCore core;


    @Override
    public void execute(CommandSender sender, String commandLabel, String[] args) {
        if (commandLabel.equalsIgnoreCase("reload")) {
            this.core.reloadConfig();
        } else if (commandLabel.equalsIgnoreCase("report")) {
            /* コマンド詳細
             * ./report <違反者のID> <報告内容>
             */
            if (args.length != 2) {//コマンドの引数の数がおかしかったらコマンドを弾く
                //実際のプラグインではその違反者が実際に存在するかを確認したほうがいいかもしれない
                //ついでにログの保存やら何かしたほうがいいかもしれない

                core.getCustomConfig().set("Players." + sender.getName(), sender);
                core.getCustomConfig().set("Players." + sender.getName() + ".MCID", args[0]);
                core.getCustomConfig().set("Players." + sender.getName() + ".MCID.reason", args[1]);
            }
            if (!(sender instanceof Player)) {//コンソールから実行した場合とりあえずコマンドを弾いてみる
            Player senders = (Player) sender;//CommandSender->Playerのキャストは基本的なことなのでしっかり覚えておくと良いです

            Set<OfflinePlayer> ops = this.getServer().getOperators();//とりあえずオペレータたちを取得
            Iterator<OfflinePlayer> ite = ops.iterator();//繰り返し処理用のイテレータを取得

            while (ite.hasNext()) {//イテレータを使って繰り返し処理するときのお約束の書き方
                OfflinePlayer offlinePlayer = ite.next();
                if (offlinePlayer.isOnline()) {//オンラインであれば
                    Player op = (Player) offlinePlayer;//Player型にキャストできる
                    op.sendMessage(args[0] + "が" + args[1] + "している可能性があります。");
                    //senderを参照すればコマンドを実行したプレイヤーの情報がわかりますのでそれを用いてメッセージを組み立ててください。
                    //記事下部に参考までにPlayerインターフェイスのjavadocのリンクを張りますのでそれを参照し、必要な情報を取得してください。
                    //あえて詳しくは書きません。
                }
            }

            senders.sendMessage("ご報告ありがとうございました。");//報告してくれた人に送信
        }
    }
}
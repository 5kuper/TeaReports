package lol.skuper.teareports.command

import kotlinx.datetime.Clock
import lol.skuper.teareports.Report
import lol.skuper.teareports.repo.ReportRepo
import lol.skuper.teareports.utils.PlayerCommandExecutor
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class ReportCommand(private val repo: ReportRepo) : PlayerCommandExecutor(), TabCompleter {
   companion object {
       const val NAME = "report"
   }

    override fun onPlayerCommand(
        sender: Player, cmd: Command, label: String, args: Array<out String>
    ): Boolean {
        if (args.size < 2) return false

        val reportedPlayer = args[0]
        if (Bukkit.getPlayer(reportedPlayer) == null) {
            sender.sendMessage(Component.text("Player isn't found.", NamedTextColor.DARK_RED))
            return true
        }

        val msgArgs = args.copyOfRange(1, args.size)
        val reportMsg = msgArgs.joinToString(" ")

        repo.create(Report(reportedPlayer, sender.name, reportMsg, Clock.System.now()))
        return true
    }

    override fun onTabComplete(
        sender: CommandSender, cmd: Command, label: String, args: Array<out String>
    ): MutableList<String>? {
        return if (args.size == 1) null else ArrayList()
    }
}
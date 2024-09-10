package lol.skuper.teareports.command

import com.github.shynixn.mccoroutine.bukkit.SuspendingCommandExecutor
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable
import lol.skuper.teareports.Report
import lol.skuper.teareports.repo.ReportRepo
import lol.skuper.teareports.util.playerOnly
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class ReportCommand(private val opt: Options, private val repoProvider: () -> ReportRepo)
        : SuspendingCommandExecutor, TabCompleter {

    companion object {
       const val NAME = "report"
   }

    @Serializable
    data class Options(
        val allowSelfReport: Boolean = false
    )

    override suspend fun onCommand(
        sender: CommandSender, command: Command, label: String, args: Array<out String>
    ): Boolean = sender.playerOnly {
        if (args.size < 2) return@playerOnly false

        val reportedPlayer = args[0]
        if (!opt.allowSelfReport && reportedPlayer == sender.name) {
            sender.sendMessage(Component.text("Why are you trying to report yourself?"))
            return@playerOnly true
        }
        if (Bukkit.getPlayer(reportedPlayer) == null) {
            sender.sendMessage(Component.text("Player isn't found.", NamedTextColor.DARK_RED))
            return@playerOnly true
        }

        val msgArgs = args.copyOfRange(1, args.size)
        val reportMsg = msgArgs.joinToString(" ")

        repoProvider().create(Report(reportedPlayer, sender.name, reportMsg, Clock.System.now()))
        sender.sendMessage(Component.text("Report has been sent."))
        return@playerOnly true
    }

    override fun onTabComplete(
        sender: CommandSender, command: Command, label: String, args: Array<out String>
    ): MutableList<String>? {
        return if (args.size == 1) null else ArrayList()
    }
}
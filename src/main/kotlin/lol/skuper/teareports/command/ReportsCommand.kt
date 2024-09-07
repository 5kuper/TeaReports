package lol.skuper.teareports.command

import com.github.shynixn.mccoroutine.bukkit.SuspendingCommandExecutor
import kotlinx.datetime.Clock
import lol.skuper.teareports.Report
import lol.skuper.teareports.repo.ReportRepo
import lol.skuper.teareports.util.playerOnly
import net.kyori.adventure.text.Component.newline
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor.*
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import java.lang.IllegalStateException

class ReportsCommand(private val repoProvider: () -> ReportRepo) : SuspendingCommandExecutor, TabCompleter {
    companion object {
        const val NAME = "reports"
    }

    enum class Subcommand(val label: String) {
        CHECK("check"), ANSWER("answer")
    }

    override suspend fun onCommand(
        sender: CommandSender, command: Command, label: String, args: Array<out String>
    ): Boolean = sender.playerOnly {
        if (args.isEmpty()) return@playerOnly false

        return@playerOnly when (args[0]) {
            Subcommand.CHECK.label -> {
                val reports = repoProvider().getAll().filter { it.answer == null }
                if (reports.isEmpty()) {
                    sender.sendMessage(text("Everything is fine, there are no reports.", GREEN))
                } else {
                    var component = text("Pending reports: ", DARK_AQUA)
                    reports.forEach {
                        component = component.append(newline())
                            .append(text("#${it.id} ", AQUA)).append(text(it.reportedPlayer, RED))
                            .append(text(" by ", WHITE)).append(text(it.reporterPlayer, BLUE))
                            .append(text(" with ", WHITE)).append(text("'${it.msg}'", GOLD))
                    }
                    sender.sendMessage(component)
                }
                true
            }
            Subcommand.ANSWER.label -> {
                if (args.size < 3) false
                else {
                    val repo = repoProvider()

                    val reportId = args[1].toIntOrNull() ?: return@playerOnly false
                    val report = repo.getById(reportId) ?: throw IllegalStateException("Report isn't found.")

                    if (report.answer != null) {
                        sender.sendMessage(text("Report #${reportId} already has an answer."))
                    } else {
                        val msgArgs = args.copyOfRange(2, args.size)
                        val answerMsg = msgArgs.joinToString(" ")

                        report.answer = Report.Answer(answerMsg, sender.name, Clock.System.now())
                        repo.update(report)
                        sender.sendMessage(text("You answered the #${reportId} report."))
                    }
                    true
                }
            }
            else -> false
        }
    }

    override fun onTabComplete(
        sender: CommandSender, command: Command, label: String, args: Array<out String>
    ): MutableList<String> {
        return if (args.size == 1) {
            Subcommand.entries.map { it.label }.toMutableList()
        } else {
            ArrayList()
        }
    }
}
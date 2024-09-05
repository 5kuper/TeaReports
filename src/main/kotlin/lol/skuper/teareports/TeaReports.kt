package lol.skuper.teareports

import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.bukkit.setSuspendingExecutor
import lol.skuper.teareports.command.ReportCommand
import lol.skuper.teareports.command.ReportsCommand
import lol.skuper.teareports.repo.ReportJsonRepo

class TeaReports : SuspendingJavaPlugin() {

    override suspend fun onEnableAsync() {
        if (!dataFolder.exists()) {
            dataFolder.mkdir()
        }

        val reportRepo = ReportJsonRepo(dataFolder)

        val reportCmd = getCommand(ReportCommand.NAME)!!
        val reportLogic = ReportCommand(reportRepo)
        reportCmd.setSuspendingExecutor(reportLogic)
        reportCmd.tabCompleter = reportLogic

        val reportsCmd = getCommand(ReportsCommand.NAME)!!
        val reportsLogic = ReportsCommand(reportRepo)
        reportsCmd.setSuspendingExecutor(reportsLogic)
        reportsCmd.tabCompleter = reportsLogic
    }

    override suspend fun onDisableAsync() {
        // Plugin shutdown logic
    }
}

package lol.skuper.teareports

import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.bukkit.setSuspendingExecutor
import lol.skuper.teareports.command.ReportCommand
import lol.skuper.teareports.repo.ReportJsonRepo

class TeaReports : SuspendingJavaPlugin() {

    override suspend fun onEnableAsync() {
        if (!dataFolder.exists()) {
            dataFolder.mkdir()
        }

        val reportRepo = ReportJsonRepo(dataFolder)
        getCommand(ReportCommand.NAME)?.setSuspendingExecutor(ReportCommand(reportRepo))
    }

    override suspend fun onDisableAsync() {
        // Plugin shutdown logic
    }
}

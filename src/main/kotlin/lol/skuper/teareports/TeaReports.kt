package lol.skuper.teareports

import lol.skuper.teareports.command.ReportCommand
import lol.skuper.teareports.repo.ReportJsonRepo
import org.bukkit.plugin.java.JavaPlugin

class TeaReports : JavaPlugin() {

    override fun onEnable() {
        if (!dataFolder.exists()) {
            dataFolder.mkdir()
        }

        val reportRepo = ReportJsonRepo(dataFolder)
        getCommand(ReportCommand.NAME)?.setExecutor(ReportCommand(reportRepo))
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}

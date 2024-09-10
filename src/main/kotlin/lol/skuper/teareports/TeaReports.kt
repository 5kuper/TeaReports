package lol.skuper.teareports

import com.charleskorn.kaml.Yaml
import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.bukkit.setSuspendingExecutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import lol.skuper.teareports.command.ReportCommand
import lol.skuper.teareports.command.ReportsCommand
import lol.skuper.teareports.repo.ReportJsonRepo
import java.io.File

class TeaReports : SuspendingJavaPlugin() {

    @Serializable
    data class Config(
        val reportCommand: ReportCommand.Options = ReportCommand.Options(),
        val reportsCommand: ReportsCommand.Options = ReportsCommand.Options()
    )

    override suspend fun onEnableAsync() {
        if (!dataFolder.exists()) dataFolder.mkdir()
        val configFile = File(dataFolder, "config.yml")

        val config = withContext(Dispatchers.IO) {
            if (configFile.exists()) {
                runCatching {
                    Yaml.default.decodeFromString<Config>(configFile.readText())
                }.onFailure {
                    if (it is SerializationException) {
                        throw SerializationException("Invalid config.yml: \"${it.message}\"." +
                                " Fix the file or remove it to create the default one.")
                    }
                }.getOrThrow()
            } else Config().apply {
                configFile.createNewFile()
                configFile.writeText(Yaml.default.encodeToString(this))
            }
        }

        val reportRepoProvider = { ReportJsonRepo(dataFolder) }

        val reportCmd = getCommand(ReportCommand.NAME)!!
        val reportLogic = ReportCommand(config.reportCommand, reportRepoProvider)
        reportCmd.setSuspendingExecutor(reportLogic)
        reportCmd.tabCompleter = reportLogic

        val reportsCmd = getCommand(ReportsCommand.NAME)!!
        val reportsLogic = ReportsCommand(config.reportsCommand, reportRepoProvider)
        reportsCmd.setSuspendingExecutor(reportsLogic)
        reportsCmd.tabCompleter = reportsLogic
    }

    override suspend fun onDisableAsync() {
        // Plugin shutdown logic
    }
}

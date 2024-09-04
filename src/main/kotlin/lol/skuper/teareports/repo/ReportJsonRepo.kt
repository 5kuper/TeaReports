package lol.skuper.teareports.repo

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import lol.skuper.teareports.Report
import java.io.File

class ReportJsonRepo(private val folder : File) : ReportRepo {
    companion object {
        const val FILENAME = "reports.json"
    }

    private val mutex = Mutex()

    override suspend fun create(report: Report) {
        val reports = getAll() + report
        val file = File(folder, FILENAME)
        mutex.withLock {
            file.createNewFile()
            file.writeText(Json.encodeToString(reports))
        }
    }

    override suspend fun getAll(): List<Report> {
        val file = File(folder, FILENAME)
        if (file.exists()) {
            mutex.withLock {
                val json = file.readText()
                return Json.decodeFromString(json)
            }
        } else {
            return emptyList()
        }
    }
}
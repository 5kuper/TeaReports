package lol.skuper.teareports.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import lol.skuper.teareports.Report
import java.io.File

class ReportJsonRepo(private val folder : File) : ReportRepo {
    companion object {
        const val FILENAME = "reports.json"
    }

    private val mutex = Mutex()
    private val json = Json { prettyPrint = true }

    override suspend fun create(report: Report) {
        val reports = getAll().toMutableList()
        reports += report.apply {
            this.id = if (reports.isEmpty()) 1 else reports.last().id + 1
        }

        val file = File(folder, FILENAME)
        withContext(Dispatchers.IO) {
            mutex.withLock {
                file.createNewFile()
                file.writeText(json.encodeToString(reports))
            }
        }
    }

    override suspend fun getAll(): List<Report> {
        val file = File(folder, FILENAME)
        return if (file.exists()) {
            withContext(Dispatchers.IO) {
                mutex.withLock {
                    json.decodeFromString(file.readText())
                }
            }
        } else {
            emptyList()
        }
    }
}
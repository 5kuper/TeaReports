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

    private var allCached: List<Report>? = null

    override suspend fun getAll(): List<Report> {
        allCached?.let { return it }
        var result: List<Report>

        val file = File(folder, FILENAME)
        if (file.exists()) {
            withContext(Dispatchers.IO) {
                mutex.withLock {
                    result = json.decodeFromString(file.readText())
                }
            }
        } else {
            result = emptyList()
        }

        allCached = result
        return result
    }

    override suspend fun getById(id: Int): Report? {
        return getAll().firstOrNull { it.id == id }
    }

    override suspend fun create(report: Report) {
        val reports = getAll().toMutableList()
        reports += report.apply {
            id = if (reports.isEmpty()) 1 else reports.last().id + 1
        }
        rewrite(reports)
    }

    override suspend fun update(report: Report) {
        val reports = getAll().toMutableList()

        val index = reports.indexOfFirst { it.id == report.id }
        if (index == -1) {
            throw IllegalStateException("Report isn't found.")
        }

        reports[index] = report
        rewrite(reports)
    }

    private suspend fun rewrite(reports: List<Report>) {
        val file = File(folder, FILENAME)
        withContext(Dispatchers.IO) {
            mutex.withLock {
                file.createNewFile()
                file.writeText(json.encodeToString(reports))
            }
        }
        allCached = null
    }
}
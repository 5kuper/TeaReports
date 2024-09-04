package lol.skuper.teareports.repo

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import lol.skuper.teareports.Report
import java.io.File

class ReportJsonRepo(private val folder : File) : ReportRepo {
    companion object {
        const val FILENAME = "reports.json"
    }

    override fun create(report: Report) {
        val reports = getAll() + report
        val file = File(folder, FILENAME)
        file.createNewFile()
        file.writeText(Json.encodeToString(reports))
    }

    override fun getAll(): List<Report> {
        val file = File(folder, FILENAME)
        if (file.exists()) {
            val json = file.readText()
            return Json.decodeFromString(json)
        } else {
            return emptyList()
        }
    }
}
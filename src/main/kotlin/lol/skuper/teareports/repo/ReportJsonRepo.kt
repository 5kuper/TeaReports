package lol.skuper.teareports.repo

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import lol.skuper.teareports.Report

class ReportJsonRepo : ReportRepo {
    override fun create(report: Report) {
        val json = Json.encodeToString(report)
        println(json)
    }

    override fun getAll(): List<Report> {
        return emptyList()
    }
}
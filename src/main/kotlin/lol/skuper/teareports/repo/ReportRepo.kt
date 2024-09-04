package lol.skuper.teareports.repo

import kotlinx.coroutines.Job
import lol.skuper.teareports.Report

interface ReportRepo {
    suspend fun create(report: Report)

    suspend fun getAll() : List<Report>
}
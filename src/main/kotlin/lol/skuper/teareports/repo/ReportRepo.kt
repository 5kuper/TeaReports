package lol.skuper.teareports.repo

import lol.skuper.teareports.Report

interface ReportRepo {
    suspend fun getAll() : List<Report>
    suspend fun getById(id: Int) : Report?
    suspend fun create(report: Report)
    suspend fun update(report: Report)
}
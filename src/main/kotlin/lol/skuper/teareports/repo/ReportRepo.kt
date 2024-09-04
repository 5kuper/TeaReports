package lol.skuper.teareports.repo

import lol.skuper.teareports.Report

interface ReportRepo {
    fun create(report: Report)

    fun getAll() : List<Report>
}
package com.innaval.desafioinchurch.data.models

data class PageModel<T>(
    val page: Int,
    val results: List<T>,
    val total_results: Int,
    val total_pages: Int
)
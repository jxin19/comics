package com.homework.comics.viewStats.application.extension

import com.homework.comics.viewStats.domain.ViewStats
import com.homework.comics.work.domain.Work

fun Work.toWorkInfo() = ViewStats.WorkInfo(
    id = id,
    title = titleValue,
    description = descriptionValue,
    author = authorValue,
    isActive = isActive,
    isAdultOnly = isAdultOnly,
)

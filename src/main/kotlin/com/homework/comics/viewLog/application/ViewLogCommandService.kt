package com.homework.comics.viewLog.application

interface ViewLogCommandService {
    fun create(workId: Long, memberId: Long)
    fun deleteByWorkId(workId: Long)
}

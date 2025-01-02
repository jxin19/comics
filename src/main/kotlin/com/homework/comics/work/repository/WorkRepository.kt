package com.homework.comics.work.repository

import com.homework.comics.work.domain.Work
import org.springframework.data.repository.CrudRepository

interface WorkRepository : CrudRepository<Work, Long> {
}

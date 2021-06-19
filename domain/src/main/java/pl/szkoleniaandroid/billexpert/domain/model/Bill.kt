package pl.szkoleniaandroid.billexpert.domain.model

import org.threeten.bp.LocalDateTime
import java.io.Serializable
import java.util.*

data class Bill(
        val userId: String,
        val date: Date = Date(),
        val name: String = "",
        val amount: Double = 0.0,
        val category: Category = Category.OTHER,
        val comment: String = "",
        val objectId: String = "",
        val createdAt: LocalDateTime? = null,
        val updatedAt: LocalDateTime? = null
) : Serializable

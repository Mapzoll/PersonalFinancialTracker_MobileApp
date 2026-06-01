package com.example.projectmap.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "financial_transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val transactionId: Int = 0,
    val firebaseId: String = "",
    val userId: String,
    val transactionType: String, // "income" or "expense"
    val amountOfMoney: Long,
    val category: String,
    val date: Long,
    val note: String = "",
    val isSynced: Boolean = false
)

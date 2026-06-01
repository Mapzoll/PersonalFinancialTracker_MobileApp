package com.example.projectmap.data.repository

import com.example.projectmap.data.local.TransactionDao
import com.example.projectmap.data.local.TransactionEntity
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TransactionRepository(
    private val transactionDao: TransactionDao,
    private val userId: String,
    private val isDemoMode: Boolean = false
) {
    private val database = if (!isDemoMode) {
        try { FirebaseDatabase.getInstance().getReference("transactions").child(userId) } catch (e: Exception) { null }
    } else {
        null
    }

    val allTransactions: Flow<List<TransactionEntity>> = transactionDao.getAllTransactions(userId)

    suspend fun addTransaction(transaction: TransactionEntity) {
        // 1. Save to Local Room first
        transactionDao.insertTransaction(transaction)
        
        // 2. Sync to Firebase if not in demo mode
        if (!isDemoMode) {
            syncToFirebase(transaction)
        }
    }

    private suspend fun syncToFirebase(transaction: TransactionEntity) {
        if (database == null) return
        try {
            val ref = if (transaction.firebaseId.isEmpty()) {
                database.push()
            } else {
                database.child(transaction.firebaseId)
            }
            
            val firebaseId = ref.key ?: ""
            val updatedTransaction = transaction.copy(firebaseId = firebaseId, isSynced = true)
            
            ref.setValue(updatedTransaction).await()
            
            // Update local Room with firebaseId and sync status
            transactionDao.updateTransaction(updatedTransaction)
        } catch (e: Exception) {
            // Keep as unsynced in Room
            e.printStackTrace()
        }
    }

    suspend fun syncUnsynced() {
        if (isDemoMode) return
        val unsynced = transactionDao.getUnsyncedTransactions()
        unsynced.forEach {
            syncToFirebase(it)
        }
    }

    // Initial fetch from Firebase to populate local Room
    fun fetchFromFirebase() {
        if (isDemoMode || database == null) return
        database.get().addOnSuccessListener { snapshot ->
            CoroutineScope(Dispatchers.IO).launch {
                snapshot.children.forEach { child ->
                    val transaction = child.getValue(TransactionEntity::class.java)
                    if (transaction != null) {
                        transactionDao.insertTransaction(transaction.copy(isSynced = true))
                    }
                }
            }
        }
    }
}

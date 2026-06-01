package com.example.projectmap.ui.dashboard

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Date

@Suppress("PropertyName")
data class Transaction(
    val transaction_id: String = "",
    val user_id: String = "",
    val transaction_type: String = "",
    val amount_of_money: Long = 0L,
    val category: String = "",
    val date: Date = Date()
)

class TransactionViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    private val _totalBalance = MutableStateFlow(0L)
    val totalBalance: StateFlow<Long> = _totalBalance.asStateFlow()

    private val _totalIncome = MutableStateFlow(0L)
    val totalIncome: StateFlow<Long> = _totalIncome.asStateFlow()

    private val _totalExpense = MutableStateFlow(0L)
    val totalExpense: StateFlow<Long> = _totalExpense.asStateFlow()

    init {
        auth.addAuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser != null) {
                fetchTransactions()
            } else {
                _transactions.value = emptyList()
                _totalBalance.value = 0L
                _totalIncome.value = 0L
                _totalExpense.value = 0L
                _isLoading.value = false
            }
        }
    }

    private fun fetchTransactions() {
        val userId = auth.currentUser?.uid ?: run {
            _isLoading.value = false
            return
        }

        _isLoading.value = true

        db.collection("financial_transactions")
            .whereEqualTo("user_id", userId)
            .addSnapshotListener { snapshot, error ->

                _isLoading.value = false
                if (error != null) {
                    println("🔥 ERROR FIREBASE: ${error.message}")
                    return@addSnapshotListener
                }

                if (snapshot == null) return@addSnapshotListener

                val transList = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Transaction::class.java)?.copy(transaction_id = doc.id)
                }

                val sortedList = transList.sortedByDescending { it.date }

                _transactions.value = sortedList
                calculateTotals(sortedList)
            }
    }

    private fun calculateTotals(list: List<Transaction>) {
        var income = 0L
        var expense = 0L
        for (t in list) {
            if (t.transaction_type == "income") income += t.amount_of_money
            else expense += t.amount_of_money
        }
        _totalIncome.value = income
        _totalExpense.value = expense
        _totalBalance.value = income - expense
    }

    fun addTransaction(type: String, amount: Long, category: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            onError("Sesi habis, silakan login ulang.")
            return
        }

        val data = hashMapOf(
            "user_id" to userId,
            "transaction_type" to type,
            "amount_of_money" to amount,
            "category" to category,
            "date" to Date()
        )

        db.collection("financial_transactions")
            .add(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onError(e.message ?: "Gagal menyimpan") }
    }

    fun updateTransaction(transactionId: String, newCategory: String, newAmount: Long, onSuccess: () -> Unit, onError: (String) -> Unit) {
        db.collection("financial_transactions").document(transactionId)
            .update(mapOf("category" to newCategory, "amount_of_money" to newAmount))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onError(e.message ?: "Gagal mengubah") }
    }

    fun deleteTransaction(transactionId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        db.collection("financial_transactions").document(transactionId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onError(e.message ?: "Gagal menghapus") }
    }

    fun refreshData() {
        fetchTransactions()
    }
}
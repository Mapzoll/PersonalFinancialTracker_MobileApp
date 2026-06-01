package com.example.projectmap.util

object NlpParser {

    data class ParsedTransaction(
        val type: String,
        val category: String,
        val amount: Int
    )

    fun parseNaturalLanguage(input: String): ParsedTransaction? {
        val lowerInput = input.lowercase().trim()

        var amount = 0
        val numberRegex = Regex("(\\d+)\\s*(rb|ribu|jt|juta)?")
        val matchResult = numberRegex.find(lowerInput)

        if (matchResult != null) {
            val baseNumber = matchResult.groupValues[1].toInt()
            val multiplierStr = matchResult.groupValues[2]

            amount = when (multiplierStr) {
                "rb", "ribu" -> baseNumber * 1000
                "jt", "juta" -> baseNumber * 1000000
                else -> baseNumber
            }
        } else {
            return null
        }

        var type = "expense"
        var category = "Lainnya"
        val incomeKeywords = listOf("gaji", "gajian", "bonus", "dapat", "masuk")
        val foodKeywords = listOf("makan", "minum", "beli kopi", "kopi", "warteg", "resto", "makanan")
        val transportKeywords = listOf("bensin", "pertamax", "parkir", "ojol", "gojek", "grab", "tol", "transportasi")
        val entertainKeywords = listOf("nonton", "bioskop", "game", "netflix", "spotify")

        if (incomeKeywords.any { lowerInput.contains(it) }) {
            type = "income"
            category = "Pemasukan"
        } else if (foodKeywords.any { lowerInput.contains(it) }) {
            category = "Makanan"
        } else if (transportKeywords.any { lowerInput.contains(it) }) {
            category = "Transportasi"
        } else if (entertainKeywords.any { lowerInput.contains(it) }) {
            category = "Hiburan"
        }

        return ParsedTransaction(type, category, amount)
    }
}
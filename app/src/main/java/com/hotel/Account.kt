package com.hotel

class Account {
    private var balance: Long = 0
    private val transactionHistory = ArrayList<String>()

    fun deposit(amount: Long): Boolean {
        balance += amount
        recordTransaction("입금", amount, balance)
        return true
    }

    fun withdraw(amount: Long): Boolean {
        if (balance >= amount) {
            balance -= amount
            recordTransaction("출금", amount, balance)
            return true
        }
        return false
    }

    fun recordTransaction(type: String, amount: Long, balance: Long) {
        transactionHistory.add("${amount}원 ${type}되었습니다. 잔액: ${balance} 원")
    }

    fun printTransactionHistory() {
        for (i in 0 until transactionHistory.size) {
            println("${i+1}. ${transactionHistory[i]}")
        }
    }

}
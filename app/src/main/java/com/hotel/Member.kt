package com.hotel

class Member {
    private var name: String
    private var balance: Long

    constructor(_name: String, _balance: Long) {
        name = _name
        balance = _balance
    }

    fun withdraw(amount: Long): Boolean {
        if (balance >= amount) {
            balance -= amount
            return true
        }
        println("잔액이 부족합니다.")
        return false
    }
}
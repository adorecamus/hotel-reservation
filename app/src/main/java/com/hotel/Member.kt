package com.hotel

class Member {
    val name: String
    val account: Account

    constructor(_name: String) {
        name = _name
        account = Account()
    }
}
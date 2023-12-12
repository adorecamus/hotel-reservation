package com.hotel

class Member {
    val name: String
    val account: Account

    constructor(_name: String, _account: Account) {
        name = _name
        account = _account
    }
}
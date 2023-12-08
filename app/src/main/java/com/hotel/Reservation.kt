package com.hotel

import java.time.LocalDate

class Reservation {
    private var member: Member
    private var roomNumber: Int
    private var checkIn: LocalDate
    private var checkOut: LocalDate
    private var expense: Long

    constructor(_member: Member, _roomNumber: Int, _checkIn: LocalDate, _checkOut: LocalDate, _expense: Long) {
        member = _member
        roomNumber = _roomNumber
        checkIn = _checkIn
        checkOut = _checkOut
        expense = _expense
    }
}
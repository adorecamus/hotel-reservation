package com.hotel

import java.time.LocalDate

class Reservation {
    val member: Member
    val roomNumber: Int
    var checkIn: LocalDate
    var checkOut: LocalDate
    val expense: Long

    constructor(_member: Member, _roomNumber: Int, _checkIn: LocalDate, _checkOut: LocalDate, _expense: Long) {
        member = _member
        roomNumber = _roomNumber
        checkIn = _checkIn
        checkOut = _checkOut
        expense = _expense
    }

    override fun toString(): String {
        return "사용자: ${member.name}, 방번호: ${roomNumber}, 체크인: ${checkIn}, 체크아웃: ${checkOut}"
    }

    fun changeReservation(checkIn: LocalDate, checkOut: LocalDate) {
        this.checkIn = checkIn
        this.checkOut = checkOut
    }
}
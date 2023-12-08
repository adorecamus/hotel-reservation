package com.hotel

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun main() {
    val memberList = ArrayList<Member>()
    val reservationHistory = ArrayList<Reservation>()

    while (true) {
        println("호텔 예약 프로그램입니다. 원하는 메뉴의 숫자를 입력하세요.")
        val menu = validateInput("menu").toString().toInt()
        when (menu) {
            1 -> {
                val balance = 1000000L
                val expense = 300000L

                val name = validateInput("name").toString()
                val roomNumber = validateInput("roomNumber").toString().toInt()
                val date = validateDate().toList()

                val member = Member(name, balance)
                memberList.add(member)

                val reservation = Reservation(member, roomNumber, date[0], date[1], expense)
                if(!member.withdraw(expense)) {
                    continue
                }
                reservationHistory.add(reservation)
                println("호텔 예약이 완료되었습니다.")
            }

            2 -> {
                println("호텔 예약 목록입니다.")
                for (i in 0 until reservationHistory.size) {
                    val reservation = reservationHistory[i]
                    println("${i+1}. $reservation")
                }
            }

            3 -> {
                println("호텔 예약 목록입니다. (체크인 날짜순 정렬)")
                val sortedHistory = reservationHistory.sortedBy { it.checkIn }
                for (i in 0 until sortedHistory.size) {
                    val reservation = sortedHistory[i]
                    println("${i+1}. $reservation")
                }
            }

            4 -> {
                println("프로그램을 종료합니다")
                break
            }

        }
    }
}

fun validateInput(type: String): Any? {
    return when (type) {
        "menu" -> {
            while (true) {
                println("[메뉴]")
                println("[1]방 예약 [2]예약 목록 [3]예약 목록 (체크인 날짜순 정렬) [4]프로그램 종료 [5]금액 입금-출금 내역 [6]예약 변경/취소")
                try {
                    var originMenu = readln()
                    val menu = originMenu.toInt()
                    if (menu < 1 || menu > 6) {
                        println("유효하지 않은 번호입니다.")
                        continue
                    }
                    return menu
                } catch (e: Exception) {
                    println("잘못된 입력입니다.")
                }
            }
        }

        "name" -> {
            while (true) {
                println("예약자분의 성함을 입력해주세요.")
                try {
                    var originName = readln()
                    return originName
                } catch (e: Exception) {
                    println("잘못된 입력입니다.")
                }
            }
        }

        "roomNumber" -> {
            while (true) {
                println("예약할 방번호를 입력해주세요. 100~999 선택 가능")
                try {
                    var originRoomNumber = readln()
                    val roomNumber: Int = originRoomNumber.toInt()
                    if (roomNumber < 100 || roomNumber > 999) {
                        println("올바르지 않은 방번호입니다.")
                        continue
                    }
                    return roomNumber
                } catch (e: Exception) {
                    println("잘못된 입력입니다.")
                }
            }
        }

        else -> {}
    }
}

fun validateDate(): Pair<LocalDate, LocalDate> {
    val formatter = DateTimeFormatter.BASIC_ISO_DATE
    val today: LocalDate = LocalDate.now()
    while (true) {
        println("체크인 날짜를 입력해주세요. 표기형식:20231208")
        try {
            var originCheckIn = readln()
            val checkIn: LocalDate = LocalDate.parse(originCheckIn, formatter)
            if (checkIn.isBefore(today)) {
                println("지난 날짜에 체크인할 수 없습니다.")
                continue
            }
            while (true) {
                println("체크아웃 날짜를 입력해주세요. 표기형식:20231208")
                try {
                    var originCheckOut = readln()
                    val checkOut: LocalDate = LocalDate.parse(originCheckOut, formatter)
                    if (checkOut.isAfter(checkIn)) {
                        return Pair(checkIn, checkOut)
                    } else {
                        println("체크인 이후의 날짜에 체크아웃할 수 있습니다.")
                    }
                } catch (e: Exception) {
                    println("잘못된 입력입니다.")
                }
            }
        } catch (e: Exception) {
            println("잘못된 입력입니다.")
        }
    }
}
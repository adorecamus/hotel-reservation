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
                val reservedRooms = reservationHistory.filter{ it.roomNumber == roomNumber}
                val date = validateDate(reservedRooms).toList()

                val member = Member(name)
                member.account.deposit(balance)
                memberList.add(member)

                val reservation = Reservation(member, roomNumber, date[0], date[1], expense)
                if(!member.account.withdraw(expense)) {
                    println("잔액이 부족합니다.")
                    continue
                }
                reservationHistory.add(reservation)
                println("호텔 예약이 완료되었습니다.")
            }

            2 -> {
                println("호텔 예약 목록입니다.")
                for (i in 0 until reservationHistory.size) {
                    println("${i+1}. ${reservationHistory[i]}")
                }
            }

            3 -> {
                println("호텔 예약 목록입니다. (체크인 날짜순 정렬)")
                val sortedHistory = reservationHistory.sortedBy { it.checkIn }
                for (i in 0 until sortedHistory.size) {
                    println("${i+1}. ${sortedHistory[i]}")
                }
            }

            4 -> {
                println("프로그램을 종료합니다")
                break
            }

            5 -> {
                val name = validateInput("name").toString()
                var memberName: String? = null
                for (member in memberList) {
                    if (name == member.name) {
                        memberName = name
                        member.account.printTransactionHistory()
                    }
                }
                if (memberName == null) {
                    println("예약된 사용자를 찾을 수 없습니다.")
                }
            }

            6 -> {
                var isCompleted = false
                while (!isCompleted) {
                    val name = validateInput("name").toString()
                    val memberReservation = reservationHistory.filter{ it.member.name == name }
                    if (memberReservation.size == 0) {
                        println("사용자 이름으로 예약된 목록을 찾을 수 없습니다.")
                        continue
                    }
                    while (true) {
                        println("$name 님이 예약한 목록입니다. 변경/취소하실 예약번호를 입력해주세요. (종료는 q 입력)")
                        for (i in 0 until memberReservation.size) {
                            println("${i+1}. ${memberReservation[i]}")
                        }
                        try {
                            var originReservationNumber = readln()
                            if (originReservationNumber == "q") {
                                isCompleted = true
                                break
                            }
                            val reservationNumber = originReservationNumber.toInt()
                            if (reservationNumber > memberReservation.size || reservationNumber < 1) {
                                println("범위에 없는 예약번호입니다.")
                                continue
                            }
                            val selectedRoom = memberReservation[reservationNumber-1]
                            println("해당 예약을 어떻게 하시겠어요? 1. 변경 2. 취소 / 이외 번호 입력 시 이전으로 돌아갑니다.")
                            var originNumber = readln()
                            val number = originNumber.toInt()
                            when (number) {
                                1 -> {
                                    val reservedRooms = reservationHistory.filter { it.roomNumber == selectedRoom.roomNumber
                                            && it.checkIn != selectedRoom.checkIn }
                                    val date = validateDate(reservedRooms).toList()
                                    selectedRoom.changeReservation(date[0], date[1])
                                    println("예약이 변경되었습니다.")
                                }

                                2 -> {

                                }

                                else -> {
                                    continue
                                }
                            }
                        } catch (e: Exception) {
                            println("잘못된 입력입니다.")
                        }
                    }
                }
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

fun validateDate(reservedRooms: List<Reservation>): Pair<LocalDate, LocalDate> {
    val formatter = DateTimeFormatter.BASIC_ISO_DATE
    val today: LocalDate = LocalDate.now()

    var isAvailable: Boolean
    var checkIn: LocalDate
    while (true) {
        isAvailable = true
        println("체크인 날짜를 입력해주세요. 표기형식:${today.format(formatter)}")
        try {
            var originCheckIn = readln()
            val tempCheckIn: LocalDate = LocalDate.parse(originCheckIn, formatter)
            if (tempCheckIn.isBefore(today)) {
                println("지난 날짜에 체크인할 수 없습니다.")
                continue
            }
            for (room in reservedRooms) {
                if (tempCheckIn.isBefore(room.checkIn) || !tempCheckIn.isBefore(room.checkOut)) {
                    continue
                } else {
                    println("해당 날짜에 이미 방을 사용 중입니다. 다른 날짜를 입력해주세요.")
                    isAvailable = false
                    break
                }
            }
            if (isAvailable) {
                checkIn = tempCheckIn
                break
            }
        } catch (e: Exception) {
            println("잘못된 입력입니다.")
        }
    }

    var checkOut: LocalDate
    while (true) {
        isAvailable = true
        println("체크아웃 날짜를 입력해주세요. 표기형식:20231208")
        try {
            var originCheckOut = readln()
            val tempCheckOut = LocalDate.parse(originCheckOut, formatter)
            if (!tempCheckOut.isAfter(checkIn)) {
                println("체크인 이후의 날짜에 체크아웃할 수 있습니다.")
                continue
            }
            for (room in reservedRooms) {
                if (checkIn.isBefore(room.checkIn) && tempCheckOut.isAfter(room.checkIn)) {
                    println("해당 날짜에 이미 방을 사용 중입니다. 다른 날짜를 입력해주세요.")
                    isAvailable = false
                    break
                }
            }
            if (isAvailable) {
                checkOut = tempCheckOut
                return Pair(checkIn, checkOut)
            }
        } catch (e: Exception) {
            println("잘못된 입력입니다.")
        }
    }
}
package com.hotel

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun main() {
    val memberList = ArrayList<Member>()
    val reservationHistory = ArrayList<Reservation>()

    while (true) {
        println("호텔 예약 프로그램입니다. 원하는 메뉴의 숫자를 입력하세요.")
        when (validateInput("menu").toString().toInt()) {
            1 -> {
                val balance = 1000000L
                val expense = 300000L

                val name = validateInput("name").toString()
                val roomNumber = validateInput("roomNumber").toString().toInt()
                val reservedRooms = reservationHistory.filter { it.roomNumber == roomNumber }
                val date = validateDate(reservedRooms).toList()

                var member = memberList.find { it.name == name }
                if (member == null) {
                    member = Member(name)
                    member.account.deposit(balance)
                    memberList.add(member)
                }

                val reservation = Reservation(member, roomNumber, date[0], date[1], expense)
                if (!member.account.withdraw(expense)) {
                    println("잔액이 부족합니다.")
                    continue
                }
                reservationHistory.add(reservation)
                println("호텔 예약이 완료되었습니다.")
            }

            2 -> {
                println("호텔 예약 목록입니다.")
                for (i in 0 until reservationHistory.size) {
                    println("${i + 1}. ${reservationHistory[i]}")
                }
            }

            3 -> {
                println("호텔 예약 목록입니다. (체크인 날짜순 정렬)")
                val sortedHistory = reservationHistory.sortedBy { it.checkIn }
                for (i in sortedHistory.indices) {
                    println("${i + 1}. ${sortedHistory[i]}")
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
                    val memberReservation = reservationHistory.filter { it.member.name == name }
                    if (memberReservation.isEmpty()) {
                        println("사용자 이름으로 예약된 목록을 찾을 수 없습니다.")
                        continue
                    }
                    while (true) {
                        println("$name 님이 예약한 목록입니다. 변경/취소하실 예약번호를 입력해주세요. (종료는 q 입력)")
                        for (i in memberReservation.indices) {
                            println("${i + 1}. ${memberReservation[i]}")
                        }
                        try {
                            val originReservationNumber = readln()
                            if (originReservationNumber == "q") {
                                isCompleted = true
                                break
                            }
                            val reservationNumber = originReservationNumber.toInt()
                            if (reservationNumber > memberReservation.size || reservationNumber < 1) {
                                println("범위에 없는 예약번호입니다.")
                                continue
                            }
                            val selectedRoom = memberReservation[reservationNumber - 1]
                            println("해당 예약을 어떻게 하시겠어요? 1. 변경 2. 취소 / 이외 번호 입력 시 이전으로 돌아갑니다.")
                            val originNumber = readln()
                            when (originNumber.toInt()) {
                                1 -> {
                                    val reservedRooms = reservationHistory.filter {
                                        it.roomNumber == selectedRoom.roomNumber
                                                && it.checkIn != selectedRoom.checkIn
                                    }
                                    val date = validateDate(reservedRooms).toList()
                                    selectedRoom.changeReservation(date[0], date[1])
                                    println("예약이 변경되었습니다.")
                                }

                                2 -> {
                                    println("[취소 유의사항]")
                                    println("1. 체크인 14일 전까지 예약금의 100% 환불")
                                    println("2. 체크인 7일 전까지 예약금의 80% 환불")
                                    println("3. 체크인 5일 전까지 예약금의 50% 환불")
                                    println("4. 체크인 3일 전까지 예약금의 30% 환불")
                                    println("5. 이후 예약금 환불 불가")

                                    val member = memberList.find { it.name == name }
                                    val difference = ChronoUnit.DAYS.between(
                                        LocalDate.now(),
                                        selectedRoom.checkIn
                                    )
                                    val returnRatio: Double = if (difference < 3) {
                                        println("예약금 환불이 불가합니다.")
                                        continue
                                    } else if (difference < 5) {
                                        0.3
                                    } else if (difference < 7) {
                                        0.5
                                    } else if (difference < 14) {
                                        0.8
                                    } else {
                                        1.0
                                    }
                                    member!!.account.deposit((selectedRoom.expense * returnRatio).toLong())
                                    println("${(selectedRoom.expense * returnRatio).toLong()}원이 환불되었습니다.")
                                    reservationHistory.remove(selectedRoom)
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

fun validateInput(type: String): Any {
    return when (type) {
        "menu" -> {
            while (true) {
                println("[메뉴]")
                println("[1]방 예약 [2]예약 목록 [3]예약 목록 (체크인 날짜순 정렬) [4]프로그램 종료 [5]금액 입금-출금 내역 [6]예약 변경/취소")
                try {
                    val originMenu = readln()
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
                    val originName = readln()
                    if (originName.isNotBlank()) {
                        return originName
                    }
                } catch (e: Exception) {
                    println("잘못된 입력입니다.")
                }
            }
        }

        "roomNumber" -> {
            while (true) {
                println("예약할 방번호를 입력해주세요. 100~999 선택 가능")
                try {
                    val originRoomNumber = readln()
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
            val originCheckIn = readln()
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
        println("체크아웃 날짜를 입력해주세요. 표기형식:${today.format(formatter)}")
        try {
            val originCheckOut = readln()
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
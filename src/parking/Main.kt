package parking

import java.util.Scanner

class ParkingLot {
    val size: Int
    val spots: Array<Spot>
    var busySpots: Int = 0

    constructor() {
        this.size = 0
        this.spots = emptyArray()
    }

    constructor(size: Int) {
        this.size = size
        this.spots = Array(size) {Spot()}
    }
}

class Spot {
    var busy = false
    var number = "free"
    var color = "free"
}

fun create(spots: Int): ParkingLot {
    val lot = ParkingLot(spots)
    println("Created a parking lot with ${lot.size} spots.")
    return lot
}

fun printSpots(lot: ParkingLot) {
    for (i in 1..lot.size) {
        val spot = lot.spots[i-1]
        if (!spot.busy) continue
        println("$i ${spot.number} ${spot.color}")
    }
}

fun status(lot: ParkingLot) {
    if (lot.busySpots == 0) println("Parking lot is empty.") else printSpots(lot)
}

fun findFreeSpot(lot: ParkingLot): Int {
    var i = 0
    var busy = true
    while (busy && i < lot.spots.size) {
        busy = lot.spots[i++].busy
    }
    return if (!busy) i else {
        println("Sorry, parking lot is full.")
        -1
    }
}

fun parkOnFreeSpot(lot: ParkingLot, spotNumber: Int, number: String, color: String) {
    val spot = lot.spots[spotNumber-1]
    spot.busy = true
    spot.number = number
    spot.color = color
    lot.busySpots++
    println("$color car parked on the spot ${spotNumber}.")
}

fun park(lot: ParkingLot, number: String, color: String) {
    val spot = findFreeSpot(lot)
    if (spot != -1) parkOnFreeSpot(lot, spot, number, color)
}

fun leave(lot: ParkingLot, spot: Int) {
    val busy = lot.spots[spot - 1].busy
    if (busy) {
        lot.spots[spot - 1] = Spot()
        lot.busySpots--
        println("Spot $spot is free.")
    } else {
        println("There is no car in the spot $spot.")
    }
}

fun checkSpot(spot: Spot, mode: String, parameter: String): Boolean {
    return when (mode) {
        "color" -> spot.color.toUpperCase() == parameter.toUpperCase()
        "reg" -> spot.number == parameter
        else -> false
    }
}

fun find(lot: ParkingLot, mode: String, parameter: String): String {
    var result = ""
    for (i in 1..lot.size) {
        val spot = lot.spots[i-1]
        if (!spot.busy) continue
        if (checkSpot(spot, mode, parameter)) result += "${i-1} "
    }
    return result
}

fun printFindResult(lot: ParkingLot, spots: String, mode: String) {
    var result = ""
    for (i in spots.trim().split(" ")) {
        result +=
                when (mode) {
                    "reg" -> "${lot.spots[i.toInt()].number}, "
                    "spot" -> "${i.toInt() + 1}, "
                    else -> ""
                }
    }
    println(result.dropLast(2))
}

fun regByColor(lot: ParkingLot, color: String) {
    val spots = find(lot, "color", color)
    if (spots == "")
        println("No cars with color ${color.toUpperCase()} were found.")
    else
        printFindResult(lot, spots, "reg")
}

fun spotByColor(lot: ParkingLot, color: String) {
    val spots = find(lot, "color", color)
    if (spots == "")
        println("No cars with color ${color.toUpperCase()} were found.")
    else
        printFindResult(lot, spots, "spot")
}

fun spotByReg(lot: ParkingLot, reg: String) {
    val spots = find(lot, "reg", reg)
    if (spots == "")
        println("No cars with registration number $reg were found.")
    else
        printFindResult(lot, spots, "spot")
}

fun created(lot: ParkingLot): Boolean {
    return if (lot.size == 0) {
        println("Sorry, parking lot is not created.")
        false
    } else true
}

fun main() {
    val scanner = Scanner(System.`in`)
    var lot = ParkingLot()

    do {
        val command = scanner.next()
        when (command) {
            "create" -> lot = create(scanner.nextInt())
            "status" -> if (created(lot)) status(lot)
            "park" -> if (created(lot)) park(lot, scanner.next(), scanner.next())
            "leave" -> if (created(lot)) leave(lot, scanner.nextInt())
            "reg_by_color" -> if (created(lot)) regByColor(lot, scanner.next())
            "spot_by_color" -> if (created(lot)) spotByColor(lot, scanner.next())
            "spot_by_reg" -> if (created(lot)) spotByReg(lot, scanner.next())
        }
    } while (command != "exit")
}

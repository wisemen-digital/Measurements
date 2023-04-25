package be.appwise.measurements.units

open class Unit private constructor() {

    constructor(symbol: String) : this() {
        this.symbol = symbol
    }

    var symbol: String = ""
        private set

    override fun equals(other: Any?): Boolean {
        val oth = (other as? Unit) ?: return false
        if (this === other) return true

        return symbol == oth.symbol
    }

    override fun hashCode(): Int {
        return symbol.hashCode()
    }
}
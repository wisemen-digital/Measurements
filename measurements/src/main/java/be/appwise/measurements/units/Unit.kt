package be.appwise.measurements.units

open class Unit(private val symbol: String) {

    override fun equals(other: Any?): Boolean {
        val oth = (other as? Unit) ?: return false
        if (this === other) return true

        return symbol == oth.symbol
    }

    override fun hashCode(): Int {
        return symbol.hashCode()
    }
}
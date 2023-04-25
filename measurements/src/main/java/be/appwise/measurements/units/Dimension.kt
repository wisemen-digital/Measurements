package be.appwise.measurements.units

import android.icu.util.MeasureUnit
import be.appwise.measurements.converters.UnitConverter

open class Dimension(symbol: String, val converter: UnitConverter, val measureUnit: MeasureUnit? = null) : Unit(symbol) {

    companion object;

    open fun baseUnit(): Dimension {
        throw Exception("*** You must override baseUnit in your class to define its base unit.")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val oth = (other as? Dimension) ?: return false

        return super.equals(other) &&
                converter == oth.converter &&
                measureUnit == oth.measureUnit
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + converter.hashCode()
        result = 31 * result + measureUnit.hashCode()
        return result
    }
}

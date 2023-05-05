package be.appwise.measurements.units

import android.icu.util.MeasureUnit
import be.appwise.measurements.converters.UnitConverter
import be.appwise.measurements.converters.UnitConverterLinear

class UnitDuration(symbol: String, converter: UnitConverter, measureUnit: MeasureUnit? = null) : Dimension(symbol, converter, measureUnit) {

    private constructor(symbol: String, coefficient: Double) : this(symbol, UnitConverterLinear(coefficient))

    private constructor(symbol: String, coefficient: Double, measureUnit: MeasureUnit?) : this(symbol, UnitConverterLinear(coefficient), measureUnit)

    private object Symbol {
        const val seconds = "s"
        const val minutes = "m"
        const val hours = "h"
    }

    private object Coefficient {
        const val seconds = 1.0
        const val minutes = 60.0
        const val hours = 3600.0
    }

    companion object {
        val seconds: UnitDuration = UnitDuration(Symbol.seconds, Coefficient.seconds)
        val minutes: UnitDuration = UnitDuration(Symbol.minutes, Coefficient.minutes)
        val hours: UnitDuration = UnitDuration(Symbol.hours, Coefficient.hours)
    }

    override fun baseUnit() = seconds

    override fun equals(other: Any?): Boolean {
        val oth = other as? UnitDuration ?: return false

        if (this === oth) return true

        return super.equals(other)
    }
}

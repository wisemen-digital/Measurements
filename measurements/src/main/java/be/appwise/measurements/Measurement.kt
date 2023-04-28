package be.appwise.measurements

import android.icu.text.MeasureFormat
import android.icu.text.NumberFormat
import android.icu.util.Measure
import android.os.Build
import androidx.annotation.IntRange
import be.appwise.measurements.Measurement.Companion.equals
import be.appwise.measurements.units.Dimension
import be.appwise.measurements.units.Unit
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

/**
 * A [Measurement] is a model that holds a [value] in the form of a [Double] associated with a [Unit]
 *
 * The [Measurement] has a full range of operator support, e.g. +, -, *, /, and also comparison operators.
 */
class Measurement<in UnitType : Unit>(
    /**
     * The value component of the [Measurement]
     */
    var value: Double,
    unit: UnitType
) {

    companion object {

        /**
         * Converts the measurement to the specified result.
         * Use this function if you don't want to create a new object but rather change the current object.
         *
         * @param otherUnit: A unit of the same '[Dimension]'
         */
        fun <UnitType : Dimension> Measurement<UnitType>.convert(otherUnit: UnitType) {
            val newValue = converted(otherUnit)
            value = newValue.value
            unit = newValue.unit
        }
    }

    /**
     * The unit component of the [Measurement]
     */
    var unit: @UnsafeVariance UnitType = unit
        private set

    // <editor-fold desc="Additions">
    operator fun plus(other: Double): Measurement<UnitType> = Measurement(value + other, unit)
    operator fun plus(other: Float): Measurement<UnitType> = Measurement(value + other, unit)
    operator fun plus(other: Int): Measurement<UnitType> = Measurement(value + other, unit)
    operator fun plus(other: Long): Measurement<UnitType> = Measurement(value + other, unit)
    operator fun plus(other: Short): Measurement<UnitType> = Measurement(value + other, unit)
    // </editor-fold>

    // <editor-fold desc="Subtractions">
    operator fun minus(other: Double): Measurement<UnitType> = Measurement(value - other, unit)
    operator fun minus(other: Float): Measurement<UnitType> = Measurement(value - other, unit)
    operator fun minus(other: Int): Measurement<UnitType> = Measurement(value - other, unit)
    operator fun minus(other: Long): Measurement<UnitType> = Measurement(value - other, unit)
    operator fun minus(other: Short): Measurement<UnitType> = Measurement(value - other, unit)
    // </editor-fold>

    // <editor-fold desc="Divisions">
    operator fun div(other: Double): Measurement<UnitType> = Measurement(value / other, unit)
    operator fun div(other: Float): Measurement<UnitType> = Measurement(value / other, unit)
    operator fun div(other: Int): Measurement<UnitType> = Measurement(value / other, unit)
    operator fun div(other: Long): Measurement<UnitType> = Measurement(value / other, unit)
    operator fun div(other: Short): Measurement<UnitType> = Measurement(value / other, unit)
    // </editor-fold>

    // <editor-fold desc="Multiplications">
    operator fun times(other: Double): Measurement<UnitType> = Measurement(value * other, unit)
    operator fun times(other: Float): Measurement<UnitType> = Measurement(value * other, unit)
    operator fun times(other: Int): Measurement<UnitType> = Measurement(value * other, unit)
    operator fun times(other: Long): Measurement<UnitType> = Measurement(value * other, unit)
    operator fun times(other: Short): Measurement<UnitType> = Measurement(value * other, unit)
    // </editor-fold>

    val description get() = "$value ${unit.symbol}"

    override fun toString(): String {
        return "\"measurement\": { \"value\": \"$value\", \"symbol\": \"${unit.symbol}\" }"
    }

    /**
     * Compare two [Measurement]s of the same [Dimension].
     * If both [unit]s are the same, just check if the [value]s are equal.
     * Otherwise convert the [value]s to the [unit]s baseUnitValue and then compare the two values.
     *
     * @return 'true' if the measurements are equal.
     */
    //TODO: not sure what I want with the hashCode.
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val oth = (other as? Measurement<*>) ?: return false

        if (this.unit == oth.unit) {
            return this.value == oth.value
        } else {
            val lhsUnit = this.unit as? Dimension
            val rhsUnit = oth.unit as? Dimension

            if (lhsUnit?.baseUnit() == rhsUnit?.baseUnit()) {
                val lhsValue = lhsUnit?.converter?.baseUnitValue(this.value)
                val rhsValue = rhsUnit?.converter?.baseUnitValue(oth.value)

                return lhsValue == rhsValue
            }
            return false
        }
    }
}

/**
 * Returns the formatted measurement "[Measurement.value] [Measurement.unit.symbol]" -> e.g. "2.15 kg".
 * Depending on the Android API version we will try to use the [MeasureFormat] if available.
 * If the Android API is not high enough (version code N) or the specific unit is not available we will fall back to our own implementation.
 *
 * @param maximumFractionDigits The amount if digits after the decimal point
 * @param locale The locale to use when Android API version is high enough (version code N) and the [MeasureFormat] can/will be used.
 * @param measureFormat The specific formatter to be used when the [MeasureFormat] api is supported
 * @return jklm
 */
fun <UnitType : Dimension> Measurement<UnitType>.format(
    @IntRange(0, Int.MAX_VALUE.toLong()) maximumFractionDigits: Int = 2,
    locale: Locale? = null,
    measureFormat: MeasureFormat? = defaultFormat(maximumFractionDigits, locale ?: Locale.getDefault())
): String {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && unit.measureUnit != null && measureFormat != null) {
        return measureFormat.format(Measure(value, unit.measureUnit))
    }

    val formatter = DecimalFormat("#." + (0 until maximumFractionDigits).joinToString("") { "#" })
    formatter.roundingMode = RoundingMode.HALF_EVEN
    val formattedValue = formatter.format(value)

    return "$formattedValue ${unit.symbol}"
}

private fun defaultFormat(@IntRange(0, Int.MAX_VALUE.toLong()) maximumFractionDigits: Int, locale: Locale): MeasureFormat? {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
        return null
    }

    val nFormat = NumberFormat.getNumberInstance(locale).also {
        it.maximumFractionDigits = maximumFractionDigits
    }
    return MeasureFormat.getInstance(locale, MeasureFormat.FormatWidth.SHORT, nFormat)
}

operator fun <UnitType : Dimension> Measurement<UnitType>.compareTo(other: Measurement<UnitType>): Int {
    if (this.unit == other.unit) {
        return this.value.compareTo(other.value)
    } else {
        val error = Exception("Attempt to compare measurements with non-equal dimensions")

        val lhsUnit = this.unit as? Dimension ?: throw error
        val rhsUnit = other.unit as? Dimension ?: throw error

        if (lhsUnit.baseUnit() == rhsUnit.baseUnit()) {
            val lhsValue = lhsUnit.converter.baseUnitValue(this.value)
            val rhsValue = rhsUnit.converter.baseUnitValue(other.value)

            return lhsValue.compareTo(rhsValue)
        }

        throw error
    }
}

/**
 * Returns a new measurement created by converting to the specified unit.
 *
 * @param otherUnit: A unit of the same '[Dimension]'
 * @return A converted measurement
 */
fun <UnitType : Dimension> Measurement<UnitType>.converted(otherUnit: UnitType): Measurement<UnitType> {
    return if (unit == otherUnit) {
        Measurement(value, otherUnit)
    } else {
        val valueInTermsOfBase = unit.converter.baseUnitValue(value)
        if (otherUnit == otherUnit.baseUnit()) {
            Measurement(valueInTermsOfBase, otherUnit)
        } else {
            val otherValueFromTermsOfBase = otherUnit.converter.value(valueInTermsOfBase)
            Measurement(otherValueFromTermsOfBase, otherUnit)
        }
    }
}

/**
 * Add two measurements of the same Dimension.
 *
 * If the [Measurement.unit] of this object and [other] are [equals], then this returns the result of adding the [Measurement.value] of each [Measurement].
 * If they are not equal, then this will convert both to the base unit of the [Dimension] and return the result as a [Measurement] of that base unit.
 * @return The result of adding the two measurements.
 * @exception Throws an exception when 2 different unit types are being added with each other (e.g. [be.appwise.measurements.units.UnitMass] and [be.appwise.measurements.units.UnitLength].
 */
operator fun <UnitType : Dimension> Measurement<UnitType>.plus(other: Measurement<UnitType>): Measurement<UnitType> {
    return if (other.unit.javaClass == this.unit.javaClass) {
        if (other.unit == unit) {
            Measurement(value + other.value, unit)
        } else {
            val lhsValueInTermsOfBase = unit.converter.baseUnitValue(value)
            val otherValueInTermsOfBase = other.unit.converter.baseUnitValue(other.value)
            Measurement(lhsValueInTermsOfBase + otherValueInTermsOfBase, unit.baseUnit())
        }
    } else {
        throw Exception("Attempt to add measurements with non-equal units: ${unit.symbol} and ${other.unit.symbol}")
    }
}

/**
 *
 *  Subtract two measurements of the same Dimension.
 *
 *  If the [Measurement.unit] of this object and [other] are [equals], then this returns the result of subtracting the [Measurement.value] of each [Measurement].
 *  If they are not equal, then this will convert both to the base unit of the [Dimension] and return the result as a [Measurement] of that base unit.
 *  @return The result of adding the two measurements.
 * @exception Throws an exception when 2 different unit types are being subtracted from each other (e.g. [be.appwise.measurements.units.UnitMass] and [be.appwise.measurements.units.UnitLength].
 */
operator fun <UnitType : Dimension> Measurement<UnitType>.minus(other: Measurement<UnitType>): Measurement<UnitType> {
    return if (other.unit.javaClass == this.unit.javaClass) {
        if (other.unit == unit) {
            Measurement(value - other.value, unit)
        } else {
            val lhsValueInTermsOfBase = unit.converter.baseUnitValue(value)
            val otherValueInTermsOfBase = other.unit.converter.baseUnitValue(other.value)
            Measurement(lhsValueInTermsOfBase - otherValueInTermsOfBase, unit.baseUnit())
        }
    } else {
        throw Exception("Attempt to add measurements with non-equal units: ${unit.symbol} and ${other.unit.symbol}")
    }
}

/**
 * Divide two measurements of the same Dimension.
 *
 * If the [Measurement.unit] of this object and [other] are [equals], then this returns the result of dividing the [Measurement.value] of each [Measurement].
 * If they are not equal, then this will convert both to the base unit of the [Dimension] and return the result as a [Measurement] of that base unit.
 * @return The result of adding the two measurements.
 * @exception Throws an exception when 2 different unit types are being divided by each other (e.g. [be.appwise.measurements.units.UnitMass] and [be.appwise.measurements.units.UnitLength].
 */
operator fun <UnitType : Dimension> Measurement<UnitType>.div(other: Measurement<UnitType>): Measurement<UnitType> {
    return if (other.unit.javaClass == this.unit.javaClass) {
        if (other.unit == unit) {
            Measurement(value / other.value, unit)
        } else {
            val lhsValueInTermsOfBase = unit.converter.baseUnitValue(value)
            val otherValueInTermsOfBase = other.unit.converter.baseUnitValue(other.value)
            Measurement(lhsValueInTermsOfBase / otherValueInTermsOfBase, unit.baseUnit())
        }
    } else {
        throw Exception("Attempt to add measurements with non-equal units: ${unit.symbol} and ${other.unit.symbol}")
    }
}

/**
 * Multiply two measurements of the same Dimension.
 *
 * If the [Measurement.unit] of this object and [other] are [equals], then this returns the result of multiplying the [Measurement.value] of each [Measurement].
 * If they are not equal, then this will convert both to the base unit of the [Dimension] and return the result as a [Measurement] of that base unit.
 * @return The result of adding the two measurements.
 * @exception Throws an exception when 2 different unit types are being multiplied by each other (e.g. [be.appwise.measurements.units.UnitMass] and [be.appwise.measurements.units.UnitLength].
 */
operator fun <UnitType : Dimension> Measurement<UnitType>.times(other: Measurement<UnitType>): Measurement<UnitType> {
    return if (other.unit.javaClass == this.unit.javaClass) {
        if (other.unit == unit) {
            Measurement(value * other.value, unit)
        } else {
            val lhsValueInTermsOfBase = unit.converter.baseUnitValue(value)
            val otherValueInTermsOfBase = other.unit.converter.baseUnitValue(other.value)
            Measurement(lhsValueInTermsOfBase * otherValueInTermsOfBase, unit.baseUnit())
        }
    } else {
        throw Exception("Attempt to add measurements with non-equal units: ${unit.symbol} and ${other.unit.symbol}")
    }
}


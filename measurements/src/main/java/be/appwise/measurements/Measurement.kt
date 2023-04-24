package be.appwise.measurements

import be.appwise.measurements.units.Dimension
import be.appwise.measurements.units.UnitMass

class Measurement<in UnitType : Dimension>(var value: Double, unit: UnitType) {

    companion object

    var unit: @UnsafeVariance UnitType = unit
        private set

    fun convert(otherUnit: UnitType) {
        val newValue = converted(otherUnit)
        value = newValue.value
        unit = newValue.unit
    }

    fun converted(otherUnit: UnitType): Measurement<UnitType> {

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

    val description get() = "$value ${unit.symbol}"

    operator fun <UnitType : Dimension> plus(other: Measurement<UnitType>): Measurement<UnitType> {
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
}





package be.appwise.measurements.units

import be.appwise.measurements.converters.UnitConverterLinear
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

internal class UnitVolumeTest {

    @Test
    fun testEquality() {
        val s1 = "a"
        val s2 = "ab"

        val uc1 = UnitConverterLinear(coefficient = 1.0, constant = 2.0)
        val uc2 = UnitConverterLinear(coefficient = 1.0, constant = 3.0)

        val u0 = Unit(symbol = s1)
        val d1 = Dimension(symbol = s1, converter = uc1)

        val u1 = UnitVolume(symbol = s1, converter = uc1)
        val u2 = UnitVolume(symbol = s1, converter = uc1)
        val u3 = UnitVolume(symbol = s2, converter = uc1)
        val u4 = UnitVolume(symbol = s1, converter = uc2)

        assertEquals(u1, u2)
        assertEquals(u2, u1)
        assertEquals(u1.hashCode(), u2.hashCode())
        assertEquals(u2.hashCode(), u1.hashCode())
        assertNotEquals(u1, u3)
        assertNotEquals(u3, u1)
        assertNotEquals(u1, u4)
        assertNotEquals(u4, u1)

        assertEquals(u0, u1)
        assertNotEquals(u1, u0)

        assertEquals(d1, u1)
        assertNotEquals(u1, d1)
    }

    @Test
    fun testBaseUnit() {
        assertEquals(UnitVolume.cubicDecimeters.baseUnit().symbol, UnitVolume.liters.symbol)
    }

    @Test
    fun testConversion() {
        val delta = 1e-9
        val testIdentity: (Dimension) -> Double = {
            val converter = it.converter
            converter.value(converter.baseUnitValue(1.0))
        }

        assertEquals(testIdentity(UnitVolume.megaliters), 1.0, delta)
        assertEquals(testIdentity(UnitVolume.kiloliters), 1.0, delta)
        assertEquals(testIdentity(UnitVolume.liters), 1.0, delta)
        assertEquals(testIdentity(UnitVolume.deciliters), 1.0, delta)
        assertEquals(testIdentity(UnitVolume.centiliters), 1.0, delta)
        assertEquals(testIdentity(UnitVolume.milliliters), 1.0, delta)
        assertEquals(testIdentity(UnitVolume.cubicKilometers), 1.0, delta)
        assertEquals(testIdentity(UnitVolume.cubicMeters), 1.0, delta)
        assertEquals(testIdentity(UnitVolume.cubicDecimeters), 1.0, delta)
        assertEquals(testIdentity(UnitVolume.cubicCentimeters), 1.0, delta)
        assertEquals(testIdentity(UnitVolume.cubicMillimeters), 1.0, delta)
        assertEquals(testIdentity(UnitVolume.cubicInches), 1.0, delta)
        assertEquals(testIdentity(UnitVolume.cubicFeet), 1.0, delta)
        assertEquals(testIdentity(UnitVolume.cubicYards), 1.0, delta)
        assertEquals(testIdentity(UnitVolume.cubicMiles), 1.0, delta)
        assertEquals(testIdentity(UnitVolume.acreFeet), 1.0, delta)
        assertEquals(testIdentity(UnitVolume.bushels), 1.0, delta)
        assertEquals(testIdentity(UnitVolume.teaspoons), 1.0, delta)
        assertEquals(testIdentity(UnitVolume.tablespoons), 1.0, delta)
        assertEquals(testIdentity(UnitVolume.fluidOunces), 1.0, delta)
        assertEquals(testIdentity(UnitVolume.cups), 1.0, delta)
        assertEquals(testIdentity(UnitVolume.pints), 1.0, delta)
        assertEquals(testIdentity(UnitVolume.quarts), 1.0, delta)
        assertEquals(testIdentity(UnitVolume.gallons), 1.0, delta)
        assertEquals(testIdentity(UnitVolume.imperialTeaspoons), 1.0, delta)
        assertEquals(testIdentity(UnitVolume.imperialTablespoons), 1.0, delta)
        assertEquals(testIdentity(UnitVolume.imperialFluidOunces), 1.0, delta)
        assertEquals(testIdentity(UnitVolume.imperialPints), 1.0, delta)
        assertEquals(testIdentity(UnitVolume.imperialQuarts), 1.0, delta)
        assertEquals(testIdentity(UnitVolume.imperialGallons), 1.0, delta)
        assertEquals(testIdentity(UnitVolume.metricCups), 1.0, delta)
    }
}
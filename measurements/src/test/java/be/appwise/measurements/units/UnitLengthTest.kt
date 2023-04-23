package be.appwise.measurements.units

import be.appwise.measurements.converters.UnitConverterLinear
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

internal class UnitLengthTest {

    @Test
    fun testEquality() {
        val s1 = "a"
        val s2 = "ab"

        val uc1 = UnitConverterLinear(coefficient = 1.0, constant = 2.0)
        val uc2 = UnitConverterLinear(coefficient = 1.0, constant = 3.0)

        val u0 = Unit(symbol = s1)
        val d1 = Dimension(symbol = s1, converter = uc1)

        val u1 = UnitLength(symbol = s1, converter = uc1)
        val u2 = UnitLength(symbol = s1, converter = uc1)
        val u3 = UnitLength(symbol = s2, converter = uc1)
        val u4 = UnitLength(symbol = s1, converter = uc2)

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
        assertEquals(UnitLength.astronomicalUnits.baseUnit().symbol, UnitLength.meters.symbol)
    }

    @Test
    fun testConversion() {
        val delta = 1e-9
        val testIdentity: (Dimension) -> Double = {
            val converter = it.converter
            converter.value(converter.baseUnitValue(1.0))
        }

        assertEquals(testIdentity(UnitLength.megameters), 1.0, delta)
        assertEquals(testIdentity(UnitLength.kilometers), 1.0, delta)
        assertEquals(testIdentity(UnitLength.hectometers), 1.0, delta)
        assertEquals(testIdentity(UnitLength.decameters), 1.0, delta)
        assertEquals(testIdentity(UnitLength.meters), 1.0, delta)
        assertEquals(testIdentity(UnitLength.decimeters), 1.0, delta)
        assertEquals(testIdentity(UnitLength.centimeters), 1.0, delta)
        assertEquals(testIdentity(UnitLength.millimeters), 1.0, delta)
        assertEquals(testIdentity(UnitLength.micrometers), 1.0, delta)
        assertEquals(testIdentity(UnitLength.nanometers), 1.0, delta)
        assertEquals(testIdentity(UnitLength.picometers), 1.0, delta)
        assertEquals(testIdentity(UnitLength.inches), 1.0, delta)
        assertEquals(testIdentity(UnitLength.feet), 1.0, delta)
        assertEquals(testIdentity(UnitLength.yards), 1.0, delta)
        assertEquals(testIdentity(UnitLength.miles), 1.0, delta)
        assertEquals(testIdentity(UnitLength.scandinavianMiles), 1.0, delta)
        assertEquals(testIdentity(UnitLength.lightyears), 1.0, delta)
        assertEquals(testIdentity(UnitLength.nauticalMiles), 1.0, delta)
        assertEquals(testIdentity(UnitLength.fathoms), 1.0, delta)
        assertEquals(testIdentity(UnitLength.furlongs), 1.0, delta)
        assertEquals(testIdentity(UnitLength.astronomicalUnits), 1.0, delta)
        assertEquals(testIdentity(UnitLength.parsecs), 1.0, delta)
    }
}
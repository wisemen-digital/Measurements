package be.appwise.measurements.units

import be.appwise.measurements.converters.UnitConverterLinear
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

internal class UnitAreaTest {

    @Test
    fun testEquality() {
        val s1 = "a"
        val s2 = "ab"

        val uc1 = UnitConverterLinear(coefficient = 1.0, constant = 2.0)
        val uc2 = UnitConverterLinear(coefficient = 1.0, constant = 3.0)

        val u0 = Unit(symbol = s1)
        val d1 = Dimension(symbol = s1, converter = uc1)

        val u1 = UnitArea(symbol = s1, converter = uc1)
        val u2 = UnitArea(symbol = s1, converter = uc1)
        val u3 = UnitArea(symbol = s2, converter = uc1)
        val u4 = UnitArea(symbol = s1, converter = uc2)

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
        assertEquals(UnitArea.acres.baseUnit().symbol, UnitArea.squareMeters.symbol)
    }

    @Test
    fun testConversion() {
        val delta = 1e-9
        val testIdentity: (Dimension) -> Double = {
            val converter = it.converter
            converter.value(converter.baseUnitValue(1.0))
        }

        assertEquals(testIdentity(UnitArea.squareMegameters), 1.0, delta)
        assertEquals(testIdentity(UnitArea.squareKilometers), 1.0, delta)
        assertEquals(testIdentity(UnitArea.squareMeters), 1.0, delta)
        assertEquals(testIdentity(UnitArea.squareCentimeters), 1.0, delta)
        assertEquals(testIdentity(UnitArea.squareMillimeters), 1.0, delta)
        assertEquals(testIdentity(UnitArea.squareMicrometers), 1.0, delta)
        assertEquals(testIdentity(UnitArea.squareNanometers), 1.0, delta)
        assertEquals(testIdentity(UnitArea.squareInches), 1.0, delta)
        assertEquals(testIdentity(UnitArea.squareFeet), 1.0, delta)
        assertEquals(testIdentity(UnitArea.squareYards), 1.0, delta)
        assertEquals(testIdentity(UnitArea.squareMiles), 1.0, delta)
        assertEquals(testIdentity(UnitArea.acres), 1.0, delta)
        assertEquals(testIdentity(UnitArea.ares), 1.0, delta)
        assertEquals(testIdentity(UnitArea.hectares), 1.0, delta)
    }
}
package be.appwise.measurements.units

import be.appwise.measurements.converters.UnitConverterLinear
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

internal class UnitPressureTest {

    @Test
    fun testEquality() {
        val s1 = "a"
        val s2 = "ab"

        val uc1 = UnitConverterLinear(coefficient = 1.0, constant = 2.0)
        val uc2 = UnitConverterLinear(coefficient = 1.0, constant = 3.0)

        val u0 = Unit(symbol = s1)
        val d1 = Dimension(symbol = s1, converter = uc1)

        val u1 = UnitPressure(symbol = s1, converter = uc1)
        val u2 = UnitPressure(symbol = s1, converter = uc1)
        val u3 = UnitPressure(symbol = s2, converter = uc1)
        val u4 = UnitPressure(symbol = s1, converter = uc2)

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
        assertEquals(UnitPressure.inchesOfMercury.baseUnit().symbol, UnitPressure.pascal.symbol)
    }

    @Test
    fun testConversion() {
        val delta = 1e-9
        val testIdentity: (Dimension) -> Double = {
            val converter = it.converter
            converter.value(converter.baseUnitValue(1.0))
        }

        assertEquals(testIdentity(UnitPressure.newtonsPerMetersSquared), 1.0, delta)
        assertEquals(testIdentity(UnitPressure.pascal), 1.0, delta)
        assertEquals(testIdentity(UnitPressure.gigapascals), 1.0, delta)
        assertEquals(testIdentity(UnitPressure.megapascals), 1.0, delta)
        assertEquals(testIdentity(UnitPressure.kilopascals), 1.0, delta)
        assertEquals(testIdentity(UnitPressure.hectopascals), 1.0, delta)
        assertEquals(testIdentity(UnitPressure.inchesOfMercury), 1.0, delta)
        assertEquals(testIdentity(UnitPressure.bars), 1.0, delta)
        assertEquals(testIdentity(UnitPressure.millibars), 1.0, delta)
        assertEquals(testIdentity(UnitPressure.millimetersOfMercury), 1.0, delta)
        assertEquals(testIdentity(UnitPressure.poundsForcePerSquareInch), 1.0, delta)
    }
}
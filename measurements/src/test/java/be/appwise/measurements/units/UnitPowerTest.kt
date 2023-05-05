package be.appwise.measurements.units

import be.appwise.measurements.converters.UnitConverterLinear
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

internal class UnitPowerTest {

    @Test
    fun testEquality() {
        val s1 = "a"
        val s2 = "ab"

        val uc1 = UnitConverterLinear(coefficient = 1.0, constant = 2.0)
        val uc2 = UnitConverterLinear(coefficient = 1.0, constant = 3.0)

        val u0 = Unit(symbol = s1)
        val d1 = Dimension(symbol = s1, converter = uc1)

        val u1 = UnitPower(symbol = s1, converter = uc1)
        val u2 = UnitPower(symbol = s1, converter = uc1)
        val u3 = UnitPower(symbol = s2, converter = uc1)
        val u4 = UnitPower(symbol = s1, converter = uc2)

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
        assertEquals(UnitPower.microwatts.baseUnit().symbol, UnitPower.watts.symbol)
    }

    @Test
    fun testConversion() {
        val delta = 1e-9
        val testIdentity: (Dimension) -> Double = {
            val converter = it.converter
            converter.value(converter.baseUnitValue(1.0))
        }

        assertEquals(testIdentity(UnitPower.terawatts), 1.0, delta)
        assertEquals(testIdentity(UnitPower.gigawatts), 1.0, delta)
        assertEquals(testIdentity(UnitPower.megawatts), 1.0, delta)
        assertEquals(testIdentity(UnitPower.kilowatts), 1.0, delta)
        assertEquals(testIdentity(UnitPower.watts), 1.0, delta)
        assertEquals(testIdentity(UnitPower.milliwatts), 1.0, delta)
        assertEquals(testIdentity(UnitPower.microwatts), 1.0, delta)
        assertEquals(testIdentity(UnitPower.nanowatts), 1.0, delta)
        assertEquals(testIdentity(UnitPower.picowatts), 1.0, delta)
        assertEquals(testIdentity(UnitPower.femtowatts), 1.0, delta)
        assertEquals(testIdentity(UnitPower.horsepower), 1.0, delta)
    }
}
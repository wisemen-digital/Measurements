package be.appwise.measurements.units

import be.appwise.measurements.converters.UnitConverterLinear
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

internal class DimensionTest {

    @Test
    fun testEquality() {
        val s1 = "a"
        val s2 = "ab"

        val u1 = Unit(symbol = s1)

        val uc1 = UnitConverterLinear(coefficient = 1.0, constant = 2.0)
        val uc2 = UnitConverterLinear(coefficient = 1.0, constant = 3.0)

        val d1 = Dimension(symbol = s1, converter = uc1)
        val d2 = Dimension(symbol = s1, converter = uc1)
        val d3 = Dimension(symbol = s2, converter = uc1)
        val d4 = Dimension(symbol = s1, converter = uc2)

        assertEquals(d1, d2)
        assertEquals(d2, d1)
        assertEquals(d1.hashCode(), d2.hashCode())
        assertEquals(d2.hashCode(), d1.hashCode())
        assertNotEquals(d1, d3)
        assertNotEquals(d3, d1)
        assertNotEquals(d1, d4)
        assertNotEquals(d4, d1)
        assertNotEquals(d1.hashCode(), d3.hashCode())
        assertNotEquals(d3.hashCode(), d1.hashCode())
        assertNotEquals(d1.hashCode(), d4.hashCode())
        assertNotEquals(d4.hashCode(), d1.hashCode())

        assertEquals(u1, d1)
        assertNotEquals(d1, u1)
        assertNotEquals(u1.hashCode(), d1.hashCode())
        assertNotEquals(d1.hashCode(), u1.hashCode())

        assertThrows(Exception::class.java) {
            d1.baseUnit()
        }
    }
}
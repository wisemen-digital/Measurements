package be.appwise.measurements.converters

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class UnitConverterReciprocalTest {

    @Test
    fun testLinearity() {
        val reciprocal = 282.481
        val baseUnitConverter = UnitConverterReciprocal(reciprocal)
        assertEquals(baseUnitConverter.value(reciprocal), 1.0, 1e-9)
        assertEquals(baseUnitConverter.baseUnitValue(1.0), reciprocal, 1e-9)
    }

    @Test
    fun testEquality() {
        val u1 = UnitConverterReciprocal(reciprocal = 1.0)
        val u2 = UnitConverterReciprocal(reciprocal = 1.0)
        assertEquals(u1, u2)
        assertEquals(u2, u1)
        assertEquals(u1.hashCode(), u2.hashCode())
        assertEquals(u2.hashCode(), u1.hashCode())

        val u3 = UnitConverterReciprocal(reciprocal = 2.0)
        assertNotEquals(u1, u3)
        assertNotEquals(u3, u1)
        assertNotEquals(u1.hashCode(), u3.hashCode())
        assertNotEquals(u3.hashCode(), u1.hashCode())
    }
}
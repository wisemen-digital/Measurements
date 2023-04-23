package be.appwise.measurements.units

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

internal class UnitTest {

    @Test
    fun testEquality() {
        val s1 = "a"
        val s2 = "ab"

        val u1 = Unit(symbol = s1)
        val u2 = Unit(symbol = s1)
        val u3 = Unit(symbol = s2)

        assertEquals(u1, u2)
        assertEquals(u2, u1)
        assertEquals(u2.hashCode(), u1.hashCode())
        assertEquals(u2.hashCode(), u1.hashCode())
        assertNotEquals(u1, u3)
        assertNotEquals(u3, u1)
        assertNotEquals(u1.hashCode(), u3.hashCode())
        assertNotEquals(u3.hashCode(), u1.hashCode())
    }
}
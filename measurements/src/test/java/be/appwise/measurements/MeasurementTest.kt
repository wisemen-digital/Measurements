package be.appwise.measurements

import be.appwise.measurements.Measurement.Companion.convert
import be.appwise.measurements.units.UnitLength
import be.appwise.measurements.units.UnitMass
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class MeasurementTest {

    @Test
    fun testDescription() {
        val m1 = Measurement(1.0, UnitMass.grams)
        val m2 = Measurement(1.0, UnitLength.kilometers)

        assertEquals("1.0 g", m1.description)
        assertEquals("1.0 km", m2.description)
    }

    @Test
    fun testConversion() {
        val m1 = Measurement(1.0, UnitMass.grams)

        assertEquals(1.0, m1.converted(UnitMass.grams).value)
        assertEquals(0.001, m1.converted(UnitMass.kilograms).value)
        assertEquals(1.0, m1.converted(UnitMass.kilograms).converted(UnitMass.grams).value)

        val m2 = Measurement(1.0, UnitLength.meters)

        assertEquals(1.0, m2.converted(UnitLength.meters).value)
        assertEquals(0.001, m2.converted(UnitLength.kilometers).value)
        assertEquals(1.0, m2.converted(UnitLength.kilometers).converted(UnitLength.meters).value)
    }

    @Test
    fun testConversionSelf() {
        val m1 = Measurement(1.0, UnitMass.grams)

        m1.convert(UnitMass.kilograms)

        assertEquals(0.001, m1.value)
    }

    @Test
    fun testAddition() {
        val m1 = Measurement(1.0, UnitMass.grams)
        val m2 = Measurement(2.0, UnitMass.grams)
        val m3 = Measurement(3.0, UnitMass.centigrams)
        val m4 = Measurement(3.0, UnitLength.kilometers)

        assertEquals(3.0, (m1 + m2).value)
        assertEquals(0.00103, (m1 + m3).value)

        assertThrows(Exception::class.java) {
            m1 + m4
        }
    }

    @Test
    fun testSubtraction() {
        val m1 = Measurement(1.0, UnitMass.grams)
        val m2 = Measurement(2.0, UnitMass.grams)
        val m3 = Measurement(30.0, UnitMass.decigrams)
        val m4 = Measurement(3.0, UnitLength.kilometers)

        assertEquals(-1.0, (m1 - m2).value)
        assertEquals(-0.002, (m1 - m3).value)

        assertThrows(Exception::class.java) {
            m1 - m4
        }
    }

    @Test
    fun testDivision() {
        val m1 = Measurement(1.0, UnitMass.grams)
        val m2 = Measurement(2.0, UnitMass.grams)
        val m3 = Measurement(40.0, UnitMass.decigrams)
        val m4 = Measurement(3.0, UnitLength.kilometers)

        assertEquals(0.5, (m1 / m2).value)
        assertEquals(0.25, (m1 / m3).value)

        assertThrows(Exception::class.java) {
            m1 / m4
        }
    }

    @Test
    fun testMultiplication() {
        val m1 = Measurement(1.0, UnitMass.grams)
        val m2 = Measurement(2.0, UnitMass.grams)
        val m3 = Measurement(40.0, UnitMass.decigrams)
        val m4 = Measurement(3.0, UnitLength.kilometers)

        assertEquals(2.0, (m1 * m2).value)
        assertEquals(0.000004, (m1 * m3).value)

        assertThrows(Exception::class.java) {
            m1 * m4
        }
    }
}
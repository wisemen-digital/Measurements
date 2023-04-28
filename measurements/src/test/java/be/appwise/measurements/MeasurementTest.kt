package be.appwise.measurements

import android.icu.text.MeasureFormat
import android.icu.text.NumberFormat
import android.os.Build
import be.appwise.measurements.Measurement.Companion.convert
import be.appwise.measurements.converters.UnitConverterLinear
import be.appwise.measurements.units.Dimension
import be.appwise.measurements.units.Unit
import be.appwise.measurements.units.UnitLength
import be.appwise.measurements.units.UnitMass
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.util.*

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
    fun testToString() {
        val s1 = "a"

        val uc1 = UnitConverterLinear(coefficient = 1.0, constant = 2.0)

        val u0 = Unit(symbol = s1)
        val d1 = Dimension(symbol = s1, converter = uc1)

        val m1 = Measurement(2.01, UnitMass.kilograms)
        val m2 = Measurement(2.1352, d1)
        val m3 = Measurement(2.1352, u0)

        assertEquals(""""measurement": { "value": "2.01", "symbol": "kg" }""", m1.toString())
        assertEquals(""""measurement": { "value": "2.1352", "symbol": "a" }""", m2.toString())
        assertEquals(""""measurement": { "value": "2.1352", "symbol": "a" }""", m3.toString())
    }

    @Test
    fun testFormat() {
        val m1 = Measurement(2.1352, UnitMass.kilograms)
        assertEquals("2.14 kg", m1.format())
        assertEquals("2.135 kg", m1.format(3))
        assertEquals("2.1352 kg", m1.format(4))
    }

    @Test // TODO: invalid test
    fun testFormatHigherThanN() {

        mockkStatic(NumberFormat::class)

        // Create a partial mock object of NumberFormat
        val numberFormatPartialMock = mockk<NumberFormat>(relaxed = true, relaxUnitFun = true)
        // Define the behavior of maximumFractionDigits on the mock object
        every { numberFormatPartialMock.maximumFractionDigits } returns 2
        // Mock the behavior of getNumberInstance() on the mock object
        every { NumberFormat.getNumberInstance(Locale.getDefault()) } returns numberFormatPartialMock

        mockkStatic(MeasureFormat::class)
        val measureFormatMock = mockk<MeasureFormat>()
        every { MeasureFormat.getInstance(Locale.getDefault(), MeasureFormat.FormatWidth.SHORT, numberFormatPartialMock) } returns measureFormatMock

        //TODO: not working quite right, the `MeasureUnit.KILOGRAM` is always returning `null` because it's an `Android framework` function/class...
//        mockkStatic(MeasureUnit.KILOGRAM::class)
//        val measureUnit = mockk<MeasureUnit>(relaxed = true, relaxUnitFun = true)
//        every { UnitMass.kilograms.measureUnit } returns measureUnit

        setFinalStatic(Build.VERSION::class.java.getField("SDK_INT"), 33)

        val m3 = Measurement(2.1352, UnitMass.kilograms)
        assertEquals("2.135 kg", m3.format(3))

        setFinalStatic(Build.VERSION::class.java.getField("SDK_INT"), 0)

        unmockkStatic(NumberFormat::class)
        unmockkStatic(MeasureFormat::class)
    }

    @Test
    fun testEquality() {
        val m1 = Measurement(2.0, UnitMass.kilograms)
        val m2 = Measurement(2.0, UnitMass.kilograms)
        val m3 = Measurement(2000.0, UnitMass.grams)
        val m4 = Measurement(2.0, UnitLength.kilometers)

        assertTrue(m1 == m2)
        assertTrue(m1 == m3)
        assertTrue(m3 == m1)
        assertTrue(m2 == m3)
        assertTrue(m3 == m1)
        assertFalse(m1 == m4)
    }

    @Test
    fun testComparator() {
        val s1 = "a"
        val u0 = Unit(symbol = s1)

        val m1 = Measurement(2.0, UnitMass.kilograms)
        val m2 = Measurement(3.0, UnitMass.kilograms)
        val m3 = Measurement(2000.1, UnitMass.grams)
        val m4 = Measurement(2000.0, UnitMass.grams)
        val m5 = Measurement(2.0, UnitLength.kilometers)

        val m6 = Measurement(2.1352, u0)

        assertTrue(m1 < m2)
        assertTrue(m2 > m1)
        assertTrue(m1 < m3)
        assertTrue(m3 > m1)
        assertTrue(m1 <= m4)
        assertTrue(m4 <= m1)
        assertTrue(m1 >= m4)
        assertTrue(m4 >= m1)

        assertThrows(Exception::class.java) {
            m1 < m6
        }

        assertThrows(Exception::class.java) {
            m1 < m5
        }
    }

    // <editor-fold desc="Calculations">
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
    fun testAdditionDouble() {
        val m1 = Measurement(1.0, UnitMass.grams)

        assertEquals(2.9, (m1 + 1.9).value)
    }

    @Test
    fun testAdditionFloat() {
        val m1 = Measurement(1.0, UnitMass.grams)

        assertEquals(2.9f, (m1 + 1.9f).value.toFloat())
    }

    @Test
    fun testAdditionInt() {
        val m1 = Measurement(1.0, UnitMass.grams)

        assertEquals(2.0, (m1 + 1).value)
    }

    @Test
    fun testAdditionLong() {
        val m1 = Measurement(1.0, UnitMass.grams)

        assertEquals(2.0, (m1 + 1L).value)
    }

    @Test
    fun testAdditionShort() {
        val m1 = Measurement(1.0, UnitMass.grams)

        assertEquals(2.0, (m1 + 1.toShort()).value)
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
    fun testSubtractionDouble() {
        val m1 = Measurement(1.0, UnitMass.grams)

        assertEquals(-0.9, (m1 - 1.9).value, 1e-9)
    }

    @Test
    fun testSubtractionFloat() {
        val m1 = Measurement(1.0, UnitMass.grams)

        assertEquals(-0.9f, (m1 - 1.9f).value.toFloat())
    }

    @Test
    fun testSubtractionInt() {
        val m1 = Measurement(1.0, UnitMass.grams)

        assertEquals(0.0, (m1 - 1).value)
    }

    @Test
    fun testSubtractionLong() {
        val m1 = Measurement(1.0, UnitMass.grams)

        assertEquals(0.0, (m1 - 1L).value)
    }

    @Test
    fun testSubtractionShort() {
        val m1 = Measurement(1.0, UnitMass.grams)

        assertEquals(0.0, (m1 - 1.toShort()).value)
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
    fun testDivisionDouble() {
        val m1 = Measurement(1.0, UnitMass.grams)

        assertEquals(0.5, (m1 / 2.0).value)
    }

    @Test
    fun testDivisionFloat() {
        val m1 = Measurement(1.0, UnitMass.grams)

        assertEquals(0.5f, (m1 / 2.0f).value.toFloat())
    }

    @Test
    fun testDivisionInt() {
        val m1 = Measurement(1.0, UnitMass.grams)

        assertEquals(0.5, (m1 / 2).value)
    }

    @Test
    fun testDivisionLong() {
        val m1 = Measurement(1.0, UnitMass.grams)

        assertEquals(0.5, (m1 / 2L).value)
    }

    @Test
    fun testDivisionShort() {
        val m1 = Measurement(1.0, UnitMass.grams)

        assertEquals(0.5, (m1 / 2.toShort()).value)
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

    @Test
    fun testMultiplicationDouble() {
        val m1 = Measurement(1.0, UnitMass.grams)

        assertEquals(2.0, (m1 * 2.0).value)
    }

    @Test
    fun testMultiplicationFloat() {
        val m1 = Measurement(1.0, UnitMass.grams)

        assertEquals(2.0f, (m1 * 2.0f).value.toFloat())
    }

    @Test
    fun testMultiplicationInt() {
        val m1 = Measurement(1.0, UnitMass.grams)

        assertEquals(2.0, (m1 * 2).value)
    }

    @Test
    fun testMultiplicationLong() {
        val m1 = Measurement(1.0, UnitMass.grams)

        assertEquals(2.0, (m1 * 2L).value)
    }

    @Test
    fun testMultiplicationShort() {
        val m1 = Measurement(1.0, UnitMass.grams)

        assertEquals(2.0, (m1 * 2.toShort()).value)
    }
    // </editor-fold>
}

fun setFinalStatic(field: Field, newValue: Any) {
    field.isAccessible = true

    val modifiersField = Field::class.java.getDeclaredField("modifiers")
    modifiersField.isAccessible = true
    modifiersField.setInt(field, field.modifiers and Modifier.FINAL.inv())

    field.set(null, newValue)
}

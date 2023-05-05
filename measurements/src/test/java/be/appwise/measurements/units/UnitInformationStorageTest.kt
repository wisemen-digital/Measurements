package be.appwise.measurements.units

import be.appwise.measurements.converters.UnitConverterLinear
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

internal class UnitInformationStorageTest {

    @Test
    fun testEquality() {
        val s1 = "a"
        val s2 = "ab"

        val uc1 = UnitConverterLinear(coefficient = 1.0, constant = 2.0)
        val uc2 = UnitConverterLinear(coefficient = 1.0, constant = 3.0)

        val u0 = Unit(symbol = s1)
        val d1 = Dimension(symbol = s1, converter = uc1)

        val u1 = UnitInformationStorage(symbol = s1, converter = uc1)
        val u2 = UnitInformationStorage(symbol = s1, converter = uc1)
        val u3 = UnitInformationStorage(symbol = s2, converter = uc1)
        val u4 = UnitInformationStorage(symbol = s1, converter = uc2)

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
        assertEquals(UnitInformationStorage.gigabits.baseUnit().symbol, UnitInformationStorage.bits.symbol)
    }

    @Test
    fun testConversion() {
        val delta = 1e-9
        val testIdentity: (Dimension) -> Double = {
            val converter = it.converter
            converter.value(converter.baseUnitValue(1.0))
        }

        assertEquals(testIdentity(UnitInformationStorage.bytes), 1.0, delta)
        assertEquals(testIdentity(UnitInformationStorage.bits), 1.0, delta)
        assertEquals(testIdentity(UnitInformationStorage.nibbles), 1.0, delta)
        assertEquals(testIdentity(UnitInformationStorage.yottabytes), 1.0, delta)
        assertEquals(testIdentity(UnitInformationStorage.zettabytes), 1.0, delta)
        assertEquals(testIdentity(UnitInformationStorage.exabytes), 1.0, delta)
        assertEquals(testIdentity(UnitInformationStorage.petabytes), 1.0, delta)
        assertEquals(testIdentity(UnitInformationStorage.terabytes), 1.0, delta)
        assertEquals(testIdentity(UnitInformationStorage.gigabytes), 1.0, delta)
        assertEquals(testIdentity(UnitInformationStorage.megabytes), 1.0, delta)
        assertEquals(testIdentity(UnitInformationStorage.kilobytes), 1.0, delta)
        assertEquals(testIdentity(UnitInformationStorage.yottabits), 1.0, delta)
        assertEquals(testIdentity(UnitInformationStorage.zettabits), 1.0, delta)
        assertEquals(testIdentity(UnitInformationStorage.exabits), 1.0, delta)
        assertEquals(testIdentity(UnitInformationStorage.petabits), 1.0, delta)
        assertEquals(testIdentity(UnitInformationStorage.terabits), 1.0, delta)
        assertEquals(testIdentity(UnitInformationStorage.gigabits), 1.0, delta)
        assertEquals(testIdentity(UnitInformationStorage.megabits), 1.0, delta)
        assertEquals(testIdentity(UnitInformationStorage.kilobits), 1.0, delta)
        assertEquals(testIdentity(UnitInformationStorage.yobibytes), 1.0, delta)
        assertEquals(testIdentity(UnitInformationStorage.zebibytes), 1.0, delta)
        assertEquals(testIdentity(UnitInformationStorage.exbibytes), 1.0, delta)
        assertEquals(testIdentity(UnitInformationStorage.pebibytes), 1.0, delta)
        assertEquals(testIdentity(UnitInformationStorage.tebibytes), 1.0, delta)
        assertEquals(testIdentity(UnitInformationStorage.gibibytes), 1.0, delta)
        assertEquals(testIdentity(UnitInformationStorage.mebibytes), 1.0, delta)
        assertEquals(testIdentity(UnitInformationStorage.kibibytes), 1.0, delta)
        assertEquals(testIdentity(UnitInformationStorage.yobibits), 1.0, delta)
        assertEquals(testIdentity(UnitInformationStorage.zebibits), 1.0, delta)
        assertEquals(testIdentity(UnitInformationStorage.exbibits), 1.0, delta)
        assertEquals(testIdentity(UnitInformationStorage.pebibits), 1.0, delta)
        assertEquals(testIdentity(UnitInformationStorage.tebibits), 1.0, delta)
        assertEquals(testIdentity(UnitInformationStorage.gibibits), 1.0, delta)
        assertEquals(testIdentity(UnitInformationStorage.mebibits), 1.0, delta)
        assertEquals(testIdentity(UnitInformationStorage.kibibits), 1.0, delta)
    }
}
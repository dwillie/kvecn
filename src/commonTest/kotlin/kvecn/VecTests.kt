package kvecn

import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class VecTests {
    class Vec2(x: Float, y: Float) : Vec<Vec2>(arrayOf(x, y)) {
        override fun create(values: Array<Float>) =
            Vec2(values[0], values[1])
    }

    @Test
    fun testEquals() {
        assertTrue("Vectors containing the same set of values should be equal") { Vec2(1f, 1f) == Vec2(1f, 1f) }
        assertFalse("Vectors containing different values should not be equal") { Vec2(1f, 1f) == Vec2(2f, 2f) }
        assertFalse("Vectors with partial matches should not be equal") { Vec2(1f, 2f) == Vec2(2f, 1f) }
        assertFalse("Vectors with partial matches should not be equal") { Vec2(2f, 1f) == Vec2(1f, 2f) }
        assertFalse("Vectors with partial matches should not be equal") { Vec2(1f, 2f) == Vec2(0f, 1f) }
        assertFalse("Vectors with partial matches should not be equal") { Vec2(3f, 1f) == Vec2(0f, 1f) }
    }

    @Test
    fun testPlus() {
        val vec1 = Vec2(23f, 15f)
        val vec2 = Vec2(1f, 2f)
        val vec3 = Vec2(24f, 17f)

        assertEquals(vec1 + vec2, vec3, "Adding two vectors should produce the expected result")
    }

    @Test
    fun testMinus() {
        val vec0 = Vec2(0f, 0f)
        val vec1 = Vec2(1f, 1f)
        val vec2 = Vec2(2f, 2f)
        val vec3 = Vec2(3f, 3f)

        assertEquals(vec3 - vec2, vec1, "Subtracting two vectors should produce the expected result")
        assertEquals(vec2 - vec1, vec1, "Subtracting two vectors should produce the expected result")
        assertEquals(vec3 - vec3, vec0, "Subtracting two vectors should produce the expected result")
        assertEquals(vec2 - vec3, vec0 - vec1, "Subtracting two vectors should produce the expected result")
    }

    @Test
    fun testDist() {
        assertEquals(
            Vec2(0f, 0f).dist(Vec2(10f, 10f)),
            sqrt(200f),
            "Should correctly determine distance between two vectors"
        )

        assertEquals(
            Vec2(654f, 654f).dist(Vec2(654f, 654f)),
            0f,
            "Should correctly determine distance between two vectors"
        )
    }

    @Test
    fun testDistSq() {
        assertEquals(
            Vec2(0f, 0f).distSq(Vec2(10f, 10f)),
            200f,
            "Should correctly determine square distance between two vectors"
        )

        assertEquals(
            Vec2(123f, 123f).distSq(Vec2(123f, 123f)),
            0f,
            "Should correctly determine square distance between two vectors"
        )
    }

    @Test
    fun testCentroid() {
        assertEquals(
            Vec.centroid(
                arrayOf(Vec2(0f, 0f), Vec2(10f, 10f))
            )!!,
            Vec2(5f, 5f)
        )
    }
}

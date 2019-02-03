package kvecn

import kotlin.math.sqrt
import kotlin.random.Random
import kotlin.test.*

class VecTests {
    class Vec2(x: Float, y: Float) : Vec<Vec2>(floatArrayOf(x, y)) {
        override fun create(values: FloatArray) =
            Vec2(values[0], values[1])

        val x: Float
            get() = values[0]
        val y: Float
            get() = values[1]
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
                listOf(Vec2(0f, 0f), Vec2(10f, 10f))
            )!!,
            Vec2(5f, 5f)
        )

        assertNull(Vec.centroid(emptyList<Vec2>()), "Should return null centroid for empty list")
    }

    @Test
    fun testGroupBy() {
        val small = Vec2(0f, 0f)
        val big = Vec2(100f, 100f)
        val smallGroup = Vec2(10f, 10f)
        val bigGroup = Vec2(90f, 90f)

        val clusters = Vec.groupBy(
            listOf(small, big),
            listOf(smallGroup, bigGroup)
        )

        assertEquals(clusters.size, 2, "Should result in two clusters")
        for (cluster in clusters) {
            if (cluster.center == smallGroup) {
                assertTrue("Small group should contain small vector") { cluster.members.contains(small) }
            } else if (cluster.center == bigGroup) {
                assertTrue("Big group should contain big vector") { cluster.members.contains(big) }
            } else {
                assertTrue("There should not be any other clusters") { false }
            }
        }
    }

    @Test
    fun testRecomputeCenter() {
        val out = Vec.recomputeCenter(Cluster(Vec2(0f, 0f), listOf(Vec2(-10f, -5f), Vec2(6f, 6f), Vec2(20f, 10f))))
        assertEquals(out.center, Vec2(6f, 6f), "Cluster center should be correctly selected")
    }

    @Test
    fun testKMeans() {
        val r = Random(10)
        val maxVec = Vec2(10f, 10f)
        val vecs = List(100) { Vec2(r.nextFloat() * maxVec.x, r.nextFloat() * maxVec.y) }
        val k = 10
        val clusters = Vec.kMeans(k, maxVec, vecs, r)

        assertEquals(k, clusters.size, "Number of clusters should equate to k where k > n")
        for (cluster in clusters) {
            assertTrue("Cluster centers should be members of the original set of vectors") { vecs.contains(cluster.center) }
        }
    }
}

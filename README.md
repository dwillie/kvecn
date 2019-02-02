# kvecn
A Kotlin multiplatform VecN implementation

--------------------------

Example:

```kotlin
class Vec2(x: Float, y: Float) : Vec<Vec2>(arrayOf(x, y)) {
    override fun create(values: Array<Float>) =
        Vec2(values[0], values[1])

    val x: Float
        get() = values[0]
    val y: Float
        get() = values[1]
}

fun main() {
    assertEquals(Vec2(0f, 1f) + Vec2(1f, 1f), Vec2(1f, 2f))
    val vecs = arrayOf(Vec2(0f, 0f), Vec2(10f, 10f))
    val centroid = Vec.centroid(vecs)
    assertEquals(centroid, Vec2(5f, 5f))
    assertEquals(Vec2(0f, 0f).distSq(Vec2(10f, 10f)), 200f)
    assertEquals(Vec2(0f, 0f).dist(Vec2(10f, 10f)), sqrt(200f))
}
```

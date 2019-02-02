package kvecn

import kotlin.math.sqrt

abstract class Vec<V : Vec<V>>(val values: FloatArray) {
    abstract fun create(values: FloatArray): V

    operator fun plus(other: V): V =
        create(values.zip(other.values).map { z -> z.first + z.second }.toFloatArray())

    operator fun minus(other: V): V =
        create(values.zip(other.values).map { z -> z.first - z.second }.toFloatArray())

    fun distSq(other: V): Float =
        values.zip(other.values)
            .map { it.first - it.second }
            .map { it * it }
            .sum()

    fun dist(other: V): Float = sqrt(distSq(other))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (other !is Vec<*>) return false

        return values.zip(other.values).none { pair -> pair.first != pair.second }
    }

    override fun hashCode(): Int {
        return values.hashCode()
    }

    override fun toString(): String {
        return "${values.size}D Vec(${values.contentToString()})"
    }

    companion object {
        fun <V : Vec<V>> centroid(vecs: Collection<V>): V? =
            vecs.firstOrNull()?.let {
                val sumValues = MutableList(it.values.size) { 0f }
                for (vec in vecs) {
                    vec.values.forEachIndexed { i, value -> sumValues[i] += value }
                }

                it.create(sumValues.map { value -> value / vecs.size }.toFloatArray())
            }
    }


}

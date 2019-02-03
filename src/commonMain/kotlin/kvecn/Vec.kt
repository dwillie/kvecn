package kvecn

import kotlin.math.sqrt
import kotlin.random.Random

data class Cluster<V : Vec<V>>(val center: V, var members: List<V>)

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
        fun <V : Vec<V>> centroid(vecs: List<V>): V? =
            vecs.firstOrNull()?.let {
                val sumValues = MutableList(it.values.size) { 0f }
                for (vec in vecs) {
                    vec.values.forEachIndexed { i, value -> sumValues[i] += value }
                }

                it.create(sumValues.map { value -> value / vecs.size }.toFloatArray())
            }

        inline fun <reified V : Vec<V>> recomputeCenter(cluster: Cluster<V>): Cluster<V> {
            val centroid = Vec.centroid(cluster.members) ?: return cluster
            return Cluster(cluster.members.sortedBy(centroid::distSq).first(), cluster.members)
        }

        fun <V : Vec<V>> groupBy(vectors: List<V>, means: List<V>): List<Cluster<V>> {
            val clusters = means.map { Cluster(it, emptyList()) }

            for (vector in vectors) {
                val nearest = clusters.sortedBy { cluster -> cluster.center.distSq(vector) }.first()
                nearest.members += vector
            }

            return clusters
        }

        inline fun <reified V : Vec<V>> kMeans(k: Int, maxVec: V, vectors: List<V>, random: Random): List<Cluster<V>> {
            val initialMeans = List(k) { maxVec.create(maxVec.values.map { random.nextFloat() * it }.toFloatArray()) }
            return kMeans(initialMeans, vectors)
        }

        inline fun <reified V : Vec<V>> kMeans(initialMeans: List<V>, vectors: List<V>): List<Cluster<V>> {
            val k = initialMeans.size
            var clusters = List(k) { i -> Cluster(initialMeans[i], emptyList()) }

            var complete = false
            while (!complete) {
                clusters = groupBy(vectors, clusters.map { it.center })

                val nextClusters = clusters.map { recomputeCenter(it) }
                complete = true
                for (i in 0 until k) {
                    complete = clusters[i].center == nextClusters[i].center
                    if (!complete) break
                }

                clusters = nextClusters
                if (!complete) clusters.forEach { it.members = emptyList() }
            }

            return clusters
        }

    }

}

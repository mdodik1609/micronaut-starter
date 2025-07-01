package hr.mdodik.service.idGenerator

import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.ReturnDocument
import com.mongodb.client.model.Updates.inc
import hr.mdodik.model.Counters
import jakarta.inject.Singleton
import org.litote.kmongo.coroutine.CoroutineCollection
import kotlin.reflect.KClass

@Singleton
class IdGeneratorService(
    private val counters: CoroutineCollection<Counters>
) {

    private suspend fun getNextSequence(entity: KClass<*>): Int {

        val result = counters.findOneAndUpdate(
            eq("_id", entity.simpleName),
            inc("seq", 1),
            FindOneAndUpdateOptions()
                .returnDocument(ReturnDocument.AFTER)
                .upsert(true)
        )
        val key = entity.simpleName ?: throw IllegalArgumentException("Class must have a name")

        return result?.seq
            ?: throw RuntimeException("Failed to generate ID for $key")
    }

    suspend fun <T : Any> generateIdFor(entityClass: KClass<T>): Int {
        return getNextSequence(entityClass)
    }
}
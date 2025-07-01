package hr.mdodik.config

import hr.mdodik.model.Counters
import hr.mdodik.model.User
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Value
import jakarta.inject.Named
import jakarta.inject.Singleton
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.reactivestreams.KMongo

@Factory
class MongoConfig {

    @Bean
    @Singleton
    fun mongoClient (
        @Value("\${mongodb.host}") host: String,
    ): CoroutineClient =
        KMongo.createClient(host).coroutine

    @Bean
    @Singleton
    fun mongoDatabase(
        @Value("\${mongodb.database}") database: String,
        mongoClient: CoroutineClient
    ) = mongoClient.getDatabase(database)

    @Bean
    @Named("counterCollection")
    @Singleton
    fun counterCollection(
        mongoDatabase: CoroutineDatabase,
    ) = mongoDatabase.getCollection<Counters>("counterCollection")

    @Bean
    @Named("userCollection")
    @Singleton
    fun userCollection(
        mongoDatabase: CoroutineDatabase,
    ) = mongoDatabase.getCollection<User>("userCollection")

}
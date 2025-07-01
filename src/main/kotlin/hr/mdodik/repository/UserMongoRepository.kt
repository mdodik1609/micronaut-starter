package hr.mdodik.repository

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.client.result.UpdateResult
import hr.mdodik.model.UpdateUserRequest
import hr.mdodik.model.User
import jakarta.inject.Singleton
import org.bson.conversions.Bson
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq

@Singleton
class UserMongoRepository(
    private val userCollection: CoroutineCollection<User>
) {
    suspend fun getOneById(id: Int) = userCollection.findOne(User::id eq id)

    suspend fun getAll() = userCollection.find().toList()

    suspend fun save(user: User) = userCollection.save(user)

    suspend fun deleteById(id: Int) = userCollection.deleteOne(User::id eq id)

    suspend fun update(id: Int, user: UpdateUserRequest): UpdateResult {
        val filter = Filters.eq("_id", id)
        val updates = mutableListOf<Bson>()
        user.email?.let {
            updates.add(Updates.set("email", it))
        }
        user.fullName?.let {
            updates.add(Updates.set("fullName", it))
        }
        return if (updates.isNotEmpty()) {
            userCollection.updateOne(filter, Updates.combine(updates))
        } else {
            UpdateResult.unacknowledged()
        }
    }
}
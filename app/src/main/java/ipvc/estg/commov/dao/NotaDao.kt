package ipvc.estg.commov.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ipvc.estg.commov.entities.Nota

@Dao
interface NotaDao {

    @Query("SELECT * from nota_table ORDER BY id DESC")
    fun getAll(): LiveData<List<Nota>>

    @Query("SELECT * FROM nota_table WHERE id == :id")
    fun getById(id: Int): LiveData<List<Nota>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(nota: Nota)

    @Update
    suspend fun update(nota: Nota)

    @Query("DELETE FROM nota_table")
    suspend fun deleteAll()

    @Query("DELETE FROM nota_table where id == :id")
    suspend fun deleteById(id: Int)

}
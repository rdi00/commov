package ipvc.estg.commov.db


import androidx.lifecycle.LiveData
import ipvc.estg.commov.dao.NotaDao
import ipvc.estg.commov.entities.Nota

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class NotaRepository(private val notaDao: NotaDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allNotas: LiveData<List<Nota>> = notaDao.getAll()

    fun getById(id: Int): LiveData<List<Nota>> {
        return notaDao.getById(id)
    }


    suspend fun insert(nota: Nota) {
        notaDao.insert(nota)
    }

    suspend fun deleteAll(){
        notaDao.deleteAll()
    }

    suspend fun deleteById(id: Int){
        notaDao.deleteById(id)
    }

    suspend fun update(desc: String, id: Int) {
        notaDao.update(desc, id)
    }

}
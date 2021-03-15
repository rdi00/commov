package ipvc.estg.commov.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nota_table")

class Nota(

    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "desc") val desc: String
)
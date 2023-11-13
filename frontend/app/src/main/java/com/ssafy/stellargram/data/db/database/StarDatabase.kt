package com.ssafy.stellargram.data.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ssafy.stellargram.data.db.dao.StarDAO
import com.ssafy.stellargram.data.db.entity.Star
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


//@Database(entities = [Star::class], version = 1)
//abstract class StarDatabase : RoomDatabase() {
//    abstract fun starDao(): StarDAO
//
//    companion object{
//        @Volatile
//        private var INSTANCE : StarDatabase? = null
//        private val MIGRATION_1_2 = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                // Define your migration logic here
//                val sqlquery =
//                    "INSERT INTO stars VALUES(1,1,224700,'','','','',6.0000000000000001517e-05,1.0890089999999998937,219.78020000000000778,-5.2000000000000001776,-1.8799999999999998934,0.0,9.0999999999999996447,2.3900000000000001243,'F5',0.48199999999999998401,219.74050199999999222,0.0034489999999999998089,4.1770649999999998058,4.0000000000000000837e-08,-5.5400000000000003213e-06,-1.9999999999999999094e-06,1.5693400000000001228e-05,0.019006788000000000155,-2.5210299999999998443e-08,-9.1145000000000003202e-09,'','','Psc',1,1,'',9.6382902359999995667,'','','')"
////                        INSERT INTO stars VALUES(2,2,224690,'','','','',0.00028299999999999999418,-19.498840000000001282,47.961599999999997125,181.21000000000000796,-0.93000000000000004884,0.0,9.2699999999999995736,5.8659999999999996589,'K3V',0.99899999999999999911,45.210917999999999496,0.0033649999999999999356,-16.008995999999999781,-7.000000000000000477e-08,4.2129999999999998127e-05,-1.9999999999999999094e-07,7.3961099999999999022e-05,-0.34031895200000000789,8.7853100000000004668e-07,-4.5087700000000003439e-09,'','','Cet',1,2,'',0.39228346299999999891,'','','')
////                        INSERT INTO stars VALUES(3,3,224699,'','','','',0.00033500000000000001131,38.859279000000000793,442.47789999999997691,5.2400000000000002131,-2.9100000000000001421,0.0,6.6100000000000003197,-1.6189999999999999946,'B9',-0.018999999999999999528,344.55278499999997165,0.030213000000000000272,277.61496499999998377,3.9199999999999997379e-06,1.1240000000000000804e-05,-4.8600000000000000551e-06,8.7626299999999998219e-05,0.67822236300000005027,2.5404200000000001259e-08,-1.4108100000000000116e-08,'','','And',1,3,'',386.90113170000000764,'','','')"""
//                database.execSQL(sqlquery)
//            }
//        }
//
//
//
//        fun getDatabase( context: Context ) = Room
//            .databaseBuilder(context, StarDatabase::class.java, "stars")
//            .createFromAsset("StarData.db")
//            .build()
//    }
//
//}

@Database(entities = [Star::class], version = 1, exportSchema = true)
abstract class StarDatabase: RoomDatabase() {
    abstract fun starDAO(): StarDAO
}


@Module
@InstallIn(SingletonComponent::class)
object StarDatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context.applicationContext,
        StarDatabase::class.java,
        "stars"
    )
        .createFromAsset("StarData.db")
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideDao(database: StarDatabase) = database.starDAO()
}

//class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
//
//    companion object {
//        private const val DATABASE_NAME = "StarData.db"
//        private const val DATABASE_VERSION = 1
//    }
//
//    override fun onCreate(db: SQLiteDatabase?) {
//        // Database creation logic if needed
//    }
//
//    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
//        // Database upgrade logic if needed
//    }
//
//    // Add your methods to interact with the database here
//
//}
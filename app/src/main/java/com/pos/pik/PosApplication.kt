package com.pos.pik

import android.app.Application
import com.pos.pik.data.local.AppDatabase
import com.pos.pik.data.repository.PosRepository

class PosApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { PosRepository(database) }
}

package com.example.njwi_ficonnect.repository

import com.example.njwi_ficonnect.model.WifiPackage
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object WifiPackageRepository {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getAllPackages(): List<WifiPackage> {
        val snapshot = db.collection("wifi_packages").get().await()
        return snapshot.documents.mapNotNull {
            it.toObject(WifiPackage::class.java)?.copy(id = it.id)
        }
    }
}
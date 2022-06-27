package com.david_glez.seccion9_proyecto_stores.mainModule.model

import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.david_glez.seccion9_proyecto_stores.StoreApplication
import com.david_glez.seccion9_proyecto_stores.common.entities.StoreEntity
import com.david_glez.seccion9_proyecto_stores.common.utils.Constants
import com.google.gson.Gson
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainInteractor { //Model

    /* esta clase tiene como objetivo abstraer la consulta de datos para despues devolverlos a quien
    * lo solicite, Model */

    fun getStores(callback: (MutableList<StoreEntity>) -> Unit){
        val URL = Constants.STORES_URL + Constants.GET_ALL_PATH
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, URL, null, { response ->
            Log.i("Response", response.toString())
            val status = response.getInt(Constants.STATUS_PROPERTY)
            if (status == Constants.SUCCESS){
                Log.i("status", status.toString())
            }
        },{
            it.printStackTrace()
        })

        StoreApplication.storeAPI.addToRequestQueue(jsonObjectRequest)
    }

    // funcion orden superios
    fun getStoresRoom(callback: (MutableList<StoreEntity>) -> Unit){
        doAsync {
            val storesList = StoreApplication.dataBase.storeDao().getAllStores()
            uiThread {
                val json = Gson().toJson(storesList)
                Log.i("Gson", json)
                callback(storesList)
            }
        }
    }


    fun deleteStore(storeEntity: StoreEntity, callback: (StoreEntity) -> Unit){
        doAsync{
            StoreApplication.dataBase.storeDao().deleteStore(storeEntity)
            uiThread {
                callback(storeEntity)
            }
        }
    }

    fun updateStore(storeEntity: StoreEntity, callback: (StoreEntity) -> Unit){
        doAsync {
            StoreApplication.dataBase.storeDao().updateStore(storeEntity)
            uiThread {
                callback(storeEntity)
            }
        }
    }
}
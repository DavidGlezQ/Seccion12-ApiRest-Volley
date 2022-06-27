package com.david_glez.seccion9_proyecto_stores.mainModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.david_glez.seccion9_proyecto_stores.common.entities.StoreEntity
import com.david_glez.seccion9_proyecto_stores.common.utils.Constants
import com.david_glez.seccion9_proyecto_stores.mainModule.model.MainInteractor

class MainViewModel: ViewModel() { // Aqui se alamcenan todas las tiendas de nuestro modelo

    // esta clase se comunica con la vista y por getStores puede devolver el resultado
    /*tambien tiene acceso al modelo, en este caso el interactor (MainInteractor) ViewModel*/
    private var interactor: MainInteractor
    private var storeList: MutableList<StoreEntity>

    init {
        storeList = mutableListOf()
        interactor = MainInteractor()
    }

    private val stores: MutableLiveData<MutableList<StoreEntity>> by lazy{
        MutableLiveData<MutableList<StoreEntity>>().also {
            loadStores()
        }
    }

    private val showProgress: MutableLiveData<Boolean> = MutableLiveData()

    fun getStores(): LiveData<MutableList<StoreEntity>>{
        return stores
    }

    fun isShowProgress(): LiveData<Boolean>{
        return showProgress
    }

    private fun loadStores(){
        // llamada funcion de orden superior
        showProgress.value = Constants.SHOW
        interactor.getStores {
            showProgress.value = Constants.HIDE
            stores.value = it
            storeList = it
        }
    }

    fun deleteStore(storeEntity: StoreEntity){
        interactor.deleteStore(storeEntity) {
            val index = storeList.indexOf(storeEntity)
            if (index != -1){
                storeList.removeAt(index)
                stores.value = storeList
            }
        }
    }

    fun updateStore(storeEntity: StoreEntity){
        storeEntity.isFavorite = !storeEntity.isFavorite
        interactor.updateStore(storeEntity) {
            val index = storeList.indexOf(storeEntity)
            if (index != -1){
                storeList.set(index, storeEntity)
                stores.value = storeList
            }
        }
    }
}
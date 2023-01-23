package com.example.mmonews.util

sealed class Ressource<T>(val data:T? = null, message:String?=null){
    // retourne les datas quand tout ce passe bien
    class Succes<T>(data:T) : Ressource<T>(data)
    // retourne un message quand il y a un problème
    class Error<T>(message :String,data:T?=null) : Ressource<T>(data,message)
    class Loading<T>() : Ressource<T>()

}

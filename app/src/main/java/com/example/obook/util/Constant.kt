package com.example.obook.util

class Constant {
    private var USERSIGNIN: Boolean = false
    private var GSIGN: Boolean = false
    private var providerId: String = ""
    private var YTAPI: String = ""

    companion object{
        private var instance: Constant? = null
        fun getInstance(): Constant{
            if(instance==null){
                instance = Constant()
            }
            return instance!!
        }
    }

    fun setInfo(info: Boolean){
        this.USERSIGNIN = info
    }
    fun getInfo(): Boolean{
        return USERSIGNIN
    }

    fun getGSign(): Boolean{
        return GSIGN
    }

    fun setGSign(info: Boolean){
        this.GSIGN = info
    }

    fun getProviderId(): String{
        return providerId
    }

    fun setProviderId(info: String){
        this.providerId = info
    }

    fun getYTAPI(): String{
        return YTAPI
    }
}
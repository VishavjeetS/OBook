package com.example.obook.util

class Constant {
    private var USERSIGNIN: Boolean = false
    private var GSIGN: Boolean = false
    private var providerId: String = ""
    private var YTAPI: String = ""
    private var isTv: Boolean = false
    private var SCREEN: String = ""
    private var GSIGN_NAME: Boolean = false

    companion object{
        private var instance: Constant? = null
        fun getInstance(): Constant{
            if(instance==null){
                instance = Constant()
            }
            return instance!!
        }
    }

    fun getGSIGN_NAME(): Boolean{
        return GSIGN_NAME
    }
    fun setGSIGN_NAME(value: Boolean){
        this.GSIGN_NAME = value
    }
    fun setScreen(screen: String){
        this.SCREEN = screen
    }
    fun getScreen(): String{
        return SCREEN
    }
    fun setTv(info: Boolean) {
        this.isTv = info
    }
    fun getTv(): Boolean{
        return isTv
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
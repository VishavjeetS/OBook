package com.example.obook.util

import com.example.obook.Model.Movie

class Constant {
    private var USERSIGNIN: Boolean = false

    fun setInfo(info: Boolean){
        this.USERSIGNIN = info
    }
    fun getInfo(): Boolean{
        return USERSIGNIN
    }
}
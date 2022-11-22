package com.example.obook.Model.UserModel

import com.example.obook.Model.MovieModel.Movies

class User {
    private var uid: String = ""
    private var name: String = ""
    private var email: String = ""
    private var uri: String = ""
    private var password: String = ""

    companion object{
        private var favList: ArrayList<Movies> = arrayListOf()
        private var instance: User? = null
        fun getInstance(): User {
            if(instance == null){
                instance = User()
            }
        return instance!!
    }
    }
    constructor(){}
    constructor(name: String, email: String){
        this.name = name
        this.email = email
    }

    fun getUID(): String{
        return uid
    }

    fun setUID(uid: String){
        this.uid = uid
    }

    fun getUri(): String{
        return uri
    }

    fun setUri(uri: String){
        this.uri = uri
    }

    fun getName(): String?{
        return name
    }

    fun setName(name: String){
        this.name = name
    }

    fun getEmail(): String?{
        return email
    }

    fun setEmail(email: String){
        this.email = email
    }

    fun getPassword(): String{
        return password
    }

    fun setPassword(info: String){
        this.password = info
    }

    fun addToList(movie: Movies){
        getFavList().add(movie)
        println("List Size: " + getFavList().size)
    }

    fun getFavList(): ArrayList<Movies>{
        return favList
    }
}
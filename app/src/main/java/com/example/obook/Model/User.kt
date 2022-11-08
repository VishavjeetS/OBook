package com.example.obook.Model

class User {
    private var instance: User? = null
    private var uid: String = ""
    private var name: String = ""
    private var email: String = ""
    private var favList: ArrayList<Movie> = arrayListOf()

    fun getInstance(): User{
        if(instance == null){
            instance = User()
        }
        return instance!!
    }

    fun getUID(): String{
        return uid
    }

    fun setUID(uid: String){
        this.uid = uid
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

    fun setFavList(favList: ArrayList<Movie>){
        this.favList = favList
    }

    fun getFavList(): ArrayList<Movie>{
        return favList
    }
}
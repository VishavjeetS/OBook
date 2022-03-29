package com.example.obook.Model

class User {
    private var uid: String = ""
    private var name: String = ""
    private var email: String = ""

    constructor()

    constructor(
        uid: String,
        name: String,
        email: String
    ) {
        this.uid = uid
        this.name = name
        this.email = email
    }

    fun getUID(): String?{
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
}
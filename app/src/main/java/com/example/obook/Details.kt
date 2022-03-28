package com.example.obook

import java.io.Serializable

class Details:Serializable {
    var date = ""
    var time = ""
    var movie = ""
    constructor()

    constructor(date:String, time:String, movie:String){
        this.date = date
        this.time = time
        this.movie = movie
    }
}
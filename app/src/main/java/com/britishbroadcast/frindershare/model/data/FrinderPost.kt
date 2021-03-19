package com.britishbroadcast.frindershare.model.data

class FrinderPost(val userId: String, val id: String, val imageUrl: String, val description: String){
    constructor(): this("","","","")
}
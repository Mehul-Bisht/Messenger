package com.example.messenger.models

class ChatMessage (val id: String, val text: String, val fromId: String, val toId: String, val timestamp: Long , val day : String ,
                   val time : String, val date : String ){
    constructor() : this("","","","",-1, "", "","")
}
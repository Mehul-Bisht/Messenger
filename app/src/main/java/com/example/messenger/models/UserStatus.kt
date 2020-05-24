package com.example.messenger.models

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.sql.Timestamp

class UserStatus( val id : String ,val statusText : String , val statusDateAndTime : String , val timestamp: Long) {
  constructor() : this("","","",-1)
}

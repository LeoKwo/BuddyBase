package com.example.buddybase.manager

class UserManager {

    var email: String? = null
        private set
    var fullName: String? = null
        private set

    fun setEmail(email: String) {
        this.email = email
    }

    fun setFullName(fullName: String) {
        this.fullName = fullName
    }


}
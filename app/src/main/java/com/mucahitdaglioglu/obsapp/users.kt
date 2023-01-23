package com.mucahitdaglioglu.obsapp

import java.io.Serializable

data class users(  val tcNo: String? = "",
                   val nameAndSurname: String? = "",
                   val telephoneNo: String? = "",
                   val mailAddress: String? = "",
                   val degree: String? = "",
                   val approved: String? = "false" ,
                   val password: String? = ""): Serializable

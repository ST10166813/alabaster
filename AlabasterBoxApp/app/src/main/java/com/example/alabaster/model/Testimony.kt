package com.example.alabaster.model

data class Testimony(
    val id: String? = null,
    val name: String? = null,
    val title: String? = null,
    val date: String? = null,
    val testimony: String? = null,
    val status: String? = null,
    val imageBase64: String? = null // new field for image
)
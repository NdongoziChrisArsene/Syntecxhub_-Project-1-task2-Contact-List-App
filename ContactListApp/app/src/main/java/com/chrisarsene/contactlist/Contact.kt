package com.chrisarsene.contactlist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact(
    val name: String,
    val phone: String,
    val email: String,
    val avatarRes: Int
) : Parcelable

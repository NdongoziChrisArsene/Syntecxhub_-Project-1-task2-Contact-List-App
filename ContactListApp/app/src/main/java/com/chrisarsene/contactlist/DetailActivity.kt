package com.chrisarsene.contactlist

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val contact = intent.getParcelableExtra<Contact>("contact") ?: return

        val avatar: ImageView = findViewById(R.id.ivDetailAvatar)
        val name: TextView = findViewById(R.id.tvDetailName)
        val phone: TextView = findViewById(R.id.tvDetailPhone)
        val email: TextView = findViewById(R.id.tvDetailEmail)
        val btnBack: ImageButton = findViewById(R.id.btnBack)
        val btnCall: ImageButton = findViewById(R.id.btnCall)
        val btnMessage: ImageButton = findViewById(R.id.btnMessage)

        avatar.setImageResource(contact.avatarRes)
        name.text = contact.name
        phone.text = contact.phone
        email.text = contact.email

        btnBack.setOnClickListener { finish() }

        btnCall.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${contact.phone}"))
            startActivity(callIntent)
        }

        btnMessage.setOnClickListener {
            val smsIntent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:${contact.phone}"))
            startActivity(smsIntent)
        }
    }
}

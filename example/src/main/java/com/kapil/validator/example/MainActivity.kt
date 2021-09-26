package com.kapil.validator.example

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kapil.validator.example.databinding.ActivityMainBinding
import com.kapil.validator.library.ValidatorBot

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var validatorBot: ValidatorBot

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initVariables()
        setUpUiWidgets()
    }

    private fun initVariables() {
        validatorBot = ValidatorBot()
    }

    private fun setUpUiWidgets() {
        binding.btnValidate.setOnClickListener {
            validateEmailField(binding.edtEmail)
            validatePasswordField(binding.edtPassword)
            validateCreditCardField(binding.edtCreditcard)
        }
    }

    private fun validateCreditCardField(editText: EditText) {
        val str = editText.text.toString()
        if (validatorBot.isEmpty(str)) {
            editText.error = "Field is empty!"
        } else if (!validatorBot.validateCreditCard(str)) {
            editText.error = "Invalid Credit Card number!"
        } else {
            Toast.makeText(this, "Valid Credit Card Number!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validatePasswordField(editText: EditText) {
        val str = editText.text.toString()
        if (validatorBot.isEmpty(str)) {
            editText.error = "Field is empty!"
        } else if (validatorBot.isAtleastLength(str, 8)
            && validatorBot.hasAtleastOneDigit(str)
            && validatorBot.hasAtleastOneUppercaseCharacter(str)
            && validatorBot.hasAtleastOneSpecialCharacter(str)
        ) {
            Toast.makeText(this, "Valid Password!", Toast.LENGTH_SHORT).show()
        } else {
            editText.error =
                "Password needs to be a minimum length of 8 characters and should have at least 1 digit, 1 upppercase letter and 1 special character "
        }
    }

    private fun validateEmailField(editText: EditText) {
        val str = editText.text.toString()
        if (validatorBot.isEmpty(str)) {
            editText.error = "Field is empty!"
        } else if (!validatorBot.isEmail(str)) {
            editText.error = "Invalid Email"
        } else {
            Toast.makeText(this, "Valid Email!", Toast.LENGTH_SHORT).show()
        }
    }
}

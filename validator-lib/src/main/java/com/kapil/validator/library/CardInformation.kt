package com.kapil.validator.library

data class CardInformation(
    var cardNumber: String,
    var cardIssuer: String? = null,
    var isValid: Boolean = false,
    var error: String? = null
) {
    constructor(num: String) : this(num, null, false, null)
}
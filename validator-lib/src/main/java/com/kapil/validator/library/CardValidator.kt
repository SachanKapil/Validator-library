/*
 * Modifications Copyright (c) 2018 Razeware LLC
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package com.kapil.validator.library


/**
 * The type Card validator.
 */
internal class CardValidator {
    /**
     * Validate credit card number.
     *
     * @param number the number
     * @return the boolean
     */
    fun validateCreditCardNumber(number: String): Boolean {
        return (checkIfNumberContainsOnlyDigits(number)
                && validateLengthOfCardNumber(number)
                && validateAndGetStartingSixDigits(number) > 0
                && validateCardNumberWithLuhnAlgo(number))
    }

    private fun getErrorInfo(number: String): String {
        if (!checkIfNumberContainsOnlyDigits(number)) {
            return "Number should be composed of only digits!"
        }
        if (!validateLengthOfCardNumber(number)) {
            return "Card number should be of length > 12 and < 19 digits!"
        }
        if (validateAndGetStartingSixDigits(number) == 0L) {
            return "Number contains leading zeros!"
        }
        return if (!validateCardNumberWithLuhnAlgo(number)) {
            "Number did not pass the Luhn Algo Test!"
        } else "NA"
    }

    private fun getCreditCardIssuer(number: String): String {
        return getTypeOfCard(validateAndGetStartingSixDigits(number))
    }

    private fun countDigitsInNumber(num: Long): Int {
        var number = num
        var count = 0
        while (number > 0) {
            number /= 10
            count++
        }
        return count
    }

    private fun validateCardNumberWithLuhnAlgo(num: String): Boolean {
        var sumOfDoubleOfDigits = 0
        if (checkIfNumberContainsOnlyDigits(num)) {
            var alternateValue = false
            for (i in num.length - 1 downTo 0) {
                var digit = num[i].toString().toInt()
                if (alternateValue) {
                    digit *= 2
                    if (digit > 9) {
                        digit -= 9
                    }
                }
                sumOfDoubleOfDigits += digit
                alternateValue = !alternateValue
            }
        }
        return sumOfDoubleOfDigits % 10 == 0
    }

    private fun checkIfNumberContainsOnlyDigits(number: String): Boolean {
        // check if number string contains only digits
        return number.matches(Regex("[0-9]+"))
    }

    private fun validateLengthOfCardNumber(number: String): Boolean {
        // check for number of digits
        return !(number.length < 12 || number.length > 19)
    }

    private fun validateAndGetStartingSixDigits(number: String): Long {
        val startSixDigitSubstring = number.substring(0, 6)
        return if (checkIfNumberContainsOnlyDigits(startSixDigitSubstring)) {
            val startNumber = startSixDigitSubstring.toLong()
            // Check for leading zeros
            if (startNumber == 0L || countDigitsInNumber(startNumber) < 6) {
                0
            } else startNumber
        } else {
            0
        }
    }

    private fun getTypeOfCard(startingSixDigits: Long): String {
        return if (startingSixDigits in 400001..499998) {
            "Visa"
        } else if (startingSixDigits in 222101..272098 || (startingSixDigits in 510001..559998)
        ) {
            "Mastercard"
        } else if (startingSixDigits in 620001..629998) {
            "China Union Pay"
        } else if (startingSixDigits in 500001..509998 || (startingSixDigits in 560001..699998)
        ) {
            "Maestro"
        } else {
            "Unknown"
        }
    }

    /**
     * Gets card information.
     *
     * @param num the num
     * @return the card information
     */
    fun getCardInformation(num: String): CardInformation {
        val cardInformation = CardInformation(num)
        cardInformation.cardIssuer = getCreditCardIssuer(num)
        cardInformation.isValid = validateCreditCardNumber(num)
        cardInformation.error = getErrorInfo(num)
        return cardInformation
    }
}
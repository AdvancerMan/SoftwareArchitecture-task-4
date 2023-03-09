package com.example.shop.service

import com.example.shop.model.CurrencyType

interface CurrencyConverterService {

    fun convertRublesTo(rubles: Double, currencyType: CurrencyType): Double
}

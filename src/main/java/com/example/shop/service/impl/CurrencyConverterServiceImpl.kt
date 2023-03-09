package com.example.shop.service.impl

import com.example.shop.model.CurrencyType
import com.example.shop.service.CurrencyConverterService
import org.springframework.stereotype.Service

@Service
class CurrencyConverterServiceImpl : CurrencyConverterService {

    companion object {
        private const val RUBLES_TO_DOLLARS = 123.0
        private const val RUBLES_TO_EUROS = 321.0
    }

    override fun convertRublesTo(rubles: Double, currencyType: CurrencyType): Double {
        return when (currencyType) {
            CurrencyType.DOLLAR -> rubles * RUBLES_TO_DOLLARS
            CurrencyType.EURO -> rubles * RUBLES_TO_EUROS
            CurrencyType.RUBLE -> rubles
        }
    }
}

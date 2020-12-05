package com.fypcuiatk.feeclearanceapp.Models.DatabaseModels

import java.sql.Timestamp

data class Receipt(var url: String, var transactionNum: String, var amount: String, val timestamp: Any?)
{
    constructor(): this("", "", "", null)
}

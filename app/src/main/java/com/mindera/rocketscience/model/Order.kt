package com.mindera.rocketscience.model

import java.time.Year
sealed class Order {
    object ASC: Order()
    object DESC: Order()
}
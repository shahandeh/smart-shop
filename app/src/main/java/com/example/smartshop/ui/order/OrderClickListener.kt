package com.example.smartshop.ui.order

interface OrderClickListener {

    fun minusCount (id: Int)

    fun plusCount (id: Int)

    fun clickItem (id: Int)
}
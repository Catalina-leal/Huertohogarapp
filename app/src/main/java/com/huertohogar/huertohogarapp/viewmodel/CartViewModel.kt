package com.huertohogar.huertohogarapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.huertohogar.huertohogarapp.model.Producto

class CartViewModel : ViewModel() {


    private val _items = mutableStateListOf<Producto>()
    val items: List<Producto> get() = _items


    val neto = mutableStateOf(0.0)   // Total sin IVA
    val iva = mutableStateOf(0.0)    // IVA del 19%
    val total = mutableStateOf(0.0)  // Neto + IVA

    fun addItem(producto: Producto) {
        val existing = _items.find { it.id == producto.id }
        if (existing != null) {
            val updated = existing.copy(cantidad = existing.cantidad + 1)
            _items.remove(existing)
            _items.add(updated)
        } else {
            _items.add(producto.copy(cantidad = 1))
        }
        calcularTotales()
    }


    fun removeItem(producto: Producto) {
        val existing = _items.find { it.id == producto.id }
        if (existing != null) {
            _items.remove(existing)
        }
        calcularTotales()
    }


    fun clearCart() {
        _items.clear()
        neto.value = 0.0
        iva.value = 0.0
        total.value = 0.0
    }


    private fun calcularTotales() {
        val subtotal = _items.sumOf { it.precio * it.cantidad } // Neto sin IVA
        val ivaCalculado = subtotal * 0.19                       // IVA 19%
        val totalCalculado = subtotal + ivaCalculado             // Neto + IVA

        neto.value = subtotal
        iva.value = ivaCalculado
        total.value = totalCalculado
    }
}


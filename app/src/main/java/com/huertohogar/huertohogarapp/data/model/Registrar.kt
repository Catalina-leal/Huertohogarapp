package com.huertohogar.huertohogarapp.data.model

// Modelo principal con el nuevo campo RUT incluido
data class Registrar(
    val rut: String = "",
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val errors: RegistrarError = RegistrarError()
)

// Modelo para guardar los posibles errores del formulario
data class RegistrarError(
    val rutError: String? = null,
    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null
) {
    // Función que indica si el formulario tiene errores
    fun hasErrors(): Boolean {
        return rutError != null ||
                nameError != null ||
                emailError != null ||
                passwordError != null ||
                confirmPasswordError != null
    }
}

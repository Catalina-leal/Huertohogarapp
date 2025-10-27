package com.huertohogar.huertohogarapp.screens
import com.huertohogar.huertohogarapp.data.model.Registrar
import com.huertohogar.huertohogarapp.data.model.RegistrarError
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun RegisterScreen(navController: NavController) {
    var registro by remember { mutableStateOf(Registrar()) }
    var errores by remember { mutableStateOf(RegistrarError()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Crear cuenta",
            color = Color(0xFF388E3C),
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = registro.name,
            onValueChange = { registro = registro.copy(name = it) },
            label = { Text("Nombre completo") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        if (errores.nameError != null)
            Text(errores.nameError!!, color = Color.Red, fontSize = 12.sp)

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = registro.email,
            onValueChange = { registro = registro.copy(email = it) },
            label = { Text("Correo electrónico") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        if (errores.emailError != null)
            Text(errores.emailError!!, color = Color.Red, fontSize = 12.sp)

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = registro.password,
            onValueChange = { registro = registro.copy(password = it) },
            label = { Text("Contraseña") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        if (errores.passwordError != null)
            Text(errores.passwordError!!, color = Color.Red, fontSize = 12.sp)

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = registro.confirmPassword,
            onValueChange = { registro = registro.copy(confirmPassword = it) },
            label = { Text("Confirmar contraseña") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        if (errores.confirmPasswordError != null)
            Text(errores.confirmPasswordError!!, color = Color.Red, fontSize = 12.sp)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                errores = validarRegistro(
                    registro.email,
                    registro.password,
                    registro.confirmPassword,
                    registro.name
                )

                if (!errores.hasErrors()) {
                    navController.navigate("login")
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarme", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { navController.navigate("login") }) {
            Text("¿Ya tienes cuenta? Inicia sesión", color = Color(0xFF388E3C))
        }
    }
}

fun validarRegistro(
    email: String,
    password: String,
    confirmPassword: String,
    name: String
): RegistrarError {
    var emailError: String? = null
    var passwordError: String? = null
    var confirmError: String? = null
    var nameError: String? = null

    if (name.isEmpty()) nameError = "El nombre es obligatorio"
    if (!email.contains("@")) emailError = "Correo inválido"
    if (password.length < 6) passwordError = "Debe tener al menos 6 caracteres"
    if (confirmPassword != password) confirmError = "Las contraseñas no coinciden"

    return RegistrarError(
        emailError = emailError,
        nameError = nameError,
        passwordError = passwordError,
        confirmPasswordError = confirmError
    )
}

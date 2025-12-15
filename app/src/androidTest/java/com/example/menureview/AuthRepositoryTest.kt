package com.example.menureview

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.menureview.data.models.CuentaEntity
import com.example.menureview.viewmodel.AuthRepository
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Pruebas instrumentadas para AuthRepository
 * Requieren emulador/dispositivo porque usa SharedPreferences
 */
@RunWith(AndroidJUnit4::class)
class AuthRepositoryTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var context: Context

    /**
     * Se ejecuta ANTES de cada test
     * Inicializa el repositorio con un contexto limpio
     */
    @Before
    fun setUp() {
        // Obtener contexto de la aplicación de prueba
        context = ApplicationProvider.getApplicationContext()

        // Limpiar SharedPreferences antes de cada test
        context.getSharedPreferences("auth", Context.MODE_PRIVATE)
            .edit()
            .clear()
            .commit()

        // Crear instancia del repositorio
        authRepository = AuthRepository(context)
    }

    /**
     * Se ejecuta DESPUÉS de cada test
     * Limpia los datos para no afectar otros tests
     */
    @After
    fun tearDown() {
        // Limpiar SharedPreferences
        context.getSharedPreferences("auth", Context.MODE_PRIVATE)
            .edit()
            .clear()
            .commit()
    }

    // ========================================
    // TESTS DE TOKEN
    // ========================================

    @Test
    fun saveToken_guardaTokenCorrectamente() {
        // Arrange (Preparar)
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test"

        // Act (Actuar)
        authRepository.saveToken(token)

        // Assert (Verificar)
        val tokenGuardado = authRepository.getToken()
        assertEquals(token, tokenGuardado)
    }

    @Test
    fun getToken_retornaNullCuandoNoHayToken() {
        // Act
        val token = authRepository.getToken()

        // Assert
        assertNull(token)
    }

    @Test
    fun getBearerToken_retornaTokenFormateado() {
        // Arrange
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test"
        authRepository.saveToken(token)

        // Act
        val bearerToken = authRepository.getBearerToken()

        // Assert
        assertEquals("Bearer $token", bearerToken)
    }

    @Test
    fun getBearerToken_retornaNullCuandoNoHayToken() {
        // Act
        val bearerToken = authRepository.getBearerToken()

        // Assert
        assertNull(bearerToken)
    }

    // ========================================
    // TESTS DE USUARIO
    // ========================================

    @Test
    fun saveUser_guardaUsuarioCorrectamente() {
        // Arrange
        val usuario = CuentaEntity(
            id = 1,
            nombre = "Test User",
            correo = "test@example.com",
            tipousuario_id = 3,
            active = true
        )

        // Act
        authRepository.saveUser(usuario)

        // Assert
        val usuarioGuardado = authRepository.getUser()
        assertNotNull(usuarioGuardado)
        assertEquals(usuario.id, usuarioGuardado?.id)
        assertEquals(usuario.nombre, usuarioGuardado?.nombre)
        assertEquals(usuario.correo, usuarioGuardado?.correo)
    }

    @Test
    fun getUser_retornaNullCuandoNoHayUsuario() {
        // Act
        val usuario = authRepository.getUser()

        // Assert
        assertNull(usuario)
    }

    @Test
    fun saveUser_sobrescribeUsuarioAnterior() {
        // Arrange
        val usuario1 = CuentaEntity(1, "Usuario 1", "user1@example.com", 3, true)
        val usuario2 = CuentaEntity(2, "Usuario 2", "user2@example.com", 3, true)

        // Act
        authRepository.saveUser(usuario1)
        authRepository.saveUser(usuario2)

        // Assert
        val usuarioGuardado = authRepository.getUser()
        assertEquals(usuario2.id, usuarioGuardado?.id)
        assertEquals(usuario2.nombre, usuarioGuardado?.nombre)
    }

    // ========================================
    // TESTS DE SESIÓN
    // ========================================

    @Test
    fun isLoggedIn_retornaFalseCuandoNoHayDatos() {
        // Act
        val isLoggedIn = authRepository.isLoggedIn()

        // Assert
        assertFalse(isLoggedIn)
    }

    @Test
    fun isLoggedIn_retornaFalseSoloConToken() {
        // Arrange
        authRepository.saveToken("test_token")

        // Act
        val isLoggedIn = authRepository.isLoggedIn()

        // Assert
        assertFalse(isLoggedIn) // Falta el usuario
    }

    @Test
    fun isLoggedIn_retornaFalseSoloConUsuario() {
        // Arrange
        val usuario = CuentaEntity(1, "Test", "test@example.com",3, true)
        authRepository.saveUser(usuario)

        // Act
        val isLoggedIn = authRepository.isLoggedIn()

        // Assert
        assertFalse(isLoggedIn) // Falta el token
    }

    @Test
    fun isLoggedIn_retornaTrueCuandoHayTokenYUsuario() {
        // Arrange
        authRepository.saveToken("test_token")
        authRepository.saveUser(CuentaEntity(1, "Test", "test@example.com",3, true))

        // Act
        val isLoggedIn = authRepository.isLoggedIn()

        // Assert
        assertTrue(isLoggedIn)
    }

    @Test
    fun clearSession_eliminaTokenYUsuario() {
        // Arrange
        authRepository.saveToken("test_token")
        authRepository.saveUser(CuentaEntity(1, "Test", "test@example.com",3, true))

        // Act
        authRepository.clearSession()

        // Assert
        assertNull(authRepository.getToken())
        assertNull(authRepository.getUser())
        assertFalse(authRepository.isLoggedIn())
    }

    @Test
    fun clearToken_aliasParaClearSession() {
        // Arrange
        authRepository.saveToken("test_token")
        authRepository.saveUser(CuentaEntity(1, "Test", "test@example.com",3, true))

        // Act
        authRepository.clearToken()

        // Assert
        assertNull(authRepository.getToken())
        assertNull(authRepository.getUser())
    }

    // ========================================
    // TESTS DE PERSISTENCIA
    // ========================================

    @Test
    fun datosPersisteEntreInstancias() {
        // Arrange
        val token = "test_token"
        val usuario = CuentaEntity(1, "Test", "test@example.com",3, true)
        authRepository.saveToken(token)
        authRepository.saveUser(usuario)

        // Act: Crear nueva instancia del repositorio
        val nuevoRepo = AuthRepository(context)

        // Assert: Los datos deben persistir
        assertEquals(token, nuevoRepo.getToken())
        assertEquals(usuario.nombre, nuevoRepo.getUser()?.nombre)
        assertTrue(nuevoRepo.isLoggedIn())
    }

    @Test
    fun getUser_manejaJSONCorrupto() {
        // Arrange: Guardar JSON inválido directamente
        context.getSharedPreferences("auth", Context.MODE_PRIVATE)
            .edit()
            .putString("user", "{invalid_json")
            .commit()

        // Act
        val usuario = authRepository.getUser()

        // Assert: Debe retornar null sin crashear
        assertNull(usuario)
    }
}
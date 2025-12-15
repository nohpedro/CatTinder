<template>
  <div>
    <h1>CatTinder Frontend</h1>
    <p v-if="authenticated"><strong>Estado:</strong> Conectado</p>
    <p v-else><strong>Estado:</strong> No autenticado</p>

    <button @click="login" v-if="!authenticated">Login</button>
    <button @click="logout" v-if="authenticated">Logout</button>

    <hr />
    <router-link to="/protected">Ir a página protegida</router-link>
  </div>
</template>

<script>
import KeycloakService from '../services/keycloak'

export default {
  data() { return { authenticated: false } },
  created() {
    this.authenticated = KeycloakService.isAuthenticated()
    setInterval(() => KeycloakService.updateToken(60), 5 * 60 * 1000)
  },
  methods: {
    login() { KeycloakService.login() },
    logout() { KeycloakService.logout() }
  }
}
</script>

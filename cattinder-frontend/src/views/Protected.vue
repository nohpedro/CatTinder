<template>
  <div>
    <h2>Protected Area</h2>

    <!-- Listar usuarios -->
    <button @click="getUsers">Ver Usuarios</button>
    <pre style="white-space:pre-wrap;margin-top:12px">{{ usersResponse }}</pre>

    <hr />

    <!-- Crear usuario -->
    <h3>Crear Usuario</h3>
    <form @submit.prevent="createUser">
      <input v-model="newUser.name" placeholder="Nombre" required />
      <input v-model="newUser.email" placeholder="Email" required />
      <label>
        Activo:
        <input type="checkbox" v-model="newUser.active" />
      </label>
      <button type="submit">Crear</button>
    </form>
    <pre style="white-space:pre-wrap;margin-top:12px">{{ createResponse }}</pre>
  </div>
</template>

<script>
import KeycloakService from '../services/keycloak'

export default {
  data() {
    return {
      usersResponse: '',
      createResponse: '',
      newUser: {
        name: '',
        email: '',
        active: true
      }
    }
  },
  methods: {
    async getToken() {
      await KeycloakService.updateToken(60)
      return KeycloakService.getToken()
    },

    async getUsers() {
      const token = await this.getToken()
      try {
        const res = await fetch('http://localhost:8083/users/api/v1/users', {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Accept': 'application/json'
          }
        })
        if (!res.ok) {
          this.usersResponse = `HTTP ${res.status} ${res.statusText}\n` + await res.text()
          return
        }
        const data = await res.json()
        this.usersResponse = JSON.stringify(data, null, 2)
      } catch (e) {
        this.usersResponse = 'Error: ' + e.message
      }
    },

    async createUser() {
      const token = await this.getToken()
      try {
        const res = await fetch('http://localhost:8083/users/api/v1/users', {
          method: 'POST',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(this.newUser)
        })
        if (!res.ok) {
          this.createResponse = `HTTP ${res.status} ${res.statusText}\n` + await res.text()
          return
        }
        const data = await res.json()
        this.createResponse = 'Usuario creado:\n' + JSON.stringify(data, null, 2)

        // Limpiar formulario
        this.newUser.name = ''
        this.newUser.email = ''
        this.newUser.active = true
      } catch (e) {
        this.createResponse = 'Error: ' + e.message
      }
    }
  }
}
</script>

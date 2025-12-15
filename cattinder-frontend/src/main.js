import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import KeycloakService from './services/keycloak'

const app = createApp(App)

KeycloakService.init()
  .then(() => {
    app.use(router)
    app.mount('#app')
  })
  .catch(err => {
    console.error('Keycloak init failed', err)
    app.use(router)
    app.mount('#app')
  })

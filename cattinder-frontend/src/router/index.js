import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import Protected from '../views/Protected.vue'
import KeycloakService from '../services/keycloak'

const routes = [
  { path: '/', component: Home },
  {
    path: '/protected',
    component: Protected,
    beforeEnter: (to, from, next) => {
      if (KeycloakService.isAuthenticated()) next()
      else KeycloakService.login()
    }
  }
]

export default createRouter({
  history: createWebHistory(),
  routes
})

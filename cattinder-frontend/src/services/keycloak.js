import Keycloak from 'keycloak-js'

const keycloakConfig = {
  url: 'http://localhost:8080', // si usas docker-compose, desde el navegador sigue siendo localhost
  realm: 'cattinder',
  clientId: 'cattinder-frontend'
}

class KeycloakService {
  constructor() {
    this.keycloak = new Keycloak(keycloakConfig)
  }

  init() {
    return new Promise((resolve, reject) => {
      this.keycloak.init({
        onLoad: 'check-sso',
        pkceMethod: 'S256',
        checkLoginIframe: false
      }).then(() => resolve()).catch(e => reject(e))
    })
  }

  login() { this.keycloak.login() }
  logout() { this.keycloak.logout({ redirectUri: window.location.origin }) }
  isAuthenticated() { return !!this.keycloak?.token }
  getToken() { return this.keycloak?.token }
  updateToken(minValidity = 60) {
    return this.keycloak.updateToken(minValidity).catch(() => {
      console.warn('Failed to refresh token')
    })
  }
}

export default new KeycloakService()

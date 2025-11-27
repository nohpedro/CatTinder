import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
    url: "http://localhost:8090",        // URL de Keycloak desde el navegador
    realm: "swipes-realm",
    clientId: "swipe-client",
});

export default keycloak;

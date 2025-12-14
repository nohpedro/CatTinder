import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
    url: "http://localhost:8080/",
    realm: "tinder-realm",
    clientId: "tinder",
});

export default keycloak;

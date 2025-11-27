import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
    url: "http://localhost:8090/",
    realm: "confi-realm",
    clientId: "conf-client",
});

export default keycloak;

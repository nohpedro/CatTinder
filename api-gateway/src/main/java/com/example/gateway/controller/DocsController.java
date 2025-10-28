package com.example.gateway.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DocsController {

    @GetMapping(value = "/swagger", produces = MediaType.TEXT_HTML_VALUE)
    public String docsHome() {
        StringBuilder html = new StringBuilder();
        html.append("<html>")
                .append("<head>")
                .append("<title>CatTinder API Docs</title>")
                .append("<style>")
                .append("body{font-family:system-ui,sans-serif;background:#0f172a;color:#f8fafc;padding:2rem;}")
                .append(".card{background:#1e293b;border-radius:1rem;padding:1.5rem 2rem;margin-bottom:1rem;")
                .append("box-shadow:0 20px 60px rgba(0,0,0,0.6);border:1px solid #334155;}")
                .append("a{color:#38bdf8;text-decoration:none;font-size:1.1rem;}")
                .append("a:hover{text-decoration:underline;color:#7dd3fc;}")
                .append("h1{font-size:1.8rem;margin-bottom:1.5rem;color:#fff;}")
                .append(".path{font-family:monospace;color:#94a3b8;font-size:.9rem;margin-top:.5rem;}")
                .append("</style>")
                .append("</head>")
                .append("<body>")
                .append("<h1>CatTinder - Documentación de Microservicios</h1>")

                // USERS SERVICE
                .append("<div class=\"card\">")
                .append("<div><strong>Users Service</strong></div>")
                .append("<div class=\"path\">/users/**</div>")
                .append("<div><a href=\"/swagger/users/swagger-ui/index.html?url=/swagger/users/v3/api-docs\" target=\"_blank\">")
                .append("Abrir Swagger Users")
                .append("</a></div>")
                .append("</div>")

                // PREFERENCES SERVICE
                .append("<div class=\"card\">")
                .append("<div><strong>Preferences Service</strong></div>")
                .append("<div class=\"path\">/preferences/**</div>")
                .append("<div><a href=\"/swagger/preferences/swagger-ui/index.html?url=/swagger/preferences/v3/api-docs\" target=\"_blank\">")
                .append("Abrir Swagger Preferences")
                .append("</a></div>")
                .append("</div>")

                // ACTUATOR GATEWAY
                .append("<div class=\"card\">")
                .append("<div><strong>Gateway Actuator</strong></div>")
                .append("<div class=\"path\">/actuator/gateway</div>")
                .append("<div><a href=\"/actuator/gateway\" target=\"_blank\">")
                .append("Ver rutas activas del Gateway")
                .append("</a></div>")
                .append("</div>")

                .append("</body>")
                .append("</html>");

        return html.toString();
    }
}

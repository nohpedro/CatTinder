package com.example.swipeservice.docs.schemas;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "MatchView", description = "Representación del match entre dos usuarios")
public class MatchView {

    @Schema(description = "Id del match", example = "a1b2c3d4e5")
    private String id;

    @Schema(description = "Usuario A", example = "uid-123")
    private String uidA;

    @Schema(description = "Usuario B", example = "uid-999")
    private String uidB;



    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUidA() { return uidA; }
    public void setUidA(String uidA) { this.uidA = uidA; }

    public String getUidB() { return uidB; }
    public void setUidB(String uidB) { this.uidB = uidB; }

}

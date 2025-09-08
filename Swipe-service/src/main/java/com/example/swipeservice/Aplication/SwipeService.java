package com.example.swipeservice.Aplication;

import com.example.swipeservice.dto.MatchDto;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SwipeService
{
    private final Map<String,String> swipes = new ConcurrentHashMap<>();
    private final Map<String,MatchDto> matches = new ConcurrentHashMap<>();

    // Normaliza cadenas: quita espacios y pone en minúsculas
    private static String norm(String s) {
        return s == null ? null : s.trim().toLowerCase();
    }

    // Construye la llave del swipe: objetivo-actor (normalizados)
    private static String swipeId(String ActorId,String ObejtivoId){
        return norm(ObejtivoId) + "-" + norm(ActorId);
    }

    // Construye la llave del match
    private static String MatchId(String a,String b){
        String A = norm(a), B = norm(b);
        return (A.compareTo(B) < 0) ? A + "_" + B : B + "_" + A;
    }

    // Indica si un swipe es positivo ("right" o "like"), normalizando
    public static boolean esPositivo(String dir){
        String d = norm(dir);
        return "right".equals(d) || "like".equals(d);
    }

    // Valida y guarda un swipe usando llaves normalizadas
    public void guardarSwipe(String actorId, String objetivoId, String dir){
        String A = norm(actorId);
        String B = norm(objetivoId);
        String D = norm(dir);

        if (A == null || A.isEmpty() || B == null || B.isEmpty()){
            throw new IllegalArgumentException("actorId y targetId son requeridos");
        }
        if (D == null || D.isEmpty()){
            throw new IllegalArgumentException("dir es requerido");
        }
        swipes.put(swipeId(A, B), D);
    }

    // Devuelve la dirección del swipe de ActorId hacia ObjetivoId (normalizados)
    private String obtenerDireccion(String ActorId,String ObejtivoId){
        return swipes.get(swipeId(ActorId, ObejtivoId));
    }

    // Procesa el swipe y, si hay reciprocidad positiva, crea/retorna matchId
    public String procesarSwipeYQuizasMatch(String actorId, String targetId, String dir) {
        guardarSwipe(actorId, targetId, dir);

        if (!esPositivo(dir)) return null;

        String retorno = obtenerDireccion(targetId, actorId); // busca el contrario
        if (retorno != null && esPositivo(retorno)) {
            String id = MatchId(actorId, targetId);
            matches.computeIfAbsent(id, k -> {
                String A = norm(actorId);
                String B = norm(targetId);
                String usuarioA = (A.compareTo(B) < 0) ? A : B;
                String usuarioB = (A.compareTo(B) < 0) ? B : A;
                return new MatchDto(usuarioA, usuarioB, "activo");
            });
            return id;
        }
        return null;
    }

    public MatchDto obtenerMatch(String uidA, String uidB) {
        return matches.get(MatchId(uidA, uidB));
    }
}

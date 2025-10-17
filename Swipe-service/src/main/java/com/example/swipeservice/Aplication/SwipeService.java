package com.example.swipeservice.Aplication;

import com.example.swipeservice.dto.MatchDto;
import com.example.swipeservice.excepcion.ExcepcionSolicitudInvalida;
import com.example.swipeservice.dominio.entidad.MatchEntity;
import com.example.swipeservice.dominio.entidad.SwipeEntity;
import com.example.swipeservice.dominio.repositorio.MatchRepository;
import com.example.swipeservice.dominio.repositorio.SwipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SwipeService
{
    private final SwipeRepository swipeRepo;
    private final MatchRepository matchRepo;

    public SwipeService(SwipeRepository swipeRepo, MatchRepository matchRepo) {
        this.swipeRepo = swipeRepo;
        this.matchRepo = matchRepo;
    }

    // Normaliza cadenas: quita espacios y pone en minúsculas
    private static String norm(String s) {
        return s == null ? null : s.trim().toLowerCase();
    }

    // Construye la llave del swipe: objetivo-actor (normalizados)  (solo para compatibilidad lógica)
    private static String swipeId(String ActorId,String ObejtivoId){
        return norm(ObejtivoId) + "-" + norm(ActorId);
    }

    // Construye la llave del match (IMPORTANTE: mantiene "_" como en tu versión)
    private static String MatchId(String a,String b){
        String A = norm(a), B = norm(b);
        return (A.compareTo(B) < 0) ? A + "_" + B : B + "_" + A;
    }

    // Indica si un swipe es positivo ("right" o "like"), normalizando
    public static boolean esPositivo(String dir){
        String d = norm(dir);
        return "right".equals(d) || "like".equals(d);
    }

    // Valida y guarda un swipe usando llaves normalizadas (UPERT en BD)
    @Transactional
    public void guardarSwipe(String actorId, String objetivoId, String dir){
        String A = norm(actorId);
        String B = norm(objetivoId);
        String D = norm(dir);

        if (A == null || A.isEmpty() || B == null || B.isEmpty()){
            throw new ExcepcionSolicitudInvalida("actorId y targetId son requeridos");
        }
        if (D == null || D.isEmpty()){
            throw new ExcepcionSolicitudInvalida("dir es requerido");
        }

        // upsert: si existe (actor=A, objetivo=B), actualiza dirección; si no, crea.
        Optional<SwipeEntity> existente = swipeRepo.findByActorAndObjetivo(A, B);
        if (existente.isPresent()) {
            SwipeEntity s = existente.get();
            s.setDir(D);
            swipeRepo.save(s);
        } else {
            swipeRepo.save(new SwipeEntity(A, B, D));
        }
    }

    // Devuelve la dirección del swipe de ActorId hacia ObjetivoId (normalizados)
    private String obtenerDireccion(String ActorId,String ObejtivoId){
        String A = norm(ActorId);
        String B = norm(ObejtivoId);
        return swipeRepo.findByActorAndObjetivo(A, B).map(SwipeEntity::getDir).orElse(null);
    }

    // Procesa el swipe y, si hay reciprocidad positiva, crea/retorna matchId (idempotente)
    @Transactional
    public String procesarSwipeYQuizasMatch(String actorId, String targetId, String dir) {
        guardarSwipe(actorId, targetId, dir);

        if (!esPositivo(dir)) return null;

        // busca el contrario
        String retorno = obtenerDireccion(targetId, actorId);
        if (retorno != null && esPositivo(retorno)) {
            String id = MatchId(actorId, targetId);

            // ordenar usuarios para guardar siempre igual
            String A = norm(actorId);
            String B = norm(targetId);
            String usuarioA = (A.compareTo(B) < 0) ? A : B;
            String usuarioB = (A.compareTo(B) < 0) ? B : A;

            // idempotente: si ya existe no duplica
            if (!matchRepo.existsByUsuarioAAndUsuarioB(usuarioA, usuarioB)) {
                matchRepo.save(new MatchEntity(usuarioA, usuarioB, "activo"));
            }
            return id;
        }
        return null;
    }

    @Transactional(readOnly = true)
    public MatchDto obtenerMatch(String uidA, String uidB) {
        String A = norm(uidA);
        String B = norm(uidB);
        String usuarioA = (A.compareTo(B) < 0) ? A : B;
        String usuarioB = (A.compareTo(B) < 0) ? B : A;

        return matchRepo.findByUsuarioAAndUsuarioB(usuarioA, usuarioB)
                .map(e -> new MatchDto(e.getUsuarioA(), e.getUsuarioB(), e.getEstado()))
                .orElse(null);
    }
}

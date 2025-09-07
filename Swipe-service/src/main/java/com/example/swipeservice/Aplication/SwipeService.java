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

    private static String swipeId(String ActorId,String ObejtivoId){
        return ObejtivoId+"-"+ActorId;
    }

    private static String MatchId(String a,String b){
        return (a.compareTo(b)<0)? a+"_"+b :b+"_"+a;
    }

    public static boolean esPositivo(String dir){
        if(dir==null)return false;
        String d = dir.toLowerCase();
        return d.equals("right")||d.equals("like");
    }

    public void guardarSwipe(String ActorId,String ObejtivoId,String dir){
        if(ActorId ==null||ActorId.isEmpty() || ObejtivoId==null||ObejtivoId.isEmpty()){
            System.out.println("Actores requeridos");
        };
        if(dir==null||dir.isEmpty()){
            System.out.println("Direcciones requeridos");
        };
        swipes.put(swipeId(ActorId,ObejtivoId),dir.toLowerCase());
    }

    private String obtenerDireccion(String ActorId,String ObejtivoId){
        return swipes.get(swipeId(ActorId,ObejtivoId));
    }


    public String procesarSwipeYQuizasMatch(String actorId, String targetId, String dir) {
        guardarSwipe(actorId, targetId, dir);

        if (!esPositivo(dir)) return null;

        String retorno = obtenerDireccion(targetId, actorId);
        if (esPositivo(retorno)) {
            String id = MatchId(actorId, targetId);
            matches.computeIfAbsent(id, k -> {
                String usuarioA = (actorId.compareTo(targetId) < 0) ? actorId : targetId;
                String usuarioB = (actorId.compareTo(targetId) < 0) ? targetId : actorId;
                MatchDto matchDto = new MatchDto(usuarioA, usuarioB, "activo");
                return matchDto;
            });
            return id;
        }
        return null;
    }

    public MatchDto obtenerMatch(String uidA, String uidB) {
        return matches.get(MatchId(uidA, uidB));
    }
    // Sirve para listar marches
    public java.util.List<String> listarMatchesDe(String userId){
        java.util.List<String> out = new java.util.ArrayList<>();
        for (var e : swipes.entrySet()){
            String k = e.getKey(); String v = e.getValue();         // k = "A->B", v = "like"|"nolike"
            if (v != null && v.equalsIgnoreCase("like")){
                int i = k.indexOf("->");
                if (i>0){
                    String a = k.substring(0,i);
                    String b = k.substring(i+2);
                    if (a.equals(userId)){
                        String back = swipes.get(b + "->" + a);      // ¿B también dio like a A?
                        if (back != null && back.equalsIgnoreCase("like")){
                            out.add(b);
                        }
                    }
                }
            }
        }
        return out;
    }


    

}

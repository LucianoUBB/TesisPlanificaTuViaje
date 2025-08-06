package com.luciano.tesisplanificatuviaje.api;

import com.luciano.tesisplanificatuviaje.models.Usuario;
import com.luciano.tesisplanificatuviaje.models.Recomendacion;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BackendService {
    
    // 👤 Endpoints de usuario
    @POST("usuarios/registro")
    Call<Usuario> registrarUsuario(@Body Usuario usuario);
    
    @POST("usuarios/login") 
    Call<Usuario> loginUsuario(@Body Usuario usuario);
    
    @GET("usuarios/{id}")
    Call<Usuario> obtenerUsuario(@Path("id") String userId);
    
    // 🎯 Endpoints de recomendaciones
    @GET("recomendaciones")
    Call<List<Recomendacion>> obtenerRecomendaciones(@Query("userId") String userId);
    
    @POST("recomendaciones")
    Call<Recomendacion> crearRecomendacion(@Body Recomendacion recomendacion);
    
    // 🤖 Endpoint para chatbot (si tienes uno en tu backend)
    @POST("chatbot/mensaje")
    Call<MessageResponse> enviarMensajeChatbot(@Body MessageRequest mensaje);
    
    // 📍 Endpoints de lugares/rutas
    @GET("lugares")
    Call<List<Object>> obtenerLugares(@Query("tipo") String tipo, @Query("ubicacion") String ubicacion);
    
    @GET("rutas/{userId}")
    Call<List<Object>> obtenerRutas(@Path("userId") String userId);
}


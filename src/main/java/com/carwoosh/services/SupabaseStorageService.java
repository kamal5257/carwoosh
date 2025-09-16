package com.carwoosh.services;

import java.io.IOException;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SupabaseStorageService {
	
    private String supabaseUrl="https://bjzmizunuyepnszcplti.supabase.co";
    private String supabaseKey="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImJqem1penVudXllcG5zemNwbHRpIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc1ODAyMjc1NCwiZXhwIjoyMDczNTk4NzU0fQ.W9NC4jx-_otCjJJCzn0PAe6OxX6NWxtXuq00CNQtoOo";
    private String bucket = "avatar";
    		
    private final OkHttpClient client = new OkHttpClient();

    public String uploadProfileImage(byte[] fileBytes, String originalFileName, String contentType) throws IOException {
        String fileName = UUID.randomUUID() + "-" + originalFileName;
        String uploadUrl = supabaseUrl + "/storage/v1/object/" + bucket + "/" + fileName;

        RequestBody body = RequestBody.create(fileBytes, MediaType.parse(contentType));

        System.out.println(" SUPASE #######################################  "+supabaseUrl+"\n"+supabaseKey+"\n"+bucket);
        
        
        Request request = new Request.Builder()
                .url(uploadUrl)
                .put(body)
                .addHeader("Authorization", "Bearer " + supabaseKey)
                .addHeader("Content-Type", contentType)
                .addHeader("x-upsert", "true")
                .addHeader("Prefer", "return=representation")
                .build();

        try (Response response = client.newCall(request).execute()) {
        	String responseBody = response.body() != null ? response.body().string() : "";
            if (!response.isSuccessful()) {
            	 System.out.println("Supabase upload failed: " + response.code() + " " + response.message());
                 System.out.println("Response body: " + responseBody);
                 throw new IOException("Failed to upload to Supabase Storage");
            }

            // ✅ Public URL for the uploaded file
            return supabaseUrl + "/storage/v1/object/public/" + bucket + "/" + fileName;
        }
    }
}

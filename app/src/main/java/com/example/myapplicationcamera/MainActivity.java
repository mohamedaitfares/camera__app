package com.example.myapplicationcamera;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.Toast;
import android.Manifest;
import android.content.Intent;
import android.provider.MediaStore;
import android.graphics.Bitmap;



import java.io.ByteArrayOutputStream;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.io.*;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.util.Base64;

public class MainActivity extends AppCompatActivity {

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    ImageView selectedImage;
    Button cameraBtn, galleryBtn, sendBtn;

    Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        selectedImage = findViewById(R.id.displayImageView);
        cameraBtn = findViewById(R.id.cameraBtn);
        galleryBtn = findViewById(R.id.galleryBtn);
        sendBtn = findViewById(R.id.sendBtn);

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askCameraPermission();
            }
        });
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Gallery Btn is Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (image != null) {
                    try {
                        Toast.makeText(MainActivity.this, "Send Btn is Clicked", Toast.LENGTH_SHORT).show();
                        // Create the URI for the HTTP request
                        URI uri = new URI("https://prod-46.westeurope.logic.azure.com:443/workflows/526ba988e78645309b844e561b7c74bb/triggers/manual/paths/invoke?api-version=2016-06-01&sp=%2Ftriggers%2Fmanual%2Frun&sv=1.0&sig=PASx0Qif9CXiWd9M1TWxtAVRzngWemTungHpR93iuoQ");

                        // Convert URI to URL
                        URL url = uri.toURL();

                        // Open a connection to the URL
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        Toast.makeText(MainActivity.this, "Connecting", Toast.LENGTH_SHORT).show();

                        // Set the request method to POST
                        connection.setRequestMethod("POST");

                        // Set the content type
                        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

                        // Enable input/output streams
                        connection.setDoOutput(true);

                        // Convert the captured image to a base64-encoded string
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] imageBytes = baos.toByteArray();
                        String base64Image = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            base64Image = Base64.getEncoder().encodeToString(imageBytes);
                            Toast.makeText(MainActivity.this, "converted", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(MainActivity.this, "hohohohohohohoho", Toast.LENGTH_SHORT).show();
                        }

                        // Define your message data as a JSON string with the base64-encoded image
                        String jsonPayload = "{\"image\":\"" + base64Image + "\"}";
                        // Define your message data as a JSON string
                        String message = "{\"message\":\"Hello, Power Automate!\"}";


                        // Write the JSON payload to the output stream
                        try (OutputStream os = connection.getOutputStream()) {
                            Toast.makeText(MainActivity.this, "sending...", Toast.LENGTH_SHORT).show();
                            byte[] input = message.getBytes(StandardCharsets.UTF_8);
                            os.write(input, 0, input.length);
                        }

                        // Get the HTTP response code
                        int responseCode = connection.getResponseCode();
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            // Handle success (HTTP 200)
                            // You can perform actions here based on a successful response.
                        } else {
                            // Handle other response codes if needed
                        }

                        // Close the connection
                        connection.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "No image is selected", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void askCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        } else {
            openCamera();
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera Permission is Required to Use camera", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (data != null) {
                image = (Bitmap) data.getExtras().get("data");
                selectedImage.setImageBitmap(image);
            } else {
                // Handle the case where data is null (e.g., user canceled)
                Toast.makeText(this, "Camera operation canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

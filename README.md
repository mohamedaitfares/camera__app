# Android Camera and SharePoint Image Upload App

This Android Studio application allows users to capture images using the device's camera or select images from the gallery. It then enables users to upload these images to a SharePoint site using HTTP POST requests.

## Getting Started

### Prerequisites

Before you start using this application, ensure that you have the following:

- Android Studio installed on your development machine.
- An Android device or emulator to run the application.

### Clone the Repository

1. Clone this repository to your local machine.

   ```bash
   git clone https://github.com/your-username/your-repository.git
   ```

2. Open the project in Android Studio.

## Usage

1. Launch the application on your Android device or emulator.

2. Grant camera permission when prompted. This permission is required to use the camera for capturing images.

3. Use the following buttons for different actions:

   - **Camera Button (`cameraBtn`):** Click this button to open the device's camera and capture a photo. The captured image will be displayed on the screen.

   - **Gallery Button (`galleryBtn`):** This button is currently a placeholder and does not perform any action. You can implement image selection from the gallery if needed.

   - **Send Button (`sendBtn`):** Click this button to upload the captured image to a SharePoint site using an HTTP POST request.

4. After clicking the "Send" button, the application will encode the image as a base64 string and send it to the specified SharePoint site using an HTTP POST request.

5. You can view the response code in the `onResponse` method and handle it accordingly based on your requirements.

## SharePoint Configuration

To configure SharePoint integration, you need to update the URI in the `MainActivity.java` file. Replace the existing URI with the SharePoint API endpoint where you want to upload the images.

```java
URI uri = new URI("https://your-sharepoint-site.com/your-api-endpoint");
```


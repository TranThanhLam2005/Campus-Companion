import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.campuscompanion.generalUi.ButtonUI
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.database.FirebaseDatabase
import java.io.File

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocketScreen(navController: NavController) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isPhotoTaken by remember { mutableStateOf(false) }
    var commentText by remember { mutableStateOf("") }
    val imageCapture = remember { ImageCapture.Builder().build() }
    var lensFacing by remember { mutableStateOf(CameraSelector.LENS_FACING_BACK) }
    val cameraSelector = remember { CameraSelector.Builder().requireLensFacing(lensFacing).build() }
    var isUploading by remember { mutableStateOf(false) }
    var uploadProgress by remember { mutableStateOf(0f) }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let {
            imageUri = it
            isPhotoTaken = true
        }
    }

    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }

    LaunchedEffect(lensFacing) {
        val cameraProvider = cameraProviderFuture.get()
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        try {
            cameraProvider.unbindAll()
            val newSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                newSelector,
                preview,
                imageCapture
            )
        } catch (e: Exception) {
            Log.e("CameraX", "Camera binding failed", e)
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .align(Alignment.Center)
        ) {
            if (!isPhotoTaken) {
                AndroidView(
                    factory = { previewView },
                    modifier = Modifier
                        .fillMaxSize()
                )
            } else {
                imageUri?.let {
                    Image(
                        painter = rememberAsyncImagePainter(it),
                        contentDescription = "Captured",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().height(420.dp)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(top = 50.dp)
            ) {
                Text(
                    "Anonymous",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.Center),
                )
                if (isPhotoTaken) {
                    IconButton(
                        enabled = !isUploading,
                        onClick = {
                            imageUri?.let { uri ->
                                isUploading = true
                                uploadProgress = 0f

                                MediaManager.get().upload(uri)
                                    .unsigned("upload") // Replace with your preset
                                    .callback(object : UploadCallback {
                                        override fun onStart(requestId: String?) {
                                            Log.d("Upload", "Started")
                                        }

                                        override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
                                            uploadProgress = bytes / totalBytes.toFloat()
                                        }

                                        override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                                            val uploadedUrl = resultData?.get("secure_url") as? String
                                            //sendToFirebase(uploadedUrl ?: "", commentText)
                                            isUploading = false
                                            imageUri = null
                                            isPhotoTaken = false
                                        }

                                        override fun onError(requestId: String?, error: ErrorInfo?) {
                                            Log.e("Upload", "Error: ${error?.description}")
                                            isUploading = false
                                        }

                                        override fun onReschedule(requestId: String?, error: ErrorInfo?) {
                                            isUploading = false
                                        }
                                    })
                                    .dispatch(context)
                            }
                        },
                        modifier = Modifier.align(Alignment.TopEnd),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send",
                            modifier = Modifier.size(30.dp),
                            tint = if (isUploading) Color.Gray else Color.White
                        )
                    }
                }
            }
            if (isUploading) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LinearProgressIndicator(
                        progress = uploadProgress,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "Uploading... ${(uploadProgress * 100).toInt()}%",
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                if (isPhotoTaken) {
                    OutlinedTextField(
                        value = commentText,
                        onValueChange = { commentText = it },
                        placeholder = { Text("Send Messages...") },
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .fillMaxWidth(),
                        textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.DarkGray,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent
                        )
                    )
                }
                Spacer(modifier = Modifier.height(26.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 86.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    IconButton(onClick = {
                        cameraLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }) {
                        Icon(Icons.Default.Image, contentDescription = "Pick Image", Modifier.size(40.dp), tint = Color.White)
                    }
                    IconButton(onClick = {
                        val file = File(
                            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                            "IMG_${System.currentTimeMillis()}.jpg"
                        )
                        val uri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.fileprovider",
                            file
                        )

                        val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
                        imageCapture.takePicture(
                            outputOptions,
                            ContextCompat.getMainExecutor(context),
                            object : ImageCapture.OnImageSavedCallback {
                                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                                    imageUri = uri
                                    isPhotoTaken = true
                                }

                                override fun onError(exception: ImageCaptureException) {
                                    Toast.makeText(context, "Capture failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                    }) {
                        Icon(Icons.Outlined.Circle, contentDescription = "Take Photo",Modifier.size(40.dp), tint = Color.White)
                    }
                    IconButton(onClick = {
                        lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK) {
                            CameraSelector.LENS_FACING_FRONT
                        } else {
                            CameraSelector.LENS_FACING_BACK
                        }
                    }) {
                        Icon(
                            Icons.Default.Cached, // use "Cached" icon to represent flip
                            contentDescription = "Switch Camera",
                            modifier = Modifier.size(40.dp),
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}



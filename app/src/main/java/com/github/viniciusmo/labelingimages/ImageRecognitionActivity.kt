package com.github.viniciusmo.labelingimages

import android.Manifest
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import io.fotoapparat.Fotoapparat
import kotlinx.android.synthetic.main.activity_image_recognition.*

class ImageRecognitionActivity : AppCompatActivity(), PermissionListener {

    private lateinit var fotoapparat: Fotoapparat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_recognition)
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(this).check()
        fabTakePicture.setOnClickListener {
            takePicture()
        }
    }

    private fun takePicture() {
        val photoResult = fotoapparat.takePicture()
        photoResult
                .toBitmap()
                .whenAvailable { bitmapPhoto ->
                    doImageRecognition(bitmapPhoto!!.bitmap)
                }
    }

    private fun doImageRecognition(bitmap: Bitmap) {
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val detector = FirebaseVision
                .getInstance()
                .visionLabelDetector
        detector.detectInImage(image)
                .addOnSuccessListener {
                    val labels = it.map { ImageLabel(it.label, it.confidence) }.toList()
                    navigateToListImageResults(labels)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Não foi possível realizar essa operação", Toast.LENGTH_SHORT).show()
                }
    }

    private fun navigateToListImageResults(labels: List<ImageLabel>) {
        val intent = ListImageLabelsActivity.callingIntent(this, ArrayList(labels))
        startActivity(intent)
    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        fotoapparat = Fotoapparat(this, cameraView)
        fotoapparat.start()
    }

    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {

    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        Toast.makeText(this, "Sem essa permissão não é possível continuar.", Toast.LENGTH_SHORT).show()
    }
}

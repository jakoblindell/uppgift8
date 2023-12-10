package se.magictechnology.intromlkit.ui

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val mutableSharedFlow = MutableStateFlow<String?>(null)
    val sharedFlow = mutableSharedFlow.asSharedFlow()

    fun runTextRecognition(selectedImage: Bitmap) {
        val image = InputImage.fromBitmap(selectedImage, 0)
        val textRecognizerOptions = TextRecognizerOptions.Builder().build()
        val recognizer = TextRecognition.getClient(textRecognizerOptions)
        recognizer.process(image)
            .addOnSuccessListener { texts ->
                processTextRecognitionResult(texts)
            }
            .addOnFailureListener { e -> // Task failed with an exception
                e.printStackTrace()
            }
    }

    private fun processTextRecognitionResult(texts: Text) {
        val blocks: List<Text.TextBlock> = texts.textBlocks
        if (blocks.isEmpty()) {
            return
        }
        val list = mutableListOf<String>()
        for (i in blocks.indices) {
            val lines: List<Text.Line> = blocks[i].lines
            for (j in lines.indices) {
                val elements: List<Text.Element> = lines[j].elements
                for (k in elements.indices) {
                    list.add(elements[k].text)
                }

            }
        }
        viewModelScope.launch {
            mutableSharedFlow.emit(list.joinToString(" "))
        }
    }
}
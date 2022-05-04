//package com.example.mymanager;
//
//import android.os.Bundle;
//import android.util.Log;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import org.tensorflow.lite.DataType;
//import org.tensorflow.lite.Interpreter;
//import org.tensorflow.lite.support.common.FileUtil;
//import org.tensorflow.lite.support.image.ImageProcessor;
//import org.tensorflow.lite.support.image.TensorImage;
//import org.tensorflow.lite.support.image.ops.ResizeOp;
//import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
//import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
//
//import java.io.IOException;
//import java.nio.MappedByteBuffer;
//
//public class tensor2Activity  extends AppCompatActivity {
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_camera);
//    }
//
//
//    // Initialization code
//// Create an ImageProcessor with all ops required. For more ops, please
//// refer to the ImageProcessor Architecture section in this README.
//    ImageProcessor imageProcessor =
//            new ImageProcessor.Builder()
//                    .add(new ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
//                    .build();
//
//    // Create a TensorImage object. This creates the tensor of the corresponding
//// tensor type (uint8 in this case) that the TensorFlow Lite interpreter needs.
//    TensorImage tImage = new TensorImage(DataType.UINT8);
//
//// Analysis code for every frame
//// Preprocess the image
////tImage.load(bitmap);
//    //tImage = imageProcessor.process(tImage);
//
//
//    // Create a container for the result and specify that this is a quantized model.
//// Hence, the 'DataType' is defined as UINT8 (8-bit unsigned integer)
//    TensorBuffer probabilityBuffer =
//            TensorBuffer.createFixedSize(new int[]{1, 1001}, DataType.UINT8);
//
//
//// Initialise the model
////try{
////        MappedByteBuffer tfliteModel
////                = FileUtil.loadMappedFile(activity,
////                "output224.tflite");
////        Interpreter tflite = new Interpreter(tfliteModel)
////    } catch (IOException e){
////        Log.e("tfliteSupport", "Error reading model", e);
////    }
////
////// Running inference
////if(null != tflite) {
////        tflite.run(tImage.getBuffer(), probabilityBuffer.getBuffer());
////    }
////
////}

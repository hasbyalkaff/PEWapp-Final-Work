package final_work.hasby.pewapp;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by Hasby on 10-Mar-19.
 */

public class CameraHandler implements CameraBridgeViewBase.CvCameraViewListener2 {
    private String TAG = "Camera Handler Class";
    private Context context;

    private CameraBridgeViewBase mCameraView;
    private CascadeClassifier mDetection;

    private Mat grayscaleImage;
    private int absoluteFaceSize;

    public void setCamera(CameraBridgeViewBase mCamera){
        mCamera.setVisibility(SurfaceView.VISIBLE);
        mCamera.setCvCameraViewListener(this);
        this.mCameraView = mCamera;
    }
    private void open_camera(){}
    private void get_moedl(){}
    private void detect_face(){}
    private void show_expression(){}

    public CameraHandler(Context context){
        this.context = context;
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(context) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    initializeDepedencies();
                } break;
                default: {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    private void initializeDepedencies(){
        try{
            InputStream is = context.getResources().openRawResource(R.raw.haarcascade_frontalface_alt);
            File cascadeDir = context.getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFile = new File(cascadeDir, "haarcascade_frontalface_alt.xml");
            FileOutputStream os = new FileOutputStream(mCascadeFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while((bytesRead = is.read(buffer)) != -1){
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();

            mDetection = new CascadeClassifier(mCascadeFile.getAbsolutePath());
            mDetection.load(mCascadeFile.getAbsolutePath());
            cascadeDir.delete();
        } catch (Exception e){
            Log.e(TAG, "Error loading cascade");
        }
        mCameraView.enableView();
    }

    public void startCamera(){
        if(OpenCVLoader.initDebug()) {
            Log.d(TAG, "Lib: Using OpenCV Manager");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, context, mLoaderCallback);
        } else {
            Log.d(TAG, "Lib: Using Internal OpenCV Library");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void disableCamera(){
        if(mCameraView != null) mCameraView.disableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        grayscaleImage = new Mat(height, width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        MatOfRect faces = new MatOfRect();

        Imgproc.cvtColor(inputFrame.rgba(), grayscaleImage, Imgproc.COLOR_RGBA2GRAY);
        if(mDetection != null)
            mDetection.detectMultiScale(grayscaleImage, faces, 1.1, 2, 2, new Size(absoluteFaceSize, absoluteFaceSize), new Size());

        Rect[] facesArray = faces.toArray();
        if(facesArray.length>0) {
            Log.i(TAG, "face was found");
            for (int i = 0; i < facesArray.length; i++) {
                Imgproc.rectangle(grayscaleImage, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 255), 3);
            }
        }
        else {
            Log.i(TAG, "face not found");
        }

        return grayscaleImage;
    }
}

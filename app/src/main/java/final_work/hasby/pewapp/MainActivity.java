package final_work.hasby.pewapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;

public class MainActivity extends AppCompatActivity {
    private String TAG = "Main Activity Class";
    private ImageView imageView;
    private CameraHandler cameraHandler;
    private CameraBridgeViewBase cameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate called");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);
        cameraView = (CameraBridgeViewBase)findViewById(R.id.camera_view);
        imageView = (ImageView)findViewById(R.id.imageView);

        cameraHandler = new CameraHandler(this);
        cameraHandler.setCamera(cameraView);
        cameraHandler.setImageView(R.id.imageView);
    }

    @Override
    public void onPause(){
        super.onPause();
        cameraHandler.disableCamera();
    }

    @Override
    public void onResume(){
        super.onResume();
        cameraHandler.startCamera();
    }

    public void onDestroy(){
        super.onDestroy();
        cameraHandler.disableCamera();
    }
}

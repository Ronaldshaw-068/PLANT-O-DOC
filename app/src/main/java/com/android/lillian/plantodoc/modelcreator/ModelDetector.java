package com.android.lillian.plantodoc.modelcreator;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;


import com.huawei.hiai.modelcreatorsdk.singleobjectdetection.PredictBox;
import com.huawei.hiai.modelcreatorsdk.singleobjectdetection.SingleObjectDetection;
import com.huawei.hiai.modelcreatorsdk.singleobjectdetection.SingleObjectDetectionConst;
import com.huawei.hiai.modelcreatorsdk.singleobjectdetection.tool.SingleObjectDetectionTool;

//import com.huawei.agconnect.config.AGConnectServicesConfig;
//import com.huawei.hmf.tasks.Task;
//import com.huawei.hms.mlsdk.common.MLApplication;
//import com.huawei.hms.mlsdk.custom.MLCustomRemoteModel;
//import com.huawei.hms.mlsdk.model.download.MLLocalModelManager;
//import com.huawei.hms.mlsdk.model.download.MLModelDownloadListener;
//import com.huawei.hms.mlsdk.model.download.MLModelDownloadStrategy;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ModelDetector {
    private static final String LOG_TAG = "hiai";
    private static final String MODEL_NAME = "objectdetection/model_name.mc";
    private SingleObjectDetection detector;
    private Context context;

    public ModelDetector(final Context context) throws Exception {
        this.context = context;
        File resDir = this.context.getFilesDir();

        loadModelFromAssets();
    }


    public void loadModelFromAssets() throws Exception {
        detector = new SingleObjectDetection(context, MODEL_NAME, 4);
        String resFileName = null;
        String runMode = null;
        if(detector.isSupportNpuPhone()) {
            resFileName = "single_object_detection_" + SingleObjectDetectionTool.getDDkVersion() + "_" + detector.getModelVersion() + "_base.mc";
            runMode = SingleObjectDetection.NPU_MODE;
        } else {
            resFileName = "single_object_detection_" + SingleObjectDetectionConst.DDK000 + "_" + detector.getModelVersion() + "_base.mc";
            runMode = SingleObjectDetection.CPU_MODE;
        }
        if(!this.containFileInAssets(resFileName)) {
            Log.e(LOG_TAG, "you need to get resFiles and put them to your app assets, please read annotation in codes");
            Toast.makeText(context, "you need to get resFiles and put them to your app assets, please read annotation in codes", Toast.LENGTH_LONG).show();
            return;
        }
        InputStream inputStream = context.getAssets().open(resFileName);
        if(detector.initFromResStream(runMode, inputStream)) {   //NPU_MODE only suport huawei android phone with NPU
            Log.d(LOG_TAG, "model from local assets init ok");
        } else {
            Log.d(LOG_TAG, "model from local assets init fail");
        }
    }


    public boolean isReady() {
        return this.detector.isInitOk();
    }

    public List<PredictBox> detect(Bitmap imageBitmap) {
        if (!this.detector.isInitOk()) {
            Log.e(LOG_TAG, "the detector is not init ok");
            Toast.makeText(this.context, "the detector is not init ok", Toast.LENGTH_SHORT).show();
            return null;
        }
        long start = System.currentTimeMillis();
        List<PredictBox> boxs = this.detector.predict(imageBitmap);
        long costTime = System.currentTimeMillis() - start;

        if (boxs == null) {
            Log.e(LOG_TAG, "the detector run error");
            return null;
        }
        for (PredictBox box : boxs) {
            box.extra.put("costTime", costTime);
        }
        return boxs;
    }

    public void release() {
        this.detector.release();
    }

    private boolean containFileInAssets(String fileName) throws IOException {
        for(String tName : this.context.getAssets().list("")) {
            if(tName.equals(fileName)) {
                return true;
            }
        }
        return false;
    }

    private static void sleep(int ts) {
        try {
            Thread.sleep(ts);
        } catch (InterruptedException e) {

        }
    }
}

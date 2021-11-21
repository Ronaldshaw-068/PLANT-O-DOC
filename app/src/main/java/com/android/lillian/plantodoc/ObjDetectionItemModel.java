package com.android.lillian.plantodoc;

import android.graphics.Bitmap;

import com.huawei.hiai.modelcreatorsdk.singleobjectdetection.PredictBox;

import java.util.List;

public class ObjDetectionItemModel {
    private String resultInf;
    private String costTime;
    private Bitmap img;
    private List<PredictBox> boxes;

    public ObjDetectionItemModel(String resultInf, String costTime, Bitmap img, List<PredictBox> boxes) {
        this.resultInf = resultInf;
        this.costTime = costTime;
        this.img = img;
        this.boxes = boxes;
    }

    public String getResultInf() {
        return resultInf;
    }

    public String getCostTime() {
        return costTime;
    }

    public Bitmap getImg() {
        return img;
    }

    public List<PredictBox> getBoxes() {
        return boxes;
    }

}

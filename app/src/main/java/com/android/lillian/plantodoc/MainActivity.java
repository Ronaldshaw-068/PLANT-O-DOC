package com.android.lillian.plantodoc;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import com.huawei.hiai.modelcreatorsdk.singleobjectdetection.PredictBox;
import com.huawei.hms.R;
import com.android.lillian.plantodoc.modelcreator.ModelDetector;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

import static com.android.lillian.plantodoc.MyUtils.GALLERY_REQUEST_CODE;
import static com.android.lillian.plantodoc.MyUtils.IMAGE_CAPTURE_REQUEST_CODE;
import static com.android.lillian.plantodoc.MyUtils.PERMISSION;


public class MainActivity extends AppCompatActivity {

    private List<ObjDetectionItemModel> items;
    private RecyclerView rv;
    private ObjDetectionAdapter adapter;
    private Button btnGallery;
    private Button btnCamera;
    private ModelDetector carDetector;
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_classify);
        initView();
        try {
            carDetector = new ModelDetector(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = MyUtils.getBitMap(this, requestCode, data);

            Log.d("hiai", "width:" + bitmap.getWidth() + "height:" + bitmap.getHeight());
            //Todo: step2 ==> get result
            List<PredictBox> boxs = carDetector.detect(bitmap);
            showResult(bitmap, boxs);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Todo: step3==> release model
        carDetector.release();
    }

    private void showResult(Bitmap bitmap, List<PredictBox> boxs) {
        if (boxs == null || boxs.size() == 0) {
            items.add(new ObjDetectionItemModel(
            "Empty result. no object detected",
            "",
                    bitmap,
                    boxs));
            adapter.notifyDataSetChanged();
            Toast.makeText(MainActivity.this, "Empty result. no object detected", Toast.LENGTH_SHORT).show();
            return;
        }
        PredictBox box = boxs.get(0);
        items.add(new ObjDetectionItemModel(
                predictionToString(box),
                "cost time: " + box.extra.get("costTime") + "ms",
                bitmap,
                boxs));

        adapter.notifyDataSetChanged();
    }

    private String predictionToString(PredictBox box) {
        String.format(box.category);
        switch (box.category)
        {
            case "Apple___Cedar_apple_rust":
                return String.format("Category  :  %s\nTreatment : Safely treat most fungal and bacterial diseases with SERENADE Gardenrust can be controlled by spraying plants with a copper solution (0.5 to 2.0 oz/ gallon of water) at least four times between late August and late October",box.category);
            case "Apple_Apple_scab":
                return String.format("Category : %s\nTreatment : Spread a 3- to 6-inch layer of compost under trees, keeping it away from the trunk, to cover soil and prevent splash dispersal of the fungal spores.For best control, spray liquid copper soap",box.category);
            case "Apple_Black_rot":
                return String.format("Category : %s\nTreatment : During the winter, check for red cankers and remove them by cutting them out or pruning away the affected limbs at least six inches (15 cm.) beyond the wound. Destroy all infected tissue immediately and keep a watchful eye out for new signs of infection." ,box.category);
            case "Corn_(maize)___Northern_Leaf_Blight":
                return String.format("Category : %s\nTreatment : When you grow corn, make sure it does not stay wet for long periods of time. The fungus that causes this infection needs between six and 18 hours of leaf wetness to develop. Plant corn with enough space for airflow and water in the morning so leaves can dry throughout the day.",box.category);
            case "Corn_Blight":
                return String.format("Category : %s\nTreatment : Fungicides. Foliar fungicides may be applied early in the growing season to corn seedlings as a risk-management tool for northern corn leaf blight and other corn diseases, including anthracnose leaf blight and corn eyespot.",box.category);
            case "Corn_Common_Rust":
                return String.format("Category : %s\nTreatment : If the corn begins to show symptoms of infection, immediately spray with a fungicide. The fungicide is most effective when started at the first sign of infection",box.category);
            case "Corn_Gray_Leaf_Spot":
                return String.format("Category : %s\nTreatment : During the growing season, foliar fungicides can be used to manage gray leaf spot outbreaks.",box.category);
            case "Grape___Black_rot":
                return String.format("Category : %s\nTreatment : A mixture of cultural and chemical control practices can manage grape black rot disease caused by Guignardia bidwellii. Cultural control aspects involve the basics in plant care and field sanitation as well as cleanup after an infectious outbreak. Chemical control has a large influence to prevent but not eliminate disease",box.category);
            case "Grape___Esca_(Black_Measles)":
                return String.format("Category : %s\nTreatment : Presently, there are no effective management strategies for measles. Wine grape growers with small vineyards will often have field crews remove infected fruit prior to harvest. Raisins affected by measles will be discarded during harvest or at the packing house",box.category);
            case "Grape___Leaf_blight_(Isariopsis_Leaf_Spot)":
                return String.format("Category : %s\nTreatment : Fungicides sprayed for other diseases in the season may help to reduce this disease.",box.category);
            case "Pepper_bell___Bacterial_spot":
                return String.format("Category : %s\nTreatment : Transplants with symptoms may be removed and destroyed or treated with streptomycinCopper sprays can be used to control bacterial leaf spot, but they are not as effective when used alone on a continuous basis.Beneficial microorganisms containing products, such as Serenade and Sonata, can reduce pepper leaf spot",box.category);
            case "Potato___Early_blight":
                return String.format("Category : %s\nTreatment : Avoid overhead irrigation and allow for sufficient aeration between plants to allow the foliage to dry as quickly as possible",box.category);
            case "Potato___Late_blight":
                return String.format("Category : %s\nTreatment : Apply a copper based fungicide (2 oz/ gallon of water) every 7 days or less, following heavy rain or when the amount of disease is increasing rapidly. If possible, time applications so that at least 12 hours of dry weather follows application.",box.category);
            case "Tomato___Bacterial_spot":
                return String.format("Category : %s\nTreatment : A plant with bacterial spot cannot be cured. Remove symptomatic plants from the field or greenhouse to prevent the spread of bacteria to healthy plants. Burn, bury or hot compost the affected plants",box.category);
            case "Tomato___Early_blight":
                return String.format("Category : %s\nTreatment : Thoroughly spray the plant (bottoms of leaves also) with Bonide Liquid Copper Fungicide concentrate",box.category);
            case "Tomato___Late_blight":
                return String.format("Category : %s\nTreatment : Before disease occurs, apply fungicides at 7-10 day intervals. After disease is detected in your area, apply fungicides at 5-7 day intervals",box.category);
            case "Tomato___Leaf_Mold":
                return String.format("Category : %s\nTreatment : strategy of management is the cultural practices for reducing the disease. It includes adequating row and plant spacing that promote better air circulation through the canopy reducing the humidity; preventing excessive nitrogen on fertilization since nitrogen out of balance enhances foliage disease development; keeping the relatively humidity below 85% (suitable on greenhouse), promote air circulation inside the greenhouse, early planting might to reduce the disease severity and seed treatment with hot water (25 minutes at 122 °F or 50 °C)",box.category);
            case "Tomato___Tomato_mosaic_virus":
                return String.format("Category : %s\nTreatment : Treating mosaic virus is difficult and there are no chemical controls like there are for fungal diseases, although some varieties of tomato are resistant to the disease, and seeds can be bought that are certified disease free. Sanitation is the most important application to practice when controlling tobacco mosaic virus" ,box.category);
            case "Tomato___Tomato_Yellow_Leaf_Curl_Virus":
                return String.format("Category : %s\nTreatment : Currently, the most effective treatments used to control the spread of TYLCV are insecticides and resistant crop varieties.",box.category);
        }
        return String.format("Disease Unknown");
    }

    private void setHeaderView(RecyclerView view) {

        View header = LayoutInflater.from(this).inflate(R.layout.recyclerview_hewader, view, false);

        btnGallery = header.findViewById(R.id.btn_gallery);
        btnCamera = header.findViewById(R.id.btn_camera);

        adapter.setHeaderView(header);
    }

    private void initView() {
        items = new ArrayList<>();

        rv = findViewById(R.id.rv);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv.setLayoutManager(manager);
        rv.addItemDecoration(new PaddingItemDecoration(getResources().getDimensionPixelOffset(R.dimen.card_decoration)));
        adapter = new ObjDetectionAdapter(items);
        rv.setAdapter(adapter);

        setHeaderView(rv);

        btnGallery.setOnClickListener(v -> chooseImageAndClassify());
        btnCamera.setOnClickListener(v -> takePictureAndClassify());

        EasyPermissions.requestPermissions(this, "request for camera and store permission", 0, PERMISSION);

        isPermission(1000, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    protected boolean isPermission(int requestCode, String... permissions) {
        List<String> required = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED
                    && !shouldShowRequestPermissionRationale(permission)) {
                required.add(permission);
            }
        }
        if (required.size() > 0) {
            requestPermissions(required.toArray(new String[required.size()]), requestCode);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void takePictureAndClassify() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, IMAGE_CAPTURE_REQUEST_CODE);
        }
    }

    private void chooseImageAndClassify() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }


}

package com.android.lillian.plantodoc;

import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.huawei.hiai.modelcreatorsdk.singleobjectdetection.PredictBox;
import com.huawei.hiai.modelcreatorsdk.singleobjectdetection.views.MarkImageView;
import com.huawei.hms.R;

import java.util.List;

public class ObjDetectionAdapter extends RecyclerView.Adapter<ObjDetectionAdapter.ObjectDetectionViewHolder> {

    private List<ObjDetectionItemModel> details;

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 2;

    private View headView;

    public void setHeaderView(View headerView) {
        headView = headerView;
        notifyItemInserted(0);
    }

    public ObjDetectionAdapter(List<ObjDetectionItemModel> details) {
        this.details = details;
    }

    @Override
    public int getItemViewType(int position) {
        if (headView == null) {
            return TYPE_NORMAL;
        }
        if (position == 0) {
            return TYPE_HEADER;
        }

        return TYPE_NORMAL;
    }

    @Override
    public ObjectDetectionViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        if (headView != null && viewType == TYPE_HEADER) {
            return new ObjectDetectionViewHolder(headView);
        }

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.classify_item, viewGroup, false);
        ObjectDetectionViewHolder cvh = new ObjectDetectionViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(ObjectDetectionViewHolder holder, int position) {

        if (getItemViewType(position) == TYPE_NORMAL) {
            if (holder instanceof ObjectDetectionViewHolder) {
                final ObjDetectionItemModel itemModel = details.get(position - 1);
                holder.refresh(itemModel);
            }
        } else if (getItemViewType(position) == TYPE_HEADER) {
            return;
        } else {
            return;
        }
    }

    @Override
    public int getItemCount() {
        if (headView == null) {
            return details.size();
        } else {
            return details.size() + 1;
        }
    }

    class ObjectDetectionViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView reusltInfView;
        MarkImageView imageView;
        private boolean isFirst = true;

        ObjectDetectionViewHolder(View itemView) {
            super(itemView);

            if (itemView == headView) {
                return;
            }

            cv = itemView.findViewById(R.id.obj_detection_item);
            reusltInfView = itemView.findViewById(R.id.result_inf);
            imageView = itemView.findViewById(R.id.image_view);
        }

        public void refresh(ObjDetectionItemModel itemModel) {
            if(isFirst) {
                String displayTime = itemModel.getCostTime();
                reusltInfView.setText(itemModel.getResultInf());

                Bitmap bitmap = itemModel.getImg();
                imageView.setImageBitmap(bitmap);
                List<PredictBox> boxes = itemModel.getBoxes();
                if (boxes != null && boxes.size() > 0) {
                    for (int i = 0; i < boxes.size(); i++) {
                        PredictBox box = boxes.get(i);
                        imageView.addMark(box);
                    }
                    imageView.commitMark();
                }
                isFirst = false;
            }
        }
    }
}

package com.softwaregiants.careertinder.adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.softwaregiants.careertinder.R;
import com.softwaregiants.careertinder.models.JobOpeningModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CandidateMatchviewerAdapter extends RecyclerView.Adapter<CandidateMatchviewerAdapter.CandidateMatchviewerViewHolder> {

    private List<JobOpeningModel> jobOpeningModels;
    private OnItemClickListener onItemClickListener;

    FirebaseStorage storage = FirebaseStorage.getInstance();

    public CandidateMatchviewerAdapter(List<JobOpeningModel> jobOpeningModelList){
        this.jobOpeningModels = jobOpeningModelList;
    }

    @NonNull
    @Override
    public CandidateMatchviewerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.candidate_matchviewer_item, parent, false);
        CandidateMatchviewerViewHolder candidateMatchviewerViewHolder = new CandidateMatchviewerViewHolder(itemView, onItemClickListener);
        return candidateMatchviewerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CandidateMatchviewerViewHolder holder, int position) {
        JobOpeningModel jobOpeningModel = jobOpeningModels.get(position);
        holder.jobTitle.setText(jobOpeningModel.getJobTitle());
        holder.placeOfWork.setText(jobOpeningModel.getPlaceOfWork());
        holder.preferredSkill.setText(jobOpeningModel.getSkill1());
        if ( null != jobOpeningModel.getImageUrl() && !jobOpeningModel.getImageUrl().isEmpty()) {
            StorageReference storageRef = storage.getReference(jobOpeningModel.getImageUrl());
            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).fit()
                            .placeholder(R.drawable.image_placeholder)
                            .error(R.drawable.image_placeholder).into(holder.jobOpening);
                }
            });
        } else {
            holder.jobOpening.setImageResource(R.drawable.image_placeholder);
        }
    }

    @Override
    public int getItemCount() {
        return jobOpeningModels.size();
    }


    public static class CandidateMatchviewerViewHolder extends RecyclerView.ViewHolder{

        public ImageView jobOpening;
        public TextView jobTitle;
        public TextView placeOfWork;
        public TextView preferredSkill;

        public CandidateMatchviewerViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            jobOpening = itemView.findViewById(R.id.IVJobOpening);
            jobTitle = itemView.findViewById(R.id.TVJobTitle);
            placeOfWork = itemView.findViewById(R.id.TVPlaceOfWork);
            preferredSkill = itemView.findViewById(R.id.TVPreferredSkill);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            listener.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener = listener;
    }
}

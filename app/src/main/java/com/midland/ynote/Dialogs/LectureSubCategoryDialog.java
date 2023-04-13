package com.midland.ynote.Dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.midland.ynote.R;
import com.midland.ynote.Activities.DocumentMetaData;
import com.midland.ynote.Activities.Next;
import com.midland.ynote.Activities.VideoUploader;
import com.midland.ynote.Adapters.LecSubFieldPagerAdapter;
import com.midland.ynote.Adapters.TagsAdapter;
import com.midland.ynote.Utilities.FilingSystem;
import com.midland.ynote.Utilities.VidUploadPopUp;

public class LectureSubCategoryDialog extends DialogFragment {

    DocUploaderPopUp docUploaderPopUp;
    VidUploadPopUp vidUploadPopUp;
    VideoUploader videoUploader;
    DocumentMetaData documentMetaData;
    Next next;
    TagsAdapter tagsAdapter;

    public LectureSubCategoryDialog(DocumentMetaData documentMetaData, TagsAdapter tagsAdapter) {
        this.documentMetaData = documentMetaData;
        this.tagsAdapter = tagsAdapter;
    }

    public LectureSubCategoryDialog(VideoUploader videoUploader) {
        this.videoUploader = videoUploader;
    }

    public LectureSubCategoryDialog(VidUploadPopUp vidUploadPopUp) {
        this.vidUploadPopUp = vidUploadPopUp;
    }

    public LectureSubCategoryDialog(DocUploaderPopUp docUploaderPopUp) {
        this.docUploaderPopUp = docUploaderPopUp;
    }

    public LectureSubCategoryDialog(Next next, TagsAdapter tagsAdapter) {
        this.next = next;
        this.tagsAdapter = tagsAdapter;
    }
    public LectureSubCategoryDialog(TagsAdapter tagsAdapter) {
        this.tagsAdapter = tagsAdapter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View v = inflater.inflate(R.layout.activity_lecture_sub_category_dialog, container, false);
        final ViewPager subCatViewPager = v.findViewById(R.id.subCategoryViewPager);
        LecSubFieldPagerAdapter subFieldPagerAdapter = new LecSubFieldPagerAdapter(getChildFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, 6);
        subCatViewPager.setAdapter(subFieldPagerAdapter);
        subCatViewPager.setOnClickListener(v1 -> Toast.makeText(getContext(), subCatViewPager.getCurrentItem(), Toast.LENGTH_SHORT).show());

        return v;
    }


    @Override
    public void onDestroyView() {
        if (docUploaderPopUp != null){
            tagsAdapter = new TagsAdapter(getContext(), FilingSystem.Companion.getAllTags());
            tagsAdapter.notifyDataSetChanged();
        }
        super.onDestroyView();
    }


}
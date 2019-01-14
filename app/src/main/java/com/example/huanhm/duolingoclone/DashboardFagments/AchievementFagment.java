package com.example.huanhm.duolingoclone.DashboardFagments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.huanhm.duolingoclone.Activities.LoginActivity;
import com.example.huanhm.duolingoclone.R;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;


@SuppressLint("ValidFragment")
public class AchievementFagment extends Fragment {

    private ImageView imageViewKnowledge, imageViewKillStreak, imageViewDumbdays, imageViewOvertime;
    private CardView cardViewKnowledge, cardViewKillStreak, cardViewDumbdays, cardViewOvertime;
    private Context mContext;

    ShareDialog shareDialog;

    public AchievementFagment(Context context) {
        this.mContext = context;
        shareDialog = new ShareDialog((Activity)mContext);
    }

    public static AchievementFagment getAchievementFagmentInstance(Context context) {
        AchievementFagment achievementFagment = new AchievementFagment(context);
        Bundle args = new Bundle();
        achievementFagment.setArguments(args);
        achievementFagment.setRetainInstance(true);
        return achievementFagment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboard_fragment_achievement,container,false);
        iniView(view);
        setOnClickShareAchievement();
        getUserAchievement();
        return view;
    }

    private void setOnClickShareAchievement() {
        if(LoginActivity.userInfo != null) {
            for (int i = 0; i < LoginActivity.userInfo.getAchievements().size(); ++i) {
                switch (LoginActivity.userInfo.getAchievements().get(i)) {
                    case 1:
                        cardViewKnowledge.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.knowledge_master);
                                shareToFaceBook(bitmap);
                            }
                        });
                        break;
                    case 2:
                        cardViewDumbdays.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.dumbdays);
                                shareToFaceBook(bitmap);
                            }
                        });
                        break;
                    case 3:
                        cardViewKillStreak.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.kill_streak);
                                shareToFaceBook(bitmap);
                            }
                        });
                        break;
                    case 4:
                        cardViewOvertime.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.overtime);
                                shareToFaceBook(bitmap);
                            }
                        });
                }
            }
        }
    }

    private void getUserAchievement() {
        if(LoginActivity.userInfo != null) {
            for (int i = 0; i < LoginActivity.userInfo.getAchievements().size(); ++i) {
                switch (LoginActivity.userInfo.getAchievements().get(i)) {
                    case 1:
                        imageViewKnowledge.setImageResource(R.drawable.knowledge_master);
                        break;
                    case 2:
                        imageViewDumbdays.setImageResource(R.drawable.dumbdays);
                        break;
                    case 3:
                        imageViewKillStreak.setImageResource(R.drawable.kill_streak);
                        break;
                    case 4:
                        imageViewOvertime.setImageResource(R.drawable.overtime);
                }
            }
        }
    }

    private void iniView(View view) {
        imageViewKnowledge = view.findViewById(R.id.iv_award_knowledge_master);
        imageViewKillStreak = view.findViewById(R.id.iv_award_kill_streak);
        imageViewDumbdays = view.findViewById(R.id.iv_award_dumbdays);
        imageViewOvertime = view.findViewById(R.id.iv_award_overtime);

        cardViewKnowledge = view.findViewById(R.id.card_view_knowledge_master_dashboard_achievement);
        cardViewDumbdays = view.findViewById(R.id.card_view_dumbdays_dashboard_achievement);
        cardViewKillStreak = view.findViewById(R.id.card_view_kill_streak_dashboard_achievement);
        cardViewOvertime = view.findViewById(R.id.card_view_overtime_dashboard_achievement);
    }

    private void shareToFaceBook(final Bitmap bitmap){
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(mContext,SweetAlertDialog.NORMAL_TYPE);
        sweetAlertDialog.setTitleText("Share to FaceBook");
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                SharePhoto sharePhoto = new SharePhoto.Builder()
                        .setBitmap(bitmap)
                        .build();

                SharePhotoContent sharePhotoContent = new SharePhotoContent.Builder()
                        .addPhoto(sharePhoto)
                        .build();
                if(shareDialog.canShow(sharePhotoContent)) {
                    shareDialog.show(sharePhotoContent);
                }

            }
        });
        sweetAlertDialog.show();
    }
}

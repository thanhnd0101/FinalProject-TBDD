package com.example.huanhm.duolingoclone.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.drm.DrmStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerTextView;
import com.example.huanhm.duolingoclone.DataModel.Lesson;
import com.example.huanhm.duolingoclone.DataModel.Question;
import com.example.huanhm.duolingoclone.DataModel.Topic;
import com.example.huanhm.duolingoclone.OpenTriviaService.OpenTriviaModel.QuestionModel;
import com.example.huanhm.duolingoclone.OpenTriviaService.OpenTriviaResponse.QuestionResponse;
import com.example.huanhm.duolingoclone.OpenTriviaService.OpenTriviaService;
import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoResponse.QuestionList;
import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoService;
import com.example.huanhm.duolingoclone.R;
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.github.bluzwong.swipeback.SwipeBackActivityHelper;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.lang.reflect.Array;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LessonActivity extends AppCompatActivity {

    public static int OvertimeCount = 0;
    public static int KillStreakCount =0;
    private final int OVERTIME_LIMIT= 5;
    private final int KILL_STREAK_LIMIT = 3;

    private final int QUESTION_AMOUNT = 5;
    private final String SECRETE_KEY ="polyglotto_secretkey";
    SwipeBackActivityHelper helper = new SwipeBackActivityHelper();

    RoundCornerProgressBar progressBar;

    String difficulty;
    int category;
    ArrayList<QuestionModel> questions = new ArrayList<>();
    int currentQuestion = 0;
    int rightAnswerCount = 0;

    RoundCornerTextView topLeft;
    RoundCornerTextView topRight;
    RoundCornerTextView bottomLeft;
    RoundCornerTextView bottomRight;
    ArrayList<RoundCornerTextView> answerTextViews = new ArrayList<>();
    int chosenAnswer = -1;

    Button checkAnswer;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new Slide(Gravity.END));
        getWindow().setExitTransition(new Slide(Gravity.START));

        setContentView(R.layout.activity_lesson);


        helper.setEdgeMode(true)
                .setParallaxMode(true)
                .setParallaxRatio(1)
                .setNeedBackgroundShadow(true)
                .init(this);

        Intent intent = getIntent();
        category = intent.getIntExtra("category", 9);
        difficulty = intent.getStringExtra("difficulty");

        progressBar = findViewById(R.id.progressbar_lesson);
        progressBar.setMax(100.0f);
        progressBar.setProgress(0.0f);

        topLeft = findViewById(R.id.top_left_answer_lesson);
        topRight = findViewById(R.id.top_right_answer_lesson);
        bottomLeft = findViewById(R.id.bottom_left_answer_lesson);
        bottomRight = findViewById(R.id.bottom_right_answer_lesson);
        answerTextViews.add(topLeft);
        answerTextViews.add(topRight);
        answerTextViews.add(bottomLeft);
        answerTextViews.add(bottomRight);

        View.OnTouchListener listener = initAnswerTouchListener();
        for (RoundCornerTextView answer : answerTextViews)
            answer.setOnTouchListener(listener);

        checkAnswer = findViewById(R.id.check_answer_button_lesson);
        checkAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUserAnswer();
            }
        });

        getQuestionList();
    }

    private void inflateQuestionIntoView() {
        QuestionModel q = questions.get(currentQuestion);
        ((RoundCornerTextView) findViewById(R.id.question_lesson)).setText(String.valueOf(q.getQuestion()));
        chosenAnswer = -1;
        setRandomAnswer(q);
        for (int i = 0; i < 4; i++) {
            /*answerTextViews.get(i).setText(q.answers.get(i));*/
            answerTextViews.get(i).setBackground(getDrawable(R.drawable.answer_background));
        }
    }

    private void setRandomAnswer(QuestionModel q) {
        Random r = new Random();
        int rightAnswer_pos = r.nextInt(4) + 1;
        switch (rightAnswer_pos) {
            case 1:
                answerTextViews.get(0).setText(q.getCorrect_answer());
                answerTextViews.get(1).setText(q.getIncorrect_answer().get(1));
                answerTextViews.get(2).setText(q.getIncorrect_answer().get(2));
                answerTextViews.get(3).setText(q.getIncorrect_answer().get(0));
                break;
            case 2:
                answerTextViews.get(0).setText(q.getIncorrect_answer().get(2));
                answerTextViews.get(1).setText(q.getCorrect_answer());
                answerTextViews.get(2).setText(q.getIncorrect_answer().get(1));
                answerTextViews.get(3).setText(q.getIncorrect_answer().get(0));
                break;
            case 3:
                answerTextViews.get(0).setText(q.getIncorrect_answer().get(2));
                answerTextViews.get(2).setText(q.getCorrect_answer());
                answerTextViews.get(1).setText(q.getIncorrect_answer().get(1));
                answerTextViews.get(3).setText(q.getIncorrect_answer().get(0));
                break;
            default:
                answerTextViews.get(0).setText(q.getIncorrect_answer().get(2));
                answerTextViews.get(3).setText(q.getCorrect_answer());
                answerTextViews.get(2).setText(q.getIncorrect_answer().get(1));
                answerTextViews.get(1).setText(q.getIncorrect_answer().get(0));
        }
    }

    private void checkUserAnswer() {
        final SweetAlertDialog dialog = new SweetAlertDialog(this);
        if (chosenAnswer < 0) {
            dialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
            dialog.setTitleText("Chưa chọn đáp án");
            dialog.setContentText("Hãy chọn 1 đáp án");
            dialog.setConfirmText("OK");
            dialog.show();
        } else {
            String userAnswer = answerTextViews.get(chosenAnswer).getText().toString();
            QuestionModel q = questions.get(currentQuestion);
            String correctAnswer = q.getCorrect_answer();
            if (correctAnswer.equals(userAnswer)) {
                rightAnswerCount++;
                dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                dialog.setTitleText("Đáp án đúng");
                dialog.setContentText("Chúc mừng, " + userAnswer + " là đáp án đúng");
                if (currentQuestion != questions.size() - 1) {
                    dialog.setConfirmText("Câu kế tiếp");
                    dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            currentQuestion++;
                            inflateQuestionIntoView();
                            progressBar.setProgress(currentQuestion * 100.0f / questions.size());
                            dialog.cancel();
                        }
                    });
                } else {
                    dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    dialog.setTitleText("Hoàn thành");
                    dialog.setConfirmText("OK");
                    //int exp = (int) (score * rightAnswerCount * 1.0f / questions.size());
                    //dialog.setContentText("Kinh nghiệm nhận được: " + String.valueOf(exp));
                    dialog.setContentText("Số câu trả lời đúng" + String.valueOf(rightAnswerCount));
                    //dialog.getProgressHelper().setProgress(rightAnswerCount * 100.0f / questions.size());
                    dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            helper.finish();
                            dialog.cancel();
                        }
                    });
                }
                dialog.show();
            } else {
                dialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                dialog.setTitleText("Đáp án sai");
                dialog.setContentText("Sai, " + correctAnswer + " mới là đáp án đúng");
                dialog.setConfirmText("Câu kế tiếp");
                if (currentQuestion != questions.size() - 1) {
                    dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            currentQuestion++;
                            inflateQuestionIntoView();
                            progressBar.setProgress(currentQuestion * 100.0f / questions.size());
                            dialog.cancel();
                        }
                    });
                } else {
                    dialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    dialog.setTitleText("Hoàn thành");
                    dialog.setConfirmText("OK");
                    dialog.setContentText("Số câu trả lời đúng" + String.valueOf(rightAnswerCount));
                    dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            if(LoginActivity.userInfo!= null) {
                                checkUserAchievement();
                            }
                            helper.finish();
                            dialog.cancel();
                        }
                    });
                }
                dialog.show();
            }
        }
    }

    private void checkUserAchievement() {
        if(LoginActivity.userInfo != null) {
            ++LessonActivity.OvertimeCount;
            if(LessonActivity.OvertimeCount == OVERTIME_LIMIT &&
                    !LoginActivity.userInfo.getAchievements().contains(4)){
                getAchievement(4);
            }
            else if (rightAnswerCount == QUESTION_AMOUNT && difficulty.equals("hard") &&
                    !LoginActivity.userInfo.getAchievements().contains(1)) {
                getAchievement(1);
            }else if(rightAnswerCount == QUESTION_AMOUNT){
                ++KillStreakCount;
                if(KillStreakCount == KILL_STREAK_LIMIT){
                    getAchievement(3);
                }
            }else if (rightAnswerCount == 0){
                getAchievement(2);
            }
        }
    }
    private String status;
    private void getAchievement(int achievementid) {
        String encodedString = generateHashWithHmac256(
                LoginActivity.profile.getId()+"|"+SECRETE_KEY+"|"+String.valueOf(achievementid),SECRETE_KEY);
        Call<String> call = PolyglottoService.postUserAchievement(LoginActivity.accessToken.getToken(),
                LoginActivity.profile.getId(),achievementid,encodedString);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if(response.isSuccessful()){
                    status = response.body();
                    if(Objects.requireNonNull(status).equals("success")){
                        Toast.makeText(getApplicationContext(), "Modify successfuly", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Modify failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "Modify failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private View.OnTouchListener initAnswerTouchListener() {
        return new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                for (int i = 0; i < answerTextViews.size(); i++) {
                    RoundCornerTextView answer = answerTextViews.get(i);
                    if (v.getId() == answer.getId()) {
                        answer.setBackground(getDrawable(R.drawable.answer_chosen_background));
                        chosenAnswer = i;
                    } else {
                        answer.setBackground(getDrawable(R.drawable.answer_background));
                    }
                }

                return false;
            }
        };
    }

    private void getQuestionList() {
        final SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("Lấy danh sách câu hỏi");
        dialog.setCancelable(false);
        dialog.show();

        Call<QuestionResponse> call = OpenTriviaService.getQuestionsWithAmountOf(QUESTION_AMOUNT,category,difficulty,"multiple");
        call.enqueue(new Callback<QuestionResponse>() {
            @Override
            public void onResponse(@NonNull Call<QuestionResponse> call, @NonNull Response<QuestionResponse> response) {
                if (response.body() == null) {
                    dialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    dialog.setTitleText("Có sự cố xảy ra, vui lòng thử lại");
                } else if (response.body().getQuestionModelList().isEmpty()) {
                    dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    dialog.setTitleText("Không có câu hỏi");
                    dialog.setConfirmText("Quay về");
                    dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            helper.finish();
                            dialog.cancel();
                        }
                    });
                } else {
                    dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    dialog.setTitleText("Thành công");
                    questions = response.body().getQuestionModelList();

                    inflateQuestionIntoView();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QuestionResponse> call, @NonNull Throwable t) {
                dialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                dialog.setTitleText("Có sự cố xảy ra, vui lòng thử lại");

                // use mock data
                for (int i = 0; i < 5; i++) {
                    List<String> incorrect = Arrays.asList("Đáp án A","Đáp án B","Đáp án C","Đáp án D");
                    QuestionModel q = new QuestionModel("easy", "Có sự cố xảy ra", "Chịu",incorrect);
                    questions.add(q);
                }

                inflateQuestionIntoView();
            }
        });
    }

    @Override
    public void onBackPressed() {
        helper.finish();
    }
    private String generateHashWithHmac256(String message, String key) {
        String messageDigest = "";
        try {
            final String hashingAlgorithm = "HmacSHA256";
            byte[] bytes = hmac(hashingAlgorithm, key.getBytes(), message.getBytes());
            messageDigest = bytesToHex(bytes);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return messageDigest;
    }

    public static byte[] hmac(String algorithm, byte[] key, byte[] message) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(key, algorithm));
        return mac.doFinal(message);
    }

    public static String bytesToHex(byte[] bytes) {
        final char[] hexArray = "0123456789abcdef".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0, v; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}

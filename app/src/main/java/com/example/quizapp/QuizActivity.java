package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class QuizActivity extends AppCompatActivity {
    private static final long COUNTDOWN_IN_MILLIS = 300000;
    private final int nrOfQuest = 5;
    private Question question1 = new Question("Qual è il tag per i link ipertestuali?","<body>", "<a","Risposta1c",1);
    private Question question2 = new Question("Qual è'","Risposta2a", "Risposta2b","Risposta2c",1);
    private Question question3 = new Question("Domanda3","Risposta3a", "Risposta3b","Risposta3c",1);
    private Question question4 = new Question("Domanda4","Risposta4a", "Risposta4b","Risposta4c",1);
    private Question question5 = new Question("Domanda5","Risposta5a", "Risposta5b","Risposta5c",1);
    private Question[] questions = new Question[]{question1, question2, question3, question4, question5};
    private int k = 0;
    private int ptperquest = 20;
    private boolean answered;
    private int score = 0;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private TextView textViewCountDown;
    private boolean testCompleted = false;

    private Button buttonConfirmNext;

    private TextView question;
    private RadioButton option1;
    private RadioButton option2;
    private RadioButton option3;

    private TextView questionCount;

    private TextView scoreText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        question = findViewById(R.id.text_view_question);

        option1 = findViewById(R.id.radio_button1);
        option2 = findViewById(R.id.radio_button2);
        option3 = findViewById(R.id.radio_button3);

        questionCount = findViewById(R.id.text_view_question_count);

        scoreText = findViewById(R.id.text_view_score);

        textViewCountDown = findViewById(R.id.text_view_countdown);

        if (k==0)
        {
            setNrOfQuest();
            changeQuestion();

        }
        timeLeftInMillis = COUNTDOWN_IN_MILLIS;
        startCountDown();



        buttonConfirmNext = findViewById(R.id.button_confirm_next);
        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                confirmAnswer();

                if(answered==true)
                {

                    k++;
                    changeQuestion();
                    setNrOfQuest();
                    setScore();
                }
                if(k>=nrOfQuest)
                {
                    passToResult();
                    testCompleted = true;
                }

            }
        });
    }
    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                if(!testCompleted)
                {
                    passToResult();

                }

            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        textViewCountDown.setText(timeFormatted);
        Log.println(seconds, "elapsed time", String.valueOf(seconds));

        if (timeLeftInMillis < 10000) {
            textViewCountDown.setTextColor(Color.RED);
        } else {
            textViewCountDown.setTextColor(Color.BLUE);
        }



    }

    private void confirmAnswer() {

            answered = false;

            if (option1.isChecked() || option2.isChecked() || option3.isChecked()) {
                checkAnswer();
                answered = true;
            } else {
                Toast.makeText(QuizActivity.this, "Scegliere una risposta", Toast.LENGTH_SHORT).show();
            }


    }
    private void checkAnswer()
    {

        int rightQuestion = questions[k].getAnswerNr();
        int answered = 0;

        if(option1.isChecked())
        {
            answered = 1;
        }
        else if(option2.isChecked())
        {
            answered = 2;
        }
        else if(option3.isChecked())
        {
            answered = 3;
        }

        if(answered == rightQuestion)
        {
            score  = score+ptperquest;
        }
        else
        {
            score = score + 0;
        }
    }
    private void changeQuestion() {



        if (k< nrOfQuest)
        {

            question.setText(questions[k].getQuestion());
            option1.setText(questions[k].getOption1());
            option2.setText(questions[k].getOption2());
            option2.setText(questions[k].getOption2());
            option3.setText(questions[k].getOption3());

        }


    }

    private void setNrOfQuest() {

        if (k< nrOfQuest)
        {
            questionCount.setText("Domanda: "+(k+1)+"/"+nrOfQuest);

        }

    }

    private void setScore() {

        scoreText.setText("Punteggio: "+String.valueOf(score));

    }

    private void passToResult()
    {
        Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
        String scr = String.valueOf(score);
        intent.putExtra("score",scr);
        startActivity(intent);
    }


}
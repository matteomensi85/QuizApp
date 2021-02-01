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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {
    private static final long COUNTDOWN_IN_MILLIS = 300000;
    private final int nrOfQuest = 5;
    private Question question1 = new Question("Qual è il tag per i link ipertestuali?","<body>", "<a>","<b>",2);
    private Question question2 = new Question("Qual è il tag di apertura di una riga di una tabella?","<table>", "<th>","<tr>",3);
    private Question question3 = new Question("Che tipo di proprietà CSS permette a un div di affiancarsi ad un altro?","float", "line-height","background-repeat",1);
    private Question question4 = new Question("Quale simbolo rappresenta il selettore di ID?","@", "#",".",2);
    private Question question5 = new Question("Che cosa succede con il seguente codice:" +"\n"+"<script src=\"https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js\" crossorigin=\"anonymous\">"+"\n"+"</script>\n","appare un link", "si importa la libreria Jquery","si usa un CSS esterno",2);
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

    private RadioGroup radioAnswers;
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

        radioAnswers = findViewById(R.id.radio_group);
        option1 = findViewById(R.id.radio_button1);
        option2 = findViewById(R.id.radio_button2);
        option3 = findViewById(R.id.radio_button3);

        questionCount = findViewById(R.id.text_view_question_count);

        scoreText = findViewById(R.id.text_view_score);

        textViewCountDown = findViewById(R.id.text_view_countdown);


        try
        {
            ReadQuestionsTextFile();
        } catch (IOException e) {
            e.printStackTrace();
        }


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
                    cleanCheckBox();
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

        int rightAnswer = questions[k].getAnswerNr();
        int getanswer = 0;

        if(option1.isChecked())
        {
            getanswer = 1;
        }
        else if(option2.isChecked())
        {
            getanswer = 2;
        }
        else if(option3.isChecked())
        {
            getanswer = 3;
        }

        if(getanswer == rightAnswer)
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
    private void cleanCheckBox()
    {
        radioAnswers.clearCheck();
    }

    private void passToResult()
    {
        Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
        String scr = String.valueOf(score);
        intent.putExtra("score",scr);
        startActivity(intent);
    }

    public void ReadQuestionsTextFile() throws IOException {
        try{
            String string = "";
            StringBuilder stringBuilder = new StringBuilder();
            InputStream is = this.getResources().openRawResource(R.raw.string);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            while (true) {
                try {
                    if ((string = reader.readLine()) == null) break;
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                stringBuilder.append(string).append("\n");
                question.setText(stringBuilder);
                //textView.setText(stringBuilder);
            }
            is.close();
            Toast.makeText(getBaseContext(), stringBuilder.toString(),
                    Toast.LENGTH_LONG).show();
        }
        catch (IOException e){
        e.printStackTrace();
        }
    }


}
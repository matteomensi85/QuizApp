package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    TextView scoreText;
    String valueScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intMess = getIntent();
        valueScore = intMess.getStringExtra("score");

        scoreText = findViewById(R.id.text_view_highscore);
        scoreText.setText("Punteggio: " + valueScore);


        Button buttonReStartQuiz = findViewById(R.id.button_restart_quiz);
        buttonReStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reStartQuiz();
            }
        });

    }
    private void reStartQuiz() {
        Intent intent = new Intent(ResultActivity.this, QuizActivity.class);
        startActivity(intent);
    }


}
package com.example.watchlistapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Questions extends AppCompatActivity {
    private Button answerFirst;
    private Button answerSecond;
    private Button answerThird;
    private TextView question;
    private int questionNo;
    private int actionGenre=0;
    private int thrillerGenre=0;
    private int romanceGenre=0;
    private int comedyGenre=0;
    private int dramaGenre=0;
    private String email;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        questionNo=1;
        Bundle b = getIntent().getExtras();
        if(b != null){
            email = b.getString("email");
            id = b.getString("id");}
        else {
            id="";
            startActivity(new Intent(Questions.this, MainActivity.class));
        }
        question= (TextView) findViewById(R.id.questionTextViewQuestion);
        answerFirst= (Button) findViewById(R.id.questionButtonFirst);
        answerSecond= (Button) findViewById(R.id.questionButtonSecond);
        answerThird= (Button) findViewById(R.id.questionButtonThird);
        answerFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 change(1);
            }
        });
        answerSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change(2);
            }
        });
        answerThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change(3);
            }
        });
    }
    void change(int answer){
        if(questionNo==1){
            if(answer==1){
                dramaGenre+=2;
                thrillerGenre+=1;
            }
            else if(answer==2){
                actionGenre+=2;
                romanceGenre+=2;
            }
            else{
                comedyGenre+=2;
            }
            questionNo++;
            question.setText("Do you want something...");
            answerFirst.setText("Thought provoking");
            answerSecond.setText("Something in the middle");
            answerThird.setText("Easy to digest");
        }
        else if(questionNo==2){
            if(answer==1){
                dramaGenre+=3;
            }
            else if(answer==2){
                actionGenre+=1;
                thrillerGenre+=2;
            }
            else{
                comedyGenre+=3;
                romanceGenre+=1;
            }
            questionNo++;
            question.setText("Are you..");
            answerFirst.setText("Excited");
            answerSecond.setText("Indifferent");
            answerThird.setText("Bored");
        }
        else{
            if(answer==1){
                thrillerGenre+=3;
                comedyGenre+=1;
            }
            else if(answer==2){
                romanceGenre+=3;
            }
            else{
                actionGenre+=3;
                dramaGenre+=1;
            }
            System.out.println("action score is "+actionGenre);
            System.out.println("thriller score is "+thrillerGenre);
            System.out.println("romance score is "+romanceGenre);
            System.out.println("comedy score is "+ comedyGenre);
            System.out.println("drama score is "+dramaGenre);
            orderAndSendResults();
        }
    }
    void orderAndSendResults(){
       String firstStr = "Action",secondStr = "Action";
       Integer firstInt = actionGenre,secondInt = actionGenre;

       if(thrillerGenre>=secondInt){
           if(thrillerGenre>=firstInt){
               secondInt=firstInt;
               secondStr=firstStr;
               firstInt=thrillerGenre;
               firstStr="Thriller";
           }
           else{
               secondInt=thrillerGenre;
               secondStr="Thriller";
           }
       }
       if(romanceGenre>=secondInt){
            if(romanceGenre>=firstInt){
                secondInt=firstInt;
                secondStr=firstStr;
                firstInt=romanceGenre;
                firstStr="Romance";
            }
            else{
                secondInt=romanceGenre;
                secondStr="Romance";
            }
       }
        if(comedyGenre>=secondInt){
            if(comedyGenre>=firstInt){
                secondInt=firstInt;
                secondStr=firstStr;
                firstInt=comedyGenre;
                firstStr="Comedy";
            }
            else{
                secondInt=comedyGenre;
                secondStr="Comedy";
            }
        }
        if(dramaGenre>=secondInt){
            if(dramaGenre>=firstInt){
                secondInt=firstInt;
                secondStr=firstStr;
                firstInt=dramaGenre;
                firstStr="Drama";
            }
            else{
                secondInt=dramaGenre;
                secondStr="Drama";
            }
        }
        System.out.println("f"+firstStr+firstInt);
        System.out.println("s"+secondStr+secondInt);
        Intent intent = new Intent(Questions.this, TestResult.class);
        Bundle bundle = new Bundle();
        bundle.putString("firstOption",firstStr);
        bundle.putString("secondOption",secondStr);
        bundle.putString("email",email);
        bundle.putString("id",id);
        bundle.putInt("genreScoreDifference",firstInt-secondInt);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}

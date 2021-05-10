package yildiz.edu.tr.onur.aydemir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class ShowQuestionsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Question> questions;
    String fn, ln;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_questions);
        setTitle("Questions");

        recyclerView = findViewById(R.id.showQuestionsRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ShowQuestionsActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        QuestionsDBConnection db = new QuestionsDBConnection(ShowQuestionsActivity.this);
        questions = db.getAllQuestions();

        QuestionAdapter questionAdapter = new QuestionAdapter(questions, ShowQuestionsActivity.this);
        recyclerView.setAdapter(questionAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ShowQuestionsActivity.this, MenuActivity.class));
    }
}
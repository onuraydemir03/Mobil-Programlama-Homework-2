package yildiz.edu.tr.onur.aydemir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class MenuActivity extends AppCompatActivity {

    Button addQuestion, showQuestions, prepareAnExam, customizeExam;
    ImageView profileImage;
    String firstname;
    String lastname;
    QuestionsDBConnection db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        firstname = MainActivity.firstname;
        lastname = MainActivity.lastname;

        this.setTitle(firstname + " " + lastname);
        addQuestion = findViewById(R.id.menuButtonAddQuestion);
        showQuestions = findViewById(R.id.menuButtonShowQuestions);
        prepareAnExam = findViewById(R.id.menuButtonPrepareAnExam);
        customizeExam = findViewById(R.id.menuButtonExamSettings);
        profileImage = findViewById(R.id.menuProfileImage);
        String name = firstname + "_"+lastname+".jpg";
        File profileImage = new File(getFilesDir() + File.separator + "ProfilePictures"+File.separator + name);
        if(profileImage.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(profileImage.getAbsolutePath());
            ImageView myImage = (ImageView) findViewById(R.id.menuProfileImage);
            myImage.setImageBitmap(myBitmap);
        }
        switchButtons();
    }

    public void switchButtons(){
        addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, AddQuestionActivity.class));
            }
        });

        showQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = new QuestionsDBConnection(MenuActivity.this);
                if(db.getAllQuestions().size() == 0){
                    Toast.makeText(MenuActivity.this, "There is no question on the database yet !", Toast.LENGTH_SHORT).show();
                }else{
                    startActivity(new Intent(MenuActivity.this, ShowQuestionsActivity.class));
                }

            }
        });

        prepareAnExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = new QuestionsDBConnection(MenuActivity.this);
                if(db.getAllQuestions().size() == 0){
                    Toast.makeText(MenuActivity.this, "There is no question on the database yet !", Toast.LENGTH_SHORT).show();
                }else {
                    startActivity(new Intent(MenuActivity.this, PrepareExamActivity.class));
                }
            }
        });

        customizeExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, CustomizeExamActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
    }

}
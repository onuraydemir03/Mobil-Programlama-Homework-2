package yildiz.edu.tr.onur.aydemir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.channels.FileChannel;

public class AddQuestionActivity extends AppCompatActivity {
    EditText question, optionA, optionB, optionC, optionD, optionE;
    RadioGroup radioButtons;
    Button addQuestion;
    ImageButton attachAFile;
    String questionText, a, b, c, d, e, answer, filePath;
    File src, dst;
    private static final int PICKFILE_RESULT_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        setTitle("Add a Question");

        question = findViewById(R.id.addQuestionQuestion);
        optionA = findViewById(R.id.addQuestionOptionA);
        optionB = findViewById(R.id.addQuestionOptionB);
        optionC = findViewById(R.id.addQuestionOptionC);
        optionD = findViewById(R.id.addQuestionOptionD);
        optionE = findViewById(R.id.addQuestionOptionE);
        radioButtons = findViewById(R.id.addQuestionRadioButtons);
        attachAFile = findViewById(R.id.addQuestionAttachmentButton);
        addQuestion = findViewById(R.id.addQuestionSubmit);
        buttonActions();
    }

    public void buttonActions(){
        attachAFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("*/*");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                startActivityForResult(intent, PICKFILE_RESULT_CODE);

            }

        });

        addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionText = question.getText().toString();
                a = optionA.getText().toString();
                b = optionB.getText().toString();
                c = optionC.getText().toString();
                d = optionD.getText().toString();
                e = optionE.getText().toString();
                answer = ((RadioButton)findViewById(radioButtons.getCheckedRadioButtonId())).getText().toString();
                if(questionText.isEmpty() || a.isEmpty() || b.isEmpty() || c.isEmpty() || d.isEmpty() || e.isEmpty() || answer.isEmpty() || answer.isEmpty()){
                    question.setText("");
                    optionA.setText("");
                    optionB.setText("");
                    optionC.setText("");
                    optionD.setText("");
                    optionE.setText("");
                    Toast.makeText(AddQuestionActivity.this, "Please fill all the blanks.", Toast.LENGTH_SHORT).show();
                }
                else{
                    QuestionsDBConnection db = new QuestionsDBConnection(AddQuestionActivity.this);
                    try{
                        copyFile(src,dst);

                    }catch (Exception e){
                        System.out.println(e);
                    }
                    Question question = new Question(questionText,a,b,c,d,e,answer, filePath,-1,MainActivity.userID);
                    db.addNewQuestion(question);
                    Toast.makeText(AddQuestionActivity.this, "Question saved successfully !", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }

            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == RESULT_OK) {

                    Uri fileUri = data.getData();
                    filePath = fileUri.toString();
                    String[] parts = fileUri.getLastPathSegment().split(":");
                    src = new File(Environment.getExternalStorageDirectory()+File.separator+parts[1]);
                    dst = new File(getFilesDir() + File.separator + "QuestionAttachments");
                    if(!dst.exists())
                        dst.mkdir();
                    dst = new File(getFilesDir() + File.separator + "QuestionAttachments"+File.separator+new File(fileUri.getPath()).getName());
                    filePath = getFilesDir() + File.separator + "QuestionAttachments"+File.separator+new File(fileUri.getPath()).getName();
                }
        }
    }
    private void copyFile(File src, File dst) throws IOException {
        FileInputStream inStream = new FileInputStream(src);
        if (!dst.exists()) {
            dst.createNewFile();
        }
        if (!dst.canWrite()) {
            System.out.print("CAN'T WRITE");
            return;
        }
        FileOutputStream outStream = new FileOutputStream(dst);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();
    }
}
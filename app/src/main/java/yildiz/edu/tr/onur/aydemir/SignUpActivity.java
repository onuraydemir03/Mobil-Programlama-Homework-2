package yildiz.edu.tr.onur.aydemir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Iterator;

public class SignUpActivity extends AppCompatActivity {

    EditText firstName, lastName, phoneNumber, eMail, password, passwordCheck;
    Button signUpButton;
    private static final int RESULT_LOAD_IMAGE = 1;
    ImageView imageView;
    File src, dst;
    String filePath;
    boolean gotAnImage = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.setTitle("Sign Up");

        firstName = findViewById(R.id.signUpFirstName);
        lastName = findViewById(R.id.signUpLastName);
        phoneNumber = findViewById(R.id.signUpPhone);
        eMail = findViewById(R.id.signUpEmail);
        password = findViewById(R.id.signUpPassword);
        passwordCheck = findViewById(R.id.signUpPasswordCheck);
        signUpButton = findViewById(R.id.signUpButton);
        imageView = findViewById(R.id.signUpImageView);
        signUp();

    }
    public boolean checkInformations(){
        UsersDBConnection db  = new UsersDBConnection(SignUpActivity.this);
        ArrayList<User> users = db.getAllUsers();
        if(firstName.getText().toString().trim().equals("")  || lastName.getText().toString().trim().equals("") || phoneNumber.getText().toString().trim().equals("") ||
                eMail.getText().toString().trim().equals("")|| password.getText().toString().trim().equals("")|| passwordCheck.getText().toString().trim().equals("")){
            Toast.makeText(SignUpActivity.this, "Please fill the blanks..", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!passwordCheck.getText().toString().equals(password.getText().toString())){
            password.setText("");
            passwordCheck.setText("");
            Toast.makeText(SignUpActivity.this, "Passwords are not matching. Try again !", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!gotAnImage){
            Toast.makeText(SignUpActivity.this, "Please select a profile image !", Toast.LENGTH_SHORT).show();
            return false;
        }
        for(User user: users){
            if(user.getFirstName().equals(firstName.getText().toString()) && user.getLastName().equals(lastName.getText().toString())){
                firstName.setText("");
                lastName.setText("");
                Toast.makeText(SignUpActivity.this, "This user registered before please try to login or register with different name !",Toast.LENGTH_SHORT).show();
                return false;
            }

            if(user.geteMail().equals(eMail.getText().toString())){
                eMail.setText("");
                Toast.makeText(SignUpActivity.this, "This email used before please try another one !",Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        User newUser = new User(firstName.getText().toString(),
                lastName.getText().toString(),
                phoneNumber.getText().toString(),
                eMail.getText().toString(),
                password.getText().toString(),
                -1);

        Toast.makeText(SignUpActivity.this, "Registration done !", Toast.LENGTH_SHORT).show();
        db.addNewUser(newUser);

        MainActivity.firstname = firstName.getText().toString();
        MainActivity.lastname = lastName.getText().toString();
        MainActivity.userID = db.getAUserWEMail(eMail.getText().toString()).getId();

        users = db.getAllUsers();
        return true;

    }
    public void signUp(){
        signUpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                boolean ret = checkInformations();

                if (ret){
                    try {
                        copyFile(src,dst);
                    }catch (Exception e){
                        System.out.println(e);
                    }
                    startActivity(new Intent(SignUpActivity.this, MenuActivity.class));
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_LOAD_IMAGE:
                if (resultCode == RESULT_OK) {

                    Uri fileUri = data.getData();
                    filePath = fileUri.toString();
                    String[] parts = fileUri.getLastPathSegment().split(":");
                    src = new File(Environment.getExternalStorageDirectory()+File.separator+parts[1]);
                    if (src.exists()){
                        gotAnImage = true;
                        Bitmap myBitmap = BitmapFactory.decodeFile(src.getAbsolutePath());
                        ImageView myImage = (ImageView) findViewById(R.id.signUpImageView);
                        myImage.setImageBitmap(myBitmap);
                    }
                    dst = new File(getFilesDir() + File.separator + "ProfilePictures");
                    if(!dst.exists())
                        dst.mkdir();

                }
        }
    }

    private void copyFile(File src, File dst) throws IOException {
        FileInputStream inStream = new FileInputStream(src);

        String name = firstName.getText().toString() + "_"+lastName.getText().toString()+".jpg";
        dst = new File(getFilesDir() + File.separator + "ProfilePictures"+File.separator + name);
        filePath = dst.toString();

        if (!dst.exists()) {
            dst.createNewFile();
        }
        if (!dst.canWrite()) {
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
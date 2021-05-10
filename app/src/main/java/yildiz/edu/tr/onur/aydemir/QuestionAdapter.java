package yildiz.edu.tr.onur.aydemir;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {
    ArrayList<Question> questions = new ArrayList<>();
    Context context;
    LayoutInflater layoutInflater;
    String filePath;
    private static final int PICK_PDF_FILE = 2;
    public QuestionAdapter(ArrayList<Question> questions, Context context){
        this.questions = questions;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(context);
        View rowView = layoutInflater.inflate(R.layout.show_question_row, parent, false);
        return new ViewHolder(rowView);
    }

    // Her bir satırın içeriği belirlenir.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.question.setText(questions.get(position).getQuestion());
        holder.a.setText(questions.get(position).getA());
        holder.b.setText(questions.get(position).getB());
        holder.c.setText(questions.get(position).getC());
        holder.d.setText(questions.get(position).getD());
        holder.e.setText(questions.get(position).getE());
        holder.answer.setText(questions.get(position).getAnswer());
        filePath = questions.get(position).getFilePath();
        int id = questions.get(position).getId();
        if(filePath == null){
            holder.inspectFileButton.setVisibility(View.GONE);
        }else{
            holder.inspectFileButton.setVisibility(View.VISIBLE);
        }

        holder.inspectFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filePath  = questions.get(position).getFilePath();

                Uri uri = FileProvider.getUriForFile(context,BuildConfig.APPLICATION_ID + ".provider", new File(filePath));

                Intent target = new Intent();
                target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                target.setData(uri);
                target.setAction(Intent.ACTION_VIEW);
                context.startActivity(target);

            }
        });
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditQuestionActivity.class);
                intent.putExtra("QID",id);
                context.startActivity(intent);

            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestionsDBConnection db  = new QuestionsDBConnection(context);
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Delete a Question")
                        .setMessage("Are you sure you want to delete this question?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                QuestionsDBConnection db  = new QuestionsDBConnection(context);
                                questions.remove(position);
                                notifyDataSetChanged();
                                db.deleteAQuestion(id);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });
    }
    @Override
    public int getItemCount() {
        return questions.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView question, a, b ,c ,d ,e, answer;
        ImageButton inspectFileButton, editButton, deleteButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.questionText);
            this.a = itemView.findViewById(R.id.optionAText);
            this.b = itemView.findViewById(R.id.optionBText);
            this.c = itemView.findViewById(R.id.optionCText);
            this.d = itemView.findViewById(R.id.optionDText);
            this.e = itemView.findViewById(R.id.optionEText);
            this.answer = itemView.findViewById(R.id.answerText);
            this.inspectFileButton = itemView.findViewById(R.id.rowItemInspectFileButton);
            this.editButton = itemView.findViewById(R.id.rowItemEditButton);
            this.deleteButton = itemView.findViewById(R.id.rowItemDeleteButton);
        }

    }

}

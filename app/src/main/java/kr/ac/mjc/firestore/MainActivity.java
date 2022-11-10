package kr.ac.mjc.firestore;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements StudentAdapter.OnStudentClickListener {

    final int REQ_ADD_STUDNET=1234;

    ArrayList<Student> mStudentList=new ArrayList<>();
    StudentAdapter mStudentAdapter;

    FirebaseFirestore firestore=FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addStudentBtn=findViewById(R.id.add_student_btn);
        RecyclerView studentListRv=findViewById(R.id.student_list_rv);

        mStudentAdapter=new StudentAdapter(mStudentList);
        mStudentAdapter.setOnStudentClickListener(this);
        studentListRv.setAdapter(mStudentAdapter);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        studentListRv.setLayoutManager(layoutManager);

        addStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,AddStudentActivity.class);
                startActivityForResult(intent,REQ_ADD_STUDNET);
            }
        });

        loadStudentList();
    }

    public void loadStudentList(){
        firestore.collection("student").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                mStudentList.clear();
                List<DocumentSnapshot> documents=queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot snapshot:documents){
                    Student student=snapshot.toObject(Student.class);
                    String id=snapshot.getId();
                    student.setId(id);
                    mStudentList.add(student);

                }
                mStudentAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQ_ADD_STUDNET&&resultCode==RESULT_OK){
            loadStudentList();
        }
    }

    @Override
    public void onClick(Student student) {
        Log.d("MainActivity",student.getId());

        AlertDialog dialog=new AlertDialog.Builder(this)
                .setItems(R.array.menu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                //수정
                            case 1:
                                //삭제
                                firestore.collection("student").document(student.getId()).delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                loadStudentList();
                                            }
                                        });

                        }
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).show();

    }
}
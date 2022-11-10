package kr.ac.mjc.firestore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    List<Student> mStudentList;
    OnStudentClickListener mOnStudentClickListener;

    public StudentAdapter(List<Student> studentList){
        this.mStudentList=studentList;
    }

    public void setOnStudentClickListener(OnStudentClickListener listener){
        this.mOnStudentClickListener=listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Student student=mStudentList.get(position);
        holder.bind(student);
    }

    @Override
    public int getItemCount() {
        return mStudentList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView nameTv;
        TextView numberTv;
        TextView departmentTv;
        Student mStudent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv=itemView.findViewById(R.id.name_tv);
            numberTv=itemView.findViewById(R.id.number_tv);
            departmentTv=itemView.findViewById(R.id.department_tv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnStudentClickListener.onClick(mStudent);
                }
            });
        }
        public void bind(Student student){
            nameTv.setText(student.getName());
            numberTv.setText(student.getNumber());
            departmentTv.setText(student.getDepartment());
            this.mStudent=student;
        }
    }

    interface OnStudentClickListener{
        void onClick(Student student);
    }
}

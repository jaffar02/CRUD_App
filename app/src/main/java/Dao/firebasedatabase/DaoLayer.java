package Dao.firebasedatabase;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaffar.firebasedatabase.Employee;
import com.jaffar.firebasedatabase.recyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class DaoLayer {
    private final String TAG = "DaoClass";
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    Context context;
    public boolean complete = false;

    public DaoLayer(Context context){
        this.context = context;
    }


    public void insert(Employee emp){
       if(emp!=null) {
           String key = db.push().getKey();
           emp.setKey(key);
           db.child("Employees").child(key).setValue(emp).addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {
                   Log.d(TAG, "onComplete: ");
               }
           });
       }
       else{
           return;
       }
    }

    public Boolean update(Employee emp) {
        complete = false;
        Employee employee = retrieve(emp.getName());
        if (employee!=null){
        if (employee.getName().equals(emp.getName())/*exists(emp.getName())*/) {
            String key = employee.getKey();
            emp.setKey(key);
            Log.d(TAG, "update: Exists");
            db.child("Employees").child(key).setValue(emp).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d(TAG, "onComplete: Done");
                }
            });
            complete = false;
            return true;
        } else {
            Log.d(TAG, "update: Not Exist");
            complete = false;
            return false;
        }
    }
        return false;
    }

    public List<Employee> retrieveAll(){
        List<Integer> checker = new ArrayList<>();
        List<Employee> empBuf = new ArrayList<>();
        db.child("Employees").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snaps : snapshot.getChildren()) {
                        Employee emp = new Employee(snaps.getValue(Employee.class).getName(),
                                snaps.getValue(Employee.class).getPosition());
                        emp.setKey(snaps.getValue(Employee.class).getKey());
                        Log.d(TAG, "onDataChange: Retrieving: " + emp.getName());
                        empBuf.add(emp);
                        checker.add(1);
                    }
                    complete = true;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: Can't retrieve");
            }
        });
        while (checker.isEmpty());
        return empBuf;
    }

    public Employee retrieve(String name){
        complete=false;
        List<Employee> empBuf = retrieveAll();
        while (!complete);
        if (!empBuf.isEmpty()){
            Log.d(TAG, "retrieve: "+empBuf.size());
        }
        for (Employee e : empBuf){
            if(e.getName().equals(name)){
                Log.d(TAG, "retrieve: Successfull");
                complete = false;
                return e;
            }
        }
        complete = false;
        return null;
    }

    public Boolean exists(String name){
        Log.d(TAG, "exists: In here");
        List<Employee> empBuf = isDatabaseEmpty();
        for (Employee e : empBuf){
            Log.d(TAG, "exists: Employee: "+e.getName());
            if(e.getName().equals(name)){
                Log.d(TAG, "exists: Yes");
                return true;
            }
        }
        return false;
    }

    public List<Employee> isDatabaseEmpty(){
        List<Employee> empBuf = retrieveAll();
        while (!complete);
        if(empBuf.isEmpty()){
            Log.d(TAG, "isDatabaseEmpty: Yes");
            complete=false;
            return empBuf;
        }
        else {
            Log.d(TAG, "isDatabaseEmpty: No");
            complete=false;
            return empBuf;
        }
    }

    public Boolean delete(String name){
        Employee employee = retrieve(name);
        if (employee!=null) {
            db.child("Employees").child(employee.getKey()).removeValue();
            return true;
        }
        return false;
    }

}

package com.example.roomexample.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.example.roomexample.R
import com.example.roomexample.App
import com.example.roomexample.room.AppDatabase
import com.example.roomexample.room.Employee
import com.example.roomexample.room.EmployeeDao
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class CreateEmpFragment : Fragment() {

    lateinit var editName: EditText
    lateinit var editSalary: EditText
    lateinit var buttonOk: Button
    lateinit var imageBack: ImageView
    lateinit var db: AppDatabase
    lateinit var employeeDao: EmployeeDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = App.getInstance().database
        employeeDao = db.employeeDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_create_emp, container, false)

        editName = view.findViewById(R.id.edit_name)
        editSalary = view.findViewById(R.id.edit_salary)
        buttonOk = view.findViewById(R.id.button_submit)
        imageBack = view.findViewById(R.id.image_back)

        imageBack.setOnClickListener{
            parentFragmentManager.popBackStackImmediate()
        }

        buttonOk.setOnClickListener{
            if (editName.text.isNotEmpty() && editSalary.text.isNotEmpty()){
                    var employee = Employee()
                employee.name = editName.text.toString()
                employee.salary = editSalary.text.toString().toInt()
                insertEmployee(employee).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe({
                    }, {}, {
                        parentFragmentManager.popBackStackImmediate()
                    })
            } else {
                Snackbar.make(view, "Text fields are empty!", Snackbar.LENGTH_SHORT).show()
            }
        }
        return view
    }

    private fun insertEmployee(employee: Employee): Observable<Employee> {
        return Observable.create{
            sub ->
            employeeDao.insert(employee)
            sub.onComplete()
        }
    }
}
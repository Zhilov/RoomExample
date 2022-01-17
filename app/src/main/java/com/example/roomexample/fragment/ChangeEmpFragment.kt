package com.example.roomexample.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.roomexample.App
import com.example.roomexample.R
import com.example.roomexample.room.AppDatabase
import com.example.roomexample.room.Employee
import com.example.roomexample.room.EmployeeDao
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class ChangeEmpFragment : Fragment() {

    private lateinit var editName: EditText
    private lateinit var editSalary: EditText
    private lateinit var buttonSubmitChanges: Button
    private lateinit var imageDelete: ImageView
    private lateinit var imageBack: ImageView
    private lateinit var db: AppDatabase
    private lateinit var employeeDao: EmployeeDao
    private lateinit var textCurrent: TextView
    private lateinit var employee: Employee

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = App.getInstance().database
        employeeDao = db.employeeDao()
        if (arguments != null) {
            employee = arguments!!.getParcelable("item_key")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_change_emp, container, false)

        editName = view.findViewById(R.id.edit_name_ch)
        editSalary = view.findViewById(R.id.edit_salary_ch)
        buttonSubmitChanges = view.findViewById(R.id.button_submit_changes)
        imageBack = view.findViewById(R.id.image_back_ch)
        imageDelete = view.findViewById(R.id.image_delete)
        textCurrent = view.findViewById(R.id.text_current)

        imageBack.setOnClickListener{
            parentFragmentManager.popBackStackImmediate()

        }

        imageDelete.setOnClickListener{
            deleteData()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe()
            parentFragmentManager.popBackStackImmediate()
        }

            editName.setText(employee.name)
            editSalary.setText(employee.salary.toString())
            textCurrent.setText("Name: ${employee.name} with salary ${employee.salary}")

        buttonSubmitChanges.setOnClickListener{
            if (editName.text.isNotEmpty() && editSalary.text.isNotEmpty()){
                changeEmployee(employee).observeOn(AndroidSchedulers.mainThread())
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

    private fun changeEmployee(employee: Employee): Observable<Employee> {
        return Observable.create{
                sub ->
            employeeDao.updateData(employee.id, editName.text.toString(), editSalary.text.toString().toDouble())
            sub.onComplete()
        }
    }

    private fun deleteData(): Observable<Any> {
        return Observable.create {
            employeeDao.deleteById(employee.id)
            it.onComplete()
        }
    }
}
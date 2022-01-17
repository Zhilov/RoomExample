package com.example.roomexample.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomexample.App
import com.example.roomexample.MainActivity
import com.example.roomexample.R
import com.example.roomexample.adapter.RecyclerViewAdapter
import com.example.roomexample.room.AppDatabase
import com.example.roomexample.room.Employee
import com.example.roomexample.room.EmployeeDao
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var db: AppDatabase
    lateinit var employeeDao: EmployeeDao
    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var adapter: RecyclerViewAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var textEmpty: TextView
    lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = App.getInstance().database
        employeeDao = db.employeeDao()
        mainActivity = (activity as MainActivity)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_main, container, false)

        linearLayoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.recycler_data)
        textEmpty = view.findViewById(R.id.text_empty)
        fab = view.findViewById(R.id.fab_add)

        getEmployeeList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe ({
                if (it.isEmpty()){
                    textEmpty.visibility = View.VISIBLE
                } else {
                    adapter = RecyclerViewAdapter(context!!, it)
                    recyclerView.layoutManager = linearLayoutManager
                    recyclerView.adapter = adapter
                }

            }, {
               Log.d("Tag", it?.localizedMessage.toString())
            }, {})

        fab.setOnClickListener{
            mainActivity.switchContent(R.id.fragment_container_view, CreateEmpFragment())
        }

        return view
    }

    private fun getEmployeeList(): Observable<ArrayList<Employee>> {
        return Observable.create{
                sub ->
            sub.onNext(employeeDao.all as ArrayList<Employee>?)
            sub.onComplete()
        }
    }
}
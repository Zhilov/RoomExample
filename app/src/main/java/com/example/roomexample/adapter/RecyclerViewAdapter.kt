package com.example.roomexample.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.roomexample.App
import com.example.roomexample.R
import com.example.roomexample.room.AppDatabase
import com.example.roomexample.room.Employee
import com.example.roomexample.room.EmployeeDao
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers


class RecyclerViewAdapter(private val employeeList: ArrayList<Employee>):RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {

    lateinit var db: AppDatabase
    lateinit var employeeDao: EmployeeDao

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val textId: TextView = itemView.findViewById(R.id.text_id)
        val textName: TextView = itemView.findViewById(R.id.text_name)
        val textSalary: TextView = itemView.findViewById(R.id.text_salary)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
        db = App.getInstance().database
        employeeDao = db.employeeDao()
        return MyViewHolder(itemView)
    }

    override fun getItemCount() = employeeList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.textId.text = employeeList[position].id.toString()
        holder.textName.text = employeeList[position].name.toString()
        holder.textSalary.text = employeeList[position].salary.toString()

        holder.itemView.setOnClickListener {
            deleteData(position).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, {}, {
                    employeeList.removeAt(position)
                    this.notifyDataSetChanged()
                })
        }
    }

    fun updateList(newlist: ArrayList<Employee>) {
        employeeList.clear()
        employeeList.addAll(newlist)
        this.notifyDataSetChanged()
    }

    private fun deleteData(position: Int): Observable<Any>{
        return Observable.create{
            sub ->
            employeeDao.deleteById(employeeList[position].id)
            sub.onComplete()
        }
    }

}

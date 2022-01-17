package com.example.roomexample.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.roomexample.App
import com.example.roomexample.MainActivity
import com.example.roomexample.R
import com.example.roomexample.fragment.ChangeEmpFragment
import com.example.roomexample.room.AppDatabase
import com.example.roomexample.room.Employee
import com.example.roomexample.room.EmployeeDao


class RecyclerViewAdapter(private val context: Context, private val employeeList: ArrayList<Employee>) :
    RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {

    private lateinit var db: AppDatabase
    private lateinit var employeeDao: EmployeeDao

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textId: TextView = itemView.findViewById(R.id.text_id)
        val textName: TextView = itemView.findViewById(R.id.text_name)
        val textSalary: TextView = itemView.findViewById(R.id.text_salary)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
        db = App.getInstance().database
        employeeDao = db.employeeDao()
        return MyViewHolder(itemView)
    }

    override fun getItemCount() = employeeList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.textId.text = "" + (position + 1)
        holder.textName.text = employeeList[position].name.toString()
        holder.textSalary.text = employeeList[position].salary.toString()

        holder.itemView.setOnClickListener {
            fragmentJump(employeeList[position])
        }
    }

    private fun fragmentJump(mItemSelected: Employee){
        val fragmentDetails = ChangeEmpFragment()
        val bundle = Bundle()
        bundle.putParcelable("item_key", mItemSelected)
        fragmentDetails.arguments = bundle
        switchContent(R.id.fragment_container_view, fragmentDetails, bundle)
    }

    private fun switchContent(id: Int, fragment: Fragment, bundle: Bundle) {
        if (context is MainActivity) {
            val mainActivity = context
            val frag: Fragment = fragment
            mainActivity.switchContent(id, frag, bundle)
        }
    }

}

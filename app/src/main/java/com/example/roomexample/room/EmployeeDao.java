package com.example.roomexample.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EmployeeDao {

    @Query("SELECT * FROM employee")
    List<Employee> getAll();

    @Query("SELECT * FROM employee WHERE id = :id")
    Employee getById(long id);

    @Query("DELETE FROM employee WHERE id = :id")
    void deleteById(long id);

    @Query("UPDATE employee SET name = :name, salary= :salary WHERE id =:id;")
    void updateData(long id, String name, Double salary);

    @Insert
    void insert(Employee employee);
}

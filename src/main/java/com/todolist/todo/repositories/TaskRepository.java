package com.todolist.todo.repositories;

import com.todolist.todo.models.Task;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByCompletedTrue();

    List<Task> findByCompletedFalse();
}

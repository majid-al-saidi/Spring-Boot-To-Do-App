package com.todolist.todo.controllers.api;

import com.todolist.todo.models.Task;
import com.todolist.todo.models.User;
import com.todolist.todo.repositories.TaskRepository;
import com.todolist.todo.repositories.UserRepository;
import com.todolist.todo.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskApiController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Task> getUserTasks() {
        User user = AuthUtils.getCurrentUser(userRepository);
        return taskRepository.findByUser(user);
    }

    @PostMapping
    public Task addTask(@RequestBody Task task) {
        User user = AuthUtils.getCurrentUser(userRepository);
        task.setUser(user);
        return taskRepository.save(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
        User user = AuthUtils.getCurrentUser(userRepository);
        Task task = taskRepository.findById(id).orElse(null);

        if (task == null || !task.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Not allowed");
        }

        task.setTitle(updatedTask.getTitle());
        task.setCompleted(updatedTask.isCompleted());
        return ResponseEntity.ok(taskRepository.save(task));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        User user = AuthUtils.getCurrentUser(userRepository);
        Task task = taskRepository.findById(id).orElse(null);

        if (task == null || !task.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Not allowed");
        }

        taskRepository.delete(task);
        return ResponseEntity.ok("Deleted");
    }
}
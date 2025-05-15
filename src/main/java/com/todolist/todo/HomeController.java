package com.todolist.todo;

import com.todolist.todo.models.Task;
import com.todolist.todo.repositories.TaskRepository;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("tasks", taskRepository.findAll());
        model.addAttribute("task", new Task());
        return "index";
    }

    @PostMapping("/add")
    public String addTask(@Valid @ModelAttribute("task") Task task,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("tasks", taskRepository.findAll()); // Required!
            return "index"; // Stay on the same page
        }

        taskRepository.save(task);
        return "redirect:/"; // Clean redirect after success
    }

    @PostMapping("/toggle/{id}")
    public String toggleTask(@PathVariable Long id) {
        taskRepository.findById(id).ifPresent(task -> {
            task.setCompleted(!task.isCompleted());
            taskRepository.save(task);
        });
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String editTask(@PathVariable Long id, Model model) {
        Task task = taskRepository.findById(id).orElse(null);
        model.addAttribute("task", task);
        model.addAttribute("tasks", taskRepository.findAll());
        return "index";
    }

    @PostMapping("/update/{id}")
    public String updateTask(@PathVariable Long id, @ModelAttribute("task") Task updatedTask) {
        Task existing = taskRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setTitle(updatedTask.getTitle());
            taskRepository.save(existing);
        }
        return "redirect:/";
    }

    @GetMapping("/filter")
    public String filterTasks(@RequestParam(required = false) String status, Model model) {
        List<Task> tasks;

        if ("complete".equals(status)) {
            tasks = taskRepository.findByCompletedTrue();
        } else if ("incomplete".equals(status)) {
            tasks = taskRepository.findByCompletedFalse();
        } else {
            tasks = taskRepository.findAll();
        }

        model.addAttribute("tasks", tasks);
        model.addAttribute("task", new Task());
        return "index";
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskRepository.deleteById(id);
        return "redirect:/";
    }
}
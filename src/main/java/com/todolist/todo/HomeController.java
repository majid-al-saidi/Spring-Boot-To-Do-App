package com.todolist.todo;

import com.todolist.todo.models.Task;
import com.todolist.todo.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String addTask(@ModelAttribute Task task) {
        taskRepository.save(task);
        return "redirect:/";
    }

    @PostMapping("/toggle/{id}")
    public String toggleTask(@PathVariable Long id) {
        taskRepository.findById(id).ifPresent(task -> {
            task.setCompleted(!task.isCompleted());
            taskRepository.save(task);
        });
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskRepository.deleteById(id);
        return "redirect:/";
    }
}
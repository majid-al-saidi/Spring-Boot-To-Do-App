package com.todolist.todo;

import com.todolist.todo.models.Task;
import com.todolist.todo.repositories.TaskRepository;
import jakarta.validation.Valid;
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

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskRepository.deleteById(id);
        return "redirect:/";
    }
}
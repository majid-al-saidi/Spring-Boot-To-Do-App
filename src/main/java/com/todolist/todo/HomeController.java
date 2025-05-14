package com.todolist.todo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Controller
public class HomeController {

    // create new task object:
    private List<Task> tasks = new ArrayList<>();
    private AtomicLong idCounter = new AtomicLong(1);


    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("tasks", tasks);
        model.addAttribute("task", new Task()); // Empty task for the form
        return "index";
    }

    @PostMapping("/add")
    public String addTask(@ModelAttribute Task task) {
        task.setId(idCounter.getAndIncrement());
        tasks.add(task);
        return "redirect:/"; // Go back to homepage after adding
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        tasks.removeIf(task->id.equals(task.getId()));
        return "redirect:/";
    }
}

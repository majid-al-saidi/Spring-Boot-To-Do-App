package com.todolist.todo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(1L , "Learning Java", false));
        tasks.add(new Task(2L, "Buy milk", false));
        tasks.add(new Task(3L, "Finish homework", true));
        tasks.add(new Task(4L, "Read a book", false));

        model.addAttribute("tasks" , tasks);
        return "index";
    }
}

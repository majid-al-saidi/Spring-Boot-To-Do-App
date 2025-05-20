package com.todolist.todo;

import com.todolist.todo.models.Task;
import com.todolist.todo.models.User;
import com.todolist.todo.repositories.TaskRepository;
import com.todolist.todo.utils.AuthUtils;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.todolist.todo.repositories.UserRepository;

@Controller
public class HomeController {

    @Autowired
    private TaskRepository taskRepository;
    private final UserRepository userRepository;

    public HomeController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @GetMapping("/")
    public String home(Model model) {
        User currentUser = AuthUtils.getCurrentUser(userRepository);
        List<Task> tasks = taskRepository.findByUser(currentUser);
        model.addAttribute("tasks", tasks);
        model.addAttribute("task", new Task());
        return "index";
    }

    @PostMapping("/add")
    public String addTask(@Valid @ModelAttribute("task") Task task,
                          BindingResult result,
                          RedirectAttributes redirectAttributes,
                          Model model) {

        if (result.hasErrors()) {
            model.addAttribute("tasks", taskRepository.findAll());
            return "index";
        }

        // Get the current user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User currentUser = userRepository.findByUsername(username).orElse(null);
        if (currentUser == null) {
            return "redirect:/login";
        }

        // 🛠️ اربط المستخدم قبل الحفظ
        task.setUser(currentUser);
        taskRepository.save(task);

        redirectAttributes.addFlashAttribute("success", "تم إضافة المهمة بنجاح!");
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

    @GetMapping("/edit/{id}")
    public String editTask(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        if (!ownsTask(id)) {
            redirectAttributes.addFlashAttribute("fail", "غير مسموح");
            return "redirect:/";
        }
        Task task = taskRepository.findById(id).orElse(null);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username).orElse(null);
        model.addAttribute("task", task);
        model.addAttribute("tasks", taskRepository.findByUser(currentUser));
        return "index";
    }



    @PostMapping("/update/{id}")
    public String updateTask(@PathVariable Long id, @ModelAttribute("task") Task updatedTask,
            RedirectAttributes redirectAttributes) {
        Task existing = taskRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setTitle(updatedTask.getTitle());
            taskRepository.save(existing);
            redirectAttributes.addFlashAttribute("success", "تم تحديث المهمة بنجاح!");
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
    public String deleteTask(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        taskRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "تم حذف المهمة بنجاح!");
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    private boolean ownsTask(Long taskId) {
        User currentUser = AuthUtils.getCurrentUser(userRepository);
        if (currentUser == null) return false;

        Task task = taskRepository.findById(taskId).orElse(null);
        return task != null && task.getUser().getId().equals(currentUser.getId());
    }

}
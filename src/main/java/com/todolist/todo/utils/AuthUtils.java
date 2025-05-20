package com.todolist.todo.utils;

import com.todolist.todo.models.User;
import com.todolist.todo.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

public class AuthUtils {

    // ✅ يرجع اسم المستخدم الحالي
    public static String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null) ? auth.getName() : null;
    }

    // ✅ يرجع المستخدم الحالي من قاعدة البيانات
    public static User getCurrentUser(UserRepository userRepository) {
        String username = getCurrentUsername();
        if (username == null) return null;
        return userRepository.findByUsername(username).orElse(null);
    }

    // ✅ يتحقق إذا فيه مستخدم مسجل دخول
    public static boolean isLoggedIn() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser");
    }

    // ✅ يتحقق إذا المستخدم له صلاحية ADMIN (اختياري لو تستخدم صلاحيات)
    public static boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;
        Collection<? extends GrantedAuthority> roles = auth.getAuthorities();
        return roles.stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
    }
}

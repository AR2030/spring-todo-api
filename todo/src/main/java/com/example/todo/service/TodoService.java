package com.example.todo.service;

import com.example.todo.model.Todo;
import com.example.todo.model.User;
import com.example.todo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TodoService {
    @Autowired
    private TodoRepository todoRepository;
    @Autowired
    private UserService userService;

    public List<Todo> getAll() {
        User currentUser = userService.getCurrentUser();
        return todoRepository.findByUser(currentUser);
    }

    public Todo save(Todo todo) {
        System.out.println("Saving todo...");
        System.out.println("Auth: " + SecurityContextHolder.getContext().getAuthentication());

        User currentUser = userService.getCurrentUser();
        System.out.println("Current User: " + currentUser.getUsername());
        todo.setUser(currentUser);
        return todoRepository.save(todo);
    }
    public void delete(Long id) {
        User currentUser = userService.getCurrentUser();
        Todo todo = todoRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found"));

        todoRepository.delete(todo);
    }


    public ResponseEntity<Todo> update(Long id, Todo todo) {
        User currentUser = userService.getCurrentUser();

        // Check if the to-do exists and belongs to the current user
        return todoRepository.findByIdAndUser(id, currentUser)
                .map(existingTodo -> {
                    existingTodo.setTitle(todo.getTitle());
                    existingTodo.setCompleted(todo.isCompleted());
                    Todo updatedTodo = todoRepository.save(existingTodo);
                    return ResponseEntity.ok(updatedTodo);
                })
                .orElse(ResponseEntity.notFound().build());

    }
}
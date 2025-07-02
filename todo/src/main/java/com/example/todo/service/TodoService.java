package com.example.todo.service;

import com.example.todo.model.Todo;
import com.example.todo.model.User;
import com.example.todo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {
    @Autowired
    private TodoRepository todoRepository;

    public List<Todo> getAll() {
        return todoRepository.findAll();
    }

    public Todo save(Todo todo) {
        // استرجع المستخدم من SecurityContext
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // اربط التودو بالمستخدم الحالي
        todo.setUser(user);

        return todoRepository.save(todo);
    }
    public void delete(Long id) {
        todoRepository.deleteById(id);
    }

    public ResponseEntity<Todo> update(Long id, Todo todo) {
        return todoRepository.findById(id)
                .map(existingTodo -> {
                    existingTodo.setTitle(todo.getTitle());
                    existingTodo.setCompleted(todo.isCompleted());
                    Todo updatedTodo = todoRepository.save(existingTodo);
                    return ResponseEntity.ok(updatedTodo);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
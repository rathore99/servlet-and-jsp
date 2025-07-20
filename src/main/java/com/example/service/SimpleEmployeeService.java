package com.example.service;

import com.example.model.SimpleEmployee;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleEmployeeService {
    private final Map<Integer, SimpleEmployee> employees = new HashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    public List<SimpleEmployee> getAll() {
        return new ArrayList<>(employees.values());
    }

    public SimpleEmployee get(int id) {
        return employees.get(id);
    }

    public SimpleEmployee create(SimpleEmployee emp) {
        int id = idCounter.getAndIncrement();
        emp.setId(id);
        employees.put(id, emp);
        return emp;
    }

    public SimpleEmployee update(int id, SimpleEmployee emp) {
        if (!employees.containsKey(id)) return null;
        emp.setId(id);
        employees.put(id, emp);
        return emp;
    }

    public boolean delete(int id) {
        return employees.remove(id) != null;
    }
} 
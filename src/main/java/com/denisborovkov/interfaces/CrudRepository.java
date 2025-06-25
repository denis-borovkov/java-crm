package com.denisborovkov.interfaces;

interface CrudRepository<T, ID> {
    T save(T t);
    T get(ID id);
    boolean isExists(ID id);
    int getCount();
    void update(T t);
    void delete(ID id);
    T getAll();
}

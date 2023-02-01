package ru.danil.springcourse.model;


import javax.persistence.*;
import javax.validation.constraints.*;


import java.util.List;


@Entity
@Table(name = "person")
public class Person {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "Имя не может быть пустым")
    @Size(min = 3, max = 150, message = "Имя должно быть от 3 до 150 символов длиной")
    @Pattern(regexp = "[А-Я][а-я]{1,33}\\s[А-Я][а-я]{1,33}\\s[А-Я][а-я]{1,33}", message = "Имя должно выглядить следующем оброзом: Иванов Иван Иванович")
    @Column(name = "name")
    private String name;

    @Min(value = 1900, message = "Год рождения должен быть больше, чем 1900")
    @Max(value = 2023, message = "Год рождения не может быть больше текущего года")
    @Column(name = "age")
    private int age;

    @OneToMany(mappedBy = "owner")
    private List<Book> bookList;

    public Person() {}

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public int getAge() {return age;}

    public void setAge(int age) {this.age = age;}

}

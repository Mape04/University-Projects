//
// Created by mazil on 6/26/2023.
//
#include <iostream>
#include <cassert>
#include "tests.h"
#include "../Domain/Expense.h"
#include "../Repository/ConsoleRepository.h"
#include "../Service/Service.h"

void testExpense(){
    Expense e1(20, 200, "mancare");
    Expense e2(20, 200, "mancare");
    assert(e1.getSum() == 200);
    assert(e1.getDay() == 20);
    assert(e1.getType() == "mancare");

    assert(e1 == e2);

    e1.setDay(10);
    e1.setSum(100);
    e1.setType("internet");

    assert(e1 != e2);

    assert(e1.getSum() == 100);
    assert(e1.getDay() == 10);
    assert(e1.getType() == "internet");

    std::cout<<"Expense Tests Passed!\n";
}

void testConsoleRepository(){
    ConsoleRepository<Expense> repo;
    Expense e1(10, 100, "mancare");
    Expense e2(20, 200, "internet");
    Expense e3(30, 300, "haine");
    repo.addExpense(e1);
    repo.addExpense(e2);
    repo.addExpense(e3);

    assert(repo.getByPosition(0) == e1);
    assert(repo.getByPosition(1) == e2);
    assert(repo.getByPosition(2) == e3);

    assert(repo.getAll().size() == 3);

    repo.deleteExpense(1);

    assert(repo.getByPosition(0) == e1);
    assert(repo.getByPosition(1) == e3);

    assert(repo.getAll().size() == 2);

    std::cout<<"Console Repository Tests Passed!\n";
}

void testService() {
//    This if your run with Console Repository
//    ConsoleRepository<Expense> repository;
//    Service service(repository);

//  This if you run with File Repository
    FileRepository<Expense> repository("tests.txt");
    Service service(repository);

    Expense e1(10, 100, "mancare");
    Expense e2(20, 200, "internet");
    Expense e3(30, 300, "haine");
    Expense e4(30, 400, "mancare");

    service.addExpense(e1);
    service.addExpense(e2);
    service.addExpense(e3);
    service.addExpense(e4);

    assert(service.sumType("mancare") == 500);
    assert(service.maxDay() == 30);
    assert(service.filterType("mancare").at(0) == e1);
    assert(service.filterType("mancare").at(1) == e4);

    assert(service.getByPosition(0) == e1);
    assert(service.getByPosition(1) == e2);
    assert(service.getByPosition(2) == e3);
    assert(service.getByPosition(3) == e4);

    assert(service.getAll().size() == 4);

    service.deleteExpense(1);

    assert(service.getByPosition(0) == e1);
    assert(service.getByPosition(1) == e3);
    assert(service.getByPosition(2) == e4);

    assert(service.getAll().size() == 3);

    assert(service.getByDay(30).at(0) == e3);
    assert(service.getByDay(30).at(1) == e4);

    service.deleteByDay(10);

    assert(service.getAll().size() == 2);
    assert(service.getByPosition(0) == e3);
    assert(service.getByPosition(1) == e4);

    ofstream ofs("tests.txt", ios::out | ios::trunc);
    ofs.close();

    std::cout<<"Service Tests Passed!\n";
}

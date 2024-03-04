//
// Created by mazil on 6/26/2023.
//

#ifndef FAMILIE_UI_H
#define FAMILIE_UI_H
#include "../Service/Service.h"

class UI {
private:
    Service service;
    //adds a expense
    void add();
    //deletes an expense
    void deleteByDay();
    //shows all expenses
    void showAll();
    //prints the menu
    static void print();
    //exit message
    static void exit();
    //command
    char phrase(string line);
    //sum of a specific type
    void sumType();
    //the day with the most money spent
    void maxDay();
    //filter expenses by a specific type
    void filterType();
    //undo last operation
    void undo();
    //insert a day
    void insertDay();

public:
    //copy constructor
    UI(const Service& otherService);
    //default destructor
    ~UI() = default;
    //run console
    void run();
};


#endif //FAMILIE_UI_H

//
// Created by mazil on 6/26/2023.
//

#include "UI.h"
#include "../Utils/Exceptions/Exceptions.h"
#include <iostream>
#include <ctime>
#include <unistd.h>

using namespace std;

UI::UI(const Service &otherService) {
    this->service = otherService;
}

void UI::add() {
    time_t theTime = time(NULL);
    struct tm *aTime = localtime(&theTime);
    int day = aTime->tm_mday;
    int sum;
    string type;
    cin >> sum;
    cin >> type;
    Expense e(day, sum, type);
    try{
        this->service.addExpense(e);
    }catch(InvalidDayException &e){
        cout<<e.what()<<endl;
    }catch(InvalidSumException &e){
        cout<<e.what()<<endl;
    }catch(InvalidTypeException &e){
        cout<<e.what()<<endl;
    }catch(...){
    }

}

void UI::deleteByDay() {
    int day;
    cin >> day;
    try{
        this->service.deleteByDay(day);
    }catch(InvalidDayException &e){
        cout<<e.what()<<endl;
    }

}

void UI::showAll() {
    for(auto expense: this->service.getAll())
        cout<<expense<<endl;
    cout<<"\n";
}

void UI::print() {
    printf(">add : Add expense.\n"
           ">delete : Delete expenses by day.\n"
           ">show : Show all expenses.\n"
           ">insert : Insert a expense.\n"
           ">max : The day with the most money spent.\n"
           ">undo: Undo.\n"
           ">filter: Filter based on type of expense.\n"
           ">sum : Sum of a specific expense type.\n"
           ">exit : Exit.\n");
}

void UI::exit() {
    cout<<"Exiting";
    usleep(150000);
    cout<<".";
    usleep(150000);
    cout<<".";
    usleep(150000);
    cout<<".\n";
}

char UI::phrase(string line) {
    if(line == "add")
        return '1';
    if(line == "delete")
        return '2';
    if(line == "show")
        return '3';
    if(line == "exit")
        return 'x';
    if(line == "sum")
        return '4';
    if(line == "filter")
        return '5';
    if(line == "max")
        return '6';
    if(line == "undo")
        return '7';
    if(line == "insert")
        return '8';
}

void UI::run() {
    string welcome = "Welcome";
    cout<<"        ";
    for(int i = 0 ; i < welcome.size(); i++){
        cout<<welcome[i];
        usleep(150000);
    }
    cout<<"\n";
    while (true) {
        char option;
        string command;
        this->print();
        cout << "Choose command: ";
        cin>>command;
        cout << "\n";
        option = phrase(command);
        switch (option) {
            case '1':
                this->add();
                break;
            case '2':
                this->deleteByDay();
                break;
            case '3':
                this->showAll();
                break;
            case '4':
                this->sumType();
                break;
            case '5':
                this->filterType();
                break;
            case '6':
                this->maxDay();
                break;
            case '7':
                this->undo();
                break;
            case '8':
                this->insertDay();
                break;
            case 'x':
                this->exit();
                return;
            default:
                printf("\x1B[31mCommand does not exist!\033[0m\t\t");
                cout<<"\n";
        }

    }
}

void UI::sumType() {
    string type;
    cin>>type;
    try{
        cout<<this->service.sumType(type)<<"/n";
    }catch(InvalidTypeException &e){
        cout<<e.what()<<endl;
    }

}

void UI::maxDay() {
    cout<<this->service.maxDay()<<endl;
}

void UI::filterType() {
    string type;
    cin>>type;
    try{
        for(auto expense : this->service.filterType(type))
            cout<<expense<<endl;
        cout<<"\n";
    }catch(InvalidTypeException &e){
        cout<<e.what()<<endl;
    }

}

void UI::undo() {
    this->service.deleteExpense(this->service.getAll().size() - 1);
}

void UI::insertDay() {
    int day, sum;
    string type;
    cin>>day>>sum>>type;
    Expense e(day,sum,type);
    try{
        this->service.addExpense(e);
    }catch(InvalidDayException &e){
        cout<<e.what()<<endl;
    }catch(InvalidSumException &e){
        cout<<e.what()<<endl;
    }catch(InvalidTypeException &e){
        cout<<e.what()<<endl;
    }catch(...){
    }
}



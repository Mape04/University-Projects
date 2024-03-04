//
// Created by mazil on 6/27/2023.
//

#ifndef FAMILIE_FILEREPOSITORY_H
#define FAMILIE_FILEREPOSITORY_H
#include "ConsoleRepository.h"
#include <string>
#include <fstream>
#include <sstream>
#include <iostream>

template <typename T>
class FileRepository: public  ConsoleRepository<T>{
private:
    string fileName;
    //load info from file
    void loadFromFile();
    //save info to file
    void saveToFile();
public:
    //constructor with params
    FileRepository(string fileName);
    //default destructor
    ~FileRepository() = default;

    //adds an expense
    void addExpense(T expense);
    //deletes an expense
    void deleteExpense(int position);
    //returns all the expenses
    vector<T> getAll();
    //returns an expense by position
    T& getByPosition(int position);

};

template<typename T>
void FileRepository<T>::loadFromFile() {
//    std::ifstream f(this->fileName);
//    T entity;
//    while (f >> entity) {
//        this->addExpense(entity);
//    }
//    f.close();

    //are un constructor de load from string
    string line;
    ifstream f(this->fileName);
    while (std::getline(f, line)) {
        try{
            T ob(line, ',');
            this->addExpense(ob);
        }
        catch(exception ex){
            // cout << "some pb: " + ex.what() << endl;
            throw ex;
        }
    }
    f.close();
}

template<typename T>
void FileRepository<T>::saveToFile() {
    std::ofstream g(this->fileName);
    for (T entity : this->getAll())
        g << entity <<"\n";
    g.close();
}

template<typename T>
FileRepository<T>::FileRepository(string fileName){
    this->fileName = fileName;
    this->loadFromFile();
}

template<typename T>
void FileRepository<T>::addExpense(T expense) {
    ConsoleRepository<T>::addExpense(expense);
    this->saveToFile();
}

template<typename T>
void FileRepository<T>::deleteExpense(int position) {
    ConsoleRepository<T>::deleteExpense(position);
    this->saveToFile();
}

template<typename T>
vector<T> FileRepository<T>::getAll() {
    return ConsoleRepository<T>::getAll();
}

template<typename T>
T &FileRepository<T>::getByPosition(int position) {
    return ConsoleRepository<T>::getByPosition(position);
}


#endif //FAMILIE_FILEREPOSITORY_H

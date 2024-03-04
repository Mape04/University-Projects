//
// Created by mazil on 6/26/2023.
//

#ifndef FAMILIE_EXCEPTIONS_H
#define FAMILIE_EXCEPTIONS_H

#include <exception>
#include <stdexcept>

class InvalidDayException : public std::invalid_argument {
public:
    //exception for invalid day
    InvalidDayException(const std::string &message)
            : std::invalid_argument(message) {
    }
};

class InvalidTypeException : public std::invalid_argument {
public:
    //exception for invalid type
    InvalidTypeException(const std::string &message)
            : std::invalid_argument(message) {
    }
};

class InvalidSumException : public std::invalid_argument {
public:
    //exception for invalid sum
    InvalidSumException(const std::string &message)
            : std::invalid_argument(message) {
    }
};

class InvalidPositionException : public std::invalid_argument {
public:
    //exception for invalid position
    InvalidPositionException(const std::string &message)
            : std::invalid_argument(message) {

    }
};

class InvalidArgsException : public std::invalid_argument {
public:
    //exception for arguments
    InvalidArgsException(const std::string &message)
            : std::invalid_argument(message) {

    }
};

#endif //FAMILIE_EXCEPTIONS_H

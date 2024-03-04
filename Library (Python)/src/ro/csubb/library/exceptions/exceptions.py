class EntityError(Exception):
    pass

class IdError(EntityError):
    pass

class MissingIdError(IdError):
    pass


class DuplicateIdError(IdError):
    pass


class FormatIdError(IdError):
    pass


class SsnFormatError(EntityError):
    pass


class StringFormatError(EntityError):
    pass

class Validators:

    @staticmethod
    def validate_book(id_book, title, description, author):
        Validators.validate_id(id_book)
        Validators.validate_string(title)
        Validators.validate_string(description)
        Validators.validate_string(author)

    @staticmethod
    def validate_client(id_client, name, ssn):
        Validators.validate_id(id_client)
        Validators.validate_string(name)
        Validators.validate_ssn(ssn)

    @staticmethod
    def validate_rent(id_rent, id_client, id_book):
        Validators.validate_id(id_rent)
        Validators.validate_id(id_client)
        Validators.validate_id(id_book)

    @staticmethod
    def validate_id(id_entity):
        if not id_entity.isnumeric():
            raise FormatIdError("ID-ul trebuie sa fie un numar natural")

    @staticmethod
    def validate_string(string):
        if string == "":
            raise StringFormatError("Titlul, descrierea, autorul trebuie sa aiba macar un caracter")

    @staticmethod
    def validate_ssn(ssn):
        if not ssn.isnumeric():
            raise SsnFormatError("CNP-ul trebuie sa fie numar natural")

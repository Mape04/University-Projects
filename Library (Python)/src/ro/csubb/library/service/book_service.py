from ro.csubb.library.domain.book import Book
from ro.csubb.library.exceptions.exceptions import Validators
from ro.csubb.library.service.service import Service


class BookService(Service):
    @staticmethod
    def create_entity(id_book, title, description, author):
        Validators.validate_book(id_book, title, description, author)
        return Book(int(id_book), title, description, author)

    def load_from_file(self):
        lines = Service.file_get_lines(self)
        Service.file_delete_contents(self)
        for line in lines:
            line = line.split(" ")
            Service.add_entity(self, BookService.create_entity(line[0], line[1], line[2], line[3]))

from dataclasses import dataclass

from ro.csubb.library.domain.entity import Entity


@dataclass
class Rent(Entity):
    __id_client: int
    __id_book: int

    @property
    def id_client(self):
        return self.__id_client

    @id_client.setter
    def id_client(self, value):
        self.__id_client = value

    @property
    def id_book(self):
        return self.__id_book

    @id_book.setter
    def id_book(self, value):
        self.__id_book = value

    def __str__(self):
        return f"{self.id} {self.id_client} {self.id_book}"

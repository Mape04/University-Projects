from dataclasses import dataclass

from ro.csubb.library.domain.entity import Entity


@dataclass
class Book(Entity):
    __title: str
    __description: str
    __author: str

    @property
    def title(self):
        return self.__title

    @title.setter
    def title(self, title):
        self.__title = title

    @property
    def description(self):
        return self.__description

    @description.setter
    def description(self, description):
        self.__description = description

    @property
    def author(self):
        return self.__author

    @author.setter
    def author(self, author):
        self.__author = author

    def __str__(self):
        return f"{self.id} {self.title} {self.description} {self.author}"

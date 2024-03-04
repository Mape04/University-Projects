from dataclasses import dataclass

from ro.csubb.library.domain.entity import Entity


@dataclass
class Client(Entity):
    __name: str
    __ssn: int

    @property
    def name(self):
        return self.__name

    @name.setter
    def name(self, name):
        self.__name = name

    @property
    def ssn(self):
        return self.__ssn

    @ssn.setter
    def ssn(self, value):
        self.__ssn = value

    def __str__(self):
        return f"{self.id} {self.name} {self.ssn}"

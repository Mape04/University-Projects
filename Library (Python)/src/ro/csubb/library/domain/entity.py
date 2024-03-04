from dataclasses import dataclass


@dataclass
class Entity:
    __id: int

    @property
    def id(self):
        return self.__id

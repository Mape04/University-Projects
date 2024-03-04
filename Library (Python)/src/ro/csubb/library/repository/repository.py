from ro.csubb.library.exceptions.exceptions import DuplicateIdError


class Repository:
    def __init__(self):
        self.__entities = {}

    def get_all(self):
        return self.__entities.values()

    def get_entities(self):
        return self.__entities

    def get_by_id(self, id_entity):
        if id_entity in self.__entities:
            return self.__entities[id_entity]
        else:
            return None

    def add_entity(self, entity):
        if self.get_by_id(entity.id) is None:
            self.__entities[entity.id] = entity
        else:
            raise DuplicateIdError("ID deja folosit!")

    def update_entity(self, entity):
        if self.get_by_id(entity.id) is not None:
            self.__entities[entity.id] = entity
        else:
            return None

    def delete_entity(self, id_entity):
        if self.get_by_id(id_entity) is not None:
            self.__entities.pop(id_entity)
        else:
            return None

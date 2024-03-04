from ro.csubb.library.exceptions.exceptions import Validators


class Service:
    def __init__(self, entity_repository):
        self.__entity_repository = entity_repository

    def add_entity(self, entity):
        self.__entity_repository.add_entity(entity)

    def update_entity(self, entity):
        self.__entity_repository.update_entity(entity)

    def delete_entity(self, id_entity):
        Validators.validate_id(id_entity)
        self.__entity_repository.delete_entity(int(id_entity))

    def get_all(self):
        return self.__entity_repository.get_all()

    def get_by_id(self, id_entity):
        Validators.validate_id(id_entity)
        return self.__entity_repository.get_by_id(int(id_entity))

    def file_get_lines(self):
        return self.__entity_repository.file_read()

    def file_delete_contents(self):
        self.__entity_repository.file_delete()

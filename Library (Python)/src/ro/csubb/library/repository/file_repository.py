from ro.csubb.library.exceptions.exceptions import DuplicateIdError
from ro.csubb.library.repository.repository import Repository


class FileRepository(Repository):
    def __init__(self, path):
        super().__init__()
        self.__entities = Repository.get_entities(self)
        self.__path = path

    def file_read(self):
        with open(f"repository/text_files/{self.__path}", "r") as file:
            file_list = file.readlines()
            for line, e in enumerate(file_list):
                file_list[line] = e.strip()
            return file_list

    def file_append_entity(self, entity):
        with open(f"repository/text_files/{self.__path}", "a") as file:
            file.write(str(entity))
            file.write('\n')

    def get_file_line_by_id(self, id_entity):
        with open(f'repository/text_files/{self.__path}', 'r') as file:
            file_list = file.readlines()
            for line in file_list:
                line = line.strip()
                line.split(" ")
                if int(line[0]) == id_entity:
                    return line
        return None

    def file_delete_entity(self, id_entity):
        poz = self.get_file_line_by_id(id_entity)
        with open(f'repository/text_files/{self.__path}', 'r') as file:
            file_list = file.readlines()
        with open(f'repository/text_files/{self.__path}', 'w') as file:
            for line, entity in enumerate(file_list):
                if line != poz:
                    file.write(str(entity))
                    file.write('\n')

    def file_delete(self):
        with open(f'repository/text_files/{self.__path}', 'w') as file:
            file.truncate(0)

    def add_entity(self, entity):
        if self.get_by_id(entity.id) is None:
            self.file_append_entity(entity)
            self.__entities[entity.id] = entity
        else:
            raise DuplicateIdError("ID deja folosit!")

    def update_entity(self, entity):
        if self.get_by_id(entity.id) is not None:
            self.__entities[entity.id] = entity

    def delete_entity(self, id_entity):
        if self.get_by_id(id_entity) is not None:
            self.file_delete_entity(id_entity)
            self.__entities.pop(id_entity)

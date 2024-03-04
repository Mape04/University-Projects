from ro.csubb.library.domain.client import Client
from ro.csubb.library.exceptions.exceptions import Validators
from ro.csubb.library.service.service import Service


class ClientService(Service):
    @staticmethod
    def create_entity(id_client, name, ssn):
        Validators.validate_client(id_client, name, ssn)
        return Client(int(id_client), name, int(ssn))

    def load_from_file(self):
        lines = Service.file_get_lines(self)
        Service.file_delete_contents(self)
        for line in lines:
            line = line.split()
            Service.add_entity(self, ClientService.create_entity(line[0], line[1], line[2]))
